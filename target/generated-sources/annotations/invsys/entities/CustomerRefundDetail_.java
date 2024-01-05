package invsys.entities;

import invsys.entities.compositkeys.CustomerRefundDetId;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CustomerRefundDetail.class)
public abstract class CustomerRefundDetail_ {

	public static volatile SingularAttribute<CustomerRefundDetail, Double> unitPrice;
	public static volatile SingularAttribute<CustomerRefundDetail, Products> product;
	public static volatile SingularAttribute<CustomerRefundDetail, CustomerRefundDetId> customerRefundDetId;
	public static volatile SingularAttribute<CustomerRefundDetail, Double> costPrice;
	public static volatile SingularAttribute<CustomerRefundDetail, Double> discount;
	public static volatile SingularAttribute<CustomerRefundDetail, Double> averageCostPrice;
	public static volatile SingularAttribute<CustomerRefundDetail, Double> salesQtys;
	public static volatile SingularAttribute<CustomerRefundDetail, Double> discountPercentage;
	public static volatile SingularAttribute<CustomerRefundDetail, Double> refundQty;
	public static volatile SingularAttribute<CustomerRefundDetail, Double> discountedunitPrice;
	public static volatile SingularAttribute<CustomerRefundDetail, Double> itemVat;
	public static volatile SingularAttribute<CustomerRefundDetail, Double> itemNetAmount;
	public static volatile SingularAttribute<CustomerRefundDetail, Double> itemGrossAmount;

	public static final String UNIT_PRICE = "unitPrice";
	public static final String PRODUCT = "product";
	public static final String CUSTOMER_REFUND_DET_ID = "customerRefundDetId";
	public static final String COST_PRICE = "costPrice";
	public static final String DISCOUNT = "discount";
	public static final String AVERAGE_COST_PRICE = "averageCostPrice";
	public static final String SALES_QTYS = "salesQtys";
	public static final String DISCOUNT_PERCENTAGE = "discountPercentage";
	public static final String REFUND_QTY = "refundQty";
	public static final String DISCOUNTEDUNIT_PRICE = "discountedunitPrice";
	public static final String ITEM_VAT = "itemVat";
	public static final String ITEM_NET_AMOUNT = "itemNetAmount";
	public static final String ITEM_GROSS_AMOUNT = "itemGrossAmount";

}

