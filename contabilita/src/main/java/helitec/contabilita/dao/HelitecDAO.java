package helitec.contabilita.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import helitec.contabilita.model.Cantiere;
import helitec.contabilita.model.Fattura;
import helitec.contabilita.model.Importo;
import helitec.contabilita.model.Lavorazione;

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
			e.printStackTrace();
			return null;
		}
	}

	public void aggiungiFatturaLavorazioni(Fattura f, List<Lavorazione> nuoveLav) {
		this.aggiungiFattura(f);
		this.aggiungiImportiFattura(f);
		// da modificare in  modo da e direttamente le liste di lavorazioni nuove e da modificare
	}


	private void aggiungiFattura(Fattura f) {
		String sql = "INSERT INTO fatture VALUES (?,?,?,?,?,?,?,?)";
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, f.getNumero());
			st.setDate(2, Date.valueOf(f.getData()));
			st.setString(3, f.getFornitore());
			st.setDouble(4, f.getImportoNoIva());
			st.setInt(5, f.getIva());
			st.setDouble(6, f.getImportoTot());
			st.setDouble(7, f.getImportoPagato());
			st.setString(8, f.getNote());
			st.executeQuery() ;
			conn.close();
	} catch (SQLException e) {
		e.printStackTrace();
		System.out.println("Errore connessione al database");
		throw new RuntimeException("Error Connection Database");
		}
	}
	
	private void aggiungiImportiFattura(Fattura f) {
		String sql = "INSERT INTO importo_lavorazione VALUES (?,?,?,?,?,?,?,?)";
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			for(Importo i : f.getImporti()) {
				st.setInt(1, i.getNumero());
				st.setString(2, f.getNumero());
				st.setDate(3, Date.valueOf(f.getData()));
				st.setString(4, f.getFornitore());
				st.setInt(5, i.getLavorazione().getCantiere().getNumero());
				st.setString(6, i.getLavorazione().getDescrizione());
				st.setDouble(7, i.getImporto());
				st.setDouble(8, i.getImportoIva());
				st.setString(9,i.getNote());
			}
			st.executeQuery() ;
			conn.close();
	} catch (SQLException e) {
		e.printStackTrace();
		System.out.println("Errore connessione al database");
		throw new RuntimeException("Error Connection Database");
		}
		
	}
	
}
