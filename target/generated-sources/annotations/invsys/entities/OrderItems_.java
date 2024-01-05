package invsys.entities;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(OrderItems.class)
public abstract class OrderItems_ {

	public static volatile SingularAttribute<OrderItems, Double> deliveredQty;
	public static volatile SingularAttribute<OrderItems, Long> orderDetId;
	public static volatile SingularAttribute<OrderItems, Products> product;
	public static volatile SingularAttribute<OrderItems, Integer> seQNo;
	public static volatile SingularAttribute<OrderItems, Double> itemVat;
	public static volatile SingularAttribute<OrderItems, Double> itemAmount;
	public static volatile SingularAttribute<OrderItems, Double> orderQty;
	public static volatile SingularAttribute<OrderItems, Double> itemDiscount;
	public static volatile SingularAttribute<OrderItems, Double> prductCost;
	public static volatile SingularAttribute<OrderItems, Boolean> deliveryStatus;
	public static volatile SingularAttribute<OrderItems, PurchaseOrder> order;
	public static volatile SingularAttribute<OrderItems, Boolean> partialDeliveryStatus;

	public static final String DELIVERED_QTY = "deliveredQty";
	public static final String ORDER_DET_ID = "orderDetId";
	public static final String PRODUCT = "product";
	public static final String SE_QNO = "seQNo";
	public static final String ITEM_VAT = "itemVat";
	public static final String ITEM_AMOUNT = "itemAmount";
	public static final String ORDER_QTY = "orderQty";
	public static final String ITEM_DISCOUNT = "itemDiscount";
	public static final String PRDUCT_COST = "prductCost";
	public static final String DELIVERY_STATUS = "deliveryStatus";
	public static final String ORDER = "order";
	public static final String PARTIAL_DELIVERY_STATUS = "partialDeliveryStatus";

}

