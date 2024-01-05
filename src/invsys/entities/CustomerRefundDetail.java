package invsys.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import invsys.entities.compositkeys.CustomerRefundDetId;

@Entity
@Table(name="customer_refund_detail")
public class CustomerRefundDetail {

	@EmbeddedId
	private CustomerRefundDetId customerRefundDetId;
	
	@Transient
	private int seqNo;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "product_id", nullable = false)
	private Products product;

	@Column(name = "unit_price", nullable = false)
	private double unitPrice;
	
	@Column(name = "discounted_unit_price", nullable = false)
	private double discountedunitPrice;

	@Column(name = "cost_price", nullable = false)
	private double costPrice;
	
	@Column(name = "average_cost_price", nullable = false)
	private double averageCostPrice;
	
	@Column(name = "item_vat", nullable = false)
	private double itemVat;
	
	@Column(name = "sales_qty", nullable = false)
	private double salesQtys;

	@Column(name = "refund_qty", nullable = false)
	private double refundQty;
	
	@Column(name = "discount")
	private double discount;
	
	@Column(name = "discount_percentage")
	private double discountPercentage;
	
	@Column(name = "gross_item_amount", nullable =false)
	private double itemGrossAmount;
			
	@Column(name = "item_net_amount", nullable =false)
	private double itemNetAmount;

	public CustomerRefundDetId getCustomerRefundDetId() {
		return customerRefundDetId;
	}

	public void setCustomerRefundDetId(CustomerRefundDetId customerRefundDetId) {
		this.customerRefundDetId = customerRefundDetId;
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

	public double getDiscountedunitPrice() {
		return discountedunitPrice;
	}

	public void setDiscountedunitPrice(double discountedunitPrice) {
		this.discountedunitPrice = discountedunitPrice;
	}

	public double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(double costPrice) {
		this.costPrice = costPrice;
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

	public double getSalesQtys() {
		return salesQtys;
	}

	public void setSalesQtys(double salesQty) {
		this.salesQtys = salesQty;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public double getItemGrossAmount() {
		return itemGrossAmount;
	}

	public void setItemGrossAmount(double itemGrossAmount) {
		this.itemGrossAmount = itemGrossAmount;
	}

	public double getItemNetAmount() {
		return itemNetAmount;
	}

	public void setItemNetAmount(double itemNetAmount) {
		this.itemNetAmount = itemNetAmount;
	}

	public double getRefundQty() {
		return refundQty;
	}

	public void setRefundQty(double refundQty) {
		this.refundQty = refundQty;
	}

	

}
