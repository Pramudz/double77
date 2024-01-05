package invsys.entities;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Role.class)
public abstract class Role_ {

	public static volatile SetAttribute<Role, RoleFunctions> roleFunctions;
	public static volatile SingularAttribute<Role, String> roleDesc;
	public static volatile SingularAttribute<Role, Integer> roleId;
	public static volatile SingularAttribute<Role, String> roleName;
	public static volatile SetAttribute<Role, Users> users;

	public static final String ROLE_FUNCTIONS = "roleFunctions";
	public static final String ROLE_DESC = "roleDesc";
	public static final String ROLE_ID = "roleId";
	public static final String ROLE_NAME = "roleName";
	public static final String USERS = "users";

}

