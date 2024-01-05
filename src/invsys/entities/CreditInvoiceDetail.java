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

import invsys.entities.compositkeys.CreditInvoiceDetailId;

@Entity
@Table(name ="credit_invoice_detail")
public class CreditInvoiceDetail implements Serializable {


	@EmbeddedId
	private CreditInvoiceDetailId creditInvoiceId;

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
			
	@Column(name = "item_net_amount", nullable =false)
	private double itemNetAmount;

	public CreditInvoiceDetail() {
		
	}

	public CreditInvoiceDetailId getCreditInvoiceId() {
		return creditInvoiceId;
	}

	public void setCreditInvoiceId(CreditInvoiceDetailId creditInvoiceId) {
		this.creditInvoiceId = creditInvoiceId;
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

	
	public double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	
	public double getDiscountedPrice() {
		return discountedPrice;
	}

	public void setDiscountedPrice(double discountedPrice) {
		this.discountedPrice = discountedPrice;
	}

	@Override
	public String toString() {
		return "CreditInvoiceDetail [creditInvoiceId=" + creditInvoiceId + ", seqNo=" + seqNo + ", product=" + product
				+ ", unitPrice=" + unitPrice + ", costPrice=" + costPrice + ", averageCostPrice=" + averageCostPrice
				+ ", itemVat=" + itemVat + ", salesQty=" + salesQty + ", discount=" + discount + ", itemGrossAmount="
				+ itemGrossAmount + ", itemNetAmount=" + itemNetAmount + "]";
	}
	
	
	
	
	
}
