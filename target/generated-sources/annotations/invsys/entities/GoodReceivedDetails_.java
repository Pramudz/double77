package invsys.entities;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(GoodReceivedDetails.class)
public abstract class GoodReceivedDetails_ {

	public static volatile SingularAttribute<GoodReceivedDetails, Double> unitPrice;
	public static volatile SingularAttribute<GoodReceivedDetails, Products> product;
	public static volatile SingularAttribute<GoodReceivedDetails, Double> receivedQty;
	public static volatile SingularAttribute<GoodReceivedDetails, Integer> seqNo;
	public static volatile SingularAttribute<GoodReceivedDetails, GoodReceived> goodRecevied;
	public static volatile SingularAttribute<GoodReceivedDetails, Double> grnItemAmount;
	public static volatile SingularAttribute<GoodReceivedDetails, PurchaseOrder> purchaseOrder;
	public static volatile SingularAttribute<GoodReceivedDetails, Double> costPrice;
	public static volatile SingularAttribute<GoodReceivedDetails, Long> grnDetId;
	public static volatile SingularAttribute<GoodReceivedDetails, Double> orderedQty;

	public static final String UNIT_PRICE = "unitPrice";
	public static final String PRODUCT = "product";
	public static final String RECEIVED_QTY = "receivedQty";
	public static final String SEQ_NO = "seqNo";
	public static final String GOOD_RECEVIED = "goodRecevied";
	public static final String GRN_ITEM_AMOUNT = "grnItemAmount";
	public static final String PURCHASE_ORDER = "purchaseOrder";
	public static final String COST_PRICE = "costPrice";
	public static final String GRN_DET_ID = "grnDetId";
	public static final String ORDERED_QTY = "orderedQty";

}

