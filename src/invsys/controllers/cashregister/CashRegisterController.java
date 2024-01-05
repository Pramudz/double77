package invsys.controllers.cashregister;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import invsys.controllers.database.HibernateUtil;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.controllers.formvalidation.ValidateInputs;
import invsys.controllers.mainpage.MainController;
import invsys.entities.CashRegister;
import invsys.entities.Users;
import invsys.entities.compositkeys.CashRegisterId;
import invsys.entitiydao.CashRegisterDao;
import invsys.entitiydao.impl.CashRegisterDaoImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class CashRegisterController implements Initializable {

	
	 @FXML
	    private JFXTextField opnBalanceField;

	    @FXML
	    private JFXTextField userNameField;

	    @FXML
	    private JFXTextField totCashSaleField;

	    @FXML
	    private JFXTextField dateFiled;

	    @FXML
	    private JFXTextField totCreditSaleField;

	    @FXML
	    private JFXTextField totalSalesField;

	    @FXML
	    private JFXTextField closingCashField;

	    @FXML
	    private JFXButton actionButtion;

	    @FXML
	    private JFXButton syncButton;
	    
	    
	    //local variables for cashRegistry Closing
	    private double cashSales;
	    private double otherSales;
	    private double totalSales;
	
	    
	    // in order to pass data between scenes
	    @FXML
	    private FXMLLoader mainPageLoader;
	    
	    
	    //Cash Register Dao Interface
	    CashRegisterDao cashRegisDao = null;
	    
	    
	    public FXMLLoader getMainPageLoader() {
			return mainPageLoader;
		}


		public void setMainPageLoader(FXMLLoader mainPageLoader) {
			this.mainPageLoader = mainPageLoader;
		}


		@Override
		public void initialize(URL location, ResourceBundle resources) {
	    	
			cashRegisDao = CashRegisterDaoImpl.getDao();
	    	
	    	cashRegistryLoad();
	    	
	    	ValidateInputs.getInstance();
	    	
			
			
		}
	    

	    @FXML
	    void OpenCloseRegisterAction(ActionEvent event) throws IOException {
	    	if(actionButtion.getText().equals("Open Register")) {
	    		
	    		try {
	    			if(ValidateInputs.validatePrices(opnBalanceField,"Opening Balance Field")) {
	    				Users user = MainController.getUserSession();
		    			java.sql.Date date = java.sql.Date.valueOf(LocalDate.now());
		    			CashRegisterId id =  new CashRegisterId();
		    			
		    			id.setRegistryDate(date);
		    			id.setUser(user);
		    			
		    			CashRegister registerCash = new CashRegister();
		    			
		    			registerCash.setId(id);
		    			registerCash.setLoginDate(date);
		    			registerCash.setLoginTime(java.sql.Time.valueOf(LocalTime.now()));
		    			registerCash.setOpeningCashBalance(Double.parseDouble(opnBalanceField.getText()));
		    			registerCash.setSignInStatus(true);
		    			registerCash.setUserName(user.getUserName());
		    			
		    			CashRegister checkExistRegister = cashRegisDao.getCashRegister(id);
		    			
		    			if(checkExistRegister != null) {
		    			 AlertHandler.getAlert(AlertType.WARNING, "You Have Already Registered", "Please Use Previous Register");
		    			 checkExistRegister.setSignInStatus(true);
		    			 if(cashRegisDao.updateCashRegister(checkExistRegister).getId().equals(id)) {
		    				 cashRegistryLoad();
			    			 return;
		    			 }
		    			 
		    			
		    			}
		    			
		    			if(AlertHandler.getAlert(AlertType.CONFIRMATION, "Are Sure You Want to Update Cash Registry",
		    					"Opening Cash Balance :"+opnBalanceField.getText()).getResult().getButtonData().equals(ButtonData.OK_DONE)) {
		    				if(cashRegisDao.saveRegister(registerCash)) {
		    					AlertHandler.getAlert(AlertType.INFORMATION, "Succesfully Updated Cash Registry",
		    							"Opening Cash : "+opnBalanceField.getText()+ System.lineSeparator() + "User Name : "+user.getUserName()+ System.lineSeparator() +"Date : "+date);
		    							opnBalanceField.setDisable(true);
		    							actionButtion.setText("Close Registry");
		    				}
		    			}
	    			
	    			}
	    			
	    			
	    			
	    		}catch (Exception e){
	    			AlertHandler.getAlert(AlertType.ERROR, "Cannot Update Cash Registry", null);
	    			e.printStackTrace();
	    			
	    		}
	    		cashRegistryLoad();
	    		return;
	    		
	    	}
	    	
	    	if(actionButtion.getText().equals("Close Registry")) {
	    		
	    		
	    		if(ValidateInputs.validatePrices(closingCashField ,"Closing Cash Balance Field")) {
	    			
	    		
	    		Users user = MainController.getUserSession();
				java.sql.Date date = java.sql.Date.valueOf(LocalDate.now());
				CashRegisterId id =  new CashRegisterId();
				
				id.setRegistryDate(date);
				id.setUser(user);
				CashRegister register = cashRegisDao.getCashRegister(id);
				if(register != null) {
					
					List<Object[]> listofPayDetails = cashRegisDao.getPaymentDetailsForCashRegistryNative(user.getUser_id(), date);
					
					double cashSales = 0;
					double otherSales = 0;
					double totalSales = 0;
					for(Object[] x : listofPayDetails) {
						
						if(x[0].equals("cash")) {
							cashSales += (Double) x[1];
						}
						else {
							otherSales += (Double) x[1];
						}
						
						totalSales += (Double) x[1];
					}
					register.setTotalCashSales(cashSales);
					register.setTotalCreditCardSales(otherSales);
					register.setLogOutDate(date);
					register.setLogOutTime(java.sql.Time.valueOf(LocalTime.now()));
					register.setSignInStatus(false);
					double closingBalance = Double.parseDouble(closingCashField.getText());
					register.setClosingCashBalance(closingBalance);
					if(AlertHandler.getAlert(AlertType.CONFIRMATION, "Are Sure You Want to Clos the Cash Registry",
	    					"Opening Cash Balance :"+opnBalanceField.getText()+"Closing Balance :"+closingBalance).getResult().getButtonData().equals(ButtonData.OK_DONE)) {
						
					
						if(cashRegisDao.updateCashRegister(register).getId().equals(id)) {
							AlertHandler.getAlert(AlertType.INFORMATION, "Succecfully Closed the Cashier", null);
							opnBalanceField.clear();
							opnBalanceField.setEditable(true);
							actionButtion.setText("Open Register");
							totCashSaleField.clear();
							totCreditSaleField.clear();
							userNameField.setText(user.getUserName());
							dateFiled.setText(date.toString());
							closingCashField.clear();
							totCashSaleField.clear();
							try {
								viewReport(user.getUser_id(),date,cashSales);
							} catch (FileNotFoundException | SQLException | URISyntaxException | JRException e1) {
									e1.printStackTrace();
							}
							MainController controller = getMainPageLoader().getController();
							controller.loadDashBoard();
						
							
						}
						
						
					}
					
					
									
				}
				
	    		}
	    	}
	    		
	    }
	    
	    //load cash registry details
	    private void cashRegistryLoad() {
	    	Users user = MainController.getUserSession();
			java.sql.Date date = java.sql.Date.valueOf(LocalDate.now());
			CashRegisterId id =  new CashRegisterId();
			
			id.setRegistryDate(date);
			id.setUser(user);
			CashRegister register = cashRegisDao.getCashRegister(id);
			if(register != null && register.isSignInStatus()) {
				
				List<Object[]> listofPayDetails = cashRegisDao.getPaymentDetailsForCashRegistryNative(user.getUser_id(), date);
				
				double cashSales = 0;
				double otherSales = 0;
				double totalSales = 0;
				for(Object[] x : listofPayDetails) {
					
					if(x[0].equals("cash")) {
						cashSales += (Double) x[1];
					}
					else {
						otherSales += (Double) x[1];
					}
					
					totalSales += (Double) x[1];
				}
			
				
				opnBalanceField.setText(String.format("%.2f",register.getOpeningCashBalance()));
				closingCashField.setEditable(true);
				opnBalanceField.setEditable(false);
				actionButtion.setText("Close Registry");
				totCashSaleField.setText(String.format("%.2f",cashSales));
				totCreditSaleField.setText(String.format("%.2f",otherSales));
				userNameField.setText(user.getUserName());
				dateFiled.setText(date.toString());
				totalSalesField.setText(String.format("%.2f",totalSales));
								
			}
			
	    }
	    
	    
	    //load cash registry details
	    public void cashRegistryLoadForSignOff(Users user, java.sql.Date date) {
	    	
			CashRegisterId id =  new CashRegisterId();
			
			id.setRegistryDate(date);
			id.setUser(user);
			CashRegister register = cashRegisDao.getCashRegister(id);
			if(register != null && register.isSignInStatus()) {
				
				List<Object[]> listofPayDetails = cashRegisDao.getPaymentDetailsForCashRegistryNative(user.getUser_id(), date);
				
				cashSales = 0;
				otherSales = 0;
				totalSales = 0;
				for(Object[] x : listofPayDetails) {
					
					if(x[0].equals("cash")) {
						cashSales += (Double) x[1];
					}
					else {
						otherSales += (Double) x[1];
					}
					
					totalSales += (Double) x[1];
				}
			
				
				opnBalanceField.setText(String.format("%.2f",register.getOpeningCashBalance()));
				opnBalanceField.setEditable(false);
				closingCashField.setEditable(true);
				actionButtion.setText("Close Registry");
				totCashSaleField.setText(String.format("%.2f",cashSales));
				totCreditSaleField.setText(String.format("%.2f",otherSales));
				userNameField.setText(user.getUserName());
				dateFiled.setText(date.toString());
				totalSalesField.setText(String.format("%.2f",totalSales));
				
				actionButtion.setOnAction(e->{
					
					register.setTotalCashSales(cashSales);
					register.setTotalCreditCardSales(otherSales);
					register.setLogOutDate(java.sql.Date.valueOf(LocalDate.now()));
					register.setLogOutTime(java.sql.Time.valueOf(LocalTime.now()));
					register.setSignInStatus(false);
					double closingBalance = Double.parseDouble(closingCashField.getText());
					register.setClosingCashBalance(closingBalance);
					if(AlertHandler.getAlert(AlertType.CONFIRMATION, "Are Sure You Want to Close the Cash Registry",
	    					"Opening Cash Balance :"+opnBalanceField.getText()+"Closing Balance :"+closingBalance).getResult().getButtonData().equals(ButtonData.OK_DONE)) {
						
					
						if(cashRegisDao.updateCashRegister(register).getId().equals(id)) {
							AlertHandler.getAlert(AlertType.INFORMATION, "Succesfully Closed the Register", null);
							opnBalanceField.clear();
							opnBalanceField.setEditable(true);
							actionButtion.setText("Open Register");
							totCashSaleField.clear();
							totCreditSaleField.clear();
							userNameField.setText(user.getUserName());
							dateFiled.setText(date.toString());
							closingCashField.clear();
							totCashSaleField.clear();					
							opnBalanceField.getScene().getWindow().hide();	
							//view Report
							try {
								viewReport(user.getUser_id(),date,cashSales);
							} catch (FileNotFoundException | SQLException | URISyntaxException | JRException e1) {
									e1.printStackTrace();
							}
							
						}
						
						
					}
				
				});
								
			}
			
	    }

	    @FXML
	    void loadData(ActionEvent event) {

	    }
	    
	    public void updateUserAndDate(String userName, String date) {
	    	userNameField.setText(userName);
	    	dateFiled.setText(date);
	    }

	    
	    //view jasper report
	    public void viewReport(long uid, java.sql.Date regDate, double totCashSales) throws SQLException, URISyntaxException, FileNotFoundException, JRException {
	    	Connection connectionForReports = HibernateUtil.getSessionFactory().getSessionFactoryOptions().getServiceRegistry().getService(ConnectionProvider.class).getConnection();
	    	String loc = "resources/jasperreports/cashRegistrySignOff.jasper";
	    	

	    	
			Map<String,Object>  parameters = new HashMap();
			parameters.put("userId", uid);
			parameters.put("totalCashSales", totCashSales);
			parameters.put("registryDate", regDate);
			parameters.put("companyName", MainController.getCompanyInfoSession().getCompanyName());
			
			JasperPrint jp = JasperFillManager.fillReport(loc,parameters , connectionForReports);
		
			
			if(jp.getPages().isEmpty()) {
				AlertHandler.getAlert(AlertType.INFORMATION, "Closed Register With No Sales", "Please hand over your Opening Balance to Chief Cashier");
			}
			
			else {
				JasperViewer.viewReport(jp, false);
			}
			
	    }
		
}
