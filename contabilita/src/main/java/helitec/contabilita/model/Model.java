package helitec.contabilita.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import helitec.contabilita.dao.HelitecDAO;
import helitec.contabilita.model.PagamentoFattura.Intero;

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
		for(Fattura f : fatture)
			System.out.println(f.toString());
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
	
	public List<Fattura> getFattureFornitoreDaPag(String fornitore) {
		List<Fattura> l = new ArrayList<>();
		for(Fattura f : this.fatture)
			if(f.getFornitore().equals(fornitore))
				if(f.getImportoPagato()==null || f.getImportoPagato()<f.getImportoTot())
					l.add(f);
		Collections.sort(l);
		return l;
	}
	
	
	//AGGIORNARE CON UPDATE PAGAMENTI
	public void elaboraFattura(Fattura f, List<Lavorazione> ll) {
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
	}

	public boolean verificaIdFattura(Fattura f) {
		if(fatture.contains(f))
			return true;
		else return false;
	}

	//COMPLETARE+FUNZIONI DAO PER AGG. DB
	public void elaboraPagamento(Pagamento p) {
		Integer importiSconosciuti = 0;
		for(PagamentoFattura pf : p.getFatture()) {
			//caso 1: importo relativo non inserito, ma fattura interamente pagata
			if(this.fatture.contains(pf.getFattura()) && pf.getImportoRelativo()==null && pf.getIntero().equals(Intero.INTERO))
				pf.setImportoRelativo(this.fatture.get(this.fatture.indexOf(pf.getFattura())).getImportoTot());
			//caso 2: inserito pagamento per fattura di importo sconosciuto
			if(pf.getImportoRelativo()==null)
				importiSconosciuti++;
		}
		for(PagamentoFattura pf : p.getFatture()) {
			//caso 2: inserito pagamento per fattura di importo sconosciuto
			if(importiSconosciuti==1 && pf.getImportoRelativo()==null) {
				Double d = p.getImporto()-p.getSommaImportiRelativi();
				BigDecimal x = new BigDecimal(d).setScale(2, RoundingMode.HALF_EVEN);
				d = x.doubleValue();
				pf.setImportoRelativo(d);
			}
			//aggiornamento importo relativo fattura
			if(this.fatture.contains(pf.getFattura()) && pf.getImportoRelativo()!=null) {
				if(this.fatture.get(this.fatture.indexOf(pf.getFattura())).getImportoPagato()==null)
					this.fatture.get(this.fatture.indexOf(pf.getFattura())).setImportoPagato(0.0);
				Double d = this.fatture.get(this.fatture.indexOf(pf.getFattura())).getImportoPagato() + pf.getImportoRelativo();
				BigDecimal x = new BigDecimal(d).setScale(2, RoundingMode.HALF_EVEN);
				d = x.doubleValue();
				this.fatture.get(this.fatture.indexOf(pf.getFattura())).setImportoPagato(d);	
			}
		}
		this.pagamenti.add(p);
		dao.aggiungiPagamento(p);
		dao.aggiorniPagamentiFattura(p);
		//aggiornamento importa pagato fattura su db da aggiornare
	}
	
}
