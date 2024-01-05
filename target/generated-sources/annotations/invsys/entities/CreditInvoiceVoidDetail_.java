package invsys.entities;

import invsys.entities.compositkeys.CreditInvoiceVoidDetId;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CreditInvoiceVoidDetail.class)
public abstract class CreditInvoiceVoidDetail_ {

	public static volatile SingularAttribute<CreditInvoiceVoidDetail, Double> unitPrice;
	public static volatile SingularAttribute<CreditInvoiceVoidDetail, Double> discountPercentage;
	public static volatile SingularAttribute<CreditInvoiceVoidDetail, Products> product;
	public static volatile SingularAttribute<CreditInvoiceVoidDetail, Double> discountedunitPrice;
	public static volatile SingularAttribute<CreditInvoiceVoidDetail, Double> itemVat;
	public static volatile SingularAttribute<CreditInvoiceVoidDetail, Double> costPrice;
	public static volatile SingularAttribute<CreditInvoiceVoidDetail, Double> salesQty;
	public static volatile SingularAttribute<CreditInvoiceVoidDetail, Double> discount;
	public static volatile SingularAttribute<CreditInvoiceVoidDetail, Double> averageCostPrice;
	public static volatile SingularAttribute<CreditInvoiceVoidDetail, CreditInvoiceVoidDetId> creditInvoiceVoidDetId;
	public static volatile SingularAttribute<CreditInvoiceVoidDetail, Double> itemNetAmount;
	public static volatile SingularAttribute<CreditInvoiceVoidDetail, Double> itemGrossAmount;

	public static final String UNIT_PRICE = "unitPrice";
	public static final String DISCOUNT_PERCENTAGE = "discountPercentage";
	public static final String PRODUCT = "product";
	public static final String DISCOUNTEDUNIT_PRICE = "discountedunitPrice";
	public static final String ITEM_VAT = "itemVat";
	public static final String COST_PRICE = "costPrice";
	public static final String SALES_QTY = "salesQty";
	public static final String DISCOUNT = "discount";
	public static final String AVERAGE_COST_PRICE = "averageCostPrice";
	public static final String CREDIT_INVOICE_VOID_DET_ID = "creditInvoiceVoidDetId";
	public static final String ITEM_NET_AMOUNT = "itemNetAmount";
	public static final String ITEM_GROSS_AMOUNT = "itemGrossAmount";

}

