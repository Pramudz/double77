package invsys.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import invsys.entities.compositkeys.CreditInvoiceVoidDetId;


@Entity
@Table(name="credit_invoice_void_detail")
public class CreditInvoiceVoidDetail implements Serializable {

	@EmbeddedId
	private CreditInvoiceVoidDetId creditInvoiceVoidDetId;
	
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
	private double salesQty;

	@Column(name = "discount")
	private double discount;
	
	@Column(name = "discount_percentage")
	private double discountPercentage;
	
	@Column(name = "gross_item_amount", nullable =false)
	private double itemGrossAmount;
			
	@Column(name = "item_net_amount", nullable =false)
	private double itemNetAmount;

	public CreditInvoiceVoidDetId getCreditInvoiceVoidDetId() {
		return creditInvoiceVoidDetId;
	}

	public void setCreditInvoiceVoidDetId(CreditInvoiceVoidDetId creditInvoiceVoidDetId) {
		this.creditInvoiceVoidDetId = creditInvoiceVoidDetId;
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

	public double getSalesQty() {
		return salesQty;
	}

	public void setSalesQty(double salesQty) {
		this.salesQty = salesQty;
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

	@Override
	public String toString() {
		return "CreditInvoiceVoidDetail [creditInvoiceVoidDetId=" + creditInvoiceVoidDetId + ", seqNo=" + seqNo
				+ ", product=" + product + ", unitPrice=" + unitPrice + ", discountedunitPrice=" + discountedunitPrice
				+ ", costPrice=" + costPrice + ", averageCostPrice=" + averageCostPrice + ", itemVat=" + itemVat
				+ ", salesQty=" + salesQty + ", discount=" + discount + ", discountPercentage=" + discountPercentage
				+ ", itemGrossAmount=" + itemGrossAmount + ", itemNetAmount=" + itemNetAmount + "]";
	}
	
	

}
