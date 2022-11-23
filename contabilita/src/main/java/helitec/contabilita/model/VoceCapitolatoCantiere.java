package helitec.contabilita.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class VoceCapitolatoCantiere {
	
	private String voceCapitolato;
	private Cantiere cantiere;
	private Double importoPrevisto;
	private Double importoPagato;

	public VoceCapitolatoCantiere(String voceCapitolato, Cantiere cantiere, Double importoPrevisto,
			Double importoPagato) {
		super();
		this.voceCapitolato = voceCapitolato;
		this.cantiere = cantiere;
		this.importoPrevisto = importoPrevisto;
		this.importoPagato = importoPagato;
	}

	public VoceCapitolatoCantiere() {
		this.voceCapitolato = null;
		this.cantiere = null;
		this.importoPrevisto = null;
		this.importoPagato = null;

	}

	public String getVoceCapitolato() {
		return voceCapitolato;
	}

	public void setVoceCapitolato(String voceCapitolato) {
		this.voceCapitolato = voceCapitolato;
	}

	public Cantiere getCantiere() {
		return cantiere;
	}

	public void setCantiere(Cantiere cantiere) {
		this.cantiere = cantiere;
	}

	public Double getImportoPrevisto() {
		return importoPrevisto;
	}

	public void setImportoPrevisto(Double importoPrevisto) {
		this.importoPrevisto = importoPrevisto;
	}

	public Double getImportoPagato() {
		return importoPagato;
	}

	public void setImportoPagato(Double importoPagato) {
		this.importoPagato = importoPagato;
	}

	@Override
	public String toString() {
		return "VoceCapitolatoCantiere [voceCapitolato=" + voceCapitolato + ", cantiere=" + cantiere
				+ ", importoPrevisto=" + importoPrevisto + ", importoPagato=" + importoPagato + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cantiere == null) ? 0 : cantiere.hashCode());
		result = prime * result + ((voceCapitolato == null) ? 0 : voceCapitolato.hashCode());
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
		VoceCapitolatoCantiere other = (VoceCapitolatoCantiere) obj;
		if (cantiere == null) {
			if (other.cantiere != null)
				return false;
		} else if (!cantiere.equals(other.cantiere))
			return false;
		if (voceCapitolato == null) {
			if (other.voceCapitolato != null)
				return false;
		} else if (!voceCapitolato.equals(other.voceCapitolato))
			return false;
		return true;
	}

	public void sommaImporto(Double importo) {
		if(this.importoPagato==null)
			this.importoPagato = 0.0;
		this.importoPagato += importo;
		BigDecimal x = new BigDecimal(this.importoPagato).setScale(2, RoundingMode.HALF_EVEN);
		this.importoPagato = x.doubleValue();
	}

}
