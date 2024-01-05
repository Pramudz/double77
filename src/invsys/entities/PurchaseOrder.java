package invsys.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "purchase_order")
public class PurchaseOrder implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id", length = 11)
	private long orderId;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "user_id", nullable = false)
	private Users user;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "sup_id", nullable = false)
	private Supplier supplier;

	@Column(name = "order_date", nullable = false)
	private java.sql.Date date;

	@Column(name = "order_amount", nullable = false)
	private double orderAmount;

	@Column(name = "expire_date", nullable = false)
	private java.sql.Date expireDate;

	@Column(name = "delivery_date", nullable = true)
	private java.sql.Date delDate;

	@Column(name = "ship_street_address", length = 30)
	private String shipStreetAddress;

	@Column(name = "ship_address_line_01", length = 30)
	private String shipAddressLine01;

	@Column(name = "ship_city", length = 30)
	private String shipCity;

	@Column(name = "partialy_del_status")
	private boolean partialStatus;

	@Column(name = "delivery_status")
	private boolean deliverStatus;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "approved_by")
	private Users approvedBy;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "deleted_by")
	private Users deletedBy;

	public PurchaseOrder() {

	}


	public long getOrderId() {
		return orderId;
	}


	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}


	public Users getUser() {
		return user;
	}


	public void setUser(Users user) {
		this.user = user;
	}


	public Supplier getSupplier() {
		return supplier;
	}


	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}


	public java.sql.Date getDate() {
		return date;
	}


	public void setDate(java.sql.Date date) {
		this.date = date;
	}


	public double getOrderAmount() {
		return orderAmount;
	}


	public void setOrderAmount(double orderAmount) {
		this.orderAmount = orderAmount;
	}


	public java.sql.Date getExpireDate() {
		return expireDate;
	}


	public void setExpireDate(java.sql.Date expireDate) {
		this.expireDate = expireDate;
	}


	public java.sql.Date getDelDate() {
		return delDate;
	}


	public void setDelDate(java.sql.Date delDate) {
		this.delDate = delDate;
	}


	public String getShipStreetAddress() {
		return shipStreetAddress;
	}


	public void setShipStreetAddress(String shipStreetAddress) {
		this.shipStreetAddress = shipStreetAddress;
	}


	public String getShipAddressLine01() {
		return shipAddressLine01;
	}


	public void setShipAddressLine01(String shipAddressLine01) {
		this.shipAddressLine01 = shipAddressLine01;
	}


	public String getShipCity() {
		return shipCity;
	}


	public void setShipCity(String shipCity) {
		this.shipCity = shipCity;
	}


	public boolean isPartialStatus() {
		return partialStatus;
	}


	public void setPartialStatus(boolean partialStatus) {
		this.partialStatus = partialStatus;
	}


	public boolean isDeliverStatus() {
		return deliverStatus;
	}


	public void setDeliverStatus(boolean deliverStatus) {
		this.deliverStatus = deliverStatus;
	}


	public Users getApprovedBy() {
		return approvedBy;
	}


	public void setApprovedBy(Users approvedBy) {
		this.approvedBy = approvedBy;
	}

	
	public Users getDeletedBy() {
		return deletedBy;
	}


	public void setDeletedBy(Users deletedBy) {
		this.deletedBy = deletedBy;
	}


	@Override
	public String toString() {
		return "PurchaseOrder [orderId=" + orderId + ", user=" + user + ", supplier=" + supplier + ", date=" + date
				+ ", orderAmount=" + orderAmount + ", expireDate=" + expireDate + ", delDate=" + delDate
				+ ", shipStreetAddress=" + shipStreetAddress + ", shipAddressLine01=" + shipAddressLine01
				+ ", shipCity=" + shipCity + ", partialStatus=" + partialStatus + ", deliverStatus=" + deliverStatus
				+ ", approvedBy=" + approvedBy + "]";
	}


	

}
