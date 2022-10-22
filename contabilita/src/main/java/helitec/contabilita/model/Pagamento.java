package helitec.contabilita.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Pagamento {
	
	private LocalDate data;
	private String fornitore;
	private Double importo;
	private String note;
	private List<PagamentoFattura> fatture;
	
	public Pagamento(LocalDate data, String fornitore, Double importo, String note) {
		super();
		this.data = data;
		this.fornitore = fornitore;
		this.importo = importo;
		this.note = note;
		this.fatture = new ArrayList<>();
	}

	public Pagamento() {
		this.data = null;
		this.fornitore = null;
		this.importo = null;
		this.note = null;
		this.fatture = new ArrayList<>();
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

	public Double getImporto() {
		return importo;
	}

	public void setImporto(Double importo) {
		this.importo = importo;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return "Pagamento del " + data.format(formatter) + " a " + fornitore + " / Importo: " + importo;
	}
	
	public String toStringConFatture() {
		String s = this.toString() + "\n";
		if(this.fatture.size()>0) {
			for(PagamentoFattura pf : this.fatture) {
				s += "\n  " + this.fatture.indexOf(pf) + ") [" + pf.getFattura().toString() + " ] / " + this.importo;
				if(this.note!=null)
					s+= " / " + this.note;
			}
		}
		return s;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((fornitore == null) ? 0 : fornitore.hashCode());
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
		Pagamento other = (Pagamento) obj;
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
		return true;
	}

	
}
