package invsys.entitiydao.impl;

import java.util.HashMap;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import invsys.controllers.database.HibernateUtil;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.entities.PriceUpdate;
import invsys.entities.Products;
import invsys.entities.Supplier;
import invsys.entitiydao.ProductDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ProductDaoImpl implements ProductDao {

	private static Session session;
	private static ProductDaoImpl daoHandler;

	// formalize method to access static method dao class
	public static ProductDaoImpl getDao() {
		if (daoHandler == null) {
			daoHandler = new ProductDaoImpl();
		}
		return daoHandler;

	}

	// save product
	public boolean saveProduct(Products product) {
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.save(product);
			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Cannot Insert Data");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
			session.getTransaction().rollback();
			return false;
		}

	}

	public long deletProduct(Products product) {
		Long id = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.delete(product);
			session.getTransaction().commit();
			id = product.getPrd_id();
		} catch (Exception e) {
			session.getTransaction().rollback();
			AlertHandler.getAlert(AlertType.ERROR, "Deletion Error", e.getLocalizedMessage());
			e.printStackTrace();

		}
		return id;
	}

	// get product by id
	public Products getProductById(long id) {
		Products product = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			product = session.get(Products.class, id);
			session.getTransaction().commit();

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Cannot Retrive Data");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
			session.getTransaction().rollback();

		}

		return product;
	}

	// get product list
	public ObservableList<Products> getProductList() {
		List<Products> prdList = null;
		ObservableList<Products> listOfProduct = FXCollections.observableArrayList();
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Products> critQuery = builder.createQuery(Products.class);

			Root<Products> root = critQuery.from(Products.class);
			root.fetch("category", JoinType.INNER);
			root.fetch("supplier", JoinType.INNER);

			prdList = session.createQuery(critQuery).getResultList();
			session.getTransaction().commit();
			prdList.stream().forEach(listOfProduct::add);
		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Cannot Retrive Data", e.getMessage());
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		return listOfProduct;
	}

	// update Product
	public boolean updateProduct(Products product) {

		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Products prdUpdate = session.get(Products.class, product.getPrd_id());
			prdUpdate.setP_name(product.getP_name());
			prdUpdate.setCategory(product.getCategory());
			prdUpdate.setSupplier(product.getSupplier());
			prdUpdate.setPack_size(product.getPack_size());
			
			//ignored below due to it should not be updated from main method
			
			//prdUpdate.setPack_price(product.getPack_price());
			//prdUpdate.setPack_cost(product.getPack_cost());
			//prdUpdate.setUnit_price(product.getUnit_price());
			//prdUpdate.setUnit_cost_price(product.getUnit_cost_price());
			//prdUpdate.setUnitAverageCost(product.getUnitAverageCost());
			
			prdUpdate.setDiscount(product.getDiscount());
			prdUpdate.setReOrderLevel(product.getReOrderLevel());
			prdUpdate.setStatus(product.isStatus());
			prdUpdate.setVat(product.getVat());
			
		
			prdUpdate.setUnitOfMeasure(product.getUnitOfMeasure());
			session.update(prdUpdate);
			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Update Error", e.getLocalizedMessage());
			session.getTransaction().rollback();
			return false;
		}
	}

	// update price changes with update prices log table
	public boolean updateProductPrices(Products prdUpdate, PriceUpdate priceUpdate) {

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.update(prdUpdate);
			session.save(priceUpdate);
			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Update Error", e.getLocalizedMessage());
			session.getTransaction().rollback();
			return false;
		}
	}

	// get product list by supplier id
	public ObservableList<Products> getPrductListBySupplier(Supplier sup) {
		ObservableList<Products> prdList = FXCollections.observableArrayList();
		List<Products> listOfProducts = null;

		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		CriteriaBuilder bulder = session.getCriteriaBuilder();
		CriteriaQuery<Products> critQuery = bulder.createQuery(Products.class);
		Root<Products> root = critQuery.from(Products.class);
		root.fetch("category", JoinType.INNER);
		root.fetch("supplier", JoinType.INNER);
		critQuery.select(root).where(bulder.equal(root.get("supplier"), sup));
		listOfProducts = session.createQuery(critQuery).list();

		session.getTransaction().commit();

		listOfProducts.stream().forEach(prdList::add);

		return prdList;
	}

	// get Product Description like product name specially for Credit Invoice
	public ObservableList<String> getPrductsLikeProductName(String prdDescTextValue) {

		ObservableList<String> listToReturn = FXCollections.observableArrayList();
		List<String> listOfProductDescriptions = null;

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();

			session.beginTransaction();
			CriteriaBuilder bulder = session.getCriteriaBuilder();
			CriteriaQuery<String> critQuery = bulder.createQuery(String.class);
			Root<Products> root = critQuery.from(Products.class);
			//root.fetch("category", JoinType.INNER);
			//root.fetch("supplier", JoinType.INNER);
			critQuery.select(root.get("p_name")).where(bulder.and(bulder.like(root.get("p_name"), prdDescTextValue + "%"),bulder.equal(root.get("status"),true)));
			listOfProductDescriptions = session.createQuery(critQuery).setMaxResults(10).getResultList();
			session.getTransaction().commit();

			listOfProductDescriptions.stream().forEach(listToReturn::add);

			return listToReturn;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			return null;
		}
	}

	// get Product by product name specially for Credit Invoice
	public Products getPrductByName(String prdDescTextValue) {

		Products listOfProductDescriptions = null;

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();

			session.beginTransaction();
			CriteriaBuilder bulder = session.getCriteriaBuilder();
			CriteriaQuery<Products> critQuery = bulder.createQuery(Products.class);
			Root<Products> root = critQuery.from(Products.class);
			root.fetch("category", JoinType.INNER);
			root.fetch("supplier", JoinType.INNER);
			Predicate[] predicate = new Predicate[2];
			predicate[0] = bulder.equal(root.get("p_name"), prdDescTextValue);
			predicate[1] = bulder.equal(root.get("status"), true);
			critQuery.select(root).where(predicate);
			listOfProductDescriptions = session.createQuery(critQuery).getSingleResult();
			session.getTransaction().commit();

			return listOfProductDescriptions;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			return null;
		}
	}
	
	
	// get Product by product name specially for Supplier Return
	public Products getPrductByNameAndSupplier(String prdDescTextValue , long supId) {

		Products listOfProductDescriptions = null;

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();

			session.beginTransaction();
			CriteriaBuilder bulder = session.getCriteriaBuilder();
			CriteriaQuery<Products> critQuery = bulder.createQuery(Products.class);
			Root<Products> root = critQuery.from(Products.class);
			root.fetch("category", JoinType.INNER);
			root.fetch("supplier", JoinType.INNER);
			critQuery.select(root).where(bulder.and(bulder.equal(root.get("p_name"), prdDescTextValue),
					bulder.equal(root.get("supplier").get("sup_id"), supId),bulder.equal(root.get("status"), true)));
			
			listOfProductDescriptions = session.createQuery(critQuery).getSingleResult();
			session.getTransaction().commit();

			return listOfProductDescriptions;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			return null;
		}
	}
	
	// get Product Description like product name specially for Supplier Return
		public ObservableList<String> getPrductsLikeProductNameAndSupplier(String prdDescTextValue , int supId) {

			ObservableList<String> listToReturn = FXCollections.observableArrayList();
			List<String> listOfProductDescriptions = null;

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();

				session.beginTransaction();
				CriteriaBuilder bulder = session.getCriteriaBuilder();
				CriteriaQuery<String> critQuery = bulder.createQuery(String.class);
				Root<Products> root = critQuery.from(Products.class);
				critQuery.select(root.get("p_name")).where(bulder.and(bulder.like(root.get("p_name"), prdDescTextValue + "%"),
				bulder.equal(root.get("supplier").get("sup_id"), supId),bulder.equal(root.get("status"), true)));
				listOfProductDescriptions = session.createQuery(critQuery).setMaxResults(10).getResultList();
				session.getTransaction().commit();

				listOfProductDescriptions.stream().forEach(listToReturn::add);

				return listToReturn;
			} catch (Exception e) {
				e.printStackTrace();
				session.getTransaction().rollback();
				return null;
			}
		}


	// get Product by id and name
	public static Products getPrductsByIdAndName(String prdName, long prdId) {
		Products product = null;
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Products> critQuery = builder.createQuery(Products.class);
			Root<Products> root = critQuery.from(Products.class);
			root.fetch("category", JoinType.INNER);
			root.fetch("supplier", JoinType.INNER);

			Predicate[] predict = new Predicate[2];
			predict[1] = builder.equal(root.get("prd_id"), prdId);
			predict[0] = builder.equal(root.get("p_name"), prdName);

			critQuery.select(root).where(predict);
			product = session.createQuery(critQuery).getSingleResult();
			session.getTransaction().commit();

		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, e.getLocalizedMessage(), e.getCause().toString());
			session.getTransaction().rollback();

		}

		return product;

	}
	
	// get Product by id and Supplier id specially for Supplier Return
	public Products getPrductsByIdAndSupplier(long prdId, int supId) {
		Products product = null;
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Products> critQuery = builder.createQuery(Products.class);
			Root<Products> root = critQuery.from(Products.class);
			root.fetch("category", JoinType.INNER);
			root.fetch("supplier", JoinType.INNER);
			Predicate[] predict = new Predicate[3];
			predict[0] = builder.equal(root.get("prd_id"), prdId);
			predict[1] = builder.equal(root.get("supplier").get("sup_id"), supId);
			predict[2] = builder.equal(root.get("status"), true);
		
			critQuery.select(root).where(predict);
			product = session.createQuery(critQuery).getSingleResult();
			session.getTransaction().commit();
			return product;

		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, e.getLocalizedMessage(), String.valueOf(e.getCause()));
			session.getTransaction().rollback();
			return null;

		}

		

	}

	// updated multiple criteria search
	public ObservableList<Products> getPrductsByMultipleCriteriasHashSearch(HashMap<String, Object> map) {
		ObservableList<Products> prdList = FXCollections.observableArrayList();
		List<Products> listOfProducts = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Products> critQuery = builder.createQuery(Products.class);
			Root<Products> root = critQuery.from(Products.class);
			root.fetch("category", JoinType.INNER);
			root.fetch("supplier", JoinType.INNER);

			Predicate[] predicate = new Predicate[map.size()];
			System.out.println(map.size());

			int i = 0;
			for (String x : map.keySet()) {
				if (x.equals("Plu")) {

					predicate[i] = builder.equal(root.get("prd_id"), map.get(x));
				}

				if (x.equals("prdCat")) {

					predicate[i] = builder.equal(root.get("category").get("categoryName"), map.get(x));
				}

				if (x.equals("prdName")) {

					predicate[i] = builder.like(root.get("p_name"), "%" + map.get(x) + "%");
				}
				if (x.equals("prdSup")) {

					predicate[i] = builder.equal(root.get("supplier").get("com_name"), map.get(x));
				}

				if (x.equals("packSize")) {

					predicate[i] = builder.equal(root.get("pack_size"), map.get(x));
				}
				if (x.equals("packPrice")) {

					predicate[i] = builder.equal(root.get("pack_price"), map.get(x));
				}
				if (x.equals("packCost")) {

					predicate[i] = builder.equal(root.get("pack_cost"), map.get(x));
				}
				if (x.equals("prdUnitPrice")) {

					predicate[i] = builder.equal(root.get("unit_price"), map.get(x));
				}
				if (x.equals("prdUnitCost")) {

					predicate[i] = builder.equal(root.get("unit_cost_price"), map.get(x));
				}
				if (x.equals("discount")) {

					predicate[i] = builder.equal(root.get("discount"), map.get(x));
				}

				if (x.equals("prdUnitCost")) {

					predicate[i] = builder.equal(root.get("unit_cost_price"), map.get(x));
				}
				if (x.equals("reorder")) {

					predicate[i] = builder.equal(root.get("reOrderLevel"), map.get(x));
				}
				i++;
			}

			if (i == 0) {
				predicate = null;
			}
			System.out.println("Total" + i);
			critQuery.select(root).where(predicate);
			listOfProducts = session.createQuery(critQuery).getResultList();
			session.getTransaction().commit();
			listOfProducts.stream().forEach(prdList::add);

		} catch (Exception e) {

			session.getTransaction().rollback();

		}

		return prdList;

	}

	// updated multiple criteria search Testing
	public HashMap<Long, List<Products>> getPrductsByMultipleCriteriasHashSearchWithPagination(
			HashMap<String, Object> map, int firstResult, int pageResult) {

		try {
			HashMap<Long, List<Products>> resultMap = new HashMap();
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery critQuery = builder.createQuery(Products.class);
			Root<Products> root = critQuery.from(Products.class);
			

			// join without fetching in order to get the count of result
			Join<Object, Object> supplier = root.join("supplier");
			Join<Object, Object> category = root.join("category");
			

			Predicate[] predicate = new Predicate[map.size()];
			System.out.println(map.size());

			int i = 0;
			for (String x : map.keySet()) {
				if (x.equals("Plu")) {

					predicate[i] = builder.equal(root.get("prd_id"), map.get(x));
				}

				if (x.equals("prdCat")) {

					predicate[i] = builder.equal(category.get("categoryName"), map.get(x));
				}

				if (x.equals("prdName")) {

					predicate[i] = builder.like(root.get("p_name"), "%" + map.get(x) + "%");
				}
				if (x.equals("prdSup")) {

					predicate[i] = builder.equal(supplier.get("com_name"), map.get(x));
				}

				if (x.equals("packSize")) {

					predicate[i] = builder.equal(root.get("pack_size"), map.get(x));
				}
				if (x.equals("packPrice")) {

					predicate[i] = builder.equal(root.get("pack_price"), map.get(x));
				}
				if (x.equals("packCost")) {

					predicate[i] = builder.equal(root.get("pack_cost"), map.get(x));
				}
				if (x.equals("prdUnitPrice")) {

					predicate[i] = builder.equal(root.get("unit_price"), map.get(x));
				}
				if (x.equals("prdUnitCost")) {

					predicate[i] = builder.equal(root.get("unit_cost_price"), map.get(x));
				}
				if (x.equals("discount")) {

					predicate[i] = builder.equal(root.get("discount"), map.get(x));
				}

				if (x.equals("prdUnitCost")) {

					predicate[i] = builder.equal(root.get("unit_cost_price"), map.get(x));
				}
				if (x.equals("reorder")) {

					predicate[i] = builder.equal(root.get("reOrderLevel"), map.get(x));
				}
				i++;
			}

			if (i == 0) {
				predicate = null;
			}
			System.out.println("Total Predicates " + i);
			long countOfResult = 0;
			if (firstResult == 0) {
				critQuery.where(predicate);
				critQuery.select(builder.count(root));
				Query<Long> count = session.createQuery(critQuery);
				countOfResult = count.getSingleResult();
			} else {
				
				// in order to avoid two database calls for supplier and category fetch should be done
				// for first call to get count fetch does not matter and work JPA guideline
				root.fetch("category", JoinType.INNER);
				root.fetch("supplier", JoinType.INNER);
			}


			critQuery.where(predicate);
			critQuery.orderBy(builder.asc(root.get("prd_id")));
			critQuery.select(root);
			Query query = session.createQuery(critQuery);
			query.setFirstResult(firstResult);
			query.setMaxResults(pageResult);
			List<Products> list = query.getResultList();
			resultMap.put(countOfResult, list);
			if (list.isEmpty()) {
				resultMap = null;
			}
			session.getTransaction().commit();
			return resultMap;
		} catch (Exception e) {

			session.getTransaction().rollback();
			e.printStackTrace();
			return null;

		}

	}

	// testing code for pagination with table view with two database call
	// finally replaced with loadItems button clicked from ProductController
	public HashMap<Long, List<Products>> paginationWithTableView(int firstResult, int pageResult) {

		try {
			HashMap<Long, List<Products>> map = new HashMap();
			if (firstResult == 0) {
				firstResult = 0;
			}
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery prdCriteria = builder.createQuery(Integer.class);
			Root<Products> root = prdCriteria.from(Products.class);
			
			long countOfResult = 0;
			if (firstResult == 0) {
				prdCriteria.select(builder.count(root));
				Query<Long> count = session.createQuery(prdCriteria);
				countOfResult = count.getSingleResult();
			}

			prdCriteria = builder.createQuery(Products.class);
			root = prdCriteria.from(Products.class);
			root.fetch("category", JoinType.INNER);
			root.fetch("supplier", JoinType.INNER);
			prdCriteria.orderBy(builder.asc(root.get("prd_id")));
			prdCriteria.select(root);
			
			Query query = session.createQuery(prdCriteria);

			query.setFirstResult(firstResult);
			query.setMaxResults(pageResult);
			List<Products> list = query.getResultList();
			map.put(countOfResult, list);
			session.getTransaction().commit();
			if (map.entrySet().isEmpty()) {
				return null;
			} else {
				return map;
			}

		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "No Data Found", e.getMessage());
			session.getTransaction().rollback();
			e.printStackTrace();
			return null;
		}

	}

	// pagination result method for product list as per the selected supplier for
	// purchase order
	public HashMap<Long, List<Products>> getPrdListSupplierWiseWithPagination(Supplier sup, int firstResult,
			int pageResult) {

		try {
			HashMap<Long, List<Products>> map = new HashMap();
			if (firstResult == 0) {
				firstResult = 0;
			}
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery prdCriteria = builder.createQuery(Products.class);
			Root<Products> root = prdCriteria.from(Products.class);
		
			// join without fetching in order to get the count of result
			Join<Object, Object> supp = root.join("supplier");
			Predicate[] predicate = new Predicate[2];
			predicate[0] = builder.equal(root.get("status"), true);
			predicate[1] = builder.equal(supp.get("sup_id"), sup.getSup_id());

			long countOfResult = 0;
			if (firstResult == 0) {
				prdCriteria.where(predicate);
				prdCriteria.select(builder.count(root));
				Query<Long> count = session.createQuery(prdCriteria);
				countOfResult = count.getSingleResult();
			} else {
				root.fetch("category", JoinType.INNER);
				root.fetch("supplier", JoinType.INNER);
			}

			
			prdCriteria.where(predicate);
			prdCriteria.select(root);
			Query query = session.createQuery(prdCriteria);
			query.setFirstResult(firstResult);
			query.setMaxResults(pageResult);
			List<Products> list = query.getResultList();
			map.put(countOfResult, list);
			session.getTransaction().commit();
			if (map.entrySet().isEmpty()) {
				return null;
			} else {
				return map;
			}

		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "No Data Found", e.getMessage());
			session.getTransaction().rollback();
			e.printStackTrace();
			return null;
		}

	}

	// get count of all products available for: DashBoard
	public long getProductCount() {
		long countOfResult = 0;
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery prdCriteria = builder.createQuery(Long.class);
			Root<Products> root = prdCriteria.from(Products.class);

			prdCriteria.select(builder.count(root));
			Query<Long> count = session.createQuery(prdCriteria);
			countOfResult = count.getSingleResult();
			session.getTransaction().commit();

		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "No Data Found", e.getMessage());
			session.getTransaction().rollback();
			e.printStackTrace();

		}

		return countOfResult;

	}

	// get count of all products with no stock available for: DashBoard
	public long getNoStockAvailableCount() {
		long countOfResult = 0;
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery prdCriteria = builder.createQuery(Long.class);
			Root<Products> root = prdCriteria.from(Products.class);

			
			
			
			prdCriteria.select(builder.count(root)).where(builder.lessThanOrEqualTo(root.get("onHandQty"), 0));
			Query<Long> count = session.createQuery(prdCriteria);
			countOfResult = count.getSingleResult();
			session.getTransaction().commit();

		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "No Data Found", e.getMessage());
			session.getTransaction().rollback();
			e.printStackTrace();

		}

		return countOfResult;

	}

}
