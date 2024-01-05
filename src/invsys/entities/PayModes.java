package invsys.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "pay_modes")
public class PayModes implements Serializable {

	@Id
	@Column(name = "pmode_id", length = 10, nullable = false)
	private String modeId;

	@Column(name = "mode_desc", length = 30)
	private String modeDesc;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "main_pay_mode", nullable = true, referencedColumnName = "pmode_id")
	private PayModes mainPayMode;

	public PayModes() {

	}

	public PayModes(String modeId, String modeDesc) {

		this.modeId = modeId;

		this.modeDesc = modeDesc;
	}

	public String getModeId() {
		return modeId;
	}

	public void setModeId(String modeId) {
		this.modeId = modeId;
	}

	public String getModeDesc() {
		return modeDesc;
	}

	public void setModeDesc(String modeDesc) {
		this.modeDesc = modeDesc;
	}

	public PayModes getMainPayMode() {
		return mainPayMode;
	}

	public void setMainPayMode(PayModes mainPayMode) {
		this.mainPayMode = mainPayMode;
	}

	@Override
	public String toString() {
		return "PayModes [modeId=" + modeId + ", modeDesc=" + modeDesc + "]";
	}

}
