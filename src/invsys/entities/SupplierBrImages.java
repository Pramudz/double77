package invsys.entities;

import java.io.Serializable;
import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "supplier_br_images")
public class SupplierBrImages implements Serializable {
	
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Column(name ="image_id")
	 private int imageId;
	 
	 @Lob
	 @Column(name = "image")
	 private Blob image;
	 
	 @ManyToOne
	 @JoinColumn(name="supplier_id" , referencedColumnName = "sup_id")
	 private Supplier suppplierForMapImage;

	public SupplierBrImages(int imageId, Blob image, Supplier supplier) {
		super();
		this.imageId = imageId;
		this.image = image;
		this.suppplierForMapImage = supplier;
	}

	public SupplierBrImages() {
	
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public Blob getImage() {
		return image;
	}

	public void setImage(Blob image) {
		this.image = image;
	}

	public Supplier getSupplier() {
		return suppplierForMapImage;
	}

	public void setSupplier(Supplier supplier) {
		this.suppplierForMapImage = supplier;
	}

	@Override
	public String toString() {
		return "SupplierBrImages [imageId=" + imageId + ", image=" + image + ", supplier=" + suppplierForMapImage + "]";
	}
	 
 
	 
	 

}
