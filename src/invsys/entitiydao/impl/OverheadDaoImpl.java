package invsys.entitiydao.impl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import invsys.controllers.database.HibernateUtil;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.entities.MonthlyOverheads;
import invsys.entities.OverheadCategory;
import invsys.entities.compositkeys.MonthlyOverheadId;
import invsys.entitiydao.OverheadDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;

public class OverheadDaoImpl implements OverheadDao {

private static Session session;
	
private static OverheadDaoImpl daoHandler;
	
	
	//formalize method to access static method dao class
	public static OverheadDaoImpl getDao() {
		if(daoHandler == null) {
			daoHandler = new OverheadDaoImpl();
		}
		
			return daoHandler;
		}
	
	
	//save new overhead category
	public boolean saveNewOverheadCategory(OverheadCategory overheadCategory) {
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.save(overheadCategory);
			session.getTransaction().commit();
			return true;
			
		}catch(Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Cannot Create Category", e.getLocalizedMessage());
			session.getTransaction().rollback();
			e.printStackTrace();
			return false;
		}
	}
	
		//save monthly overhead
		public boolean saveMonthlyOverhead(MonthlyOverheads overhead) {
			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				session.save(overhead);
				session.getTransaction().commit();
				return true;
				
			}catch(Exception e) {
				AlertHandler.getAlert(AlertType.ERROR, "Cannot Create Overhead", e.getLocalizedMessage());
				session.getTransaction().rollback();
				e.printStackTrace();
				return false;
			}
		}
		
		//get monthly overhead by MonthlyOverheadId 
		public MonthlyOverheads getMonthlyOverheadById(MonthlyOverheadId id) {
			MonthlyOverheads tobeRetunred = null;
			try {
				
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				tobeRetunred = session.get(MonthlyOverheads.class, id);
				session.getTransaction().commit();
				
			}catch(Exception e) {
				AlertHandler.getAlert(AlertType.ERROR, "Details Could not be loaded", e.getLocalizedMessage());
				e.printStackTrace();
				session.getTransaction().rollback();
			}
			
			return tobeRetunred;
		}
		
		
		// update monthly overhead
				public boolean updateMonthlyOverhead(MonthlyOverheads monthlyOverhead) {
					MonthlyOverheads tobeRetunred = null;
					try {
						
						session = HibernateUtil.getSessionFactory().getCurrentSession();
						session.beginTransaction();
						MonthlyOverheads overHeadTobeUpdated = session.get(MonthlyOverheads.class, monthlyOverhead.getMonthlyOverheadId());
						overHeadTobeUpdated.setAmount(monthlyOverhead.getAmount());
						session.update(overHeadTobeUpdated);
						session.getTransaction().commit();
						return true;
					}catch(Exception e) {
						AlertHandler.getAlert(AlertType.ERROR, "Details Could not be loaded", e.getLocalizedMessage());
						e.printStackTrace();
						session.getTransaction().rollback();
						return false;
					}
					
					}
		
		//get overhead category by name
		public OverheadCategory getOverheadCategoryByName(String overhead) {
					try {
						session = HibernateUtil.getSessionFactory().getCurrentSession();
						session.beginTransaction();
						CriteriaBuilder builder = session.getCriteriaBuilder();
						CriteriaQuery<OverheadCategory> criteria = builder.createQuery(OverheadCategory.class);
						Root<OverheadCategory> root = criteria.from(OverheadCategory.class);
						criteria.where(builder.equal(root.get("overheadCategory"), overhead));
						OverheadCategory overheadCat = session.createQuery(criteria).getSingleResult();
						session.getTransaction().commit();
						return overheadCat;
						
					}catch(Exception e) {
						AlertHandler.getAlert(AlertType.ERROR, "Cannot Create Category", e.getLocalizedMessage());
						session.getTransaction().rollback();
						e.printStackTrace();
						return null;
					}
				}
		
		
		// get overhead categories all
		public ObservableList<String> getOverheadCatNames() {

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<String> criteria = builder.createQuery(String.class);
				Root<OverheadCategory> root = criteria.from(OverheadCategory.class);
				criteria.select(root.get("overheadCategory"));
				Query<String> query = session.createQuery(criteria);
				ObservableList<String> catNames = FXCollections.observableArrayList(query.getResultList());
				session.getTransaction().commit();
				return catNames;
			} catch (Exception e) {
				AlertHandler.getAlert(AlertType.ERROR, "Cannot Load Overhead Categories", e.getLocalizedMessage());
				session.getTransaction().rollback();
				return null;

			}

		}

}

