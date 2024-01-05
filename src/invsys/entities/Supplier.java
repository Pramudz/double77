package invsys.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "supplier")
public class Supplier implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	@Column(name = "sup_id", nullable = false)
	private int sup_id;

	@Column(name = "com_name" , unique = true)
	private String com_name;

	@Column(name = "contact_fname")
	private String contact_fname;

	@Column(name = "contact_lname")
	private String contact_lname;

	@Column(name = "contact_person_desig")
	private String contact_person_desig;

	@Column(name = "street_address")
	private String streetAddress;

	@Column(name = "address_line_01")
	private String addressLine01;

	@Column(name = "city")
	private String city;

	@Column(name = "email")
	private String email;

	@Column(name = "mobile_no")
	private int mobile_no;

	@Column(name = "telephone")
	private int telephone;

	@Column(name = "payment_period", length = 3)
	private int payment_period;
	
	@Column(name ="br_number")
	private String brNumber;
	
	@OneToMany(mappedBy = "suppplierForMapImage" , cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	private Set<SupplierBrImages> supplierBrImages;
	
	
	@Column(name="block_status" , columnDefinition = "boolean default false")
	private boolean blockStatus;

	public Supplier() {

	}


	public Supplier(int sup_id, String com_name, String contact_fname, String contact_lname,
			String contact_person_desig, String streetAddress, String addressLine01, String city, String email,
			int mobile_no, int telephone, int payment_period, String brNumber) {
		super();
		this.sup_id = sup_id;
		this.com_name = com_name;
		this.contact_fname = contact_fname;
		this.contact_lname = contact_lname;
		this.contact_person_desig = contact_person_desig;
		this.streetAddress = streetAddress;
		this.addressLine01 = addressLine01;
		this.city = city;
		this.email = email;
		this.mobile_no = mobile_no;
		this.telephone = telephone;
		this.payment_period = payment_period;
		this.brNumber = brNumber;
		
	}


	public Supplier(String com_name, String contact_fname, String contact_lname, String contact_person_desig,
			String streetAddress, String addressLine01, String city, String email, int mobile_no, int telephone,
			int payment_period, String brNumber) {
		super();
		this.com_name = com_name;
		this.contact_fname = contact_fname;
		this.contact_lname = contact_lname;
		this.contact_person_desig = contact_person_desig;
		this.streetAddress = streetAddress;
		this.addressLine01 = addressLine01;
		this.city = city;
		this.email = email;
		this.mobile_no = mobile_no;
		this.telephone = telephone;
		this.payment_period = payment_period;
		this.brNumber = brNumber;
	
	}


	public int getSup_id() {
		return sup_id;
	}


	public void setSup_id(int sup_id) {
		this.sup_id = sup_id;
	}


	public String getCom_name() {
		return com_name;
	}


	public void setCom_name(String com_name) {
		this.com_name = com_name;
	}


	public String getContact_fname() {
		return contact_fname;
	}


	public void setContact_fname(String contact_fname) {
		this.contact_fname = contact_fname;
	}


	public String getContact_lname() {
		return contact_lname;
	}


	public void setContact_lname(String contact_lname) {
		this.contact_lname = contact_lname;
	}


	public String getContact_person_desig() {
		return contact_person_desig;
	}


	public void setContact_person_desig(String contact_person_desig) {
		this.contact_person_desig = contact_person_desig;
	}


	public String getStreetAddress() {
		return streetAddress;
	}


	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}


	public String getAddressLine01() {
		return addressLine01;
	}


	public void setAddressLine01(String addressLine01) {
		this.addressLine01 = addressLine01;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public int getMobile_no() {
		return mobile_no;
	}


	public void setMobile_no(int mobile_no) {
		this.mobile_no = mobile_no;
	}


	public int getTelephone() {
		return telephone;
	}


	public void setTelephone(int telephone) {
		this.telephone = telephone;
	}


	public int getPayment_period() {
		return payment_period;
	}


	public void setPayment_period(int payment_period) {
		this.payment_period = payment_period;
	}


	public String getBrNumber() {
		return brNumber;
	}


	public void setBrNumber(String brNumber) {
		this.brNumber = brNumber;
	}


	public Set<SupplierBrImages> getSupplierBrImages() {
		return supplierBrImages;
	}


	public void setSupplierBrImages(Set<SupplierBrImages> supplierBrImages) {
		this.supplierBrImages = supplierBrImages;
	}
	
	
	public boolean isBlockStatus() {
		return blockStatus;
	}


	public void setBlockStatus(boolean blockStatus) {
		this.blockStatus = blockStatus;
	}


	@Override
	public String toString() {
		return "Supplier [sup_id=" + sup_id + ", com_name=" + com_name + ", contact_fname=" + contact_fname
				+ ", contact_lname=" + contact_lname + ", contact_person_desig=" + contact_person_desig
				+ ", streetAddress=" + streetAddress + ", addressLine01=" + addressLine01 + ", city=" + city
				+ ", email=" + email + ", mobile_no=" + mobile_no + ", telephone=" + telephone + ", payment_period="
				+ payment_period + ", brNumber=" + brNumber + "]";
	}
	

	
}
