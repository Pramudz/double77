package invsys.entities;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SalesCancel.class)
public abstract class SalesCancel_ {

	public static volatile SingularAttribute<SalesCancel, Integer> salesCancelId;
	public static volatile SingularAttribute<SalesCancel, Users> cancelUser;
	public static volatile SingularAttribute<SalesCancel, Sales> sales;

	public static final String SALES_CANCEL_ID = "salesCancelId";
	public static final String CANCEL_USER = "cancelUser";
	public static final String SALES = "sales";

}

