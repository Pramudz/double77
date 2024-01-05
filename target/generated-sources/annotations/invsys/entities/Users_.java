package invsys.entities;

import java.sql.Date;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Users.class)
public abstract class Users_ {

	public static volatile SingularAttribute<Users, Date> date;
	public static volatile SingularAttribute<Users, String> lastName;
	public static volatile SingularAttribute<Users, Date> lastLogin;
	public static volatile SingularAttribute<Users, String> address;
	public static volatile SetAttribute<Users, Role> role;
	public static volatile SingularAttribute<Users, String> userName;
	public static volatile SingularAttribute<Users, Boolean> loginStatus;
	public static volatile SingularAttribute<Users, Boolean> activeStat;
	public static volatile SingularAttribute<Users, Integer> contactNum;
	public static volatile SingularAttribute<Users, String> firstName;
	public static volatile SingularAttribute<Users, String> password;
	public static volatile SingularAttribute<Users, Integer> user_id;
	public static volatile SingularAttribute<Users, String> userEmail;

	public static final String DATE = "date";
	public static final String LAST_NAME = "lastName";
	public static final String LAST_LOGIN = "lastLogin";
	public static final String ADDRESS = "address";
	public static final String ROLE = "role";
	public static final String USER_NAME = "userName";
	public static final String LOGIN_STATUS = "loginStatus";
	public static final String ACTIVE_STAT = "activeStat";
	public static final String CONTACT_NUM = "contactNum";
	public static final String FIRST_NAME = "firstName";
	public static final String PASSWORD = "password";
	public static final String USER_ID = "user_id";
	public static final String USER_EMAIL = "userEmail";

}

