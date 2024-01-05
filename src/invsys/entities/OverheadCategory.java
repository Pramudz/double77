package invsys.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="overhead_category")
public class OverheadCategory implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="overhead_category_id")
	private int overheadCategoryId;
	
	
	@Column(name="overhead_category")
	private String overheadCategory;


	
	
	public OverheadCategory() {
		super();
		// TODO Auto-generated constructor stub
	}


	public int getOverheadCategoryId() {
		return overheadCategoryId;
	}


	public void setOverheadCategoryId(int overheadCategoryId) {
		this.overheadCategoryId = overheadCategoryId;
	}


	public String getOverheadCategory() {
		return overheadCategory;
	}


	public void setOverheadCategory(String overheadCategory) {
		this.overheadCategory = overheadCategory;
	}


	@Override
	public String toString() {
		return "OverheadCategory [overheadCategoryId=" + overheadCategoryId + ", overheadCategory=" + overheadCategory
				+ "]";
	}
	
	
	

}
