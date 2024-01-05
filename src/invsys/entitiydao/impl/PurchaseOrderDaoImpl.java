package invsys.entitiydao.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import invsys.controllers.database.HibernateUtil;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.entities.OrderItems;
import invsys.entities.PurchaseOrder;
import invsys.entitiydao.PurchaseOrderDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;

public class PurchaseOrderDaoImpl implements PurchaseOrderDao{

	
	private static Session session;
	
	
	private static PurchaseOrderDaoImpl daoHandler;
	
	
	//formalize method to access static method dao class
	public static PurchaseOrderDaoImpl getDao() {
		if(daoHandler == null) {
			daoHandler = new PurchaseOrderDaoImpl();
		}
		
			return daoHandler;
		
	}
	
	
	
	//Order Saving Method
	public boolean saveOrder(PurchaseOrder purchaseOrder, ObservableList<OrderItems> orderItems) {

		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();

			session.save(purchaseOrder);
			for (int i = 0; i < orderItems.toArray().length; i++) {
				session.save(orderItems.get(i));
				if (i % 50 == 0) {
					session.flush();
					session.clear();
				}

			}

			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			session.getTransaction().rollback();
			AlertHandler.getAlert(AlertType.ERROR, "Unable to Save Data",
					"Somthing went wrong please contact your administrator");
			e.printStackTrace();
			return false;
		}

	}
	
	// this method is to obtain list of purchase orders of the select purchase order button clicked on GRN GUI where POs will be loaded
	//based on the validity of Purchase order and deleted status
	public ObservableList<PurchaseOrder> getPurchaseOrderListForGrn(){
		List<PurchaseOrder> temporyList = null;
		ObservableList<PurchaseOrder> toBeReturnedList = FXCollections.observableArrayList();
		
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<PurchaseOrder> criteriaQuery = builder.createQuery(PurchaseOrder.class);
			Root<PurchaseOrder> root = criteriaQuery.from(PurchaseOrder.class);
			root.fetch("supplier", JoinType.INNER);
			root.fetch("approvedBy",JoinType.INNER);
			
			Predicate[] predict = new Predicate[3];
			predict[0] = builder.isNotNull(root.get("approvedBy"));
			predict[1] = builder.notEqual(root.get("deliverStatus"), true);
			predict[2] = builder.isNull(root.get("deletedBy"));
			criteriaQuery.select(root).where(predict);
			temporyList = session.createQuery(criteriaQuery).getResultList();
			session.getTransaction().commit();
			
			temporyList.stream().forEach(toBeReturnedList::add);
		}catch(HibernateException e) {
			AlertHandler.getAlert(AlertType.ERROR, "Cannot Retrive Purchase Order Data", "Please Contact Administrator");
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		
		return toBeReturnedList;
	}
	
		// load purchase order by order id with complex join
		public List<OrderItems> getOrderDetailsById(long orderId){
				
			List<OrderItems> orderItemsList = null;
			
			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<OrderItems> criteriaQuery = builder.createQuery(OrderItems.class);
				Root<OrderItems> root = criteriaQuery.from(OrderItems.class);
			
					 
			
			  Join<Object, Object> po = (Join<Object, Object>) root.fetch("order",JoinType.INNER); 
			  Join<Object, Object> supplier = (Join<Object, Object>) po.fetch("supplier",JoinType.INNER);
			  Join<Object, Object> user = (Join<Object, Object>) po.fetch("approvedBy",JoinType.INNER); 
			  Join<Object, Object> product = (Join<Object, Object>) root.fetch("product",JoinType.INNER);
			  Join<Object,Object> category = (Join<Object, Object>) product.fetch("category", JoinType.INNER);
			  
			 
						
				
				Predicate[] predict = new Predicate[5];
				predict[0] = builder.isNotNull(po.get("approvedBy"));
				predict[1] = builder.notEqual(po.get("deliverStatus"), true);
				predict[2] = builder.isNull(po.get("deletedBy"));
				predict[3] = builder.equal(po.get("orderId"), orderId);
				predict[4] = builder.equal(root.get("deliveryStatus"), false);
				criteriaQuery.select(root).where(predict);
				orderItemsList  = session.createQuery(criteriaQuery).getResultList();
				session.getTransaction().commit();
				
				
			}catch(Exception e) {
				AlertHandler.getAlert(AlertType.ERROR, "Cannot Retrive Purchase Order Data", "Please Contact Administrator");
				session.getTransaction().rollback();
				e.printStackTrace();
			}
			
			return orderItemsList;
		}



		@Override
		public boolean updateExpireDate(PurchaseOrder order) {
			
			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				PurchaseOrder toBeUpdatedOrder = session.get(PurchaseOrder.class, order.getOrderId());
				toBeUpdatedOrder.setExpireDate(order.getExpireDate());
				session.update(toBeUpdatedOrder);
				session.getTransaction().commit();
				return true;
			}
			catch (Exception e) {
				AlertHandler.getAlert(AlertType.ERROR, "Order Expire Date Could not be Updatedr", " deu to"+e.getLocalizedMessage());
				session.getTransaction().rollback();
				e.printStackTrace();
				return false;
			}
			
			
			
		}

}
