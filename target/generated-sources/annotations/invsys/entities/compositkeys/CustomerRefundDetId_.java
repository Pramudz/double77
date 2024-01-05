package invsys.entities.compositkeys;

import invsys.entities.CustomerRefunds;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CustomerRefundDetId.class)
public abstract class CustomerRefundDetId_ {

	public static volatile SingularAttribute<CustomerRefundDetId, Integer> seqNo;
	public static volatile SingularAttribute<CustomerRefundDetId, CustomerRefunds> customerRefund;

	public static final String SEQ_NO = "seqNo";
	public static final String CUSTOMER_REFUND = "customerRefund";

}

