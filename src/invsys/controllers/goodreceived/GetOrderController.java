package invsys.controllers.goodreceived;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import com.jfoenix.controls.JFXTextField;

import invsys.entities.PurchaseOrder;
import invsys.entitiydao.PurchaseOrderDao;
import invsys.entitiydao.impl.PurchaseOrderDaoImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class GetOrderController implements Initializable {

	@FXML
	private JFXTextField searchTextField;

	@FXML
	private TableView ordersTable;

	@FXML
	private TableColumn<PurchaseOrder, Integer> orderNumColumn;

	@FXML
	private TableColumn<PurchaseOrder, String> supNameCol;

	// in order pass data between scenes need to track parent FXMLloader file
	@FXML
	private FXMLLoader ParentFXMLLoader;

	// Obserable List of Purchase Order
	private ObservableList<PurchaseOrder> purchaseOrderList = FXCollections.observableArrayList();
	private FilteredList<PurchaseOrder> filteredData = new FilteredList<>(purchaseOrderList, e -> true);
	SortedList<PurchaseOrder> sList = new SortedList<PurchaseOrder>(filteredData);

	//Dao Handler/Classes
	
	PurchaseOrderDao purchaseOrderDao = null;
	
	// initialization method
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// get Instance of the Purchase Order Model
		purchaseOrderDao = PurchaseOrderDaoImpl.getDao();
		//
		searchItems();
		initTableColumns();
		loadPurchaseOrdersToTable();

	}

	// getterSetter for Instance Variables

	public FXMLLoader getParentFXMLLoader() {
		return ParentFXMLLoader;
	}

	public void setParentFXMLLoader(FXMLLoader parentFXMLLoader) {
		ParentFXMLLoader = parentFXMLLoader;
	}

	// getter setters end

	// table Column Initialization
	private void initTableColumns() {
		orderNumColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
		supNameCol.setCellValueFactory(
				(TableColumn.CellDataFeatures<PurchaseOrder, String> data) -> new SimpleStringProperty(
						data.getValue().getSupplier().getCom_name()));
	}

	// load Purchase Order data
	private void loadPurchaseOrdersToTable() {
		purchaseOrderList.clear();
		ordersTable.getItems().clear();
		purchaseOrderList.addAll(purchaseOrderDao.getPurchaseOrderListForGrn());
		ordersTable.setItems(purchaseOrderList);
	}

	// pass data between scene GRN window GRN Derails list from
	// this GRN pending table window
	public void setOrderDetToGrnController() {
		PurchaseOrder tempDet;
		tempDet = (PurchaseOrder) ordersTable.getSelectionModel().getSelectedItem();

		String poNum = String.valueOf(tempDet.getOrderId());

		if (ParentFXMLLoader.getController().getClass().equals(GoodReceivedController.class)) {
			GoodReceivedController passdatatoOrderWindow = ParentFXMLLoader.getController();
			passdatatoOrderWindow.setOrderNoField(poNum);

		}

	}

	@FXML
	void keyEvenHandler(KeyEvent event) {
		Stage thisStage = (Stage) searchTextField.getScene().getWindow();

		if (event.getCode() == KeyCode.ENTER && ordersTable.isFocused()) {
			// if (event.getCode() == KeyCode.ENTER)
			setOrderDetToGrnController();
			thisStage.close();

		} else if (event.getCode() == KeyCode.ESCAPE && ordersTable.isFocused()) {
			searchTextField.requestFocus();

		}

	}

	@FXML
	void mouseEventHandler(MouseEvent event) {
		Stage thisStage = (Stage) searchTextField.getScene().getWindow();
		PurchaseOrder tempDet;
		tempDet = (PurchaseOrder) ordersTable.getSelectionModel().getSelectedItem();
		if (event.getClickCount() == 2 && tempDet != null) {
			setOrderDetToGrnController();
			thisStage.close();
		}
	}

	@FXML
	void searchItems() {
		searchTextField.textProperty().addListener((ObservableValue, oldValue, newValue) -> {
			filteredData.setPredicate((Predicate<? super PurchaseOrder>) order -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				if (String.valueOf(order.getSupplier().getCom_name()).toLowerCase().contains(lowerCaseFilter)) {
					return true;
				} else if (String.valueOf(order.getOrderId()).toLowerCase().contains(lowerCaseFilter)) {
					return true;
				}

				return false;
			});
		});

		sList = new SortedList<>(filteredData);

		sList.comparatorProperty().bind(ordersTable.comparatorProperty());
		ordersTable.setItems(sList);

	}

}
