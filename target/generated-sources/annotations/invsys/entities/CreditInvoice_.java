package invsys.entities;

import invsys.entities.compositkeys.CreditInvoiceId;
import java.sql.Date;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CreditInvoice.class)
public abstract class CreditInvoice_ {

	public static volatile SingularAttribute<CreditInvoice, Double> invoiceGrossAmount;
	public static volatile SingularAttribute<CreditInvoice, CreditInvoiceId> creditInvoiceId;
	public static volatile SingularAttribute<CreditInvoice, Double> invoiceDiscount;
	public static volatile SingularAttribute<CreditInvoice, Date> expiredDate;
	public static volatile SingularAttribute<CreditInvoice, Boolean> refundStatus;
	public static volatile SingularAttribute<CreditInvoice, Users> settledBy;
	public static volatile SingularAttribute<CreditInvoice, Integer> creditPeriod;
	public static volatile SingularAttribute<CreditInvoice, String> customerName;
	public static volatile SingularAttribute<CreditInvoice, Double> settledAmount;
	public static volatile SingularAttribute<CreditInvoice, Double> invoiceNetAmount;
	public static volatile SingularAttribute<CreditInvoice, Boolean> settledStatus;
	public static volatile SingularAttribute<CreditInvoice, Double> ifAdvancePayment;
	public static volatile SingularAttribute<CreditInvoice, Date> settledDate;
	public static volatile SingularAttribute<CreditInvoice, Customer> customer;

	public static final String INVOICE_GROSS_AMOUNT = "invoiceGrossAmount";
	public static final String CREDIT_INVOICE_ID = "creditInvoiceId";
	public static final String INVOICE_DISCOUNT = "invoiceDiscount";
	public static final String EXPIRED_DATE = "expiredDate";
	public static final String REFUND_STATUS = "refundStatus";
	public static final String SETTLED_BY = "settledBy";
	public static final String CREDIT_PERIOD = "creditPeriod";
	public static final String CUSTOMER_NAME = "customerName";
	public static final String SETTLED_AMOUNT = "settledAmount";
	public static final String INVOICE_NET_AMOUNT = "invoiceNetAmount";
	public static final String SETTLED_STATUS = "settledStatus";
	public static final String IF_ADVANCE_PAYMENT = "ifAdvancePayment";
	public static final String SETTLED_DATE = "settledDate";
	public static final String CUSTOMER = "customer";

}

