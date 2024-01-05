package invsys.entitiydao.impl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import invsys.controllers.database.HibernateUtil;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.entities.Category;
import invsys.entitiydao.CategoryDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class CategoryDaoImpl implements CategoryDao {

	private static Session session;
	
private static CategoryDaoImpl daoHandler;
	
	public static CategoryDaoImpl getDao() {
		if(daoHandler == null) {
			daoHandler = new CategoryDaoImpl();
		}
	
			return daoHandler;
		
	}

	public boolean saveCategory(Category category) {

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.save(category);
			session.getTransaction().commit();
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			AlertHandler.getAlert(AlertType.ERROR, "Cannot Update Data", e.getCause().toString());
			session.getTransaction().rollback();
			return false;
		}
		
	}

	public  Category getCategoryById(long id) {

		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Category category = session.get(Category.class, id);
		session.getTransaction().commit();

		return category;
	}

	public Category getCategoryByName(String catName) {

		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Query query = session.createQuery("from Category where categoryName=:name");
		query.setParameter("name", catName);
		Category category = (Category) query.uniqueResult();
		session.getTransaction().commit();

		return category;
	}

	public ObservableList<String> getCatNames() {

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<String> criteria = builder.createQuery(String.class);
			Root<Category> root = criteria.from(Category.class);
			//criteria.where(builder.notEqual(root.get("mainCategory"), "main"));
			criteria.select(root.get("categoryName"));
			Query<String> query = session.createQuery(criteria);

			ObservableList<String> catNames = FXCollections.observableArrayList(query.getResultList());
			session.getTransaction().commit();
			return catNames;
		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Cannot Load  Categories", e.getLocalizedMessage());
			session.getTransaction().rollback();
			return null;

		}

	}

	public ObservableList<Object[]> getCatIdAndName() {

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Object[]> criteria = builder.createQuery(Object[].class);
			Root<Category> root = criteria.from(Category.class);
			//criteria.where(builder.notEqual(root.get("categoryType"), "main"));
			criteria.multiselect(root.get("categoryName"), root.get("category_id"));
			Query<Object[]> query = session.createQuery(criteria);

			ObservableList<Object[]> catNames = FXCollections.observableArrayList(query.getResultList());
			session.getTransaction().commit();
			return catNames;

		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText("Hibernate Error-Cannot Load Category list");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
			session.getTransaction().rollback();
			return null;
		}

	}

	@Override
	public boolean updateCategory(Category category) {
		
		return false;
	}

	@Override
	public boolean deleteCategory(Category category) {
		
		return false;
	}

	@Override
	public ObservableList<String> getMainCategories() {
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<String> criteria = builder.createQuery(String.class);
			Root<Category> root = criteria.from(Category.class);
			criteria.where(builder.equal(root.get("categoryType"), "main"));
			criteria.select(root.get("categoryName"));
			Query<String> query = session.createQuery(criteria);

			ObservableList<String> catNames = FXCollections.observableArrayList(query.getResultList());
			session.getTransaction().commit();
			return catNames;
		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Cannot Load  Categories", e.getLocalizedMessage());
			session.getTransaction().rollback();
			return null;

		}
	}
}
