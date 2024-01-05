package invsys.controllers.salesrefund;

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
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import invsys.controllers.database.HibernateUtil;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.controllers.formvalidation.ValidateInputs;
import invsys.controllers.mainpage.MainController;
import invsys.controllers.usermaintain.UserTableController;
import invsys.entities.CreditInvoice;
import invsys.entities.CreditInvoiceDetail;
import invsys.entities.CreditInvoiceVoid;
import invsys.entities.CreditInvoiceVoidDetail;
import invsys.entities.CustomerRefundDetail;
import invsys.entities.CustomerRefunds;
import invsys.entities.Sales;
import invsys.entities.SalesDetails;
import invsys.entities.Users;
import invsys.entities.compositkeys.CreditInvoiceVoidDetId;
import invsys.entities.compositkeys.CustomerRefundDetId;
import invsys.entitiydao.CreditInvoiceDao;
import invsys.entitiydao.RefundDao;
import invsys.entitiydao.SalesDao;
import invsys.entitiydao.UserDao;
import invsys.entitiydao.impl.CreditInvoiceDaoImpl;
import invsys.entitiydao.impl.RefundDaoImpl;
import invsys.entitiydao.impl.SalesDaoImpl;
import invsys.entitiydao.impl.UserDaoImpl;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.converter.DoubleStringConverter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

public class SalesRefundController implements Initializable {

	@FXML
	private JFXTextField billNoField;

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
	private JFXComboBox<String> refundModeCombo;

	@FXML
	private AnchorPane anchor;

	@FXML
	private TextField itemCodeField;

	@FXML
	private JFXComboBox<String> itemDescCombo;

	@FXML
	private TextField qtyField;

	@FXML
	private TextField discountField;

	@FXML
	private TextField unitPriceField;

	@FXML
	private TextField onSaleQtyField;

	@FXML
	private TableView refundTableView;

	@FXML
	private TableColumn<?,?> snoCol;

	@FXML
	private TableColumn<?,?> itemCodeCol;

	@FXML
	private TableColumn<?,?> descCol;

	@FXML
	private TableColumn qtyCol;

	@FXML
	private TableColumn<?,?> priceCol;

	@FXML
	private TableColumn<?,?> discountPercentageCol;

	@FXML
	private TableColumn<?,?> discountCol;

	@FXML
	private TableColumn<?,?> grossAmountCol;

	@FXML
	private TableColumn<?,?> netAmountCol;

	@FXML
	private TextField totalItemsField;

	@FXML
	private TextField totalQuantityField;

	@FXML
	private TextField totalDiscountField;

	@FXML
	private TextField totalGrossAmountField;

	@FXML
	private TextField totalNetAmountField;

	@FXML
	private Button saveButton;
	
	@FXML
	private Button removeItemButton;

	// tempory product variable to keep track the setCombo box on clicked function
	// pressed
	//
	private CustomerRefundDetail temporyCustomerRefundDet;

	//get user instance and hold it untill load refund details
	//to pass composite id
	private Users temporyUser;
	
	//hold instance of Sales & Credit Invoice when loading
	private Sales sales;
	private CreditInvoice creditInvoice;

	// observable list for Sales Refund details for table view
	private ObservableList observableListForRefunds = FXCollections.observableArrayList();
	private ObservableList observableListForRefundValidation = FXCollections.observableArrayList();
	

	// in order to pass data between this loader and user Select table loader
	private FXMLLoader thiLoader;

	// User Stage table Instance tracking for opening and closing stage
	private Stage userLoadTableStage;

	// other global vairables to track , no of product count , sum of quantity, sum
	// of total

	private double totalNetAmount;
	private double billItemCount;
	private double billItemQty;
	private double billDicount;
	private int seqNo;
	private double grossBillAmount;

	
	
	//Dao Classes
	CreditInvoiceDao creditInvoiceDao = null;
	RefundDao refundDao = null;
	SalesDao salesDao = null;
	UserDao userDao = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ValidateInputs.getInstance();
		salesDao =SalesDaoImpl.getDao();
		userDao = UserDaoImpl.getDao();
		creditInvoiceDao = CreditInvoiceDaoImpl.getDao();
		refundDao = RefundDaoImpl.getDao();
		getProductFromDescComboField();
		getProdcutFromItemCode();

		ValidateInputs.setDateFormatter(invoiceDateField);
		refundModeComboSelectionChange();

		refundModeCombo.getItems().addAll("POSS", "Credit Invoice");

		qtyField.setOnKeyReleased(e -> {
			if (e.getCode().equals(KeyCode.ESCAPE)) {
				clearTextFields();
			}
		});

	}

	// get loader details from MainController class in order pass data between
	// scenes
	public FXMLLoader getThiLoader() {
		return thiLoader;
	}

	// set loader details from MainController class in order pass data between
	// scenes
	public void setThiLoader(FXMLLoader thiLoader) {
		this.thiLoader = thiLoader;
	}

	// refund mode combo selection change
	private void refundModeComboSelectionChange() {

		refundModeCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal.equals("POSS")) {
				initSalesRefundCustomerColumns();	
			}
			
			if (newVal.equals("Credit Invoice")) {
			initCreditInvoiceReturnTableColumns();
			}
			ClearAllFields();
			refreshSummaryData();
			billNoField.requestFocus();
		});
	}
	
	private void initSalesRefundCustomerColumns() {
	
		((TableColumn<CustomerRefundDetail, String>) itemCodeCol).setCellValueFactory(
				(TableColumn.CellDataFeatures<CustomerRefundDetail, String> cdata) -> new SimpleStringProperty(
						String.valueOf(cdata.getValue().getProduct().getPrd_id())));
		((TableColumn<CustomerRefundDetail, String>)descCol).setCellValueFactory(
				(TableColumn.CellDataFeatures<CustomerRefundDetail, String> data) -> new SimpleStringProperty(
						data.getValue().getProduct().getP_name()));
		priceCol.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
		snoCol.setCellValueFactory(new PropertyValueFactory<>("seqNo"));
		qtyCol.setCellValueFactory(new PropertyValueFactory<>("refundQty"));
		((TableColumn<CustomerRefundDetail, String>) discountCol).setCellValueFactory(celldata -> Bindings.format("%.2f", celldata.getValue().getDiscount()));
		((TableColumn<CustomerRefundDetail, String>) netAmountCol).setCellValueFactory(celldata -> Bindings.format("%.2f", celldata.getValue().getItemNetAmount()));
		((TableColumn<CustomerRefundDetail, String>) grossAmountCol).setCellValueFactory(celldata -> Bindings.format("%.2f", celldata.getValue().getItemGrossAmount()));
		discountPercentageCol.setCellValueFactory(new PropertyValueFactory<>("discountPercentage"));
		
		
		qtyCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
	}
	
	//initialize credit invoice return columns
	private void initCreditInvoiceReturnTableColumns() {
		
		((TableColumn<CreditInvoiceVoidDetail, String>) itemCodeCol).setCellValueFactory(
				(TableColumn.CellDataFeatures<CreditInvoiceVoidDetail, String> cdata) -> new SimpleStringProperty(
						String.valueOf(cdata.getValue().getProduct().getPrd_id())));
		((TableColumn<CreditInvoiceVoidDetail, String>)descCol).setCellValueFactory(
				(TableColumn.CellDataFeatures<CreditInvoiceVoidDetail, String> data) -> new SimpleStringProperty(
						data.getValue().getProduct().getP_name()));
		priceCol.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
		qtyCol.setCellValueFactory(new PropertyValueFactory<>("salesQty"));
		snoCol.setCellValueFactory(new PropertyValueFactory<>("seqNo"));
		((TableColumn<CreditInvoiceVoidDetail, String>) discountCol).setCellValueFactory(celldata -> Bindings.format("%.2f", celldata.getValue().getDiscount()));
		((TableColumn<CreditInvoiceVoidDetail, String>) netAmountCol).setCellValueFactory(celldata -> Bindings.format("%.2f", celldata.getValue().getItemNetAmount()));
		((TableColumn<CreditInvoiceVoidDetail, String>) grossAmountCol).setCellValueFactory(celldata -> Bindings.format("%.2f", celldata.getValue().getItemGrossAmount()));
		discountPercentageCol.setCellValueFactory(new PropertyValueFactory<>("discountPercentage"));
		
	}

	@FXML
	void ClearAllFields() {
		sales = null;
		creditInvoice = null;
		temporyUser = null;
		temporyCustomerRefundDet = null;
		itemDescCombo.getEditor().clear();
		itemDescCombo.getSelectionModel().clearSelection();
		itemDescCombo.getItems().clear();
		itemCodeField.clear();
		unitPriceField.clear();
		qtyField.clear();
		discountField.clear();
		onSaleQtyField.clear();
		billNoField.clear();
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
		totalQuantityField.clear();
		observableListForRefunds.clear();
		refundTableView.getItems().clear();
		observableListForRefunds.clear();
		observableListForRefundValidation.clear();
		refundTableView.getItems().clear();
		
	}

	
	
	
	
	
	
	//add refund quantity method only for POSS sales refunds
	@FXML
	void addRefundQtyMethod() {
		
		try {
			if(refundModeCombo.getValue().equals("POSS")) {
				
				if(ValidateInputs.validatePrices(qtyField , "Quantity Field") && ValidateInputs.validatePrices(discountField , "Quantity Field")) {
					
					double refundQty = Double.parseDouble(qtyField.getText());
					double discountPresentage = Double.valueOf(discountField.getText());
					
					if(discountPresentage > temporyCustomerRefundDet.getProduct().getDiscount()) {
						AlertHandler.getAlert(AlertType.ERROR, "Max DiscountError", "Only Allowable Max Discount is :" 
					+temporyCustomerRefundDet.getProduct().getDiscount());
						return;
					}
					
					if(temporyCustomerRefundDet.getProduct().getUnitOfMeasure().equals("unit")) {
						String checkValue = String.valueOf(refundQty);
						checkValue = checkValue.contains(".") ? checkValue.replaceAll("0*$","").replaceAll("\\.$","") : checkValue;
						boolean check = ValidateInputs.validateQtyForUnitItemsWithString(checkValue, "Quantity Field");
						if(!check) {
							return;
						}
					}
					
					for (Object obj : observableListForRefunds) {
						if (temporyCustomerRefundDet.getProduct().getPrd_id() == ((CustomerRefundDetail) obj).getProduct().getPrd_id()) {
							AlertHandler.getAlert(AlertType.WARNING, "Duplicate Entry : You Have already Entered this Item",
									"if you want to modify you can edit it using table view");
							clearTextFields();
							return;

						}

					}
					
					if (refundQty > temporyCustomerRefundDet.getSalesQtys() || refundQty <= 0) {
						AlertHandler.getAlert(AlertType.ERROR, "Refund Quantity Error",
								"Refund Qty should be less than or equal to Sales Qty");
						return;
					} 
					else {
						
						CustomerRefundDetail possrefund = new CustomerRefundDetail();
						possrefund.setAverageCostPrice(temporyCustomerRefundDet.getAverageCostPrice());
						possrefund.setCostPrice(temporyCustomerRefundDet.getCostPrice());
						possrefund.setItemGrossAmount(refundQty * temporyCustomerRefundDet.getUnitPrice());
						possrefund.setItemVat(temporyCustomerRefundDet.getItemVat());
						possrefund.setProduct(temporyCustomerRefundDet.getProduct());
						possrefund.setSalesQtys(temporyCustomerRefundDet.getSalesQtys());
						possrefund.setRefundQty(refundQty);
						possrefund.setUnitPrice(temporyCustomerRefundDet.getUnitPrice());
						
						if(discountPresentage > 0) {
							possrefund.setDiscountPercentage(discountPresentage);
							double realDiscountPersentage = discountPresentage / 100;
							double actualUnitPrice = temporyCustomerRefundDet.getUnitPrice();
							double discountValue =  actualUnitPrice * realDiscountPersentage;
							double discountedPrice = actualUnitPrice - discountValue;
							
							possrefund.setDiscountedunitPrice(discountedPrice);
							possrefund.setItemNetAmount(refundQty * discountedPrice );
							possrefund.setDiscount(discountValue * refundQty);
						}
						else {
							possrefund.setItemNetAmount(temporyCustomerRefundDet.getUnitPrice() * refundQty);
							possrefund.setDiscountedunitPrice(temporyCustomerRefundDet.getUnitPrice());
						}
						
						observableListForRefunds.add(possrefund);
						refundTableView.setItems(observableListForRefunds);
						temporyCustomerRefundDet = null;
						possrefund = null;
						clearTextFields();
						itemCodeField.requestFocus();
						refreshSummaryData();
						
					}
					
				}		
				
			}
		}catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Cannot Add Item", e.getLocalizedMessage());
		}
		
		
		
	}

	// Cell edit event on grn table qty populate data throuhout the list
	@FXML
	public void editQtyButtonClicked(CellEditEvent<CustomerRefundDetail, Double> event) {
		CustomerRefundDetail tempData;

		tempData = (CustomerRefundDetail) refundTableView.getSelectionModel().getSelectedItem();
		try {
			if (observableListForRefunds.isEmpty()) {
				System.out.println("No Data to Edit");
			} else {
				
				if(tempData.getProduct().getUnitOfMeasure().equals("unit")) {
					String checkValue = event.getNewValue().toString();
					checkValue = checkValue.contains(".") ? checkValue.replaceAll("0*$","").replaceAll("\\.$","") : checkValue;
					boolean check = ValidateInputs.validateQtyForUnitItemsWithString(checkValue , "Quantity Field");
					if(!check) {
						event.getTableView().getItems().set(event.getTablePosition().getRow(), tempData);
						return;
					}
					
				}
				double qty = Double.parseDouble(event.getNewValue().toString());
				if (qty > tempData.getSalesQtys()) {
					AlertHandler.getAlert(AlertType.ERROR, "Refund Quantity Max Error",
							"Refund Qty should be less than or equal to Sales Qty");
					event.getTableView().getItems().set(event.getTablePosition().getRow(), tempData);

				} else {
					tempData.setRefundQty(qty);
					tempData.setItemNetAmount(tempData.getDiscountedunitPrice()*qty);
					tempData.setItemGrossAmount(tempData.getUnitPrice()*qty);
					refreshSummaryData();
					event.getTableView().getItems().set(event.getTablePosition().getRow(), tempData);
				}
			} 
		}
		catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Cell Edit Event Error", e.getLocalizedMessage());
			event.getTableView().getItems().set(event.getTablePosition().getRow(), tempData);
			e.printStackTrace();
		}
	}
	

	@FXML
	void clearTextFields() {
		
			
		itemDescCombo.getEditor().clear();
		itemDescCombo.getSelectionModel().clearSelection();
		//itemDescCombo.getItems().clear();
		itemCodeField.clear();
		unitPriceField.clear();
		qtyField.clear();
		discountField.clear();
		onSaleQtyField.clear();
		itemCodeField.requestFocus();
	}

		
	//get product from item Code field
		// specialized algorithm by summarizing quantity of the duplicate items in the sales details to single instance
	//for example same item can be in more than one line when raising POS sales
	//hold instance variable to track how many times same item has been discounted
	private void getProdcutFromItemCode() {
		itemCodeField.setOnAction(e->{
			try {
				if(!observableListForRefundValidation.isEmpty()) {
					
				long prdId = Long.parseLong(itemCodeField.getText());
				if(ValidateInputs.validateNumbers(itemCodeField , "Item Code Field")) {
					for (Object obj : observableListForRefundValidation) {
						if(obj instanceof CustomerRefundDetail) {
							if(prdId == ((CustomerRefundDetail) obj).getProduct().getPrd_id()){
								temporyCustomerRefundDet = new CustomerRefundDetail();
								temporyCustomerRefundDet.setAverageCostPrice(((CustomerRefundDetail) obj).getAverageCostPrice());
								temporyCustomerRefundDet.setCostPrice(((CustomerRefundDetail) obj).getCostPrice());
								temporyCustomerRefundDet.setItemGrossAmount(((CustomerRefundDetail) obj).getItemGrossAmount());
								temporyCustomerRefundDet.setItemVat(((CustomerRefundDetail) obj).getItemVat());
								temporyCustomerRefundDet.setProduct(((CustomerRefundDetail) obj).getProduct());
								temporyCustomerRefundDet.setUnitPrice(((CustomerRefundDetail) obj).getUnitPrice());
								
								double salesQty = 0;
								
								for(Object dupObj : observableListForRefundValidation) {
									
									if(dupObj instanceof CustomerRefundDetail) {
										long dupObjPrdId = ((CustomerRefundDetail) dupObj).getProduct().getPrd_id();
										long objPrdId = ((CustomerRefundDetail) obj).getProduct().getPrd_id();
										if(objPrdId == dupObjPrdId) {
											
											salesQty += ((CustomerRefundDetail) dupObj).getSalesQtys();
										}
										
										
									}
									
								}
								
								temporyCustomerRefundDet.setSalesQtys(salesQty);
								itemDescCombo.getEditor().setText(temporyCustomerRefundDet.getProduct().getP_name());
								itemCodeField.setText(String.valueOf(((CustomerRefundDetail) obj).getProduct().getPrd_id()));
								unitPriceField.setText(String.valueOf(((CustomerRefundDetail) obj).getUnitPrice()));
								onSaleQtyField.setText(String.valueOf(temporyCustomerRefundDet.getSalesQtys()));
								discountField.setText(String.valueOf(0));
								qtyField.requestFocus();
								break;
								
							
							}
						}
					}
				}
				}
				
			}catch(Exception f) {
				AlertHandler.getAlert(AlertType.ERROR, "Cannot Load Data", f.getLocalizedMessage());
				f.printStackTrace();
			}
		});
	}
	
	
	//get product from item combo field
	// specialized algorithm
	@FXML
	void getProductFromDescComboField() {
		
		itemDescCombo.getEditor().setOnKeyReleased(e -> {

			if (e.getCode() == KeyCode.ENTER) {

				if (itemDescCombo.getEditor().getText().isEmpty() || itemDescCombo.getItems().isEmpty()) {
					return;
				} else {
					try {
						String prd = itemDescCombo.getEditor().getText();
						for (Object obj : observableListForRefundValidation) {
							if(obj instanceof CustomerRefundDetail) {
								if(prd.equals(((CustomerRefundDetail) obj).getProduct().getP_name())){
									temporyCustomerRefundDet = new CustomerRefundDetail();
									temporyCustomerRefundDet.setAverageCostPrice(((CustomerRefundDetail) obj).getAverageCostPrice());
									temporyCustomerRefundDet.setCostPrice(((CustomerRefundDetail) obj).getCostPrice());
									temporyCustomerRefundDet.setItemGrossAmount(((CustomerRefundDetail) obj).getItemGrossAmount());
									temporyCustomerRefundDet.setItemVat(((CustomerRefundDetail) obj).getItemVat());
									temporyCustomerRefundDet.setProduct(((CustomerRefundDetail) obj).getProduct());
									temporyCustomerRefundDet.setUnitPrice(((CustomerRefundDetail) obj).getUnitPrice());
									
									double salesQty = 0;
									
									for(Object dupObj : observableListForRefundValidation) {
										
										if(dupObj instanceof CustomerRefundDetail) {
											long dupObjPrdId = ((CustomerRefundDetail) dupObj).getProduct().getPrd_id();
											long objPrdId = ((CustomerRefundDetail) obj).getProduct().getPrd_id();
											if(objPrdId == dupObjPrdId) {
												
												salesQty += ((CustomerRefundDetail) dupObj).getSalesQtys();
											}
											
											
										}
										
									}
								
									temporyCustomerRefundDet.setSalesQtys(salesQty);
									itemCodeField.setText(String.valueOf(((CustomerRefundDetail) obj).getProduct().getPrd_id()));
									unitPriceField.setText(String.valueOf(((CustomerRefundDetail) obj).getUnitPrice()));
									onSaleQtyField.setText(String.valueOf(temporyCustomerRefundDet.getSalesQtys()));
									discountField.setText(String.valueOf(0));
									qtyField.requestFocus();
									break;
									
								
								}
							}
						}

					} catch (Exception f) {
						AlertHandler.getAlert(AlertType.ERROR, "Cannot Find or Load Item Invalid",
								f.getLocalizedMessage());
						f.printStackTrace();
					}
				}

			}

		});

	}
	
	private void refreshSummaryData() {
		totalNetAmount = 0;
		billDicount = 0;
		billItemQty = 0;
		billItemCount = 0;
		seqNo = 0;
		grossBillAmount =0;
		int itemCount = 0;
		
		for (Object e : observableListForRefunds) {
		
			if(e instanceof CustomerRefundDetail) {
				((CustomerRefundDetail) e).setSeqNo(++seqNo);
				itemCount++;
				if (((CustomerRefundDetail) e).getDiscount() > 0) {
					billDicount = billDicount + (((CustomerRefundDetail) e).getDiscount());
				}
				totalNetAmount = totalNetAmount + (((CustomerRefundDetail) e).getItemNetAmount());
				billItemQty = billItemQty + (((CustomerRefundDetail) e).getRefundQty());
				grossBillAmount = grossBillAmount + ((CustomerRefundDetail) e).getItemGrossAmount();
				
			}
			if(e instanceof CreditInvoiceVoidDetail) {
				((CreditInvoiceVoidDetail) e).setSeqNo(++seqNo);
				itemCount++;
				if (((CreditInvoiceVoidDetail) e).getDiscount() > 0) {
					billDicount = billDicount + (((CreditInvoiceVoidDetail) e).getDiscount());
				}
				totalNetAmount = totalNetAmount + (((CreditInvoiceVoidDetail) e).getItemNetAmount());
				billItemQty = billItemQty + (((CreditInvoiceVoidDetail) e).getSalesQty());
				grossBillAmount = grossBillAmount + ((CreditInvoiceVoidDetail) e).getItemGrossAmount();
				
				
			}
			
		}
		
		billItemCount = itemCount;
		totalNetAmountField.setText(String.format("%.2f",totalNetAmount));
		totalItemsField.setText(String.format("%.2f",billItemCount));
		totalDiscountField.setText(String.format("%.2f",billDicount));
		totalQuantityField.setText(String.format("%.2f",billItemQty));
		totalGrossAmountField.setText(String.format("%.2f",grossBillAmount));
		
		
	}
	
	@FXML
	void loadRefundDetails() {
		
		if(!observableListForRefunds.isEmpty()) {
			AlertHandler.getAlert(AlertType.ERROR, "Refund Already Loaded", "Clear or finalize existing data before selecting another");
			return;
		}
		observableListForRefunds.clear();
		observableListForRefundValidation.clear();
		refundTableView.getItems().clear();
		itemDescCombo.getItems().clear();
		clearTextFields();
		custIdField.setText("");
		custNamefield.setText("");
				
		if(ValidateInputs.validateInputEmpty(refundModeCombo , "Refund Mode Field") && ValidateInputs.validateNumbers(billNoField , "Bill No Field") &&
			ValidateInputs.validateDateField(invoiceDateField , "Invoice Date Field") &&
			ValidateInputs.validateUserNames(userNameFiled , "User Name Field")) {
			try {
				String userText = userNameFiled.getText();
				temporyUser = userDao.getUserByName(userText);
				System.out.println(temporyUser.getUserName());
				java.sql.Date date = java.sql.Date.valueOf(invoiceDateField.getValue());
				long billNo = Long.parseLong(billNoField.getText());
				
				
				if(refundModeCombo.getValue().equals("POSS")) {
					
					
					for(SalesDetails det : salesDao.getSaleDetailsByBillNoDateUser(billNo, date, temporyUser)) {
						
						if(sales == null) {
							sales = det.getSalesDetId().getSales();
						}
						
						if(sales.isRefundStatus()) {
							AlertHandler.getAlert(AlertType.ERROR, "Duplicate Refund Error", "This BIll Has Already Refunded");
							ClearAllFields();
							return;
						}
						
						CustomerRefundDetail possrefund = new CustomerRefundDetail();
						possrefund.setAverageCostPrice(det.getAverageCostPrice());
						possrefund.setCostPrice(det.getCostPrice());
						possrefund.setDiscount(det.getDiscount());
						possrefund.setDiscountedunitPrice(det.getDiscountedPrice());
						possrefund.setDiscountPercentage(det.getDiscountPercentage());
						possrefund.setItemGrossAmount(det.getItemGrossAmount());
						possrefund.setItemNetAmount(det.getItemAmount());
						possrefund.setItemVat(det.getItemVat());
						possrefund.setProduct(det.getProduct());
						possrefund.setSalesQtys(det.getSalesQty());
						possrefund.setRefundQty(det.getSalesQty());
						possrefund.setUnitPrice(det.getUnitPrice());					
						observableListForRefundValidation.add(possrefund);
					 
					}
					if(!observableListForRefundValidation.isEmpty()) {
						if(AlertHandler.getAlertYesNo(AlertType.CONFIRMATION, "Confirmation", "Do You want to make a Full Bill Refund ?").getResult().getButtonData().equals(ButtonData.YES)){
							observableListForRefunds.addAll(observableListForRefundValidation);
							refreshSummaryData();
							removeItemButton.setDisable(true);
							refundTableView.setEditable(false);
							refundTableView.setItems(observableListForRefunds);
															
						} else {
							refundTableView.setEditable(true);
							removeItemButton.setDisable(false);
							for(Object x : observableListForRefundValidation) {
								if(x instanceof CustomerRefundDetail) {
									itemDescCombo.getItems().add(((CustomerRefundDetail) x).getProduct().getP_name());
								}
							}
						}
					} else {
						AlertHandler.getAlert(AlertType.ERROR, "No Data Found", null);
					}
				
					
				}
				
				if(refundModeCombo.getValue().equals("Credit Invoice")) {
					
					for(CreditInvoiceDetail det : creditInvoiceDao.getInvoiceDetailsByBillNoDateUser(billNo, date, temporyUser)) {
						
						if(creditInvoice == null) {
							creditInvoice = det.getCreditInvoiceId().getCreditInvoice();
						}
						
						if(creditInvoice.isRefundStatus()) {
							AlertHandler.getAlert(AlertType.ERROR, "Duplicate Refund Error", "This Invoice Has Already Refunded");
							ClearAllFields();
							return;
						}
						
						CreditInvoiceVoidDetail possrefund = new CreditInvoiceVoidDetail();
						possrefund.setAverageCostPrice(det.getAverageCostPrice());
						possrefund.setCostPrice(det.getCostPrice());
						possrefund.setDiscount(det.getDiscount());
						possrefund.setDiscountedunitPrice(det.getDiscountedPrice());
						possrefund.setDiscountPercentage(det.getDiscountPercentage());
						possrefund.setItemGrossAmount(det.getItemGrossAmount());
						possrefund.setItemNetAmount(det.getItemNetAmount());
						possrefund.setItemVat(det.getItemVat());
						possrefund.setProduct(det.getProduct());
						possrefund.setSalesQty(det.getSalesQty());
						possrefund.setUnitPrice(det.getUnitPrice());
						observableListForRefunds.add(possrefund);
						observableListForRefundValidation.add(possrefund);
					
					}
					refundTableView.setEditable(false);
					removeItemButton.setDisable(true);
					refreshSummaryData();
					refundTableView.setItems(observableListForRefunds);
					
										
					if(creditInvoice != null) {
						custIdField.setText(String.valueOf(creditInvoice.getCustomer().getCustomerId()));
						custNamefield.setText(creditInvoice.getCustomerName());
					}
					
					
					if(observableListForRefundValidation.isEmpty()) {
						
						AlertHandler.getAlert(AlertType.ERROR, "No Data Found", null);
						custIdField.setText("");
						custNamefield.setText("");
						return;
					}
				}		
				
				
			}catch(Exception e) {
				AlertHandler.getAlert(AlertType.ERROR, "Cannot Load Data", null);
				e.printStackTrace();
			}
			
		}

	}

	// load user table view to pick users
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
			getControl.setUserMainStageloader(thiLoader);
			getControl.setUserTableStage(userLoadTableStage);
			
		} else if (userLoadTableStage.isShowing()) {
			userLoadTableStage.toFront();

		} else {
			userLoadTableStage.show();
		}

	}
	
	
	// get user name from user table view to this userinterface username field
	public void setUserNameFromUserTableView(String userName) {
		userNameFiled.setText(userName);
	}

	@FXML
	void removeItemFromTableView() {
		try {
		CustomerRefundDetail selecteRowData = (CustomerRefundDetail) refundTableView.getSelectionModel().getSelectedItem();
		if (!observableListForRefunds.isEmpty() && !refundTableView.getSelectionModel().getSelectedItems().isEmpty()) {
			observableListForRefunds.remove(selecteRowData);
			refreshSummaryData();
			
			if(observableListForRefunds.isEmpty()) {
				ClearAllFields();
			}
			
		} else {
			AlertHandler.getAlert(AlertType.WARNING, "Data Error",
					"No data to Delete");
		}
		} catch(Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Error Deleting Items", "if this is a Credit Invoice Refund not deletion can be made");
			e.printStackTrace();
		}
	}

	
	// save refund 
	@FXML
	void saveRefundAction() {
		try {
			
			if(!observableListForRefunds.isEmpty()) {
				
				Users user = MainController.getUserSession();
				
				
				// Poss refund Refunding saving Method started from here
				if(refundModeCombo.getValue().equals("POSS") && sales != null) {
					
					CustomerRefunds refundMass = new CustomerRefunds();
					refundMass.setGrossRefundAmount(grossBillAmount);
					refundMass.setNetRefundAmount(totalNetAmount);
					refundMass.setRefundDate(java.sql.Date.valueOf(LocalDate.now()));
					refundMass.setRefundDiscountFromBill(billDicount);
					refundMass.setRefundedBy(user);
					refundMass.setSales(sales);
					
					for(Object refundDetail : observableListForRefunds) {
						
						if(refundDetail instanceof CustomerRefundDetail) {
							CustomerRefundDetId refundDetId = new CustomerRefundDetId();
							refundDetId.setCustomerRefund(refundMass);
							refundDetId.setSeqNo(((CustomerRefundDetail) refundDetail).getSeqNo());
							((CustomerRefundDetail) refundDetail).setCustomerRefundDetId(refundDetId);
						
						}
						
					}
					
				if(AlertHandler.getAlert(AlertType.CONFIRMATION, "Are You Sure ?", ""
						+ "You want to update this refund").getResult().getButtonData().equals(ButtonData.OK_DONE)) {
					if(refundDao.savePossBillRefundAndUpdateStock(refundMass, observableListForRefunds)) {
						AlertHandler.getAlert(AlertType.INFORMATION, "Success", null);
						viewReport(refundMass.getRefundId(),"resources/jasperreports/posBillRefundInquiry.jasper" , "POSS Refund");
						clearTextFields();
						ClearAllFields();
						
					}
				}
					
					
				}
							
								
				// credit Invoice Refunding saving Method started from here
				else if(refundModeCombo.getValue().equals("Credit Invoice") && creditInvoice != null) {
					
					CreditInvoiceVoid refundMass = new CreditInvoiceVoid();
					refundMass.setGrossVoidAmount(grossBillAmount);
					refundMass.setNetVoidAmount(totalNetAmount);
					refundMass.setVoidDate(java.sql.Date.valueOf(LocalDate.now()));
					refundMass.setVoidDiscountFromInvoice(billDicount);
					refundMass.setVoidedBy(user);
					refundMass.setCreditInvoice(creditInvoice);
				
					
					for(Object refundDetail : observableListForRefunds) {
						
						if(refundDetail instanceof CreditInvoiceVoidDetail) {
							CreditInvoiceVoidDetId refundDetId = new CreditInvoiceVoidDetId();
							refundDetId.setCreditInvoiceVoid(refundMass);
							refundDetId.setSeqNo(((CreditInvoiceVoidDetail) refundDetail).getSeqNo());
							((CreditInvoiceVoidDetail) refundDetail).setCreditInvoiceVoidDetId(refundDetId);
						
						}
						
					}
					
				if(AlertHandler.getAlert(AlertType.CONFIRMATION, "Are You Sure ?", ""
						+ "You want to update this refund").getResult().getButtonData().equals(ButtonData.OK_DONE)) {
					if(refundDao.saveCreditInvoiceRefundAndUpdateStock(refundMass, observableListForRefunds)) {
						AlertHandler.getAlert(AlertType.INFORMATION, "Success", null);
						viewReport(refundMass.getVoid_id(),"resources/jasperreports/CreditInvoiceRefundInquiry.jasper" , "Credit Invoice Refund");
						clearTextFields();
						ClearAllFields();
						
					}
				}
					
					
				}
			}
			
		}catch(Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Error Saving Data", e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	// view GRN Report
			public void viewReport(long refundId , String loc ,String refundMode) throws JRException, IOException, SQLException {

							
					
					
					Connection connectionForReports = HibernateUtil.getSessionFactory().getSessionFactoryOptions()
							.getServiceRegistry().getService(ConnectionProvider.class).getConnection();

					Map<String, Object> parameters = new HashMap();
					parameters.put("companyName", MainController.getCompanyInfoSession().getCompanyName());
					parameters.put("streetAddress", MainController.getCompanyInfoSession().getAddressLine1());
					parameters.put("addressLine", MainController.getCompanyInfoSession().getAddressLine2());
					parameters.put("city", MainController.getCompanyInfoSession().getAddressLine3());
					parameters.put("telephoneNum", MainController.getCompanyInfoSession().getTelephoneNum());
					parameters.put("refundId", refundId);
					parameters.put("documentOriginalStatus", "Original");
					parameters.put("printedBy", MainController.getUserSession().getUserName());

					JasperPrint jp = JasperFillManager.fillReport(loc, parameters, connectionForReports);

					ByteArrayOutputStream output = new ByteArrayOutputStream();
					JasperExportManager.exportReportToPdfStream(jp, output);
					final String PDF_FILE = "resources/pdfreports/"+refundMode+".pdf";
					OutputStream pdfFile = new FileOutputStream(new File(PDF_FILE));
					pdfFile.write(output.toByteArray());
					pdfFile.flush();
					pdfFile.close();
					Desktop.getDesktop().open(new File(PDF_FILE));
				
			}


}
