package invsys.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import invsys.entities.compositkeys.MonthlyOverheadId;

@Entity
@Table(name = "monthly_overheads")
public class MonthlyOverheads implements Serializable{

	@EmbeddedId
	private MonthlyOverheadId monthlyOverheadId;
	
	@Column(name="date_of_entry")
	private java.sql.Date dateOfEntry;
	
	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName = "user_id")
	private Users user;
	
	@Column(name="amount")
	private double amount;

	public MonthlyOverheads() {
		
	}

	public MonthlyOverheadId getMonthlyOverheadId() {
		return monthlyOverheadId;
	}

	public void setMonthlyOverheadId(MonthlyOverheadId monthlyOverheadId) {
		this.monthlyOverheadId = monthlyOverheadId;
	}

	public java.sql.Date getDateOfEntry() {
		return dateOfEntry;
	}

	public void setDateOfEntry(java.sql.Date dateOfEntry) {
		this.dateOfEntry = dateOfEntry;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "MonthlyOverheads [monthlyOverheadId=" + monthlyOverheadId + ", dateOfEntry=" + dateOfEntry + ", user="
				+ user + ", amount=" + amount + "]";
	}
	
	
	
}
