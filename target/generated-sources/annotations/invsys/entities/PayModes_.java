package invsys.entities;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PayModes.class)
public abstract class PayModes_ {

	public static volatile SingularAttribute<PayModes, String> modeId;
	public static volatile SingularAttribute<PayModes, String> modeDesc;
	public static volatile SingularAttribute<PayModes, PayModes> mainPayMode;

	public static final String MODE_ID = "modeId";
	public static final String MODE_DESC = "modeDesc";
	public static final String MAIN_PAY_MODE = "mainPayMode";

}

