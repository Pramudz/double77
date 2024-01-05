package invsys.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="price_update")
public class PriceUpdate {

	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int priceUpdateId;
	
	@ManyToOne
	@JoinColumn(name="user_id" , nullable = false, referencedColumnName = "user_id")
	private Users user;
	
	
	@ManyToOne
	@JoinColumn(name="prd_id" ,nullable = false,  referencedColumnName = "prd_id")
	private Products product;
	
	@Column(name="revision_date", nullable = false)
	private java.sql.Date revisionDate;
	
	@Column(name="old_pack_cost_price")
	private double oldPackCostPrice;
	
	@Column(name="old_pack_unit_price")
	private double oldPackUnitPrice;
	
	@Column(name ="old_cost_price")
	private double oldCostPrice;
	
	@Column(name ="old_unit_price")
	private double oldUnitPrice;
	
	@Column(name="new_pack_cost_price")
	private double newPackCostPrice;
	
	@Column(name="new_pack_unit_price")
	private double newPackUnitPrice;
	
	@Column(name ="new_cost_price")
	private double newCostPrice;
	
	@Column(name ="new_unit_price")
	private double newUnitPrice;

	public PriceUpdate() {
		
	}

	public int getPriceUpdateId() {
		return priceUpdateId;
	}

	public void setPriceUpdateId(int priceUpdateId) {
		this.priceUpdateId = priceUpdateId;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Products getProduct() {
		return product;
	}

	public void setProduct(Products product) {
		this.product = product;
	}

	public java.sql.Date getRevisionDate() {
		return revisionDate;
	}

	public void setRevisionDate(java.sql.Date revisionDate) {
		this.revisionDate = revisionDate;
	}

	public double getOldPackCostPrice() {
		return oldPackCostPrice;
	}

	public void setOldPackCostPrice(double oldPackCostPrice) {
		this.oldPackCostPrice = oldPackCostPrice;
	}

	public double getOldPackUnitPrice() {
		return oldPackUnitPrice;
	}

	public void setOldPackUnitPrice(double oldPackUnitPrice) {
		this.oldPackUnitPrice = oldPackUnitPrice;
	}

	public double getOldCostPrice() {
		return oldCostPrice;
	}

	public void setOldCostPrice(double oldCostPrice) {
		this.oldCostPrice = oldCostPrice;
	}

	public double getOldUnitPrice() {
		return oldUnitPrice;
	}

	public void setOldUnitPrice(double oldUnitPrice) {
		this.oldUnitPrice = oldUnitPrice;
	}

	public double getNewPackCostPrice() {
		return newPackCostPrice;
	}

	public void setNewPackCostPrice(double newPackCostPrice) {
		this.newPackCostPrice = newPackCostPrice;
	}

	public double getNewPackUnitPrice() {
		return newPackUnitPrice;
	}

	public void setNewPackUnitPrice(double newPackUnitPrice) {
		this.newPackUnitPrice = newPackUnitPrice;
	}

	public double getNewCostPrice() {
		return newCostPrice;
	}

	public void setNewCostPrice(double newCostPrice) {
		this.newCostPrice = newCostPrice;
	}

	public double getNewUnitPrice() {
		return newUnitPrice;
	}

	public void setNewUnitPrice(double newUnitPrice) {
		this.newUnitPrice = newUnitPrice;
	}

	@Override
	public String toString() {
		return "PriceUpdate [priceUpdateId=" + priceUpdateId + ", user=" + user + ", product=" + product
				+ ", revisionDate=" + revisionDate + ", oldPackCostPrice=" + oldPackCostPrice + ", oldPackUnitPrice="
				+ oldPackUnitPrice + ", oldCostPrice=" + oldCostPrice + ", oldUnitPrice=" + oldUnitPrice
				+ ", newPackCostPrice=" + newPackCostPrice + ", newPackUnitPrice=" + newPackUnitPrice
				+ ", newCostPrice=" + newCostPrice + ", newUnitPrice=" + newUnitPrice + "]";
	}
	
}
