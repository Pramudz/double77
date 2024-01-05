package invsys.entities.compositkeys;

import invsys.entities.Users;
import java.sql.Date;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CashRegisterId.class)
public abstract class CashRegisterId_ {

	public static volatile SingularAttribute<CashRegisterId, Date> registryDate;
	public static volatile SingularAttribute<CashRegisterId, Users> user;

	public static final String REGISTRY_DATE = "registryDate";
	public static final String USER = "user";

}

