package invsys.controllers.suppliermaintanance;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class SupplierLoad {

	private SimpleIntegerProperty supplierCode;
	private SimpleStringProperty supplierName;
	private SimpleStringProperty supCity;
	private SimpleStringProperty supAddress;
	private SimpleStringProperty supCountry;
	private SimpleIntegerProperty supMobile;
	private SimpleIntegerProperty supTelephone;
	private SimpleStringProperty supCreateDate;

	public SupplierLoad(int supplierCode, String supplierName, String supCity, String supAddress, String supCountry,
			int supMobile, int supTelephone, String supCreateDate) {

		this.supplierCode = new SimpleIntegerProperty(supplierCode);
		this.supplierName = new SimpleStringProperty(supplierName);
		this.supCity = new SimpleStringProperty(supCity);
		this.supAddress = new SimpleStringProperty(supAddress);
		this.supCountry = new SimpleStringProperty(supCountry);
		this.supMobile = new SimpleIntegerProperty(supMobile);
		this.supTelephone = new SimpleIntegerProperty(supTelephone);
		this.supCreateDate = new SimpleStringProperty(supCreateDate);

	}

	public int getSupplierCode() {
		return supplierCode.get();
	}

	public String getSupplierName() {
		return supplierName.get();
	}

	public String getSupCity() {
		return supCity.get();
	}

	public String getSupAddress() {
		return supAddress.get();
	}

	public String getSupCountry() {
		return supCountry.get();
	}

	public int getSupMobile() {
		return supMobile.get();
	}

	public int getSupTelephone() {
		return supTelephone.get();
	}

	public String getSupCreateDate() {
		return supCreateDate.get();
	}

}
