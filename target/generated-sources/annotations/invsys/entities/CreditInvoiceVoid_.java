package invsys.entities;

import java.sql.Date;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CreditInvoiceVoid.class)
public abstract class CreditInvoiceVoid_ {

	public static volatile SingularAttribute<CreditInvoiceVoid, Users> voidedBy;
	public static volatile SingularAttribute<CreditInvoiceVoid, Double> grossVoidAmount;
	public static volatile SingularAttribute<CreditInvoiceVoid, Long> void_id;
	public static volatile SingularAttribute<CreditInvoiceVoid, Double> netVoidAmount;
	public static volatile SingularAttribute<CreditInvoiceVoid, CreditInvoice> creditInvoice;
	public static volatile SingularAttribute<CreditInvoiceVoid, Date> voidDate;
	public static volatile SingularAttribute<CreditInvoiceVoid, Double> voidDiscountFromInvoice;

	public static final String VOIDED_BY = "voidedBy";
	public static final String GROSS_VOID_AMOUNT = "grossVoidAmount";
	public static final String VOID_ID = "void_id";
	public static final String NET_VOID_AMOUNT = "netVoidAmount";
	public static final String CREDIT_INVOICE = "creditInvoice";
	public static final String VOID_DATE = "voidDate";
	public static final String VOID_DISCOUNT_FROM_INVOICE = "voidDiscountFromInvoice";

}

