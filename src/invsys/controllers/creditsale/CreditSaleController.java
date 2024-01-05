package invsys.controllers.creditsale;

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

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import invsys.controllers.customer.CustomerTableController;
import invsys.controllers.database.HibernateUtil;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.controllers.formvalidation.ValidateInputs;
import invsys.controllers.mainpage.MainController;
import invsys.entities.CreditInvoice;
import invsys.entities.CreditInvoiceDetail;
import invsys.entities.Customer;
import invsys.entities.Products;
import invsys.entities.Users;
import invsys.entities.compositkeys.CreditInvoiceDetailId;
import invsys.entities.compositkeys.CreditInvoiceId;
import invsys.entitiydao.CreditInvoiceDao;
import invsys.entitiydao.CustomerDao;
import invsys.entitiydao.ProductDao;
import invsys.entitiydao.impl.CreditInvoiceDaoImpl;
import invsys.entitiydao.impl.CustomerDaoImpl;
import invsys.entitiydao.impl.ProductDaoImpl;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.converter.DoubleStringConverter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

public class CreditSaleController implements Initializable {

	@FXML
	private JFXTextField customerAutoIdField;

	@FXML
	private JFXTextField nicNumbField;
	
	@FXML
	private JFXTextField creditPeriodField;

	@FXML
	private JFXTextField expireDateField;


	@FXML
	private JFXTextField outstandingField;

	@FXML
	private Label custNamefield;

	@FXML
	private Label custAddressField;

	@FXML
	private Label comNameField;

	@FXML
	private AnchorPane anchor;

	@FXML
	private JFXComboBox<String> itemDescCombo;

	@FXML
	private TextField itemCodeField;

	@FXML
	private TextField unitPriceField;

	@FXML
	private TextField qtyField;

	@FXML
	private TextField discountField;

	@FXML
	private TextField onHandQtyField;

	@FXML
	private TableView<CreditInvoiceDetail> creditSaleTable;

	@FXML
	private TableColumn<CreditInvoiceDetail, Integer> snoCol;

	@FXML
	private TableColumn<CreditInvoiceDetail, String> itemCodeCol;

	@FXML
	private TableColumn<CreditInvoiceDetail, String> descCol;

	@FXML
	private TableColumn<CreditInvoiceDetail, Double> qtyCol;

	@FXML
	private TableColumn<CreditInvoiceDetail, String> discountCol;

	@FXML
	private TableColumn<CreditInvoiceDetail, Double> priceCol;

	@FXML
	private TableColumn<CreditInvoiceDetail, String> grossAmountCol;

	@FXML
	private TableColumn<CreditInvoiceDetail, String> netAmountCol;
	
	@FXML
	private TableColumn<CreditInvoiceDetail, Double> discountPercentageCol;

	@FXML
	private TextField totalItemsField;

	@FXML
	private TextField totalQuantityField;

	@FXML
	private TextField totalDiscountField;

	@FXML
	private TextField totalNetAmountField;

	@FXML
	private TextField advancePaymentField;

	@FXML
	private TextField totalDueAmountField;
	
	@FXML
	private Button saveButton;

	// tempory product variable to keep track the setCombo box on clicked function
	// pressed
	//
	private Products temporyProduct;
	
	//hold customer instance through out the saving method
	//and assing customer when select customer
	private Customer customerInstance;

	// observable list for Product details for table view
	private ObservableList<CreditInvoiceDetail> creditIncoiveDetailList = FXCollections.observableArrayList();

	// in order to pass data between this loader to payment
	// this loader
	private FXMLLoader thiLoader;
	
	// customerload table related variables
		private Stage customerTableLoadStage;

	// other global vairables to track , no of product count , sum of quantity, sum
	// of total

	private double totalNetAmount;
	private double billItemCount;
	private double billItemQty;
	private double billDicount;
	private int seqNo;
	private double grossBillAmount;
	private long lastBillNo;
	
	
	//dao classes 
	CustomerDao customerDao = null;
	CreditInvoiceDao creditInvoiceDao = null;
	ProductDao productDao = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		customerDao = CustomerDaoImpl.getDao();
		creditInvoiceDao=CreditInvoiceDaoImpl.getDao();
		initTableViewColumns();
		productDao = ProductDaoImpl.getDao();
		creditPeriodAndAdvanceAmountAction();
		ValidateInputs.getInstance();

		onPrdDescComboAction();

		//getProductFromItemCodeField();

		qtyField.setOnKeyReleased(e -> {
			if (e.getCode().equals(KeyCode.ESCAPE)) {
				clearTextFields();
			}
		});
		
		updateLastBillNo();

	}

	
	
	//get loader details from MainController class in order pass data between
	// scenes
	public FXMLLoader getThiLoader() {
		return thiLoader;
	}



	//set loader details from MainController class in order pass data between
		// scenes
	public void setThiLoader(FXMLLoader thiLoader) {
		this.thiLoader = thiLoader;
		
	}

	//set customer id to customeridField from customer table view GUI
	public void setCustomeIdToCustomerIdField(long cusId) {
		customerAutoIdField.setText(String.valueOf(cusId));
	}
	
	//set customer instance
	public void updateCustomerInstance() {
		
		try {
			CustomerDao customerDao = CustomerDaoImpl.getDao();
			long cusId = Long.parseLong(customerAutoIdField.getText());
			this.customerInstance = customerDao.getCustomerById(cusId);
			
			nicNumbField.setText(customerInstance.getNicNumber());
			custNamefield.setText(customerInstance.getFirstName()+" "+customerInstance.getLastName());
			custAddressField.setText(customerInstance.getStreetAddress()+","+customerInstance.getAddressLine02()+","+ customerInstance.getCity());
			comNameField.setText(customerInstance.getCompanyName());
			itemCodeField.requestFocus();
			
			
		}catch(Exception e){
			AlertHandler.getAlert(AlertType.ERROR, "Error Loading Customer", e.getLocalizedMessage());
			e.printStackTrace();
		}
	}



	private void initTableViewColumns() {
		snoCol.setCellValueFactory(new PropertyValueFactory<>("seqNo"));
		itemCodeCol.setCellValueFactory(
				(TableColumn.CellDataFeatures<CreditInvoiceDetail, String> cdata) -> new SimpleStringProperty(
						String.valueOf(cdata.getValue().getProduct().getPrd_id())));
		descCol.setCellValueFactory(
				(TableColumn.CellDataFeatures<CreditInvoiceDetail, String> data) -> new SimpleStringProperty(
						data.getValue().getProduct().getP_name()));
		priceCol.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
		qtyCol.setCellValueFactory(new PropertyValueFactory<>("salesQty"));
		discountCol.setCellValueFactory(cellData -> Bindings.format("%.2f", cellData.getValue().getDiscount()));
		netAmountCol.setCellValueFactory(cellData -> Bindings.format("%.2f", cellData.getValue().getItemNetAmount()));
		grossAmountCol.setCellValueFactory(cellData -> Bindings.format("%.2f", cellData.getValue().getItemGrossAmount()));
		discountPercentageCol.setCellValueFactory(new PropertyValueFactory<>("discountPercentage"));
		
		creditSaleTable.setEditable(true);
		qtyCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
	}

	// get product entity from product description gotten from combo and populate
	// other
	// data to the other text box such as price,etc...
	private void onPrdDescComboAction() {

		itemDescCombo.getEditor().setOnKeyReleased(e -> {

			if (e.getCode() == KeyCode.ENTER) {

				if (itemDescCombo.getEditor().getText().isEmpty()) {
					return;
				} else {
					try {
						String prd = itemDescCombo.getEditor().getText();

						temporyProduct = productDao.getPrductByName(prd);
						
						itemCodeField.setText(String.valueOf(temporyProduct.getPrd_id()));
						unitPriceField.setText(String.valueOf(temporyProduct.getUnit_price()));
						discountField.setText(String.valueOf(0));
						onHandQtyField.setText(String.valueOf(temporyProduct.getOnHandQty()));
						qtyField.requestFocus();
						itemDescCombo.getSelectionModel().clearSelection();
						// in order to mapp searching operation in combo box
						itemDescCombo.getEditor().setText(prd);

					} catch (Exception f) {
						AlertHandler.getAlert(AlertType.ERROR, "Cannot Find or Load Item Invalid",
								f.getLocalizedMessage());
						f.printStackTrace();
					}
				}

			}

			if (e.getCode().isLetterKey()) {

				String text = itemDescCombo.getEditor().getText();
				ObservableList<String> prdDescriptionList = productDao.getPrductsLikeProductName(text);
				for (String x : prdDescriptionList) {
					System.out.println(x);
				}
				itemDescCombo.setItems(prdDescriptionList);

				if (!itemDescCombo.getItems().isEmpty())
					itemDescCombo.show();

			}
			if (e.getCode().equals(KeyCode.BACK_SPACE) || e.getCode().equals(KeyCode.DELETE)) {
				itemDescCombo.getSelectionModel().clearSelection();
			}

			if (itemDescCombo.getItems().size() == 1) {
				if (e.getCode().equals(KeyCode.DOWN))
					itemDescCombo.getSelectionModel().selectFirst();
			}

		});

	}

	// code field on action
	@FXML
	void getProductFromItemCodeField() {
			
				try {
					long codeText = Long.valueOf(itemCodeField.getText());
					temporyProduct = productDao.getProductById(codeText);
					
					if(!temporyProduct.isStatus()) {
						AlertHandler.getAlert(AlertType.ERROR, "This Product is Blocked", "Please Activate to Use");
						itemCodeField.requestFocus();
						return;
					}
					itemDescCombo.getEditor().setText(temporyProduct.getP_name());
					unitPriceField.setText(String.valueOf(temporyProduct.getUnit_price()));
					discountField.setText(String.valueOf(0));
					onHandQtyField.setText(String.valueOf(temporyProduct.getOnHandQty()));
					qtyField.requestFocus(); 

				} catch (Exception f) {
					AlertHandler.getAlert(AlertType.ERROR, "Cannot Load Item Invalid", f.getMessage());
					f.printStackTrace();
				}
			
		
	}

	@FXML
	public void ClearAllFields() {
		customerInstance = null;
		temporyProduct = null;
		itemDescCombo.getEditor().clear();
		itemDescCombo.getSelectionModel().clearSelection();
		itemDescCombo.getItems().clear();
		itemCodeField.clear();
		unitPriceField.clear();
		qtyField.clear();
		discountField.clear();
		onHandQtyField.clear();
		itemCodeField.requestFocus();
		creditPeriodField.clear();
		expireDateField.clear();
		advancePaymentField.clear();
		totalDueAmountField.clear();
		comNameField.setText("");
		custNamefield.setText("");
		customerAutoIdField.clear();
		custAddressField.setText("");
		nicNumbField.clear();
		outstandingField.clear();
		totalNetAmountField.clear();
		totalItemsField.clear();
		totalDiscountField.clear();
		totalQuantityField.clear();
		creditIncoiveDetailList.clear();
		creditSaleTable.getItems().clear();
	}

		
	
	private void refreshSummaryData() {
		totalNetAmount = 0;
		billDicount = 0;
		billItemQty = 0;
		billItemCount = 0;
		seqNo = 0;
		grossBillAmount =0;
		int itemCount = 0;
		
		for (CreditInvoiceDetail e : creditIncoiveDetailList) {
			e.setSeqNo(++seqNo);
			itemCount++;
			if (e.getDiscount() > 0) {
				billDicount = billDicount + (e.getDiscount());
			}
			totalNetAmount = totalNetAmount + (e.getItemNetAmount());
			billItemQty = billItemQty + (e.getSalesQty());
			grossBillAmount = grossBillAmount + e.getItemGrossAmount();
		}
		billItemCount = itemCount;

		totalNetAmountField.setText(String.format("%.2f",totalNetAmount));
		totalItemsField.setText(String.format("%.2f",billItemCount));
		totalDiscountField.setText(String.format("%.2f",billDicount));
		totalQuantityField.setText(String.format("%.2f",billItemQty));
		
	}

	@FXML
	void addSellingQtyMethod() {
		try {
						
			if(ValidateInputs.validatePrices(qtyField , "Quantity Field") & ValidateInputs.validatePrices(discountField , "Discount Field")) {
				
			
			if(temporyProduct.getUnitOfMeasure().equals("unit")) {
				boolean check = ValidateInputs.validateQtyForUnitItems(qtyField ,"Quantity Field");
				if (!check)
					return;
			}
			
			double quantity = Double.valueOf(qtyField.getText());
			double discountPresentage = Double.valueOf(discountField.getText());
			
			for(CreditInvoiceDetail x : creditIncoiveDetailList) {
				if(x.getProduct().getPrd_id() == temporyProduct.getPrd_id()) {
					AlertHandler.getAlert(AlertType.WARNING, "Duplicate Entry : You Have already Entered this Item",
							"if you want to modify you can edit it using table view");
					clearTextFields();
					return;
				}
			}
			
			if(discountPresentage > temporyProduct.getDiscount()) {
				AlertHandler.getAlert(AlertType.ERROR, "Max DiscountError", "Only Allowable Max Discount is :" +temporyProduct.getDiscount());
				return;
			}

			if (quantity <= 0) {
				AlertHandler.getAlert(AlertType.ERROR, "Invalid Number", null);
				qtyField.getStyleClass().add("danger-for-warning");
				return;
			} else {
				CreditInvoiceDetail salesDet = new CreditInvoiceDetail();
				salesDet.setProduct(temporyProduct);
				salesDet.setCostPrice(temporyProduct.getUnit_cost_price());
				salesDet.setUnitPrice(temporyProduct.getUnit_price());
				salesDet.setAverageCostPrice(temporyProduct.getUnitAverageCost());
				salesDet.setItemVat(temporyProduct.getVat());
				salesDet.setSalesQty(quantity);
				salesDet.setItemGrossAmount(salesDet.getUnitPrice() * quantity);
				
				if (discountPresentage > 0) {
					salesDet.setDiscountPercentage(discountPresentage);
					double realDiscountPersentage = discountPresentage / 100;
					double actualUnitPrice = temporyProduct.getUnit_price();
					double discountValue =  actualUnitPrice * realDiscountPersentage;
					double discountedPrice = actualUnitPrice - discountValue;
					salesDet.setDiscountedPrice(discountedPrice);
					salesDet.setDiscount(discountValue * quantity);
					salesDet.setItemNetAmount(discountedPrice * quantity);
				} else {
					salesDet.setItemNetAmount(salesDet.getUnitPrice() * quantity);
					salesDet.setDiscountedPrice(salesDet.getUnitPrice());
				}
				creditIncoiveDetailList.add(salesDet);
				creditSaleTable.setItems(creditIncoiveDetailList);
				temporyProduct = null;
				salesDet = null;
				clearTextFields();
				refreshSummaryData();
				//itemCodeField.requestFocus();
				

			}
			}

		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "InValid Number", null);
			
			e.printStackTrace();
		}
		
	}

	@FXML
	void clearTextFields() {

		itemDescCombo.getEditor().clear();
		itemDescCombo.getSelectionModel().clearSelection();
		itemDescCombo.getItems().clear();
		itemCodeField.clear();
		unitPriceField.clear();
		qtyField.clear();
		discountField.clear();
		onHandQtyField.clear();
		itemCodeField.requestFocus();

	}

	@FXML
	void editQtyButtonClicked(CellEditEvent<CreditInvoiceDetail, Double> event) {
		CreditInvoiceDetail tempData;

		tempData = (CreditInvoiceDetail) creditSaleTable.getSelectionModel().getSelectedItem();
		
		try {
			if (creditIncoiveDetailList.isEmpty()) {
				System.out.println("No Data to Edit");
			} else {
				
				if(tempData.getProduct().getUnitOfMeasure().equals("unit")) {
					String checkValue = event.getNewValue().toString();
					checkValue = checkValue.contains(".") ? checkValue.replaceAll("0*$","").replaceAll("\\.$","") : checkValue;
					boolean check = ValidateInputs.validateQtyForUnitItemsWithString(checkValue, "Quantity Field");
					if(!check) {
						event.getTableView().getItems().set(event.getTablePosition().getRow(), tempData);
						return;
					}
					
				}
				
				double qty = event.getNewValue();
				tempData.setSalesQty(qty);
				
				if(tempData.getDiscountPercentage() > 0) {
					double realDiscountPersentage = tempData.getDiscountPercentage()/100;
					double discountValue =  tempData.getUnitPrice() * realDiscountPersentage;
					double discountedPrice = tempData.getUnitPrice()  - discountValue;
					tempData.setDiscount(discountValue * qty);
					tempData.setItemNetAmount(discountedPrice * qty);
				}
				else {
					tempData.setItemNetAmount(qty * tempData.getUnitPrice());
				}
				tempData.setItemGrossAmount(qty * tempData.getUnitPrice());
				refreshSummaryData();
				event.getTableView().getItems().set(event.getTablePosition().getRow(), tempData);
			}
			
		}catch(Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Cell Edit Event Error", e.getLocalizedMessage());
			event.getTableView().getItems().set(event.getTablePosition().getRow(), tempData);
			e.printStackTrace();
		}
		
	}

	@FXML
	void loadCustomerButtonClicked(ActionEvent event) throws IOException {
		
		if(customerTableLoadStage != null) {
			if(AlertHandler.getAlert(AlertType.CONFIRMATION, "Confirmation", ""
					+ "Do you want to get Updated Customer List ?").getResult().getButtonData().
			equals(ButtonData.OK_DONE)){
			customerTableLoadStage = null;
			}
			
		}
		
		if (customerTableLoadStage == null) {
			String loc = "/fxml/customer/customerTable.fxml";
			FXMLLoader loader = new FXMLLoader(getClass().getResource(loc));
			Scene scene = new Scene(loader.load());
			CustomerTableController passLoadertoSupplierTable = loader.getController();
			passLoadertoSupplierTable.setLoader(this.thiLoader);
			customerTableLoadStage = new Stage(StageStyle.DECORATED);
			customerTableLoadStage.setScene(scene);
			customerTableLoadStage.show();
		} else if (customerTableLoadStage.isShowing()) {
			customerTableLoadStage.toFront();

		} else {
			customerTableLoadStage.show();
		}
		
		

	}

	@FXML
	void removeItemFromTableView(ActionEvent event) {
		CreditInvoiceDetail selecteRowData = (CreditInvoiceDetail) creditSaleTable.getSelectionModel().getSelectedItem();
		if (!creditIncoiveDetailList.isEmpty() && !creditSaleTable.getSelectionModel().getSelectedItems().isEmpty()) {
			creditIncoiveDetailList.remove(selecteRowData);
			refreshSummaryData();
			creditSaleTable.setItems(creditIncoiveDetailList);
		} else {
			AlertHandler.getAlert(AlertType.WARNING, "Data Error",
					"No data to Delete");
		}
	}
	
	// update last bill no data
		private void updateLastBillNo() {

			long lastNumber = creditInvoiceDao.getLastBillNo(MainController.getUserSession(),
					java.sql.Date.valueOf(LocalDate.now()));

			lastBillNo = 1 + lastNumber;

			System.out.println(lastBillNo);
			

		}

	@FXML
	void saveCreditNoteClicked(ActionEvent event) {
		
		try {
		
			if(!creditIncoiveDetailList.isEmpty() && customerInstance !=null) {
				
				if(ValidateInputs.validateNumbers(creditPeriodField , "Credit Period Field")) {
					
					Users user = MainController.getUserSession();
					
					CreditInvoiceId invoiceId = new CreditInvoiceId();
					invoiceId.setInvoceId(lastBillNo);
					invoiceId.setDate(java.sql.Date.valueOf(LocalDate.now()));
					invoiceId.setUser(user);
					CreditInvoice creditInvoice = new CreditInvoice();
					creditInvoice.setCreditInvoiceId(invoiceId);
					creditInvoice.setCreditPeriod(Integer.parseInt(creditPeriodField.getText()));
					creditInvoice.setCustomer(customerInstance);
					if(customerInstance.getCompanyName() == null || customerInstance.getCompanyName().isEmpty()) {
						creditInvoice.setCustomerName(customerInstance.getFirstName()+" "+customerInstance.getLastName());
					}
					else {
						creditInvoice.setCustomerName(customerInstance.getCompanyName());
					}
					
					creditInvoice.setExpiredDate(java.sql.Date.valueOf(expireDateField.getText()));
					if(advancePaymentField.getText().isEmpty() && advancePaymentField.getText().isBlank()) {
						creditInvoice.setIfAdvancePayment(0);
					}
					else {
						creditInvoice.setIfAdvancePayment(Double.parseDouble(advancePaymentField.getText()));
					
					}
					creditInvoice.setInvoiceDiscount(billDicount);
					creditInvoice.setInvoiceGrossAmount(grossBillAmount);
					creditInvoice.setInvoiceNetAmount(totalNetAmount);
					creditInvoice.setSettledStatus(false);
					creditInvoice.setSettledAmount(0);
					
					for(CreditInvoiceDetail crdDetails : creditIncoiveDetailList) {
						
						CreditInvoiceDetailId idOfDetail = new CreditInvoiceDetailId();
						idOfDetail.setCreditInvoice(creditInvoice);
						idOfDetail.setSeqNo(crdDetails.getSeqNo());
						crdDetails.setCreditInvoiceId(idOfDetail);
					}
					
					if(AlertHandler.getAlert(AlertType.CONFIRMATION, "Are Your Sure?", "You Want to Update this").getResult().getButtonData().equals(ButtonData.OK_DONE)) {
						if(creditInvoiceDao.saveCreditInvoice(creditInvoice, creditIncoiveDetailList)){
							AlertHandler.getAlert(AlertType.INFORMATION, "Sucess", null);
							viewReport(creditInvoice.getCreditInvoiceId());
							creditIncoiveDetailList.clear();
							creditSaleTable.getItems().clear();
							refreshSummaryData();
							ClearAllFields();
							updateLastBillNo();
							customerAutoIdField.requestFocus();
						}
					}
					
				}
				
				
				
			}
		
			
		}catch(Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Saving Credit Invoice Error", e.getLocalizedMessage());
		}

	}
	
	public void viewReport(CreditInvoiceId crdId) throws JRException, IOException, SQLException {

		
		String loc= "resources/jasperreports/creditinvoiceinquary.jasper";
		
		String userName = crdId.getUser().getUserName();
		java.sql.Date date = crdId.getDate();
		long invoiceId = crdId.getInvoceId();
		
		System.out.println(userName);
		System.out.println(date);
		System.out.println(invoiceId);
		
		Connection connectionForReports = HibernateUtil.getSessionFactory().getSessionFactoryOptions()
				.getServiceRegistry().getService(ConnectionProvider.class).getConnection();

		Map<String, Object> parameters = new HashMap();
		parameters.put("companyName", MainController.getCompanyInfoSession().getCompanyName());
		parameters.put("streetAddress", MainController.getCompanyInfoSession().getAddressLine1());
		parameters.put("addressLine", MainController.getCompanyInfoSession().getAddressLine2());
		parameters.put("city", MainController.getCompanyInfoSession().getAddressLine3());
		parameters.put("telephoneNum", MainController.getCompanyInfoSession().getTelephoneNum());
		parameters.put("documentOriginalStatus", "Original");
		parameters.put("invoiceId", invoiceId);
		parameters.put("userName", userName);
		parameters.put("date", date);
		parameters.put("printedBy", userName);

		JasperPrint jp = JasperFillManager.fillReport(loc, parameters, connectionForReports);

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		JasperExportManager.exportReportToPdfStream(jp, output);
		final String PDF_FILE = "resources/pdfreports/creditInvoice.pdf";
		OutputStream pdfFile = new FileOutputStream(new File(PDF_FILE));
		pdfFile.write(output.toByteArray());
		pdfFile.flush();
		pdfFile.close();
		Desktop.getDesktop().open(new File(PDF_FILE));
	
}
	
	private void creditPeriodAndAdvanceAmountAction() {
		
		creditPeriodField.setOnKeyReleased(e->{
			
			if(e.getCode().isDigitKey()) {
			
					int getNoOfDays = Integer.parseInt(creditPeriodField.getText());
					
					java.sql.Date setDate = java.sql.Date.valueOf(LocalDate.now().plusDays(getNoOfDays));
					
					expireDateField.setText(setDate.toString());
										
				}else if(e.getCode().isLetterKey() || e.getCode().equals(KeyCode.SPACE))  {
					AlertHandler.getAlert(AlertType.ERROR, "Input Type Error",null);
					expireDateField.clear();
					creditPeriodField.clear();				
				}
				else if(e.getCode().equals(KeyCode.BACK_SPACE) || e.getCode().equals(KeyCode.DELETE)) {
					expireDateField.clear();
					creditPeriodField.clear();
				}
				else if(e.getCode().equals(KeyCode.ENTER)) {
					if(!creditPeriodField.getText().isBlank() && !expireDateField.getText().isEmpty()) {
						saveButton.requestFocus();
					}
				}
			
		
			
		});
		

		
		advancePaymentField.setOnKeyReleased(e->{
			
			if(!creditIncoiveDetailList.isEmpty()) {
			if(e.getCode().isDigitKey()) {
			
					double getAdvAmount = Double.parseDouble(advancePaymentField.getText());
					totalDueAmountField.setText(String.valueOf((totalNetAmount-getAdvAmount)));
										
				}else if(e.getCode().isLetterKey() || e.getCode().equals(KeyCode.SPACE))  {
					AlertHandler.getAlert(AlertType.ERROR, "Input Type Error",null);
					totalDueAmountField.clear();
					advancePaymentField.clear();
				}
				else if(e.getCode().equals(KeyCode.BACK_SPACE) || e.getCode().equals(KeyCode.DELETE)) {
					totalDueAmountField.clear();
					advancePaymentField.clear();
				}
				else if(e.getCode().equals(KeyCode.ENTER)) {
					if(!advancePaymentField.getText().isBlank() && !totalDueAmountField.getText().isEmpty()) {
						creditPeriodField.requestFocus();
					}
				}
			}
			
		});
		
	}

	@FXML
	void viewOutstandingClicked(ActionEvent event) {

	}

}
