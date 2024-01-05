package invsys.entitiydao;

import java.util.List;

import invsys.entities.Customer;
import javafx.collections.ObservableList;

public interface CustomerDao {

	
	boolean saveCustomer(Customer customer);
	
	long saveCustomerGetLastIndexId(Customer customer);
	
	Long deleteCustomer(Customer customer);
	
	boolean updateCustomer(Customer customer);
	
	ObservableList<Customer> getCustomers();
	
	Customer getCustomerById(long id);
	
	long getLastIndex();
		
	List<Customer> getCustomerByNicEmailContactNum(String nic, String email, String contactNum);
	
	List<Customer> getCustomerByNicEmailContactNumForUpdate(long custId, String nic, String email, String contactNum);
}
