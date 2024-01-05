package invsys.entitiydao.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import invsys.controllers.database.HibernateUtil;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.entities.Supplier;
import invsys.entities.SupplierBrImages;
import invsys.entitiydao.SupplierDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SupplierDaoImpl implements SupplierDao {

	private static Session session;
	
	private static SupplierDaoImpl daoHandler;
	
	
	//formalize method to access static method dao class
	public static SupplierDaoImpl getDao() {
		if(daoHandler == null) {
			daoHandler = new SupplierDaoImpl();
		}
		
			return daoHandler;
		
	}

	/*
	 * // insert into supplier data to database specific method for inserting
	 * Supplier Data public boolean saveSupplier(Supplier supplier ,
	 * HashMap<ImageView ,byte[]> images) { try { session =
	 * HibernateUtil.getSessionFactory().getCurrentSession();
	 * session.beginTransaction(); session.save(supplier); for(Entry<ImageView,
	 * byte[]> x : images.entrySet()) { SupplierBrImages brImages = new
	 * SupplierBrImages(); brImages.setSupplier(supplier);
	 * brImages.setImage(BlobProxy.generateProxy(x.getValue()));
	 * session.save(brImages); } session.getTransaction().commit(); return true; }
	 * catch (Exception e) { session.getTransaction().rollback(); Alert alert = new
	 * Alert(AlertType.ERROR); alert.setHeaderText("Cannot Insert Data");
	 * alert.setContentText(e.getMessage()); alert.showAndWait(); return false; }
	 * 
	 * }
	 */
	
	
		public boolean saveSupplier(Supplier supplier) {
			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				session.persist(supplier);
				session.getTransaction().commit();
				return true;
			} catch (Exception e) {
				session.getTransaction().rollback();
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Cannot Insert Data");
				alert.setContentText(e.getMessage());
				alert.showAndWait();
				e.printStackTrace();
				return false;
			}

		}
	
	//get Supplierentity with image enitty using Object observable list using core sql query at once
	public ObservableList<Object> getSupplierWithImages(int id) {
		List<Object> temporyList = null;
		ObservableList<Object> tobeReturnedList = FXCollections.observableArrayList();
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Query query = session.createQuery("SELECT sup FROM Supplier sup LEFT JOIN FETCH  SupplierBrImages images ON sup=images.suppplierForMapImage WHERE sup.sup_id=:sid");
			//Query query = session.createQuery("SELECT i FROM SupplierBrImages i RIGHT JOIN FETCH i.suppplierForMapImage s WHERE s.sup_id=:sid");
			query.setParameter("sid", id);
			temporyList = query.getResultList();
			session.getTransaction().commit();
			temporyList.stream().forEach(tobeReturnedList::add);
		
		} catch (Exception e) {
			session.getTransaction().rollback();
		}
		return tobeReturnedList;
		
	}
	
	public ObservableList<SupplierBrImages> getImages(Supplier supplier){
		List<SupplierBrImages> temporyList = null;
		ObservableList<SupplierBrImages> tobeReturnedList = FXCollections.observableArrayList();
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Query query = session.createQuery("FROM SupplierBrImages WHERE suppplierForMapImage=:sup");
			query.setParameter("sup", supplier);
			temporyList = query.getResultList();
			session.getTransaction().commit();
			temporyList.stream().forEach(tobeReturnedList::add);
		}catch(Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Cannot Retrive Image Data", e.getMessage());
			session.getTransaction().rollback();
		
		}
		return tobeReturnedList;
	}
	
	// return list of the supplier
	public ObservableList<Supplier> getSuppliers() {
		List<Supplier> suppliers = null;
		ObservableList<Supplier> listOfSuppler = FXCollections.observableArrayList();
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			suppliers = session.createQuery("from Supplier").list();
			session.getTransaction().commit();
			suppliers.stream().forEach(listOfSuppler::add);

		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Cannot Retrive Data", e.getMessage());
			session.getTransaction().rollback();
		}
		return listOfSuppler;
	}

	// get supplier by id method with Images
	public Supplier getSupplierByIdWithImages(int id) {
		
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Query query = session.createQuery("SELECT sup FROM Supplier sup LEFT JOIN FETCH sup.supplierBrImages r WHERE sup.sup_id=:sid");
			query.setParameter("sid", id);
			Supplier supplier = (Supplier) query.getSingleResult();
			session.getTransaction().commit();
			return supplier;
		
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
			return null;
		}
		

	}
	
	// get supplier by id method
		public Supplier getSupplierById(int id) {
			
			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				
				Supplier supplier = session.get(Supplier.class, id);
				session.getTransaction().commit();
				return supplier;
			
			} catch (Exception e) {
				session.getTransaction().rollback();
				return null;
			}
			

		}

	// update supplier by id method

	public boolean updateSupplier(Supplier supplier) {
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			
			
			Query query = session.createQuery("SELECT sup FROM Supplier sup LEFT JOIN FETCH sup.supplierBrImages r WHERE sup.sup_id=:sid");
			query.setParameter("sid", supplier.getSup_id());
			Supplier updateSupplier = (Supplier) query.getSingleResult();
			updateSupplier.setCom_name(supplier.getCom_name());
			updateSupplier.setContact_fname(supplier.getContact_fname());
			updateSupplier.setContact_lname(supplier.getContact_lname());
			updateSupplier.setContact_person_desig(supplier.getContact_person_desig());
			updateSupplier.setStreetAddress(supplier.getStreetAddress());
			updateSupplier.setEmail(supplier.getEmail());
			updateSupplier.setCity(supplier.getCity());
			updateSupplier.setMobile_no(supplier.getMobile_no());
			updateSupplier.setTelephone(supplier.getTelephone());
			updateSupplier.setAddressLine01(supplier.getAddressLine01());
			updateSupplier.setPayment_period(supplier.getPayment_period());
			
			for(SupplierBrImages x : updateSupplier.getSupplierBrImages()) {
					session.delete(x);
				}
			
			updateSupplier.setSupplierBrImages(supplier.getSupplierBrImages());
			session.merge(updateSupplier);
			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText("Supplier Cannot Update-Somthing Went Wrong !");
			e.printStackTrace();
			alert.setContentText(e.getMessage());
			alert.showAndWait();
			session.getTransaction().rollback();
			return false;
		}

	}
	
	
	//update modify
	public boolean updateSupplierTextOnly(Supplier supplier) {
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Supplier updateSupplier = session.get(Supplier.class, supplier.getSup_id());
			updateSupplier.setCom_name(supplier.getCom_name());
			updateSupplier.setContact_fname(supplier.getContact_fname());
			updateSupplier.setContact_lname(supplier.getContact_lname());
			updateSupplier.setContact_person_desig(supplier.getContact_person_desig());
			updateSupplier.setStreetAddress(supplier.getStreetAddress());
			updateSupplier.setEmail(supplier.getEmail());
			updateSupplier.setCity(supplier.getCity());
			updateSupplier.setMobile_no(supplier.getMobile_no());
			updateSupplier.setTelephone(supplier.getTelephone());
			updateSupplier.setAddressLine01(supplier.getAddressLine01());
			updateSupplier.setPayment_period(supplier.getPayment_period());
			updateSupplier.setBrNumber(supplier.getBrNumber());
			session.merge(updateSupplier);
			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText("Supplier Cannot Update-Somthing Went Wrong !");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
			session.getTransaction().rollback();
			return false;
		}

	}

	// get suppplier by sup name
	public Supplier getSupplierByName(String spName) {

		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Query query = session.createQuery("from Supplier where com_name=:name");
		query.setParameter("name", spName);
		Supplier supplier = (Supplier) query.uniqueResult();
		session.getTransaction().commit();

		return supplier;
	}

	// get supplier names list only
	public ObservableList<String> getSupName() {

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<String> criteria = builder.createQuery(String.class);
			Root<Supplier> root = criteria.from(Supplier.class);
			criteria.select(root.get("com_name"));
			Query<String> query = session.createQuery(criteria);

			ObservableList<String> spNames = FXCollections.observableArrayList(query.getResultList());
			session.getTransaction().commit();
			return spNames;

		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText("Hibernate Error-Cannot Load Category list");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
			session.getTransaction().rollback();
			return null;
		}

	}

	
	// get supplier id & name
	public ObservableList<Object[]> getSupIdAndName() {

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Object[]> criteria = builder.createQuery(Object[].class);
			Root<Supplier> root = criteria.from(Supplier.class);
			criteria.multiselect(root.get("com_name"), root.get("sup_id"));
			Query<Object[]> query = session.createQuery(criteria);

			ObservableList<Object[]> spNames = FXCollections.observableArrayList(query.getResultList());
			session.getTransaction().commit();
			return spNames;

		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText("Hibernate Error-Cannot Load Category list");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
			session.getTransaction().rollback();
			return null;
		}

	}
	
	// Delete Supplier Regular Method
		public int deleteSupplier(Supplier sup) {
			Integer id = null;
			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				for(SupplierBrImages y : sup.getSupplierBrImages()) {
					session.delete(y);
				}
				session.delete(sup);
				id = sup.getSup_id();
				session.getTransaction().commit();
				return id;
			} catch (Exception e) {
				AlertHandler.getAlert(AlertType.ERROR, "Cannot Insert Data", e.getMessage());
				e.printStackTrace();
				return id;
			}

		}

	
}
