package invsys.entities.compositkeys;

import invsys.entities.OverheadCategory;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MonthlyOverheadId.class)
public abstract class MonthlyOverheadId_ {

	public static volatile SingularAttribute<MonthlyOverheadId, Integer> month;
	public static volatile SingularAttribute<MonthlyOverheadId, Short> year;
	public static volatile SingularAttribute<MonthlyOverheadId, OverheadCategory> overheadCategory;

	public static final String MONTH = "month";
	public static final String YEAR = "year";
	public static final String OVERHEAD_CATEGORY = "overheadCategory";

}

