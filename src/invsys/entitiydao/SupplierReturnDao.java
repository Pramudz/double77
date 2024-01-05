package invsys.entitiydao;

import invsys.entities.SupplierReturn;
import invsys.entities.SupplierReturnDetail;
import javafx.collections.ObservableList;

public interface SupplierReturnDao {

	boolean saveSupplierReturn(SupplierReturn supReturn, ObservableList<SupplierReturnDetail> supReturnDetails);
	
	long saveSupplierReturnWithReturnId(SupplierReturn supReturn, ObservableList<SupplierReturnDetail> supReturnDetails);
	
	
}
