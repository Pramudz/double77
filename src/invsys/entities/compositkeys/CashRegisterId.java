package invsys.entities.compositkeys;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import invsys.entities.Users;

@Embeddable
public class CashRegisterId implements Serializable {

	
	@OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "user_id")
	private Users user;

	@Column(name = "registry_date")
	private java.sql.Date registryDate;

	public CashRegisterId() {
	
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public java.sql.Date getRegistryDate() {
		return registryDate;
	}

	public void setRegistryDate(java.sql.Date registryDate) {
		this.registryDate = registryDate;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof CashRegisterId))
			return false;
		CashRegisterId that = (CashRegisterId) obj;
		return Objects.equals(getUser(), that.getUser()) && Objects.equals(getRegistryDate(), that.getRegistryDate());
		
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getUser(), getRegistryDate());
	}
	
	
}
