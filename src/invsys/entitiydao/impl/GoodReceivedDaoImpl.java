package invsys.entitiydao.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.hibernate.Session;

import invsys.controllers.database.HibernateUtil;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.entities.GoodReceived;
import invsys.entities.GoodReceivedDetails;
import invsys.entities.OrderItems;
import invsys.entities.Products;
import invsys.entities.PurchaseOrder;
import invsys.entitiydao.GoodReceivedDao;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;

public class GoodReceivedDaoImpl implements GoodReceivedDao {

private static Session session;
	
	
	private static GoodReceivedDaoImpl daoHandler;
	
	
	//formalize method to access static method dao class
	public static GoodReceivedDaoImpl getDao() {
		if(daoHandler == null) {
			daoHandler = new GoodReceivedDaoImpl();
		}
		
			return daoHandler;
	
	}
	
	
	//GRN Saving Method 
	//since only one product can be inserted to table view this method work
	//other wise new product should be obtained from database to update on hand qty Ex Products prod = session.get(Prodcuts.class , id)
	public boolean saveGrn(GoodReceived grn, ObservableList<GoodReceivedDetails> grnDets , List<OrderItems> orderItems ,boolean partialDelStatus, boolean deliverStatus) {

		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();

			session.save(grn);
			for (int i = 0; i < grnDets.toArray().length; i++) {
				Products prd = grnDets.get(i).getProduct();
				double oldQty = prd.getOnHandQty();
				double newQty = grnDets.get(i).getReceivedQty();
				double nowQty = oldQty+ newQty;
				
				double grnItemPrice = grnDets.get(i).getCostPrice();
				double oldtemAveragePrice = prd.getUnitAverageCost();
			
				if(oldQty <=0) {
					oldQty = 1;
					
				}
				double newAveragePrice = ((grnItemPrice*newQty) + (oldtemAveragePrice*oldQty))/(newQty+oldQty);
				double towDecimalValueToInject = new BigDecimal(newAveragePrice).setScale(2, RoundingMode.HALF_UP).doubleValue();
				prd.setUnitAverageCost(towDecimalValueToInject);
				prd.setOnHandQty(nowQty);
				session.update(prd);
				session.save(grnDets.get(i));
					
				if (i % 50 == 0) {
					session.flush();
					session.clear();
				}

			}
			PurchaseOrder mainOrder = orderItems.get(0).getOrder();
			mainOrder.setPartialStatus(partialDelStatus);
			mainOrder.setDeliverStatus(deliverStatus);
			session.update(mainOrder);
			
			for(int i =0; i <orderItems.toArray().length; i++) {
				
				session.update(orderItems.get(i));
				
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
}
