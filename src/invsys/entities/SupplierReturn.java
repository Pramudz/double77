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
@Table(name="supplier_return_mass")
public class SupplierReturn implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="sup_return_id")
	private long supReturnId;
	
	@ManyToOne
	@JoinColumn(name = "sup_id", referencedColumnName = "sup_id", nullable = false)
	private Supplier supplier;
	
	@ManyToOne
	@JoinColumn(name="user_id" , referencedColumnName = "user_id" , nullable = false)
	private Users user;
	
	@Column(name="return_date" , nullable = false)
	private java.sql.Date returnDate;
	
	@Column(name="return_amount" , nullable =false)
	private double returnAmount;
	
	@Column(name="payment_utilized_status")
	private boolean paymentUtilizedStatus;
	
	
	@Column(name="document_type" , nullable =false)
	private String documentType;
	
	public SupplierReturn() {
		
	}


	public long getSupReturnId() {
		return supReturnId;
	}


	public void setSupReturnId(long supReturnId) {
		this.supReturnId = supReturnId;
	}


	public Supplier getSupplier() {
		return supplier;
	}


	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}


	public Users getUser() {
		return user;
	}


	public void setUser(Users user) {
		this.user = user;
	}


	public java.sql.Date getReturnDate() {
		return returnDate;
	}


	public void setReturnDate(java.sql.Date returnDate) {
		this.returnDate = returnDate;
	}


	public double getReturnAmount() {
		return returnAmount;
	}


	public void setReturnAmount(double returnAmount) {
		this.returnAmount = returnAmount;
	}


	public boolean isPaymentUtilizedStatus() {
		return paymentUtilizedStatus;
	}


	public void setPaymentUtilizedStatus(boolean paymentUtilizedStatus) {
		this.paymentUtilizedStatus = paymentUtilizedStatus;
	}


	public String getDocumentType() {
		return documentType;
	}


	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	
	
	
}
