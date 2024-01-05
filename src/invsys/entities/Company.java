package invsys.entities;

import java.io.Serializable;
import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name ="company")
public class Company implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="comp_id")
	int compId;
	
	@Column(name ="company_name" , unique = true, nullable=false)
	private String companyName;
	
	@Column(name ="telephone_num")
	private String telephoneNum;
	
	@Column(name = "fax_number")
	private String faxNumber;
	
	@Column(name ="address_line_1")
	private String addressLine1;
	
	@Column(name = "address_line_2")
	private String addressLine2;
	
	@Column(name = "address_line_3")
	private String addressLine3;
	
	@Column (name ="province")
	private String province;
	
	@Column(name = "company_email")
	private String companyEmail;
	
	@Column(name="company_register_num")
	private String companyRegNum;
	
	@Lob
	@Column(name = "companyLogo")
	private Blob companyLogo;

	public Company() {
		
	}

	public int getCompId() {
		return compId;
	}

	public void setCompId(int compId) {
		this.compId = compId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getTelephoneNum() {
		return telephoneNum;
	}

	public void setTelephoneNum(String telephoneNum) {
		this.telephoneNum = telephoneNum;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getAddressLine3() {
		return addressLine3;
	}

	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	public String getCompanyEmail() {
		return companyEmail;
	}

	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}

	public String getCompanyRegNum() {
		return companyRegNum;
	}

	public void setCompanyRegNum(String companyRegNum) {
		this.companyRegNum = companyRegNum;
	}

	public Blob getCompanyLogo() {
		return companyLogo;
	}

	public void setCompanyLogo(Blob companyLogo) {
		this.companyLogo = companyLogo;
	}
	
	
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Override
	public String toString() {
		return "Company [compId=" + compId + ", companyName=" + companyName + ", telephoneNum=" + telephoneNum
				+ ", faxNumber=" + faxNumber + ", addressLine1=" + addressLine1 + ", addressLine2=" + addressLine2
				+ ", addressLine3=" + addressLine3 + ", province=" + province + ", companyEmail=" + companyEmail
				+ ", companyRegNum=" + companyRegNum + ", companyLogo=" + companyLogo + "]";
	}



}
