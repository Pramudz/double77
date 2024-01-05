package invsys.entities;

import java.io.Serializable;
import java.sql.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.codec.digest.DigestUtils;



@Entity
@Table(name = "users")
public class Users implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private int user_id;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "user_name", unique = true)
	private String userName;

	@Column(name = "dob")
	private java.sql.Date date;

	@Column(name = "address")
	private String address;

	@Column(name = "contact_num")
	private int contactNum;

	@Column(name = "user_email", unique = true , nullable = true)
	private String userEmail;

	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@JoinTable(name = "user_role", joinColumns = {
			@JoinColumn(referencedColumnName = "user_id" , nullable = false) }, inverseJoinColumns = {
					@JoinColumn(referencedColumnName = "role_id", nullable = false) })
	private Set<Role> role;

	@Column(name = "password", nullable = true)
	private String password;

	@Column(name = "last_login")
	private java.sql.Date lastLogin;

	@Column(name = "status")
	private boolean activeStat;
	
	@Column(name = "loginStatus",nullable = true)
	private boolean loginStatus = false;

	// this method only created for Table View mapping in order to convert set
	// data to string and map it to the table view
	@Transient
	private String rolesToTableView;

	public String getRolesToTableView() {
		return rolesToTableView;
	}

	public void setRolesToTableView(String rolesToTableView) {
		this.rolesToTableView = rolesToTableView;
	}

	public Users() {

	}

	public Users(int user_id, String firstName, String lastName, String userName, Date date, String address,
			int contactNum, String userEmail, Set<Role> role, String password, Date lastLogin, boolean activeStat) {
		super();
		this.user_id = user_id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.date = date;
		this.address = address;
		this.contactNum = contactNum;
		this.userEmail = userEmail;
		this.role = role;
		this.password = password;
		this.lastLogin = lastLogin;
		this.activeStat = activeStat;
	}

	public Users(String firstName, String lastName, String userName, Date date, String address, int contactNum,
			String userEmail, Set<Role> role, String password, Date lastLogin, boolean activeStat) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.date = date;
		this.address = address;
		this.contactNum = contactNum;
		this.userEmail = userEmail;
		this.role = role;
		this.password = password;
		this.lastLogin = lastLogin;
		this.activeStat = activeStat;
	}

	public int getUser_id() {
		return user_id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getUserName() {
		return userName;
	}

	public java.sql.Date getDate() {
		return date;
	}

	public String getAddress() {
		return address;
	}

	public int getContactNum() {
		return contactNum;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public Set<Role> getRole() {
		return role;
	}

	public String getPassword() {
		return password;
	}

	public java.sql.Date getLastLogin() {
		return lastLogin;
	}

	public boolean isActiveStat() {
		return activeStat;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setDate(java.sql.Date date) {
		this.date = date;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setContactNum(int contactNum) {
		this.contactNum = contactNum;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public void setRole(Set<Role> role) {
		this.role = role;
	}

	public void setPassword(String password) {
		
		this.password = DigestUtils.sha1Hex(password);
	}

	public void setLastLogin(java.sql.Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public void setActiveStat(boolean activeStat) {
		this.activeStat = activeStat;
	}

	public boolean isLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(boolean loginStatus) {
		this.loginStatus = loginStatus;
	}

	@Override
	public String toString() {
		return "Users [user_id=" + user_id + ", firstName=" + firstName + ", lastName=" + lastName + ", userName="
				+ userName + ", date=" + date + ", address=" + address + ", contactNum=" + contactNum + ", userEmail="
				+ userEmail + ", role=" + role + ", password=" + password + ", lastLogin=" + lastLogin + ", activeStat="
				+ activeStat + "]";
	}

}
