package invsys.entities.compositkeys;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import invsys.entities.Users;

@Embeddable
public class SalesId implements Serializable {
	

	@Column(name = "bill_no")
	private int bill_no;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "user_id")
	private Users user;

	@Column(name = "sales_date")
	private java.sql.Date date;
	
	

	public SalesId() {

	}

	public SalesId(int bill_no, Users user_id, Date date) {
		this.bill_no = bill_no;
		this.user = user_id;
		this.date = date;
	}

	public int getBill_no() {
		return bill_no;
	}

	public Users getUser_id() {
		return user;
	}

	public java.sql.Date getDate() {
		return date;
	}

	public void setBill_no(int bill_no) {
		this.bill_no = bill_no;
	}

	public void setUser_id(Users user_id) {
		this.user = user_id;
	}

	public void setDate(java.sql.Date date) {
		this.date = date;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof SalesId))
			return false;
		SalesId that = (SalesId) obj;
		return Objects.equals(getBill_no(), that.getBill_no()) && Objects.equals(getUser_id(), that.getUser_id())
				&& Objects.equals(getDate(), that.getDate());

	}

	@Override
	public int hashCode() {
		return Objects.hash(getBill_no(), getUser_id(), getDate());
	}
}
