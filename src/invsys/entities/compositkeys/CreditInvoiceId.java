package invsys.entities.compositkeys;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import invsys.entities.Users;

@Embeddable
public class CreditInvoiceId implements Serializable {
	
	
	@Column(name="invoice_id")
	private long invoiceId;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "user_id")
	private Users user;

	@Column(name = "invoice_date")
	private java.sql.Date date;
	
	
	
	public CreditInvoiceId() {
		
	}

	public long getInvoceId() {
		return invoiceId;
	}

	public void setInvoceId(long invoceId) {
		this.invoiceId = invoceId;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public java.sql.Date getDate() {
		return date;
	}

	public void setDate(java.sql.Date date) {
		this.date = date;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof CreditInvoiceId))
			return false;
		CreditInvoiceId that = (CreditInvoiceId) obj;
		return Objects.equals(getInvoceId(), that.getInvoceId()) && Objects.equals(getUser(), that.getUser())
				&& Objects.equals(getDate(), that.getDate());

	}

	@Override
	public int hashCode() {
		return Objects.hash(getInvoceId(), getUser(), getDate());
	}
	

}
