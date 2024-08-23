package invsys.controllers.goodreceived;

import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.controlsfx.control.Notifications;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import invsys.controllers.database.HibernateUtil;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.controllers.formvalidation.ValidateInputs;
import invsys.controllers.mainpage.MainController;
import invsys.entities.GoodReceived;
import invsys.entities.GoodReceivedDetails;
import invsys.entities.OrderItems;
import invsys.entities.PurchaseOrder;
import invsys.entitiydao.GoodReceivedDao;
import invsys.entitiydao.PurchaseOrderDao;
import invsys.entitiydao.impl.GoodReceivedDaoImpl;
import invsys.entitiydao.impl.PurchaseOrderDaoImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.converter.DoubleStringConverter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class GoodReceivedController implements Initializable {

	@FXML
	private JFXTextField orderNoField;

	@FXML
	private JFXTextField orderDateField;

	@FXML
	private JFXTextField orderExpField;
	
	@FXML
	private JFXTextField renewDaysField;
	
	@FXML
	private JFXButton renewOrderButton;
	
	@FXML
	private JFXTextField invoiceNumField;
	

	@FXML
	private Label supplierLabel;

	@FXML
	private JFXComboBox<String> itemDescField;

	@FXML
	private TextField itemCodeField;

	@FXML
	private TextField costPriceField;

	@FXML
	private TextField receivedQtyField;
	
	@FXML
	private TextField deliveredQtyField;

	@FXML
	private TextField orderedQtyField;
	
	@FXML
	private TableView grnTable;

	@FXML
	private TableColumn<GoodReceivedDetails, Integer> snoCol;

	@FXML
	private TableColumn<GoodReceivedDetails, String> itemCodeCol;

	@FXML
	private TableColumn<GoodReceivedDetails, String> descCol;

	@FXML
	private TableColumn<GoodReceivedDetails, String> orderedQtyCol;

	@FXML
	private TableColumn<GoodReceivedDetails, Double> receivedQtyCol;

	@FXML
	private TableColumn<GoodReceivedDetails, Double> costPriceCol;

	@FXML
	private TableColumn<GoodReceivedDetails, Double> amountCol;

	@FXML
	private TextField totReceviedQtyField;

	@FXML
	private TextField totOrderedQtyField;

	@FXML
	private TextField totOrderedAmountFiled;

	@FXML
	private TextField totReceivedAmntField;

	@FXML
	private TextField totOrderedItemsField;

	@FXML
	private TextField totReceivedItemsField;
	

	// this FXMLloader when loading this class inorder to pass data between
	// orderTableStage
	private FXMLLoader thisGrnLoader;

	// in order to keep one instance of purchase order table when hiding and closing
	// the purchase order tableview
	private Stage OrderTableStage;

	// grn Item Listfor TableView and update data to database
	private ObservableList<GoodReceivedDetails> grnTableDetailList = FXCollections.observableArrayList();

	// orderItems list to manupulate GRN through ordered items
	private List<OrderItems> purchaseOrderDetailList = new ArrayList<>();

	// tempory OrderItems instance variable to add received qty method
	private OrderItems temporyOrderItem;

	// sequence no of grn item instance variable
	private int seqNo;

	// other global vairables to track , no of product count , sum of quantity, sum
	// of total
	private double totalReceivedQty;
	private int receivedItems;
	private double totalReceivedAmount;

	// In order avoid duplicate updating bindautocomletion text as
	// "TextFields.bindAutoCompletion(itemDescField.getEditor(),
	// itemDescField.getItems());"
	Set<String> suggestions = new HashSet<String>();
	SuggestionProvider<String> provider = SuggestionProvider.create(suggestions);
	AutoCompletionTextFieldBinding<String> textFieldsAutoComplet;
	
	
	//Dao Classes
	GoodReceivedDao goodReceivedDao = null;
	PurchaseOrderDao purchaseOrderDao = null;

	
	 
	
	// initialization method
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		goodReceivedDao = GoodReceivedDaoImpl.getDao();
		purchaseOrderDao = PurchaseOrderDaoImpl.getDao();
		loadPurchaseOrderDetails();
		initGrnTableViewColumns();
		onPrdDescComboAction();

	}

	// getters and setters for instance variables
	public FXMLLoader getThisGrnLoader() {
		return thisGrnLoader;
	}

	public void setThisGrnLoader(FXMLLoader thisGrnLoader) {
		this.thisGrnLoader = thisGrnLoader;
	}

	// getter setter end

	@FXML
	void ClearAllFields() {

		totalReceivedQty = 0;
		receivedItems = 0;
		totalReceivedAmount = 0;
		seqNo = 0;
		temporyOrderItem = null;
		purchaseOrderDetailList.clear();
		grnTableDetailList.clear();
		suggestions.clear();
		provider.clearSuggestions();
		if (textFieldsAutoComplet != null)
			textFieldsAutoComplet.dispose();
		clearTextFields();
		totReceivedAmntField.clear();
		totReceivedItemsField.clear();
		totReceviedQtyField.clear();
		grnTable.getItems().clear();
		itemDescField.getItems().clear();
		itemCodeField.setEditable(false);
		itemDescField.setEditable(false);
		supplierLabel.setText("");
		orderDateField.clear();
		orderExpField.clear();
		orderNoField.clear();
		totOrderedQtyField.clear();
		totOrderedAmountFiled.clear();
		totOrderedItemsField.clear();
		totReceivedAmntField.clear();
		totReceivedItemsField.clear();
		totReceviedQtyField.clear();
		deliveredQtyField.clear();
		invoiceNumField.clear();
		orderedQtyField.clear();

	}
	
	@FXML
	private void clearAllExpectOrderField() {

		totalReceivedQty = 0;
		receivedItems = 0;
		totalReceivedAmount = 0;
		seqNo = 0;
		temporyOrderItem = null;
		purchaseOrderDetailList.clear();
		grnTableDetailList.clear();
		suggestions.clear();
		provider.clearSuggestions();
		if (textFieldsAutoComplet != null)
			textFieldsAutoComplet.dispose();
		clearTextFields();
		totReceivedAmntField.clear();
		totReceivedItemsField.clear();
		totReceviedQtyField.clear();
		grnTable.getItems().clear();
		itemDescField.getItems().clear();
		itemCodeField.setEditable(false);
		itemDescField.setEditable(false);
		supplierLabel.setText("");
		orderDateField.clear();
		orderExpField.clear();
		totOrderedQtyField.clear();
		totOrderedAmountFiled.clear();
		totOrderedItemsField.clear();
		totReceivedAmntField.clear();
		totReceivedItemsField.clear();
		totReceviedQtyField.clear();
		deliveredQtyField.clear();
		orderedQtyField.clear();
		

	}

	
	
	
	// received qty field enter method
	@FXML
	void addReceivedQtyMethod(ActionEvent event) {

		try {
			if (ValidateInputs.validatePrices(receivedQtyField ,"Received Qty Field")) {
				
				double receivedQty = Double.parseDouble(receivedQtyField.getText());
				
				if(temporyOrderItem.getProduct().getUnitOfMeasure().equals("unit")) {
					String checkValue = String.valueOf(receivedQty);
					checkValue = checkValue.contains(".") ? checkValue.replaceAll("0*$","").replaceAll("\\.$","") : checkValue;
					boolean check = ValidateInputs.validateQtyForUnitItemsWithString(checkValue , "Quantity Field");
					if(!check) {
						return;
					}
					
				}
				
			

				for (GoodReceivedDetails good : grnTableDetailList) {
					if (temporyOrderItem.getProduct().equals(good.getProduct())) {
						AlertHandler.getAlert(AlertType.WARNING, "Duplicate Entry : You Have already Entered this Item",
								"if you want to modify you can edit it using table view");
						clearTextFields();
						return;

					}

				}
				
			
					if (receivedQty > temporyOrderItem.getOrderQty() && temporyOrderItem.getDeliveredQty() == 0) {
						AlertHandler.getAlert(AlertType.ERROR, "Order Quantity Max Error",
								"Received Qty should be less than or equal to Ordered Qty if you wish to get this additional items please place another order");
						return;
					} 
					
				
				
					if (receivedQty > (temporyOrderItem.getOrderQty()-temporyOrderItem.getDeliveredQty()) && temporyOrderItem.getDeliveredQty() > 0) {
						AlertHandler.getAlert(AlertType.ERROR, "Order Quantity Max Error",
								"This Item Already Delivered Partialy Qty should be less than or equal to Ordered Qty if you wish to get this additional items please place another order");
						return;
					} 
				
				
				
				if (receivedQty <= 0 ) {
					AlertHandler.getAlert(AlertType.ERROR, "Order Quantity Error",
							"Received Qty should be less than or equal to Ordered Qty");
					return;
				} 
				else {

					GoodReceivedDetails grn = new GoodReceivedDetails();
					
					grn.setProduct(temporyOrderItem.getProduct());
					grn.setPurchaseOrder(temporyOrderItem.getOrder());
					grn.setCostPrice(temporyOrderItem.getPrductCost());
					grn.setGrnItemAmount(receivedQty * temporyOrderItem.getPrductCost());
					if(!temporyOrderItem.isPartialDeliveryStatus()) {
						grn.setOrderedQty(temporyOrderItem.getOrderQty());
					}
					else {
						grn.setOrderedQty(temporyOrderItem.getOrderQty()-temporyOrderItem.getDeliveredQty());
					}
					
					grn.setReceivedQty(receivedQty);
					
					// seq no will be added in refreshSummaryData(); method
					//grn.setSeqNo(++seqNo);
					grn.setUnitPrice(temporyOrderItem.getProduct().getUnit_price());
					grnTableDetailList.add(grn);

					grnTable.setItems(grnTableDetailList);

					refreshSummaryData();
					itemCodeField.clear();
					itemDescField.getEditor().clear();
					costPriceField.clear();
					receivedQtyField.clear();
					orderedQtyField.clear();
					deliveredQtyField.clear();
					itemDescField.getEditor().requestFocus();
				}
			}

		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Error In Input Data", "Please Enter Valid Qty");
			e.printStackTrace();
		}

	}

	// Cell edit event on grn table qty populate data throuhout the list
	@FXML
	public void editQtyButtonClicked(CellEditEvent<GoodReceivedDetails, Double> event) {
		GoodReceivedDetails tempData;

		tempData = (GoodReceivedDetails) grnTable.getSelectionModel().getSelectedItem();
		try {
			if (grnTableDetailList.isEmpty()) {
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
				if (qty > tempData.getOrderedQty()) {
					AlertHandler.getAlert(AlertType.ERROR, "Order Quantity Max Error",
							"Received Qty should be less than or equal to Ordered Qty if you wish to get this additional items please place another order");
					event.getTableView().getItems().set(event.getTablePosition().getRow(), tempData);

				} else {
					tempData.setReceivedQty(qty);
					tempData.setGrnItemAmount(qty * tempData.getCostPrice());
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

	// referesh table data and other data when qty field enter and item removed from
	// table view refresh

	private void refreshSummaryData() {
		totalReceivedQty = 0;
		receivedItems = 0;
		totalReceivedAmount = 0;
		seqNo = 0;
		
		for (GoodReceivedDetails detail : grnTableDetailList) {
			totalReceivedQty += detail.getReceivedQty();
			receivedItems++;
			totalReceivedAmount += detail.getGrnItemAmount();
			detail.setSeqNo(++seqNo);
		}

		totReceivedAmntField.setText(String.format("%.2f",totalReceivedAmount));
		totReceivedItemsField.setText(String.valueOf(receivedItems));
		totReceviedQtyField.setText(String.format("%.2f",totalReceivedQty));
		
		
	}

	// clear text fields
	@FXML
	void clearTextFields() {
		itemCodeField.clear();
		itemDescField.getEditor().clear();
		costPriceField.clear();
		receivedQtyField.clear();
		itemDescField.getEditor().requestFocus();
	}

	// get product entity from product description gotten from combo and populate
	// other
	// data to the other text box such as price,etc...

	private void onPrdDescComboAction() {

		itemDescField.getEditor().setOnKeyReleased(e -> {

			if (e.getCode() == KeyCode.ENTER) {

				if (itemDescField.getEditor().getText().isEmpty()) {
					return;
				} else {
					try {
						String prd = itemDescField.getEditor().getText();
						for (OrderItems ordDet : purchaseOrderDetailList) {
							if (prd.equals(ordDet.getItemDesc())) {
								itemCodeField.setText(ordDet.getItemCode());
								itemDescField.getEditor().setText(ordDet.getItemDesc());
								costPriceField.setText(String.valueOf(ordDet.getPrductCost()));
								deliveredQtyField.setText(String.valueOf(ordDet.getDeliveredQty()));
								orderedQtyField.setText(String.valueOf(ordDet.getOrderQty()));
								receivedQtyField.requestFocus();
								temporyOrderItem = ordDet;
								receivedQtyField.requestFocus();
								break;
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

	// codeField enter with the code request focus to QtyField while gathering
	// loded data
	@FXML
	public boolean itemCodeFieldEnter() {
		int getTexfieldItemCode = Integer.parseInt(itemCodeField.getText());

		boolean check = false;

		for (OrderItems getMe : purchaseOrderDetailList) {

			if (Integer.parseInt(getMe.getItemCode()) == getTexfieldItemCode) {
				itemDescField.getEditor().setText(getMe.getItemDesc());
				costPriceField.setText(String.valueOf(getMe.getPrductCost()));
				deliveredQtyField.setText(String.valueOf(getMe.getDeliveredQty()));
				orderedQtyField.setText(String.valueOf(getMe.getOrderQty()));
				receivedQtyField.requestFocus();
				temporyOrderItem = getMe;
				check = true;
				break;
			}

		}
		if (!check) {
			AlertHandler.getAlert(AlertType.ERROR, "Item Not Found in the Purchase Order", null);
		}
		return check;
	}

	private void initGrnTableViewColumns() {
		descCol.setCellValueFactory(
				(TableColumn.CellDataFeatures<GoodReceivedDetails, String> data) -> new SimpleStringProperty(
						data.getValue().getProduct().getP_name()));
		itemCodeCol.setCellValueFactory(
				(TableColumn.CellDataFeatures<GoodReceivedDetails, String> data) -> new SimpleStringProperty(
						String.valueOf(data.getValue().getProduct().getPrd_id())));
		snoCol.setCellValueFactory(new PropertyValueFactory<>("seqNo"));
		orderedQtyCol.setCellValueFactory(new PropertyValueFactory<>("orderedQty"));
		receivedQtyCol.setCellValueFactory(new PropertyValueFactory<>("receivedQty"));
		costPriceCol.setCellValueFactory(new PropertyValueFactory<>("costPrice"));
		amountCol.setCellValueFactory(new PropertyValueFactory<>("grnItemAmount"));

		grnTable.setEditable(true);
		receivedQtyCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
	}

	
	
	//get ordernumber from getOrdercontroller class tableview
	public void setOrderNoField(String orderNumber) {
		
		orderNoField.setText(orderNumber);
		orderNoField.requestFocus();
		
	}
	
	
	
	// this method is call when type purchase order no in orderNofield and enter
	// Purchase order details will be loaded
	// to GRN list
	// along with the optimise bind autocomletion textbox
	@FXML
	private void loadPurchaseOrderDetails() {
		itemDescField.setEditable(false);
		itemCodeField.setEditable(false);

		try {

			orderNoField.setOnAction(e -> {
				// this 3 will be called on clearALlField Method
				// purchaseOrderDetailList.clear();
				// itemDescField.getItems().clear();
				// provider.clearSuggestions();
				// suggestions.clear();
				
				clearAllExpectOrderField();
				
				
				if (textFieldsAutoComplet != null) {
					textFieldsAutoComplet.dispose();
				}
				
				try {
					
				
				long orderNo = Long.parseLong(orderNoField.getText());

				purchaseOrderDetailList = purchaseOrderDao.getOrderDetailsById(orderNo);

				if (purchaseOrderDetailList.isEmpty()) {
					AlertHandler.getAlert(AlertType.ERROR, "This PO Number is Not Valid", null);
				} else {
					supplierLabel.setText(purchaseOrderDetailList.get(0).getOrder().getSupplier().getCom_name());
					orderExpField.setText(purchaseOrderDetailList.get(0).getOrder().getExpireDate().toString());
					orderDateField.setText(purchaseOrderDetailList.get(0).getOrder().getDate().toString());
					java.sql.Date date = purchaseOrderDetailList.get(0).getOrder().getExpireDate();
					if(Date.valueOf(LocalDate.now()).after(date)) {
						AlertHandler.getAlert(AlertType.WARNING, "Order Expiry Error", "This Order has been expired pl renew to proceed");
						renewDaysField.setDisable(false);
						renewOrderButton.setDisable(false);
						renewDaysField.requestFocus();
						
						
					} else {
						double orderedQty = 0;
						int orderedItems = 0;
						double orderedAmount = 0;
						
						for (OrderItems ordDet : purchaseOrderDetailList) {
							itemDescField.getItems().add(ordDet.getItemDesc());
							suggestions.add(ordDet.getItemDesc());

							orderedQty += ordDet.getOrderQty();
							orderedItems++;
							orderedAmount += ordDet.getItemAmount();

						}
						renewDaysField.setDisable(true);
						renewOrderButton.setDisable(true);
						
						totOrderedAmountFiled.setText(String.valueOf(orderedAmount));
						totOrderedItemsField.setText(String.valueOf(orderedItems));
						totOrderedQtyField.setText(String.valueOf(orderedQty));
						itemCodeField.setEditable(true);
						itemDescField.setEditable(true);
						provider.addPossibleSuggestions(suggestions);
						textFieldsAutoComplet = new AutoCompletionTextFieldBinding<>(itemDescField.getEditor(), provider);
						AlertHandler.getAlert(AlertType.INFORMATION, "GRN Data is Loaded", null);
						itemDescField.getEditor().requestFocus();
						
						
						
					
					}
					

				}
				} catch(Exception ex) {
					AlertHandler.getAlert(AlertType.ERROR, "Load Purcae Order Details Error", "Couse By"+ex);
				}
			});

		}

		catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Load Purcae Order Details Error", "Couse By");
		}

	}
	
	// to renew PO 
	public void renewOrderClicked() {
		if(ValidateInputs.validateNumbers(renewDaysField , "Renew Days Field")) {
			if(purchaseOrderDetailList.isEmpty()) {
				AlertHandler.getAlert(AlertType.ERROR, "Purchase Order Entity is Empty", "Please Load PO First");
			} else {
			
				int noOfDaysToRenew = Integer.parseInt(renewDaysField.getText());
				java.sql.Date toDate = java.sql.Date.valueOf(LocalDate.now().plusDays(noOfDaysToRenew));
				PurchaseOrder poToUpdate = purchaseOrderDetailList.get(0).getOrder();
				
				
				if(AlertHandler.getAlert(AlertType.CONFIRMATION, "Are You Sure You Want to Update", null).getResult().getButtonData().equals(ButtonData.OK_DONE)) {
					poToUpdate.setExpireDate(toDate);
					System.out.println(toDate);
					if(purchaseOrderDao.updateExpireDate(poToUpdate)) {
				Notifications.create().title("Update Success, Load Again").graphic(null).hideAfter(Duration.seconds(2)).
				darkStyle().position(Pos.CENTER).show();
				ClearAllFields();
				
					}
					
				}
				
			}
		}
		
	}
	
	

	@FXML
	void loadOrderActionClicked(ActionEvent event) throws IOException {

		if (grnTableDetailList.isEmpty()) {

			String loc = "/fxml/grn/loadOrderTable.fxml";
			FXMLLoader loader = new FXMLLoader(getClass().getResource(loc));
			Scene scene = new Scene(loader.load());
			GetOrderController passDataToGetOrderController = loader.getController();
			passDataToGetOrderController.setParentFXMLLoader(thisGrnLoader);

			OrderTableStage = new Stage(StageStyle.DECORATED);
			OrderTableStage.setScene(scene);

			OrderTableStage.show();

			OrderTableStage.setOnHidden(e -> {
				OrderTableStage = null;
			});

		}

		else {
			AlertHandler.getAlert(AlertType.WARNING, "Purchase Order Load Warning",
					"Make Sure to Save or Clear loaded GRN"+System.lineSeparator()+" before accesing another Order");
		}

	}

	@FXML
	void removeItemFromTableView(ActionEvent event) {
		GoodReceivedDetails selecteRowData = (GoodReceivedDetails) grnTable.getSelectionModel().getSelectedItem();
		if (!grnTableDetailList.isEmpty() && !grnTable.getSelectionModel().getSelectedItems().isEmpty()) {
			grnTableDetailList.remove(selecteRowData);
			refreshSummaryData();
			grnTable.setItems(grnTableDetailList);
		} else {
			AlertHandler.getAlert(AlertType.WARNING, "Data Error",
					"No data to Delete");
		}
	}

	//jasper report viewing when view order button clicked
	@FXML
	void viewOrderClicked(ActionEvent event) throws FileNotFoundException, JRException, SQLException, URISyntaxException {
		if(!purchaseOrderDetailList.isEmpty() & !orderNoField.getText().isEmpty()) {
			Connection connectionForReports = HibernateUtil.getSessionFactory().getSessionFactoryOptions().getServiceRegistry().getService(ConnectionProvider.class).getConnection();
			String loc = "resources/jasperreports/OrderReport_5.jasper";
			
			
			Map<String,Object>  parameters = new HashMap();
			parameters.put("companyName", MainController.getCompanyInfoSession().getCompanyName());
			parameters.put("streetAddress", MainController.getCompanyInfoSession().getAddressLine1());
			parameters.put("addressLine", MainController.getCompanyInfoSession().getAddressLine2());
			parameters.put("city", MainController.getCompanyInfoSession().getAddressLine3());
			parameters.put("telephoneNum", MainController.getCompanyInfoSession().getTelephoneNum());
			parameters.put("orderNo", Integer.parseInt(orderNoField.getText()));
			JasperPrint jp = JasperFillManager.fillReport(loc,parameters , connectionForReports);
			JasperViewer.viewReport(jp, false);
			
		} else {
			AlertHandler.getAlert(AlertType.ERROR, "Jasper Report Error", "Cannot Compile Report");
		}
		
		
		
	}
	
	@FXML
	void SaveGrnClicked() {
		
		if(invoiceNumField.getText().isEmpty()) {
			AlertHandler.getAlert(AlertType.ERROR, "Input Empty Error", "Please Type Invoice Number before Save data");
			invoiceNumField.requestFocus();
			}
		
		else if(grnTableDetailList.isEmpty()) {
			AlertHandler.getAlert(AlertType.ERROR, "Input Empty Error", "No Data to Save");
			
		}
		
		else {	try {
			GoodReceived grn = new GoodReceived();
			grn.setGrnAmount(totalReceivedAmount);
			grn.setGrnDate(java.sql.Date.valueOf(LocalDate.now()));
			grn.setPurchaseOrder(grnTableDetailList.get(0).getPurchaseOrder());
			grn.setSupplier(grnTableDetailList.get(0).getPurchaseOrder().getSupplier());
			grn.setPaidStatus(false);
			grn.setUser(MainController.getUserSession());
			grn.setInvoiceNum(invoiceNumField.getText());
			grn.setDocumentType("GRN");
			
			boolean mainOrderPartialDelStatus = true;
			boolean mainOrderDeliveryStatus = true;
		
			for(OrderItems orDet : purchaseOrderDetailList) {
				
				for(GoodReceivedDetails grnDet : grnTableDetailList) {
					grnDet.setGoodRecevied(grn);
					
					if(orDet.getProduct().equals(grnDet.getProduct())) {
							
							if(!orDet.isPartialDeliveryStatus()) {
								if(orDet.getOrderQty() == grnDet.getReceivedQty()) {
									orDet.setDeliveryStatus(true);
									orDet.setPartialDeliveryStatus(false);
									
							} else if(orDet.getOrderQty() > grnDet.getReceivedQty()) {
									orDet.setPartialDeliveryStatus(true);
									orDet.setDeliveryStatus(false);
									
							}
							}
							else {
								if((orDet.getOrderQty() -orDet.getDeliveredQty()) == grnDet.getReceivedQty()) {
									
									orDet.setDeliveryStatus(true);
									orDet.setPartialDeliveryStatus(false);
									
							} else if((orDet.getOrderQty() -orDet.getDeliveredQty()) > grnDet.getReceivedQty()) {
									orDet.setPartialDeliveryStatus(true);
									orDet.setDeliveryStatus(false);
									
							}
								
							}
							
							
							orDet.setDeliveredQty(orDet.getDeliveredQty()+grnDet.getReceivedQty());
							break;
					}
				}
				
				mainOrderDeliveryStatus = mainOrderDeliveryStatus && orDet.isDeliveryStatus();
								
			}
			
			if(mainOrderDeliveryStatus) {
				mainOrderPartialDelStatus = false;
			}
			
			
			if (AlertHandler.getAlert(AlertType.CONFIRMATION, "Are your sure you want save this GRN", null)
					.getResult().getButtonData().equals(ButtonData.OK_DONE)) {
				if (goodReceivedDao.saveGrn(grn, grnTableDetailList ,purchaseOrderDetailList ,mainOrderPartialDelStatus,mainOrderDeliveryStatus)) {
					Notifications.create().title("Save Success").graphic(null).hideAfter(Duration.seconds(2))
							.darkStyle().position(Pos.CENTER).show();
					ClearAllFields();
					viewReport(grn.getGrnId());

				}

			}
			
			
		}catch(Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Somthing Went Wrong", e.getMessage());
			e.printStackTrace();
		}
	}
	}
	
	
	// view GRN Report
		public void viewReport(long grnNo) throws JRException, IOException, SQLException {

						
				String loc= "resources/jasperreports/grnInquiry.jasper";
				
				Connection connectionForReports = HibernateUtil.getSessionFactory().getSessionFactoryOptions()
						.getServiceRegistry().getService(ConnectionProvider.class).getConnection();

				Map<String, Object> parameters = new HashMap();
				parameters.put("companyName", MainController.getCompanyInfoSession().getCompanyName());
				parameters.put("streetAddress", MainController.getCompanyInfoSession().getAddressLine1());
				parameters.put("addressLine", MainController.getCompanyInfoSession().getAddressLine2());
				parameters.put("city", MainController.getCompanyInfoSession().getAddressLine3());
				parameters.put("telephoneNum", MainController.getCompanyInfoSession().getTelephoneNum());
				parameters.put("grnNo", grnNo);
				parameters.put("documentOriginalStatus", "Original");
				parameters.put("printedBy", MainController.getUserSession().getUserName());

				JasperPrint jp = JasperFillManager.fillReport(loc, parameters, connectionForReports);

				ByteArrayOutputStream output = new ByteArrayOutputStream();
				JasperExportManager.exportReportToPdfStream(jp, output);
				final String PDF_FILE = "resources/pdfreports/grn.pdf";
				OutputStream pdfFile = new FileOutputStream(new File(PDF_FILE));
				pdfFile.write(output.toByteArray());
				pdfFile.flush();
				pdfFile.close();
				Desktop.getDesktop().open(new File(PDF_FILE));
			
		}

		
		// Added on 13th May 2023 to Load All GRN data to table view at once and save at once
		// because of the client request
		 public void loadAllOrder() {
			 try {
				 if(!grnTableDetailList.isEmpty()) {
					 grnTableDetailList.clear();
				 }
				 for (OrderItems getMe : purchaseOrderDetailList) {

					 	GoodReceivedDetails grn = new GoodReceivedDetails();
						grn.setProduct(getMe.getProduct());
						grn.setPurchaseOrder(getMe.getOrder());
						grn.setCostPrice(getMe.getPrductCost());
						grn.setGrnItemAmount(getMe.getOrderQty() * getMe.getPrductCost());
						if(!getMe.isPartialDeliveryStatus()) {
							grn.setOrderedQty(getMe.getOrderQty());
						}
						else {
							grn.setOrderedQty(getMe.getOrderQty()-getMe.getDeliveredQty());
						}
						
						grn.setReceivedQty(getMe.getOrderQty());
						
						// seq no will be added in refreshSummaryData(); method
						//grn.setSeqNo(++seqNo);
						grn.setUnitPrice(getMe.getProduct().getUnit_price());
						grnTableDetailList.add(grn);
						
						}	
						grnTable.setItems(grnTableDetailList);

						refreshSummaryData();
						itemCodeField.clear();
						itemDescField.getEditor().clear();
						costPriceField.clear();
						receivedQtyField.clear();
						orderedQtyField.clear();
						deliveredQtyField.clear();
						itemDescField.getEditor().requestFocus();
					

				} catch (Exception e) {
					AlertHandler.getAlert(AlertType.ERROR, "Error Loading Data", "Contact Administrator");
					e.printStackTrace();
				}

		  }

}
