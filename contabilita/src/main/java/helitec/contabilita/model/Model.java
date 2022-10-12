package helitec.contabilita.model;

import java.util.List;

import helitec.contabilita.dao.HelitecDAO;

public class Model {
	
	private HelitecDAO dao = null;
	private List<Cantiere> cantieri = null;
	private List<Fattura> fatture = null;
	//private List<>
	
	public Model() {
		this.dao = new HelitecDAO();
		this.cantieri = dao.listCantieri();
		this.fatture = dao.listFatture();
	}	
	
	public List<Cantiere> getCantieri() {
		return this.cantieri;
	}
	
}
