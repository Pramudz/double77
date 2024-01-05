package invsys.entities.compositkeys;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import invsys.entities.CustomerRefunds;

@Embeddable
public class CustomerRefundDetId implements Serializable {

	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name="refund_id", referencedColumnName = "refund_id")
	private CustomerRefunds customerRefund;
	
	@Column(name="seq_no")
	private int seqNo;

	public CustomerRefunds getCustomerRefund() {
		return customerRefund;
	}

	public void setCustomerRefund(CustomerRefunds customerRefund) {
		this.customerRefund = customerRefund;
	}

	public int getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this== obj)
			return true;
		if(!((obj) instanceof CustomerRefundDetId))
			return false;
		CustomerRefundDetId that = (CustomerRefundDetId) obj;
		return Objects.equals(getCustomerRefund(), that.getCustomerRefund()) &&
				Objects.equals(getSeqNo(), that.getSeqNo());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getCustomerRefund(), getSeqNo());
	}
}
