package invsys.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="customer_refunds")
public class CustomerRefunds implements Serializable {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="refund_id")
	private long refundId;
	
	@ManyToOne
	@JoinColumn(name="refunded_by", referencedColumnName = "user_id" , nullable =false)
	private Users refundedBy;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumns(value = {
			@JoinColumn(name = "bill_no", referencedColumnName = "bill_no"),
			@JoinColumn(name = "user_id", referencedColumnName = "user_id"),
			@JoinColumn(name = "sales_date", referencedColumnName = "sales_date") })
	private Sales sales;
	
	
	@Column(name="refund_date")
	private java.sql.Date refundDate;
	
	@Column(name ="gross_refund_amount" , nullable=false)
	private double grossRefundAmount;
	
	@Column(name = "net_refund_amount", nullable = false)
	private double netRefundAmount;
	
	@Column(name="refund_discount_from_bill")
	private double refundDiscountFromBill;

	public long getRefundId() {
		return refundId;
	}

	public void setRefundId(long refundId) {
		this.refundId = refundId;
	}

	public Users getRefundedBy() {
		return refundedBy;
	}

	public void setRefundedBy(Users refundedBy) {
		this.refundedBy = refundedBy;
	}

	public Sales getSales() {
		return sales;
	}

	public void setSales(Sales sales) {
		this.sales = sales;
	}

	public java.sql.Date getRefundDate() {
		return refundDate;
	}

	public void setRefundDate(java.sql.Date refundDate) {
		this.refundDate = refundDate;
	}

	public double getGrossRefundAmount() {
		return grossRefundAmount;
	}

	public void setGrossRefundAmount(double grossRefundAmount) {
		this.grossRefundAmount = grossRefundAmount;
	}

	public double getNetRefundAmount() {
		return netRefundAmount;
	}

	public void setNetRefundAmount(double netRefundAmount) {
		this.netRefundAmount = netRefundAmount;
	}

	public double getRefundDiscountFromBill() {
		return refundDiscountFromBill;
	}

	public void setRefundDiscountFromBill(double refundDiscountFromBill) {
		this.refundDiscountFromBill = refundDiscountFromBill;
	}

	@Override
	public String toString() {
		return "CustomerRefunds [refundId=" + refundId + ", refundedBy=" + refundedBy + ", sales=" + sales
				+ ", refundDate=" + refundDate + ", grossRefundAmount=" + grossRefundAmount + ", netRefundAmount="
				+ netRefundAmount + ", refundDiscountFromBill=" + refundDiscountFromBill + "]";
	}
	
	
	
}
