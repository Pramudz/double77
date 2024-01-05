package invsys.entities.compositkeys;

import invsys.entities.Users;
import java.sql.Date;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SalesId.class)
public abstract class SalesId_ {

	public static volatile SingularAttribute<SalesId, Date> date;
	public static volatile SingularAttribute<SalesId, Integer> bill_no;
	public static volatile SingularAttribute<SalesId, Users> user;

	public static final String DATE = "date";
	public static final String BILL_NO = "bill_no";
	public static final String USER = "user";

}

