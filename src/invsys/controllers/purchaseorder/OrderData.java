package invsys.controllers.purchaseorder;

import java.io.Serializable;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class OrderData implements Serializable {

	private SimpleStringProperty itemCode;
	private SimpleStringProperty itemDesc;
	private SimpleIntegerProperty qty;
	private SimpleDoubleProperty amount;
	private SimpleDoubleProperty price;

	public OrderData(String itemCode, String itemDesc, int qty, double price, double amount) {

		this.itemCode = new SimpleStringProperty(itemCode);
		this.itemDesc = new SimpleStringProperty(itemDesc);
		this.qty = new SimpleIntegerProperty(qty);
		this.amount = new SimpleDoubleProperty(amount);
		this.price = new SimpleDoubleProperty(price);

	}

	public String getItemCode() {
		return itemCode.get();
	}

	public String getItemDesc() {
		return itemDesc.get();
	}

	public int getQty() {
		return qty.get();
	}

	public double getAmount() {
		return amount.get();
	}

	public double getPrice() {
		return price.get();
	}

	public void setQty(int qty) {
		this.qty.set(qty);
	}

	public void setAmount(double amount) {
		this.amount.set(amount);
	}

}