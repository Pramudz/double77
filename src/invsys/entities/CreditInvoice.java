package invsys.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import invsys.entities.compositkeys.CreditInvoiceId;

@Entity
@Table(name="credit_invoice")
public class CreditInvoice implements Serializable {

	@EmbeddedId
	private CreditInvoiceId creditInvoiceId;
			
	@ManyToOne
	@JoinColumn(name= "customer_id")
	private Customer customer;
	
	@Column(name="customer_name")
	private String customerName;
	
	@Column(name="if_advance_payment")
	private double ifAdvancePayment;
	
	@Column(name ="invoice_gross_amount" , nullable = false)
	private double invoiceGrossAmount;
	
	@Column(name="invoice_net_amount" , nullable = false)
	private double invoiceNetAmount;
	
	@Column(name="invoice_discount")
	private double invoiceDiscount;
	
	@Column(name="credit_period" , nullable = false)
	private int creditPeriod;
	
	@Column(name="expired_date" , nullable = false)
	private java.sql.Date expiredDate;
	
	@Column(name="settled_status")
	private boolean settledStatus;
		
	@Column(name="settled_amount")
	private double settledAmount;
	
	@ManyToOne
	@JoinColumn(name="settled_by")
	private Users settledBy;

	@Column(name="refund_status" , columnDefinition = "boolean default false")
	private boolean refundStatus;

	@Column(name="settled_date")
	private java.sql.Date settledDate;

	
	public CreditInvoice() {
		
	}

	public CreditInvoiceId getCreditInvoiceId() {
		return creditInvoiceId;
	}

	public void setCreditInvoiceId(CreditInvoiceId creditInvoiceId) {
		this.creditInvoiceId = creditInvoiceId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public double getIfAdvancePayment() {
		return ifAdvancePayment;
	}

	public void setIfAdvancePayment(double ifAdvancePayment) {
		this.ifAdvancePayment = ifAdvancePayment;
	}

	public double getInvoiceGrossAmount() {
		return invoiceGrossAmount;
	}

	public void setInvoiceGrossAmount(double invoiceGrossAmount) {
		this.invoiceGrossAmount = invoiceGrossAmount;
	}

	public double getInvoiceNetAmount() {
		return invoiceNetAmount;
	}

	public void setInvoiceNetAmount(double invoiceNetAmount) {
		this.invoiceNetAmount = invoiceNetAmount;
	}

	public double getInvoiceDiscount() {
		return invoiceDiscount;
	}

	public void setInvoiceDiscount(double invoiceDiscount) {
		this.invoiceDiscount = invoiceDiscount;
	}

	public int getCreditPeriod() {
		return creditPeriod;
	}

	public void setCreditPeriod(int creditPeriod) {
		this.creditPeriod = creditPeriod;
	}

	public java.sql.Date getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(java.sql.Date expiredDate) {
		this.expiredDate = expiredDate;
	}

	public boolean isSettledStatus() {
		return settledStatus;
	}

	public void setSettledStatus(boolean settledStatus) {
		this.settledStatus = settledStatus;
	}

	public double getSettledAmount() {
		return settledAmount;
	}

	public void setSettledAmount(double settledAmount) {
		this.settledAmount = settledAmount;
	}
	
		
	public boolean isRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(boolean refundStatus) {
		this.refundStatus = refundStatus;
	}
	
	public Users getSettledBy() {
		return settledBy;
	}

	public void setSettledBy(Users settledBy) {
		this.settledBy = settledBy;
	}
	
	
	public java.sql.Date getSettledDate() {
		return settledDate;
	}

	public void setSettledDate(java.sql.Date settledDate) {
		this.settledDate = settledDate;
	}

	@Override
	public String toString() {
		return "CreditInvoice [creditInvoiceId=" + creditInvoiceId + ", customer=" + customer + ", customerName="
				+ customerName + ", ifAdvancePayment=" + ifAdvancePayment + ", invoiceGrossAmount=" + invoiceGrossAmount
				+ ", invoiceNetAmount=" + invoiceNetAmount + ", invoiceDiscount=" + invoiceDiscount + ", creditPeriod="
				+ creditPeriod + ", expiredDate=" + expiredDate + ", settledStatus=" + settledStatus
				+ ", settledAmount=" + settledAmount + "]";
	}
	
	
	
	
}
