package invsys.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import invsys.entities.compositkeys.SalesDetailId;

@Entity
@Table(name = "sales_detail")
public class SalesDetails {

	@EmbeddedId
	private SalesDetailId salesDetId;

	@Transient
	private int seqNo;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "product_id", nullable = false)
	private Products product;

	@Column(name = "unit_price", nullable = false)
	private double unitPrice;

	@Column(name = "cost_price", nullable = false)
	private double costPrice;
	
	@Column(name = "average_cost_price", nullable = false)
	private double averageCostPrice;
	
	@Column(name = "item_vat", nullable = false)
	private double itemVat;
	
	@Column(name = "sales_qty", nullable = false)
	private double salesQty;

	@Column(name = "discount")
	private double discount;
	
	@Column(name = "discount_percentage")
	private double discountPercentage;
	
	@Column(name="discounted_price")
	private double discountedPrice;
	
	@Column(name = "gross_item_amount", nullable =false)
	private double itemGrossAmount;
	
	@Column(name = "item_cancel_status",nullable = false)
	private boolean itemCancelStatus;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name="canceledby", nullable =true)
	private Users canceledBy;
	
	@Column(name = "item_net_amount", nullable =false)
	private double itemAmount;
	
	@Column(name ="warranty_period", nullable=false , columnDefinition =  "varchar(255) default 'No Warranty'")
	private String warrantyPeriod;
	
	@Column(name ="serial_no", nullable=false , columnDefinition =  "varchar(255) default 'No SN'")
	private String serialNo;



	public String getWarrantyPeriod() {
		return warrantyPeriod;
	}


	public void setWarrantyPeriod(String warrantyPeriod) {
		this.warrantyPeriod = warrantyPeriod;
	}


	public String getSerialNo() {
		return serialNo;
	}


	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}


	public SalesDetails() {

	}

	
	public SalesDetailId getSalesDetId() {
		return salesDetId;
	}


	public void setSalesDetId(SalesDetailId salesDetId) {
		this.salesDetId = salesDetId;
	}


	public int getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}

	public Products getProduct() {
		return product;
	}

	public void setProduct(Products product) {
		this.product = product;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(double costPrice) {
		this.costPrice = costPrice;
	}


	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getItemAmount() {
		return itemAmount;
	}

	public void setItemAmount(double itemAmount) {
		this.itemAmount = itemAmount;
	}

	public boolean isItemCancelStatus() {
		return itemCancelStatus;
	}

	public void setItemCancelStatus(boolean itemCancelStatus) {
		this.itemCancelStatus = itemCancelStatus;
	}

	public Users getCanceledBy() {
		return canceledBy;
	}

	public void setCanceledBy(Users canceledBy) {
		this.canceledBy = canceledBy;
	}


	public double getItemGrossAmount() {
		return itemGrossAmount;
	}


	public void setItemGrossAmount(double itemGrossAmount) {
		this.itemGrossAmount = itemGrossAmount;
	}


	public double getAverageCostPrice() {
		return averageCostPrice;
	}


	public void setAverageCostPrice(double averageCostPrice) {
		this.averageCostPrice = averageCostPrice;
	}


	public double getItemVat() {
		return itemVat;
	}


	public void setItemVat(double itemVat) {
		this.itemVat = itemVat;
	}


	public double getDiscountPercentage() {
		return discountPercentage;
	}


	public void setDiscountPercentage(double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}


	public double getSalesQty() {
		return salesQty;
	}


	public void setSalesQty(double salesQty) {
		this.salesQty = salesQty;
	}


	public double getDiscountedPrice() {
		return discountedPrice;
	}


	public void setDiscountedPrice(double discountedPrice) {
		this.discountedPrice = discountedPrice;
	}
	
	
}
