package invsys.entities.compositkeys;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

import invsys.entities.Sales;

@Embeddable
public class POSPayDetailId implements Serializable {

	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumns(value = {
			@JoinColumn(name = "bill_no", referencedColumnName = "bill_no"),
			@JoinColumn(name = "user_id", referencedColumnName = "user_id"),
			@JoinColumn(name = "sales_date", referencedColumnName = "sales_date") })
	private Sales sales;
	
	@Column(name="doc_type")
	private String docType;
	
	@Column(name="seq_no")
	private int seqNo;
	
	public Sales getSales() {
		return sales;
	}

	public void setSales(Sales sales) {
		this.sales = sales;
	}

	public int getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
	
	
	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof POSPayDetailId))
			return false;
		POSPayDetailId that = (POSPayDetailId) obj;
		return Objects.equals(getSeqNo(), that.getSeqNo()) && Objects.equals(getSales(), that.getSales())
				&& Objects.equals(getDocType(), that.getDocType());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getSales(), getDocType(),getSeqNo());
	}
}
