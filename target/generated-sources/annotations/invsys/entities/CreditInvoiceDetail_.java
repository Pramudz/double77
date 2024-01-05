package invsys.entities;

import invsys.entities.compositkeys.CreditInvoiceDetailId;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CreditInvoiceDetail.class)
public abstract class CreditInvoiceDetail_ {

	public static volatile SingularAttribute<CreditInvoiceDetail, Double> unitPrice;
	public static volatile SingularAttribute<CreditInvoiceDetail, Double> discountPercentage;
	public static volatile SingularAttribute<CreditInvoiceDetail, Products> product;
	public static volatile SingularAttribute<CreditInvoiceDetail, CreditInvoiceDetailId> creditInvoiceId;
	public static volatile SingularAttribute<CreditInvoiceDetail, Double> itemVat;
	public static volatile SingularAttribute<CreditInvoiceDetail, Double> discountedPrice;
	public static volatile SingularAttribute<CreditInvoiceDetail, Double> costPrice;
	public static volatile SingularAttribute<CreditInvoiceDetail, Double> salesQty;
	public static volatile SingularAttribute<CreditInvoiceDetail, Double> discount;
	public static volatile SingularAttribute<CreditInvoiceDetail, Double> averageCostPrice;
	public static volatile SingularAttribute<CreditInvoiceDetail, Double> itemNetAmount;
	public static volatile SingularAttribute<CreditInvoiceDetail, Double> itemGrossAmount;

	public static final String UNIT_PRICE = "unitPrice";
	public static final String DISCOUNT_PERCENTAGE = "discountPercentage";
	public static final String PRODUCT = "product";
	public static final String CREDIT_INVOICE_ID = "creditInvoiceId";
	public static final String ITEM_VAT = "itemVat";
	public static final String DISCOUNTED_PRICE = "discountedPrice";
	public static final String COST_PRICE = "costPrice";
	public static final String SALES_QTY = "salesQty";
	public static final String DISCOUNT = "discount";
	public static final String AVERAGE_COST_PRICE = "averageCostPrice";
	public static final String ITEM_NET_AMOUNT = "itemNetAmount";
	public static final String ITEM_GROSS_AMOUNT = "itemGrossAmount";

}

