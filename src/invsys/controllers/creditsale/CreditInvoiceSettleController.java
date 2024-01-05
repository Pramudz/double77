package invsys.controllers.creditsale;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import invsys.controllers.formvalidation.AlertHandler;
import invsys.controllers.formvalidation.ValidateInputs;
import invsys.controllers.mainpage.MainController;
import invsys.controllers.usermaintain.UserTableController;
import invsys.entities.CreditInvoice;
import invsys.entities.CreditInvoiceDetail;
import invsys.entities.Users;
import invsys.entitiydao.CreditInvoiceDao;
import invsys.entitiydao.UserDao;
import invsys.entitiydao.impl.CreditInvoiceDaoImpl;
import invsys.entitiydao.impl.UserDaoImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class CreditInvoiceSettleController implements Initializable{
	

    @FXML
    private JFXTextField invoiceNumField;

    @FXML
    private JFXTextField userNameFiled;

    @FXML
    private JFXDatePicker invoiceDateField;

    @FXML
    private Label custNamefield;

    @FXML
    private Label custIdField;

    @FXML
    private Label comNameField;

    @FXML
    private TableView<CreditInvoiceDetail> crdSettleTableView;

    @FXML
    private TableColumn<CreditInvoiceDetail, String> snoCol;

    @FXML
    private TableColumn<CreditInvoiceDetail, String> itemCodeCol;

    @FXML
    private TableColumn<CreditInvoiceDetail, String> descCol;

    @FXML
    private TableColumn<CreditInvoiceDetail, Double> qtyCol;

    @FXML
    private TableColumn<CreditInvoiceDetail, Double> priceCol;

    @FXML
    private TableColumn<CreditInvoiceDetail, Double> discountPercentageCol;

    @FXML
    private TableColumn<CreditInvoiceDetail, Double> discountCol;

    @FXML
    private TableColumn<CreditInvoiceDetail, Double> grossAmountCol;

    @FXML
    private TableColumn<CreditInvoiceDetail, Double> netAmountCol;
    
    
	
    @FXML
    private TextField totalItemsField;

   
    @FXML
    private TextField totalDiscountField;

    @FXML
    private TextField totalGrossAmountField;

    @FXML
    private TextField totalNetAmountField;

    @FXML
    private TextField totalTobePaidField;

    @FXML
    private TextField expiredOnField;
    
    @FXML
    private TextField advancePaymField;
    
    
    
    
    //Hold Instance vairables for credit Invoice until save the settlement
    CreditInvoice creditInvoice;
    
    
    //Dao Objects
    CreditInvoiceDao creditInvoiceDao = null;
    UserDao userDao =null;
    
    //This Fxml Loader Class
    FXMLLoader thisLoader = null;
    
 // User Stage table Instance tracking for opening and closing stage
 	private Stage userLoadTableStage;

 	ObservableList<CreditInvoiceDetail> creditInvoiceDetList = FXCollections.observableArrayList();
 	
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	ValidateInputs.getInstance();
    	creditInvoiceDao = CreditInvoiceDaoImpl.getDao();
    	userDao = UserDaoImpl.getDao();
    	ValidateInputs.setDateFormatter(invoiceDateField);
		initColumns();
		
	}
    
    private void initColumns() {
    //	snoCol.setCellFactory(new PropertyValueFactory("seqNo"));
    	
    	snoCol.setCellValueFactory((TableColumn.CellDataFeatures<CreditInvoiceDetail,String> cdata) -> new
    			SimpleStringProperty(String.valueOf(cdata.getValue().getCreditInvoiceId().getSeqNo())));
    	
    	itemCodeCol.setCellValueFactory((TableColumn.CellDataFeatures<CreditInvoiceDetail,String> cdata) -> new
    			SimpleStringProperty(String.valueOf(cdata.getValue().getProduct().getPrd_id())));
    	
    	descCol.setCellValueFactory((TableColumn.CellDataFeatures<CreditInvoiceDetail,String> cdata) -> new
    			SimpleStringProperty(String.valueOf(cdata.getValue().getProduct().getP_name())));
    	
    	qtyCol.setCellValueFactory(new PropertyValueFactory("salesQty"));
    	priceCol.setCellValueFactory(new PropertyValueFactory("unitPrice"));
    	discountPercentageCol.setCellValueFactory(new PropertyValueFactory("discountPercentage"));
    	discountCol.setCellValueFactory(new PropertyValueFactory("discount"));
    	grossAmountCol.setCellValueFactory(new PropertyValueFactory("itemGrossAmount"));
    	netAmountCol.setCellValueFactory(new PropertyValueFactory("itemNetAmount"));
    	  	
    	
    }

 // get loader details from MainController class in order pass data between
 	// scenes
 	public FXMLLoader getThiLoader() {
 		return thisLoader;
 	}

 	// set loader details from MainController class in order pass data between
 	// scenes
 	public void setThiLoader(FXMLLoader thiLoader) {
 		this.thisLoader = thiLoader;
 	}
    

    @FXML
    void ClearAllFields() {
		creditInvoice = null;
		invoiceNumField.clear();
		invoiceDateField.getEditor().setText("");
		invoiceDateField.setValue(null);
		totalGrossAmountField.clear();
		comNameField.setText("");
		custNamefield.setText("");
		userNameFiled.clear();
		custIdField.setText("");
		totalNetAmountField.clear();
		totalItemsField.clear();
		totalDiscountField.clear();
		creditInvoiceDetList.clear();
		crdSettleTableView.getItems().clear();
		expiredOnField.clear();
		totalTobePaidField.clear();
		advancePaymField.clear();
		

    }

    @FXML
    void loadCreditInvoiceDetails() {
    	
    	if(!creditInvoiceDetList.isEmpty()) {
    		AlertHandler.getAlert(AlertType.ERROR, "Credit Invoice Already Loaded" ,"A Instance of a Credit Invoice has been already loaded");
    		return;
    	}
    	
    	crdSettleTableView.getItems().clear();
    	
    	if(ValidateInputs.validateNumbers(invoiceNumField, "Invoice Number Field") && ValidateInputs.validateDateField(invoiceDateField, "Invoice Date Field") &&
    			ValidateInputs.validateUserNames(userNameFiled, "User Name Field")) {
    		
    		String userText = userNameFiled.getText();
			Users user  = userDao.getUserByName(userText);
			java.sql.Date date = java.sql.Date.valueOf(invoiceDateField.getValue());
			long billNo = Long.parseLong(invoiceNumField.getText());
			creditInvoiceDetList.addAll(creditInvoiceDao.getInvoiceDetailsByBillNoDateUser(billNo, date, user));
			
			
			if(creditInvoiceDetList.isEmpty()) {
				AlertHandler.getAlert(AlertType.ERROR, "No Data Found", null);
				return;
			}
			
			
			if(creditInvoice == null)
				creditInvoice = creditInvoiceDetList.get(0).getCreditInvoiceId().getCreditInvoice();		
			
			if(creditInvoice.isRefundStatus() || creditInvoice.isSettledStatus()) {
				AlertHandler.getAlert(AlertType.ERROR, "Error Loading Data", "This Invoice has already been settled or refuneded");
				creditInvoice = null;
				userNameFiled.clear();
				invoiceDateField.getEditor().clear();
				invoiceDateField.setValue(null);
				invoiceNumField.clear();
				creditInvoiceDetList.clear();
			}
			else {
				totalItemsField.setText(String.valueOf(creditInvoiceDetList.size()));
				totalDiscountField.setText(String.format("%.2f",creditInvoice.getInvoiceDiscount()));
				totalGrossAmountField.setText(String.format("%.2f",creditInvoice.getInvoiceGrossAmount()));
				totalNetAmountField.setText(String.format("%.2f", creditInvoice.getInvoiceNetAmount()));
				totalTobePaidField.setText(String.format("%.2f", creditInvoice.getInvoiceNetAmount()-creditInvoice.getIfAdvancePayment()));
				expiredOnField.setText(String.valueOf(creditInvoice.getExpiredDate()));
				advancePaymField.setText(String.format("%.2f", creditInvoice.getIfAdvancePayment()));
				custNamefield.setText(creditInvoice.getCustomerName());
				custIdField.setText(String.valueOf(creditInvoice.getCustomer().getCustomerId()));
				crdSettleTableView.getItems().addAll(creditInvoiceDetList);
			}
			
    	}

    }

    @FXML
    void loadUserButtonClicked() throws IOException {
    	if (userLoadTableStage == null) {
			String loc = "/fxml/users/userTable.fxml";
			FXMLLoader loader = new FXMLLoader(getClass().getResource(loc));

			Scene scene = new Scene(loader.load());
			UserTableController getControl = loader.getController();
			userLoadTableStage = new Stage(StageStyle.DECORATED);
			userLoadTableStage.initModality(Modality.APPLICATION_MODAL);
			userLoadTableStage.setScene(scene);
			userLoadTableStage.show();
			getControl.setUserMainStageloader(thisLoader);
			getControl.setUserTableStage(userLoadTableStage);
			
		} else if (userLoadTableStage.isShowing()) {
			userLoadTableStage.toFront();

		} else {
			userLoadTableStage.show();
		}

    }
    
    public  void setUserNameFiledFormUsertable(String userName) {
    	userNameFiled.setText(userName);
    }

    @FXML
    void settleButtonAction() {
    	
    	if(!creditInvoiceDetList.isEmpty()) {
    		Users user = MainController.getUserSession();
    		
    		creditInvoice.setSettledStatus(true);
    		creditInvoice.setSettledAmount(creditInvoice.getInvoiceNetAmount());
    		creditInvoice.setSettledBy(user);
    		creditInvoice.setSettledDate(java.sql.Date.valueOf(LocalDate.now()));
    		
    		if(AlertHandler.getAlert(AlertType.CONFIRMATION, "Are Your Sure You want to Settle This", null)
    				.getResult().getButtonData().equals(ButtonData.OK_DONE)) {
    			if(creditInvoiceDao.settleCreditInvoice(creditInvoice)) {
    				Notifications.create().title("Update Success").graphic(null).hideAfter(Duration.seconds(2))
					.darkStyle().position(Pos.CENTER).show();
    				ClearAllFields();
    			}
    		}
    		
    	}
    	

    }



}


