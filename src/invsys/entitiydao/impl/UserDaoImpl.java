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
import invsys.controllers.mainpage.MainController;
import invsys.entities.Users;
import invsys.entitiydao.UserDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;

public class UserDaoImpl implements UserDao {

	private static Session session;
	
	private String adminName = "admin";

	private static UserDaoImpl daoHandler;

	// formalize method to access static method dao class
	public static UserDaoImpl getDao() {
		if (daoHandler == null) {
			daoHandler = new UserDaoImpl();
		}

		return daoHandler;

	}

	// insert into supplier data to database
	public boolean saveUser(Users user) {
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.save(user);
			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Cannot Insert Data", e.getMessage());
			return false;
		}

	}

	// insert into supplier data to database while returning last index
	public int saveUserReturnLastIndex(Users user) {
		Integer id = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.save(user);
			id = user.getUser_id();
			session.getTransaction().commit();
			return id;
		} catch (Exception e) {
			session.getTransaction().rollback();
			AlertHandler.getAlert(AlertType.ERROR, "Cannot Insert Data", e.getMessage());
			e.printStackTrace();
			return id;
		}

	}

	// Delete user Regular Method
	public int deleteUser(Users user) {
		Integer id = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.delete(user);
			id = user.getUser_id();
			session.getTransaction().commit();
			return id;
		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Cannot Insert Data", e.getMessage());
			id = null;
		}
		return id;
	}

	// update user regular method
	public boolean updateUser(Users user) {

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			System.out.println(user.getUser_id());
			Users updateUser = session.get(Users.class, user.getUser_id());
			updateUser.setActiveStat(user.isActiveStat());
			updateUser.setAddress(user.getAddress());
			updateUser.setContactNum(user.getContactNum());
			updateUser.setDate(user.getDate());
			updateUser.setFirstName(user.getFirstName());
			updateUser.setLastName(user.getLastName());
			updateUser.setRole(user.getRole());
			updateUser.setUserEmail(user.getUserEmail());
			updateUser.setUserName(user.getUserName());
			updateUser.setLastLogin(user.getLastLogin());
			updateUser.setLoginStatus(user.isLoginStatus());
			session.update(updateUser);
			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Cannot Update Data", e.getMessage());
			e.printStackTrace();
			session.getTransaction().rollback();
			return false;
		}
	}

	// update userpass word method 12th June 2023  - Used for Password Modification Controller
	public boolean updateUserPassword(Users user) {

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			System.out.println(user.getUser_id());
			session.update(user);
			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Cannot Update Data", e.getMessage());
			e.printStackTrace();
			session.getTransaction().rollback();
			return false;
		}
	}

	
	// reset userpass word method 12th June 2023
		public boolean resetUserPassword(Users user) {

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				System.out.println(user.getUser_id());
				Users updateUser = session.get(Users.class, user.getUser_id());
				updateUser.setPassword("12345678");
				session.update(updateUser);
				session.getTransaction().commit();
				return true;
			} catch (Exception e) {
				AlertHandler.getAlert(AlertType.ERROR, "Cannot Update Data", e.getMessage());
				e.printStackTrace();
				session.getTransaction().rollback();
				return false;
			}
		}
	// return list of the users lazily -- modified on 12th June to Not To Load User Maintenance
	public ObservableList<Users> getUsers() {
		List<Users> users = null;
		ObservableList<Users> listOfUsers = FXCollections.observableArrayList();
		try {
			
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Query query  = session.createQuery("from Users e WHERE e.userName != :userName");
			if(MainController.getUserSession().getUserName().contentEquals(adminName)) {
				query.setParameter("userName", "notauser");
			}
			else {
				query.setParameter("userName", adminName);
			}
			users = query.getResultList();
			session.getTransaction().commit();
			users.stream().forEach(listOfUsers::add);

		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Cannot Retrive Data", e.getMessage());
			session.getTransaction().rollback();
		}
		return listOfUsers;
	}

	// return list of the users without lazy but optimized using join
	public ObservableList<Users> getUsersWithRoles() {
		List<Users> users = null;
		ObservableList<Users> listOfUsers = FXCollections.observableArrayList();
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			users = session.createQuery("SELECT DISTINCT e FROM Users e LEFT JOIN FETCH e.role r").getResultList();
			session.getTransaction().commit();
			listOfUsers.addAll(users);

		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Cannot Retrive Data", e.getMessage());
			session.getTransaction().rollback();
		}
		return listOfUsers;
	}

	// get user by id method lazily
	public Users getUserById(int id) {
		Users user = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			user = session.get(Users.class, id);
			session.getTransaction().commit();
			return user;
		} catch (Exception e) {
			session.getTransaction().rollback();
		}
		return user;

	}

	// get user by id method without Lazy initialization but optimized using
	// JPQL (Join)
	public Users getUserByIdWithRoles(int id) {
		Users user = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Query query = session
					.createQuery("SELECT DISTINCT e FROM Users e JOIN FETCH e.role r WHERE e.user_id = :userId");
			query.setParameter("userId", id);
			user = (Users) query.getSingleResult();
			session.getTransaction().commit();
			return user;
		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Cannot Retrive Data", e.getMessage());
			session.getTransaction().rollback();
		}
		return user;

	}

	// get UserBy Name
	public Users getUserByName(String userName) {

		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Query query = session.createQuery("from Users where userName=:name");
		query.setParameter("name", userName);
		Users user = (Users) query.uniqueResult();
		session.getTransaction().commit();

		return user;
	}

	// get UserBy Name with roles & role functions
	public Users getUserByNameWithRoles(String userName) {

		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Query query = session.createQuery(
				"SELECT DISTINCT e FROM Users e INNER JOIN FETCH e.role r INNER JOIN FETCH r.roleFunctions f WHERE e.userName = :name AND f.roleAcess =:acess");
		query.setParameter("name", userName);
		query.setParameter("acess", true);
		Users user = (Users) query.uniqueResult();
		session.getTransaction().commit();

		return user;
	}
	// get user by id & Names

	public Users getUserByIdAndName(int id, String userName) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Users> critQuery = builder.createQuery(Users.class);
		Root<Users> root = critQuery.from(Users.class);
		critQuery.select(root).where(builder.equal(root.get("user_id"), id),
				builder.equal(root.get("userName"), userName));
		Query<Users> query = session.createQuery(critQuery);
		Users singleUser = query.getSingleResult();

		session.getTransaction().commit();

		return singleUser;

	}

	// check user password & username is correct when cancel Bills and other special
	// cases

	public Users getUserbyUserNamePassword(String username, String password) {

		Users user = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Query query = session.createQuery(
					"SELECT DISTINCT e FROM Users e JOIN FETCH e.role r JOIN FETCH r.roleFunctions f where e.userName=:name and e.password=:pass and f.roleAcess=:access");
			query.setParameter("name", username);
			query.setParameter("pass", password);
			query.setParameter("access", true);
			user = (Users) query.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Database Error", "User does not Exist or Hibernate Error");
			session.getTransaction().rollback();
		}

		if (user == null) {
			AlertHandler.getAlert(AlertType.ERROR, "User Does Not Exist", null);
		}
		return user;

	}

	// get usernames
	public ObservableList<String> getUserNames() {

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<String> criteria = builder.createQuery(String.class);
			Root<Users> root = criteria.from(Users.class);
			criteria.select(root.get("userName"));
			Query<String> query = session.createQuery(criteria);

			ObservableList<String> userNames = FXCollections.observableArrayList(query.getResultList());
			session.getTransaction().commit();
			return userNames;

		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Cannot Load Username list Data", e.getMessage());
			session.getTransaction().rollback();
			return null;
		}

	}

	// get UserId And Names
	public ObservableList<Object[]> getUserIdAndNames() {

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Object[]> criteria = builder.createQuery(Object[].class);
			Root<Users> root = criteria.from(Users.class);
			criteria.multiselect(root.get("user_id"), root.get("userName"));
			Query<Object[]> query = session.createQuery(criteria);

			ObservableList<Object[]> roleNames = FXCollections.observableArrayList(query.getResultList());
			session.getTransaction().commit();
			return roleNames;

		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Cannot Load Data", e.getMessage());
			session.getTransaction().rollback();
			return null;
		}

	}

	// get last inserted id
	public int getLastIndex() {
		Integer id = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Integer> criQueury = builder.createQuery(Integer.class);
			Root<Users> root = criQueury.from(Users.class);
			criQueury.select(builder.max(root.get("user_id")));
			Query<Integer> query = session.createQuery(criQueury);
			id = query.getSingleResult();
			if (id == null) {
				id = 0;
			}
			session.getTransaction().commit();

		} catch (HibernateException e) {
			AlertHandler.getAlert(AlertType.ERROR, "Cannot Load Last Inserted Id of User Data", e.getMessage());
		}

		return id;
	}

	// get This Details for Error Handling when Insertinc User entity
	public List<Users> getUserForDuplicateInsertionCheck(String userName, String email) {
		List<Users> userList = null;

		if (email.isEmpty() && email.isBlank())
			email = null;

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Users> criQueury = builder.createQuery(Users.class);
			Root<Users> root = criQueury.from(Users.class);

			Predicate predict[] = new Predicate[2];
			predict[0] = builder.equal(root.get("userName"), userName);
			predict[1] = builder.equal(root.get("userEmail"), email);
			criQueury.where(builder.or(predict));
			Query<Users> query = session.createQuery(criQueury);
			userList = query.getResultList();
			session.getTransaction().commit();

		} catch (HibernateException e) {
			AlertHandler.getAlert(AlertType.ERROR, "Cannot Access Database or Null Error", e.getCause().toString());
			session.getTransaction().rollback();
			e.printStackTrace();
		}

		return userList;
	}

	// get This Details for Error Handling when Insertinc User entity
	public List<Users> getUserForDuplicateUpdationCheck(int userId, String userName, String email) {
		List<Users> userList = null;

		if (email.isEmpty() && email.isBlank())
			email = null;

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Users> criQueury = builder.createQuery(Users.class);
			Root<Users> root = criQueury.from(Users.class);

			Predicate predict[] = new Predicate[2];
			predict[0] = builder.equal(root.get("userName"), userName);
			predict[1] = builder.equal(root.get("userEmail"), email);
			criQueury.where(builder.or(predict), builder.notEqual(root.get("user_id"), userId));
			Query<Users> query = session.createQuery(criQueury);
			userList = query.getResultList();
			session.getTransaction().commit();

		} catch (HibernateException e) {
			AlertHandler.getAlert(AlertType.ERROR, "Cannot Access Database or Null Error", e.getCause().toString());
			session.getTransaction().rollback();
			e.printStackTrace();
		}

		return userList;
	}

}
