package invsys.entitiydao.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import invsys.controllers.database.HibernateUtil;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.entities.CreditInvoice;
import invsys.entities.CreditInvoiceDetail;
import invsys.entities.CreditInvoiceDetail_;
import invsys.entities.CreditInvoice_;
import invsys.entities.Products;
import invsys.entities.Products_;
import invsys.entities.Users;
import invsys.entities.compositkeys.CreditInvoiceDetailId;
import invsys.entities.compositkeys.CreditInvoiceDetailId_;
import invsys.entities.compositkeys.CreditInvoiceId;
import invsys.entities.compositkeys.CreditInvoiceId_;
import invsys.entitiydao.CreditInvoiceDao;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;

public class CreditInvoiceDaoImpl implements CreditInvoiceDao {
	
	private static Session session ;

	
	private static CreditInvoiceDaoImpl creditInvoceModel;
	
	
	public static CreditInvoiceDaoImpl getDao() {
		if(creditInvoceModel == null) {
			creditInvoceModel = new CreditInvoiceDaoImpl();
		}
		return creditInvoceModel;
	}
	
	
	// Save Sales updated method on 08-08-2020
			public boolean saveCreditInvoice(CreditInvoice sale, ObservableList<CreditInvoiceDetail> salesDetails) {
				ProductDaoImpl.getDao();
				try {
					session = HibernateUtil.getSessionFactory().getCurrentSession();
					session.beginTransaction();
					session.save(sale);
								
					for (int i = 0; i < salesDetails.toArray().length; i++) {
						session.save(salesDetails.get(i));
						Products prd = salesDetails.get(i).getProduct();
												
						Products prdUpdate = session.get(Products.class, prd.getPrd_id());
						prdUpdate.setOnHandQty(prdUpdate.getOnHandQty()-salesDetails.get(i).getSalesQty());
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
			
			// get Last Bill no for Billing
			public long  getLastBillNo(Users user,java.sql.Date date) {
				Long lastBillNo = null;
				try {
					session = HibernateUtil.getSessionFactory().getCurrentSession();
					session.beginTransaction();
					CriteriaBuilder builder = session.getCriteriaBuilder();
					CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
					Root<CreditInvoice> root = criteriaQuery.from(CreditInvoice.class);
					criteriaQuery.select(builder.max(root.get("creditInvoiceId").get("invoiceId")));
					criteriaQuery.where(builder.equal(root.get("creditInvoiceId").get("user"), user),
							builder.equal(root.get("creditInvoiceId").get("date"), date)
							);
					Query<Long> query = session.createQuery(criteriaQuery);
					lastBillNo = query.getSingleResult();
					session.getTransaction().commit();
					if(lastBillNo == null) {
						lastBillNo = 0L;
					}
					
					
				}catch(Exception e) {
					AlertHandler.getAlert(AlertType.ERROR, "SQL Error "+e.getLocalizedMessage(), null);
					e.printStackTrace();
					session.getTransaction().rollback();
					lastBillNo = null;
				}
				return lastBillNo;
			}
			
			//get valid Credit Invoice from credit Invoice by bill no, date, cashier id
			public List<CreditInvoiceDetail> getInvoiceDetailsByBillNoDateUser(long billNo,java.sql.Date date, Users user){
				List<CreditInvoiceDetail> listToReturn = null;
				try {
					session = HibernateUtil.getSessionFactory().getCurrentSession();
					session.beginTransaction();
					CriteriaBuilder builder = session.getCriteriaBuilder();
					CriteriaQuery<CreditInvoiceDetail> criteria = builder.createQuery(CreditInvoiceDetail.class);
					Root<CreditInvoiceDetail> root = criteria.from(CreditInvoiceDetail.class);
					Join<Object,Object> product = (Join<Object,Object>) root.fetch(CreditInvoiceDetail_.PRODUCT, JoinType.INNER);
					Join<CreditInvoiceDetail,CreditInvoiceDetailId> creditInvDetId = root.join(CreditInvoiceDetail_.CREDIT_INVOICE_ID);
					Join<CreditInvoiceDetailId,CreditInvoice> creditInvoice = creditInvDetId.join(CreditInvoiceDetailId_.creditInvoice,JoinType.INNER);
					Join<CreditInvoice,CreditInvoiceId> creditInvoiceId = creditInvoice.join(CreditInvoice_.CREDIT_INVOICE_ID);
					Join<CreditInvoiceId,Users> userJoin = creditInvoiceId.join(CreditInvoiceId_.user, JoinType.INNER);
					product.fetch(Products_.CATEGORY,JoinType.INNER);
					product.fetch(Products_.SUPPLIER,JoinType.INNER);
					creditInvDetId.fetch(CreditInvoiceDetailId_.creditInvoice, JoinType.INNER);
					Predicate[] predicate = new Predicate[3];
					predicate[0] = builder.equal(userJoin, user);
					predicate[1] = builder.equal(creditInvoiceId.get("invoiceId"), billNo);
					predicate[2] = builder.equal(creditInvoiceId.get("date"), date);
					criteria.where(predicate);
					listToReturn = session.createQuery(criteria).getResultList();
					session.getTransaction().commit();
					return listToReturn;
				}catch(Exception e) {
					AlertHandler.getAlert(AlertType.ERROR, "Hibernate Error In Loading Details", e.getLocalizedMessage());
					session.getTransaction().rollback();
					e.printStackTrace();
					return null;
				}
			}
			
			public boolean settleCreditInvoice(CreditInvoice settleInvoice) {
				try {
					session = HibernateUtil.getSessionFactory().getCurrentSession();
					session.beginTransaction();
					CreditInvoice crdToUpdate = session.get(CreditInvoice.class, settleInvoice.getCreditInvoiceId());
					crdToUpdate.setSettledStatus(settleInvoice.isSettledStatus());
					crdToUpdate.setSettledBy(settleInvoice.getSettledBy());
					crdToUpdate.setSettledAmount(settleInvoice.getSettledAmount());
					System.out.println(settleInvoice.getSettledDate());
					crdToUpdate.setSettledDate(settleInvoice.getSettledDate());
					session.update(crdToUpdate);
					session.getTransaction().commit();
					return true;
				}catch(Exception e) {
					AlertHandler.getAlert(AlertType.ERROR, "Hibernate Error", e.getLocalizedMessage());
					session.getTransaction().rollback();
					e.printStackTrace();
					return false;
				}
				
			}
		
	
}
