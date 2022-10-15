package helitec.contabilita.model;

public class Importo {
	
	private Integer numero;
	private Fattura fattura;
	private Lavorazione lavorazione;
	private Double importo;
	private Double importoIva;
	private String note;
	
	public Importo(Integer numero, Fattura fattura, Lavorazione lavorazione, Double importo, Double importoIva, String note) {
		super();
		this.numero = numero;
		this.fattura = fattura;
		this.lavorazione = lavorazione;
		this.importo = importo;
		this.importoIva = importoIva;
		this.note = note;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Fattura getFattura() {
		return fattura;
	}

	public void setFattura(Fattura fattura) {
		this.fattura = fattura;
	}

	public Lavorazione getLavorazione() {
		return lavorazione;
	}

	public void setLavorazione(Lavorazione lavorazione) {
		this.lavorazione = lavorazione;
	}

	public Double getImporto() {
		return importo;
	}
	
	public Double getImportoIva() {
		return importoIva;
	}

	public void setImportoIva(Double importoIva) {
		this.importoIva = importoIva;
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
		String s = numero + ": ";
		if(lavorazione!=null) {
			if(lavorazione.getCantiere()==null)
				s += "-- / ";
			else 
				s += lavorazione.getCantiere().toString() + " / ";
			if(lavorazione.getDescrizione()==null)
				s += "--";
			else 
				s += lavorazione.getDescrizione();
			if(lavorazione.getVoceCapitolato()==null)
				s += "\n     --";
			else
				s += "\n     " + lavorazione.getVoceCapitolato();
			s += "\n     " + this.importo + " / " + this.importoIva;
		} else 
			s += "-- / --\n     --\n     " +  + this.importo + " / " + this.importoIva;
		if(note!=null)
			s += "\n     Note: " + note;
		return s;			 
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fattura == null) ? 0 : fattura.hashCode());
		result = prime * result + ((lavorazione == null) ? 0 : lavorazione.hashCode());
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
		Importo other = (Importo) obj;
		if (fattura == null) {
			if (other.fattura != null)
				return false;
		} else if (!fattura.equals(other.fattura))
			return false;
		if (lavorazione == null) {
			if (other.lavorazione != null)
				return false;
		} else if (!lavorazione.equals(other.lavorazione))
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		return true;
	}
	
}
