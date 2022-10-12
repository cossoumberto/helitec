package helitec.contabilita.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import helitec.contabilita.model.Cantiere;
import helitec.contabilita.model.Fattura;

public class HelitecDAO {
	
	public List<Fattura> listFatture() {
		String sql = "SELECT * FROM fattura";
		List<Fattura> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(new Fattura(res.getString("numero"), res.getDate("data").toLocalDate(), res.getString("fornitore"),
						res.getDouble("importo_no_iva"), res.getInt("iva"), res.getDouble("importoTot"),
						res.getDouble("ImportoPagato"), res.getString("note")));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<Cantiere> listCantieri(){
		String sql = "SELECT * FROM cantiere";
		List<Cantiere> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(new Cantiere(res.getInt("numero"), res.getString("denominazione"), res.getString("indirizzo"),
						res.getString("comune"), res.getDouble("preventivo"), res.getDouble("importoTot")));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
