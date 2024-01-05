package invsys.entitiydao.impl;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import invsys.controllers.database.HibernateUtil;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.entities.CashRegister;
import invsys.entities.CashRegister_;
import invsys.entities.Users;
import invsys.entities.compositkeys.CashRegisterId;
import invsys.entities.compositkeys.CashRegisterId_;
import invsys.entitiydao.CashRegisterDao;
import javafx.scene.control.Alert.AlertType;

public class CashRegisterDaoImpl implements CashRegisterDao {

	
private static Session session;

	
	
	private static CashRegisterDaoImpl daoHandler;
	
	
	//formalize method to access static method dao class
	public static CashRegisterDaoImpl getDao() {
		if(daoHandler == null) {
			daoHandler = new CashRegisterDaoImpl();
		}
		return daoHandler;
	}
	

	
	
	//check & Get cash register method to see wheather cahs register registered 
	//for particular date 
	public CashRegister getCashRegister(CashRegisterId id) {
		CashRegister cashRegi = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			cashRegi = session.get(CashRegister.class, id);
			session.getTransaction().commit();
			
		}catch(Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Cannot Load Cash Register Details", null);
			session.getTransaction().rollback();
		}
		
		return cashRegi;
	}
	
	// insert into cashRegister data to database
		public boolean saveRegister(CashRegister cashReg) {
			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				session.save(cashReg);
				session.getTransaction().commit();
				return true;
			} catch (Exception e) {
				AlertHandler.getAlert(AlertType.ERROR, "Cannot Insert Data", e.getMessage());
				return false;
			}

		}
		//update Cash Registry
		public CashRegister updateCashRegister(CashRegister updateRegister) {
			CashRegister cashRegi = null;
			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				session.update(updateRegister);
				cashRegi = updateRegister;
				session.getTransaction().commit();
				
			}catch(Exception e) {
				AlertHandler.getAlert(AlertType.ERROR, "Cannot Update Cash Register Details", null);
				session.getTransaction().rollback();
			}
			
			return cashRegi;
		}
		
			
		//get payment details for closing cash register
		public List<Object[]> getPaymentDetailsForCashRegistryNative(long userId, java.sql.Date date) {
			List<Object[]> paySelectedDetails = null;
			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
							
				String sql = "select p.sub_pay_mode, sum(p.amount-s.balance) from pos_pay_detail p inner join "
						+ "sales s on s.sales_date=p.sales_date and s.user_id=p.user_id and s.bill_no=p.bill_no"
						+ " where s.cancel_status=false and p.sales_date =:date and"
						+ " p.user_id=:userId group by p.sub_pay_mode";
								
				Query query =session.createNativeQuery(sql);
				query.setParameter("userId", userId);
				query.setParameter("date", date);
				
				paySelectedDetails = query.getResultList();
				
				session.getTransaction().commit();
				
				
			}catch(Exception e) {
				
				e.printStackTrace();
				session.getTransaction().rollback();
				AlertHandler.getAlert(AlertType.ERROR, "Cannot Retrieve Data", "Cash Registry Details Cannot be Loaded");
				
			}
			
			
			return paySelectedDetails;
		}
		
		//check before user login in to the system whether all cash register are clear
		public List<CashRegister> getNotSignOffCashRegisters(Users user){
			List<CashRegister> cashRegistersList = null;
			
			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<CashRegister> criteria = builder.createQuery(CashRegister.class);
				Root<CashRegister> root = criteria.from(CashRegister.class);
				
				Join<CashRegister,CashRegisterId> cashRegistId = root.join(CashRegister_.id);
				Join<CashRegisterId, Users> userJoin = cashRegistId.join(CashRegisterId_.user);
				
				Predicate predict[] = new Predicate[3];
				
				predict[0] = builder.equal(userJoin, user);
				predict[1] = builder.equal(root.get("signInStatus"), true);
				predict[2] = builder.lessThan(root.get("loginDate"), java.sql.Date.valueOf(LocalDate.now()));
				
				criteria.where(predict);
				cashRegistersList = session.createQuery(criteria).getResultList();
				
				session.getTransaction().commit();
				
				for(CashRegister reg : cashRegistersList) {
					System.out.println(reg.getUserName());
				}
				
			}catch(Exception e) {
				
				
				session.getTransaction().rollback();
				AlertHandler.getAlert(AlertType.ERROR,"Cash Registry Load Error","Sign in Cash Registers cannot be loaded");
				e.printStackTrace();
				//System.exit(1);
			}
			
			return cashRegistersList;
		}
		
	
	
}
