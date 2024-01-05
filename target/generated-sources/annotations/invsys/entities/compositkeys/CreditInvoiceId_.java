package invsys.entities.compositkeys;

import invsys.entities.Users;
import java.sql.Date;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CreditInvoiceId.class)
public abstract class CreditInvoiceId_ {

	public static volatile SingularAttribute<CreditInvoiceId, Date> date;
	public static volatile SingularAttribute<CreditInvoiceId, Long> invoiceId;
	public static volatile SingularAttribute<CreditInvoiceId, Users> user;

	public static final String DATE = "date";
	public static final String INVOICE_ID = "invoiceId";
	public static final String USER = "user";

}

