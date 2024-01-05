package invsys.entitiydao.impl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import invsys.controllers.database.HibernateUtil;
import invsys.entities.GoodReceived;
import invsys.entities.Supplier;
import invsys.entities.SupplierPaymentDetail;
import invsys.entities.SupplierPayments;
import invsys.entities.SupplierReturn;
import invsys.entitiydao.SupplierPaymentDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SupplierPaymentDaoImpl implements SupplierPaymentDao {

	private static Session session;

	private static SupplierPaymentDaoImpl daoHandler;

	// formalize method to access static method dao class
	public static SupplierPaymentDaoImpl getDao() {
		if (daoHandler == null) {
			daoHandler = new SupplierPaymentDaoImpl();
		} 
			return daoHandler;
		
	}
	
	
	// save payment details with propogating other data
	
	public boolean savePayment(SupplierPayments supMassPayment , ObservableList<SupplierPaymentDetail> payDetailList) {
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			
			session.save(supMassPayment);
			
			for (int i = 0; i < payDetailList.toArray().length; i++) {
				session.save(payDetailList.get(i));
				String docType = payDetailList.get(i).getDocumentType();
				long grnRonId = payDetailList.get(i).getGrnRonId();
				
				if(docType.equals("GRN")) {
					GoodReceived grnUpdate = session.get(GoodReceived.class, grnRonId);
					grnUpdate.setPaidStatus(true);
					session.update(grnUpdate);
				}
				
				if(docType.equals("RON")) {
					SupplierReturn supReturnUpdate = session.get(SupplierReturn.class, grnRonId);
					supReturnUpdate.setPaymentUtilizedStatus(true);
					session.update(supReturnUpdate);
				}
					
				if (i % 50 == 0) {
					session.flush();
					session.clear();
				}

			}
			session.getTransaction().commit();
			return true;
		} catch(Exception e){
			session.getTransaction().rollback();
			e.printStackTrace();
			return false;
		}
		
		
	}

	// get GRN details for Supplier Payment Window
	public double getSumOfGrnPayableToSupplier(Supplier supplier) {

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Double> grnCriteria = builder.createQuery(Double.class);
			Root<GoodReceived> root = grnCriteria.from(GoodReceived.class);
			Join<Object, Object> supp = root.join("supplier");

			Predicate[] predicate = new Predicate[2];

			predicate[0] = builder.equal(root.get("supplier"), supplier);
			predicate[1] = builder.equal(root.get("paidStatus"), false);
			grnCriteria.where(predicate);
			grnCriteria.select(builder.sumAsDouble(root.get("grnAmount")));
			double result = session.createQuery(grnCriteria).getSingleResult();
			session.getTransaction().commit();

			return result;

		} catch (Exception e) {
			session.getTransaction().rollback();
			return 0;
		}

	}

	// get RON details for Supplier Payment Window
	public double getSumRonReturnableToSupplier(Supplier supplier) {

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Double> ronCriteria = builder.createQuery(Double.class);
			Root<SupplierReturn> root = ronCriteria.from(SupplierReturn.class);
			Join<Object, Object> supp = root.join("supplier");

			Predicate[] predicate = new Predicate[2];

			predicate[0] = builder.equal(root.get("supplier"), supplier);
			predicate[1] = builder.equal(root.get("paymentUtilizedStatus"), false);
			ronCriteria.where(predicate);
			ronCriteria.select(builder.sumAsDouble(root.get("returnAmount")));
			double result = session.createQuery(ronCriteria).getSingleResult();

			session.getTransaction().commit();

			return result;

		} catch (Exception e) {
			session.getTransaction().rollback();
			return 0;
		}

	}

	// get SumofAmount Payable to Supplier
	public ObservableList<Object[]> getGrnForPayments(Supplier supplier) {

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Object[]> grnCriteria = builder.createQuery(Object[].class);
			Root<GoodReceived> root = grnCriteria.from(GoodReceived.class);
			Join<Object, Object> supp = root.join("supplier");

			Predicate[] predicate = new Predicate[2];

			predicate[0] = builder.equal(root.get("supplier"), supplier);
			predicate[1] = builder.equal(root.get("paidStatus"), false);
			grnCriteria.where(predicate);
			grnCriteria.multiselect(root.get("grnId"), root.get("documentType"), root.get("grnDate"),
					root.get("grnAmount"));
			Query<Object[]> query = session.createQuery(grnCriteria);
			ObservableList<Object[]> spNames = FXCollections.observableArrayList(query.getResultList());
			session.getTransaction().commit();

			return spNames;

		} catch (Exception e) {
			session.getTransaction().rollback();
		}
		return null;

	}
	
	// get Single GRN Object for Payment
		public GoodReceived getSingleGrnObjectForPayment(Supplier supplier , long grnId) {

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<GoodReceived> grnCriteria = builder.createQuery(GoodReceived.class);
				Root<GoodReceived> root = grnCriteria.from(GoodReceived.class);
				Join<Object, Object> supp = root.join("supplier");

				Predicate[] predicate = new Predicate[3];

				predicate[0] = builder.equal(root.get("supplier"), supplier);
				predicate[1] = builder.equal(root.get("paidStatus"), false);
				predicate[2] = builder.equal(root.get("grnId"), grnId);
				grnCriteria.where(predicate);
				grnCriteria.select(root);
				GoodReceived obj = session.createQuery(grnCriteria).getSingleResult();
				session.getTransaction().commit();

				return obj;

			} catch (Exception e) {
				session.getTransaction().rollback();
				e.printStackTrace();
				return null;
			}
			

		}

	// get sumof Amount returnable to Supplier
	public ObservableList<Object[]> getRonForSupplierPayments(Supplier supplier) {

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Object[]> ronCriteria = builder.createQuery(Object[].class);
			Root<SupplierReturn> root = ronCriteria.from(SupplierReturn.class);
			Join<Object, Object> supp = root.join("supplier");

			Predicate[] predicate = new Predicate[2];

			predicate[0] = builder.equal(root.get("supplier"), supplier);
			predicate[1] = builder.equal(root.get("paymentUtilizedStatus"), false);
			ronCriteria.where(predicate);
			ronCriteria.multiselect(root.get("supReturnId"), root.get("documentType"), root.get("returnDate"),
					root.get("returnAmount"));
			Query<Object[]> query = session.createQuery(ronCriteria);
			ObservableList<Object[]> spNames = FXCollections.observableArrayList(query.getResultList());
			session.getTransaction().commit();

			return spNames;

		} catch (Exception e) {
			session.getTransaction().rollback();
			return null;
		}
		

	}
	
	
	//  get Single RON Object for Payment
		public SupplierReturn getSingleRonObjectForPayment(Supplier supplier , long ronId) {

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<SupplierReturn> ronCriteria = builder.createQuery(SupplierReturn.class);
				Root<SupplierReturn> root = ronCriteria.from(SupplierReturn.class);
				Join<Object, Object> supp = root.join("supplier");

				Predicate[] predicate = new Predicate[3];

				predicate[0] = builder.equal(root.get("supplier"), supplier);
				predicate[1] = builder.equal(root.get("paymentUtilizedStatus"), false);
				predicate[2] = builder.equal(root.get("supReturnId"), ronId);
				ronCriteria.where(predicate);
				ronCriteria.select(root);
				SupplierReturn obj = session.createQuery(ronCriteria).getSingleResult();
				
				session.getTransaction().commit();

				return obj;

			} catch (Exception e) {
				session.getTransaction().rollback();
				return null;
			}
			

		}
	
	
}
