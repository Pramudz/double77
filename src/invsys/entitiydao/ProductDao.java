package invsys.entitiydao;

import java.util.HashMap;
import java.util.List;

import invsys.entities.PriceUpdate;
import invsys.entities.Products;
import invsys.entities.Supplier;
import javafx.collections.ObservableList;

public interface ProductDao {

	
	boolean saveProduct(Products product);
	
	long deletProduct(Products product);
	
	Products getProductById(long id);
	
	ObservableList<Products> getProductList();
	
	boolean updateProduct(Products product);
	
	boolean updateProductPrices(Products prdUpdate, PriceUpdate priceUpdate);
	
	ObservableList<Products> getPrductListBySupplier(Supplier sup);
		
	//specifically for supplier return
	ObservableList<String> getPrductsLikeProductName(String prdDescTextValue);
	
	// get Product by product name specially for Credit Invoice
	Products getPrductByName(String prdDescTextValue);
	
	// get Product by product name specially for Supplier Return
	Products getPrductByNameAndSupplier(String prdDescTextValue , long supId);
	
	// get Product Description like product name specially for Supplier Return
	ObservableList<String> getPrductsLikeProductNameAndSupplier(String prdDescTextValue , int supId);
	
		
	// get Product by id and Supplier id specially for Supplier Return
	Products getPrductsByIdAndSupplier(long prdId, int supId);
	
	
	//without pagination old report with multiple criteria hash
	ObservableList<Products> getPrductsByMultipleCriteriasHashSearch(HashMap<String, Object> dbTableColumns);
	
	
	//specifically for Product maintenance Table with Pagination
	HashMap<Long, List<Products>> getPrductsByMultipleCriteriasHashSearchWithPagination(
			HashMap<String, Object> dbTableColumns, int firstResult, int pageResult);
	
	//specifically for Product maintenance Table
	HashMap<Long, List<Products>> paginationWithTableView(int firstResult, int pageResult);
	
	//specifically for Purchase Order - pagination
	HashMap<Long, List<Products>> getPrdListSupplierWiseWithPagination(Supplier sup, int firstResult,
			int pageResult);
	
	// for dashboard	
	long getProductCount();
	
	// for dashboard	
	long getNoStockAvailableCount();
}
