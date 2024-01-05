package invsys.entities;

import java.sql.Blob;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SupplierBrImages.class)
public abstract class SupplierBrImages_ {

	public static volatile SingularAttribute<SupplierBrImages, Blob> image;
	public static volatile SingularAttribute<SupplierBrImages, Integer> imageId;
	public static volatile SingularAttribute<SupplierBrImages, Supplier> suppplierForMapImage;

	public static final String IMAGE = "image";
	public static final String IMAGE_ID = "imageId";
	public static final String SUPPPLIER_FOR_MAP_IMAGE = "suppplierForMapImage";

}

