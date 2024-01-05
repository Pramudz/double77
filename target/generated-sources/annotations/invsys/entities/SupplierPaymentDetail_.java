package invsys.entities;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SupplierPaymentDetail.class)
public abstract class SupplierPaymentDetail_ {

	public static volatile SingularAttribute<SupplierPaymentDetail, Double> documentAmount;
	public static volatile SingularAttribute<SupplierPaymentDetail, Long> grnRonId;
	public static volatile SingularAttribute<SupplierPaymentDetail, String> documentType;
	public static volatile SingularAttribute<SupplierPaymentDetail, Long> paymentDetid;
	public static volatile SingularAttribute<SupplierPaymentDetail, SupplierPayments> supplierPayments;

	public static final String DOCUMENT_AMOUNT = "documentAmount";
	public static final String GRN_RON_ID = "grnRonId";
	public static final String DOCUMENT_TYPE = "documentType";
	public static final String PAYMENT_DETID = "paymentDetid";
	public static final String SUPPLIER_PAYMENTS = "supplierPayments";

}

