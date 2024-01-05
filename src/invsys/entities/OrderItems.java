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
import javax.persistence.Transient;

@Entity
@Table(name = "purchase_order_det")
public class OrderItems implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "orddet_id")
	private long orderDetId;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "order_id", nullable = false)
	private PurchaseOrder order;

	@Column(name = "seq_no", nullable =false)
	private int seQNo;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "prd_id" , nullable = false)
	private Products product;

	@Column(name = "prd_cost", nullable = false)
	private double prductCost;

	@Column(name = "order_Qty", nullable = false)
	private double orderQty;

	@Column(name = "delivered_qty")
	private double deliveredQty;

	@Column(name = "item_vat",  nullable = false)
	private double itemVat;

	@Column(name = "item_amount",  nullable = false)
	private double itemAmount;

	@Column(name = "item_discount")
	private double itemDiscount;

	@Column(name="partial_delivery_status")
	private boolean partialDeliveryStatus;
	
	@Column(name="delivery_status")
	private boolean deliveryStatus;
	
	@Transient
	private String itemCode;
	
	@Transient 
	private String itemDesc;
	
	
	public OrderItems() {

	}

	public OrderItems(long orderDetId, PurchaseOrder order, int seQNo, Products product, double prductCost, int orderQty,
			int deliveredQty, double itemVat, double itemAmount, double itemDiscount) {
		super();
		this.orderDetId = orderDetId;
		this.order = order;
		this.seQNo = seQNo;
		this.product = product;
		this.prductCost = prductCost;
		this.orderQty = orderQty;
		this.deliveredQty = deliveredQty;
		this.itemVat = itemVat;
		this.itemAmount = itemAmount;
		this.itemDiscount = itemDiscount;
	}

	public long getOrderDetId() {
		return orderDetId;
	}

	public PurchaseOrder getOrder() {
		return order;
	}

	public int getSeQNo() {
		return seQNo;
	}

	public Products getProduct() {
		return product;
	}

	public double getPrductCost() {
		return prductCost;
	}

	

	public double getDeliveredQty() {
		return deliveredQty;
	}

	public double getItemVat() {
		return itemVat;
	}

	public double getItemAmount() {
		return itemAmount;
	}

	public double getItemDiscount() {
		return itemDiscount;
	}

	public void setOrderDetId(long orderDetId) {
		this.orderDetId = orderDetId;
	}

	public void setOrder(PurchaseOrder order) {
		this.order = order;
	}

	public void setSeQNo(int seQNo) {
		this.seQNo = seQNo;
	}

	public void setProduct(Products product) {
		this.product = product;
	}

	public void setPrductCost(double prductCost) {
		this.prductCost = prductCost;
	}

	
	public void setDeliveredQty(double deliveredQty) {
		this.deliveredQty = deliveredQty;
	}

	public void setItemVat(double itemVat) {
		this.itemVat = itemVat;
	}

	public void setItemAmount(double itemAmount) {
		this.itemAmount = itemAmount;
	}

	public void setItemDiscount(double itemDiscount) {
		this.itemDiscount = itemDiscount;
	}
	
	public String getItemCode() {
		itemCode = String.valueOf(product.getPrd_id());
		return itemCode;
	}

	public String getItemDesc() {
		itemDesc = product.getP_name();
		return itemDesc;
	}
	
	
	public boolean isPartialDeliveryStatus() {
		return partialDeliveryStatus;
	}

	public void setPartialDeliveryStatus(boolean partialDeliveryStatus) {
		this.partialDeliveryStatus = partialDeliveryStatus;
	}

	public boolean isDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(boolean deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}
	
	
	public double getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(double orderQty) {
		this.orderQty = orderQty;
	}

	@Override
	public String toString() {
		return "OrderItems [orderDetId=" + orderDetId + ", order=" + order + ", seQNo=" + seQNo + ", product=" + product
				+ ", prductCost=" + prductCost + ", orderQty=" + orderQty + ", deliveredQty=" + deliveredQty
				+ ", itemVat=" + itemVat + ", itemAmount=" + itemAmount + ", itemDiscount=" + itemDiscount
				+ ", partialDeliveryStatus=" + partialDeliveryStatus + ", deliveryStatus=" + deliveryStatus
				+ ", itemCode=" + itemCode + ", itemDesc=" + itemDesc + "]";
	}

	

}
