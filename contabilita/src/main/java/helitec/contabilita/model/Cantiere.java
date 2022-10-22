package helitec.contabilita.model;

public class Cantiere implements Comparable<Cantiere>{
	
	private Integer numero;
	private String denominazione;
	private String indirizzo;
	private String comune;
	private Double preventivo;
	private Double importoTotale;
	
	public Cantiere(Integer numero, String denominazione, String indirizzo, String comune, Double preventivo,
			Double importoTotale) {
		super();
		this.numero = numero;
		this.denominazione = denominazione;
		this.indirizzo = indirizzo;
		this.comune = comune;
		this.preventivo = preventivo;
		this.importoTotale = importoTotale;
	}
	
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public String getComune() {
		return comune;
	}
	public void setComune(String comune) {
		this.comune = comune;
	}
	public Double getPreventivo() {
		return preventivo;
	}
	public void setPreventivo(Double preventivo) {
		this.preventivo = preventivo;
	}
	public Double getImportoTotale() {
		return importoTotale;
	}
	public void setImportoTotale(Double importoTotale) {
		this.importoTotale = importoTotale;
	}

	@Override
	public String toString() {
		return numero + " - " + denominazione ;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		Cantiere other = (Cantiere) obj;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		return true;
	}

	@Override
	public int compareTo(Cantiere o) {
		return this.numero.compareTo(o.getNumero());
	}
	
}
