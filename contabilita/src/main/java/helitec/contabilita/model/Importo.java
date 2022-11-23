package helitec.contabilita.model;

public class Importo implements Comparable<Importo>{
	
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
	
	public Importo(Importo i) {
		super();
		this.numero = i.numero;
		this.fattura = i.fattura;
		this.lavorazione = i.lavorazione;
		this.importo = i.importo;
		this.importoIva = i.importoIva;
		this.note = i.note;
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
	
	public String toStringFattura() {
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
	
	public boolean equalsTotale(Importo i) {
		if(i.fattura.equals(this.fattura) && i.importo.equals(this.importo) && i.importoIva.equals(this.importoIva)
				&& i.lavorazione.equals(this.lavorazione) 
				&& ( (i.lavorazione.getVoceCapitolato()==null && this.lavorazione.getVoceCapitolato()==null)
						|| (i.lavorazione.getVoceCapitolato()!=null && this.lavorazione.getVoceCapitolato()!=null 
							&& i.lavorazione.getVoceCapitolato().equals(this.lavorazione.getVoceCapitolato())) )
				&& ( (i.note==null && this.note==null) || i.note!=null && this.note!=null && i.note.equals(this.note) )
				&& i.numero.equals(this.numero)) {
			return true;
		} 
		return false;
	}

	@Override
	public String toString() {
		if(this.numero!=null) {
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
			} else 
				s += "-- / --";
			return s;			
		} else return "Inserisci nuovo importo";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fattura == null) ? 0 : fattura.hashCode());
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
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		return true;
	}

	@Override
	public int compareTo(Importo o) {
		return this.numero.compareTo(o.numero);
	}
	
}
