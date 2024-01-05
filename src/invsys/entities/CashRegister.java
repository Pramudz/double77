package invsys.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import invsys.entities.compositkeys.CashRegisterId;

@Entity
@Table(name = "cash_register")
public class CashRegister implements Serializable {

	@EmbeddedId
	private CashRegisterId id;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "signin_status")
	private boolean signInStatus;

	@Column(name = "opening_cash_balance")
	private double openingCashBalance;

	//total cash sales
	@Column(name = "total_cash_sales")
	private double totalCashSales;
	
	
	// for credit card & other type sales
	@Column(name = "total_credit_card_sales")
	private double totalCreditCardSales;

	//total balanced paid to customers and this will not be used
	@Column(name = "cash_payments")
	private double payments;
	
	@Column(name = "closingCashBalance")
	private double closingCashBalance;

	@Column(name = "log_in_time")
	private java.sql.Time loginTime;

	@Column(name = "log_out_time")
	private java.sql.Time logOutTime;

	@Column(name = "log_in_date")
	private java.sql.Date loginDate;

	@Column(name = "log_out_Date")
	private java.sql.Date logOutDate;

	public CashRegister() {

	}

	public CashRegisterId getId() {
		return id;
	}

	public void setId(CashRegisterId id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isSignInStatus() {
		return signInStatus;
	}

	public void setSignInStatus(boolean signInStatus) {
		this.signInStatus = signInStatus;
	}

	public double getOpeningCashBalance() {
		return openingCashBalance;
	}

	public void setOpeningCashBalance(double openingCashBalance) {
		this.openingCashBalance = openingCashBalance;
	}

	public double getTotalCashSales() {
		return totalCashSales;
	}

	public void setTotalCashSales(double totalCashSales) {
		this.totalCashSales = totalCashSales;
	}

	public double getTotalCreditCardSales() {
		return totalCreditCardSales;
	}

	public void setTotalCreditCardSales(double totalCreditCardSales) {
		this.totalCreditCardSales = totalCreditCardSales;
	}

	public double getPayments() {
		return payments;
	}

	public void setPayments(double payments) {
		this.payments = payments;
	}

	public java.sql.Time getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(java.sql.Time loginTime) {
		this.loginTime = loginTime;
	}

	public java.sql.Time getLogOutTime() {
		return logOutTime;
	}

	public void setLogOutTime(java.sql.Time logOutTime) {
		this.logOutTime = logOutTime;
	}

	public java.sql.Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(java.sql.Date loginDate) {
		this.loginDate = loginDate;
	}

	public java.sql.Date getLogOutDate() {
		return logOutDate;
	}

	public void setLogOutDate(java.sql.Date logOutDate) {
		this.logOutDate = logOutDate;
	}

	public double getClosingCashBalance() {
		return closingCashBalance;
	}

	public void setClosingCashBalance(double closingCashBalance) {
		this.closingCashBalance = closingCashBalance;
	}


	

}
