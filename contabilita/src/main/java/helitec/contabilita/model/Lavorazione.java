package helitec.contabilita.model;

import java.util.ArrayList;
import java.util.List;

public class Lavorazione {
	
	private String descrizione;
	private String voceCapitolato;
	private Cantiere cantiere;
	private Double importo;
	private List<Importo> importi;
	
	public Lavorazione(String descrizione, String voceCapitolato, Cantiere cantiere, Double importo) {
		super();
		this.descrizione = descrizione;
		this.voceCapitolato = voceCapitolato;
		this.cantiere = cantiere;
		this.importo = importo;
		this.importi = new ArrayList<>();
	}

	public Lavorazione() {
		this.descrizione = null;
		this.voceCapitolato = null;
		this.cantiere = null;
		this.importo = null;
		this.importi = new ArrayList<>();
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
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

	public Double getImporto() {
		return importo;
	}

	public void setImporto(Double importo) {
		this.importo = importo;
	}

	@Override
	public String toString() {
		return "Lavorazione [descrizione=" + descrizione + ", voceCapitolato=" + voceCapitolato + ", cantiere="
				+ cantiere + ", importo=" + importo + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cantiere == null) ? 0 : cantiere.hashCode());
		result = prime * result + ((descrizione == null) ? 0 : descrizione.hashCode());
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
		Lavorazione other = (Lavorazione) obj;
		if (cantiere == null) {
			if (other.cantiere != null)
				return false;
		} else if (!cantiere.equals(other.cantiere))
			return false;
		if (descrizione == null) {
			if (other.descrizione != null)
				return false;
		} else if (!descrizione.equals(other.descrizione))
			return false;
		return true;
	}

}
