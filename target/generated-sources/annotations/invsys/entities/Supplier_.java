package invsys.entities;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Supplier.class)
public abstract class Supplier_ {

	public static volatile SingularAttribute<Supplier, String> city;
	public static volatile SingularAttribute<Supplier, Integer> mobile_no;
	public static volatile SingularAttribute<Supplier, Boolean> blockStatus;
	public static volatile SingularAttribute<Supplier, Integer> telephone;
	public static volatile SingularAttribute<Supplier, String> contact_lname;
	public static volatile SingularAttribute<Supplier, Integer> sup_id;
	public static volatile SingularAttribute<Supplier, String> brNumber;
	public static volatile SingularAttribute<Supplier, String> contact_fname;
	public static volatile SingularAttribute<Supplier, String> streetAddress;
	public static volatile SingularAttribute<Supplier, Integer> payment_period;
	public static volatile SingularAttribute<Supplier, String> addressLine01;
	public static volatile SetAttribute<Supplier, SupplierBrImages> supplierBrImages;
	public static volatile SingularAttribute<Supplier, String> contact_person_desig;
	public static volatile SingularAttribute<Supplier, String> email;
	public static volatile SingularAttribute<Supplier, String> com_name;

	public static final String CITY = "city";
	public static final String MOBILE_NO = "mobile_no";
	public static final String BLOCK_STATUS = "blockStatus";
	public static final String TELEPHONE = "telephone";
	public static final String CONTACT_LNAME = "contact_lname";
	public static final String SUP_ID = "sup_id";
	public static final String BR_NUMBER = "brNumber";
	public static final String CONTACT_FNAME = "contact_fname";
	public static final String STREET_ADDRESS = "streetAddress";
	public static final String PAYMENT_PERIOD = "payment_period";
	public static final String ADDRESS_LINE01 = "addressLine01";
	public static final String SUPPLIER_BR_IMAGES = "supplierBrImages";
	public static final String CONTACT_PERSON_DESIG = "contact_person_desig";
	public static final String EMAIL = "email";
	public static final String COM_NAME = "com_name";

}

