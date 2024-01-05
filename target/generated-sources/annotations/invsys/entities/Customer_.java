package invsys.entities;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Customer.class)
public abstract class Customer_ {

	public static volatile SingularAttribute<Customer, String> lastName;
	public static volatile SingularAttribute<Customer, String> city;
	public static volatile SingularAttribute<Customer, String> companyName;
	public static volatile SingularAttribute<Customer, String> customerTelephone;
	public static volatile SingularAttribute<Customer, Boolean> blockStatus;
	public static volatile SingularAttribute<Customer, String> firstName;
	public static volatile SingularAttribute<Customer, String> streetAddress;
	public static volatile SingularAttribute<Customer, String> customerEmail;
	public static volatile SingularAttribute<Customer, Long> customerId;
	public static volatile SingularAttribute<Customer, String> nicNumber;
	public static volatile SingularAttribute<Customer, String> addressLine02;
	public static volatile SingularAttribute<Customer, String> customerMobile;
	public static volatile SingularAttribute<Customer, Integer> customerPoints;

	public static final String LAST_NAME = "lastName";
	public static final String CITY = "city";
	public static final String COMPANY_NAME = "companyName";
	public static final String CUSTOMER_TELEPHONE = "customerTelephone";
	public static final String BLOCK_STATUS = "blockStatus";
	public static final String FIRST_NAME = "firstName";
	public static final String STREET_ADDRESS = "streetAddress";
	public static final String CUSTOMER_EMAIL = "customerEmail";
	public static final String CUSTOMER_ID = "customerId";
	public static final String NIC_NUMBER = "nicNumber";
	public static final String ADDRESS_LINE02 = "addressLine02";
	public static final String CUSTOMER_MOBILE = "customerMobile";
	public static final String CUSTOMER_POINTS = "customerPoints";

}

