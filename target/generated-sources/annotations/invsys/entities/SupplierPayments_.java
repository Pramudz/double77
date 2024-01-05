package invsys.entities;

import java.sql.Date;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SupplierPayments.class)
public abstract class SupplierPayments_ {

	public static volatile SingularAttribute<SupplierPayments, String> supplierName;
	public static volatile SingularAttribute<SupplierPayments, Date> dateOfPayment;
	public static volatile SingularAttribute<SupplierPayments, Double> amountPaid;
	public static volatile SingularAttribute<SupplierPayments, String> documentType;
	public static volatile SingularAttribute<SupplierPayments, Long> paymentId;
	public static volatile SingularAttribute<SupplierPayments, PayModes> payMode;
	public static volatile SingularAttribute<SupplierPayments, Supplier> supplier;
	public static volatile SingularAttribute<SupplierPayments, Users> user;

	public static final String SUPPLIER_NAME = "supplierName";
	public static final String DATE_OF_PAYMENT = "dateOfPayment";
	public static final String AMOUNT_PAID = "amountPaid";
	public static final String DOCUMENT_TYPE = "documentType";
	public static final String PAYMENT_ID = "paymentId";
	public static final String PAY_MODE = "payMode";
	public static final String SUPPLIER = "supplier";
	public static final String USER = "user";

}

