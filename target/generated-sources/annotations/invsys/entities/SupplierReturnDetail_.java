package invsys.entities;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SupplierReturnDetail.class)
public abstract class SupplierReturnDetail_ {

	public static volatile SingularAttribute<SupplierReturnDetail, String> reason;
	public static volatile SingularAttribute<SupplierReturnDetail, Products> product;
	public static volatile SingularAttribute<SupplierReturnDetail, Integer> seqNo;
	public static volatile SingularAttribute<SupplierReturnDetail, Double> itemVat;
	public static volatile SingularAttribute<SupplierReturnDetail, Double> returnQty;
	public static volatile SingularAttribute<SupplierReturnDetail, Long> returnDetId;
	public static volatile SingularAttribute<SupplierReturnDetail, SupplierReturn> supplierReturn;
	public static volatile SingularAttribute<SupplierReturnDetail, Double> prductCost;
	public static volatile SingularAttribute<SupplierReturnDetail, String> productName;
	public static volatile SingularAttribute<SupplierReturnDetail, Double> prdAmount;

	public static final String REASON = "reason";
	public static final String PRODUCT = "product";
	public static final String SEQ_NO = "seqNo";
	public static final String ITEM_VAT = "itemVat";
	public static final String RETURN_QTY = "returnQty";
	public static final String RETURN_DET_ID = "returnDetId";
	public static final String SUPPLIER_RETURN = "supplierReturn";
	public static final String PRDUCT_COST = "prductCost";
	public static final String PRODUCT_NAME = "productName";
	public static final String PRD_AMOUNT = "prdAmount";

}

