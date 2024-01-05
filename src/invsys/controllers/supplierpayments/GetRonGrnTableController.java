package invsys.controllers.supplierpayments;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import com.jfoenix.controls.JFXTextField;

import invsys.entities.Supplier;
import invsys.entities.SupplierPaymentDetail;
import invsys.entitiydao.SupplierPaymentDao;
import invsys.entitiydao.impl.SupplierPaymentDaoImpl;
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

public class GetRonGrnTableController implements Initializable {
	
	
	@FXML
	private JFXTextField searchTextField;

	@FXML
	private TableView grnRonTableView;

	@FXML
	private TableColumn<SupplierPaymentDetail, String> docTypeCol;

	@FXML
	private TableColumn<SupplierPaymentDetail, Long> docNumCol;
	
	@FXML
	private TableColumn<SupplierPaymentDetail, java.sql.Date> dateOfRaisedCol;
	
	@FXML
	private TableColumn<SupplierPaymentDetail, java.sql.Date> grnRonAmountCol;

	// in order pass data between scenes need to track parent FXMLloader file
	@FXML
	private FXMLLoader ParentFXMLLoader;
	
	// Obserable List of Purchase Order
	private ObservableList<SupplierPaymentDetail> grnRonList = FXCollections.observableArrayList();
	private FilteredList<SupplierPaymentDetail> filteredData = new FilteredList<>(grnRonList, e -> true);
	SortedList<SupplierPaymentDetail> sList = new SortedList<SupplierPaymentDetail>(filteredData);

	
	//DAO Handlers
	SupplierPaymentDao supplierPaymentDao = null;
	
	
	// initialization method
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// get Instance of the Purchase Order Model
		supplierPaymentDao = SupplierPaymentDaoImpl.getDao();
		//
		initTableColumns();
		
		searchItems();
		
		

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
		docTypeCol.setCellValueFactory(new PropertyValueFactory<>("documentType"));
		docNumCol.setCellValueFactory(new PropertyValueFactory<>("grnRonId"));
		dateOfRaisedCol.setCellValueFactory(new PropertyValueFactory<>("documentRaisedDate"));
		grnRonAmountCol.setCellValueFactory(new PropertyValueFactory<>("documentAmount"));
		
	}

	// load pending grnRon for payments while matching GRN or RON type
	public void loadPendingPaymentGrnRons(Supplier supplier, String grnRon) {
		
		
		grnRonList.clear();
		grnRonTableView.getItems().clear();
		
		ObservableList<Object[]> temporyList = null;
		if(grnRon.equals("RON")) {
			temporyList = supplierPaymentDao.getRonForSupplierPayments(supplier);
		}
		
		if(grnRon.equals("GRN")) {
			temporyList = supplierPaymentDao.getGrnForPayments(supplier);
		}
		
		for(Object [] obj : temporyList) {
			
			long grnRonId = (long) obj[0];
			String docType = obj[1].toString();
			java.sql.Date date = (java.sql.Date) obj[2];
			double docAmount = (double) obj[3];
			SupplierPaymentDetail paymentDetail = new SupplierPaymentDetail();
			paymentDetail.setGrnRonId(grnRonId);
			paymentDetail.setDocumentType(docType);
			paymentDetail.setDocumentRaisedDate(date);
			paymentDetail.setDocumentAmount(docAmount);
			grnRonList.add(paymentDetail);
			
		}
		
		grnRonTableView.setItems(grnRonList);
		temporyList = null;
		
	}

	// pass data between scene GRN window GRN Derails list from
	// this GRN pending table window
	private void setOrderDetToGrnController() {
		SupplierPaymentDetail tempDet;
		tempDet = (SupplierPaymentDetail) grnRonTableView.getSelectionModel().getSelectedItem();

		if(tempDet != null) {
			long grnRonId = tempDet.getGrnRonId();
	    	String docType = tempDet.getDocumentType();
	    	double amount = tempDet.getDocumentAmount();
	    	java.sql.Date dateOfRaised = tempDet.getDocumentRaisedDate();

			if (ParentFXMLLoader.getController().getClass().equals(SupplierPaymentController.class)) {
				SupplierPaymentController passdatat = ParentFXMLLoader.getController();
				passdatat.addPaymentMethodFromGrnRonTableView(grnRonId, docType, amount, dateOfRaised);
				grnRonTableView.getItems().remove(tempDet);

			}

		}
		
	}

	@FXML
	void keyEvenHandler(KeyEvent event) {
		Stage thisStage = (Stage) searchTextField.getScene().getWindow();

		if (event.getCode() == KeyCode.ENTER && grnRonTableView.isFocused()) {
			// if (event.getCode() == KeyCode.ENTER)
			//setOrderDetToGrnController();
			//thisStage.close();

		} else if (event.getCode() == KeyCode.ESCAPE && grnRonTableView.isFocused()) {
			searchTextField.requestFocus();

		}

	}

	@FXML
	void mouseEventHandler(MouseEvent event) {
		Stage thisStage = (Stage) searchTextField.getScene().getWindow();
		SupplierPaymentDetail tempDet;
		tempDet = (SupplierPaymentDetail) grnRonTableView.getSelectionModel().getSelectedItem();
		if (event.getClickCount() == 2 && tempDet != null) {
			setOrderDetToGrnController();
			//thisStage.close();
		}
	}

	@FXML
	void searchItems() {
		searchTextField.textProperty().addListener((ObservableValue, oldValue, newValue) -> {
			filteredData.setPredicate((Predicate<? super SupplierPaymentDetail>) payDet -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				if (String.valueOf(payDet.getDocumentRaisedDate()).toLowerCase().contains(lowerCaseFilter)) {
					return true;
				} else if (String.valueOf(payDet.getGrnRonId()).toLowerCase().contains(lowerCaseFilter)) {
					return true;
				}

				return false;
			});
		});

		sList = new SortedList<>(filteredData);

		sList.comparatorProperty().bind(grnRonTableView.comparatorProperty());
		grnRonTableView.setItems(sList);

	}
}
