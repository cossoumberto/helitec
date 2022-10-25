package helitec.contabilita.dao;

import java.sql.Connection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import helitec.contabilita.model.Cantiere;
import helitec.contabilita.model.Fattura;
import helitec.contabilita.model.Importo;
import helitec.contabilita.model.Lavorazione;
import helitec.contabilita.model.Pagamento;
import helitec.contabilita.model.PagamentoFattura;
import helitec.contabilita.model.PagamentoFattura.Intero;
import helitec.contabilita.model.VoceCapitolatoCantiere;

public class HelitecDAO {
	
	public List<String> listFornitori() {
		String sql = "SELECT * FROM fornitore";
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(res.getString("nome"));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
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
	
	public List<Lavorazione> listLavorazioni(List <Cantiere> cantieri, List<VoceCapitolatoCantiere> vociCapitolatoCantieri) {
		String sql = "SELECT * FROM lavorazione";
		List<Lavorazione> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Cantiere cantiere = null;
				for(Cantiere c : cantieri)
					if(c.getNumero().equals(res.getInt("cantiere")))
						cantiere = c;
				VoceCapitolatoCantiere voceCapitolatoCantiere = null;
				for(VoceCapitolatoCantiere vcc : vociCapitolatoCantieri)
					if(vcc.getCantiere().equals(cantiere) && vcc.getVoceCapitolato().equals(res.getString("voce_capitolato")))
							voceCapitolatoCantiere = vcc;
				result.add(new Lavorazione(res.getString("descrizione"), res.getString("voce_capitolato"), cantiere,
						voceCapitolatoCantiere, res.getDouble("importo")));
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
	
	public List<VoceCapitolatoCantiere> listVociCapitolatoCantiere(List<Cantiere> cantieri) {
		String sql = "SELECT * FROM voce_cantiere";
		List<VoceCapitolatoCantiere> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Cantiere cantiere = null;
				for(Cantiere c : cantieri)
					if(c.getNumero().equals(res.getInt("cantiere")))
						cantiere = c;
				result.add(new VoceCapitolatoCantiere(res.getString("voce_capitolato"),cantiere,
						 res.getDouble("importo_previsto"), res.getDouble("importo_pagato")));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> listVociCapitolato(){
		String sql = "SELECT * FROM voce_capitolato";
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(res.getString("voce_capitolato"));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Pagamento> listPagamenti() {
		String sql = "SELECT * FROM pagamento";
		List<Pagamento> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next())
				result.add(new Pagamento(res.getDate("data").toLocalDate(), res.getString("fornitore"), res.getDouble("importo")));
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void setImporti(List<Fattura> fatture, List<Lavorazione> lavorazioni) {
		String sql = "SELECT * FROM importo_lavorazione";
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Fattura fattura = new Fattura();
				fattura.setNumero(res.getString("numero_fattura"));
				fattura.setData(res.getDate("data_fattura").toLocalDate());
				fattura.setFornitore(res.getString("fornitore"));
				if(fatture.contains(fattura))
					fattura = fatture.get(fatture.indexOf(fattura));
				else fattura = null;
				Lavorazione lavorazione = new Lavorazione();
				lavorazione.setDescrizione(res.getString("descrizione"));
				for(Lavorazione l : lavorazioni)
					if(l.getCantiere().getNumero().equals(res.getInt("cantiere")))
							lavorazione.setCantiere(l.getCantiere());
				if(lavorazioni.contains(lavorazione))
					lavorazione = lavorazioni.get(lavorazioni.indexOf(lavorazione));
				else lavorazione = null;
				Importo i = new Importo(res.getInt("numero"), fattura, lavorazione, res.getDouble("importo"), 
						res.getDouble("importoIva"), res.getString("note"));
				i.getFattura().addImportoDB(i);
				i.getLavorazione().addImportoDB(i);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setPagamentiFattura(List<Pagamento> pagamenti, List<Fattura> fatture) {
		String sql = "SELECT * FROM pagamento_fattura";
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Fattura fattura = new Fattura();
				fattura.setNumero(res.getString("numero_fattura"));
				fattura.setData(res.getDate("data_fattura").toLocalDate());
				fattura.setFornitore(res.getString("fornitore"));
				if(fatture.contains(fattura))
					fattura = fatture.get(fatture.indexOf(fattura));
				else fattura = null;
				Pagamento pagamento = new Pagamento();
				pagamento.setData(res.getDate("data_pagamento").toLocalDate());
				pagamento.setFornitore("fornitore");
				if(pagamenti.contains(pagamento))
					pagamento = pagamenti.get(pagamenti.indexOf(pagamento));
				else pagamento = null;
				Intero intero = null;
				if(res.getInt("intero")==1)
					intero = Intero.INTERO;
				else if(res.getInt("acconto")==1)
					intero = Intero.ACCONTO;
				else if(res.getInt("saldo")==1)
					intero = Intero.SALDO;
				PagamentoFattura pg = new PagamentoFattura(fattura, pagamento, res.getDouble("impoto_relativo"), 
						intero, res.getString("note"));
				pg.getPagamento().aggiungiPagFattura(pg);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void aggiungiFattura(Fattura f) {
		String sql = "INSERT INTO fattura VALUES (?,?,?,?,?,?,?,?)";
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, f.getNumero());
			st.setDate(2, Date.valueOf(f.getData()));
			st.setString(3, f.getFornitore());
			st.setDouble(4, f.getImportoNoIva());
			st.setInt(5, f.getIva());
			st.setDouble(6, f.getImportoTot());
			if(f.getImportoPagato()!=null)
				st.setDouble(7, f.getImportoPagato());
			else 
				st.setNull(7, Types.DOUBLE);
			if(f.getNote()!=null)
				st.setString(8, f.getNote());
			else 
				st.setNull(8, Types.VARCHAR);
			st.executeQuery() ;
			conn.close();
	} catch (SQLException e) {
		e.printStackTrace();
		System.out.println("Errore connessione al database");
		throw new RuntimeException("Error Connection Database");
		}
	}
	
	public void aggiungiImportiFattura(Fattura f) {
		String sql = "INSERT INTO importo_lavorazione VALUES (?,?,?,?,?,?,?,?,?)";
		for(Importo i : f.getImporti()) {
			try {
				Connection conn = DBConnect.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
				st.setInt(1, i.getNumero());
				st.setString(2, f.getNumero());
				st.setDate(3, Date.valueOf(f.getData()));
				st.setString(4, f.getFornitore());
				if(i.getLavorazione().getCantiere()!=null)
					st.setInt(5, i.getLavorazione().getCantiere().getNumero()); 
				else 
					st.setNull(5, Types.INTEGER);
				if(i.getLavorazione().getDescrizione()!=null)
					st.setString(6, i.getLavorazione().getDescrizione());
				else
					st.setNull(6, Types.VARCHAR);
				st.setDouble(7, i.getImporto());
				st.setDouble(8, i.getImportoIva());
				if(i.getNote()!=null)
					st.setString(9, i.getNote());
				else 
					st.setNull(9, Types.VARCHAR);
				st.executeQuery() ;
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Errore connessione al database");
				throw new RuntimeException("Error Connection Database");
			}
		}
	}
	

	public void aggiungiLavorazioni(List<Lavorazione> nuoveLav) {
		String sql = "INSERT INTO lavorazione VALUES (?,?,?,?)";
		for(Lavorazione l : nuoveLav) {
			try {
				Connection conn = DBConnect.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
				if(l.getDescrizione()!=null)
					st.setString(1, l.getDescrizione());
				else 
					st.setNull(1, Types.VARCHAR);
				if(l.getVoceCapitolato()!=null)
					st.setString(2, l.getVoceCapitolato());
				else 
					st.setNull(2, Types.VARCHAR);
				if(l.getCantiere()!=null)
					st.setInt(3, l.getCantiere().getNumero());
				else 
					st.setNull(3, Types.INTEGER);
				st.setDouble(4, l.getImporto());
				st.executeQuery() ;
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Errore connessione al database");
				throw new RuntimeException("Error Connection Database");
			}
		}
	}
	
	public void aggiornaImportiLavorazioni(List<Lavorazione> lavMod) {
		String sql = "UPDATE lavorazione SET importo = ? WHERE cantiere = ? AND descrizione = ?";
		for(Lavorazione l : lavMod) {
			try {
				Connection conn = DBConnect.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
				st.setDouble(1, l.getImporto());
				st.setInt(2, l.getCantiere().getNumero());
				st.setString(3, l.getDescrizione());
				st.executeQuery();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Errore connessione al database");
				throw new RuntimeException("Error Connection Database");
			}
		}
	}
	
	public void aggiornaImportiCantiere(List<Cantiere> cantieri) {
		String sql = "UPDATE cantiere SET importoTot = ? WHERE numero = ?";
		for(Cantiere c : cantieri) {
			try {
				Connection conn = DBConnect.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
				st.setDouble(1, c.getImportoTotale());
				st.setInt(2, c.getNumero());
				st.executeQuery() ;
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Errore connessione al database");
				throw new RuntimeException("Error Connection Database");
			}
		}	
	}
	
	public void aggiungiVociCapitolatoCantiere(List<VoceCapitolatoCantiere> nuoveVoci) {
		String sql = "INSERT INTO voce_cantiere VALUES (?,?,?,?)";
		for(VoceCapitolatoCantiere vcc : nuoveVoci) {
			try {
				Connection conn = DBConnect.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
				st.setString(1, vcc.getVoceCapitolato());
				st.setInt(2, vcc.getCantiere().getNumero());
				if(vcc.getImportoPrevisto()!=null)
					st.setDouble(3, vcc.getImportoPrevisto());
				else
					st.setNull(3, Types.DOUBLE);
				st.setDouble(4, vcc.getImportoPagato());
				st.executeQuery() ;
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Errore connessione al database");
				throw new RuntimeException("Error Connection Database");
			}
		}
	}
	
	public void aggiornaImportiVociCapitolato(List<VoceCapitolatoCantiere> voci) {
		String sql = "UPDATE voce_cantiere SET importo_pagato = ? WHERE voce_capitolato = ? AND cantiere = ?";
		for(VoceCapitolatoCantiere vcc : voci) {
			try {
				Connection conn = DBConnect.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
				st.setDouble(1, vcc.getImportoPagato());
				st.setString(2, vcc.getVoceCapitolato());
				st.setInt(3, vcc.getCantiere().getNumero());
				st.executeQuery() ;
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Errore connessione al database");
				throw new RuntimeException("Error Connection Database");
			}
		}	
	}
	
	public void aggiungiFornitore(String fornitore) {
		String sql = "INSERT INTO fornitore VALUES (?)";
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, fornitore);
			st.executeQuery() ;
			conn.close();
	} catch (SQLException e) {
		e.printStackTrace();
		System.out.println("Errore connessione al database");
		throw new RuntimeException("Error Connection Database");
		}
	}

	public void aggiungiPagamento(Pagamento p) {
		String sql = "INSERT INTO pagamento VALUES (?,?,?)";
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDate(1, Date.valueOf(p.getData()));
			st.setString(2, p.getFornitore());
			st.setDouble(3, p.getImporto());
			st.executeQuery();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public void aggiorniPagamentiFattura(Pagamento p) {
		String sql = "INSERT INTO pagamento_fattura VALUES (?,?,?,?,?,?,?,?,?)";
		for(PagamentoFattura pf : p.getFatture()) {
			try {
				Connection conn = DBConnect.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
				st.setString(1, pf.getFattura().getNumero());
				if(pf.getFattura().getData()!=null)
					st.setDate(2, Date.valueOf(pf.getFattura().getData()));
				else
					st.setNull(2, Types.DATE);
				st.setString(3, p.getFornitore());
				st.setDate(4, Date.valueOf(p.getData()));
				if(pf.getImportoRelativo()!=null)
					st.setDouble(5, pf.getImportoRelativo());
				else 
					st.setNull(5, Types.DOUBLE);
				st.setInt(6, 0);
				st.setInt(7, 0);
				st.setInt(8, 0);
				if(pf.getIntero().equals(Intero.INTERO))
					st.setInt(6, 1);
				else if(pf.getIntero().equals(Intero.ACCONTO))
					st.setInt(7, 1);
				else if(pf.getIntero().equals(Intero.SALDO))
					st.setInt(8, 1);
				if(pf.getNote()!=null)
					st.setString(9, pf.getNote());
				else 
					st.setNull(9, Types.VARCHAR);
				st.executeQuery() ;
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Errore connessione al database");
				throw new RuntimeException("Error Connection Database");
			}
		}
		
	}
	
}
