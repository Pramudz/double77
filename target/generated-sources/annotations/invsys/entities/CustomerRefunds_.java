package invsys.entities;

import java.sql.Date;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CustomerRefunds.class)
public abstract class CustomerRefunds_ {

	public static volatile SingularAttribute<CustomerRefunds, Users> refundedBy;
	public static volatile SingularAttribute<CustomerRefunds, Double> grossRefundAmount;
	public static volatile SingularAttribute<CustomerRefunds, Date> refundDate;
	public static volatile SingularAttribute<CustomerRefunds, Long> refundId;
	public static volatile SingularAttribute<CustomerRefunds, Sales> sales;
	public static volatile SingularAttribute<CustomerRefunds, Double> refundDiscountFromBill;
	public static volatile SingularAttribute<CustomerRefunds, Double> netRefundAmount;

	public static final String REFUNDED_BY = "refundedBy";
	public static final String GROSS_REFUND_AMOUNT = "grossRefundAmount";
	public static final String REFUND_DATE = "refundDate";
	public static final String REFUND_ID = "refundId";
	public static final String SALES = "sales";
	public static final String REFUND_DISCOUNT_FROM_BILL = "refundDiscountFromBill";
	public static final String NET_REFUND_AMOUNT = "netRefundAmount";

}

