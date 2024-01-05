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
@Table(name ="good_received_mass")
public class GoodReceived implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="grn_id")
	private long grnId;
	
	@ManyToOne
	@JoinColumn(name="order_id" , referencedColumnName = "order_id" , nullable = false)
	private PurchaseOrder purchaseOrder;
	
	@ManyToOne
	@JoinColumn(name="sup_id" , referencedColumnName = "sup_id" , nullable = false)
	private Supplier supplier;
	
	@ManyToOne
	@JoinColumn(name="user_id" , referencedColumnName = "user_id" , nullable = false)
	private Users user;
	
	@Column(name="grn_date" , nullable = false)
	private java.sql.Date grnDate;
	
	@Column(name="grn_amount" , nullable =false)
	private double grnAmount;
	
	@ManyToOne
	@JoinColumn(name="apprved_by" , referencedColumnName = "user_id")
	private Users grnApprovedBy;
	
	@Column(name="apprved_date")
	private java.sql.Date approvedDate;
	
	@Column(name="invoice_no" , nullable =false)
	private String invoiceNum;
	
	@Column(name="paid_status")
	private boolean paidStatus;
	
	@Column(name="document_type")
	private String documentType;

	public GoodReceived() {
	
	}

	public long getGrnId() {
		return grnId;
	}

	public void setGrnId(long grnId) {
		this.grnId = grnId;
	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public java.sql.Date getGrnDate() {
		return grnDate;
	}

	public void setGrnDate(java.sql.Date grnDate) {
		this.grnDate = grnDate;
	}

	public double getGrnAmount() {
		return grnAmount;
	}

	public void setGrnAmount(double grnAmount) {
		this.grnAmount = grnAmount;
	}

	public Users getGrnApprovedBy() {
		return grnApprovedBy;
	}

	public void setGrnApprovedBy(Users grnApprovedBy) {
		this.grnApprovedBy = grnApprovedBy;
	}

	public java.sql.Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(java.sql.Date approvedDate) {
		this.approvedDate = approvedDate;
	}
	
	
	public String getInvoiceNum() {
		return invoiceNum;
	}

	public void setInvoiceNum(String invoiceNum) {
		this.invoiceNum = invoiceNum;
	}

	
	public boolean isPaidStatus() {
		return paidStatus;
	}

	public void setPaidStatus(boolean paidStatus) {
		this.paidStatus = paidStatus;
	}
	
	
	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	
	
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	@Override
	public String toString() {
		return "GoodReceived [grnId=" + grnId + ", purchaseOrder=" + purchaseOrder + ", user=" + user + ", grnDate="
				+ grnDate + ", grnAmount=" + grnAmount + ", grnApprovedBy=" + grnApprovedBy + ", approvedDate="
				+ approvedDate + "]";
	}
	
	
	
	
	
	
	
	
	
}
