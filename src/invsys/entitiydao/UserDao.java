package invsys.entitiydao;

import java.util.List;

import invsys.entities.Users;
import javafx.collections.ObservableList;

public interface UserDao {

	boolean saveUser(Users user);
	
	int saveUserReturnLastIndex(Users user);
	
	int deleteUser(Users user);
	
	boolean updateUser(Users user);
	
	boolean updateUserPassword(Users user);
	
	boolean resetUserPassword(Users user);
	
	ObservableList<Users> getUsers();
	
	// return list of the users without lazy but optimized using join
	ObservableList<Users> getUsersWithRoles();
	
	Users getUserById(int id);
	
	// get user by id method without Lazy initialization but optimized using
		// JPQL (Join)
	Users getUserByIdWithRoles(int id);
	
	Users getUserByName(String userName);
	
	Users getUserByNameWithRoles(String userName);
	
	Users getUserByIdAndName(int id, String userName);
	
	Users getUserbyUserNamePassword(String username, String password);
	
	ObservableList<String> getUserNames();
	
	ObservableList<Object[]> getUserIdAndNames();
	
	int getLastIndex();
	
	List<Users> getUserForDuplicateInsertionCheck(String userName, String email);
	
	List<Users> getUserForDuplicateUpdationCheck(int userId,String userName, String email);
	
	
}
