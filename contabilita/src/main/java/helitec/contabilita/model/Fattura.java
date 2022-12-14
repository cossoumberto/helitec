package helitec.contabilita.model;

import java.math.BigDecimal;

import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import helitec.contabilita.model.PagamentoFattura.Intero;

public class Fattura implements Comparable<Fattura>{
	
	private String numero;
	private LocalDate data;
	private String fornitore;
	private Double importoNoIva;
	private Integer iva;
	private Double importoTot;
	private Double importoPagato;
	private String note;
	private List<Importo> importi;
	

	public Fattura(String numero, LocalDate data, String fornitore, Double importoNoIva, Integer iva, Double importoTot,
			Double importoPagato, String note) {
		super();
		this.numero = numero;
		this.data = data;
		this.fornitore = fornitore;
		this.importoNoIva = importoNoIva;
		this.iva = iva;
		this.importoTot = importoTot;
		this.importoPagato = importoPagato;
		this.note = note;
		this.importi = new ArrayList<>();
	}

	public Fattura() {
		this.numero = null;
		this.data = null;
		this.fornitore = null;
		this.importoNoIva = null;																	
		this.iva = null;
		this.importoTot = null;																			
		this.importoPagato = null;				
		this.note = null;
		this.importi = new ArrayList<>();
	}


	public Fattura(Fattura f) {
		this.numero = f.numero;
		this.data = f.data;
		this.fornitore = f.fornitore;
		this.importoNoIva = f.importoNoIva;
		this.iva = f.iva;
		this.importoTot = f.importoTot;
		this.importoPagato = f.importoPagato;
		this.note = f.note;
		this.importi = new ArrayList<Importo>(f.importi);
	}

	public String getNumero() {
		return numero;
	}


	public void setNumero(String numero) {
		this.numero = numero;
	}


	public LocalDate getData() {
		return data;
	}


	public void setData(LocalDate data) {
		this.data = data;
	}


	public String getFornitore() {
		return fornitore;
	}


	public void setFornitore(String fornitore) {
		this.fornitore = fornitore;
	}


	public Double getImportoNoIva() {
		return importoNoIva;
	}


	public void setImportoNoIva(Double importoNoIva) {
		this.importoNoIva = importoNoIva;
	}


	public Integer getIva() {
		return iva;
	}
	
	public Double getIvaDecimale() {
		return iva.doubleValue()/100;
	}


	public void setIva(Integer iva) {
		this.iva = iva;
	}


	public Double getImportoTot() {
		return importoTot;
	}


	public void setImportoTot(Double importoTot) {
		this.importoTot = importoTot;
	}


	public Double getImportoPagato() {
		return importoPagato;
	}


	public void setImportoPagato(Double importoPagato) {
		this.importoPagato = importoPagato;
	}
	
	public int aggiornaImportoPagato(PagamentoFattura pf) {
		//se non conosco importo relativo, ma pagamento intero
		if(pf.getImportoRelativo()==null)																	
			if(pf.getIntero().equals(Intero.INTERO)) {
				pf.setImportoRelativo(this.importoTot);
			}
		//se conosco importo relativo
		if(pf.getImportoRelativo()!=null) {																	
			if(pf.getImportoRelativo().equals(this.importoTot)) {
				if(pf.getIntero().equals(Intero.INTERO)) {				
					if(this.importoPagato==null)														
						this.importoPagato = 0.0;
					Double d = this.importoPagato + pf.getImportoRelativo();
					BigDecimal x = new BigDecimal(d).setScale(2, RoundingMode.HALF_EVEN);
					this.importoPagato = x.doubleValue();
					return 1;
				} else return 0;
			} else if(pf.getImportoRelativo()<this.importoTot) {
				if(pf.getIntero().equals(Intero.SALDO) || pf.getIntero().equals(Intero.ACCONTO)) {
					if(this.importoPagato==null)														
						this.importoPagato = 0.0;
					Double d = this.importoPagato + pf.getImportoRelativo();
					BigDecimal x = new BigDecimal(d).setScale(2, RoundingMode.HALF_EVEN);
					this.importoPagato = x.doubleValue();
					return 1;
				} else return 0;
			} else return 0;
		} 
		// se non conosco importo relativo e i pagamenti sono acconti o saldi
		return 2;
	}
	
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	public void rimuoviImporti() {
		this.importi.clear();
		this.importoNoIva = null;
		this.importoTot = null;
	}

	public List<Importo> getImporti() {
		return importi;
	}
	
	public void addImporto (Importo i) {
		this.importi.add(i);
		if(this.importoNoIva==null || this.importoTot==null) {											
			this.importoNoIva = 0.0;
			this.importoTot = 0.0;
		}
		this.importoNoIva += i.getImporto();
		BigDecimal x = new BigDecimal(this.importoNoIva).setScale(2, RoundingMode.HALF_EVEN);
		this.importoNoIva = x.doubleValue();
		this.importoTot += i.getImportoIva();
		BigDecimal y = new BigDecimal(this.importoTot).setScale(2, RoundingMode.HALF_EVEN);
		this.importoTot= y.doubleValue();
	}
	
	public void addImportoDB(Importo i) {
		this.importi.add(i);
	}
	
	public void cancImporto(Importo i) {
		if(this.importi.contains(i)) {
			importi.remove(i);
			for(Importo j : this.importi)
				j.setNumero(importi.indexOf(j)+1);
		}
		this.calcolaImportoTot();
	}
	
	public void ordinaImporti() {
		Collections.sort(importi);
	}	
	
	public void cancLastImporto () {
		this.importoNoIva -= importi.get(importi.size()-1).getImporto();
		BigDecimal x = new BigDecimal(this.importoNoIva).setScale(2, RoundingMode.HALF_EVEN);
		this.importoNoIva = x.doubleValue();
		this.importoTot -= importi.get(importi.size()-1).getImportoIva();
		BigDecimal y = new BigDecimal(this.importoTot).setScale(2, RoundingMode.HALF_EVEN);
		this.importoTot= y.doubleValue();
		this.importi.remove(importi.size()-1);
	}
	
	public List<Cantiere> getCantieriFattura() {
		List<Cantiere> list = new ArrayList<>();
		for(Importo i : this.importi)
			if(!list.contains(i.getLavorazione().getCantiere()))
				list.add(i.getLavorazione().getCantiere());
		return list;
	}
	
	public List<String> getDescrLavorazioniFattura() {
		List<String> list = new ArrayList<>();
		for(Importo i : this.importi)
			if(!list.contains(i.getLavorazione().getDescrizione()))
				list.add(i.getLavorazione().getDescrizione());
		return list;
	}
	
	public List<String> getVociCapitolatoFattura() {
		List<String> list = new ArrayList<>();
		for(Importo i : this.importi)
			if(!list.contains(i.getLavorazione().getVoceCapitolato()))
				list.add(i.getLavorazione().getVoceCapitolato());
		return list;
	}
	
	public void calcolaImportoTot() {
		Double d1 = 0.0;
		Double d2 = 0.0;
		for(Importo i : this.importi) {
			d1 += i.getImporto();
			BigDecimal x = new BigDecimal(d1).setScale(2, RoundingMode.HALF_EVEN);
			d1 = x.doubleValue();
			d2 += i.getImportoIva();
			BigDecimal y = new BigDecimal(d2).setScale(2, RoundingMode.HALF_EVEN);
			d2 = y.doubleValue();
		}
		this.importoNoIva = d1;
		this.importoTot = d2;
		
	}

	public boolean equalsTotale(Fattura f) {
		if(f.numero.equals(this.numero)) 
			if(f.fornitore.equals(this.fornitore)) 
				if(f.data.equals(this.data)) 
					if(f.importoNoIva.equals(this.importoNoIva)) 
						if(f.iva.equals(this.iva)) 
							if(f.importoTot.equals(this.importoTot)) 
								if( (f.importoPagato==null && this.importoPagato==null) || 
										(this.importoPagato!=null && f.importoPagato!=null 
										&& f.importoPagato.equals(this.importoPagato)) ) 
									if( (f.note==null && this.note==null) || (f.note!=null && this.note!=null && f.note.equals(this.note)) ) 
										if(f.importi.size()==this.importi.size()) {
											for(Importo i : f.importi)
												if(!i.equalsTotale(this.importi.get(f.importi.indexOf(i))))
														return false;
											return true;
										}
		return false;
	}

	public String toStringConImporti() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String s = "Fornitore: " + fornitore + "\nFattura " + numero + " del " + data.format(formatter) + "     IVA = " + iva +"%\n";
		for (Importo i : importi)
			s += "\n\n" + i.toStringFattura();
		if(this.note!=null)
			s += "\n\n\n" + this.note;
		return s;
	}

	@Override
	public String toString() {
		String s = null;
		if(this.data!=null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			s = fornitore + " - Fattura " + numero + " del " + data.format(formatter) 
			+ " - Importo: " + importoTot + " - Pagato: ";	
			if(this.importoPagato==null)
				s += 0;
			else 
				s += this.importoPagato;
		} else
			s = fornitore + " - Fattura " + numero;
		return s;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((fornitore == null) ? 0 : fornitore.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Fattura other = (Fattura) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (fornitore == null) {
			if (other.fornitore != null)
				return false;
		} else if (!fornitore.equals(other.fornitore))
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		return true;
	}

	@Override
	public int compareTo(Fattura o) {
		if(!this.fornitore.equals(o.fornitore))
			return this.fornitore.compareTo(o.fornitore);
		else if(!this.data.equals(o.data))
			return this.data.compareTo(o.data);
		else return this.numero.compareTo(o.numero);
	}
}

