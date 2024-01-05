package invsys.entities;

import java.sql.Blob;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Company.class)
public abstract class Company_ {

	public static volatile SingularAttribute<Company, String> province;
	public static volatile SingularAttribute<Company, String> companyEmail;
	public static volatile SingularAttribute<Company, Integer> compId;
	public static volatile SingularAttribute<Company, String> telephoneNum;
	public static volatile SingularAttribute<Company, Blob> companyLogo;
	public static volatile SingularAttribute<Company, String> companyName;
	public static volatile SingularAttribute<Company, String> faxNumber;
	public static volatile SingularAttribute<Company, String> addressLine1;
	public static volatile SingularAttribute<Company, String> addressLine2;
	public static volatile SingularAttribute<Company, String> addressLine3;
	public static volatile SingularAttribute<Company, String> companyRegNum;

	public static final String PROVINCE = "province";
	public static final String COMPANY_EMAIL = "companyEmail";
	public static final String COMP_ID = "compId";
	public static final String TELEPHONE_NUM = "telephoneNum";
	public static final String COMPANY_LOGO = "companyLogo";
	public static final String COMPANY_NAME = "companyName";
	public static final String FAX_NUMBER = "faxNumber";
	public static final String ADDRESS_LINE1 = "addressLine1";
	public static final String ADDRESS_LINE2 = "addressLine2";
	public static final String ADDRESS_LINE3 = "addressLine3";
	public static final String COMPANY_REG_NUM = "companyRegNum";

}

