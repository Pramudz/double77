package invsys.controllers.reports.dailyreports;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import invsys.controllers.billingwindow.BillingController;
import invsys.controllers.database.HibernateUtil;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.controllers.formvalidation.ValidateInputs;
import invsys.controllers.mainpage.MainController;
import invsys.entities.POSPayDetails;
import invsys.entities.Sales;
import invsys.entities.SalesDetails;
import invsys.entities.Users;
import invsys.entitiydao.SalesDao;
import invsys.entitiydao.UserDao;
import invsys.entitiydao.impl.SalesDaoImpl;
import invsys.entitiydao.impl.UserDaoImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class DailyReportController {
	

    @FXML
    private JFXTextField grnOrPoNumField;
    
    @FXML
    private JFXDatePicker dateField;
    
    @FXML
    private JFXDatePicker toDateField;

    @FXML
    private JFXComboBox<String> userNameCombo;
    
    @FXML
    private JFXComboBox<String> refundModeCombo;
    
    @FXML
    private JFXButton printButton;

    private String mainReportName;

    @FXML
    void ClearFields() {

    }

    
    public void updateGrnPoFieldFromReport(String reportName) {
    	
    	ValidateInputs.getInstance();
    	if(reportName.equalsIgnoreCase("GRN Inquiry")) {
    		grnOrPoNumField.setPromptText("GRN Number");
    		dateField.setVisible(false);
    		userNameCombo.setVisible(false);
    		toDateField.setVisible(false);
    		refundModeCombo.setVisible(false);
    		printButton.setVisible(false);
    		
    	}
    	if(reportName.equalsIgnoreCase("PO Inquiry")) {
    		grnOrPoNumField.setPromptText("PO Number");
    		dateField.setVisible(false);
    		userNameCombo.setVisible(false);
    		toDateField.setVisible(false);
    		refundModeCombo.setVisible(false);
    		printButton.setVisible(false);
    	}
    	
    	if(reportName.equalsIgnoreCase("POS Bill Inquiry")) {
    		grnOrPoNumField.setPromptText("Invoice Id");
    		dateField.setVisible(true);
    		userNameCombo.setVisible(true);
    		toDateField.setVisible(false);
    		UserDao userDao = UserDaoImpl.getDao();
    		userNameCombo.getItems().setAll(userDao.getUserNames());
    		ValidateInputs.setDateFormatter(dateField);
    		refundModeCombo.setVisible(false);
    		printButton.setVisible(true);

    		
    	}
    	
    	if(reportName.equalsIgnoreCase("Credit Invoice Inquiry")) {
    		grnOrPoNumField.setPromptText("Invoice Id");
    		dateField.setVisible(true);
    		userNameCombo.setVisible(true);
    		toDateField.setVisible(false);
    		UserDao userDao = UserDaoImpl.getDao();
    		userNameCombo.getItems().setAll(userDao.getUserNames());
    		ValidateInputs.setDateFormatter(dateField);
    		refundModeCombo.setVisible(false);
    		printButton.setVisible(false);

    		
    	}
    	
    	
    	if(reportName.equalsIgnoreCase("Refund Inquiry")) {
    		refundModeCombo.getItems().clear();
    		refundModeCombo.getItems().addAll("POSS Refund", "CRDT Refund");
    		grnOrPoNumField.setPromptText("Refund Id");
    		dateField.setVisible(false);
    		userNameCombo.setVisible(false);
    		toDateField.setVisible(false);  		       	   
    	    refundModeCombo.setVisible(true);
    	    printButton.setVisible(false);
    	}
    	
    	if(reportName.equalsIgnoreCase("POS Sales Report") || reportName.equalsIgnoreCase("Price Revisions") || reportName.equalsIgnoreCase("Credit Invoice Summary")) {
    		grnOrPoNumField.setVisible(false);
    		dateField.setVisible(true);
    		userNameCombo.setVisible(false);
    		toDateField.setVisible(true);
    		dateField.setPromptText("From Date");
    		toDateField.setPromptText("To Date");
    		ValidateInputs.setDateFormatter(dateField);
    		ValidateInputs.setDateFormatter(toDateField);
    		refundModeCombo.setVisible(false);
    		printButton.setVisible(false);
    		
    	}
    
    	mainReportName=reportName;
    }
    
    @FXML
    void viewReportClicked() throws FileNotFoundException, SQLException, URISyntaxException, JRException {
  
    	
    
    	String reportPromptText = grnOrPoNumField.getPromptText();
    
    		
    		Map<String,Object>  parameters = new HashMap();
			parameters.put("companyName", MainController.getCompanyInfoSession().getCompanyName());
			parameters.put("streetAddress", MainController.getCompanyInfoSession().getAddressLine1());
			parameters.put("addressLine", MainController.getCompanyInfoSession().getAddressLine2());
			parameters.put("city", MainController.getCompanyInfoSession().getAddressLine3());
			parameters.put("telephoneNum", MainController.getCompanyInfoSession().getTelephoneNum());
			parameters.put("printedBy", MainController.getUserSession().getUserName());
			
			String reportLoc = "";
    	
			//for Grn Inquiry
    		if(mainReportName.contentEquals("GRN Inquiry")) {
    			if(ValidateInputs.validateNumbers(grnOrPoNumField, reportPromptText)) {
    				parameters.put("grnNo", Long.parseLong(grnOrPoNumField.getText()));
        			parameters.put("documentOriginalStatus", "Duplicate");
        			reportLoc = "resources/jasperreports/grnInquiry.jasper";
        			viewReport(reportLoc, parameters);
    			}
    			
    		}
    		
    		// for PO Inquiry
    		if(mainReportName.contentEquals("PO Inquiry")) {
    			if(ValidateInputs.validateNumbers(grnOrPoNumField, reportPromptText)) {
    				parameters.put("orderNo", Long.parseLong(grnOrPoNumField.getText()));
        			reportLoc = "resources/jasperreports/OrderReport_5.jasper";
        			viewReport(reportLoc, parameters);
    			}
    			
    		}
    		
    		//for Pos Sales Receipt re Print
    		if(mainReportName.contentEquals("POS Bill Inquiry")) {
    			if(ValidateInputs.validateNumbers(grnOrPoNumField, reportPromptText) && ValidateInputs.validateDateField(dateField, "Date Field ") && ValidateInputs.validateInputEmpty(userNameCombo, "User Name Field "))  {
    				
    			java.sql.Date date = java.sql.Date.valueOf(dateField.getValue());
    			String userName = userNameCombo.getValue();
    			long billNo = Long.parseLong(grnOrPoNumField.getText());
    			
    			parameters.put("billNo", billNo);
    			parameters.put("userName", userName);
    			parameters.put("date", date);
    			
    	
    			
    			//reportLoc = "resources/jasperreports/pos_bill_reprint.jasper";
    			//below is specifically for Double 7 Car Audio
    			
    			reportLoc = "resources/jasperreports/pos_bill_reprint-with-cus-for New.jasper";
    			
    			
    			viewReport(reportLoc, parameters);
    			}
    			
    			
    		}
    		
    		//for credit invoice inquiry
    		if(mainReportName.contentEquals("Credit Invoice Inquiry")) {
    			if(ValidateInputs.validateNumbers(grnOrPoNumField, reportPromptText) && ValidateInputs.validateDateField(dateField, "Date Field ") && ValidateInputs.validateInputEmpty(userNameCombo, "User Name Field "))  {
    				
    			java.sql.Date date = java.sql.Date.valueOf(dateField.getValue());
    			String userName = userNameCombo.getValue();
    			long invoiceId = Long.parseLong(grnOrPoNumField.getText());
    			parameters.put("documentOriginalStatus", "Duplicate");
    			parameters.put("invoiceId", invoiceId);
    			parameters.put("userName", userName);
    			parameters.put("date", date);
    			
    			reportLoc = "resources/jasperreports/creditinvoiceinquary.jasper";
    			viewReport(reportLoc, parameters);
    			
    			}
    			
    			
    		}
    		
    		//for pos sales summary
    		if(mainReportName.contentEquals("POS Sales Report")) {
    			
    			if(ValidateInputs.validateDateField(dateField, "From Date Field") && ValidateInputs.validateDateField(toDateField, "To Date Field")) {
    				java.sql.Date date = java.sql.Date.valueOf(dateField.getValue());
    				java.sql.Date toDate = java.sql.Date.valueOf(toDateField.getValue());
    				parameters.put("fromDate", date);
    				parameters.put("toDate", toDate);
    				reportLoc ="resources/jasperreports/posSalesSummary.jasper";
    				viewReport(reportLoc, parameters);
    			}
    			
    		}
    		
    		// for credit invoice summary
    		if(mainReportName.contentEquals("Credit Invoice Summary")) {
    			
    			if(ValidateInputs.validateDateField(dateField, "From Date Field") && ValidateInputs.validateDateField(toDateField, "To Date Field")) {
    				java.sql.Date date = java.sql.Date.valueOf(dateField.getValue());
    				java.sql.Date toDate = java.sql.Date.valueOf(toDateField.getValue());
    				parameters.put("fromDate", date);
    				parameters.put("toDate", toDate);
    				reportLoc ="resources/jasperreports/credit_invoice_summary.jasper";
    				viewReport(reportLoc, parameters);
    			}
    			
    		}
    		
    		//for price revision summary
    		if(mainReportName.contentEquals("Price Revisions")) {
    			
    			if(ValidateInputs.validateDateField(dateField, "From Date Field") && ValidateInputs.validateDateField(toDateField, "To Date Field")) {
    				java.sql.Date date = java.sql.Date.valueOf(dateField.getValue());
    				java.sql.Date toDate = java.sql.Date.valueOf(toDateField.getValue());
    				parameters.put("fromDate", date);
    				parameters.put("toDate", toDate);
    				
    				reportLoc ="resources/jasperreports/priceRevisionSummary.jasper";
    				viewReport(reportLoc, parameters);
    			}
    			
    		}
    		
    		//for refund inquiry
    		if(mainReportName.contentEquals("Refund Inquiry")) {
    			
    			if(ValidateInputs.validateInputEmptyComboWithoutEditorStringOnly(refundModeCombo, "Refund Mode Combo") && ValidateInputs.validateNumbers(grnOrPoNumField, reportPromptText)) {
    				parameters.put("refundId", Long.parseLong(grnOrPoNumField.getText()));
    				
    				if(refundModeCombo.getValue().contentEquals("POSS Refund")) {
    				reportLoc ="resources/jasperreports/posBillRefundInquiry.jasper";
    				}
    				
    				if(refundModeCombo.getValue().contentEquals("CRDT Refund")) {
    					reportLoc ="resources/jasperreports/CreditInvoiceRefundInquiry.jasper";
    				}
    				
    				
    				parameters.put("documentOriginalStatus", "Duplicate");
    				viewReport(reportLoc, parameters);
    			}
    			
    		}
    		
    		
    	
    	
    	
    }
    
    
    
    private void viewReport(String loc, Map<String, Object> parameters)
			throws SQLException, URISyntaxException, FileNotFoundException, JRException {
		Connection connectionForReports = HibernateUtil.getSessionFactory().getSessionFactoryOptions()
				.getServiceRegistry().getService(ConnectionProvider.class).getConnection();
		JasperPrint jp = JasperFillManager.fillReport(loc, parameters, connectionForReports);
		if(jp.getPages().isEmpty()) {
			AlertHandler.getAlert(AlertType.ERROR, "No Data Found", null);
		}
		else {
			JasperViewer.viewReport(jp, false);
		}

	}
    
    // added on 7th July for Pos bill re print
    @FXML
    void printButtonClicked() throws IOException {
    	
    	if(ValidateInputs.validateNumbers(grnOrPoNumField, grnOrPoNumField.getPromptText()) && ValidateInputs.validateDateField(dateField, "Date Field ") && ValidateInputs.validateInputEmpty(userNameCombo, "User Name Field "))  {
			
			java.sql.Date date = java.sql.Date.valueOf(dateField.getValue());
			String userName = userNameCombo.getValue();
			long billNo = Long.parseLong(grnOrPoNumField.getText());
			SalesDao salesDao = SalesDaoImpl.getDao();
			UserDao userDao = UserDaoImpl.getDao();
			Users user = userDao.getUserByName(userName);
			
			ObservableList<POSPayDetails> payDetails = salesDao.getPosPaymentforReprint(billNo, user, date);
			
			Sales  sales  = payDetails.get(0).getPosPayDetId().getSales();
			List<SalesDetails> list = salesDao.getSaleDetailsByBillNoDateUser(billNo, date, user);
			ObservableList<SalesDetails> salesDetailList = FXCollections.observableArrayList();
			salesDetailList.setAll(list);
			
			BillingController.printBillWithJavax(salesDetailList, sales, payDetails,"reprint");
			}
			
			
		}
    	
    }


