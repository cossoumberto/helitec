package helitec.contabilita.dao;

import java.util.List;

import helitec.contabilita.model.Cantiere;
import helitec.contabilita.model.Fattura;

public class TestDAO {

	public static void main(String[] args) {
		HelitecDAO dao = new HelitecDAO ();
		List<Fattura> l = dao.listFatture();
		List<Cantiere> l2 = dao.listCantieri();
		for(Fattura f : l)
			System.out.println(f);
		for(Cantiere c : l2)
			System.out.println(c);
	}

}
