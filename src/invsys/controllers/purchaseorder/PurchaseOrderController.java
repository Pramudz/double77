package invsys.controllers.purchaseorder;

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
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import org.controlsfx.control.Notifications;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

import com.jfoenix.controls.JFXTextField;

import invsys.controllers.database.HibernateUtil;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.controllers.formvalidation.ValidateInputs;
import invsys.controllers.mainpage.MainController;
import invsys.entities.OrderItems;
import invsys.entities.Products;
import invsys.entities.PurchaseOrder;
import invsys.entities.Supplier;
import invsys.entities.Users;
import invsys.entitiydao.ProductDao;
import invsys.entitiydao.PurchaseOrderDao;
import invsys.entitiydao.SupplierDao;
import invsys.entitiydao.impl.ProductDaoImpl;
import invsys.entitiydao.impl.PurchaseOrderDaoImpl;
import invsys.entitiydao.impl.SupplierDaoImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

public class PurchaseOrderController implements Initializable {

	ObservableList<Products> productList = FXCollections.observableArrayList();
	ObservableList<OrderItems> orderItemList = FXCollections.observableArrayList();
	FilteredList<Products> filteredData = new FilteredList<>(productList, e -> true);
	SortedList<Products> sList = new SortedList<Products>(filteredData);
	
	@FXML
	private Label supDescLabel;

	@FXML
	private JFXTextField supCodeField;

	@FXML
	private JFXTextField itemCodeField;

	@FXML
	private JFXTextField itemDescField;

	@FXML
	private JFXTextField qtyField;
	@FXML
	private JFXTextField thisSearchTextField;
	@FXML
	private JFXTextField priceField;

	// inorder for show the no of items,qty & summ of value of order items
	@FXML
	private TextField itemCountField;

	@FXML
	private TextField sumOfQtyField;

	@FXML
	private TextField stretAddressField;
	
	@FXML
	private TextField addressField;
	
	@FXML
	private TextField cityField;
	
	@FXML
	private CheckBox defaultAddressCheckBox;
	@FXML
	private TextField sumOfValueField;
	
	@FXML
	private DatePicker expiryDatePicker;

	@FXML
	private FXMLLoader loadPurchaseOrder;

	// Controllers variables for loadDatitems table view
	@FXML
	private TableColumn<Products, Integer> loadItemCodeCol;

	@FXML
	private TableColumn<Products, String> loadItemDescCol;
	@FXML
	private TableColumn<Products, Double> loadOnHQtry;
	@FXML
	private TableColumn<Products, Double> loadPackSizeCol;
	@FXML
	private TableColumn<Products, Double> loadCostPriceCol;
	@FXML
	private TableColumn<Products, String> loadActionCol;
	@FXML
	TableView loadTableView;

	// Controllers variables for Order Data table view
	@FXML
	TableView orderTableView;

	@FXML
	private TableColumn<OrderItems, String> orderITemCodeCol;

	@FXML
	private TableColumn<OrderItems, String> orderItemDescCol;

	@FXML
	private TableColumn<OrderItems, Double> orderQtyCol;

	@FXML
	private TableColumn<OrderItems, Double> orderPriceCol;

	@FXML
	private TableColumn<OrderItems, Double> orderValueCol;

	private Stage currentStage;

	// in order to prevent from opening more than one stage for supplier getting
	// table
	private Stage supplierStage;

	// supplier Instance for get relevant supplier related data to the Item
	// Table



	// in order to track down supplier entity from supplier load table
	private Supplier supplierEntity;

	// this Main product entity value is assigned to the add button clicked method
	Products prd = null;
	

	
	// to track seq no of items
		int seqNo = 0;
		
		
	//dao handler/Classes
	ProductDao productDao = null;
	PurchaseOrderDao purchaseOrderDao = null;
	SupplierDao supplierDao = null;

	// Initialize method
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// DBHandler.getInstance();

		supplierDao = SupplierDaoImpl.getDao();

		productDao = ProductDaoImpl.getDao();
		purchaseOrderDao = PurchaseOrderDaoImpl.getDao();
		ValidateInputs.getInstance();

		itemDescField.setDisable(true);
		priceField.setDisable(true);
		initColloadItemTable();
		initColumnsForOrderData();
		setDateFormatter();
		
		defaultAddressCheckBox.setOnAction(e->{
			if(defaultAddressCheckBox.isSelected()) {
								
				stretAddressField.setText(MainController.getCompanyInfoSession().getAddressLine1());
				addressField.setText(MainController.getCompanyInfoSession().getAddressLine2());
				cityField.setText(MainController.getCompanyInfoSession().getAddressLine3());
				
				stretAddressField.setDisable(true);
				addressField.setDisable(true);
				cityField.setDisable(true);
			}
			
			else {
				stretAddressField.setDisable(false);
				addressField.setDisable(false);
				cityField.setDisable(false);
				
				stretAddressField.clear();
				addressField.clear();
				cityField.clear();
			}
		});
	}

	// set Loaded stage to this
	public void setCurrentStage(Stage stage) {
		this.currentStage = stage;
	}

	// set supplier entity globally from supplier load table
	// in order to keep supplier entity for saving database
	public void setSupplierEntity(Supplier sup) {
		this.supplierEntity = sup;
	}

	
	// set product entity from Products class throut the table view action button clicked
	public void setProductFromTableView() {
		
	}
	
	// load supplier table in order to select appropriate supplier
	public void loadSupplierTable() throws IOException {

		if (orderItemList.isEmpty()) {
			if (supplierStage == null) {
				String loc = "/fxml/purchase/SupplierTable.fxml";
				FXMLLoader loader = new FXMLLoader(getClass().getResource(loc));
				Scene scene = new Scene(loader.load());
				GetSupplierController passLoadertoSupplierTable = loader.getController();
				passLoadertoSupplierTable.setLoader(loadPurchaseOrder);

				supplierStage = new Stage(StageStyle.DECORATED);
				supplierStage.setScene(scene);

				supplierStage.show();
			} else if (supplierStage.isShowing()) {
				supplierStage.toFront();

			} else {
				supplierStage.show();
			}

		} else {
			AlertHandler.getAlert(AlertType.WARNING, "Supplier Details Cannot be Loaded untill Order is Finalized",
					"Make Sure that Order data is Finalized or Clear before you load another supplier");
		}
		clearTextFields();

	}

	public JFXTextField setQtyField() {
		return this.qtyField;
	}

	// initialize column for load item table
	public void initColloadItemTable() {
		loadItemCodeCol.setCellValueFactory(new PropertyValueFactory<>("prd_id"));
		loadItemDescCol.setCellValueFactory(new PropertyValueFactory<>("p_name"));
		loadOnHQtry.setCellValueFactory(new PropertyValueFactory<>("onHandQty"));
		loadCostPriceCol.setCellValueFactory(new PropertyValueFactory<>("unit_cost_price"));
		loadActionCol.setCellValueFactory(new PropertyValueFactory<>("addButton"));
		loadPackSizeCol.setCellValueFactory(new PropertyValueFactory<>("pack_size"));
		loadTableView.setEditable(true);

	}

	// initialize columns for orderData table view
	private void initColumnsForOrderData() {
		orderITemCodeCol.setCellValueFactory(
				(TableColumn.CellDataFeatures<OrderItems, String> data) -> new SimpleStringProperty(
						String.valueOf(data.getValue().getProduct().getPrd_id())));
		orderItemDescCol.setCellValueFactory(
				(TableColumn.CellDataFeatures<OrderItems, String> data) -> new SimpleStringProperty(
						data.getValue().getProduct().getP_name()));
		orderQtyCol.setCellValueFactory(new PropertyValueFactory<>("orderQty"));
		orderPriceCol.setCellValueFactory(new PropertyValueFactory<>("prductCost"));
		orderValueCol.setCellValueFactory(new PropertyValueFactory<>("itemAmount"));
		orderTableView.setEditable(true);

		orderQtyCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
	}

	// set supplier description label from the supplier table window to this
	// window
	public Label setSuplierDescription() {
		return supDescLabel;
	}

	// set supplier code from the supplier table window to this
	// window
	public JFXTextField setSupplierCode() {
		return supCodeField;
	}

	// getpurchaseOrder window loader in order set data from supplier table
	public void setOrderLoader(FXMLLoader loader) {
		this.loadPurchaseOrder = loader;
	}

	// load item Table to item

	public void loadItemTable() throws SQLException {
		productList.clear();
		loadTableView.setItems(productList);

		if (ValidateInputs.validateNumbers(supCodeField , "Supplier Code Field")) {

			supplierEntity = supplierDao.getSupplierById(Integer.parseInt(supCodeField.getText()));
			
			if(supplierEntity != null) {
				paginatedItemTableViewForSupplierWise(supplierEntity);
			}
			

		}

	}

	// filter data according to the key pressed in the textfield
	public void searchItems(KeyEvent event) {

		if (event.getCode() == KeyCode.DOWN) {
			loadTableView.requestFocus();
		}
		thisSearchTextField.textProperty().addListener((ObservableValue, oldValue, newValue) -> {
			filteredData.setPredicate((Predicate<? super Products>) LoadData -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				if (String.valueOf(LoadData.getPrd_id()).toLowerCase().contains(lowerCaseFilter)) {
					return true;
				} else if (LoadData.getP_name().toLowerCase().contains(lowerCaseFilter)) {
					return true;
				}

				return false;
			});
		});

		sList = new SortedList<>(filteredData);

		sList.comparatorProperty().bind(loadTableView.comparatorProperty());
		loadTableView.setItems(sList);
	}

	// codeField enter with the code request focus to QtyField while gathering
	// loded data
	public Products codeFieldEnter() {
		int getTexfieldItemCode = Integer.parseInt(itemCodeField.getText());

		boolean check = false;

		for (Products getMe : productList) {

			if (getMe.getPrd_id() == getTexfieldItemCode) {
				itemDescField.setText(getMe.getP_name());
				priceField.setText(String.valueOf(getMe.getUnit_cost_price()));
				qtyField.requestFocus();
				check = true;
				prd = getMe;
				break;
			}

		}
		if (!check) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText("No Items Found");
			alert.showAndWait();
		}
		return prd;
	}
	
	
	

	

	// Add button clicked while getting data
	public void addButtonClicked() {
		try {
			
			if(prd.getUnitOfMeasure().equals("unit")) {
				boolean check = ValidateInputs.validateQtyForUnitItems(qtyField , "Quantity Field");
				if (!check)
					return;
			}
			double qty = Double.parseDouble(qtyField.getText());
			// String itemCode = itemCodeField.getText().toLowerCase();
			// String itemDesc = itemDescField.getText();
			double price = Double.valueOf(priceField.getText());
			
			double unitizeCheck = qty%this.prd.getPack_size();
			
			if(unitizeCheck != 0) {
				AlertHandler.getAlert(AlertType.ERROR, "Unitize Error", "Order Quantity Should be Multiple of Pack Size");
				return;
			}
			
			double amount = qty * price;
			for(OrderItems x : orderItemList) {
				if(x.getProduct().getPrd_id() == prd.getPrd_id()) {
					AlertHandler.getAlert(AlertType.WARNING, "Duplicate Entry : You Have already Entered this Item",
							"if you want to modify you can edit it using table view");
					clearTextFields();
					return;
				}
			}
			OrderItems orderItems = new OrderItems();
			orderItems.setProduct(prd);
			orderItems.setOrderQty(qty);
			orderItems.setPrductCost(price);
			orderItems.setItemAmount(amount);
			orderItems.setSeQNo(seqNo);
			orderItems.setPartialDeliveryStatus(false);
			orderItems.setItemVat(prd.getVat());
			orderItems.setDeliveryStatus(false);
			orderItemList.add(orderItems);

			clearTextFields();
			itemCodeField.requestFocus();

			seqNo++;
			refreshOrdertable();

		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Error Inserting Data", null);
			e.printStackTrace();
		}

	}
	
	
	// Add button clicked From TablieView getting data
		public void addButtonClickedFromTableView(double qty, double price, Products product) {
			try {
								
				this.prd = product;
				
				if(prd.getUnitOfMeasure().equals("unit")) {
					String checkValue = String.valueOf(qty);
					checkValue = checkValue.contains(".") ? checkValue.replaceAll("0*$","").replaceAll("\\.$","") : checkValue;
					boolean check = ValidateInputs.validateQtyForUnitItemsWithString(checkValue , "Quantity Field");
					if(!check) {
						return;
					}
					
				}
				
				double unitizeCheck = qty%this.prd.getPack_size();
				
				if(unitizeCheck != 0) {
					AlertHandler.getAlert(AlertType.ERROR, "Unitize Error", "Order Quantity Should be Multiple of Pack Size");
					return;
				}
				
				double amount = qty * price;
				for(OrderItems x : orderItemList) {
					if(x.getProduct().getPrd_id() == prd.getPrd_id()) {
						AlertHandler.getAlert(AlertType.WARNING, "Duplicate Entry : You Have already Entered this Item",
								"if you want to modify you can edit it using table view");
						clearTextFields();
						return;
					}
				}
				OrderItems orderItems = new OrderItems();
				orderItems.setProduct(prd);
				orderItems.setOrderQty(qty);
				orderItems.setPrductCost(price);
				orderItems.setItemAmount(amount);
				orderItems.setSeQNo(seqNo);
				orderItems.setPartialDeliveryStatus(false);
				orderItems.setItemVat(prd.getVat());
				orderItems.setDeliveryStatus(false);
				orderItemList.add(orderItems);

				clearTextFields();
				itemCodeField.requestFocus();

				seqNo++;
				refreshOrdertable();

			} catch (Exception e) {
				AlertHandler.getAlert(AlertType.ERROR, "Error Inserting Data", null);
				e.printStackTrace();
			}

		}

	// in order addbutton clicked method handling in the product class
	public ObservableList<Products> getLoadItemList() {
		return productList;
	}

	public void refreshOrdertable() {
		orderTableView.getItems().setAll(orderItemList);
		int noOfItems = 0;
		double sumOfAmount = 0;
		double sumOfqty = 0;
		// Object[] getOrderData = orderItemList.toArray();
		for (OrderItems iter : orderItemList) {
			sumOfAmount += iter.getItemAmount();
			sumOfqty += iter.getOrderQty();
			noOfItems++;
		}
		itemCountField.setText(String.valueOf(noOfItems));
		sumOfQtyField.setText(String.format("%.2f",sumOfqty));
		sumOfValueField.setText(String.format("%.2f",sumOfAmount));

	}

	// clear textFields in
	private void clearTextFields() {
		qtyField.clear();
		itemCodeField.clear();
		itemDescField.clear();
		priceField.clear();
	}

	// clear all data in two tables
	public void clearData() {
		
		supCodeField.clear();
		supDescLabel.setText("");
		supplierEntity = null;
		expiryDatePicker.getEditor().clear();
		clearTextFields();
		productList.clear();
		orderItemList.clear();
		loadTableView.getItems().clear();
		refreshOrdertable();
		sumOfQtyField.clear();
		sumOfValueField.clear();
		itemCountField.clear();

	}

	// close existing window
	public void closeWindow() {

	}

	// Cell edit event on order table qty populate data throuhout the list
	public void qtyCellClicke(CellEditEvent event) {
		OrderItems tempData;

		tempData = (OrderItems) orderTableView.getSelectionModel().getSelectedItem();

		if (orderItemList.isEmpty()) {
			System.out.println("No Data to Edit");
		} else {
			
			if(tempData.getProduct().getUnitOfMeasure().equals("unit")) {
				String checkValue = event.getNewValue().toString();
				checkValue = checkValue.contains(".") ? checkValue.replaceAll("0*$","").replaceAll("\\.$","") : checkValue;
				boolean check = ValidateInputs.validateQtyForUnitItemsWithString(checkValue ,"Quantity Field");
				if(!check) {
					event.getTableView().getItems().set(event.getTablePosition().getRow(), tempData);
					return;
				}
				
			}
			
			
			double qty = Double.parseDouble(event.getNewValue().toString());
			
			double unitizeCheck = qty%tempData.getProduct().getPack_size();
			
			if(unitizeCheck != 0) {
				AlertHandler.getAlert(AlertType.ERROR, "Unitize Error", "Order Quantity Should be Multiple of Pack Size");
				event.getTableView().getItems().set(event.getTablePosition().getRow(), tempData);
				return;
			}
			tempData.setOrderQty(qty);
			tempData.setItemAmount(qty * tempData.getPrductCost());
			refreshOrdertable();
		}
	}

	@FXML
	public void removeItemFromOrderAction() {
		OrderItems selecteRowData = (OrderItems) orderTableView.getSelectionModel().getSelectedItem();
		if (!orderItemList.isEmpty() && !orderTableView.getSelectionModel().getSelectedItems().isEmpty()) {
			orderItemList.remove(selecteRowData);
			seqNo--;
			refreshOrdertable();
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(null);
			alert.setHeaderText("No data to Delete");
		}
	}

	// view Report
	public void viewReport(long orderNo) throws JRException, IOException, SQLException {

		// JasperDesign jasperDesign =
		// JRXmlLoader.load("src/pharmacySystem/resources/Simple_Blue_Table_Based.jrxml");
		// String sql = "select * from itemmaster";
		// JRDesignQuery newQuery = new JRDesignQuery();
		// newQuery.setText(sql);
		// jasperDesign.setQuery(newQuery);

		// JasperReport jasperRep =
		// JasperCompileManager.compileReport(jasperDesign);
		// JasperPrint printRepo = JasperFillManager.fillReport(jasperRep, null,
		// DBHandler.getConnection());
		// JasperViewer.viewReport(printRepo, false);
			
			String supplierName = supDescLabel.getText();
			java.sql.Date orderDate = java.sql.Date.valueOf(LocalDate.now());

			String loc= "resources/jasperreports/OrderReport_5.jasper";
			
			Connection connectionForReports = HibernateUtil.getSessionFactory().getSessionFactoryOptions()
					.getServiceRegistry().getService(ConnectionProvider.class).getConnection();

			//JRDataSource dataSource = new JRBeanCollectionDataSource(orderItemList);
			Map<String, Object> parameters = new HashMap();
			parameters.put("companyName", MainController.getCompanyInfoSession().getCompanyName());
			parameters.put("streetAddress", MainController.getCompanyInfoSession().getAddressLine1());
			parameters.put("addressLine", MainController.getCompanyInfoSession().getAddressLine2());
			parameters.put("city", MainController.getCompanyInfoSession().getAddressLine3());
			parameters.put("telephoneNum", MainController.getCompanyInfoSession().getTelephoneNum());
			parameters.put("supplierName", supplierName);
			parameters.put("orderDate", orderDate);
			parameters.put("orderNo", orderNo);
			parameters.put("printedBy", MainController.getUserSession().getUserName());

			JasperPrint jp = JasperFillManager.fillReport(loc, parameters, connectionForReports);

			// JasperViewer.viewReport(jp, false);
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			JasperExportManager.exportReportToPdfStream(jp, output);
			final String PDF_FILE = "resources/pdfreports/order.pdf";
			OutputStream pdfFile = new FileOutputStream(new File(PDF_FILE));
			pdfFile.write(output.toByteArray());
			pdfFile.flush();
			pdfFile.close();
			Desktop.getDesktop().open(new File(PDF_FILE));
		
	}

	public void savePurchaseOrderClicked() {
		try {
			
			if(ValidateInputs.validateDateWithFutureDays(expiryDatePicker , "Order Expiry Date Field",7) && 
					ValidateInputs.validateInputEmpty(stretAddressField , "Street Address Field") &&
					ValidateInputs.validateInputEmpty(addressField , "Address Field")
					&& ValidateInputs.validateInputEmpty(cityField, "City Field")) {
				
			
			PurchaseOrder order = new PurchaseOrder();
			order.setDate(java.sql.Date.valueOf(LocalDate.now()));
			order.setDelDate(null);
			order.setDeliverStatus(false);
			order.setPartialStatus(false);
			order.setShipStreetAddress(stretAddressField.getText());
			order.setShipAddressLine01(addressField.getText());
			order.setShipCity(cityField.getText());
			order.setOrderAmount(Double.parseDouble(sumOfValueField.getText()));
			order.setExpireDate(java.sql.Date.valueOf(expiryDatePicker.getEditor().getText()));
			Users user = MainController.getUserSession();
			if (user == null || supplierEntity == null) {
				AlertHandler.getAlert(AlertType.ERROR, "User or Supplier Entity is Null", "Could not Save the Data");
			} else {
				order.setUser(user);
				order.setSupplier(supplierEntity);
				// for Temporality in order to track grns
				order.setApprovedBy(user);
				//
				orderItemList.forEach(e -> {
					e.setOrder(order);
				});

				if (AlertHandler.getAlert(AlertType.CONFIRMATION, "Are your sure you want create this Order", null)
						.getResult().getButtonData().equals(ButtonData.OK_DONE)) {
					if (purchaseOrderDao.saveOrder(order, orderItemList)) {
						Notifications.create().title("Save Success").graphic(null).hideAfter(Duration.seconds(2))
								.darkStyle().position(Pos.CENTER).show();
						viewReport(order.getOrderId());
						clearData();

					}

				}

			}
			
		}

		}
		
		
		catch (Exception e) {
			if (e instanceof IOException || e instanceof JRException) {
				clearData();
				AlertHandler.getAlert(AlertType.ERROR, "Could not Print the Data", e.getLocalizedMessage());
				e.printStackTrace();
			}
			else {
				AlertHandler.getAlert(AlertType.ERROR, "Cannot save Data Somthing Went Wrong", e.getLocalizedMessage());
				e.printStackTrace();
			}
			
		}

	}
	
	// date formatter for date picker of Expiry Date Picker
		public void setDateFormatter() {

			// Converter
			StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
				DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

				@Override
				public String toString(LocalDate date) {
					if (date != null) {
						return dateFormatter.format(date);
					} else {
						return "";
					}
				}

				@Override
				public LocalDate fromString(String string) {
					if (string != null && !string.isEmpty()) {
						return LocalDate.parse(string, dateFormatter);
					} else {
						return null;
					}
				}
			};

			expiryDatePicker.setConverter(converter);

		}

	// in case of key event handling this method can be used while validating
	// data

	public void keyEventHandling(KeyEvent event) {
		if (!orderItemList.isEmpty() && orderTableView.isFocused() && !orderTableView.getSelectionModel().isEmpty()) {
			if (event.getCode() == KeyCode.DELETE) {
				removeItemFromOrderAction();
			}
		}

	}
	
	
	//pagination idexes;
		int pageResult = 50;
		int firstResult = 0;
		long maxRecord = 0;
		int nextResult = pageResult;
		int prevResult = 0;
		
	
	//hbox for pagination buttons
		@FXML
		private HBox hboxPagination;

		
		//pagination method amended some instance variable in order to match with purchase order entity
		public void paginatedItemTableViewForSupplierWise(Supplier sup) {
			
			//amended instance variable names
			//loadTableView , filteredData
			
			
			hboxPagination.getChildren().clear();
			pageResult = 50;
			firstResult = 0;
			maxRecord = 0;
			nextResult = pageResult;
			prevResult = 0;
			filteredData.clear();
			sList.clear();
			productList.clear();
			loadTableView.setItems(productList);
			HashMap<Long, List<Products>> map = productDao.getPrdListSupplierWiseWithPagination(sup,0, pageResult);
			int count = 1;
			
			if(map == null) {
				AlertHandler.getAlert(AlertType.ERROR, "No Data Found", null);
				return;
			}
			for (Entry<Long, List<Products>> x : map.entrySet()) {
				//initializing next and previous buttons
				Button nextButton = new Button("Next");
				Button prevButton = new Button("Prev");
				prevButton.setDisable(true);
				
				//next button setOnAction action
				nextButton.setOnAction(e -> {

					HashMap<Long, List<Products>> maping = productDao.getPrdListSupplierWiseWithPagination(sup,nextResult, pageResult);
					for (Entry<Long, List<Products>> y : maping.entrySet()) {
						System.out.println();
						System.out.println(pageResult);
						productList.clear();
						//productList.addAll(y.getValue());
						//above method removed and added below method to match with Purchase order Table View
						for (Products tmpPrd : y.getValue()) {
							Products loadProducts = new Products(tmpPrd.getPrd_id(), tmpPrd.getP_name(), tmpPrd.getOnHandQty(),tmpPrd.getPack_size(), tmpPrd.getUnit_cost_price(), tmpPrd.getUnitOfMeasure());
							productList.add(loadProducts);
						}
							
						loadTableView.setItems(productList);
						prevResult = nextResult - pageResult;
						nextResult = nextResult + pageResult;

						if (nextResult >= firstResult) {
							nextButton.setDisable(true);
						} else {
							nextButton.setDisable(false);
						}
						
						if (prevResult < 0) {
							prevButton.setDisable(true);
						} else {
							prevButton.setDisable(false);
						}
						maping = null;
						break;

					}

				});

				
				//Prev button setOnAction action
				prevButton.setOnAction(e -> {
					HashMap<Long, List<Products>> maping = productDao.getPrdListSupplierWiseWithPagination(sup, prevResult, pageResult);
					for (Entry<Long, List<Products>> y : maping.entrySet()) {
						System.out.println(pageResult);
						productList.clear();
						//productList.addAll(y.getValue());
						//above method removed and added below method to match with Purchase order Table View
						for (Products tmpPrd : y.getValue()) {
							Products loadProducts = new Products(tmpPrd.getPrd_id(), tmpPrd.getP_name(), tmpPrd.getOnHandQty(), tmpPrd.getPack_size() ,tmpPrd.getUnit_cost_price(), tmpPrd.getUnitOfMeasure());
							productList.add(loadProducts);
						}
						loadTableView.setItems(productList);
						nextResult = prevResult + pageResult;
						prevResult = prevResult - pageResult;
						
						if (nextResult >= firstResult) {
							nextButton.setDisable(true);
						} else {
							nextButton.setDisable(false);
						}
						if (prevResult < 0) {
							prevButton.setDisable(true);
						} else {
							prevButton.setDisable(false);
						}
						break;

					}
				});

				//initially adding nextButton to the HBOX pagination
				hboxPagination.getChildren().add(prevButton);

				
				//clear all vales of product list and itemtableview
				//productList.addAll(x.getValue());
				//above method removed and added below method to match with Purchase order Table View
				for (Products tmpPrd : x.getValue()) {
					Products loadProducts = new Products(tmpPrd.getPrd_id(), tmpPrd.getP_name(), tmpPrd.getOnHandQty(),tmpPrd.getPack_size(), tmpPrd.getUnit_cost_price(), tmpPrd.getUnitOfMeasure());
					productList.add(loadProducts);
				}
				
				loadTableView.setItems(productList);
				
				//get no of record from hashmap where hasMap key will be returned as the No of Record from the ProductDaoImpl
				// using two database calls only from the initial database call;
				maxRecord = x.getKey();
				
				if(maxRecord <= pageResult) {
					nextButton.setDisable(true);
				}
				else {
					nextButton.setDisable(false);
				}
				
				
				//no of Buttons required for pagination result will be required no of Buttons 1-many
				int noOfBtns = (int) ((maxRecord + pageResult - 1) / pageResult);
				
				//iterate each and every noOfbuttons 
				for (int i = 1; i <= noOfBtns; i++) {
					
					//Initialization of pagination buttons
					Button btn = new Button(String.valueOf(i));
					
					//setting button id of each and every button where button id value will be first result of the particular database call
					//example : HashMap<Long, List<Products>> maping = ProductDaoImpl.paginationWithTableView(Integer.parseInt(btn.getId()), pageResult);
								
					btn.setId(String.valueOf(firstResult));
					firstResult = firstResult + pageResult;
					
					//pagination button setonAction method for each and every button
					btn.setOnAction(e -> {
						
						//iterate each and every buttons in the Hbox where and Hide buttons appropriately where button
						// value greater than/ or less than  + or - (specific nof buttons to show) 
						//in order to hide
						for (Node b : hboxPagination.getChildren()) {
							Button reviseButton = (Button) b;

							int currentButton = Integer.parseInt(btn.getText());
							if (!reviseButton.getText().equals("Prev") && !reviseButton.getText().equals("Next")
									&& !reviseButton.getText().equals("1")
									&& !reviseButton.getText().equals("" + noOfBtns + "")) {
								if (Integer.parseInt(reviseButton.getText()) < (currentButton - 2)
										|| Integer.parseInt(reviseButton.getText()) > (currentButton + 2)) {
									reviseButton.setVisible(false);
									reviseButton.setManaged(false);
								}

								else if (Integer.parseInt(reviseButton.getText()) < (currentButton + 3)) {
									reviseButton.setVisible(true);
									reviseButton.setManaged(true);
								}
							}

						}

						
						//finally get result from the dao class and set next Result and previous result appropriately 
						HashMap<Long, List<Products>> maping = productDao
								.getPrdListSupplierWiseWithPagination(sup,Integer.parseInt(btn.getId()), pageResult);
						for (Entry<Long, List<Products>> y : maping.entrySet()) {
							System.out.println(btn.getText());
							System.out.println(pageResult);
							productList.clear();
							//productList.addAll(y.getValue());
							
							//above method removed and added below method to match with Purchase order Table View
							for (Products tmpPrd : y.getValue()) {
								Products loadProducts = new Products(tmpPrd.getPrd_id(), tmpPrd.getP_name(), tmpPrd.getOnHandQty(), tmpPrd.getPack_size(), tmpPrd.getUnit_cost_price(), tmpPrd.getUnitOfMeasure());
								productList.add(loadProducts);
							}
							
							loadTableView.setItems(productList);
							nextResult = Integer.parseInt(btn.getId()) + pageResult;
							prevResult = Integer.parseInt(btn.getId()) - pageResult;
							if (nextResult >= firstResult) {
								nextButton.setDisable(true);
							} else {
								nextButton.setDisable(false);
							}

							if (prevResult < 0) {
								System.out.println("prev:"+prevResult);
								prevButton.setDisable(true);
							} else {
								prevButton.setDisable(false);
							}
							break;

						}
					});
					//paginate button action over
					
					// hide buttons in order to limit only three buttons to show except 1 and last
					if (Integer.parseInt(btn.getText()) > 3 && Integer.parseInt(btn.getText()) != noOfBtns) {
						btn.setVisible(false);
						btn.setManaged(false);
					}
					
					//adding paginate buuttons to the Hbox
					hboxPagination.getChildren().add(btn);
				}
			
				//adding Previos button lastly to the Hbox
				hboxPagination.getChildren().add(nextButton);

				// in order to make sure that iteration only happens once otherwise break
				if (count > 1) {
					break;
				}
				count++;

			}

		}	

		

}
