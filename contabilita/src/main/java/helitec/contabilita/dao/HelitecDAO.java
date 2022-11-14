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
				Double d1 = res.getDouble("importo_no_iva");
				Double d2= res.getDouble("importo_tot");
				Double d3 = res.getDouble("importo_pagato");
				if(d1==0) d1=null;
				if(d2==0) d2=null;
				if(d3==0) d3=null;
 				result.add(new Fattura(res.getString("numero"), res.getDate("data").toLocalDate(), res.getString("fornitore"),
						d1, res.getInt("iva"), d2, d3, res.getString("note")));
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
					if(res.getString("cantiere")!=null)
						if(c.getNumero().equals(res.getInt("cantiere")))
							cantiere = c;
				VoceCapitolatoCantiere voceCapitolatoCantiere = null;
				for(VoceCapitolatoCantiere vcc : vociCapitolatoCantieri)
					if(vcc.getCantiere().equals(cantiere) && vcc.getVoceCapitolato().equals(res.getString("voce_capitolato")))
							voceCapitolatoCantiere = vcc;
				Double d1 = res.getDouble("importo");
				if(d1==0) d1=null;
				result.add(new Lavorazione(res.getString("descrizione"), res.getString("voce_capitolato"), cantiere,
						voceCapitolatoCantiere, d1));
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
				Double d1= res.getDouble("preventivo");
				Double d2 = res.getDouble("importo_tot");
				if(d1==0) d1=null;
				if(d2==0) d2=null;
				result.add(new Cantiere(res.getInt("numero"), res.getString("denominazione"), res.getString("indirizzo"),
						res.getString("comune"), d1, d2));
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
				Double d1= res.getDouble("importo_previsto");
				Double d2 = res.getDouble("importo_pagato");
				if(d1==0) d1=null;
				if(d2==0) d2=null;
				result.add(new VoceCapitolatoCantiere(res.getString("voce_capitolato"),cantiere, d1, d2));
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
			while (res.next()) {
				Double d1 = res.getDouble("importo");
				if(d1==0) d1=null;
				result.add(new Pagamento(res.getDate("data").toLocalDate(), res.getString("fornitore"), d1));
			}
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
				for(Lavorazione l : lavorazioni) {
					if(l.getCantiere()!=null && res.getString("cantiere")!=null &&
							l.getCantiere().getNumero().equals(res.getInt("cantiere")))
						lavorazione.setCantiere(l.getCantiere());
					else if(l.getCantiere()==null && res.getString("cantiere")==null)
						lavorazione.setCantiere(null);
				}
				if(lavorazioni.contains(lavorazione))
					lavorazione = lavorazioni.get(lavorazioni.indexOf(lavorazione));
				else lavorazione = null;
				Double d1= res.getDouble("importo");
				Double d2 = res.getDouble("importo_iva");
				if(d1==0) d1=null;
				if(d2==0) d2=null;
				Importo i = new Importo(res.getInt("numero"), fattura, lavorazione, d1, d2, res.getString("note"));
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
				if(res.getDate("data_fattura")==null)
					fattura.setData(null);
				else fattura.setData(res.getDate("data_fattura").toLocalDate());
				fattura.setFornitore(res.getString("fornitore"));
				if(fatture.contains(fattura))
					fattura = fatture.get(fatture.indexOf(fattura));
				Pagamento pagamento = new Pagamento();
				pagamento.setData(res.getDate("data_pagamento").toLocalDate());
				pagamento.setFornitore(res.getString("fornitore"));
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
				Double d1 = res.getDouble("importo_relativo");
				if(d1==0) d1=null;
				PagamentoFattura pg = new PagamentoFattura(fattura, pagamento, d1, intero, res.getString("note"));
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
				if(i.getLavorazione()!=null) {
					if(i.getLavorazione().getCantiere()!=null)
						st.setInt(5, i.getLavorazione().getCantiere().getNumero()); 
					else 
						st.setNull(5, Types.INTEGER);
					if(i.getLavorazione().getDescrizione()!=null)
						st.setString(6, i.getLavorazione().getDescrizione());
					else
						st.setNull(6, Types.VARCHAR);
				} else {
					st.setNull(5, Types.INTEGER);
					st.setNull(6, Types.VARCHAR);
				}
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
		String sql = "UPDATE cantiere SET importo_tot = ? WHERE numero = ?";
		for(Cantiere c : cantieri) {
			try {
				Connection conn = DBConnect.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
				if(c.getImportoTotale()!=null)
					st.setDouble(1, c.getImportoTotale());
				else
					st.setNull(1, Types.DOUBLE);
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
				if(vcc.getImportoPagato()!=null)
					st.setDouble(1, vcc.getImportoPagato());
				else
					st.setNull(1, Types.DOUBLE);
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

	public void aggiungiPagamentiFattura(Pagamento p) {
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

	public void aggiornaImportiRelativiFattura(List<Fattura> fmod) {
		String sql = "UPDATE fattura SET importo_pagato = ? WHERE numero = ? AND data = ? AND fornitore = ?";
		for(Fattura f : fmod) {
			try {
				Connection conn = DBConnect.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
				st.setDouble(1, f.getImportoPagato());
				st.setString(2, f.getNumero());
				st.setDate(3, Date.valueOf(f.getData()));
				st.setString(4, f.getFornitore());
				st.executeQuery() ;
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Errore connessione al database");
				throw new RuntimeException("Error Connection Database");
			}
		}
	}

	public void aggiornaPagamentiFattura(List<PagamentoFattura> pfMod) {
		String sql = "UPDATE pagamento_fattura SET data_fattura= ?, importo_relativo = ?" +
					"WHERE numero_fattura = ? AND fornitore = ? AND data_pagamento = ?";
		for(PagamentoFattura pf : pfMod) {
			try {
				Connection conn = DBConnect.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
				st.setDate(1, Date.valueOf(pf.getFattura().getData()));
				st.setDouble(2, pf.getImportoRelativo());
				st.setString(3, pf.getFattura().getNumero());
				st.setString(4, pf.getPagamento().getFornitore());
				st.setDate(5, Date.valueOf(pf.getPagamento().getData()));
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
