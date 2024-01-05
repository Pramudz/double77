package invsys.entitiydao.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import invsys.controllers.database.HibernateUtil;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.controllers.mainpage.MainController;
import invsys.entities.Role;
import invsys.entities.RoleFunctions;
import invsys.entities.Role_;
import invsys.entities.Users;
import invsys.entities.Users_;
import invsys.entitiydao.RoleDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;

public class RoleDaoImpl implements RoleDao {

	private static Session session;

	
	
	private static RoleDaoImpl daoHandler;
	
	
	//formalize method to access static method dao class
	public static RoleDaoImpl getDao() {
		if(daoHandler == null) {
			daoHandler = new RoleDaoImpl();
		}
		
			return daoHandler;
		
	}
	// insert into Role data to database
	public boolean saveRole(Role role) {
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.save(role);
			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			
			AlertHandler.getAlert(AlertType.ERROR, "SQL Error"+e.getCause(), e.getMessage());
			e.printStackTrace();
			session.getTransaction().rollback();
			return false;
		}

	}
	
	
	//create roleFeatures
	public boolean createRoleFeatures(RoleFunctions roleF) {
		
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.save(roleF);
			session.getTransaction().commit();
			return true;
			
		}catch (Exception e) {
			session.getTransaction().rollback();
			AlertHandler.getAlert(AlertType.ERROR, "SQL Error"+ e.getCause(), e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	
	//get parent role features by function type "Parent" or "Report"
	public ObservableList<RoleFunctions> getParentRoleFeaturesByType(String type) {
		List<RoleFunctions> roleFeatureTempList = null;
		ObservableList<RoleFunctions> roleFeatureTempLists = FXCollections.observableArrayList();
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<RoleFunctions> criteria = builder.createQuery(RoleFunctions.class);
			Root<RoleFunctions> root = criteria.from(RoleFunctions.class);
			
			if(type.contentEquals("Parent")) {
				criteria.where(builder.equal(root.get("mainRoleFunction"), null));
			}
			if(type.contentEquals("Report")) {
				criteria.where(builder.equal(root.get("mainRoleFunction"), "Reports"));
			}
			roleFeatureTempList = session.createQuery(criteria).getResultList();
			session.getTransaction().commit();
			roleFeatureTempList.stream().forEach(roleFeatureTempLists::add);
			return roleFeatureTempLists;
			
		}catch(Exception e) {
			
			session.getTransaction().rollback();
			AlertHandler.getAlert(AlertType.ERROR, "SQL Error"+ e.getCause(), e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	
	//get roleFeatures only nodes with parent nodes
	public ObservableList<RoleFunctions> getRoleFeatures(){
		List<RoleFunctions> roleFeatureTempList = null;
		ObservableList<RoleFunctions> listOfFeatures = FXCollections.observableArrayList();
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			roleFeatureTempList = session.createQuery("from RoleFunctions").getResultList();
			session.getTransaction().commit();
			roleFeatureTempList.stream().forEach(listOfFeatures::add);
			return listOfFeatures;
			
		}catch (Exception e) {
			session.getTransaction().rollback();
			AlertHandler.getAlert(AlertType.ERROR, "SQL Error"+ e.getCause(), e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	
	//get roleFeatures for RoleMamagment Treeview to make treeview from leafnodes to backward towards parent nodes
	// funtionId, function Name , parentId, JoinedLeafNodeId, LeafName
		public ObservableList<Object[]> getRoleFeaturesForTreeView(){
			List<Object[]> roleFeatureTempList = null;
			ObservableList<Object[]> listOfFeatures = FXCollections.observableArrayList();
			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				
				
				String sql = "select distinct rl.func_id as functionId , rl.role_function_name as functionName , rl.main_role_function  as parentId,"+
						"rr.func_id as joinedLeafId , rr.role_function_name as leafName from rolefunctions rl left join rolefunctions rr on rl.func_id=rr.main_role_function where rr.func_id is null order by rl.main_role_function";
				Query<Object[]> query = session.createNativeQuery(sql);
				roleFeatureTempList = query.getResultList();
				session.getTransaction().commit();
				roleFeatureTempList.stream().forEach(listOfFeatures::add);
				return listOfFeatures;
				
			}catch (Exception e) {
				session.getTransaction().rollback();
				AlertHandler.getAlert(AlertType.ERROR, "SQL Error"+ e.getCause(), e.getMessage());
				e.printStackTrace();
				return null;
			}
		}
		
		
	//get distinct RoleFeature
		public Set<RoleFunctions> getDistinctRoleFeatures(String user){
			List<RoleFunctions> roleFeatureTempList = null;
			Set<RoleFunctions> listOfFeatures = new HashSet<RoleFunctions>();
			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				Query query = session.createQuery("SELECT DISTINCT e FROM RoleFunctions e INNER JOIN FETCH e.rolelist x INNER JOIN FETCH x.users r where r.userName=:add order by e.roleFunction");
				
				CriteriaBuilder builder =session.getCriteriaBuilder();
				CriteriaQuery<RoleFunctions> critQuery = builder.createQuery(RoleFunctions.class);
				Root<Users> root = critQuery.from(Users.class);
				Join<Users, Role> role = root.join(Users_.ROLE);
				Join<Role, RoleFunctions> roleFunctions = role.join(Role_.ROLE_FUNCTIONS);
				critQuery.where(builder.equal(root.get("userName"), user));
				
								
				query.setParameter("add", user);
				roleFeatureTempList = query.getResultList();
				session.getTransaction().commit();
				roleFeatureTempList.stream().forEach(listOfFeatures::add);
				return listOfFeatures;
				
			}catch (Exception e) {
				session.getTransaction().rollback();
				AlertHandler.getAlert(AlertType.ERROR, "SQL Error"+ e.getCause(), e.getMessage());
				e.printStackTrace();
				return null;
			}
		}
	
	//get Role Feature By Name
		public RoleFunctions getRoleFeatureByName(String roleFeatureName){
		
			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				Query query = session.createQuery("from RoleFunctions where roleFunction=:feature");
				query.setParameter("feature", roleFeatureName);
				RoleFunctions rolef = (RoleFunctions) query.getSingleResult();
				session.getTransaction().commit();
				return rolef;
				
				
			}catch (Exception e) {
				session.getTransaction().rollback();
				AlertHandler.getAlert(AlertType.ERROR, "SQL Error"+ e.getCause(), e.getMessage());
				e.printStackTrace();
				return null;
			}
		}
	
		
	//delete role
	public boolean deleteRole(Role role) {
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			role.setRoleFunctions(role.getRoleFunctions());
			session.merge(role);
			Role rolex = session.get(Role.class, role.getRoleId());
			session.remove(rolex);
			session.getTransaction().commit();
			
			return true;
		}catch(Exception e) {
			session.getTransaction().rollback();
			AlertHandler.getAlert(AlertType.ERROR, "SQL Error"+ e.getCause(), e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	//get all roles
	public ObservableList<Role> getRoles() {
		List<Role> role = null;
		ObservableList<Role> listOfRoles = FXCollections.observableArrayList();
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			role = session.createQuery("from Role").list();
			session.getTransaction().commit();
			role.stream().forEach(listOfRoles::add);

		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "SQL Error "+e.getCause(), e.getMessage());
			session.getTransaction().rollback();
		}
		return listOfRoles;
	}

	// get Role by id method
	public Role getRoleById(int id) {
		Role role = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			role = session.get(Role.class, id);
			session.getTransaction().commit();
			return role;
		} catch (Exception e) {
			session.getTransaction().rollback();
		}
		return role;

	}

	// update Role only for role functions
	public boolean updateRole(Role role) {
		
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Role updateRole = session.get(Role.class, role.getRoleId());
			updateRole.setRoleName(role.getRoleName());
			updateRole.setRoleFunctions(role.getRoleFunctions());
			updateRole.setRoleDesc(role.getRoleDesc());
			session.merge(role);
			session.getTransaction().commit();
			return true;
			
		} catch(HibernateException e) {
			AlertHandler.getAlert(AlertType.ERROR, "Cannot Update Sql Error", e.getMessage());
			session.getTransaction().rollback();
			return false;
		}
		

	}


	//get role by id and name and inner join role functions
	public Role getRoleByIdAndName(int id, String userName) {
		Role singleRole = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Role> critQuery = builder.createQuery(Role.class);
			Root<Role> root = critQuery.from(Role.class);
			root.fetch("roleFunctions",JoinType.LEFT);
			
			critQuery.select(root).where(builder.equal(root.get("roleId"), id),
					builder.equal(root.get("roleName"), userName));
			
			Query<Role> query = session.createQuery(critQuery);
			singleRole = query.getSingleResult();
			session.getTransaction().commit();

		}catch (HibernateException e) {
			AlertHandler.getAlert(AlertType.ERROR, "SQL Error"+e.getCause(), e.getLocalizedMessage());
			session.getTransaction().rollback();
			e.printStackTrace();
		}catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "SQL Error"+e.getCause(), e.getLocalizedMessage());
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		

		return singleRole;

	}
	//get role by id and name and inner join role functions and their rolelist 
	// removed lazy initialization from this in order to make the update works
	public Role getRoleByIdWithoutJoins(int id, String userName) {
		Role singleRole = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Role> critQuery = builder.createQuery(Role.class);
			Root<Role> root = critQuery.from(Role.class);
		
			critQuery.select(root).where(builder.equal(root.get("roleId"), id),
					builder.equal(root.get("roleName"), userName));
			
			Query<Role> query = session.createQuery(critQuery);
			singleRole = query.getSingleResult();
			session.getTransaction().commit();

		}catch (HibernateException e) {
			AlertHandler.getAlert(AlertType.ERROR, "SQL Error"+e.getCause(), e.getLocalizedMessage());
			session.getTransaction().rollback();
			e.printStackTrace();
		}catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "SQL Error"+e.getCause(), e.getLocalizedMessage());
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		

		return singleRole;

	}

	
	// get role names only  Modified on 12th June 2023 for Admin only can
	// visible root role details
	public ObservableList<String> getRoleNames() {

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<String> criteria = builder.createQuery(String.class);
			Root<Role> root = criteria.from(Role.class);
			criteria.select(root.get("roleName"));
			if(!MainController.getUserSession().getUserName().contentEquals("admin")) {
				criteria.where(builder.notEqual(root.get("roleName"), "root"));
				}
			Query<String> query = session.createQuery(criteria);

			ObservableList<String> roleNames = FXCollections.observableArrayList(query.getResultList());
			session.getTransaction().commit();
			return roleNames;

		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "SQL Error"+e.getCause(), e.getLocalizedMessage());
			session.getTransaction().rollback();
			e.printStackTrace();
			return null;
		}

	}

	//get role id and names list - Modified on 12th June 2023 for Admin only can
	// visible root role details
	public ObservableList<Object[]> getRoleIdAndNames() {

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Object[]> criteria = builder.createQuery(Object[].class);
			Root<Role> root = criteria.from(Role.class);
			if(!MainController.getUserSession().getUserName().contentEquals("admin")) {
			criteria.where(builder.notEqual(root.get("roleName"), "root"));
			}
			criteria.multiselect(root.get("roleName"), root.get("roleId"));
			Query<Object[]> query = session.createQuery(criteria);

			ObservableList<Object[]> roleNames = FXCollections.observableArrayList(query.getResultList());
			session.getTransaction().commit();
			return roleNames;

		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "SQL Error"+e.getCause(), e.getLocalizedMessage());
			session.getTransaction().rollback();
			e.printStackTrace();
			return null;
		}

	}
}
