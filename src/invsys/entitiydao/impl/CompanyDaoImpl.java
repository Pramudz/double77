package invsys.entitiydao.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;

import invsys.controllers.database.HibernateUtil;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.entities.Company;
import invsys.entitiydao.CompanyDao;
import javafx.scene.control.Alert.AlertType;

public class CompanyDaoImpl implements CompanyDao {

	private static Session session;

	private static CompanyDaoImpl daoHandler;

	public static CompanyDaoImpl getDao() {
		if (daoHandler == null) {
			daoHandler = new CompanyDaoImpl();
		}

		return daoHandler;
	}

	// save Company Info
	public boolean saveCompanyInfo(Company company) {
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.save(company);
			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			session.getTransaction().rollback();
			AlertHandler.getAlert(AlertType.ERROR, "Error Inserting Data", e.getLocalizedMessage());
			e.printStackTrace();
			return false;
		}

	}

	// update Company Info
	public boolean updateCompanyInfo(Company company) {
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Company updateCompany = session.get(Company.class, company.getCompId());
			updateCompany.setAddressLine1(company.getAddressLine1());
			updateCompany.setAddressLine2(company.getAddressLine2());
			updateCompany.setAddressLine3(company.getAddressLine3());
			updateCompany.setCompanyEmail(company.getCompanyEmail());
			updateCompany.setCompanyLogo(company.getCompanyLogo());
			updateCompany.setCompanyName(company.getCompanyName());
			updateCompany.setCompanyRegNum(company.getCompanyRegNum());
			updateCompany.setFaxNumber(company.getFaxNumber());
			updateCompany.setTelephoneNum(company.getTelephoneNum());
			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			session.getTransaction().rollback();
			AlertHandler.getAlert(AlertType.ERROR, "Error Updating Data", e.getLocalizedMessage());
			return false;
		}

	}

	// get Company Info
	public Company getCompany(Company company) {
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Company companyTobeReturned = session.get(Company.class, company.getCompId());
			session.getTransaction().commit();
			return companyTobeReturned;
		} catch (Exception e) {
			session.getTransaction().rollback();
			AlertHandler.getAlert(AlertType.ERROR, "Error Retriving Data", e.getLocalizedMessage());
			e.printStackTrace();
			return null;
		}

	}
	
	// get Max Result
		public Company getCompanyLast() {
			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				Query query = session.createQuery("from Company");
				query.getMaxResults();
				Company companyTobeReturned = (Company) query.getSingleResult();
				session.getTransaction().commit();
				return companyTobeReturned;
			} catch (Exception e) {
				session.getTransaction().rollback();
				//AlertHandler.getAlert(AlertType.ERROR, "Error Retriving Company Data", e.getLocalizedMessage());
				e.printStackTrace();
				return null;
			}

		}

}
