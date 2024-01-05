package invsys.entities.compositkeys;

import java.io.Serializable;
import java.time.Month;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import invsys.entities.OverheadCategory;


@Embeddable
public class MonthlyOverheadId implements Serializable {

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "overhead_category_id", referencedColumnName = "overhead_category_id")
	private OverheadCategory overheadCategory;

	@Column(name = "year", columnDefinition = "smallint")
	private short year;

	@Column(name = "month", columnDefinition = "smallint")
	@Enumerated
	private Month month;

	public MonthlyOverheadId() {
		
	}

	public OverheadCategory getOverheadCategory() {
		return overheadCategory;
	}

	public void setOverheadCategory(OverheadCategory overheadCategory) {
		this.overheadCategory = overheadCategory;
	}

	public short getYear() {
		return year;
	}

	public void setYear(short year) {
		this.year = year;
	}

	public Month getMonth() {
		return month;
	}

	public void setMonth(Month month) {
		this.month = month;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof MonthlyOverheadId))
			return false;
		MonthlyOverheadId that = (MonthlyOverheadId) obj;
		return Objects.equals(getOverheadCategory(), that.getOverheadCategory()) && Objects.equals(getYear(), that.getYear())
				&& Objects.equals(getMonth(), that.getMonth());

	}

	@Override
	public int hashCode() {
		return Objects.hash(getOverheadCategory(), getYear(), getMonth());
	}
		
}


