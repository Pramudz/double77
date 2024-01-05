package invsys.controllers.supplierpayments;

import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

import com.jfoenix.controls.JFXTextField;

import invsys.controllers.database.HibernateUtil;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.controllers.formvalidation.ValidateInputs;
import invsys.controllers.mainpage.MainController;
import invsys.controllers.purchaseorder.GetSupplierController;
import invsys.entities.GoodReceived;
import invsys.entities.PayModes;
import invsys.entities.Supplier;
import invsys.entities.SupplierPaymentDetail;
import invsys.entities.SupplierPayments;
import invsys.entities.SupplierReturn;
import invsys.entitiydao.PayModeDao;
import invsys.entitiydao.SupplierDao;
import invsys.entitiydao.SupplierPaymentDao;
import invsys.entitiydao.impl.PayModeDaoImpl;
import invsys.entitiydao.impl.SupplierDaoImpl;
import invsys.entitiydao.impl.SupplierPaymentDaoImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

public class SupplierPaymentController implements Initializable {
	
	
	@FXML
    private JFXTextField supplierCodeField;
	
	@FXML
	private Button addDataButtonToTable;

    @FXML
    private JFXTextField totReturnField;

    @FXML
    private JFXTextField totalPayableField;

    @FXML
    private Label supNameLabel;

    @FXML
    private AnchorPane anchor;

    @FXML
    private TextField grnRonNumField;

    @FXML
    private TextField documentDateField;

    @FXML
    private TextField grnRonAmountField;

    @FXML
    private ComboBox<String> docTypeCombo;

    @FXML
    private TableView<SupplierPaymentDetail> paymentTable;

    @FXML
    private TableColumn<SupplierPaymentDetail, Integer> docTypeColumn;

    @FXML
    private TableColumn<SupplierPaymentDetail, String> docNumColumn;

    @FXML
    private TableColumn<SupplierPaymentDetail, Double> amountCol;

    @FXML
    private TableColumn<SupplierPaymentDetail, java.sql.Date> docRaisedDateCol;
    
    @FXML
    private TextField totPaymentField;

    @FXML
    private TextField outstandingField;

    @FXML
    private TextField chequeNoField;

    @FXML
    private ComboBox<String> payModeCombo;
    
    //payment detail list to manipulate table view and database save method
    
    private ObservableList<SupplierPaymentDetail> supPaymentDetailList = FXCollections.observableArrayList();
    
   
    
    
 // this FXMLloader when loading this class inorder to pass data between
 	// scenes
 	private FXMLLoader thisSupplierPaymentLoader;
    
 	//// in order to prevent from opening more than one stage for supplier getting
	// table
 	private Stage supplierStage;
 	
 	
 // in order to keep one instance of GRN Ron table when hiding and closing
 	// the purchase order tableview
 	private Stage grnRonStage;
    
	// other global vairables to track , no of product count , sum of quantity, sum
	// of total
	
	private double totalPayment;
	private double totalOutstandig;
	
	
	//tempory supplier Instance
	private Supplier temporySupplier;
	
	
	//Dao handlers/Classes
	
	PayModeDao payModeDao = null;
	SupplierDao supplierDao = null;
	SupplierPaymentDao supplierPaymentDao = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ValidateInputs.getInstance();
		supplierDao =SupplierDaoImpl.getDao();
		supplierPaymentDao = SupplierPaymentDaoImpl.getDao();
		initColumn();
		loadPayModes();
		payModeCombo.setEditable(true);
		
		
		//setting doctype default GRN/RON string
		docTypeCombo.getItems().add("GRN");
		docTypeCombo.getItems().add("RON");
		
		//in order avoid mis entering GRN/RON type while inserting to table view supplierPaymentDetails
		//keep the consistency of data
		docTypeCombo.getSelectionModel().selectedItemProperty().addListener((v,oldVal,newVal) ->{
			grnRonNumField.clear();
			documentDateField.clear();
			grnRonAmountField.clear();
		});
		
		
	}
    
    
    
    
     
    

	
	public FXMLLoader getThisSupplierPaymentLoader() {
		return thisSupplierPaymentLoader;
	}






	public void setThisSupplierPaymentLoader(FXMLLoader thisSupplierPaymentLoader) {
		this.thisSupplierPaymentLoader = thisSupplierPaymentLoader;
	}




    //initialize table column of the Table View

	private  void initColumn() {
		docTypeColumn.setCellValueFactory(new PropertyValueFactory<>("documentType"));
		docNumColumn.setCellValueFactory(new PropertyValueFactory<>("grnRonId"));
		docRaisedDateCol.setCellValueFactory(new PropertyValueFactory<>("documentRaisedDate"));
		amountCol.setCellValueFactory(new PropertyValueFactory<>("documentAmount"));
		
		paymentTable.setRowFactory(e -> new TableRow<SupplierPaymentDetail>() {
			@Override
			protected void updateItem(SupplierPaymentDetail arg0, boolean arg1) {
				super.updateItem(arg0, arg1);
				if (arg0 == null) {
					setStyle("");
				} else if (arg0.getDocumentType().equals("RON")) {
					setStyle("-fx-background-color: brown");
				} else {
					setStyle("");
				}

			}
		});
		
	}
    
	
	//load pay type data to paymodCombo
		private void loadPayModes() {
			payModeDao = PayModeDaoImpl.getDao();
			payModeCombo.getItems().clear();
			payModeCombo.setItems(payModeDao.getAllPayModes());
			payModeCombo.setValue("cash");
			
	}
    
    
    
    

    @FXML
    void ClearAllFields(ActionEvent event) {
    	
    	//payModeCombo.getItems().clear();
    	paymentTable.getItems().clear();
    	supPaymentDetailList.clear();
    	totalPayment = 0;
    	totalOutstandig = 0;
    	totPaymentField.clear();
    	outstandingField.clear();
    	chequeNoField.clear();
    	supplierCodeField.clear();
    	totReturnField.clear();
    	totalPayableField.clear();
    	supNameLabel.setText("");
    	docTypeCombo.setValue("");
    	grnRonNumField.clear();
    	documentDateField.clear();
    	grnRonAmountField.clear();
    	chequeNoField.clear();
    	temporySupplier = null;
    	supplierStage = null;
    	grnRonStage = null;
    }

    
    //add payment details to table view
    @FXML
    void addPaymentMethod() {
    	try {
    		
        	long grnRonId = Long.parseLong(grnRonNumField.getText());
        	String docType = docTypeCombo.getValue();
        	double amount = Double.parseDouble(grnRonAmountField.getText());
        	java.sql.Date dateOfRaised = java.sql.Date.valueOf(documentDateField.getText());
        	String concatForEnterText =docType.concat(String.valueOf(grnRonId));
        	for(SupplierPaymentDetail det :supPaymentDetailList ) {
        	String concatListText = det.getDocumentType().concat(String.valueOf(det.getGrnRonId()));
        		if(concatListText.equals(concatForEnterText)) {
        			AlertHandler.getAlert(AlertType.WARNING, "Duplicate Entry : You Have already Added this PaymentData",
							"You Cannot Add Same Item for Payments");
					clearTextFields();
					grnRonNumField.requestFocus();
					return;
        		}
        		
        	}
        	SupplierPaymentDetail detail = new SupplierPaymentDetail();
        	detail.setDocumentType(docType);
        	detail.setGrnRonId(grnRonId);
        	detail.setDocumentRaisedDate(dateOfRaised);
        	detail.setDocumentAmount(amount);
        	
        	supPaymentDetailList.add(detail);
        	paymentTable.setItems(supPaymentDetailList);
        	clearTextFields();
        	grnRonNumField.requestFocus();
        	refreshSummaryData();
    	}catch(Exception e) {
    		e.printStackTrace();
    		AlertHandler.getAlert(AlertType.ERROR, "Cannot Add Item", "no data to Add");
    	}
    	

    }
    
    //add payment details to table view from GrnRonTableView
     public void addPaymentMethodFromGrnRonTableView(long grnRonId, String docType , double amount , java.sql.Date dateOfRaised) {
    	try {
    		
            String concatForEnterText =docType.concat(String.valueOf(grnRonId));
        	for(SupplierPaymentDetail det :supPaymentDetailList ) {
        	String concatListText = det.getDocumentType().concat(String.valueOf(det.getGrnRonId()));
        		if(concatListText.equals(concatForEnterText)) {
        			AlertHandler.getAlert(AlertType.WARNING, "Duplicate Entry : You Have already Added this PaymentData",
							"You Cannot Add Same Item for Payments");
					clearTextFields();
					grnRonNumField.requestFocus();
					return;
        		}
        		
        	}
        	SupplierPaymentDetail detail = new SupplierPaymentDetail();
        	detail.setDocumentType(docType);
        	detail.setGrnRonId(grnRonId);
        	detail.setDocumentRaisedDate(dateOfRaised);
        	detail.setDocumentAmount(amount);
        	
        	supPaymentDetailList.add(detail);
        	paymentTable.setItems(supPaymentDetailList);
        	clearTextFields();
        	grnRonNumField.requestFocus();
        	refreshSummaryData();
    	}catch(Exception e) {
    		e.printStackTrace();
    		AlertHandler.getAlert(AlertType.ERROR, "Cannot Add Item", "no data to Add");
    	}
    	

    }
    
    
    

    @FXML
    void clearTextFields() {
    	grnRonNumField.clear();
    	grnRonAmountField.clear();
    	documentDateField.clear();
    }

    @FXML
    void grnRonNumFieldEnter(ActionEvent event) {
    	if(this.temporySupplier == null) {
    		AlertHandler.getAlert(AlertType.ERROR, "Null Pointer Error", "Supplier Entity is Null Please Select Supplier First");
    		return;
    	}
    	if(ValidateInputs.validateInputEmptyComboWithoutEditorStringOnly(docTypeCombo , "Document Type Field") && ValidateInputs.validateNumbers(grnRonNumField, "GRN And RON Number Field")) {
    		String grnRonType = docTypeCombo.getValue();
    		long grnRonNumber = Long.parseLong(grnRonNumField.getText());
    		if(grnRonType.equals("GRN")) {
    			GoodReceived grnforPay = supplierPaymentDao.getSingleGrnObjectForPayment(this.temporySupplier, grnRonNumber);
    			if(grnforPay != null) {
    				documentDateField.setText(String.valueOf(grnforPay.getGrnDate()));
    				grnRonAmountField.setText(String.valueOf(grnforPay.getGrnAmount()));
    				addDataButtonToTable.requestFocus();   				
    			} else {
    				AlertHandler.getAlert(AlertType.ERROR, "Data Not Found", null);
    			}
    		}
    		
    		if(grnRonType.equals("RON")) {
    			SupplierReturn ronforPay = supplierPaymentDao.getSingleRonObjectForPayment(this.temporySupplier, grnRonNumber);
    			if(ronforPay != null) {
    				documentDateField.setText(String.valueOf(ronforPay.getReturnDate()));
    				grnRonAmountField.setText(String.valueOf(ronforPay.getReturnAmount()));
    				addDataButtonToTable.requestFocus();   				
    			} else {
    				AlertHandler.getAlert(AlertType.ERROR, "Data Not Found", null);
    			}
    		}
    	}
    }

    
    //load GRN Ron dataTable
    @FXML
    void loadGrnRonClicked(ActionEvent event) throws IOException {
    	
    	if(ValidateInputs.validateInputEmptyComboWithoutEditorStringOnly(docTypeCombo , "Document Type Field") && temporySupplier != null) {
    		
    		String loc = "/fxml/supplier/pendingPaymentsGrnRon.fxml";
			FXMLLoader loader = new FXMLLoader(getClass().getResource(loc));
			Scene scene = new Scene(loader.load());
			GetRonGrnTableController passDataToGetRonGRnTableCont = loader.getController();
			passDataToGetRonGRnTableCont.setParentFXMLLoader(this.thisSupplierPaymentLoader);
			passDataToGetRonGRnTableCont.loadPendingPaymentGrnRons(this.temporySupplier, docTypeCombo.getValue());
			grnRonStage = new Stage(StageStyle.DECORATED);
			grnRonStage.setScene(scene);

			grnRonStage.show();

			grnRonStage.setOnHidden(e -> {
				grnRonStage = null;
			});
    		
    	}
   

    }

    
    //setdata to thisScene from SupplierTable View 
    // setTemporySupplierEntity
    //set SupplierName for Lable
    //set Total Return
    //set Total Payable Amount
    public void setSupplierDetailsToMainScene(Supplier supplier) {
    	temporySupplier = supplier;
    	supNameLabel.setText(supplier.getCom_name());
    	totalPayableField.setText(String.valueOf(supplierPaymentDao.getSumOfGrnPayableToSupplier(supplier)));
    	totReturnField.setText(String.valueOf(supplierPaymentDao.getSumRonReturnableToSupplier(supplier)));
    	
    }
    //similar to above method setSupplierDetailsToMainScene but getting supplier details direcly from SupplierCod
    @FXML
    public void supCodFieldSetOnAction() {
    	try {
    		
    		if(supPaymentDetailList.isEmpty()) {
    			int supId = Integer.parseInt(supplierCodeField.getText());
        		Supplier supplier = supplierDao.getSupplierById(supId);
        		temporySupplier = supplier;
        		supNameLabel.setText(supplier.getCom_name());
        		totalPayableField.setText(String.valueOf(supplierPaymentDao.getSumOfGrnPayableToSupplier(supplier)));
            	totReturnField.setText(String.valueOf(supplierPaymentDao.getSumRonReturnableToSupplier(supplier)));
    		}
    		else {
    			AlertHandler.getAlert(AlertType.WARNING, "Supplier Details Cannot be loaded",
    					"Make Sure that Loaded Payment Details are clear Or Finalized");
    		}
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    		AlertHandler.getAlert(AlertType.ERROR, "Cannot Load Data", e.getCause().toString());
    	}
    
    	
    }
    //load supplier Window
    @FXML
    void loadSupplierButtonClicked() throws IOException {
    	if (supPaymentDetailList.isEmpty()) {
			if (supplierStage == null) {
				String loc = "/fxml/supplier/SupplierTable.fxml";
				FXMLLoader loader = new FXMLLoader(getClass().getResource(loc));
				Scene scene = new Scene(loader.load());
				GetSupplierController passLoadertoSupplierTable = loader.getController();
				passLoadertoSupplierTable.setLoader(thisSupplierPaymentLoader);

				supplierStage = new Stage(StageStyle.DECORATED);
				supplierStage.setScene(scene);

				supplierStage.show();
			} else if (supplierStage.isShowing()) {
				supplierStage.toFront();

			} else {
				supplierStage.show();
			}

		} else {
			AlertHandler.getAlert(AlertType.WARNING, "Supplier Details Cannot be loaded",
					"Make Sure that Loaded Payment Details are clear Or Finalized");
		}
		   	
    	
    	}

    

    @FXML
    void removeItemFromTableView(ActionEvent event) {
    	SupplierPaymentDetail selecteRowData = (SupplierPaymentDetail) paymentTable.getSelectionModel().getSelectedItem();
		if (!supPaymentDetailList.isEmpty() && !paymentTable.getSelectionModel().getSelectedItems().isEmpty()) {
			supPaymentDetailList.remove(selecteRowData);
			refreshSummaryData();
			paymentTable.setItems(supPaymentDetailList);
		} else {
			AlertHandler.getAlert(AlertType.WARNING, "Data Error",
					"No data to Delete");
		}
    }

    private void refreshSummaryData() {
    	
    	totalPayment = 0;
    	totalOutstandig = 0;
    	double totalToBePaid = Double.parseDouble(totalPayableField.getText())-Double.parseDouble(totReturnField.getText());
		
		
		for (SupplierPaymentDetail detail : supPaymentDetailList) {
			
			if(detail.getDocumentType().contentEquals("GRN")) {
				totalPayment += detail.getDocumentAmount();
			}
			
			if(detail.getDocumentType().contentEquals("RON")) {
				totalPayment -= detail.getDocumentAmount();
			}
			
			
		}
		totalOutstandig = totalToBePaid - totalPayment;
		totPaymentField.setText(String.valueOf(totalPayment));
		outstandingField.setText(String.valueOf(totalOutstandig));
			
	}






	@FXML
    void savePaymentClicked(ActionEvent event) {
		
		if(supPaymentDetailList.isEmpty()) {
			AlertHandler.getAlert(AlertType.ERROR, "Input Empty Error", "No Data to Save");
		}
		else {
			try {
				PayModes payMode = payModeDao.getPayModeById(payModeCombo.getValue());
				SupplierPayments supplierPayMass = new SupplierPayments();
				supplierPayMass.setAmountPaid(totalPayment);
				supplierPayMass.setDateOfPayment(java.sql.Date.valueOf(LocalDate.now()));
				supplierPayMass.setDocumentType("SPAYM");
				supplierPayMass.setPayMode(payMode);
				supplierPayMass.setSupplier(this.temporySupplier);
				supplierPayMass.setSupplierName(this.temporySupplier.getCom_name());
				supplierPayMass.setUser(MainController.getUserSession());
				
				for(SupplierPaymentDetail payDet : supPaymentDetailList) {
					payDet.setSupplierPayments(supplierPayMass);
				}
				
				if (AlertHandler.getAlert(AlertType.CONFIRMATION, "Are your sure you want create this Order", null)
						.getResult().getButtonData().equals(ButtonData.OK_DONE)) {
					if(supplierPaymentDao.savePayment(supplierPayMass, supPaymentDetailList)) {
						Notifications.create().title("Save Success").graphic(null).hideAfter(Duration.seconds(2))
						.darkStyle().position(Pos.CENTER).show();
						viewReport(supplierPayMass.getPaymentId());
						clearTextFields();
						ClearAllFields(event);
					}
				}
				
			}catch (Exception e){
				AlertHandler.getAlert(AlertType.ERROR, "Cannot Save Data", e.getCause().toString());
			}
			
		}

    }
	
	
	// view Supplier Payment Inquiry Report
			public void viewReport(long paymentId) throws JRException, IOException, SQLException {

							
					String loc= "resources/jasperreports/supplierPaymentInquiry.jasper";
					
					Connection connectionForReports = HibernateUtil.getSessionFactory().getSessionFactoryOptions()
							.getServiceRegistry().getService(ConnectionProvider.class).getConnection();

					Map<String, Object> parameters = new HashMap();
					parameters.put("companyName", MainController.getCompanyInfoSession().getCompanyName());
					parameters.put("streetAddress", MainController.getCompanyInfoSession().getAddressLine1());
					parameters.put("addressLine", MainController.getCompanyInfoSession().getAddressLine2());
					parameters.put("city", MainController.getCompanyInfoSession().getAddressLine3());
					parameters.put("telephoneNum", MainController.getCompanyInfoSession().getTelephoneNum());
					parameters.put("supPaymentId", paymentId);
					parameters.put("documentOriginalStatus", "Original");
					parameters.put("printedBy", MainController.getUserSession().getUserName());

					JasperPrint jp = JasperFillManager.fillReport(loc, parameters, connectionForReports);

					ByteArrayOutputStream output = new ByteArrayOutputStream();
					JasperExportManager.exportReportToPdfStream(jp, output);
					final String PDF_FILE = "resources/pdfreports/supPay.pdf";
					OutputStream pdfFile = new FileOutputStream(new File(PDF_FILE));
					pdfFile.write(output.toByteArray());
					pdfFile.flush();
					pdfFile.close();
					Desktop.getDesktop().open(new File(PDF_FILE));
				
			}

    @FXML
    void viewHistoryJasper(ActionEvent event) {

    }






	

}


