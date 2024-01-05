package invsys.entitiydao.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import invsys.controllers.database.HibernateUtil;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.entities.PayModes;
import invsys.entitiydao.PayModeDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;

public class PayModeDaoImpl implements PayModeDao{

	
private static Session session;

	
	
	private static PayModeDaoImpl daoHandler;
	
	
	//formalize method to access static method dao class
	public static PayModeDaoImpl getDao() {
		if(daoHandler == null) {
			daoHandler = new PayModeDaoImpl();
		}
		
			return daoHandler;
	
	}
	// insert into Pay Mode data to database
	public boolean savePayMode(PayModes payMode) {
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.save(payMode);
			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			
			AlertHandler.getAlert(AlertType.ERROR, "SQL Error"+e.getCause(), e.getMessage());
			e.printStackTrace();
			session.getTransaction().rollback();
			return false;
		}

	}
	
	// get payMode by id method
		public PayModes getPayModeById(String id) {
			PayModes payMode = null;
			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				payMode = session.get(PayModes.class, id);
				session.getTransaction().commit();
			} catch (Exception e) {
				AlertHandler.getAlert(AlertType.ERROR, "SQL Error"+e.getCause(), e.getMessage());
				e.printStackTrace();
				session.getTransaction().rollback();
				
			}
			return payMode;

		}
		
		//get all paymode ids
		public ObservableList<String> getAllPayModes(){
			
			try {
				List<String> payModes = null;
				ObservableList<String> payModesReturn = FXCollections.observableArrayList();
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<String> critQuery = builder.createQuery(String.class);
				Root<PayModes> root = critQuery.from(PayModes.class);
				critQuery.select(root.get("modeId"));
				payModes = session.createQuery(critQuery).getResultList();
				session.getTransaction().commit();
				payModes.stream().forEach(payModesReturn::add);
				return payModesReturn;
			}
			catch(Exception e) {
				AlertHandler.getAlert(AlertType.ERROR, "Data Access Or SQL Error"+e.getCause(), e.getMessage());
				e.printStackTrace();
				session.getTransaction().rollback();
				return null;
			}
			
			
		}
		
		//get Main Paymodes
		public ObservableList<String> getMainPayModes(){
			
			try {
				List<String> payModes = null;
				ObservableList<String> payModesReturn = FXCollections.observableArrayList();
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<String> critQuery = builder.createQuery(String.class);
				Root<PayModes> root = critQuery.from(PayModes.class);
				critQuery.where(builder.isNull(root.get("mainPayMode")));
				critQuery.select(root.get("modeId"));
				payModes = session.createQuery(critQuery).getResultList();
				session.getTransaction().commit();
				payModes.stream().forEach(payModesReturn::add);
				return payModesReturn;
			}
			catch(Exception e) {
				AlertHandler.getAlert(AlertType.ERROR, "Data Access Or SQL Error"+e.getCause(), e.getMessage());
				e.printStackTrace();
				session.getTransaction().rollback();
				return null;
			}
			
			
		}
		
		//get Child Paymodes
				public ObservableList<String> getChildPayModesByMain(String mainPayMode){
					
					try {
						List<String> payModes = null;
						ObservableList<String> payModesReturn = FXCollections.observableArrayList();
						session = HibernateUtil.getSessionFactory().getCurrentSession();
						session.beginTransaction();
						CriteriaBuilder builder = session.getCriteriaBuilder();
						CriteriaQuery<String> critQuery = builder.createQuery(String.class);
						Root<PayModes> root = critQuery.from(PayModes.class);
						critQuery.where(builder.equal(root.get("mainPayMode").get("modeId"), mainPayMode));
						critQuery.select(root.get("modeId"));
						payModes = session.createQuery(critQuery).getResultList();
						session.getTransaction().commit();
						payModes.stream().forEach(payModesReturn::add);
						return payModesReturn;
					}
					catch(Exception e) {
						AlertHandler.getAlert(AlertType.ERROR, "Data Access Or SQL Error"+e.getCause(), e.getMessage());
						e.printStackTrace();
						session.getTransaction().rollback();
						return null;
					}
					
					
				}

}
