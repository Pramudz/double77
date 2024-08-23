package invsys.controllers.customer;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import org.controlsfx.control.Notifications;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import invsys.controllers.billingwindow.BillingController;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.controllers.formvalidation.ValidateInputEach;
import invsys.controllers.formvalidation.ValidateInputs;
import invsys.entities.Customer;
import invsys.entitiydao.CustomerDao;
import invsys.entitiydao.impl.CustomerDaoImpl;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class CustomerController implements Initializable {

    @FXML
    private JFXTextField viewCustomerField;

    @FXML
    private JFXButton buttonTableDrawer;

    @FXML
    private TextField custIdField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField nicField;

    @FXML
    private TextField companyNameField;

    @FXML
    private TextField contactNumField;

    @FXML
    private TextField telphoneField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField streetAddress;

    @FXML
    private TextField addressLine02Field;

    @FXML
    private TextField cityField;

    @FXML
    private Label fnameLabel;

    @FXML
    private Label lnameLabel;

    @FXML
    private Label nicLabel;

    @FXML
    private Label contactNumLabel;

    @FXML
    private Label telphoneLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label streetAddressLable;

    @FXML
    private Label addressLineLabel;

    @FXML
    private Label cityLabel;

    @FXML
    private Label companyNameLabel;

    @FXML
    private VBox tableDrawerVBox;

    @FXML
    private JFXTextField thisSearchTextField;

    @FXML
    private TableView<Customer> itemTableView;

    @FXML
    private TableColumn<Customer, String> colNic;

    @FXML
    private TableColumn<Customer, String> colFirstName;

    @FXML
    private TableColumn<Customer, String> compNameCol;

    @FXML
    private TableColumn<Customer, String> colEmail;

    @FXML
    private TableColumn<Customer, String> colContactNum;

    @FXML
    private TableColumn<Customer, String> colTelephone;

    @FXML
    private TableColumn<Customer, String> colStreetAddress;

    @FXML
    private TableColumn<Customer, String> colCity;

    @FXML
    private JFXButton actionButton;

    @FXML
    private JFXButton updateActionButton;

    // specially created for update due to validate username update where same
    // username canbe updated
    private Customer customerInstance;

    // userload table related variables
    private Stage customerTableLoadStage;

    // main loader for pass within the Scene
    @FXML
    private FXMLLoader thisMainLoader;

    //dao Handlers
    CustomerDao customerDao = null;

    // flag for POS and Normal Customer Saving
    // if POS =POS , Else "Normal"
    private String posCustomer;

    private FXMLLoader posBillingLoader;

    public String getPosCustomer() {
        return posCustomer;
    }

    public void setPosCustomer(String posCustomer) {
        this.posCustomer = posCustomer;
    }

    public FXMLLoader getPosBillingLoader() {
        return posBillingLoader;
    }

    public void setPosBillingLoader(FXMLLoader posBillingLoader) {
        this.posBillingLoader = posBillingLoader;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ValidateInputEach.getInstance();
        ValidateInputs.getInstance();
        customizeForm();
        initColumns();
        customerDao = CustomerDaoImpl.getDao();
        validateEachField();
        getTableDrawer();

    }
    
    private void customizeForm(){
        nicLabel.setVisible(false);
        nicField.setVisible(false);
        
        addressLine02Field.setVisible(false);
        addressLineLabel.setVisible(false);
        
        streetAddress.setVisible(false);
        streetAddressLable.setVisible(false);
        
        emailField.setVisible(false);
        emailLabel.setVisible(false);
        
        nicField.setVisible(false);
        nicField.setVisible(false);
        
        cityField.setVisible(false);
        cityLabel.setVisible(false);
    }

    // get loader details from MainController class in order pass data between
    // scenes
    public void setThisMainLoader(FXMLLoader thisMainLoader) {
        this.thisMainLoader = thisMainLoader;
    }

    // in order to swap user list within table view of main window & the select
    // user window to optimize data
    private ObservableList<Customer> customerListMain = FXCollections.observableArrayList();
    private FilteredList<Customer> filteredDataList = new FilteredList<>(customerListMain, e -> true);

    // in order to swap user list within table view of main window & the select
    // user window to optimise data
    public ObservableList<Customer> getUserListMain() {
        return customerListMain;
    }

    // in order to swap user list within table view of main window & the select
    // user window to optimize data
    public void setCustomerListMain(ObservableList<Customer> customerList) {
        this.customerListMain = customerList;
    }

    // setText Method from Customer Table view to this Text field
    public void setviewItemField(String x) {
        this.viewCustomerField.setText(x);
    }

    // initilization of Table view Columns
    private void initColumns() {
        colNic.setCellValueFactory(new PropertyValueFactory<>("nicNumber"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        compNameCol.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("customerEmail"));
        colContactNum.setCellValueFactory(new PropertyValueFactory<>("customerMobile"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("customerTelephone"));
        colStreetAddress.setCellValueFactory(new PropertyValueFactory<>("streetAddress"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
    }

    @FXML
    void setTextFieldEditable() {
        firstNameField.setEditable(true);
        lastNameField.setEditable(true);
        nicField.setEditable(true);
        contactNumField.setEditable(true);
        telphoneField.setEditable(true);
        emailField.setEditable(true);
        streetAddress.setEditable(true);
        addressLine02Field.setEditable(true);
        cityField.setEditable(true);
        companyNameField.setEditable(true);
        removeTextFieldCss();
        addTextFieldCss();
    }

    @FXML
    void textFieldNotEditable() {
        firstNameField.setEditable(false);
        lastNameField.setEditable(false);
        nicField.setEditable(false);
        contactNumField.setEditable(false);
        telphoneField.setEditable(false);
        emailField.setEditable(false);
        streetAddress.setEditable(false);
        addressLine02Field.setEditable(false);
        cityField.setEditable(false);
        companyNameField.setEditable(false);
        removeTextFieldCss();
        addOriginalCss();
    }

    // CSS for Error Handlings adding & Removing Methods
    private void addTextFieldCss() {
        firstNameField.getStyleClass().add("itext-field");
        lastNameField.getStyleClass().add("itext-field");
        nicField.getStyleClass().add("itext-field");
        contactNumField.getStyleClass().add("itext-field");
        telphoneField.getStyleClass().add("itext-field");
        emailField.getStyleClass().add("itext-field");
        streetAddress.getStyleClass().add("itext-field");
        addressLine02Field.getStyleClass().add("itext-field");
        cityField.getStyleClass().add("itext-field");
        companyNameField.getStyleClass().add("itext-field");

    }

    private void addOriginalCss() {
        firstNameField.getStyleClass().add("set-not-editable-text");
        lastNameField.getStyleClass().add("set-not-editable-text");
        nicField.getStyleClass().add("set-not-editable-text");
        contactNumField.getStyleClass().add("set-not-editable-text");
        telphoneField.getStyleClass().add("set-not-editable-text");
        emailField.getStyleClass().add("set-not-editable-text");
        streetAddress.getStyleClass().add("set-not-editable-text");
        addressLine02Field.getStyleClass().add("set-not-editable-text");
        cityField.getStyleClass().add("set-not-editable-text");
        companyNameField.getStyleClass().add("set-not-editable-text");

    }

    private void removeTextFieldCss() {
        firstNameField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
        lastNameField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
        nicField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
        contactNumField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
        telphoneField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
        emailField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
        streetAddress.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
        addressLine02Field.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
        cityField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
        companyNameField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");

    }

    // table drawer method for getting hidden tablview view to the current scene
    private void getTableDrawer() {

        TranslateTransition openNav = new TranslateTransition(new Duration(350), tableDrawerVBox);
        openNav.setToX(-150);

        TranslateTransition closeNav = new TranslateTransition(new Duration(350), tableDrawerVBox);
        buttonTableDrawer.setOnAction(e -> {
            if (tableDrawerVBox.getTranslateX() != -150) {
                tableDrawerVBox.setVisible(true);
                buttonTableDrawer.setText("Close Table");
                openNav.play();
                /*
				 * buttonTableDrawer.getStyleClass().remove("hamburger-button");
				 * buttonTableDrawer.getStyleClass().add("open-menu");
                 */

            } else {

                closeNav.setToX(610);
                closeNav.play();
                /*
				 * buttonTableDrawer.getStyleClass().remove("open-menu");
				 * buttonTableDrawer.getStyleClass().add("hamburger-button");
                 */
                PauseTransition visiblePause = new PauseTransition(
                        Duration.millis(350)
                );
                visiblePause.setOnFinished(
                        event -> {
                        	tableDrawerVBox.setVisible(false);
                            buttonTableDrawer.setText("Open Table");
                        }
                );
                visiblePause.play();
                
            }
        });
    }

    @FXML
    void clearDataClicked() {

        custIdField.clear();
        viewCustomerField.clear();
        firstNameField.clear();
        lastNameField.clear();
        nicField.clear();
        contactNumField.clear();
        telphoneField.clear();
        emailField.clear();
        streetAddress.clear();
        addressLine02Field.clear();
        cityField.clear();
        textFieldNotEditable();
        actionButton.setDisable(true);
        updateActionButton.setDisable(true);
        refreshTableData();
        customerInstance = null;
        companyNameField.clear();

    }

    private void refreshTableData() {
        customerListMain.clear();
        itemTableView.getItems().clear();
        customerTableLoadStage = null;

    }

    @FXML
    void createNewButton(ActionEvent event) {
        clearDataClicked();
        setTextFieldEditable();
        updateActionButton.setDisable(true);
        actionButton.setDisable(false);
        custIdField.setText(String.valueOf(customerDao.getLastIndex() + 1));

    }

    @FXML
    void deleteButton(ActionEvent event) {

        if (customerInstance != null) {

            if (AlertHandler.getAlert(AlertType.CONFIRMATION, "Are You Sure you want to Delete this",
                    customerInstance.getFirstName()).getResult().getButtonData().equals(ButtonData.OK_DONE)) {

                if (customerDao.deleteCustomer(customerInstance) == customerInstance.getCustomerId()) {
                    Notifications.create().title("Delete Success").graphic(null).hideAfter(Duration.seconds(1))
                            .darkStyle().position(Pos.CENTER).show();
                    clearDataClicked();
                    updateActionButton.setDisable(true);
                    actionButton.setDisable(true);

                }
            }

        }

    }

    @FXML
    void keyEventHandling(KeyEvent event) {

    }

    @FXML
    void loadCustomerAction(ActionEvent event) throws IOException {

        if (customerTableLoadStage != null) {
            if (AlertHandler.getAlert(AlertType.CONFIRMATION, "Confirmation", ""
                    + "Do you want to get Updated Customer List ?").getResult().getButtonData().
                    equals(ButtonData.OK_DONE)) {
                customerTableLoadStage = null;
            }

        }

        if (customerTableLoadStage == null) {
            String loc = "/fxml/customer/customerTable.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(loc));
            Scene scene = new Scene(loader.load());
            CustomerTableController passLoadertoSupplierTable = loader.getController();
            passLoadertoSupplierTable.setLoader(thisMainLoader);
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
    void loadItems(ActionEvent event) {
        customerListMain.clear();
        customerListMain.addAll(customerDao.getCustomers());
        itemTableView.setItems(customerListMain);

    }

    // Validate Form before Entering
    private boolean validateForm() {

        return ValidateInputs.validateUserNames(firstNameField, "First Name Field")
//                & ValidateInputs.validateUserNames(lastNameField, "last Name Field")
//                & ValidateInputs.validateNicField(nicField, "NIC Field")
                & ValidateInputs.validateTelphoneNumbers(contactNumField, "Mobile Number Field");
//                & ValidateInputs.validateTelphoneNumbers(telphoneField, "Telephone Number Field");
//                & ValidateInputs.validateEmailEmptyAllowed(emailField, "Email Field")
//                & ValidateInputs.validateInputEmpty(streetAddress, "Street Address Field")
//                & ValidateInputs.validateInputEmpty(addressLine02Field, "Address Field")
//                & ValidateInputs.validateInputEmpty(cityField, "City Fields");

    }

    // validate each field when entering
    private void validateEachField() {
        firstNameField.setOnKeyReleased(e -> {
            ValidateInputEach.validateUserNames(firstNameField, fnameLabel);
        });

        lastNameField.setOnKeyReleased(e -> {
            ValidateInputEach.validateUserNames(lastNameField, lnameLabel);
        });

//        nicField.setOnKeyReleased(e -> {
//            ValidateInputEach.validateNicField(nicField, "Nic Field", nicLabel);
//        });

        contactNumField.setOnKeyReleased(e -> {
            ValidateInputEach.validateMobileNumbers(contactNumField, contactNumLabel);
        });

//        telphoneField.setOnKeyReleased(e -> {
//            ValidateInputEach.validateMobileNumbers(telphoneField, telphoneLabel);
//        });

//        emailField.setOnKeyReleased(e -> {
//
//            ValidateInputEach.validateEmailWithEmptyAllowed(emailField, emailLabel);
//
//        });

//        streetAddress.setOnKeyReleased(e -> {
//            ValidateInputEach.validateInputEmpty(streetAddress, streetAddressLable);
//        });

//        addressLine02Field.setOnKeyReleased(e -> {
//            ValidateInputEach.validateInputEmpty(addressLine02Field, addressLineLabel);
//        });

//        cityField.setOnKeyReleased(e -> {
//            ValidateInputEach.validateInputEmpty(cityField, cityLabel);
//        });
    }

    @FXML
    void saveButtonClicked(ActionEvent event) {

    	//if (validateForm()) - Removed as requseted by Double 77 Car audio
    	
//        if (ValidateInputs.validateUserNames(firstNameField, "First Name Field")  - Removed as requseted by Double 77 Car audio
//                & ValidateInputs.validateUserNames(lastNameField, "last Name Field") & ValidateInputs.validateTelphoneNumbers(contactNumField, "Mobile Number Field") ) {
               if (ValidateInputs.validateUserNames(firstNameField, "First Name Field")) {
            try {
                String fName = firstNameField.getText() == null ? "" : firstNameField.getText();
                String lName = lastNameField.getText() == null ? "" : lastNameField.getText();
                String nic = nicField.getText() == null ? "" : nicField.getText();
                String mobNumber = contactNumField.getText() == null ? "" : contactNumField.getText();
                String telNumber = telphoneField.getText() == null ? "" : telphoneField.getText();
                String email = emailField.getText() == null ? "" : emailField.getText();
                String strtAddr = emailField.getText() == null ? "" : emailField.getText();
                String addrLine2 = addressLine02Field.getText() == null ? "" : addressLine02Field.getText();
                String city = cityField.getText() == null ? "" : cityField.getText();
                String compName = companyNameField.getText() == null ? "" : companyNameField.getText();

                Customer customer = new Customer();

                customer.setAddressLine02(addrLine2);
                customer.setCity(city);
                customer.setCompanyName(compName);
                if (!email.trim().equals("") || !email.equals(null) || !email.isBlank()) {
                    customer.setCustomerEmail(email);
                }

                if (email.isEmpty() || email.isBlank()) {
                    customer.setCustomerEmail(null);
                }
                
                
                if (!nic.trim().equals("") || !nic.equals(null) || !nic.isBlank()) {
                	 customer.setNicNumber(nic);
                }

                if (nic.isEmpty() || nic.isBlank()) {
                    customer.setNicNumber(null);
                }
                

                List<Customer> cutomerCheckDuplicatetList = customerDao.getCustomerByNicEmailContactNum(nic, email,
                        mobNumber);

//                if (!cutomerCheckDuplicatetList.isEmpty()) {
//                    AlertHandler.getAlert(AlertType.ERROR, "Duplicate Entry Error",
//                            "" + "You Have Entered Duplicate Entries to the Customer Form");
//                    for (Customer x : cutomerCheckDuplicatetList) {
//                        if (x.getCustomerMobile().equals(mobNumber)) {
//                            contactNumField.getStyleClass().add("danger-for-warning");
//                            contactNumLabel.setText("another customer is exist with the same mobile number");
//                        }
//
//                        if (x.getCustomerEmail() != null) {
//                            if (x.getCustomerEmail().equals(email)) {
//                                emailField.getStyleClass().add("danger-for-warning");
//                                emailLabel.setText("another customer is exist with the same email");
//
//                            }
//
//                        }
//
//                        
//                        if (!nic.trim().equals("") || !nic.equals(null) || !nic.isBlank()) {
//                        	 if (x.getNicNumber().equalsIgnoreCase(nic)) {
//                                 nicField.getStyleClass().add("danger-for-warning");
//                                 nicLabel.setText("another customer is exist with the same NIC");
//                             }
//                       }
//                        
//                       
//
//                    }
//                    return;
//                }

                customer.setCustomerMobile(mobNumber);
                customer.setCustomerTelephone(telNumber);
                customer.setFirstName(fName);
                customer.setLastName(lName);
               
                customer.setStreetAddress(strtAddr);

                if (AlertHandler
                        .getAlert(AlertType.CONFIRMATION, "Confirmation",
                                "Are you sure you want to create this Customer")
                        .getResult().getButtonData().equals(ButtonData.OK_DONE)) {
                    if (customerDao.saveCustomer(customer)) {
                        Notifications.create().title("Customer Created").graphic(null).hideAfter(Duration.seconds(1))
                                .darkStyle().position(Pos.CENTER).show();

                        if (posCustomer.equals("POS")) {
                            BillingController getController = posBillingLoader.getController();
                            getController.setCustomer(customer);
                            getController.updateCustomerTextField(customer.getFirstName());
                            Stage stage = (Stage) buttonTableDrawer.getScene().getWindow();
                            stage.close();
                        }

                        clearDataClicked();
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
                AlertHandler.getAlert(AlertType.ERROR, "Error In the Form", e.getLocalizedMessage());
            }
        }

    }

    @FXML
    void searchItems(KeyEvent event) {
        thisSearchTextField.textProperty().addListener((ObservableValue, oldValue, newValue) -> {
            filteredDataList.setPredicate((Predicate<? super Customer>) customer -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (String.valueOf(customer.getNicNumber()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (customer.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (customer.getCompanyName() != null) {
                    if (customer.getCompanyName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                }
                if (String.valueOf(customer.getCustomerMobile()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                return false;
            });
        });

        SortedList<Customer> sList = new SortedList<>(filteredDataList);

        sList.comparatorProperty().bind(itemTableView.comparatorProperty());

        itemTableView.setItems(sList);

    }

    @FXML
    void updateActionButtonClicked(ActionEvent event) {

        if (validateForm()) {
            try {
                long custId = Long.parseLong(custIdField.getText());
                String fName = firstNameField.getText() == null ? "" : firstNameField.getText();
                String lName = lastNameField.getText() == null ? "" : lastNameField.getText();
                String nic = nicField.getText() == null ? "" : nicField.getText();
                String mobNumber = contactNumField.getText() == null ? "" : contactNumField.getText();
                String telNumber = telphoneField.getText() == null ? "" : telphoneField.getText();
                String email = emailField.getText() == null ? "" : emailField.getText();
                String strtAddr = emailField.getText() == null ? "" : emailField.getText();
                String addrLine2 = addressLine02Field.getText() == null ? "" : addressLine02Field.getText();
                String city = cityField.getText() == null ? "" : cityField.getText();
                String compName = companyNameField.getText() == null ? "" : companyNameField.getText();

                Customer customer = new Customer();

                customer.setCustomerId(custId);
                customer.setAddressLine02(addrLine2);
                customer.setCompanyName(compName);
                customer.setCity(city);
                if (!email.trim().equals("") || !email.isBlank()) {
                    customer.setCustomerEmail(email);
                }

                if (email.isEmpty() || email.isBlank()) {
                    customer.setCustomerEmail(null);
                }

                List<Customer> cutomerCheckDuplicatetList = customerDao
                        .getCustomerByNicEmailContactNumForUpdate(custId, nic, email, mobNumber);

                if (!cutomerCheckDuplicatetList.isEmpty()) {
                    AlertHandler.getAlert(AlertType.ERROR, "Duplicate Entry Error",
                            "" + "You Have Entered Duplicate Entries to the Customer Form");
                    for (Customer x : cutomerCheckDuplicatetList) {
                        if (x.getCustomerMobile().equals(mobNumber)) {
                            contactNumField.getStyleClass().add("danger-for-warning");
                            contactNumLabel.setText("another customer is exist with the same mobile number");
                        }

                        if (x.getCustomerEmail() != null) {
                            if (x.getCustomerEmail().equals(email)) {
                                emailField.getStyleClass().add("danger-for-warning");
                                emailLabel.setText("another customer is exist with the same email");

                            }

                        }

                        if (x.getNicNumber().equals(nic)) {
                            nicField.getStyleClass().add("danger-for-warning");
                            nicLabel.setText("another customer is exist with the same NIC");
                        }

                    }
                    return;
                }
                customer.setCustomerMobile(mobNumber);
                customer.setCustomerTelephone(telNumber);
                customer.setFirstName(fName);
                customer.setLastName(lName);
                customer.setNicNumber(nic);
                customer.setStreetAddress(strtAddr);

                if (AlertHandler
                        .getAlert(AlertType.CONFIRMATION, "Confirmation",
                                "Are you sure you want to Update this Customer")
                        .getResult().getButtonData().equals(ButtonData.OK_DONE)) {
                    if (customerDao.updateCustomer(customer)) {
                        Notifications.create().title("Update Success").graphic(null).hideAfter(Duration.seconds(1))
                                .darkStyle().position(Pos.CENTER).show();
                        clearDataClicked();
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
                AlertHandler.getAlert(AlertType.ERROR, "Error In the Form", e.getLocalizedMessage());
            }
        }

    }

    @FXML
    void updateButton(ActionEvent event) {
        if (!viewCustomerField.getText().isEmpty() && actionButton.isDisable() && customerInstance != null) {
            setTextFieldEditable();
            updateActionButton.setDisable(false);
        } else {
            AlertHandler.getAlert(AlertType.ERROR, "You Must Select the User First", "");
        }
    }

    @FXML
    void viewInfoClicked() {
        try {
            if (actionButton.isVisible() || updateActionButton.isVisible()) {
                updateActionButton.setDisable(true);

                actionButton.setDisable(true);
            }

            long custId = Long.parseLong(viewCustomerField.getText());

            customerInstance = customerDao.getCustomerById(custId);

            if (customerInstance == null) {
                AlertHandler.getAlert(AlertType.ERROR, "No Data", "Customer Entity Not Exist");
                return;
            }

            removeTextFieldCss();
            addOriginalCss();
            textFieldNotEditable();

            custId = customerInstance.getCustomerId();
            String fName = customerInstance.getFirstName();
            String lName = customerInstance.getLastName();
            String nic = customerInstance.getNicNumber();
            String mobNumber = customerInstance.getCustomerMobile();
            String telNumber = customerInstance.getCustomerTelephone();
            String email = customerInstance.getCustomerEmail();
            String strtAddr = customerInstance.getStreetAddress();
            String addrLine2 = customerInstance.getAddressLine02();
            String city = customerInstance.getCity();
            String compName = customerInstance.getCompanyName();

            custIdField.setText(String.valueOf(custId));
            firstNameField.setText(fName);
            lastNameField.setText(lName);
            nicField.setText(nic);
            contactNumField.setText(mobNumber);
            telphoneField.setText(telNumber);
            emailField.setText(email);
            streetAddress.setText(strtAddr);
            addressLine02Field.setText(addrLine2);
            cityField.setText(city);
            companyNameField.setText(compName);

        } catch (Exception e) {
            clearDataClicked();
            AlertHandler.getAlert(AlertType.ERROR, "User cannot Find- pls enter valid User Id",
                    e.getLocalizedMessage());

        }

    }

}
