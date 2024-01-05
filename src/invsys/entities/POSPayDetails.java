package invsys.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import invsys.entities.compositkeys.POSPayDetailId;

@Entity
@Table(name="pos_pay_detail")
public class POSPayDetails implements Serializable{

	

	@EmbeddedId
	private POSPayDetailId posPayDetId;
	
	@ManyToOne
	@JoinColumn(name = "pay_mode",referencedColumnName = "pmode_id")
	private PayModes payMode;
	
	@Transient
	private int temporySeqNo;
	
			
	@Column(name="sub_pay_mode")
	private String subPayMode;
	
	@Column(name="amount")
	private double amount;
	
	@Column(name="slip_number")
	private String slipNumber;
	
	
	@Column(name="reference_num")
	private String referenceNum;


	
	
	public POSPayDetails() {
	
	}


	public POSPayDetailId getPosPayDetId() {
		return posPayDetId;
	}


	public void setPosPayDetId(POSPayDetailId posPayDetId) {
		this.posPayDetId = posPayDetId;
	}


	public PayModes getPayMode() {
		return payMode;
	}


	public void setPayMode(PayModes payMode) {
		this.payMode = payMode;
	}


	public double getAmount() {
		return amount;
	}


	public void setAmount(double amount) {
		this.amount = amount;
	}


	public String getSlipNumber() {
		return slipNumber;
	}


	public void setSlipNumber(String slipNumber) {
		this.slipNumber = slipNumber;
	}


	public String getReferenceNum() {
		return referenceNum;
	}


	public void setReferenceNum(String referenceNum) {
		this.referenceNum = referenceNum;
	}


	public String getSubPayMode() {
		return subPayMode;
	}


	public void setSubPayMode(String supPayMode) {
		this.subPayMode = supPayMode;
	}


	public int getTemporySeqNo() {
		return temporySeqNo;
	}


	public void setTemporySeqNo(int temporySeqNo) {
		this.temporySeqNo = temporySeqNo;
	}
	
	
	
	
	
}
