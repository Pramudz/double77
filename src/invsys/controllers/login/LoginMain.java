package invsys.controllers.login;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import invsys.controllers.database.HibernateUtil;
import invsys.controllers.formvalidation.AlertHandler;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginMain extends Application {

	public static void main(String args[]) {

		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
	
		
	HibernateUtil.getSessionFactory();
		
	UserDao userDao = UserDaoImpl.getDao();
	
	if(userDao.getLastIndex() <=0) {
		AlertHandler.getAlert(AlertType.INFORMATION, "Administration User Will Be Created"
				, ". User Name : admin  , Password : admin Make sure to set Strong Password After login");
		
		PayModeDao payModeDao = PayModeDaoImpl.getDao();
		RoleDao roleDao = RoleDaoImpl.getDao();
		
		 
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
			/*
			 * f1 = new RoleFunctions(); f1.setRoleFunction("Inventory Count");
			 * f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Monthly Operations"));
			 * f1.setRoleAcess(true); roleDao.createRoleFeatures(f1); func.add(f1);
			 */
		
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("Expenses");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Monthly Operations"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("Settle Credit Invoice");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Monthly Operations"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		
		// Sales Reports
		f1 = new RoleFunctions();
		f1.setRoleFunction("Sales Reports");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Reports"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("Category Wise");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Sales Reports"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
			
		
		// End Sales Reports

		// Month End Reports
		f1 = new RoleFunctions();
		f1.setRoleFunction("Month End Reports");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Reports"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("Profit & Loss Report");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Month End Reports"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("Expenses Report");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Month End Reports"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		//End Month end Reports
		
		
		// Daily Reports
		f1 = new RoleFunctions();
		f1.setRoleFunction("Daily Reports");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Reports"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("POS Sales Report");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Daily Reports"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("POS Bill Inquiry");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Daily Reports"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("GRN Inquiry");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Daily Reports"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("PO Inquiry");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Daily Reports"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("Refund Inquiry");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Daily Reports"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
					
		f1 = new RoleFunctions();
		f1.setRoleFunction("Price Revisions");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Daily Reports"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("Credit Invoice Inquiry");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Daily Reports"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("Credit Invoice Summary");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Daily Reports"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		// End Daily Reports 
		
		
		// general Reprots
		f1 = new RoleFunctions();
		f1.setRoleFunction("General Reports");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Reports"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
			
		f1 = new RoleFunctions();
		f1.setRoleFunction("Supplier Detail Report");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("General Reports"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("Customer Detail Report");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("General Reports"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		// End General Reports
		
		//Stock Reports
		
		
		//Supplier Related Reports
		f1 = new RoleFunctions();
		f1.setRoleFunction("Supplier Related");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Reports"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("Supplier Payment History");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Supplier Related"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("Supplier Return History");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Supplier Related"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("Supplier Return Inquiry");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Supplier Related"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("Supplier Payment Inquiry");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Supplier Related"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("Supplier Wise Sales");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Supplier Related"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		
		//End Supplier Related Reports
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("Stock Reports");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Reports"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("Category Wise Stock Reports");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Stock Reports"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("Out of Stock Reports");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Stock Reports"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("GRN Summary Reports");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Stock Reports"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("PO Summary Reports");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Stock Reports"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("Item Wise Stock Report");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Stock Reports"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		f1 = new RoleFunctions();
		f1.setRoleFunction("Bincard Summary");
		f1.setMainRoleFunction(roleDao.getRoleFeatureByName("Stock Reports"));
		f1.setRoleAcess(true);
		roleDao.createRoleFeatures(f1);
		func.add(f1);
		
		
		//End Stock Reports
		
		
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
		f1.setRoleFunction("System Functions");
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
		String root = "admin";
		user.setActiveStat(true);
		user.setAddress("");
		user.setContactNum(717768726);
		user.setDate(java.sql.Date.valueOf(LocalDate.now()));
		user.setFirstName("");
		user.setLastName("");
		user.setLoginStatus(false);
		user.setPassword("admin");
		Set<Role> roles = new HashSet<Role>();
		roleDao.getRoles().forEach(e->{
			roles.add(e);
		});
		user.setRole(roles);
		user.setUserEmail("admin@admin.com");
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
		
	} 
		 
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
		Scene scene = new Scene(loader.load());
		primaryStage.setScene(scene);
		LoginController controlLogin = loader.getController();
		controlLogin.setStage(primaryStage);
		primaryStage.getIcons().add(new Image("/images/Prosinc.jpg"));
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.show();
	}

}
