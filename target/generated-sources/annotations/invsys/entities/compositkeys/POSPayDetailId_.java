package invsys.entities.compositkeys;

import invsys.entities.Sales;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(POSPayDetailId.class)
public abstract class POSPayDetailId_ {

	public static volatile SingularAttribute<POSPayDetailId, Integer> seqNo;
	public static volatile SingularAttribute<POSPayDetailId, String> docType;
	public static volatile SingularAttribute<POSPayDetailId, Sales> sales;

	public static final String SEQ_NO = "seqNo";
	public static final String DOC_TYPE = "docType";
	public static final String SALES = "sales";

}

