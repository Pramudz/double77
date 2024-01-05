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
@Table(name="credit_invoice_void")
public class CreditInvoiceVoid implements Serializable {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="void_id")
	private long void_id;
	
	@ManyToOne
	@JoinColumn(name="voided_by", referencedColumnName = "user_id" , nullable =false)
	private Users voidedBy;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumns(value = {
			@JoinColumn(name = "invoice_id", referencedColumnName = "invoice_id"),
			@JoinColumn(name = "user_id", referencedColumnName = "user_id"),
			@JoinColumn(name = "invoice_date", referencedColumnName = "invoice_date") })
	private CreditInvoice creditInvoice;
	
	
	@Column(name="void_date")
	private java.sql.Date voidDate;
	
	@Column(name ="gross_void_amount" , nullable=false)
	private double grossVoidAmount;
	
	@Column(name = "net_void_amount", nullable = false)
	private double netVoidAmount;
	
	@Column(name="void_discount_from_invoice")
	private double voidDiscountFromInvoice;

	public long getVoid_id() {
		return void_id;
	}

	public void setVoid_id(long void_id) {
		this.void_id = void_id;
	}

	public Users getVoidedBy() {
		return voidedBy;
	}

	public void setVoidedBy(Users voided_by) {
		this.voidedBy = voided_by;
	}

	public CreditInvoice getCreditInvoice() {
		return creditInvoice;
	}

	public void setCreditInvoice(CreditInvoice creditInvoice) {
		this.creditInvoice = creditInvoice;
	}

	public java.sql.Date getVoidDate() {
		return voidDate;
	}

	public void setVoidDate(java.sql.Date voidDate) {
		this.voidDate = voidDate;
	}

	public double getGrossVoidAmount() {
		return grossVoidAmount;
	}

	public void setGrossVoidAmount(double grossVoidAmount) {
		this.grossVoidAmount = grossVoidAmount;
	}

	public double getNetVoidAmount() {
		return netVoidAmount;
	}

	public void setNetVoidAmount(double netVoidAmount) {
		this.netVoidAmount = netVoidAmount;
	}

	public double getVoidDiscountFromInvoice() {
		return voidDiscountFromInvoice;
	}

	public void setVoidDiscountFromInvoice(double voidDiscountFromInvoice) {
		this.voidDiscountFromInvoice = voidDiscountFromInvoice;
	}

	@Override
	public String toString() {
		return "CreditInvoiceVoid [void_id=" + void_id + ", voided_by=" + voidedBy + ", creditInvoice=" + creditInvoice
				+ ", voidDate=" + voidDate + ", grossVoidAmount=" + grossVoidAmount + ", netVoidAmount=" + netVoidAmount
				+ ", voidDiscountFromInvoice=" + voidDiscountFromInvoice + "]";
	}
	
	
}
