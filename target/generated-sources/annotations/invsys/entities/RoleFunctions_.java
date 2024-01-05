package invsys.entities;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RoleFunctions.class)
public abstract class RoleFunctions_ {

	public static volatile SetAttribute<RoleFunctions, Role> rolelist;
	public static volatile SingularAttribute<RoleFunctions, String> roleFunction;
	public static volatile SingularAttribute<RoleFunctions, Boolean> roleAcess;
	public static volatile SingularAttribute<RoleFunctions, Integer> id;
	public static volatile SingularAttribute<RoleFunctions, RoleFunctions> mainRoleFunction;

	public static final String ROLELIST = "rolelist";
	public static final String ROLE_FUNCTION = "roleFunction";
	public static final String ROLE_ACESS = "roleAcess";
	public static final String ID = "id";
	public static final String MAIN_ROLE_FUNCTION = "mainRoleFunction";

}

