package invsys.entities;

import invsys.entities.compositkeys.SalesDetailId;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SalesDetails.class)
public abstract class SalesDetails_ {

	public static volatile SingularAttribute<SalesDetails, Double> unitPrice;
	public static volatile SingularAttribute<SalesDetails, Products> product;
	public static volatile SingularAttribute<SalesDetails, Double> costPrice;
	public static volatile SingularAttribute<SalesDetails, SalesDetailId> salesDetId;
	public static volatile SingularAttribute<SalesDetails, Double> discount;
	public static volatile SingularAttribute<SalesDetails, Users> canceledBy;
	public static volatile SingularAttribute<SalesDetails, Double> averageCostPrice;
	public static volatile SingularAttribute<SalesDetails, String> serialNo;
	public static volatile SingularAttribute<SalesDetails, Double> discountPercentage;
	public static volatile SingularAttribute<SalesDetails, String> warrantyPeriod;
	public static volatile SingularAttribute<SalesDetails, Double> itemVat;
	public static volatile SingularAttribute<SalesDetails, Double> discountedPrice;
	public static volatile SingularAttribute<SalesDetails, Double> itemAmount;
	public static volatile SingularAttribute<SalesDetails, Double> salesQty;
	public static volatile SingularAttribute<SalesDetails, Double> itemGrossAmount;
	public static volatile SingularAttribute<SalesDetails, Boolean> itemCancelStatus;

	public static final String UNIT_PRICE = "unitPrice";
	public static final String PRODUCT = "product";
	public static final String COST_PRICE = "costPrice";
	public static final String SALES_DET_ID = "salesDetId";
	public static final String DISCOUNT = "discount";
	public static final String CANCELED_BY = "canceledBy";
	public static final String AVERAGE_COST_PRICE = "averageCostPrice";
	public static final String SERIAL_NO = "serialNo";
	public static final String DISCOUNT_PERCENTAGE = "discountPercentage";
	public static final String WARRANTY_PERIOD = "warrantyPeriod";
	public static final String ITEM_VAT = "itemVat";
	public static final String DISCOUNTED_PRICE = "discountedPrice";
	public static final String ITEM_AMOUNT = "itemAmount";
	public static final String SALES_QTY = "salesQty";
	public static final String ITEM_GROSS_AMOUNT = "itemGrossAmount";
	public static final String ITEM_CANCEL_STATUS = "itemCancelStatus";

}

