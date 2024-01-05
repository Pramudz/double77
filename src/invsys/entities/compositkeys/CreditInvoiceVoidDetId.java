package invsys.entities.compositkeys;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import invsys.entities.CreditInvoiceVoid;


@Embeddable
public class CreditInvoiceVoidDetId implements Serializable {

	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name="void_id", referencedColumnName = "void_id")
	private CreditInvoiceVoid creditInvoiceVoid;
	
	@Column(name="seq_no")
	private int seqNo;

	
	public CreditInvoiceVoid getCreditInvoiceVoid() {
		return creditInvoiceVoid;
	}

	public void setCreditInvoiceVoid(CreditInvoiceVoid creditInvoiceVoid) {
		this.creditInvoiceVoid = creditInvoiceVoid;
	}

	public int getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this== obj)
			return true;
		if(!((obj) instanceof CreditInvoiceVoidDetId))
			return false;
		CreditInvoiceVoidDetId that = (CreditInvoiceVoidDetId) obj;
		return Objects.equals(getCreditInvoiceVoid(), that.getCreditInvoiceVoid()) &&
				Objects.equals(getSeqNo(), that.getSeqNo());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getCreditInvoiceVoid(), getSeqNo());
	}
}
