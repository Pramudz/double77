package invsys.entitiydao;

import java.util.Set;

import invsys.entities.Role;
import invsys.entities.RoleFunctions;
import javafx.collections.ObservableList;

public interface RoleDao {

	
	
	boolean saveRole(Role role);
	
	boolean deleteRole(Role role);
	
	boolean updateRole(Role role);
	 
	Role getRoleByIdAndName(int id, String userName);
	
	Role getRoleByIdWithoutJoins(int id, String userName);
	
	ObservableList<String> getRoleNames();
	
	
	ObservableList<Object[]> getRoleIdAndNames();
	
	Role getRoleById(int id);
	
	ObservableList<Role> getRoles();
	
	
	
	
	//role features/ role functions creation
	boolean createRoleFeatures(RoleFunctions roleF);
	
	ObservableList<RoleFunctions> getRoleFeatures();
	
	 ObservableList<Object[]> getRoleFeaturesForTreeView();
	
	
	Set<RoleFunctions> getDistinctRoleFeatures(String user);
	
	RoleFunctions getRoleFeatureByName(String roleFeatureName);
	
	ObservableList<RoleFunctions> getParentRoleFeaturesByType(String type);
}
