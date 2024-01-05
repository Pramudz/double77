package invsys.controllers.customer;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import com.jfoenix.controls.JFXTextField;

import invsys.controllers.billingwindow.BillingController;
import invsys.controllers.creditsale.CreditSaleController;
import invsys.entities.Customer;
import invsys.entitiydao.CustomerDao;
import invsys.entitiydao.impl.CustomerDaoImpl;
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

public class CustomerTableController implements Initializable {

	@FXML
	private JFXTextField searchTextField;

	@FXML
	private TableView<Customer> customerTableView;

	@FXML
	private TableColumn<Customer, String> customerNicCol;

	@FXML
	private TableColumn<Customer, String> firstNameCol;

	@FXML
	private TableColumn<Customer, String> companyNameCol;

	@FXML
	private TableColumn<Customer, String> custEmailCol;

	private FXMLLoader ParentFXMLLoader;

	// Observable List for Customers and Filtered List
	private ObservableList<Customer> customerList = FXCollections.observableArrayList();
	private FilteredList<Customer> filteredData = new FilteredList<>(customerList, e -> true);

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		initColumn();
		loadCustomerData();
		searchItems();
		
	}

	// set loader of the CustomerController window to this in order to pass data
	// between
	// scene
	public void setLoader(FXMLLoader loader) {
		this.ParentFXMLLoader = loader;
	}

	// initialize tableview column
	private void initColumn() {
		customerNicCol.setCellValueFactory(new PropertyValueFactory<>("nicNumber"));
		firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		companyNameCol.setCellValueFactory(new PropertyValueFactory<>("companyName"));
		custEmailCol.setCellValueFactory(new PropertyValueFactory<>("customerEmail"));

	}

	// load Customer data to table when initializing
	public void loadCustomerData() {
		CustomerDao customerDao = CustomerDaoImpl.getDao();
		customerList.addAll(customerDao.getCustomers());
		customerTableView.setItems(customerList);
	}

	@FXML
	void keyEvenHandler(KeyEvent event) {
		Stage thisStage = (Stage) searchTextField.getScene().getWindow();

		if (event.getCode() == KeyCode.ENTER && customerTableView.isFocused()) {
			// if (event.getCode() == KeyCode.ENTER)
			setCustomerDataToOtherControllers();
			thisStage.close();

		} else if (event.getCode() == KeyCode.ESCAPE && customerTableView.isFocused()) {
			searchTextField.requestFocus();

		}

	}

	@FXML
	void mouseEventHandler(MouseEvent event) {
		Stage thisStage = (Stage) searchTextField.getScene().getWindow();
		if (event.getClickCount() == 2 && customerTableView.isFocused()) {
			setCustomerDataToOtherControllers();
			thisStage.close();
		}

	}

	// Search & load filtered data to to tableview according to the text field inputs
	private void searchItems() {
		searchTextField.textProperty().addListener((ObservableValue, oldValue, newValue) -> {
			filteredData.setPredicate((Predicate<? super Customer>) customer -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				if (customer.getNicNumber().toLowerCase().contains(lowerCaseFilter)) {
					return true;
				} if (customer.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
					return true;
				} if (customer.getCompanyName() != null) {
					if(customer.getCompanyName().toLowerCase().contains(lowerCaseFilter)) {
						return true;
					}
				}if (customer.getCustomerEmail() != null) {
				if(customer.getCustomerEmail().toLowerCase().contains(lowerCaseFilter))
					return true;
				}
								
				return false;
			});
		});

		SortedList<Customer> sList = new SortedList<>(filteredData);
		sList.comparatorProperty().bind(customerTableView.comparatorProperty());
		customerTableView.setItems(sList);
	}

	// pass data between scene Customer table controller to required other
	// controllers
	// Example : Credit Invoice Controller
	public void setCustomerDataToOtherControllers() {
		Customer tempDet;
		tempDet = (Customer) customerTableView.getSelectionModel().getSelectedItem();

		if (tempDet != null) {

			String cusCode = String.valueOf(tempDet.getCustomerId());
			String cusName = tempDet.getFirstName();
			String cusId = tempDet.getNicNumber();
			String custMobile = tempDet.getCustomerMobile();

			if (ParentFXMLLoader.getController().getClass().equals(CustomerController.class)) {
				CustomerController passdatatoOrderWindow = ParentFXMLLoader.getController();
				passdatatoOrderWindow.setviewItemField(cusCode);
				passdatatoOrderWindow.viewInfoClicked();

			}
			
			if (ParentFXMLLoader.getController().getClass().equals(CreditSaleController.class)) {
				CreditSaleController passdatatoOrderWindow = ParentFXMLLoader.getController();
				passdatatoOrderWindow.setCustomeIdToCustomerIdField(tempDet.getCustomerId());
				passdatatoOrderWindow.updateCustomerInstance();

			}
                        
                    if (ParentFXMLLoader.getController().getClass().equals(BillingController.class)) {
                        BillingController passdatatoOrderWindow = ParentFXMLLoader.getController();
                        passdatatoOrderWindow.setCustomer(tempDet);
                        passdatatoOrderWindow.updateCustomerTextField(tempDet.getFirstName());

                    }

		}

	}

}
