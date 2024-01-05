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
public class SalesDetailId implements Serializable{
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumns(value = {
			@JoinColumn(name = "bill_no", referencedColumnName = "bill_no"),
			@JoinColumn(name = "user_id", referencedColumnName = "user_id"),
			@JoinColumn(name = "sales_date", referencedColumnName = "sales_date") })
	private Sales sales;
	
	@Column(name = "seq_no")
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof SalesDetailId))
			return false;
		SalesDetailId that = (SalesDetailId) obj;
		return Objects.equals(getSeqNo(), that.getSeqNo()) && Objects.equals(getSales(), that.getSales());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getSales(), getSeqNo());
	}

}
