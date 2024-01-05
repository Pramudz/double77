package invsys.entities;

import java.sql.Date;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SupplierReturn.class)
public abstract class SupplierReturn_ {

	public static volatile SingularAttribute<SupplierReturn, Long> supReturnId;
	public static volatile SingularAttribute<SupplierReturn, Date> returnDate;
	public static volatile SingularAttribute<SupplierReturn, String> documentType;
	public static volatile SingularAttribute<SupplierReturn, Supplier> supplier;
	public static volatile SingularAttribute<SupplierReturn, Boolean> paymentUtilizedStatus;
	public static volatile SingularAttribute<SupplierReturn, Users> user;
	public static volatile SingularAttribute<SupplierReturn, Double> returnAmount;

	public static final String SUP_RETURN_ID = "supReturnId";
	public static final String RETURN_DATE = "returnDate";
	public static final String DOCUMENT_TYPE = "documentType";
	public static final String SUPPLIER = "supplier";
	public static final String PAYMENT_UTILIZED_STATUS = "paymentUtilizedStatus";
	public static final String USER = "user";
	public static final String RETURN_AMOUNT = "returnAmount";

}

