package invsys.entities;

import java.sql.Date;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(GoodReceived.class)
public abstract class GoodReceived_ {

	public static volatile SingularAttribute<GoodReceived, Double> grnAmount;
	public static volatile SingularAttribute<GoodReceived, Date> approvedDate;
	public static volatile SingularAttribute<GoodReceived, Users> grnApprovedBy;
	public static volatile SingularAttribute<GoodReceived, Boolean> paidStatus;
	public static volatile SingularAttribute<GoodReceived, String> documentType;
	public static volatile SingularAttribute<GoodReceived, Supplier> supplier;
	public static volatile SingularAttribute<GoodReceived, PurchaseOrder> purchaseOrder;
	public static volatile SingularAttribute<GoodReceived, Date> grnDate;
	public static volatile SingularAttribute<GoodReceived, Users> user;
	public static volatile SingularAttribute<GoodReceived, String> invoiceNum;
	public static volatile SingularAttribute<GoodReceived, Long> grnId;

	public static final String GRN_AMOUNT = "grnAmount";
	public static final String APPROVED_DATE = "approvedDate";
	public static final String GRN_APPROVED_BY = "grnApprovedBy";
	public static final String PAID_STATUS = "paidStatus";
	public static final String DOCUMENT_TYPE = "documentType";
	public static final String SUPPLIER = "supplier";
	public static final String PURCHASE_ORDER = "purchaseOrder";
	public static final String GRN_DATE = "grnDate";
	public static final String USER = "user";
	public static final String INVOICE_NUM = "invoiceNum";
	public static final String GRN_ID = "grnId";

}

