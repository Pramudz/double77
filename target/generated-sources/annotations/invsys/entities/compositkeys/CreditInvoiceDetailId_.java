package invsys.entities.compositkeys;

import invsys.entities.CreditInvoice;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CreditInvoiceDetailId.class)
public abstract class CreditInvoiceDetailId_ {

	public static volatile SingularAttribute<CreditInvoiceDetailId, Integer> seqNo;
	public static volatile SingularAttribute<CreditInvoiceDetailId, CreditInvoice> creditInvoice;

	public static final String SEQ_NO = "seqNo";
	public static final String CREDIT_INVOICE = "creditInvoice";

}

