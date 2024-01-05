package invsys.entities.compositkeys;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

import invsys.entities.CreditInvoice;

@Embeddable
public class CreditInvoiceDetailId implements Serializable {

	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumns(value = {
			@JoinColumn(name = "invoice_id", referencedColumnName = "invoice_id"),
			@JoinColumn(name = "user_id", referencedColumnName = "user_id"),
			@JoinColumn(name = "invoice_date", referencedColumnName = "invoice_date") })
	private CreditInvoice creditInvoice;
	
	@Column(name = "seq_no")
	private int seqNo;

	public CreditInvoice getCreditInvoice() {
		return creditInvoice;
	}

	public void setCreditInvoice(CreditInvoice creditInvoice) {
		this.creditInvoice = creditInvoice;
	}

	public int getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof CreditInvoiceDetailId))
			return false;
		CreditInvoiceDetailId that = (CreditInvoiceDetailId) obj;
		return Objects.equals(getCreditInvoice(), that.getCreditInvoice()) && Objects.equals(getSeqNo(), that.getSeqNo());

	}

	@Override
	public int hashCode() {
		return Objects.hash(getCreditInvoice(), getSeqNo());
	}
	
	
	
}
