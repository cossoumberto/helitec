package helitec.contabilita.model;

public class PagamentoFattura {
	
	private Fattura fattura;
	private Pagamento pagamento;
	private Double importoRelativo;
	private String note;
	
	public PagamentoFattura(Fattura fattura, Pagamento pagamento, Double importoRelativo, String note) {
		super();
		this.fattura = fattura;
		this.pagamento = pagamento;
		this.importoRelativo = importoRelativo;
		this.note = note;
	}

	public Fattura getFattura() {
		return fattura;
	}

	public void setFattura(Fattura fattura) {
		this.fattura = fattura;
	}

	public Pagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}

	public Double getImportoRelativo() {
		return importoRelativo;
	}

	public void setImportoRelativo(Double importoRelativo) {
		this.importoRelativo = importoRelativo;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		return "PagamentoFattura [fattura=" + fattura + ", pagamento=" + pagamento + ", importoRelativo="
				+ importoRelativo + ", note=" + note + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fattura == null) ? 0 : fattura.hashCode());
		result = prime * result + ((pagamento == null) ? 0 : pagamento.hashCode());
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
		PagamentoFattura other = (PagamentoFattura) obj;
		if (fattura == null) {
			if (other.fattura != null)
				return false;
		} else if (!fattura.equals(other.fattura))
			return false;
		if (pagamento == null) {
			if (other.pagamento != null)
				return false;
		} else if (!pagamento.equals(other.pagamento))
			return false;
		return true;
	}
	
	

}
