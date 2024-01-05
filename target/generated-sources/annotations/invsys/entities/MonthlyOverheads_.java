package invsys.entities;

import invsys.entities.compositkeys.MonthlyOverheadId;
import java.sql.Date;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MonthlyOverheads.class)
public abstract class MonthlyOverheads_ {

	public static volatile SingularAttribute<MonthlyOverheads, Date> dateOfEntry;
	public static volatile SingularAttribute<MonthlyOverheads, Double> amount;
	public static volatile SingularAttribute<MonthlyOverheads, MonthlyOverheadId> monthlyOverheadId;
	public static volatile SingularAttribute<MonthlyOverheads, Users> user;

	public static final String DATE_OF_ENTRY = "dateOfEntry";
	public static final String AMOUNT = "amount";
	public static final String MONTHLY_OVERHEAD_ID = "monthlyOverheadId";
	public static final String USER = "user";

}

