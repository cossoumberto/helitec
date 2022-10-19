package helitec.contabilita.model;

import java.util.ArrayList;
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
	
	public Model() {
		this.dao = new HelitecDAO();
		this.cantieri = dao.listCantieri();
		this.fatture = dao.listFatture();
		this.vociCapitolatoCantiere = dao.listVociCapitolatoCantiere(cantieri);
		this.lavorazioni = dao.listLavorazioni(cantieri, vociCapitolatoCantiere);
		this.vociCapitolato = dao.listVociCapitolato();
		this.fornitori = dao.listFornitori();
	}	
	
	public List<Cantiere> getCantieri() {
		return this.cantieri;
	}
	public List<String> getVociCapitolato(){
		return this.vociCapitolato;
	}

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
	
}
