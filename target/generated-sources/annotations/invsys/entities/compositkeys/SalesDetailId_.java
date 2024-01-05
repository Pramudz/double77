package invsys.entities.compositkeys;

import invsys.entities.Sales;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SalesDetailId.class)
public abstract class SalesDetailId_ {

	public static volatile SingularAttribute<SalesDetailId, Integer> seqNo;
	public static volatile SingularAttribute<SalesDetailId, Sales> sales;

	public static final String SEQ_NO = "seqNo";
	public static final String SALES = "sales";

}

