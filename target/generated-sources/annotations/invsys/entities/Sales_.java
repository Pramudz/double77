package invsys.entities;

import invsys.entities.compositkeys.SalesId;
import java.sql.Time;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Sales.class)
public abstract class Sales_ {

	public static volatile SingularAttribute<Sales, Double> billAmount;
	public static volatile SingularAttribute<Sales, SalesId> salesId;
	public static volatile SingularAttribute<Sales, Double> amountPaid;
	public static volatile SingularAttribute<Sales, Double> balance;
	public static volatile SingularAttribute<Sales, Double> billDiscount;
	public static volatile SingularAttribute<Sales, Boolean> cancelStatus;
	public static volatile SingularAttribute<Sales, Double> grossBillAmount;
	public static volatile SingularAttribute<Sales, Users> canceledBy;
	public static volatile SingularAttribute<Sales, Boolean> refundStatus;
	public static volatile SingularAttribute<Sales, Time> startTime;
	public static volatile SingularAttribute<Sales, Time> endTime;
	public static volatile SingularAttribute<Sales, Customer> customer;

	public static final String BILL_AMOUNT = "billAmount";
	public static final String SALES_ID = "salesId";
	public static final String AMOUNT_PAID = "amountPaid";
	public static final String BALANCE = "balance";
	public static final String BILL_DISCOUNT = "billDiscount";
	public static final String CANCEL_STATUS = "cancelStatus";
	public static final String GROSS_BILL_AMOUNT = "grossBillAmount";
	public static final String CANCELED_BY = "canceledBy";
	public static final String REFUND_STATUS = "refundStatus";
	public static final String START_TIME = "startTime";
	public static final String END_TIME = "endTime";
	public static final String CUSTOMER = "customer";

}

