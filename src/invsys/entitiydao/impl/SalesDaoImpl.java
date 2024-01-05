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
import invsys.entities.POSPayDetails;
import invsys.entities.POSPayDetails_;
import invsys.entities.Products;
import invsys.entities.Products_;
import invsys.entities.Sales;
import invsys.entities.SalesDetails;
import invsys.entities.SalesDetails_;
import invsys.entities.Sales_;
import invsys.entities.Users;
import invsys.entities.compositkeys.POSPayDetailId;
import invsys.entities.compositkeys.POSPayDetailId_;
import invsys.entities.compositkeys.SalesDetailId;
import invsys.entities.compositkeys.SalesDetailId_;
import invsys.entities.compositkeys.SalesId;
import invsys.entities.compositkeys.SalesId_;
import invsys.entitiydao.SalesDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;

public class SalesDaoImpl implements SalesDao {

	private static Session session;

	private static SalesDaoImpl daoHandler;

	// formalize method to access static method dao class
	public static SalesDaoImpl getDao() {
		if (daoHandler == null) {
			daoHandler = new SalesDaoImpl();
		}
		return daoHandler;

	}

	// Save Sales updated method on 08-08-2020
	public boolean saveSale(Sales sale, ObservableList<SalesDetails> salesDetails,
			ObservableList<POSPayDetails> payDetailList) {
		ProductDaoImpl.getDao();
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.save(sale);

			for (int i = 0; i < salesDetails.toArray().length; i++) {
				session.save(salesDetails.get(i));
				Products prd = salesDetails.get(i).getProduct();
				if (!salesDetails.get(i).isItemCancelStatus()) {

					Products prdUpdate = session.get(Products.class, prd.getPrd_id());
					prdUpdate.setOnHandQty(prdUpdate.getOnHandQty() - salesDetails.get(i).getSalesQty());
					session.update(prdUpdate);

				}
				if (i % 50 == 0) {
					session.flush();
					session.clear();
				}

			}
			for (POSPayDetails saveDets : payDetailList) {
				session.save(saveDets);
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

	//Full bill cancellation
	public boolean cancelFullBill(Sales sale, ObservableList<SalesDetails> salesDetails) {
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.save(sale);

			for (int i = 0; i < salesDetails.toArray().length; i++) {
				session.save(salesDetails.get(i));
			
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
	public int getLastBillNo(Users user, java.sql.Date date) {
		Integer lastBillNo = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Integer> criteriaQuery = builder.createQuery(Integer.class);
			Root<Sales> root = criteriaQuery.from(Sales.class);
			criteriaQuery.select(builder.max(root.get("salesId").get("bill_no")));
			criteriaQuery.where(builder.equal(root.get("salesId").get("user"), user),
					builder.equal(root.get("salesId").get("date"), date));
			Query<Integer> query = session.createQuery(criteriaQuery);
			lastBillNo = query.getSingleResult();
			session.getTransaction().commit();
			if (lastBillNo == null) {
				lastBillNo = 0;
			}

		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "SQL Error " + e.getLocalizedMessage(), null);
			e.printStackTrace();
			session.getTransaction().rollback();
			lastBillNo = null;
		}
		return lastBillNo;
	}

	// get sales value by month
	public List<Object[]> getSalesValueByMonth() {
		List<Object[]> salesAndMonth = null;
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Object[]> critQuery = builder.createQuery(Object[].class);
			Root<Sales> root = critQuery.from(Sales.class);
			critQuery.multiselect(root.get("salesId").get("date"), builder.sum(root.get("billAmount")));
			critQuery.groupBy(builder.function("MONTH", Integer.class, root.get("salesId").get("date")));
			Query<Object[]> query = session.createQuery(critQuery);
			salesAndMonth = query.getResultList();
			session.getTransaction().commit();

		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "SQL Error " + e.getLocalizedMessage(), null);
			e.printStackTrace();
			session.getTransaction().rollback();

		}
		return salesAndMonth;
	}

	// get valid sales from sales by bill no, date, cashier id
	public List<SalesDetails> getSaleDetailsByBillNoDateUser(long billNo, java.sql.Date date, Users user) {
		List<SalesDetails> listToReturn = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<SalesDetails> criteria = builder.createQuery(SalesDetails.class);
			Root<SalesDetails> root = criteria.from(SalesDetails.class);
			Join<Object, Object> product = (Join<Object, Object>) root.fetch(SalesDetails_.PRODUCT, JoinType.INNER);
			product.fetch(Products_.CATEGORY, JoinType.INNER);
			product.fetch(Products_.SUPPLIER, JoinType.INNER);
			Join<SalesDetails, SalesDetailId> salesdetId = root.join(SalesDetails_.SALES_DET_ID);
			Join<SalesDetailId, Sales> sales = salesdetId.join(SalesDetailId_.sales, JoinType.INNER);
			Join<Sales, SalesId> salesId = sales.join(Sales_.salesId);
			Join<SalesId, Users> userJoin = salesId.join(SalesId_.user, JoinType.INNER);
			salesdetId.fetch(SalesDetailId_.SALES, JoinType.INNER);
			Predicate[] predicate = new Predicate[5];
			predicate[0] = builder.equal(userJoin, user);
			predicate[1] = builder.equal(salesId.get("bill_no"), billNo);
			predicate[2] = builder.equal(salesId.get("date"), date);
			predicate[3] = builder.equal(sales.get("cancelStatus"), false);
			predicate[4] = builder.equal(root.get("itemCancelStatus"), false);
			criteria.where(predicate);
			criteria.orderBy(builder.asc(salesdetId.get("seqNo")));
			listToReturn = session.createQuery(criteria).getResultList();
			session.getTransaction().commit();
			return listToReturn;
		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Hibernate Error In Loading Details", e.getLocalizedMessage());
			session.getTransaction().rollback();
			e.printStackTrace();
			return null;
		}
	}

	// get daily sales count based on dashboard manager
	public long getDailyPosSalesCount(java.sql.Date date, Users user , boolean isManager) {
		long valueToReturn = 0;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
			Root<Sales> root = criteria.from(Sales.class);
			
			Join<Sales, SalesId> salesId = root.join(Sales_.salesId);
			Join<SalesId, Users> userJoin = salesId.join(SalesId_.user, JoinType.INNER);
		
			Predicate[] predicate = null;
			if(isManager) {
				predicate = new Predicate[2];
				predicate[0] = builder.equal(salesId.get("date"), date);
				predicate[1] = builder.equal(root.get("cancelStatus"), false);
			}
			else {
				predicate = new Predicate[3];
				predicate[0] = builder.equal(userJoin, user);
				predicate[1] = builder.equal(salesId.get("date"), date);
				predicate[2] = builder.equal(root.get("cancelStatus"), false);
			}
				
			criteria.where(predicate).select(builder.count(root));
			valueToReturn = session.createQuery(criteria).getSingleResult();
			session.getTransaction().commit();
			
		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Hibernate Error In Loading Details", e.getLocalizedMessage());
			session.getTransaction().rollback();
			e.printStackTrace();
			return 0;

		}
		
		return valueToReturn;
	}

	// get total Day Sale value for dash board manager roles(Overall Figures) to
	// dash board - uses native query due to complexity
	public double getDaySalesValueNativeQueryForManagers(java.sql.Date date) {
		double daysaleValue = 0;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();

			String sql = "select sum(withoutTaxSales) as withouttaxsale from ((select sal.user_id as user, sal.sales_date as tdate,cat.cat_name as catname,prd.prd_id as prdid, prd.p_name as pname,round(sum(sal.sales_qty),1) as salesqty ,round(sum(sal.item_net_amount),2) as TotalSales,"
					+ "round((sum(sal.discounted_price/ ((sal.item_vat+100)/100)*sal.item_vat/100 * sal.sales_qty)),2) as vat, round((sum(sal.item_net_amount/((100+sal.item_vat)/100))),2) as withoutTaxSales from sales_detail sal inner join products prd on prd.prd_id=sal.product_id inner join category cat on cat.cat_id=prd.cat_id where sal.item_cancel_status=false group by tdate,cat.cat_name,prd.prd_id having sum(sal.sales_qty) > 0 order by sal.sales_date,cat.cat_name,(sal.sales_qty) desc)"
					+ "union all"
					+ "(select sal.user_id as user, sal.invoice_date as tdate,cat.cat_name as catname , prd.prd_id as prdid, prd.p_name as pname, round(sum(sal.sales_qty),1) as salesqty ,round(sum(sal.item_net_amount),2) as TotalSales , "
					+ "round((sum(sal.discounted_price/ ((sal.item_vat+100)/100)*sal.item_vat/100 * sal.sales_qty)),2) as vat,"
					+ "round((sum(sal.item_net_amount/((100+sal.item_vat)/100))),2) as withoutTaxSales from credit_invoice_detail sal inner join products prd on prd.prd_id=sal.product_id inner join category cat on cat.cat_id=prd.cat_id group by tdate,cat.cat_name ,prd.prd_id having sum(sal.sales_qty) > 0 order by sal.invoice_date,cat.cat_name,(sal.sales_qty) desc)"
					+ "union all"
					+ "(select v.user_id as user,v.void_date as tdate,cat.cat_name as catname , prd.prd_id as prdid, prd.p_name as pname, round(sum(sal.sales_qty*(-1)),1) as salesqty ,round(sum(sal.item_net_amount*(-1)),2) as TotalSales , "
					+ "round((sum(sal.discounted_unit_price/ ((sal.item_vat+100)/100)*sal.item_vat/100 * sal.sales_qty)*(-1)),2) as vat,"
					+ "round((sum(sal.item_net_amount/((100+sal.item_vat)/100))*(-1)),2) as withoutTaxSales from credit_invoice_void v inner join credit_invoice_void_detail sal on v.void_id=sal.void_id inner join products prd on prd.prd_id=sal.product_id inner join category cat on cat.cat_id=prd.cat_id group by tdate,cat.cat_name ,prd.prd_id having sum(sal.sales_qty)>0 order by v.void_date,cat.cat_name,(sal.sales_qty) desc)"
					+ "union all"
					+ "(select ref.user_id as user, ref.refund_date as tdate,cat.cat_name as catname , prd.prd_id as prdid, prd.p_name as pname, round(sum(sal.sales_qty*(-1)),1) as salesqty ,round(sum(sal.item_net_amount*(-1)),2) as TotalSales , "
					+ "round((sum(sal.discounted_unit_price/ ((sal.item_vat+100)/100)*sal.item_vat/100 * sal.sales_qty)*(-1)),2) as vat,"
					+ "round((sum(sal.item_net_amount/((100+sal.item_vat)/100))*(-1)),2) as withoutTaxSales from customer_refunds ref inner join customer_refund_detail sal on ref.refund_id=sal.refund_id inner join products prd on prd.prd_id=sal.product_id inner join category cat on cat.cat_id=prd.cat_id group by tdate,cat.cat_name,prd.prd_id having sum(sal.sales_qty) > 0  order by ref.refund_date,cat.cat_name,(sal.sales_qty) desc)"
					+ " ) as t where tdate between :date and :date having sum(salesqty) <> 0 order by catname,salesqty ;";

			Query query = session.createSQLQuery(sql);
			query.setParameter("date", date);

			daysaleValue = (double) query.getSingleResult();

			session.getTransaction().commit();

		} catch (Exception e) {

			e.printStackTrace();
			session.getTransaction().rollback();
			//AlertHandler.getAlert(AlertType.WARNING, "No Sales for Today", "Day sales Details Cannot be Loaded");

		}

		return daysaleValue;
	}

	
	// get monthly sales values for xy chart manager roles(Overall Figures) to
		// dash board - uses native query due to complexity
		public List<Object[]> getMonthlyTotalSalesNativeQueryForManagers(java.sql.Date fromDate , java.sql.Date toDate) {
			List<Object[]> salesAndMonth = null;
			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();

				String sql = "select sum(withoutTaxSales) , tdate from" + 
						"((select sum(s.net_bill_amount) as withoutTaxSales, s.sales_date as tdate from sales s where s.cancel_status=false group by tdate)"
						+"union all"
						+"(select sum(c.invoice_net_amount) as withoutTaxSales , c.invoice_date as tdate from credit_invoice c group by tdate)"
						+"union all"
						+"(select sum(cref.net_refund_amount*-1) as withoutTaxSales , cref.refund_date as tdate from customer_refunds cref group by tdate)"
						+"union all"
						+"(select sum(crvoid.net_void_amount*-1) as withoutTaxSales, crvoid.void_date as tdate from credit_invoice_void crvoid group by tdate))"
						+"as t where tdate between :fromdate and :todate group by month(tdate)";

								
				Query query = session.createSQLQuery(sql);
				query.setParameter("fromdate", fromDate);
				query.setParameter("todate", toDate);

				salesAndMonth = query.getResultList();

				session.getTransaction().commit();

			} catch (Exception e) {

				e.printStackTrace();
				session.getTransaction().rollback();
				AlertHandler.getAlert(AlertType.WARNING, "No Sales for Today", "Monthly sales Details Cannot be Loaded");

			}

			return salesAndMonth;
		}
		
		// get monthly sales values for xy chart manager roles(Overall Figures) to
				// dash board - uses native query due to complexity
				public List<Object[]> getMonthlyTotalSalesNativeQueryForUsers(java.sql.Date fromDate , java.sql.Date toDate, int userId) {
					List<Object[]> salesAndMonth = null;
					
					
					try {
						session = HibernateUtil.getSessionFactory().getCurrentSession();
						session.beginTransaction();

						String sql = "select sum(withoutTaxSales) , tdate from" + 
								"((select sum(s.net_bill_amount) as withoutTaxSales, s.sales_date as tdate , s.user_id as userId from sales s where s.cancel_status=false group by tdate, userId)"
								+"union all"
								+"(select sum(c.invoice_net_amount) as withoutTaxSales , c.invoice_date as tdate ,c.user_id as userId from credit_invoice c group by tdate,userId)"
								+"union all"
								+"(select sum(cref.net_refund_amount*-1) as withoutTaxSales , cref.refund_date as tdate, cref.user_id as userId from customer_refunds cref group by tdate, userId)"
								+"union all"
								+"(select sum(crvoid.net_void_amount*-1) as withoutTaxSales, crvoid.void_date as tdate , crvoid.user_id as userId from credit_invoice_void crvoid group by tdate, userId))"
								+"as t where tdate between :fromdate and :todate and userId=:user group by month(tdate)";

										
						Query query = session.createSQLQuery(sql);
						query.setParameter("fromdate", fromDate);
						query.setParameter("todate", toDate);
						query.setParameter("user", userId);

						salesAndMonth = query.getResultList();

						session.getTransaction().commit();

					} catch (Exception e) {

						e.printStackTrace();
						session.getTransaction().rollback();
						AlertHandler.getAlert(AlertType.WARNING, "No Sales for Today", "Monthly sales Details Cannot be Loaded");

					}

					return salesAndMonth;
				}
		
	// get total Day Sale value for users wise figures to dash board - uses native
	// query due to complexity
	public double getDaySalesValueNativeQueryForNonManagers(java.sql.Date date, Users user) {
		double daysaleValue = 0;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();

			String sql = "select sum(withoutTaxSales) as withouttaxsale from ((select sal.user_id as user, sal.sales_date as tdate,cat.cat_name as catname,prd.prd_id as prdid, prd.p_name as pname,round(sum(sal.sales_qty),1) as salesqty ,round(sum(sal.item_net_amount),2) as TotalSales,"
					+ "round((sum(sal.discounted_price/ ((sal.item_vat+100)/100)*sal.item_vat/100 * sal.sales_qty)),2) as vat, round((sum(sal.item_net_amount/((100+sal.item_vat)/100))),2) as withoutTaxSales from sales_detail sal inner join products prd on prd.prd_id=sal.product_id inner join category cat on cat.cat_id=prd.cat_id where sal.item_cancel_status=false"
					+ " group by tdate,cat.cat_name,prd.prd_id,user having sum(sal.sales_qty) > 0 order by sal.sales_date,cat.cat_name,(sal.sales_qty) desc)"
					+ "union all"
					+ "(select sal.user_id as user, sal.invoice_date as tdate,cat.cat_name as catname , prd.prd_id as prdid, prd.p_name as pname, round(sum(sal.sales_qty),1) as salesqty ,round(sum(sal.item_net_amount),2) as TotalSales , "
					+ "round((sum(sal.discounted_price/ ((sal.item_vat+100)/100)*sal.item_vat/100 * sal.sales_qty)),2) as vat,"
					+ "round((sum(sal.item_net_amount/((100+sal.item_vat)/100))),2) as withoutTaxSales from credit_invoice_detail sal inner join products prd on prd.prd_id=sal.product_id inner join category cat on cat.cat_id=prd.cat_id group by tdate,cat.cat_name ,prd.prd_id,user having sum(sal.sales_qty) <> 0 order by sal.invoice_date,cat.cat_name,(sal.sales_qty) desc)"
					+ "union all"
					+ "(select v.user_id as user,v.void_date as tdate,cat.cat_name as catname , prd.prd_id as prdid, prd.p_name as pname, round(sum(sal.sales_qty*(-1)),1) as salesqty ,round(sum(sal.item_net_amount*(-1)),2) as TotalSales , "
					+ "round((sum(sal.discounted_unit_price/ ((sal.item_vat+100)/100)*sal.item_vat/100 * sal.sales_qty)*(-1)),2) as vat,"
					+ "round((sum(sal.item_net_amount/((100+sal.item_vat)/100))*(-1)),2) as withoutTaxSales from credit_invoice_void v inner join credit_invoice_void_detail sal on v.void_id=sal.void_id inner join products prd on prd.prd_id=sal.product_id inner join category cat on cat.cat_id=prd.cat_id group by tdate,cat.cat_name ,prd.prd_id,user having sum(sal.sales_qty) <>0 order by v.void_date,cat.cat_name,(sal.sales_qty) desc)"
					+ "union all"
					+ "(select ref.user_id as user, ref.refund_date as tdate,cat.cat_name as catname , prd.prd_id as prdid, prd.p_name as pname, round(sum(sal.sales_qty*(-1)),1) as salesqty ,round(sum(sal.item_net_amount*(-1)),2) as TotalSales , "
					+ "round((sum(sal.discounted_unit_price/ ((sal.item_vat+100)/100)*sal.item_vat/100 * sal.sales_qty)*(-1)),2) as vat,"
					+ "round((sum(sal.item_net_amount/((100+sal.item_vat)/100))*(-1)),2) as withoutTaxSales from customer_refunds ref inner join customer_refund_detail sal on ref.refund_id=sal.refund_id inner join products prd on prd.prd_id=sal.product_id inner join category cat on cat.cat_id=prd.cat_id group by tdate,cat.cat_name,prd.prd_id,user having sum(sal.sales_qty) > 0  order by ref.refund_date,cat.cat_name,(sal.sales_qty) desc)"
					+ " ) as t where user=:userid and tdate between :date and :date having sum(salesqty) <> 0 order by catname,salesqty ;";

			Query query = session.createSQLQuery(sql);
			query.setParameter("date", date);
			query.setParameter("userid", user.getUser_id());

			daysaleValue = (double) query.getSingleResult();

			session.getTransaction().commit();

		} catch (Exception e) {

			e.printStackTrace();
			session.getTransaction().rollback();
			AlertHandler.getAlert(AlertType.WARNING, "No Sales for Today", "Day sales Details Cannot be Loaded");

		}

		return daysaleValue;
	}

	// get Top 10 item of the month for the dashboard using native queryies due to
	// complexity
	public List<Object[]> getTopTenItemsOfTheMonth(java.sql.Date fromDate, java.sql.Date toDate) {
		List<Object[]> listOfTopTenItems = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();

			String sql = "select pname, sum(withoutTaxSales) as withouttaxsale , sum(salesqty) as salesqty from"
					+ "((select sal.user_id as user, sal.sales_date as tdate,cat.cat_name as catname , prd.prd_id as prdid, prd.p_name as pname, round(sum(sal.sales_qty),1) as salesqty ,round(sum(sal.item_net_amount),2) as TotalSales,"
					+ "round((sum(sal.discounted_price/ ((sal.item_vat+100)/100)*sal.item_vat/100 * sal.sales_qty)),2) as vat,"
					+ " round((sum(sal.item_net_amount/((100+sal.item_vat)/100))),2) as withoutTaxSales from sales_detail sal inner join products prd on prd.prd_id=sal.product_id inner join category cat on cat.cat_id= prd.cat_id where sal.item_cancel_status=false group by tdate,cat.cat_name ,prd.prd_id having sum(sal.sales_qty) > 0 order by sal.sales_date,cat.cat_name,(sal.sales_qty) desc)"
					+ "union all"
					+ "(select sal.user_id as user, sal.invoice_date as tdate,cat.cat_name as catname , prd.prd_id as prdid, prd.p_name as pname, round(sum(sal.sales_qty),1) as salesqty ,round(sum(sal.item_net_amount),2) as TotalSales ,"
					+ "round((sum(sal.discounted_price/ ((sal.item_vat+100)/100)*sal.item_vat/100 * sal.sales_qty)),2) as vat,"
					+ "round((sum(sal.item_net_amount/((100+sal.item_vat)/100))),2) as withoutTaxSales from credit_invoice_detail sal inner join products prd on prd.prd_id=sal.product_id inner join category cat on cat.cat_id= prd.cat_id group by tdate,cat.cat_name ,prd.prd_id having sum(sal.sales_qty) > 0 order by sal.invoice_date,cat.cat_name,(sal.sales_qty) desc)"
					+ "union all"
					+ "(select v.user_id as user,v.void_date as tdate,cat.cat_name as catname , prd.prd_id as prdid, prd.p_name as pname, round(sum(sal.sales_qty*(-1)),1) as salesqty ,round(sum(sal.item_net_amount*(-1)),2) as TotalSales,"
					+ "round((sum(sal.discounted_unit_price/ ((sal.item_vat+100)/100)*sal.item_vat/100 * sal.sales_qty)*(-1)),2) as vat,"
					+ "round((sum(sal.item_net_amount/((100+sal.item_vat)/100))*(-1)),2) as withoutTaxSales from credit_invoice_void v inner join credit_invoice_void_detail sal on v.void_id=sal.void_id inner join products prd on prd.prd_id=sal.product_id inner join category cat on cat.cat_id= prd.cat_id group by tdate,cat.cat_name ,prd.prd_id having sum(sal.sales_qty) > 0 order by v.void_date,cat.cat_name,(sal.sales_qty) desc)"
					+ "union all"
					+ "(select ref.user_id as user, ref.refund_date as tdate,cat.cat_name as catname , prd.prd_id as prdid, prd.p_name as pname, round(sum(sal.sales_qty*(-1)),1) as salesqty ,round(sum(sal.item_net_amount*(-1)),2) as TotalSales ,"
					+ "round((sum(sal.discounted_unit_price/ ((sal.item_vat+100)/100)*sal.item_vat/100 * sal.sales_qty)*(-1)),2) as vat,"
					+ "round((sum(sal.item_net_amount/((100+sal.item_vat)/100))*(-1)),2) as withoutTaxSales from customer_refunds ref inner join customer_refund_detail sal on ref.refund_id=sal.refund_id inner join products prd on prd.prd_id=sal.product_id inner join category cat on cat.cat_id= prd.cat_id group by tdate,cat.cat_name ,prd.prd_id having sum(sal.sales_qty) > 0 order by ref.refund_date,cat.cat_name,(sal.sales_qty) desc)"
					+ " ) as t where tdate between :fromdate and :todate group by prdid having sum(salesqty) <> 0 order by salesqty desc limit 10";

			Query query = session.createSQLQuery(sql);
			query.setParameter("fromdate", fromDate);
			query.setParameter("todate", toDate);

			listOfTopTenItems = query.getResultList();

			session.getTransaction().commit();

		} catch (Exception e) {

			e.printStackTrace();
			session.getTransaction().rollback();
			AlertHandler.getAlert(AlertType.ERROR, "Cannot Retrieve Data", "Day sales Details Cannot be Loaded");

		}

		return listOfTopTenItems;
	}

	
	public ObservableList<POSPayDetails> getPosPaymentforReprint(long billNo, Users user,java.sql.Date date){
		List<POSPayDetails> listToReturn = null;
		ObservableList<POSPayDetails> possPaydetailsList= FXCollections.observableArrayList();
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<POSPayDetails> criteria = builder.createQuery(POSPayDetails.class);
			Root<POSPayDetails> root = criteria.from(POSPayDetails.class);
			Join<Object, Object> paymentDets = (Join<Object, Object>) root.fetch(POSPayDetails_.POS_PAY_DET_ID ,JoinType.INNER);
			Join<POSPayDetails, POSPayDetailId> salesdetId = root.join(POSPayDetails_.POS_PAY_DET_ID);
			Join<POSPayDetailId, Sales> sales = salesdetId.join(POSPayDetailId_.sales, JoinType.INNER);
			Join<Sales, SalesId> salesId = sales.join(Sales_.salesId);
			Join<SalesId, Users> userJoin = salesId.join(SalesId_.user, JoinType.INNER);
			salesdetId.fetch(POSPayDetailId_.SALES, JoinType.INNER);
			Predicate[] predicate = new Predicate[3];
			predicate[0] = builder.equal(userJoin, user);
			predicate[1] = builder.equal(salesId.get("bill_no"), billNo);
			predicate[2] = builder.equal(salesId.get("date"), date);
			criteria.where(predicate);
			listToReturn = session.createQuery(criteria).getResultList();
			session.getTransaction().commit();
			possPaydetailsList.setAll(listToReturn);
			return possPaydetailsList;
			
		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Hibernate Error In Loading Details", e.getLocalizedMessage());
			session.getTransaction().rollback();
			e.printStackTrace();
			return null;
		}
	}
}
