package invsys.entities.compositkeys;

import invsys.entities.CreditInvoiceVoid;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CreditInvoiceVoidDetId.class)
public abstract class CreditInvoiceVoidDetId_ {

	public static volatile SingularAttribute<CreditInvoiceVoidDetId, Integer> seqNo;
	public static volatile SingularAttribute<CreditInvoiceVoidDetId, CreditInvoiceVoid> creditInvoiceVoid;

	public static final String SEQ_NO = "seqNo";
	public static final String CREDIT_INVOICE_VOID = "creditInvoiceVoid";

}

