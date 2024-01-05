package invsys.controllers.purchaseorder;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import com.jfoenix.controls.JFXTextField;

import invsys.controllers.suppliermaintanance.SupplierMaintananceController;
import invsys.controllers.supplierpayments.SupplierPaymentController;
import invsys.controllers.supplierreturn.SupplierReturnController;
import invsys.entities.Supplier;
import invsys.entitiydao.SupplierDao;
import invsys.entitiydao.impl.SupplierDaoImpl;
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

public class GetSupplierController implements Initializable {

	@FXML
	private JFXTextField searchTextField;

	@FXML
	private TableColumn<Integer, Supplier> supCodeCol;

	@FXML
	private TableColumn<String, Supplier> supNameCol;
	@FXML
	private TableView supplierTable;

	@FXML
	private FXMLLoader ParentFXMLLoader;

	@FXML
	private Stage supplierControllerStage;

	

	private ObservableList<Supplier> supplierList = FXCollections.observableArrayList();
	private FilteredList<Supplier> filteredData = new FilteredList<>(supplierList, e -> true);

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// DBHandler.getInstance();
		initColumn();
		try {
			loadSupplierData();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	// set loader of the Purchaseorder window to this in order pass data between
	// scene

	public void setLoader(FXMLLoader loader) {
		this.ParentFXMLLoader = loader;
	}

	// load supplier data to table when initializing
	public void loadSupplierData() throws Exception {
		SupplierDao supplierDao = SupplierDaoImpl.getDao();
		supplierList.addAll(supplierDao.getSuppliers());
		supplierTable.getItems().setAll(supplierList);
	}

	// initialize columns when loading while mapping to the wrapper class
	public void initColumn() {
		supCodeCol.setCellValueFactory(new PropertyValueFactory<>("sup_id"));
		supNameCol.setCellValueFactory(new PropertyValueFactory<>("com_name"));
	}

	// filter data according to the key pressed in the textfield
	public void searchItems() {
		searchTextField.textProperty().addListener((ObservableValue, oldValue, newValue) -> {
			filteredData.setPredicate((Predicate<? super Supplier>) supplier -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				if (String.valueOf(supplier.getSup_id()).toLowerCase().contains(lowerCaseFilter)) {
					return true;
				} else if (supplier.getCom_name().toLowerCase().contains(lowerCaseFilter)) {
					return true;
				}

				return false;
			});
		});

		SortedList<Supplier> sList = new SortedList<>(filteredData);

		sList.comparatorProperty().bind(supplierTable.comparatorProperty());
		supplierTable.setItems(sList);
	}

	// pass data between scene purchase order window supplier name and code from
	// this supplier table window
	public void setSupDetToPurchaseOrder() {
		Supplier tempDet;
		tempDet = (Supplier) supplierTable.getSelectionModel().getSelectedItem();
		
	
			String supCode = String.valueOf(tempDet.getSup_id());
			String supName = tempDet.getCom_name();
			
			if(ParentFXMLLoader.getController().getClass().equals(PurchaseOrderController.class)) {
				PurchaseOrderController passdatatoOrderWindow = ParentFXMLLoader.getController();
				passdatatoOrderWindow.setSuplierDescription().setText(supName);
				passdatatoOrderWindow.setSupplierCode().setText(supCode);
				passdatatoOrderWindow.setSupplierEntity(tempDet);
			}
			
			if(ParentFXMLLoader.getController().getClass().equals(SupplierMaintananceController.class)) {
				SupplierMaintananceController passdatatoSupplierWindow = ParentFXMLLoader.getController();
				passdatatoSupplierWindow.setTextFieldFromSupplier(supCode);
				passdatatoSupplierWindow.viewInfoClicked();
				
			}
			
			if(ParentFXMLLoader.getController().getClass().equals(SupplierPaymentController.class)) {
				SupplierPaymentController passDatatoSupPayWindow = ParentFXMLLoader.getController();
				passDatatoSupPayWindow.setSupplierDetailsToMainScene(tempDet);
				
			}
			
			if(ParentFXMLLoader.getController().getClass().equals(SupplierReturnController.class)) {
				SupplierReturnController passDatatoSupReturnWindow = ParentFXMLLoader.getController();
				passDatatoSupReturnWindow.setSupplierDetailsToMainScene(tempDet);
				
			}
			
			}

	public void keyEvenHandler(KeyEvent event) {
		Stage thisStage = (Stage) searchTextField.getScene().getWindow();

		if (event.getCode() == KeyCode.ENTER && supplierTable.isFocused()) {
			// if (event.getCode() == KeyCode.ENTER)
			setSupDetToPurchaseOrder();
			thisStage.close();

		} else if (event.getCode() == KeyCode.ESCAPE && supplierTable.isFocused()) {
			searchTextField.requestFocus();

		}

	}

	public void mouseEventHandler(MouseEvent event) {
		Stage thisStage = (Stage) searchTextField.getScene().getWindow();
		Supplier tempDet;
		tempDet = (Supplier) supplierTable.getSelectionModel().getSelectedItem();
		if (event.getClickCount() == 2 && tempDet != null) {
			setSupDetToPurchaseOrder();
			thisStage.close();
		}
	}
}
