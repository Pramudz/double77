package invsys.entities;

import invsys.entities.compositkeys.POSPayDetailId;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(POSPayDetails.class)
public abstract class POSPayDetails_ {

	public static volatile SingularAttribute<POSPayDetails, String> referenceNum;
	public static volatile SingularAttribute<POSPayDetails, Double> amount;
	public static volatile SingularAttribute<POSPayDetails, String> subPayMode;
	public static volatile SingularAttribute<POSPayDetails, PayModes> payMode;
	public static volatile SingularAttribute<POSPayDetails, String> slipNumber;
	public static volatile SingularAttribute<POSPayDetails, POSPayDetailId> posPayDetId;

	public static final String REFERENCE_NUM = "referenceNum";
	public static final String AMOUNT = "amount";
	public static final String SUB_PAY_MODE = "subPayMode";
	public static final String PAY_MODE = "payMode";
	public static final String SLIP_NUMBER = "slipNumber";
	public static final String POS_PAY_DET_ID = "posPayDetId";

}

