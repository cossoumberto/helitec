package helitec.contabilita.model;

import java.math.BigDecimal;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import helitec.contabilita.dao.HelitecDAO;

public class Model {
	
	private HelitecDAO dao = null;
	private List<Cantiere> cantieri = null;
	private List<Fattura> fatture = null;
	private List<Lavorazione> lavorazioni = null;
	private List<VoceCapitolatoCantiere> vociCapitolatoCantiere = null;
	private List<String> vociCapitolato = null;
	private List<String> fornitori = null;
	private List<Pagamento> pagamenti = null;
	
	public Model() {
		this.dao = new HelitecDAO();
		this.cantieri = dao.listCantieri();
		this.fatture = dao.listFatture();
		this.vociCapitolatoCantiere = dao.listVociCapitolatoCantiere(cantieri);
		this.lavorazioni = dao.listLavorazioni(cantieri, vociCapitolatoCantiere);
		dao.setImporti(fatture, lavorazioni);
		this.vociCapitolato = dao.listVociCapitolato();
		this.fornitori = dao.listFornitori();
		this.pagamenti = dao.listPagamenti();
		dao.setPagamentiFattura(pagamenti, fatture);
	}
	
	public List<Cantiere> getCantieri() {
		Collections.sort(this.cantieri);
		return this.cantieri;
	}
	
	public List<String> getVociCapitolato(){
		Collections.sort(this.vociCapitolato);
		return this.vociCapitolato;
	}
	
	public List<String> getFornitori(){
		Collections.sort(this.fornitori);
		return this.fornitori;
	}
	
	public List<Pagamento> getPagamenti(){
		return this.pagamenti;
	}
	
	public List<Fattura> getFattureFornitoreDaPag(String fornitore) {
		List<Fattura> l = new ArrayList<>();
		for(Fattura f : this.fatture)
			if(f.getFornitore().equals(fornitore))
				if(f.getImportoPagato()==null || f.getImportoPagato()<f.getImportoTot())
					l.add(f);
		Collections.sort(l);
		return l;
	}
	
	public Integer elaboraFattura(Fattura f, List<Lavorazione> ll) {
		List<Lavorazione> nuoveLav = new ArrayList<>();
		List<Lavorazione> lavMod = new ArrayList<>();
		List<VoceCapitolatoCantiere> nuoveVoci= new ArrayList<>();
		List<VoceCapitolatoCantiere> vociMod = new ArrayList<>();
		if(ll.size()>0) {
			VoceCapitolatoCantiere voceCapitolatoCantiere = null;
			for(Lavorazione l : ll) {
				if(l.getCantiere()!=null && l.getVoceCapitolato()!=null) {
					voceCapitolatoCantiere = new VoceCapitolatoCantiere(l.getVoceCapitolato(), l.getCantiere(), null, null);
					if(!this.vociCapitolatoCantiere.contains(voceCapitolatoCantiere)) {
						this.vociCapitolatoCantiere.add(voceCapitolatoCantiere);
						nuoveVoci.add(voceCapitolatoCantiere);
					} else
						vociMod.add(this.vociCapitolatoCantiere.get(this.vociCapitolatoCantiere.indexOf(voceCapitolatoCantiere)));
					l.setVoceCapitolatoCantiere(this.vociCapitolatoCantiere.get(this.vociCapitolatoCantiere.indexOf(voceCapitolatoCantiere)));
					this.vociCapitolatoCantiere.get(this.vociCapitolatoCantiere.indexOf(voceCapitolatoCantiere)).aggiungiLavorazione(l);
				}
				for(Importo i : f.getImporti())
					if(i.getLavorazione().equals(l))
						l.addImporto(i);
				if(!lavorazioni.contains(l)) {
					lavorazioni.add(l);
					nuoveLav.add(l);
				} else {
					for(Importo i : l.getImporti())
						lavorazioni.get(lavorazioni.indexOf(l)).addImporto(i);
					lavMod.add(l);
				}
			}
		}
		fatture.add(f);
		if(!this.fornitori.contains(f.getFornitore())) {
			fornitori.add(f.getFornitore());
			dao.aggiungiFornitore(f.getFornitore());
		}
		dao.aggiungiFattura(f);
		dao.aggiungiImportiFattura(f);
		if(nuoveLav.size()>0)
			dao.aggiungiLavorazioni(nuoveLav);
		if(lavMod.size()>0)
			dao.aggiornaImportiLavorazioni(lavMod);
		List<Cantiere> cTemp = new ArrayList<>();
		for(Lavorazione l : nuoveLav)
			if(l.getCantiere()!=null && !cTemp.contains(l.getCantiere()))
				cTemp.add(l.getCantiere());
		for(Lavorazione l : lavMod)
			if(l.getCantiere()!=null && !cTemp.contains(l.getCantiere()))
				cTemp.add(l.getCantiere());
		if(cTemp.size()>0)
			dao.aggiornaImportiCantiere(cTemp);
		if(nuoveVoci.size()>0)
			dao.aggiungiVociCapitolatoCantiere(nuoveVoci);
		if(vociMod.size()>0)
			dao.aggiornaImportiVociCapitolato(vociMod);
		//aggiornamento sezione pagamenti, se fattura già pagata						
			//se return 0 = errore inserimento importo e/o totalità pagamento
			//se return 1 = ok, fattura attribuita correttamente a pagamento esistente
			//se return 2 = impossibile attribuire importo pagato a fattura già esistente
				//in quanto non si conosce l'entità del pagamento relativo (acconto / saldo)
																			//se return -1 = errore date -> DA CAPIRE SE HA SENSO
			//se return null pagamento non ancora inserito
		Integer i = null;
		List<PagamentoFattura> pfMod = new ArrayList<>();
		for(Pagamento p : this.pagamenti)
			for(PagamentoFattura pf : p.getFatture())
				if(p.getFornitore().equals(f.getFornitore()) && f.getNumero().contains(pf.getFattura().getNumero()) 
						&& !this.fatture.contains(pf.getFattura())) { //MANCA DA GESTIRE LA POSSIBILITA' CHE ESISTANO 
					i = f.aggiornaImportoPagato(pf);					//PIU PAGAMENTI ANCORA DA ASSEGNARE,
					pf.setFattura(f);									//ALLO STESSO FORN CON FATTURE CON STESSO CODICE 
					pfMod.add(pf);
				}
		if(i!=null && i>0) {
			dao.aggiornaPagamentiFattura(pfMod);
			List<Fattura> l = new ArrayList<>();
			l.add(f);
			dao.aggiornaImportiRelativiFattura(l);
		}
		return i;
	}

	public boolean verificaIdFattura(Fattura f) {
		if(fatture.contains(f))
			return true;
		else return false;
	}

	public List<String> elaboraPagamento(Pagamento p) {
		//se return 0 = errore inserimento dati => incongruenza importo-totalità pagamento
		//se return 1 = ok, pagamento attribuito correttamente a fattura esistente
		//se return 2 = impossibile attribuire importo pagato a fattura già esistente
			//in quanto non si conosce l'entità del pagamento relativo (acconto / saldo)
		//se return null = inserito pagamento fattura sconosciuta
		Integer importiSconosciuti = 0;
		List<PagamentoFattura> ok = new ArrayList<>();
		List<String> stamp = new ArrayList<>();
		List<Fattura> fmod = new ArrayList<>();
		for(PagamentoFattura pf : p.getFatture())
			//inserito pagamento per fattura di importo sconosciuto
			if(pf.getImportoRelativo()==null)
				importiSconosciuti++;
		for(PagamentoFattura pf : p.getFatture()) {
			//inserito pagamento per fattura di importo sconosciuto
			if(importiSconosciuti==1 && pf.getImportoRelativo()==null) {
				Double d = p.getImporto()-p.getSommaImportiRelativi();
				BigDecimal x = new BigDecimal(d).setScale(2, RoundingMode.HALF_EVEN);
				d = x.doubleValue();
				pf.setImportoRelativo(d);
			}
			//aggiornamento importo relativo fattura
			if(this.fatture.contains(pf.getFattura())) {
				int i = pf.getFattura().aggiornaImportoPagato(pf);
				if(i==0)
					stamp.add("Errore: incongruenza tra importo fattura selezionata, importo pagamento e totalità pagamento");
				else if (i==1)
					stamp.add("Pagamento caricato correttamente - Attribuito a fattura esistente");
				else if(i==2)
					stamp.add("Pagamento caricato correttamnte - Impossibile attrubuire pagamento a fattura esistente: "
							+ "non si può conoscere l'entità del pagamento relativo");
				if(i==1 || i==2) {
					ok.add(pf);
					fmod.add(pf.getFattura());
				}
			} else {
				stamp.add("Pagamento caricato correttamente - Riferito a fattura sconosciuta");
				ok.add(pf);
			} 
		}
		p.setFatture(ok);
		this.pagamenti.add(p);
		dao.aggiungiPagamento(p);
		dao.aggiungiPagamentiFattura(p);
		dao.aggiornaImportiRelativiFattura(fmod);
		return stamp;
	}
	
}
