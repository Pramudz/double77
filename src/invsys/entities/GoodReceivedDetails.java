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
@Table(name ="good_received_details")
public class GoodReceivedDetails implements Serializable {

	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name="grn_det_id")
	private long grnDetId;
	
	@ManyToOne
	@JoinColumn(name="order_id" , referencedColumnName = "order_id" , nullable = false)
	private PurchaseOrder purchaseOrder;
	
	@ManyToOne
	@JoinColumn(name="grn_id" , referencedColumnName = "grn_id" , nullable = false)
	private GoodReceived goodRecevied;
	
	
	@Column(name="seq_no")
	private int seqNo;
	
	@ManyToOne
	@JoinColumn(name="prd_id" , referencedColumnName = "prd_id" , nullable = false)
	private Products product;
	
	
	@Column(name="cost_price", nullable =false)
	private double costPrice;
	
	@Column(name="unit_price", nullable =false)
	private double unitPrice;
	
	
	@Column(name="ordered_qty", nullable =false)
	private double orderedQty;
	
	@Column(name="received_qty", nullable =false)
	private double receivedQty;
	
	@Column(name="grn_item_amount", nullable =false)
	private double grnItemAmount;

	public GoodReceivedDetails() {
		
	}

	public long getGrnDetId() {
		return grnDetId;
	}

	public void setGrnDetId(long grnDetId) {
		this.grnDetId = grnDetId;
	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
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

	public double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(double costPrice) {
		this.costPrice = costPrice;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getOrderedQty() {
		return orderedQty;
	}

	public void setOrderedQty(double orderedQty) {
		this.orderedQty = orderedQty;
	}

	public double getReceivedQty() {
		return receivedQty;
	}

	public void setReceivedQty(double receivedQty) {
		this.receivedQty = receivedQty;
	}

	public double getGrnItemAmount() {
		return grnItemAmount;
	}

	public void setGrnItemAmount(double grnItemAmount) {
		this.grnItemAmount = grnItemAmount;
	}

	
	public GoodReceived getGoodRecevied() {
		return goodRecevied;
	}

	public void setGoodRecevied(GoodReceived goodRecevied) {
		this.goodRecevied = goodRecevied;
	}

	@Override
	public String toString() {
		return "GoodReceivedDetails [grnDetId=" + grnDetId + ", purchaseOrder=" + purchaseOrder + ", seqNo=" + seqNo
				+ ", product=" + product + ", costPrice=" + costPrice + ", unitPrice=" + unitPrice + ", orderedQty="
				+ orderedQty + ", receivedQty=" + receivedQty + ", grnItemAmount=" + grnItemAmount + "]";
	}
	
		
	
}
