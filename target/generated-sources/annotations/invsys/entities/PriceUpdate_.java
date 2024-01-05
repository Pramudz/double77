package invsys.entities;

import java.sql.Date;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PriceUpdate.class)
public abstract class PriceUpdate_ {

	public static volatile SingularAttribute<PriceUpdate, Double> newPackCostPrice;
	public static volatile SingularAttribute<PriceUpdate, Products> product;
	public static volatile SingularAttribute<PriceUpdate, Double> oldPackCostPrice;
	public static volatile SingularAttribute<PriceUpdate, Date> revisionDate;
	public static volatile SingularAttribute<PriceUpdate, Double> newCostPrice;
	public static volatile SingularAttribute<PriceUpdate, Double> oldUnitPrice;
	public static volatile SingularAttribute<PriceUpdate, Double> oldPackUnitPrice;
	public static volatile SingularAttribute<PriceUpdate, Integer> priceUpdateId;
	public static volatile SingularAttribute<PriceUpdate, Double> oldCostPrice;
	public static volatile SingularAttribute<PriceUpdate, Users> user;
	public static volatile SingularAttribute<PriceUpdate, Double> newPackUnitPrice;
	public static volatile SingularAttribute<PriceUpdate, Double> newUnitPrice;

	public static final String NEW_PACK_COST_PRICE = "newPackCostPrice";
	public static final String PRODUCT = "product";
	public static final String OLD_PACK_COST_PRICE = "oldPackCostPrice";
	public static final String REVISION_DATE = "revisionDate";
	public static final String NEW_COST_PRICE = "newCostPrice";
	public static final String OLD_UNIT_PRICE = "oldUnitPrice";
	public static final String OLD_PACK_UNIT_PRICE = "oldPackUnitPrice";
	public static final String PRICE_UPDATE_ID = "priceUpdateId";
	public static final String OLD_COST_PRICE = "oldCostPrice";
	public static final String USER = "user";
	public static final String NEW_PACK_UNIT_PRICE = "newPackUnitPrice";
	public static final String NEW_UNIT_PRICE = "newUnitPrice";

}

