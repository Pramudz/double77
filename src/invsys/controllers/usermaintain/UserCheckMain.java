package invsys.controllers.usermaintain;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import invsys.entities.PayModes;
import invsys.entities.Role;
import invsys.entities.RoleFunctions;
import invsys.entities.Users;
import invsys.entitiydao.PayModeDao;
import invsys.entitiydao.RoleDao;
import invsys.entitiydao.UserDao;
import invsys.entitiydao.impl.PayModeDaoImpl;
import invsys.entitiydao.impl.RoleDaoImpl;
import invsys.entitiydao.impl.UserDaoImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UserCheckMain extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		
		
		
		
		
		/*
		 * for(int i=0; i<50; i++) { Category cat = new Category();
		 * cat.setCategory_id("CAT_"+i); cat.setCategoryName("Category");
		 * CategoryDaoImpl.getDao().saveCategory(cat); cat = null; }
		 * 
		 * 
		 * 
		 * 
		 * 
		 * Supplier sup = SupplierDaoImpl.getDao().getSupplierById(1); Category cat =
		 * CategoryDaoImpl.getDao().getCategory("CAT_1"); for(int i=0; i<1000; i++) {
		 * Products prd = new Products(); prd.setCategory(cat); prd.setDiscount(0);
		 * prd.setP_name("product"+i); prd.setPack_cost(1000); prd.setPack_price(1500);
		 * prd.setPack_size(1); prd.setReOrderLevel(30); prd.setStatus(true);
		 * prd.setSupplier(sup); prd.setUnit_cost_price(1000); prd.setUnit_price(1500);
		 * prd.setVat(8);
		 * 
		 * ProductDaoImpl.getDao().saveProduct(prd); }
		 */
		
		PayModeDao payModeDao = PayModeDaoImpl.getDao();
		RoleDao roleDao = RoleDaoImpl.getDao();
		
		UserDao userDao = UserDaoImpl.getDao();
		
		 
		Set<RoleFunctions> func = new HashSet<RoleFunctions>();
		
		RoleFunctions f1 = new RoleFunctions();
		f1.setRoleFunction("Products");
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		f1 = new RoleFunctions();
		f1.setRoleFunction("Customer Master");
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		f1 = new RoleFunctions();
		f1.setRoleFunction("Point Of Sales");
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		f1 = new RoleFunctions();
		f1.setRoleFunction("Supplier");
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		f1 = new RoleFunctions();
		f1.setRoleFunction("Daily Operations");
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		f1 = new RoleFunctions();
		f1.setRoleFunction("Reports");
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		f1 = new RoleFunctions();
		f1.setRoleFunction("User");
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		f1 = new RoleFunctions();
		f1.setRoleFunction("Monthly Operations");
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		f1 = new RoleFunctions();
		f1.setRoleFunction("Create PO");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Daily Operations"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		f1 = new RoleFunctions();
		f1.setRoleFunction("Grn Entry");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Daily Operations"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		f1 = new RoleFunctions();
		f1.setRoleFunction("Product Maintenance");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Products"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		f1 = new RoleFunctions();
		f1.setRoleFunction("Create Category");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Products"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		f1 = new RoleFunctions();
		f1.setRoleFunction("Price Update");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Products"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		f1 = new RoleFunctions();
		f1.setRoleFunction("Billing");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Point Of Sales"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		f1 = new RoleFunctions();
		f1.setRoleFunction("Cash Registry");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Point Of Sales"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		f1 = new RoleFunctions();
		f1.setRoleFunction("Customer Refunds");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Point Of Sales"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		f1 = new RoleFunctions();
		f1.setRoleFunction("Credit Invoice");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Daily Operations"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		f1 = new RoleFunctions();
		f1.setRoleFunction("Supplier Return");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Supplier"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		f1 = new RoleFunctions();
		f1.setRoleFunction("Inventory Count");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Monthly Operations"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		f1 = new RoleFunctions();
		f1.setRoleFunction("Expenses");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Monthly Operations"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("CRDN Settle");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Monthly Operations"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("Supplier Master");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Supplier"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("Supplier Payments");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Supplier"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("User Maintenance");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("User"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("User Roles");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("User"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("Bill Canceling");
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("Setting");
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("Company Info");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Setting"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("Manager Dashboard");
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		Role role = new Role();
		role.setRoleName("root");
		
		
		
		role.setRoleFunctions(func);
		
		roleDao.saveRole(role);
		
		Users user = new Users();
		String root = "root";
		user.setActiveStat(true);
		user.setAddress("root");
		user.setContactNum(717768726);
		user.setDate(java.sql.Date.valueOf(LocalDate.now()));
		user.setFirstName(root);
		user.setLastName(root);
		user.setLoginStatus(false);
		user.setPassword("root@123");
		Set<Role> roles = new HashSet<Role>();
		roleDao.getRoles().forEach(e->{
			roles.add(e);
		});
		user.setRole(roles);
		user.setUserEmail("root@prosinc.com");
		user.setUserName(root);
		
		userDao.saveUser(user);
		
		PayModes pay = new PayModes();
		pay.setModeId("cash");
		pay.setModeDesc("cash");
		payModeDao.savePayMode(pay);
		
		pay = new PayModes();
		pay.setModeId("credit");
		pay.setModeDesc("credit");
		payModeDao.savePayMode(pay);
		
		pay = new PayModes();
		pay.setModeId("Cheque");
		pay.setModeDesc("Cheque");
		payModeDao.savePayMode(pay);
		
		pay = new PayModes();
		pay.setModeId("Voucher");
		pay.setModeDesc("Voucher");
		payModeDao.savePayMode(pay);
		
		pay = new PayModes();
		pay.setModeId("Sampath");
		pay.setModeDesc("Credit");
		pay.setMainPayMode(payModeDao.getPayModeById("credit"));
		payModeDao.savePayMode(pay);
		
		pay = new PayModes();
		pay.setModeId("Commercial");
		pay.setModeDesc("Credit");
		pay.setMainPayMode(payModeDao.getPayModeById("credit"));
		payModeDao.savePayMode(pay);
		
		pay = new PayModes();
		pay.setModeId("HNB");
		pay.setModeDesc("Credit");
		pay.setMainPayMode(payModeDao.getPayModeById("credit"));
		payModeDao.savePayMode(pay);
		
		
		 
		
		String loc = "/fxml/users/userGUI.fxml";
		FXMLLoader loader = new FXMLLoader(getClass().getResource(loc));
		Scene scene = new Scene(loader.load());

		UserController control = loader.getController();
		control.setThisMainLoader(loader);
		primaryStage.setScene(scene);
		
		
		

		primaryStage.show();

	}

}
