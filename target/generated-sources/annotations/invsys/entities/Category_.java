package invsys.entities;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Category.class)
public abstract class Category_ {

	public static volatile SingularAttribute<Category, String> categoryType;
	public static volatile SingularAttribute<Category, Long> category_id;
	public static volatile SingularAttribute<Category, Category> mainCategory;
	public static volatile SingularAttribute<Category, String> categoryName;
	public static volatile SingularAttribute<Category, String> categoryDescription;

	public static final String CATEGORY_TYPE = "categoryType";
	public static final String CATEGORY_ID = "category_id";
	public static final String MAIN_CATEGORY = "mainCategory";
	public static final String CATEGORY_NAME = "categoryName";
	public static final String CATEGORY_DESCRIPTION = "categoryDescription";

}

