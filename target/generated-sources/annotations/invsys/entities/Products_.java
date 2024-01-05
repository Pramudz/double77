package invsys.entities;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Products.class)
public abstract class Products_ {

	public static volatile SingularAttribute<Products, Double> reOrderLevel;
	public static volatile SingularAttribute<Products, String> unitOfMeasure;
	public static volatile SingularAttribute<Products, Double> vat;
	public static volatile SingularAttribute<Products, Double> discount;
	public static volatile SingularAttribute<Products, Double> unit_cost_price;
	public static volatile SingularAttribute<Products, Long> prd_id;
	public static volatile SingularAttribute<Products, Double> unit_price;
	public static volatile SingularAttribute<Products, Double> pack_size;
	public static volatile SingularAttribute<Products, String> p_name;
	public static volatile SingularAttribute<Products, Supplier> supplier;
	public static volatile SingularAttribute<Products, Double> unitAverageCost;
	public static volatile SingularAttribute<Products, Double> pack_cost;
	public static volatile SingularAttribute<Products, Double> onHandQty;
	public static volatile SingularAttribute<Products, Double> pack_price;
	public static volatile SingularAttribute<Products, Category> category;
	public static volatile SingularAttribute<Products, Boolean> status;

	public static final String RE_ORDER_LEVEL = "reOrderLevel";
	public static final String UNIT_OF_MEASURE = "unitOfMeasure";
	public static final String VAT = "vat";
	public static final String DISCOUNT = "discount";
	public static final String UNIT_COST_PRICE = "unit_cost_price";
	public static final String PRD_ID = "prd_id";
	public static final String UNIT_PRICE = "unit_price";
	public static final String PACK_SIZE = "pack_size";
	public static final String P_NAME = "p_name";
	public static final String SUPPLIER = "supplier";
	public static final String UNIT_AVERAGE_COST = "unitAverageCost";
	public static final String PACK_COST = "pack_cost";
	public static final String ON_HAND_QTY = "onHandQty";
	public static final String PACK_PRICE = "pack_price";
	public static final String CATEGORY = "category";
	public static final String STATUS = "status";

}

