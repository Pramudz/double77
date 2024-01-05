package invsys.entities;

import java.sql.Date;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PurchaseOrder.class)
public abstract class PurchaseOrder_ {

	public static volatile SingularAttribute<PurchaseOrder, Date> date;
	public static volatile SingularAttribute<PurchaseOrder, String> shipCity;
	public static volatile SingularAttribute<PurchaseOrder, Date> delDate;
	public static volatile SingularAttribute<PurchaseOrder, Long> orderId;
	public static volatile SingularAttribute<PurchaseOrder, String> shipAddressLine01;
	public static volatile SingularAttribute<PurchaseOrder, Users> approvedBy;
	public static volatile SingularAttribute<PurchaseOrder, Boolean> partialStatus;
	public static volatile SingularAttribute<PurchaseOrder, Boolean> deliverStatus;
	public static volatile SingularAttribute<PurchaseOrder, String> shipStreetAddress;
	public static volatile SingularAttribute<PurchaseOrder, Users> deletedBy;
	public static volatile SingularAttribute<PurchaseOrder, Double> orderAmount;
	public static volatile SingularAttribute<PurchaseOrder, Supplier> supplier;
	public static volatile SingularAttribute<PurchaseOrder, Date> expireDate;
	public static volatile SingularAttribute<PurchaseOrder, Users> user;

	public static final String DATE = "date";
	public static final String SHIP_CITY = "shipCity";
	public static final String DEL_DATE = "delDate";
	public static final String ORDER_ID = "orderId";
	public static final String SHIP_ADDRESS_LINE01 = "shipAddressLine01";
	public static final String APPROVED_BY = "approvedBy";
	public static final String PARTIAL_STATUS = "partialStatus";
	public static final String DELIVER_STATUS = "deliverStatus";
	public static final String SHIP_STREET_ADDRESS = "shipStreetAddress";
	public static final String DELETED_BY = "deletedBy";
	public static final String ORDER_AMOUNT = "orderAmount";
	public static final String SUPPLIER = "supplier";
	public static final String EXPIRE_DATE = "expireDate";
	public static final String USER = "user";

}

