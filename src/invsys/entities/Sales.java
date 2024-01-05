package invsys.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import invsys.entities.compositkeys.SalesId;

@Entity(name = "Sales")
@Table(name = "sales")
public class Sales implements Serializable {

	@EmbeddedId
	private SalesId salesId;
                
        @ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "customerId")
        private Customer customer;

	@Column(name = "start_time", nullable = false)
	private java.sql.Time startTime;

	@Column(name = "end_time", nullable = false)
	private java.sql.Time endTime;

	@Column(name = "amount_paid", nullable = false)
	private double amountPaid;

	@Column(name = "balance", nullable = true)
	private double balance;

	@Column(name ="gross_bill_amount" , nullable=false)
	private double grossBillAmount;
	
	@Column(name = "net_bill_amount", nullable = false)
	private double billAmount;

	@Column(name = "cancel_status", nullable = false)
	private boolean cancelStatus;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name="canceledby", nullable =true)
	private Users canceledBy;
	
	@Column(name="discount")
	private double billDiscount;
	
	@Column(name="refund_status" , columnDefinition = "boolean default false")
	private boolean refundStatus;
	
	

	public Sales() {

	}
	
	

	public SalesId getSalesId() {
		return salesId;
	}



	public void setSalesId(SalesId salesId) {
		this.salesId = salesId;
	}



	public java.sql.Time getStartTime() {
		return startTime;
	}

	public void setStartTime(java.sql.Time startTime) {
		this.startTime = startTime;
	}

	public java.sql.Time getEndTime() {
		return endTime;
	}

	public void setEndTime(java.sql.Time endTime) {
		this.endTime = endTime;
	}

	public double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(double billAmount) {
		this.billAmount = billAmount;
	}

	public boolean isCancelStatus() {
		return cancelStatus;
	}

	public void setCancelStatus(boolean cancelStatus) {
		this.cancelStatus = cancelStatus;
	}

	public Users getCanceledBy() {
		return canceledBy;
	}

	public void setCanceledBy(Users canceledBy) {
		this.canceledBy = canceledBy;
	}


	public double getBillDiscount() {
		return billDiscount;
	}



	public void setBillDiscount(double billDiscount) {
		this.billDiscount = billDiscount;
	}



	public double getGrossBillAmount() {
		return grossBillAmount;
	}



	public void setGrossBillAmount(double grossBillAmount) {
		this.grossBillAmount = grossBillAmount;
	}


	public boolean isRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(boolean refundStatus) {
		this.refundStatus = refundStatus;
	}

        public Customer getCustomer() {
            return customer;
        }

        public void setCustomer(Customer customer) {
            this.customer = customer;
        }

	

}
