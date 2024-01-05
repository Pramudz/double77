package invsys.entitiydao.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import invsys.controllers.database.HibernateUtil;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.entities.Customer;
import invsys.entitiydao.CustomerDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;

public class CustomerDaoImpl implements CustomerDao {

	
private static Session session;
	
	private static CustomerDaoImpl daoHandler;
	
	
	//formalize method to access static method dao class
		public static  CustomerDaoImpl getDao() {
			if(daoHandler == null) {
				daoHandler =  new CustomerDaoImpl();
			}
			return daoHandler;
		}
		
		

		// insert into Customer data to database
		public boolean saveCustomer(Customer customer) {
			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				session.save(customer);
				session.getTransaction().commit();
				return true;
			} catch (Exception e) {
				AlertHandler.getAlert(AlertType.ERROR, "Cannot Insert Data", e.getCause().toString());
				session.getTransaction().rollback();
				return false;
				
			}

		}
		
		// insert into Customer data to database while returning last index
		public long saveCustomerGetLastIndexId(Customer customer) {
			Long id =  null;
			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				session.save(customer);
				id = customer.getCustomerId();
				session.getTransaction().commit();
				return id;
			} catch (Exception e) {
				AlertHandler.getAlert(AlertType.ERROR, "Cannot Insert Data", e.getCause().toString());
				return (Long) null;
			}

		}
		
		// Delete Customer regular Method
		public Long deleteCustomer(Customer customer) {
			Long id =  null;
			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				session.delete(customer);
				id = customer.getCustomerId();
				session.getTransaction().commit();
				
			} catch (Exception e) {
				AlertHandler.getAlert(AlertType.ERROR, "Cannot Insert Data", e.getMessage());
				id = null;
			}
			return id;
		}
		
		// update Customer Regular Method
		public boolean updateCustomer(Customer customer) {

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				System.out.println(customer.getCustomerId());
				Customer updateCustomer = session.get(Customer.class, customer.getCustomerId());
				updateCustomer.setAddressLine02(customer.getAddressLine02());
				updateCustomer.setCity(customer.getCity());
				updateCustomer.setCustomerEmail(customer.getCustomerEmail());
				updateCustomer.setCustomerMobile(customer.getCustomerMobile());
				updateCustomer.setCustomerTelephone(customer.getCustomerTelephone());
				updateCustomer.setFirstName(customer.getFirstName());
				updateCustomer.setLastName(customer.getLastName());
				updateCustomer.setNicNumber(customer.getNicNumber());
				updateCustomer.setStreetAddress(customer.getStreetAddress());
				updateCustomer.setCompanyName(customer.getCompanyName());
				session.merge(updateCustomer);
				session.getTransaction().commit();
				return true;
			} catch (Exception e) {
				AlertHandler.getAlert(AlertType.ERROR, "Cannot Update Data", e.getCause().toString());
				e.printStackTrace();
				session.getTransaction().rollback();
				return false;
			}
		}

		// return list of the Customers
		public ObservableList<Customer> getCustomers() {
			List<Customer> customerList = null;
			ObservableList<Customer> listOfCustomers = FXCollections.observableArrayList();
			try {

				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				customerList = session.createQuery("from Customer").list();
				session.getTransaction().commit();

				customerList.stream().forEach(listOfCustomers::add);

			} catch (Exception e) {
				AlertHandler.getAlert(AlertType.ERROR, "Cannot Retrive Data", e.getMessage());
				session.getTransaction().rollback();
				return null;
			}
			return listOfCustomers;
		}
		
		// get Customer by id method 
		public Customer getCustomerById(long id) {
			Customer customer = null;
			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				customer = session.get(Customer.class, id);
				session.getTransaction().commit();
				
			} catch (Exception e) {
				session.getTransaction().rollback();
			
			}
			return customer;

		}
		
		// get last inserted id
		public long getLastIndex() {
			Long id = null;
			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<Long> criQueury = builder.createQuery(Long.class);
				Root<Customer> root = criQueury.from(Customer.class);
				criQueury.select(builder.max(root.get("customerId")));
				Query<Long> query = session.createQuery(criQueury);
				id = query.getSingleResult();
				if (id == null) {
					AlertHandler.getAlert(AlertType.INFORMATION, "Welcome you are going to create First User", "");
					id=(long) 0;
				}
				session.getTransaction().commit();

			} catch (HibernateException e) {
				AlertHandler.getAlert(AlertType.ERROR, "Cannot Load Last Inserted Id of User Data", e.getMessage());
			}

			return id;
		}
		
		
		// get This Details for Error Handling when Insertinc customer entity
				public List<Customer> getCustomerByNicEmailContactNum(String nic, String email, String contactNum) {
					List<Customer> customerList = null;
					try {
						session = HibernateUtil.getSessionFactory().getCurrentSession();
						session.beginTransaction();
						CriteriaBuilder builder = session.getCriteriaBuilder();
						CriteriaQuery<Customer> criQueury = builder.createQuery(Customer.class);
						Root<Customer> root = criQueury.from(Customer.class);
						
						Predicate predict[] = new Predicate[3];
						predict[0] = builder.equal(root.get("nicNumber"), nic);
						predict[1] = builder.equal(root.get("customerMobile"), contactNum);
						predict[2] = builder.equal(root.get("customerEmail"), email);
																	
						criQueury.where(builder.or(predict));
						Query<Customer> query = session.createQuery(criQueury);
						customerList = query.getResultList();
						session.getTransaction().commit();

					} catch (HibernateException e) {
						AlertHandler.getAlert(AlertType.ERROR, "Cannot Load Data", e.getMessage());
						session.getTransaction().rollback();
						e.printStackTrace();
					}

					return customerList;
				}
				
				// get This Details for Error Handling when Updating Customer Details
				public List<Customer> getCustomerByNicEmailContactNumForUpdate(long custId, String nic, String email, String contactNum) {
					List<Customer> customerList = null;
					try {
						session = HibernateUtil.getSessionFactory().getCurrentSession();
						session.beginTransaction();
						CriteriaBuilder builder = session.getCriteriaBuilder();
						CriteriaQuery<Customer> criQueury = builder.createQuery(Customer.class);
						Root<Customer> root = criQueury.from(Customer.class);
						
						Predicate predict[] = new Predicate[3];
						predict[0] = builder.equal(root.get("nicNumber"), nic);
						predict[1] = builder.equal(root.get("customerMobile"), contactNum);
						predict[2] = builder.equal(root.get("customerEmail"), email);
																	
						criQueury.where(builder.or(predict),builder.notEqual(root.get("customerId"), custId));
						Query<Customer> query = session.createQuery(criQueury);
						customerList = query.getResultList();
						session.getTransaction().commit();

					} catch (HibernateException e) {
						AlertHandler.getAlert(AlertType.ERROR, "Cannot Load Data", e.getMessage());
						session.getTransaction().rollback();
						e.printStackTrace();
					}

					return customerList;
				}


}
