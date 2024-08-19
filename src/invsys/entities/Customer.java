package invsys.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="customer")
public class Customer implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="customer_id")
	private long customerId;
	
	@Column(name="first_name")
	private String firstName;
	
	@Column(name="last_name")
	private String lastName;
	
	// Only for double 77 Car Audio in the UI section mention as Vehicle Number
	// Modified on 08th October - Pramud - Ui to be changed
	@Column(name="company_name")
	private String companyName;
	// kanishk 2024-01-07 
        // remove uniqe keys double 7
	@Column(name="nic_number", nullable = true)
	private String nicNumber;
	// kanishk 2024-07-01 
        // remove uniqe keys double 7
	@Column(name="customer_mobile")
	private String customerMobile;
	
	// Only for double 77 Car Audio in the UI section mention as Vehicle Type
	// Modified on 30th December - Pramud - Ui  changed
	@Column(name="telephone")
	private String customerTelephone;
	
        // kanishk 2024-01-07 
        // remove uniqe keys double 7
	@Column(name="customer_email",  nullable = true)
	private String customerEmail;
	
	@Column(name="street_address")
	private String streetAddress;
	
	@Column(name="address_line02")
	private String addressLine02;
	
	@Column(name="city")
	private String city;
	
	@Column(name="customer_points")
	private int customerPoints;
	
	@Column(name="block_status" , columnDefinition = "boolean default false")
	private boolean blockStatus;

	public Customer() {
		
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getNicNumber() {
		return nicNumber;
	}

	public void setNicNumber(String nicNumber) {
		this.nicNumber = nicNumber;
	}

	public String getCustomerMobile() {
		return customerMobile;
	}

	public void setCustomerMobile(String customerMobile) {
		this.customerMobile = customerMobile;
	}

	public String getCustomerTelephone() {
		return customerTelephone;
	}

	public void setCustomerTelephone(String customerTelephone) {
		this.customerTelephone = customerTelephone;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getAddressLine02() {
		return addressLine02;
	}

	public void setAddressLine02(String addressLine02) {
		this.addressLine02 = addressLine02;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getCustomerPoints() {
		return customerPoints;
	}

	public void setCustomerPoints(int customerPoints) {
		this.customerPoints = customerPoints;
	}

	
	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}
	

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	
	public boolean isBlockStatus() {
		return blockStatus;
	}

	public void setBlockStatus(boolean blockStatus) {
		this.blockStatus = blockStatus;
	}

	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", nicNumber=" + nicNumber + ", customerMobile=" + customerMobile + ", customerTelephone="
				+ customerTelephone + ", streetAddress=" + streetAddress + ", addressLine02=" + addressLine02
				+ ", city=" + city + ", customerPoints=" + customerPoints + "]";
	}
	
	
	
}
