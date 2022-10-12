package helitec.contabilita.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Fattura {
	
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
	
	public String getNote() {
		return note;
	}


	public void setNote(String note) {
		this.note = note;
	}


	@Override
	public String toString() {
		return "Fattura [numero=" + numero + ", data=" + data + ", fornitore=" + fornitore + ", importoNoIva="
				+ importoNoIva + ", iva=" + iva + ", importoTot=" + importoTot + ", importoPagato=" + importoPagato
				+ "]";
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
	
		
}

