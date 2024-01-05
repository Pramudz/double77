package invsys.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="supplier_payment_detail")
public class SupplierPaymentDetail {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="pay_det_id")
	private long paymentDetid;
	
	
	@ManyToOne
	@JoinColumn(name="payment_id" , referencedColumnName = "payment_id" , nullable = false)
	private SupplierPayments supplierPayments;
	
	
	@Column(name="grn_ron_id")
	private long grnRonId;
	
	@Column(name="doc_amount" , nullable = false)
	private double documentAmount;
	
	
	@Column(name="document_type")
	private String documentType;
	
	
	// to maintain table view
	@Transient
	private java.sql.Date documentRaisedDate;
	
	
	public SupplierPaymentDetail() {
		
	}


	public long getPaymentDetid() {
		return paymentDetid;
	}


	public void setPaymentDetid(long paymentDetid) {
		this.paymentDetid = paymentDetid;
	}


	public SupplierPayments getSupplierPayments() {
		return supplierPayments;
	}


	public void setSupplierPayments(SupplierPayments supplierPayments) {
		this.supplierPayments = supplierPayments;
	}


	public long getGrnRonId() {
		return grnRonId;
	}


	public void setGrnRonId(long grnRonId) {
		this.grnRonId = grnRonId;
	}


	public double getDocumentAmount() {
		return documentAmount;
	}


	public void setDocumentAmount(double documentAmount) {
		this.documentAmount = documentAmount;
	}


	public String getDocumentType() {
		return documentType;
	}


	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}


	public java.sql.Date getDocumentRaisedDate() {
		return documentRaisedDate;
	}


	public void setDocumentRaisedDate(java.sql.Date documentRaisedDate) {
		this.documentRaisedDate = documentRaisedDate;
	}


	@Override
	public String toString() {
		return "SupplierPaymentDetail [paymentDetid=" + paymentDetid + ", supplierPayments=" + supplierPayments
				+ ", grnRonId=" + grnRonId + ", documentAmount=" + documentAmount + ", documentType=" + documentType
				+ "]";
	}
	
	
}
