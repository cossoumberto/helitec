package helitec.contabilita.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Lavorazione {
	
	private String descrizione;
	private String voceCapitolato;
	private Cantiere cantiere;
	private VoceCapitolatoCantiere voceCapitolatoCantiere;
	private Double importo;
	private List<Importo> importi;

	public Lavorazione(String descrizione, String voceCapitolato, Cantiere cantiere,
			VoceCapitolatoCantiere voceCapitolatoCantiere, Double importo) {
		super();
		this.descrizione = descrizione;
		this.voceCapitolato = voceCapitolato;
		this.cantiere = cantiere;
		this.voceCapitolatoCantiere = voceCapitolatoCantiere;
		this.importo = importo;
		this.importi = new ArrayList<>();
	}

	public Lavorazione() {
		this.descrizione = null;
		this.voceCapitolato = null;
		this.cantiere = null;
		this.voceCapitolatoCantiere = null;
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
	
	public VoceCapitolatoCantiere getVoceCapitolatoCantiere() {
		return voceCapitolatoCantiere;
	}

	public void setVoceCapitolatoCantiere(VoceCapitolatoCantiere voceCapitolatoCantiere) {
		this.voceCapitolatoCantiere = voceCapitolatoCantiere;
	}

	public Double getImporto() {
		return importo;
	}

	public void setImporto(Double importo) {
		this.importo = importo;
	}
	
	public List<Importo> getImporti() {
		return importi;
	}

	public void addImporto (Importo i) {
		//aggiornamento importi lavorazione
		this.importi.add(i);
		if(this.importo==null) 																	
			this.importo = 0.0;
		this.importo += i.getImporto();
		BigDecimal x = new BigDecimal(this.importo).setScale(2, RoundingMode.HALF_EVEN);
		this.importo = x.doubleValue();
		//aggiornamento importo cantiere
		if(this.cantiere!=null) {
			if(this.cantiere.getImportoTotale()==null)											
				this.cantiere.setImportoTotale(0.0);
			Double importoc = this.cantiere.getImportoTotale() + i.getImporto();
			BigDecimal x1 = new BigDecimal(importoc).setScale(2, RoundingMode.HALF_EVEN);
			this.cantiere.setImportoTotale(x1.doubleValue());
		}
		//aggiornamento importo voce capitolato
		if(this.voceCapitolatoCantiere!=null) {
			if(this.voceCapitolatoCantiere.getImportoPagato()==null)								
				this.voceCapitolatoCantiere.setImportoPagato(0.0);
			Double importov = this.voceCapitolatoCantiere.getImportoPagato() + i.getImporto();
			BigDecimal x2 = new BigDecimal(importov).setScale(2, RoundingMode.HALF_EVEN);
			this.voceCapitolatoCantiere.setImportoPagato(x2.doubleValue());
		}
	}
	
	public void addImportoDB(Importo i) {
		this.importi.add(i);
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
