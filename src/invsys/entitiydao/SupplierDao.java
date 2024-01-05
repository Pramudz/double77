package invsys.entitiydao;

import invsys.entities.Supplier;
import invsys.entities.SupplierBrImages;
import javafx.collections.ObservableList;

public interface SupplierDao {

	boolean saveSupplier(Supplier supplier);
	
	boolean updateSupplier(Supplier supplier);
	
	boolean updateSupplierTextOnly(Supplier supplier);
	
	int deleteSupplier(Supplier sup);
	
	ObservableList<Object> getSupplierWithImages(int id);
	
	ObservableList<SupplierBrImages> getImages(Supplier supplier);
	
	ObservableList<Supplier> getSuppliers();
	
	Supplier getSupplierByIdWithImages(int id);
	
	Supplier getSupplierById(int id);
	
	Supplier getSupplierByName(String spName);
	
	//get sup names only
	ObservableList<String> getSupName();
	
	
	ObservableList<Object[]> getSupIdAndName();
}
