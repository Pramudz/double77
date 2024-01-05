package invsys.entitiydao.impl;

import org.hibernate.Session;

import invsys.controllers.database.HibernateUtil;
import invsys.controllers.formvalidation.AlertHandler;
import javafx.scene.control.Alert.AlertType;

public class ServerDateDao {

	
private static Session session;

	
	
	private static ServerDateDao daoHandler;
	
	
	//formalize method to access static method dao class
	public static ServerDateDao getDao() {
		if(daoHandler == null) {
			daoHandler = new ServerDateDao();
		}
		
			return daoHandler;
	}
	
	
	public static java.sql.Date getServerDate(){
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			java.sql.Date date = (java.sql.Date) session.createQuery("select date(now()) from Company").setMaxResults(1).getSingleResult();
			session.getTransaction().commit();
			System.out.println(date);
			return date;
		} catch(Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Server Error", "Cannot Obtain server date contact admin");
			session.getTransaction().rollback();
			e.printStackTrace();
			return null;
		
		}
		
		
	}
}
