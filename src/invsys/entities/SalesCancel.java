package invsys.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="sales_cancel")
public class SalesCancel implements Serializable {

	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sales_cancel_id")
	private int salesCancelId;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumns(value = {
			@JoinColumn(name = "bill_no", referencedColumnName = "bill_no"),
			@JoinColumn(name = "user_id", referencedColumnName = "user_id"),
			@JoinColumn(name = "sales_date", referencedColumnName = "sales_date") })
	private Sales sales;
	
	
	@ManyToOne(cascade= CascadeType.MERGE)
	@JoinColumn(name="canceled_by" , referencedColumnName ="user_id" )
	private Users cancelUser;
	
	public SalesCancel() {
		
	}

	public Sales getSales() {
		return sales;
	}

	public void setSales(Sales sales) {
		this.sales = sales;
	}

	public Users getCancelUser() {
		return cancelUser;
	}

	public void setCancelUser(Users cancelUser) {
		this.cancelUser = cancelUser;
	}
	
	
}
