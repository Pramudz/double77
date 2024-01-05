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
@Table(name ="supplier_return_det")
public class SupplierReturnDetail implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "return_det_id")
	private long returnDetId;

	@ManyToOne
	@JoinColumn(name = "sup_return_id", referencedColumnName = "sup_return_id", nullable = false)
	private SupplierReturn supplierReturn;

	@ManyToOne
	@JoinColumn(name = "prd_id" , referencedColumnName = "prd_id", nullable = false)
	private Products product;
	
	@Column(name="seqNo")
	private int seqNo;
	
	@Column(name="prd_name")
	private String productName;

	@Column(name = "prd_cost", nullable = false)
	private double prductCost;

	@Column(name = "return_qty",nullable = false)
	private double returnQty;

	@Column(name = "item_vat",  nullable = false)
	private double itemVat;

	@Column(name = "prd_amount",  nullable = false)
	private double prdAmount;

	@Column(name="reason")
	private String reason;
	
	public SupplierReturnDetail() {
	
	}
	
	
	public int getSeqNo() {
		return seqNo;
	}


	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}


	public long getReturnDetId() {
		return returnDetId;
	}

	public void setReturnDetId(long returnDetId) {
		this.returnDetId = returnDetId;
	}

	public SupplierReturn getSupplierReturn() {
		return supplierReturn;
	}

	public void setSupplierReturn(SupplierReturn supplierReturn) {
		this.supplierReturn = supplierReturn;
	}

	public Products getProduct() {
		return product;
	}

	public void setProduct(Products product) {
		this.product = product;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getPrductCost() {
		return prductCost;
	}

	public void setPrductCost(double prductCost) {
		this.prductCost = prductCost;
	}

	public double getReturnQty() {
		return returnQty;
	}

	public void setReturnQty(double returnQty) {
		this.returnQty = returnQty;
	}

	public double getItemVat() {
		return itemVat;
	}

	public void setItemVat(double itemVat) {
		this.itemVat = itemVat;
	}

	public double getPrdAmount() {
		return prdAmount;
	}

	public void setPrdAmount(double prdAmount) {
		this.prdAmount = prdAmount;
	}


	public String getReason() {
		return reason;
	}


	public void setReason(String reason) {
		this.reason = reason;
	}

	
}
