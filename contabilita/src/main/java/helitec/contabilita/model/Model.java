package helitec.contabilita.model;

import java.math.BigDecimal;

import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import helitec.contabilita.FXMLController.InputType;
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
		//
		this.aggiornaImportiCantieri(this.cantieri);
		dao.aggiornaImportiCantiere(cantieri);
		this.aggiornaImportiVociCapitolatoCantiere(vociCapitolatoCantiere);
		dao.aggiornaImportiVociCapitolato(vociCapitolatoCantiere);
		for(Lavorazione l : this.lavorazioni)
			l.aggiornaImportoTotLavorazione();
		dao.aggiornaImportiLavorazioni(lavorazioni);
		
	}
	
	public List<Cantiere> getCantieri() {
		Collections.sort(this.cantieri);
		return this.cantieri;
	}
	
	public List<Fattura> getFatture() {
		Collections.sort(this.fatture);
		return fatture;
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
	
	public List<Cantiere> getCantieriAttivi(){
		List<Cantiere> list = new ArrayList<>();
		for(Lavorazione l : this.lavorazioni) {
			if(!list.contains(l.getCantiere()))
				list.add(l.getCantiere());
		}
		boolean b = false;
		if(list.contains(null)) {
			list.remove(null);
			b = true;
		}
		Collections.sort(list);
		if(b==true)
			list.add(0, null);
		return list;
	}
	
	public List<String> getDescrizioniLavorazioni(){
		List<String> list = new ArrayList<>();
		for(Lavorazione l : this.lavorazioni)
			if(!list.contains(l.getDescrizione()))
				list.add(l.getDescrizione());
		boolean b = false;
		if(list.contains(null)) {
			list.remove(null);
			b = true;
		}
		Collections.sort(list);
		if(b==true)
			list.add(0, null);
		return list;
	}
	
	public List<String> getVociCapitolatoAttive(){
		List<String> list = new ArrayList<>();
		for(Lavorazione l : this.lavorazioni)
			if(!list.contains(l.getVoceCapitolato()))
				list.add(l.getVoceCapitolato());
		boolean b = false;
		if(list.contains(null)) {
			list.remove(null);
			b = true;
		}
		Collections.sort(list);
		if(b==true)
			list.add(0, null);
		return list;
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
		List<VoceCapitolatoCantiere> vccAgg = new ArrayList<>();
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
					lavMod.add(lavorazioni.get(lavorazioni.indexOf(l)));
				}
			}
		}
		vccAgg.addAll(nuoveVoci);
		vccAgg.addAll(vociMod);
		this.aggiornaImportiVociCapitolatoCantiere(vccAgg);
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
		this.aggiornaImportiCantieri(cTemp);
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
	
	public void aggiornaFattura(Fattura oldF, Fattura newF) {
		List<Cantiere> modC = new ArrayList<>();
		List<Lavorazione> oldL = new ArrayList<>();
		List<Lavorazione> modL = new ArrayList<>();
		//List<Lavorazione> newL = new ArrayList<>();
		List<VoceCapitolatoCantiere> oldVcc = new ArrayList<>();
		List<VoceCapitolatoCantiere> modVcc = new ArrayList<>();
		List<VoceCapitolatoCantiere> newVcc = new ArrayList<>();
		//Scorro vecchia fattura
		for(Importo i : oldF.getImporti()) {
			//elimino importi vecchia fattura dalle lavorazioni
			i.getLavorazione().eliminaImporto(i);
			//memorizzo le lavorazioni modificate
			if(!modL.contains(i.getLavorazione()))
				modL.add(i.getLavorazione());
			//elimino lav se, in seguito all'eliminazione dell'importo, tale fattura non ha più importi
			if(i.getLavorazione().getImporti().size()==0) {
				this.lavorazioni.remove(i.getLavorazione());
				//rimuovo la lav senza importi dalle lavorazioni modificate
				if(modL.contains(i.getLavorazione()))
					modL.remove(i.getLavorazione());
				//aggiungo la lav senza importi nelle lavorazioni da eliminare
				if(!oldL.contains(i.getLavorazione()))
					oldL.add(i.getLavorazione());
			}
			//memorizzo i cantieri dove è avvenuta la variazione
			if(i.getLavorazione().getCantiere()!=null)
				modC.add(i.getLavorazione().getCantiere());
			//memorizzo le voci di capitolato cantiere modificate, settando il loro costo = 0
			if(i.getLavorazione().getVoceCapitolatoCantiere()!=null 
					&& this.vociCapitolatoCantiere.contains(i.getLavorazione().getVoceCapitolatoCantiere())
					&& !modVcc.contains(i.getLavorazione().getVoceCapitolatoCantiere())) {
				i.getLavorazione().getVoceCapitolatoCantiere().setImportoPagato(null);
				modVcc.add(i.getLavorazione().getVoceCapitolatoCantiere());
			}
		}
		//scorro nuova fattura		// da verificare eliminazione/memorizzazione nuove lavorazioni su dao
		for(Importo i : newF.getImporti()) {
			//se l'importo presente sulla nuova fattura è riferito a una lavorazione già esistente:
			//	//memorizzo la lavorazione già esistente nell'importo, aggiungo l'importo della fattura alla lavorazione
			//	//memorizzo la lavorazione modificata a parte
			if(this.lavorazioni.contains(i.getLavorazione())) {
				i.setLavorazione(this.lavorazioni.get(this.lavorazioni.indexOf(i.getLavorazione())));
				i.getLavorazione().addImporto(i);
			} else {
			//se l'importo è riferito a una lavorazione non esistente: aggiungo l'importo alla lavorazione,
			//	//salvo la nuova fattura nel programma e la memorizzo a parte
				i.getLavorazione().addImporto(i);
				this.lavorazioni.add(i.getLavorazione());
				/*if(!newL.contains(i.getLavorazione()))
					newL.add(i.getLavorazione());*/
			}
			if(!modL.contains(i.getLavorazione()))
				modL.add(i.getLavorazione());
			//memorizzo i cantieri dove sono cambiate le lavorazioni
			if(i.getLavorazione().getCantiere()!=null)
				modC.add(i.getLavorazione().getCantiere());
			//se esiste già una voce di capitolato cantiere relativa alla lavorazione:
			//	//la memorizzo nella lavorazione, ne setto il costo = 0 e memorizzo a parte la voce di capitolato modificata
			//viceversa: memorizzo la nuova voce di capitolato nella lavorazione e anche a parte
			if(i.getLavorazione().getVoceCapitolato()!=null) {
				VoceCapitolatoCantiere vcc =
					new VoceCapitolatoCantiere(i.getLavorazione().getVoceCapitolato(), i.getLavorazione().getCantiere(), null, null);
				if(this.vociCapitolatoCantiere.contains(vcc)) {
					i.getLavorazione().setVoceCapitolatoCantiere
						(this.vociCapitolatoCantiere.get(this.vociCapitolatoCantiere.indexOf(vcc)));
					i.getLavorazione().getVoceCapitolatoCantiere().setImportoPagato(null);
					if(!modVcc.contains(i.getLavorazione().getVoceCapitolatoCantiere())) {
						i.getLavorazione().getVoceCapitolatoCantiere().setImportoPagato(null);
						modVcc.add(i.getLavorazione().getVoceCapitolatoCantiere());
					}
				} else { 
					i.getLavorazione().setVoceCapitolatoCantiere(vcc);
					if(!newVcc.contains(i.getLavorazione().getVoceCapitolatoCantiere()))
						newVcc.add(i.getLavorazione().getVoceCapitolatoCantiere());
				}
			}
		}
		//ricalcolo gli importi dei cantieri modificati
		this.aggiornaImportiCantieri(modC);
		//rimuovo dal programma tutte le voci di capitolato modificate e ne ricalcolo gli importi totali
		this.vociCapitolatoCantiere.removeAll(modVcc);
		for(VoceCapitolatoCantiere vcc : modVcc) {
			for(Lavorazione l : this.lavorazioni)
				if(l.getVoceCapitolatoCantiere()!=null && l.getVoceCapitolatoCantiere().equals(vcc))
					vcc.sommaImporto(l.getImporto());
		}
		//memorizzo a parte eventuali voci che non presentano più lavorazioni e le rimuovo dall'elenco di voci modificate
		for(VoceCapitolatoCantiere vcc : modVcc)
			if(vcc.getImportoPagato()==null)
				oldVcc.add(vcc);
		modVcc.removeAll(oldVcc);
		//rimemorizzo nel programma le voci ancora esistenti modificate
		for(VoceCapitolatoCantiere vcc : modVcc) {
			if(!this.vociCapitolatoCantiere.contains(vcc))
				this.vociCapitolatoCantiere.add(vcc);
			else {
				this.vociCapitolato.remove(this.vociCapitolatoCantiere.indexOf(vcc));
				this.vociCapitolatoCantiere.add(vcc);
			}
		}
		//calcoli l'importo delle nuove voci e le memorizzo
		for(VoceCapitolatoCantiere vcc : newVcc) {
			for(Lavorazione l : this.lavorazioni)
				if(l.getVoceCapitolatoCantiere()!=null && l.getVoceCapitolatoCantiere().equals(vcc))
					vcc.sommaImporto(l.getImporto());
		}
		this.vociCapitolatoCantiere.addAll(newVcc);
		//rimuovo la vecchia fattura e aggiungo quella nuova
		if(this.fatture.contains(oldF)) {
			this.fatture.remove(oldF);
			this.fatture.add(newF);
		}
		//DAO AGGIORNAMENTO DB
		dao.aggiornaImportiCantiere(modC);
		dao.eliminaFattura(oldF);
		dao.eliminaImportiFattura(oldF);
		dao.aggiungiFattura(newF);
		dao.aggiungiImportiFattura(newF);
		dao.eliminaLavorazioni(oldL);
		dao.eliminaLavorazioni(modL);
		dao.aggiungiLavorazioni(modL);
		//dao.aggiungiLavorazioni(newL);
		dao.eliminaVociCapitolatoCantiere(oldVcc);
		dao.eliminaVociCapitolatoCantiere(modVcc);
		dao.aggiungiVociCapitolatoCantiere(modVcc);
		dao.aggiungiVociCapitolatoCantiere(newVcc);
	}
	

	private void aggiornaImportiCantieri(List<Cantiere> cTemp) {
		if(cTemp.size()>0) {
			for(Cantiere c : cTemp) {
				Double d = 0.0;
				for(Lavorazione l : this.lavorazioni) {
					if(c.equals(l.getCantiere())) {
						d += l.getImporto();
						BigDecimal x = new BigDecimal(d).setScale(2, RoundingMode.HALF_EVEN);
						d = x.doubleValue();
					}
				}
				if(d!=0.0)
					c.setImportoTotale(d);
				else
					c.setImportoTotale(null);
			}
		}
	}
	
	private void aggiornaImportiVociCapitolatoCantiere(List<VoceCapitolatoCantiere> listVcc) {
		for(VoceCapitolatoCantiere vcc : listVcc) {
			Double d = 0.0;
			for(Lavorazione l : this.lavorazioni) {
				if(vcc.equals(l.getVoceCapitolatoCantiere()))
					d += l.getImporto();
					BigDecimal x = new BigDecimal(d).setScale(2, RoundingMode.HALF_EVEN);
					d = x.doubleValue();
			}
			vcc.setImportoPagato(d);
		}
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

	public List<Fattura> getFattureRichieste(List<String> forn, List<Cantiere> cant, List<String> lav,
												List<String> voci, LocalDate dataDa, LocalDate dataA, Map<Integer, InputType> inputList) {
		List<Fattura> list = new ArrayList<>();
		List<Fattura> r = new ArrayList<>();
		int i = 0;
		if(inputList.size()>0) {
			if(inputList.get(i).equals(InputType.Fornitore)) {
				while(i<forn.size() && inputList.get(i).equals(InputType.Fornitore)) {
					for(Fattura f : fatture)
						if(!list.contains(f) && forn.get(i).equals(f.getFornitore()))
							list.add(f);
					i++;
				}
			} else if(inputList.get(i).equals(InputType.Cantiere)) {
				while(i<cant.size() && inputList.get(i).equals(InputType.Cantiere)) {
					for(Fattura f : fatture)
							if(!list.contains(f) && f.getCantieriFattura().contains(cant.get(i)))
								list.add(f);
					i++;
				}
			} else if(inputList.get(i).equals(InputType.Lavorazione)) {
				while(i<lav.size() && inputList.get(i).equals(InputType.Lavorazione)) {
					for(Fattura f : fatture)
							if(!list.contains(f) && f.getDescrLavorazioniFattura().contains(lav.get(i)))
								list.add(f);
					i++;
				}
			} else if(inputList.get(i).equals(InputType.VoceCap)) {
				while(i<voci.size() && inputList.get(i).equals(InputType.VoceCap)) {
					for(Fattura f : fatture)
						if(!list.contains(f) && f.getVociCapitolatoFattura().contains(voci.get(i)))
								list.add(f);
					i++;
				}
			} 
		} else {
			list.addAll(fatture);
		}
		for(Fattura f : list)
			if(!r.contains(f)) {
				if(inputList.size()>1) {
					if(!inputList.get(0).equals(InputType.Fornitore) && forn.size()>0 && !forn.contains(f.getFornitore()))
						r.add(f);
					if(!inputList.get(0).equals(InputType.Cantiere) && cant.size()>0) {
						int count = 0;
						for(Cantiere c : cant) 
							if(f.getCantieriFattura().contains(c))
								count++;
						if(count==0)
							r.add(f);
					}
					if(!inputList.get(0).equals(InputType.Lavorazione) && lav.size()>0) {
						int count = 0;
						for(String s : lav) 
							if(f.getDescrLavorazioniFattura().contains(s))
								count++;
						if(count==0)
							r.add(f);
					}
					if(!inputList.get(0).equals(InputType.VoceCap) && voci.size()>0) {
						int count = 0;
						for(String s : voci) 
							if(f.getVociCapitolatoFattura().contains(s))
								count++;
						if(count==0)
							r.add(f);
					}
				}
				if(dataDa!=null && f.getData().isBefore(dataDa))
					r.add(f);
				if(dataA!=null && f.getData().isAfter(dataA))
					r.add(f);
			}
		list.removeAll(r);
		Collections.sort(list);
		return list;
	}
	
	public List<String> getFornitoriFatture(List<Fattura> list){
		List<String> forn = new ArrayList<>();
		for(Fattura f : list)
			if(!forn.contains(f.getFornitore()))
				forn.add(f.getFornitore());
		Collections.sort(forn);
		return forn;
	}

	public List<Cantiere> getCantieriFatture(List<Fattura> list, List<String> lav, List<String> voci) {
		List<Cantiere> cant = new ArrayList<>();
		for(Fattura f : list)
			for(Importo i : f.getImporti())
				if(!cant.contains(i.getLavorazione().getCantiere()))
					if(lav.size()==0 || lav.contains(i.getLavorazione().getDescrizione()))
						if(voci.size()==0 || voci.contains(i.getLavorazione().getVoceCapitolato()))
							cant.add(i.getLavorazione().getCantiere());
		boolean b = false;
		if(cant.contains(null)) {
			cant.remove(null);
			b = true;
		}
		Collections.sort(cant);
		if(b==true)
			cant.add(0, null);
		return cant;
	}
	
	public List<String> getDescrLavorazioniFatture(List<Fattura> list, List<Cantiere> cant, List<String> voci) {
		List<String> lav = new ArrayList<>();
		for(Fattura f : list)
			for(Importo i : f.getImporti())
				if(!lav.contains(i.getLavorazione().getDescrizione()))
					if(cant.size()==0 || cant.contains(i.getLavorazione().getCantiere()))
						if(voci.size()==0 || voci.contains(i.getLavorazione().getVoceCapitolato()))
							lav.add(i.getLavorazione().getDescrizione());
		boolean b = false;
		if(lav.contains(null)) {
			lav.remove(null);
			b = true;
		}
		Collections.sort(lav);
		if(b==true)
			lav.add(0, null);
		return lav;
	}
	
	public List<String> getVociFatture(List<Fattura> list, List<Cantiere> cant, List<String> lav) {
		List<String> voci = new ArrayList<>();
		for(Fattura f : list)
			for(Importo i : f.getImporti())
				if(!voci.contains(i.getLavorazione().getVoceCapitolato()))
					if(cant.size()==0 || cant.contains(i.getLavorazione().getCantiere()))
						if(lav.size()==0 || lav.contains(i.getLavorazione().getDescrizione()))
							voci.add(i.getLavorazione().getVoceCapitolato());
		boolean b = false;
		if(voci.contains(null)) {
			voci.remove(null);
			b = true;
		}
		Collections.sort(voci);
		if(b==true)
			voci.add(0, null);
		return voci;
	}
	
	public LocalDate getDataMinFatture (List<Fattura> list) {
		LocalDate ld = null;
		for(Fattura f : list)
			if(ld==null || f.getData().isBefore(ld))
				ld = f.getData();
		return ld;
	}
	
	public LocalDate getDataMaxFatture (List<Fattura> list) {
		LocalDate ld = null;
		for(Fattura f : list)
			if(ld==null || f.getData().isAfter(ld))
				ld = f.getData();
		return ld;
	}

}
