package invsys.entitiydao.impl;

import org.hibernate.Session;

import invsys.controllers.database.HibernateUtil;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.entities.Products;
import invsys.entities.SupplierReturn;
import invsys.entities.SupplierReturnDetail;
import invsys.entitiydao.SupplierReturnDao;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;

public class SupplierReturnDaoImpl implements SupplierReturnDao {
	
	private static Session session;
	
	private static SupplierReturnDaoImpl daohandler;
	
	
	public static SupplierReturnDaoImpl getDao() {
		if(daohandler == null) {
			daohandler = new SupplierReturnDaoImpl();
		}
		return daohandler;
	}


				//save Supplier Return data boolean
				public boolean saveSupplierReturn(SupplierReturn supReturn, ObservableList<SupplierReturnDetail> supReturnDetails) {
					ProductDaoImpl.getDao();
					try {
						session = HibernateUtil.getSessionFactory().getCurrentSession();
						session.beginTransaction();
						session.save(supReturn);
									
						for (int i = 0; i < supReturnDetails.toArray().length; i++) {
							session.save(supReturnDetails.get(i));
							Products prd = supReturnDetails.get(i).getProduct();
													
							Products prdUpdate = session.get(Products.class, prd.getPrd_id());
							prdUpdate.setOnHandQty(prdUpdate.getOnHandQty()-supReturnDetails.get(i).getReturnQty());
							session.update(prdUpdate);
														
							
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
				
				//save Supplier Return data long
				public long saveSupplierReturnWithReturnId(SupplierReturn supReturn, ObservableList<SupplierReturnDetail> supReturnDetails) {
					Long supReturnNumber = null;
					ProductDaoImpl.getDao();
					try {
						session = HibernateUtil.getSessionFactory().getCurrentSession();
						session.beginTransaction();
						session.save(supReturn);
									
						for (int i = 0; i < supReturnDetails.toArray().length; i++) {
							session.save(supReturnDetails.get(i));
							Products prd = supReturnDetails.get(i).getProduct();
													
							Products prdUpdate = session.get(Products.class, prd.getPrd_id());
							prdUpdate.setOnHandQty(prdUpdate.getOnHandQty()-supReturnDetails.get(i).getReturnQty());
							session.update(prdUpdate);			
							
							if (i % 50 == 0) {
								session.flush();
								session.clear();
							}

						}
						
						supReturnNumber = supReturn.getSupReturnId();
						session.getTransaction().commit();
						return supReturnNumber;
					} catch (Exception e) {
						session.getTransaction().rollback();
						AlertHandler.getAlert(AlertType.ERROR, "Unable to Save Data",
								"Somthing went wrong please contact your administrator");
						e.printStackTrace();
						return (Long) null;
					}

				}
	
	
	
}
