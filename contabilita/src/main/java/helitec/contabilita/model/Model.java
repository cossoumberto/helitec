package helitec.contabilita.model;

import java.util.ArrayList;
import java.util.List;

import helitec.contabilita.dao.HelitecDAO;

public class Model {
	
	private HelitecDAO dao = null;
	private List<Cantiere> cantieri = null;
	private List<Fattura> fatture = null;
	private List<Lavorazione> lavorazioni = null;
	
	public Model() {
		this.dao = new HelitecDAO();
		this.cantieri = dao.listCantieri();
		this.fatture = dao.listFatture();
	}	
	
	public List<Cantiere> getCantieri() {
		return this.cantieri;
	}

	public void elaboraFattura(Fattura f, List<Lavorazione> ll) {
		//da modificare in moda da passare al dao la fattura, lista di lavorazioni nuove, lista di lavotazioni da modificare
		List<Lavorazione> nuoveLav = new ArrayList<>();
		List<Integer> indiciNuoveLav = new ArrayList<>();
		if(ll.size()>0) {
			for(Lavorazione l : ll) {
				if(!lavorazioni.contains(l)) {
					lavorazioni.add(l);
				}
			}
			for(Importo i : f.getImporti()) {
				for(Lavorazione l : lavorazioni) {
					if(i.getLavorazione().equals(l)) {
						l.addImporto(i);
						indiciNuoveLav.add(lavorazioni.indexOf(l));
					}
				}
			}
			for(Integer i : indiciNuoveLav)
				nuoveLav.add(lavorazioni.get(i));
		}
		fatture.add(f);
		dao.aggiungiFatturaLavorazioni(f, nuoveLav);
	}

	public boolean verificaIdFattura(Fattura f) {
		if(fatture.contains(f))
			return true;
		else return false;
	}
	
}
