package invsys.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="supplier_payments")
public class SupplierPayments implements Serializable {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="payment_id")
	private long paymentId;
	
	
	@ManyToOne
	@JoinColumn(name="supplier_id" , referencedColumnName = "sup_id" , nullable = false)
	private Supplier supplier;
	
	
	@Column(name="supplier_name")
	private String supplierName;
	
	
	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName = "user_id", nullable = false)
	private Users user;
	
	@Column(name="date_of_payment" , nullable = false)
	private java.sql.Date dateOfPayment;
	
	
	@ManyToOne
	@JoinColumn(name="payModId" , referencedColumnName = "pmode_id" , nullable = false)
	private PayModes payMode;
	
	
	@Column(name="amount_paid" , nullable = false)
	private double amountPaid;
	
	
	@Column(name="document_type")
	private String documentType;


	public SupplierPayments() {
	
	}


	public long getPaymentId() {
		return paymentId;
	}


	public void setPaymentId(long paymentId) {
		this.paymentId = paymentId;
	}


	public Supplier getSupplier() {
		return supplier;
	}


	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}


	public String getSupplierName() {
		return supplierName;
	}


	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}


	public Users getUser() {
		return user;
	}


	public void setUser(Users user) {
		this.user = user;
	}


	public java.sql.Date getDateOfPayment() {
		return dateOfPayment;
	}


	public void setDateOfPayment(java.sql.Date dateOfPayment) {
		this.dateOfPayment = dateOfPayment;
	}


	public PayModes getPayMode() {
		return payMode;
	}


	public void setPayMode(PayModes payMode) {
		this.payMode = payMode;
	}


	public double getAmountPaid() {
		return amountPaid;
	}


	public void setAmountPaid(double amountPaid) {
		this.amountPaid = amountPaid;
	}


	public String getDocumentType() {
		return documentType;
	}


	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}


	@Override
	public String toString() {
		return "SupplierPayments [paymentId=" + paymentId + ", supplier=" + supplier + ", supplierName=" + supplierName
				+ ", user=" + user + ", dateOfPayment=" + dateOfPayment + ", payMode=" + payMode + ", amountPaid="
				+ amountPaid + ", documentType=" + documentType + "]";
	}
	

}
