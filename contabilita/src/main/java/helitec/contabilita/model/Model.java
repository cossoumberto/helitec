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
					lavMod.add(l);
				}
			}
		}
		vccAgg.addAll(nuoveVoci);
		vccAgg.addAll(vociMod);
		this.aggiornaImportiVociCapitolatoCantierer(vccAgg);
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
	
	

	private void aggiornaImportiCantieri(List<Cantiere> cTemp) {
		for(Cantiere c : cTemp) {
			Double d = 0.0;
			for(Lavorazione l : this.lavorazioni) {
				if(c.equals(l.getCantiere())) {
					d += l.getImporto();
					BigDecimal x = new BigDecimal(d).setScale(2, RoundingMode.HALF_EVEN);
					d = x.doubleValue();
				}
			}
			c.setImportoTotale(d);
		}
	}
	
	private void aggiornaImportiVociCapitolatoCantierer(List<VoceCapitolatoCantiere> listVcc) {
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
		if(inputList.size()==0)
			return fatture;
		List<Fattura> list = new ArrayList<>();
		List<Fattura> r = new ArrayList<>();
		int i = 0;
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
		for(Fattura f : list)
			if(!r.contains(f)) {
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
			/*	if(!retain.contains(f) && 
						( (dataA==null || f.getData().isBefore(dataA)) && (dataDa==null || f.getData().isAfter(dataDa)) ) )
					retain.add(f);*/
			}
		list.removeAll(r);
		Collections.sort(list);
		return list;
	}
		
	//da valutare se tenere	
	/*public List<Fattura> getFattureRichieste(String forn, Cantiere cant, String lav, String voce,
			LocalDate dataDa, LocalDate dataA) {
		List<Fattura> list = new ArrayList<>();
		List<Fattura> remove = new ArrayList<>();
		for(Fattura f : fatture)
			for(Importo i : f.getImporti()) {
				if(cant==null || cant.equals(i.getLavorazione().getCantiere())) {
					list.add(f);
					if(lav!=null && !lav.equals(i.getLavorazione().getDescrizione())) 
						remove.add(f);
					else if(voce!=null && !voce.equals(i.getLavorazione().getVoceCapitolato()))
						remove.add(f);
					else if(forn!=null && !forn.equals(f.getFornitore()))
						remove.add(f);
					else if( (dataA!=null && f.getData().isAfter(dataA)) || (dataDa!=null && f.getData().isBefore(dataDa)) )
						remove.add(f);
					break;
				} 
			}
		list.removeAll(remove);
		Collections.sort(list);
		System.out.println(fatture.size() + " " + list.size());
		return list;
	}
	
	public List<String> getFornitoriRichiesti(String s, Cantiere cant, String lav, String voce, LocalDate dataDa, LocalDate dataA) {
		List<String> list = new ArrayList<>();
		List<String> remove = new ArrayList<>();
		for(Fattura f : this.fatture) {
			for(Importo i : f.getImporti()) {
				if((s==null || f.getFornitore().contains(s)) && (cant==null || i.getLavorazione().getCantiere().equals(cant)) )
					if(!list.contains(f.getFornitore())) {
						list.add(f.getFornitore());
						if(lav!=null && !lav.equals(i.getLavorazione().getDescrizione()))
							if(!remove.contains(f.getFornitore()))
								remove.add(f.getFornitore());
						else if(voce!=null && !voce.equals(i.getLavorazione().getVoceCapitolato()))
							if(!remove.contains(f.getFornitore()))
								remove.add(f.getFornitore());
						else if( (dataDa!=null && f.getData().isBefore(dataDa)) || 
								(dataA!=null && f.getData().isAfter(dataA)) )
							if(!remove.contains(f.getFornitore()))
								remove.add(f.getFornitore());
						break;
					}
			}
		}
		list.removeAll(remove);
		Collections.sort(list);
		return list;
	}
	
	public List<Cantiere> getCantieriRichiesti(String s, String forn, String lav, String voce, LocalDate dataDa, LocalDate dataA) {
		List<Cantiere> list = new ArrayList<>();
		List<Cantiere> remove = new ArrayList<>();
		for(Fattura f : this.fatture) {
			for(Importo i : f.getImporti()) {
				if( (s==null || i.getLavorazione().getCantiere().toString().contains(s)) && (forn==null || f.getFornitore().equals(forn)))
					if(!list.contains(i.getLavorazione().getCantiere())) {
						list.add(i.getLavorazione().getCantiere());
						if(lav!=null && !lav.equals(i.getLavorazione().getDescrizione()))
							if(!remove.contains(i.getLavorazione().getCantiere()))
								remove.add(i.getLavorazione().getCantiere());
						else if(voce!=null && !voce.equals(i.getLavorazione().getVoceCapitolato()))
							if(!remove.contains(i.getLavorazione().getCantiere()))
								remove.add(i.getLavorazione().getCantiere());
						else if( (dataDa!=null && f.getData().isBefore(dataDa)) || 
								(dataA!=null && f.getData().isAfter(dataA)) )
							if(!remove.contains(i.getLavorazione().getCantiere()))
								remove.add(i.getLavorazione().getCantiere());
						break;
					}
			}
		}
		list.removeAll(remove);
		Collections.sort(list);
		return list;
	}
	
	public List<String> getLavorazioniRichieste(String s, String forn, Cantiere cant, String voce, LocalDate dataDa, LocalDate dataA) {
		List<String> list = new ArrayList<>();
		List<String> remove = new ArrayList<>();
		for(Fattura f : this.fatture) {
			for(Importo i : f.getImporti()) {
				if( (s==null || i.getLavorazione().getDescrizione().contains(s)) && (forn==null ||f.getFornitore().equals(forn)) )
					if(!list.contains(i.getLavorazione().getDescrizione())) {
						list.add(i.getLavorazione().getDescrizione());
						if(cant!=null && !cant.equals(i.getLavorazione().getCantiere()))
							if(!remove.contains(i.getLavorazione().getDescrizione()))
								remove.add(i.getLavorazione().getDescrizione());
						else if(voce!=null && !voce.equals(i.getLavorazione().getVoceCapitolato()))
							if(!remove.contains(i.getLavorazione().getDescrizione()))
								remove.add(i.getLavorazione().getDescrizione());
						else if( (dataDa!=null && f.getData().isBefore(dataDa)) || 
								(dataA!=null && f.getData().isAfter(dataA)) )
							if(!remove.contains(i.getLavorazione().getDescrizione()))
								remove.add(i.getLavorazione().getDescrizione());
						break;
					}
			}
		}
		list.removeAll(remove);
		Collections.sort(list);
		return list;
	}
	
	public List<String> getVociRichieste(String s, String forn, Cantiere cant, String lav, LocalDate dataDa, LocalDate dataA) {
		List<String> list = new ArrayList<>();
		List<String> remove = new ArrayList<>();
		for(Fattura f : this.fatture) {
			for(Importo i : f.getImporti()) {
				if( (s==null || i.getLavorazione().getVoceCapitolato().contains(s)) && (forn==null ||f.getFornitore().equals(forn)) )
					if(!list.contains(i.getLavorazione().getVoceCapitolato())) {
						list.add(i.getLavorazione().getVoceCapitolato());
						if(cant!=null && !cant.equals(i.getLavorazione().getCantiere()))
							if(!remove.contains(i.getLavorazione().getVoceCapitolato()))
								remove.add(i.getLavorazione().getVoceCapitolato());
						else if(lav!=null && !lav.equals(i.getLavorazione().getDescrizione()))
							if(!remove.contains(i.getLavorazione().getVoceCapitolato()))
								remove.add(i.getLavorazione().getVoceCapitolato());
						else if( (dataDa!=null && f.getData().isBefore(dataDa)) || 
								(dataA!=null && f.getData().isAfter(dataA)) )
							if(!remove.contains(i.getLavorazione().getVoceCapitolato()))
								remove.add(i.getLavorazione().getVoceCapitolato());
						break;
					}
			}
		}
		list.removeAll(remove);
		Collections.sort(list);
		return list;
	}*/
	
	public List<String> getFornitoriFatture(List<Fattura> list){
		List<String> forn = new ArrayList<>();
		for(Fattura f : list)
			if(!forn.contains(f.getFornitore()))
				forn.add(f.getFornitore());
		Collections.sort(forn);
		return forn;
	}
	
	/*public List<Cantiere> getCantieriFatture(List<Fattura> list){
		List<Cantiere> cant = new ArrayList<>();
		for(Fattura f : list)
			for(Importo i : f.getImporti())
				if(!cant.contains(i.getLavorazione().getCantiere()))
					cant.add(i.getLavorazione().getCantiere());
		if(cant.contains(null))
			cant.remove(null);
		Collections.sort(cant);
		cant.add(0, null);
		return cant;
	}*/
	
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
	
	/*
	public List<String> getDescrLavorazioniFatture(List<Fattura> list){
		List<String> lav = new ArrayList<>();
		for(Fattura f : list)
			for(Importo i : f.getImporti())
				if(!lav.contains(i.getLavorazione().getDescrizione()))
					lav.add(i.getLavorazione().getDescrizione());
		if(lav.contains(null))
			lav.remove(null);
		Collections.sort(lav);
		lav.add(0, null);
		return lav;
	}*/
	
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
	/*
	public List<String> getVociFatture(List<Fattura> list){
		List<String> voci = new ArrayList<>();
		for(Fattura f : list)
			for(Importo i : f.getImporti())
				if(!voci.contains(i.getLavorazione().getVoceCapitolato()))
					voci.add(i.getLavorazione().getVoceCapitolato());
		if(voci.contains(null))
			voci.remove(null);
		Collections.sort(voci);
		voci.add(0, null);
		return voci;
	}*/
	
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
