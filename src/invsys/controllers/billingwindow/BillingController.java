package invsys.controllers.billingwindow;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Pattern;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Sides;

import org.controlsfx.control.Notifications;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import invsys.controllers.customer.CustomerController;
import invsys.controllers.customer.CustomerTableController;
import invsys.controllers.database.HibernateUtil;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.controllers.formvalidation.DialogHandler;
import invsys.controllers.formvalidation.ValidateInputs;
import invsys.controllers.mainpage.MainController;
import invsys.controllers.pospaydetail.PosPaymentController;
import invsys.entities.CashRegister;
import invsys.entities.Customer;
import invsys.entities.POSPayDetails;
import invsys.entities.Products;
import invsys.entities.Role;
import invsys.entities.RoleFunctions;
import invsys.entities.Sales;
import invsys.entities.SalesDetails;
import invsys.entities.Users;
import invsys.entities.compositkeys.POSPayDetailId;
import invsys.entities.compositkeys.SalesDetailId;
import invsys.entities.compositkeys.SalesId;
import invsys.entitiydao.ProductDao;
import invsys.entitiydao.SalesDao;
import invsys.entitiydao.UserDao;
import invsys.entitiydao.impl.CustomerDaoImpl;
import invsys.entitiydao.impl.ProductDaoImpl;
import invsys.entitiydao.impl.SalesDaoImpl;
import invsys.entitiydao.impl.UserDaoImpl;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.converter.DoubleStringConverter;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.view.JasperViewer;

public class BillingController implements Initializable {

    @FXML
    private AnchorPane anchor;

    @FXML
    private TextField billNo;

    @FXML
    private TextField dateField;

    @FXML
    private TextField code;

    @FXML
    private TextField unitPrice;

    @FXML
    private TextField qty;

    @FXML
    private JFXComboBox<String> prdDesc;

    @FXML
    private TableView<SalesDetails> detTable;

    @FXML
    private TableColumn<SalesDetails, Integer> snoCol;

    @FXML
    private TableColumn<SalesDetails, String> codeCol;

    @FXML
    private TableColumn<SalesDetails, String> descCol;

    @FXML
    private TableColumn<SalesDetails, Double> qtyCol;

    @FXML
    private TableColumn<SalesDetails, Double> priceCol;

    @FXML
    private TableColumn<SalesDetails, Double> discCol;

    @FXML
    private TableColumn<SalesDetails, String> amountCol;

    @FXML
    private TableColumn<SalesDetails, String> grossAmountCol;

    @FXML
    private TableColumn<SalesDetails, Double> discountPercentageCol;
    

    

    @FXML
    private JFXButton fullBillCancelButton;

    @FXML
    private JFXButton billFinalizeButton;

    @FXML
    private JFXButton lineCancelButton;

    @FXML
    private TextField prdCount;

    @FXML
    private TextField sumOfQty;

    @FXML
    private TextField subTotal;

    @FXML
    private TextField discTextField;

    @FXML
    private TextField discountValueField;

    @FXML
    private Label timeLabel;

    @FXML
    private CheckBox discountEnableDisCheckBox;

    @FXML
    private CheckBox totalbilldiscountCheckBox;

    @FXML
    private Label lbldisc;

    @FXML
    private JFXButton searchCutomerBtn;

    @FXML
    private TextField cutomerNameTxtFld;

    @FXML
    private JFXButton createCutomerBtn;
    
    //31st Dec 2023 mapping Serial Number and Warranty for Double 7 - Pramud
    @FXML
    private TextField warrantyDetTxtField;
    
    @FXML
    private TextField snField;
    
    @FXML
    private TableColumn<SalesDetails, String> warrantyDetColumn;
    
    @FXML
    private TableColumn<SalesDetails, String> srNoColumn;
    

    // tempory product variable to keep track the setCombo box on clicked function
    // pressed
    private Products temporyProduct;

    // in order update cash registry values when updatin sales
    private CashRegister cashRegister;

    // other global vairables to track , no of product count , sum of quantity, sum
    // of total
    private double billAmount;
    private double billItemCount;
    private double billItemQty;
    private double billDicount;
    private java.sql.Time startTime;
    private java.sql.Time endTime;
    private int lastBillNo;
    private int seqNo;
    private double grossBillAmount;
    private Customer customer;
    private Stage customerTableLoadStage;
    private Stage customerMasterStage;
    private Customer dummyCustomer;

    // observable list for Product details for table view
    private ObservableList<SalesDetails> salesDetailsList = FXCollections.observableArrayList();

    // To load all item Description to the Hash Map and get Id with respect to it
    // when nessasary
    // mostly for get Product from where id and item Description
    private HashMap<String, Long> productMap = new HashMap<String, Long>();

    // in order to pass data between this loader to payment
    // this loader
    private FXMLLoader thiLoader;

    // payment stage
    private Stage paymentStage;

    // loader payment loader
    private FXMLLoader paymentFXLoader;

    // In order avoid duplicate updating bindautocomletion text as
    // "TextFields.bindAutoCompletion(itemDescField.getEditor(),
    // itemDescField.getItems());"
    Set<String> suggestions = new HashSet<String>();
    SuggestionProvider<String> provider = SuggestionProvider.create(suggestions);
    AutoCompletionTextFieldBinding<String> textFieldsAutoComplet;

    //dao handler/Classes
    ProductDao productDao = null;
    SalesDao salesDao = null;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        //for time in the POS window
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            timeLabel.setText(currentTime.getHour() + ":" + currentTime.getMinute() + ":" + currentTime.getSecond());
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        dummyCustomer = CustomerDaoImpl.getDao().getCustomerById(Long.parseLong(MainController.getDummyCustomer()));
        cutomerNameTxtFld.setText(dummyCustomer.getFirstName());

        salesDao = SalesDaoImpl.getDao();

        ValidateInputs.getInstance();

        // set place empty place holder to table view
        detTable.setPlaceholder(new Label(""));

        // set product Description combo box editable
        prdDesc.setEditable(true);

        // set date Field disable & get current date to the system
        dateField.setDisable(true);
        billNo.setDisable(true);
        java.sql.Date date = java.sql.Date.valueOf(LocalDate.now());
        dateField.setText(date.toString());

        // update combo box items when initializing
        loadItemstoComboBox();

        SalesDaoImpl.getDao();

        // on item desciption combo box key pressed action
        onPrdDescComboAction();

        // initialize the column
        initColumn();
        updateLastBillNo();

        discountEnableDisCheckBox.setOnAction(e -> {
            if (discountEnableDisCheckBox.isSelected()) {
                discTextField.setDisable(false);
                discTextField.setText(String.valueOf(0));
            } else {
                discTextField.setDisable(true);
                discTextField.setText(String.valueOf(0));
            }
        });

        totalbilldiscountCheckBox.setOnAction(e -> {
            if (totalbilldiscountCheckBox.isSelected()) {
                lbldisc.setText("Disc Total");
            } else {
                lbldisc.setText("Disc %");
            }
        });

        qty.setOnKeyReleased(e -> {
            if (e.getCode().equals(KeyCode.ESCAPE)) {
                clearTextFields();
            }
        });
        
        warrantyDetTxtField.setOnKeyPressed(e-> {
        	if (e.getCode().equals(KeyCode.ENTER)) {
        		qty.requestFocus();
        	}
        });
        
        snField.setOnKeyPressed(e-> {
        	if (e.getCode().equals(KeyCode.ENTER)) {
        		warrantyDetTxtField.requestFocus();
        	}
        });
    }

    // getter and setter of CashRegister
    public CashRegister getCashRegister() {
        return cashRegister;
    }

    public void setCashRegister(CashRegister cashRegister) {
        this.cashRegister = cashRegister;
    }
    // end getter setter for cash register

    // getters and setter for fxmlloaders
    public FXMLLoader getThiLoader() {
        return thiLoader;
    }

    public void setThiLoader(FXMLLoader thiLoader) {
        this.thiLoader = thiLoader;
    }

    public FXMLLoader getPaymentFXLoader() {
        return paymentFXLoader;
    }

    public void setPaymentFXLoader(FXMLLoader paymentFXLoader) {
        this.paymentFXLoader = paymentFXLoader;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    // getter setter ends
    // intialize table view column and map with the entity data
    public void initColumn() {

        // Pos Bill Columns Initialization
    	warrantyDetColumn.setCellValueFactory(new PropertyValueFactory<>("warrantyPeriod"));
    	srNoColumn.setCellValueFactory(new PropertyValueFactory<>("serialNo"));
        snoCol.setCellValueFactory(new PropertyValueFactory<>("seqNo"));
        codeCol.setCellValueFactory(
                (TableColumn.CellDataFeatures<SalesDetails, String> cdata) -> new SimpleStringProperty(
                        String.valueOf(cdata.getValue().getProduct().getPrd_id())));
        descCol.setCellValueFactory(
                (TableColumn.CellDataFeatures<SalesDetails, String> data) -> new SimpleStringProperty(
                        data.getValue().getProduct().getP_name()));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("salesQty"));
        discCol.setCellValueFactory(new PropertyValueFactory<>("discount"));
        amountCol.setCellValueFactory(celldata -> Bindings.format("%.2f", celldata.getValue().getItemAmount()));
        grossAmountCol.setCellValueFactory(celldata -> Bindings.format("%.2f", celldata.getValue().getItemGrossAmount()));
        discountPercentageCol.setCellValueFactory(new PropertyValueFactory<>("discountPercentage"));

        detTable.setEditable(true);
        qtyCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        detTable.setRowFactory(e -> new TableRow<SalesDetails>() {
            @Override
            protected void updateItem(SalesDetails arg0, boolean arg1) {
                super.updateItem(arg0, arg1);
                if (arg0 == null) {
                    setStyle("");
                } else if (arg0.isItemCancelStatus()) {
                    setStyle("-fx-background-color: brown");
                } else {
                    setStyle("");
                }

            }
        });
        
        
        

    }

    // To load all item Description to the Hash Map and get Id with respect to it
    // when nessasary
    // mostly for get Product from where id and item Description
    public void loadItemstoComboBox() {

        // clear suggestions for bindAutoCompletion
        suggestions.clear();
        if (textFieldsAutoComplet != null) {
            textFieldsAutoComplet.dispose();
        }

        // clear suggestions for suggesion provider bindAuto
        provider.clearSuggestions();
        productMap.clear();
        prdDesc.getItems().clear();

        var oldDisptacher = prdDesc.getEventDispatcher();

        prdDesc.setEventDispatcher((event, tail) -> {
            if (event.getEventType() == KeyEvent.KEY_RELEASED && ((KeyEvent) event).getCode() == KeyCode.F4) {
                return event;
            }

            return oldDisptacher.dispatchEvent(event, tail);
        });

        productDao = ProductDaoImpl.getDao();
        productDao.getProductList().stream().forEach(e -> {

            productMap.put(e.getP_name(), e.getPrd_id());
            prdDesc.getItems().add(e.getP_name());
            suggestions.add(e.getP_name());
        });

        provider.addPossibleSuggestions(suggestions);
        textFieldsAutoComplet = new AutoCompletionTextFieldBinding<>(prdDesc.getEditor(), provider);

    }

    public void updateCustomerTextField(String prm_customerName) {
        cutomerNameTxtFld.setText(prm_customerName);
    }

    // get product entity from product description gotten from combo and populate
    // other
    // data to the other text box such as price,etc...
    private void onPrdDescComboAction() {

        prdDesc.getEditor().setOnKeyReleased(e -> {

            if (e.getCode() == KeyCode.F4 && !salesDetailsList.isEmpty()) {
                try {
                    loadPaymentWindow();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            if (e.getCode() == KeyCode.ENTER) {

                if (prdDesc.getEditor().getText().isEmpty()) {
                    return;
                } else {
                    try {
                        String prd = prdDesc.getEditor().getText();
                        temporyProduct = ProductDaoImpl.getPrductsByIdAndName(prd, productMap.get(prd));

                        if (!temporyProduct.isStatus()) {
                            AlertHandler.getAlert(AlertType.ERROR, "This Product is Blocked", "Please Activate to Use");
                            prdDesc.requestFocus();
                            return;
                        }
                        code.setText(String.valueOf(temporyProduct.getPrd_id()));
                        unitPrice.setText(String.valueOf(temporyProduct.getUnit_price()));
                        discTextField.setText(String.valueOf(0));
                        // discTextField.setText(String.valueOf(temporyProduct.getDiscount()));

                        //qty.requestFocus();
                        snField.requestFocus();
                    } catch (Exception f) {
                        AlertHandler.getAlert(AlertType.ERROR, "Cannot Find or Load Item Invalid",
                                f.getLocalizedMessage());
                        f.printStackTrace();
                    }
                }

            }

        });

    }

    @FXML
    void KeyEvenHandler(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.F4 && !salesDetailsList.isEmpty() && code.getText().isEmpty()) {
            loadPaymentWindow();
        }

        if (event.getCode() == KeyCode.F2 && !salesDetailsList.isEmpty() && code.getText().isEmpty()) {
            fullBillCancelAction();
        }

        if (event.getCode() == KeyCode.F1 && !salesDetailsList.isEmpty() && !detTable.getSelectionModel().isEmpty()) {
            lineCancelAction();
        }

        if (event.getCode() == KeyCode.F10) {
            loadItemstoComboBox();
        }
    }

    @FXML
    void fullBillCancelAction() {

        try {

            if (!salesDetailsList.isEmpty()) {

                Users userSupervisor = DialogHandler.getDialog();
                Users currentUserSession = MainController.getUserSession();
                if (userSupervisor != null & currentUserSession != null) {
                    if (userSupervisor.getUserName().equals(currentUserSession.getUserName())) {
                        AlertHandler.getAlert(AlertType.ERROR, "You Have No Access", null);
                    } else {
                        String userName = userSupervisor.getUserName();
                        String pass = userSupervisor.getPassword();
                        UserDao userDao = UserDaoImpl.getDao();
                        Users validatedSupUser = userDao.getUserbyUserNamePassword(userName, pass);
                        boolean validatedStatus = false;
                        if (validatedSupUser != null) {
                            Set<Role> roles = validatedSupUser.getRole();
                            outer:
                            for (Role roleLoop : roles) {
                                for (RoleFunctions roleFunc : roleLoop.getRoleFunctions()) {
                                    if (roleFunc.getRoleFunction().equals("Bill Canceling")) {
                                        if (roleFunc.isRoleAcess()) {
                                            validatedStatus = true;
                                            break outer;
                                        }
                                    }
                                }
                            }
                            if (validatedStatus) {

                                SalesId salesId = new SalesId();
                                salesId.setUser_id(currentUserSession);
                                salesId.setDate(java.sql.Date.valueOf(LocalDate.now()));
                                salesId.setBill_no(lastBillNo);
                                Sales sales = new Sales();
                                sales.setAmountPaid(0);
                                sales.setBalance(0);
                                sales.setGrossBillAmount(grossBillAmount);
                                sales.setBillDiscount(billDicount);
                                sales.setBillAmount(billAmount);
                                sales.setCancelStatus(true);

                                sales.setStartTime(startTime);
                                sales.setSalesId(salesId);
                                sales.setCanceledBy(validatedSupUser);

                                for (SalesDetails det : salesDetailsList) {
                                    SalesDetailId salesDetId = new SalesDetailId();
                                    salesDetId.setSales(sales);
                                    salesDetId.setSeqNo(det.getSeqNo());
                                    det.setSalesDetId(salesDetId);
                                    det.setCanceledBy(validatedSupUser);
                                    det.setItemCancelStatus(true);

                                }

                                endTime = java.sql.Time.valueOf(LocalTime.now());
                                sales.setEndTime(endTime);
                                if (salesDao.cancelFullBill(sales, salesDetailsList)) {
                                    Notifications.create().title("Bill Cancelled").graphic(null)
                                            .hideAfter(Duration.seconds(2)).position(Pos.CENTER).show();
                                    salesDetailsList.clear();
                                    detTable.getItems().clear();
                                    clearData();
                                    prdDesc.getEditor().requestFocus();
                                    refreshSummaryData();
                                    updateLastBillNo();
                                    discTextField.setText(String.valueOf(0));
                                    discTextField.setDisable(true);
                                    discountEnableDisCheckBox.setSelected(false);
                                    totalbilldiscountCheckBox.setSelected(false);
                                }
                            }
                        }

                    }
                }

            }
        } catch (Exception e) {
            AlertHandler.getAlert(AlertType.ERROR, "Cannot Cancel this sale somthing went wrong",
                    e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void createCutomerBtnAction(ActionEvent event) {
        try {
            if (customerMasterStage != null) {
                if (AlertHandler.getAlert(AlertType.CONFIRMATION, "Confirmation", ""
                        + "Do you want to get Updated Customer List ?").getResult().getButtonData().
                        equals(ButtonData.OK_DONE)) {
                    customerMasterStage = null;
                }

            }

            if (customerMasterStage == null) {
                String loc = "/fxml/customer/CustomerGUI.fxml";

                FXMLLoader loader = new FXMLLoader(getClass().getResource(loc));
                Scene scene = new Scene(loader.load());
                CustomerController customerMasterControler = loader.getController();
                customerMasterControler.setThisMainLoader(loader);
                customerMasterControler.setPosCustomer("POS");
                customerMasterControler.setPosBillingLoader(thiLoader);
                customerMasterStage = new Stage(StageStyle.DECORATED);
                customerMasterStage.initModality(Modality.WINDOW_MODAL);
                customerMasterStage.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
                customerMasterStage.setScene(scene);
                customerMasterStage.showAndWait();

            } else {
                customerMasterStage.showAndWait();
            }
        } catch (Exception e) {

            AlertHandler.getAlert(AlertType.ERROR, "Cannot Load Customer Master Screen",
                    e.getLocalizedMessage());
            e.printStackTrace();
        }

    }

    // testing with jasper report
    @FXML
    void clearTextFields() {
        code.clear();
        prdDesc.getEditor().clear();
        unitPrice.clear();
        qty.clear();
        discTextField.clear();
        prdDesc.requestFocus();

    }

    @FXML
    void lineCancelAction() {
        if (!salesDetailsList.isEmpty()) {
            SalesDetails det = detTable.getSelectionModel().getSelectedItem();

            if (!det.isItemCancelStatus()) {
                Users user = DialogHandler.getDialog();
                if (user != null & MainController.getUserSession() != null) {
                    if (user.getUserName().equals(MainController.getUserSession().getUserName())) {
                        AlertHandler.getAlert(AlertType.ERROR, "You Have No Access", null);
                    } else {
                        String username = user.getUserName();
                        String password = user.getPassword();
                        UserDao userDao = UserDaoImpl.getDao();
                        Users userValidate = userDao.getUserbyUserNamePassword(username, password);

                        if (userValidate != null) {
                            Set<Role> roleSet = userValidate.getRole();
                            boolean status = false;
                            outer:
                            for (Role role : roleSet) {
                                for (RoleFunctions functions : role.getRoleFunctions()) {
                                    if (functions.getRoleFunction().equals("Bill Canceling")) {
                                        if (functions.isRoleAcess()) {
                                            status = true;
                                            break outer;
                                        }
                                    }
                                }
                            }

                            if (status) {
                                det.setItemCancelStatus(true);
                                det.setCanceledBy(userValidate);
                                detTable.getSelectionModel().clearSelection();
                                refreshSummaryData();
                                prdDesc.getEditor().requestFocus();
                            } else {
                                AlertHandler.getAlert(AlertType.ERROR, "SuperVisor does not have access", null);
                            }
                        }

                    }

                } else {
                    AlertHandler.getAlert(AlertType.ERROR, "Supervisor Entity is Null Pls Contact Admin", null);
                }

            }
        }
    }

    @FXML
    void editQtyButtonClicked(CellEditEvent<SalesDetails, Double> event) {

        SalesDetails tempData;

        tempData = (SalesDetails) detTable.getSelectionModel().getSelectedItem();

        try {
            if (salesDetailsList.isEmpty()) {
                System.out.println("No Data to Edit");
            } else {

                if (tempData.getProduct().getUnitOfMeasure().equals("unit")) {
                    String checkValue = event.getNewValue().toString();
                    checkValue = checkValue.contains(".") ? checkValue.replaceAll("0*$", "").replaceAll("\\.$", "")
                            : checkValue;
                    boolean check = ValidateInputs.validateQtyForUnitItemsWithString(checkValue, "Quantity Field");
                    if (!check) {
                        event.getTableView().getItems().set(event.getTablePosition().getRow(), tempData);
                        return;
                    }

                }
                double qty = event.getNewValue();
                tempData.setSalesQty(qty);

                if (tempData.getDiscountPercentage() > 0) {
                    double realDiscountPersentage = tempData.getDiscountPercentage() / 100;
                    double discountValue = tempData.getUnitPrice() * realDiscountPersentage;
                    double discountedPrice = tempData.getUnitPrice() - discountValue;
                    tempData.setDiscount(discountValue * qty);
                    tempData.setItemAmount(discountedPrice * qty);
                } else {
                    tempData.setItemAmount(qty * tempData.getUnitPrice());
                }
                tempData.setItemGrossAmount(qty * tempData.getUnitPrice());
                refreshSummaryData();
                event.getTableView().getItems().set(event.getTablePosition().getRow(), tempData);
            }

        } catch (Exception e) {
            AlertHandler.getAlert(AlertType.ERROR, "Cell Edit Event Error", e.getLocalizedMessage());
            event.getTableView().getItems().set(event.getTablePosition().getRow(), tempData);
            e.printStackTrace();
        }

    }

    // discount field enter
    private void itemDiscountPercentage() {
        try {
            if (salesDetailsList.isEmpty()) {
                startTime = java.sql.Time.valueOf(LocalTime.now());
            }

            if (ValidateInputs.validatePrices(qty, "Quantity Field") & ValidateInputs.validatePrices(discTextField, "Discount Field")) {

                if (temporyProduct.getUnitOfMeasure().equals("unit")) {
                    boolean check = ValidateInputs.validateQtyForUnitItems(qty, "Quantity Field");
                    if (!check) {
                        return;
                    }
                }
                
                // kanishka(2024-01-07) request double 7
                // discount taken as amount
                  double quantity = Double.valueOf(qty.getText());
//                double discountPresentage = Double.valueOf(discTextField.getText());
                  double discountValue = Double.valueOf(discTextField.getText());
                  double discountPresentage = (discountValue /temporyProduct.getUnit_price()) *100 ;               

//                if (discountPresentage > temporyProduct.getDiscount()) {
//                    AlertHandler.getAlert(AlertType.ERROR, "Max DiscountError",
//                            "Only Allowable Max Discount is :" + temporyProduct.getDiscount());
//                    return;
//                }

//                      if (discountPresentage > temporyProduct.getDiscount()) {
//                    AlertHandler.getAlert(AlertType.ERROR, "Max DiscountError",
//                            "Only Allowable Max Discount is :" + temporyProduct.getDiscount());
//                    return;
//                }
                
                if (quantity <= 0 || (!snField.getText().isBlank() && quantity > 1)) {
                    AlertHandler.getAlert(AlertType.ERROR, "InValid Number", null);
                } else {
                    SalesDetails salesDet = new SalesDetails();
                    salesDet.setProduct(temporyProduct);
                    salesDet.setCostPrice(temporyProduct.getUnit_cost_price());
                    salesDet.setUnitPrice(temporyProduct.getUnit_price());
                    salesDet.setAverageCostPrice(temporyProduct.getUnitAverageCost());
                    salesDet.setItemVat(temporyProduct.getVat());
                    salesDet.setSalesQty(quantity);
                    salesDet.setItemGrossAmount(salesDet.getUnitPrice() * quantity);
                    salesDet.setItemCancelStatus(false);
                    
                    //31st Dec New Added Warranty and Ser No
                    
                    String warranty = warrantyDetTxtField.getText();
                    String serialNo = snField.getText();
                    
                    if (warranty.isBlank() || warranty.isEmpty()) {
                    	warranty = "";
                    }
                    
                    if (serialNo.isBlank() || serialNo.isEmpty()) {
                    	serialNo = "";
                    }
                    salesDet.setWarrantyPeriod(warranty);
                    salesDet.setSerialNo(serialNo);
                    // End 
                  
                    if (discountPresentage > 0) {
//                        salesDet.setDiscountPercentage(discountPresentage);
//                        double realDiscountPersentage = discountPresentage / 100;
//                        double actualUnitPrice = temporyProduct.getUnit_price();
//                        double discountValue = actualUnitPrice * realDiscountPersentage;
//                        double discountedPrice = actualUnitPrice - discountValue;
//                        salesDet.setDiscountedPrice(discountedPrice);
//                        salesDet.setDiscount(discountValue * quantity);
//                        salesDet.setItemAmount(discountedPrice * quantity);
                        
                        
                        double realDiscountPersentage = discountPresentage / 100;
                        double actualUnitPrice = temporyProduct.getUnit_price();
                        double discountValueItem = actualUnitPrice * realDiscountPersentage;
                        double discountedPrice = actualUnitPrice - discountValueItem;
                        salesDet.setDiscountedPrice(discountedPrice);
                        salesDet.setDiscount(discountValueItem * quantity);
                        salesDet.setItemAmount(discountedPrice * quantity);
                        salesDet.setDiscountPercentage(discountPresentage);
                    } else {
                        salesDet.setItemAmount(salesDet.getUnitPrice() * quantity);
                        salesDet.setDiscountedPrice(salesDet.getUnitPrice());
                    }

                    salesDetailsList.add(salesDet);
                    detTable.setItems(salesDetailsList);
                    temporyProduct = null;
                    salesDet = null;
                    clearData();
                    prdDesc.getEditor().requestFocus();
                    refreshSummaryData();

                }
            }
        } catch (Exception e) {
            AlertHandler.getAlert(AlertType.ERROR, "InValid Discount", null);
            e.printStackTrace();
        }
    }

    private void totalBillDiscount() {
        try {
            final Pattern pattern = Pattern.compile("^\\d*\\.?\\d+$");
            if (pattern.matcher(discTextField.getText().trim()).matches()) {

                double discountValue = Double.parseDouble(discTextField.getText().trim());

                for (SalesDetails salesDet : salesDetailsList) {
                    double salesContribution = salesDet.getItemGrossAmount() / grossBillAmount;
                    double dicountValueforItem = discountValue * salesContribution;
                    double discountpercent = dicountValueforItem / salesDet.getItemGrossAmount() * 100;

                    salesDet.setDiscountPercentage(discountpercent);
                    double realDiscountPersentage = discountpercent / 100;
                    double actualUnitPrice = salesDet.getUnitPrice();
                    double itemdiscountValue = actualUnitPrice * realDiscountPersentage;
                    double discountedPrice = actualUnitPrice - itemdiscountValue;
                    salesDet.setDiscountedPrice(discountedPrice);
                    salesDet.setDiscount(itemdiscountValue * salesDet.getSalesQty());
                    salesDet.setItemAmount((discountedPrice * salesDet.getSalesQty()));
                    
					/*
					 * double salesContribution = salesDet.getItemAmount() / grossBillAmount; double
					 * dicountValueforItem = discountValue * salesContribution; double
					 * discountpercent = dicountValueforItem / salesDet.getItemAmount() * 100;
					 * 
					 * salesDet.setDiscountPercentage(Math.round(discountpercent * 100) / 100);
					 * double realDiscountPersentage = discountpercent / 100; double actualUnitPrice
					 * = salesDet.getUnitPrice(); double itemdiscountValue = actualUnitPrice *
					 * realDiscountPersentage; double discountedPrice = actualUnitPrice -
					 * itemdiscountValue; salesDet.setDiscountedPrice(Math.round(discountedPrice *
					 * 100) / 100); salesDet.setDiscount(Math.round((itemdiscountValue *
					 * salesDet.getSalesQty()) * 100) / 100);
					 * salesDet.setItemAmount(Math.round((discountedPrice * salesDet.getSalesQty())
					 * * 100) / 100);
					 */
                    
                    
                }
                detTable.refresh();
                clearData();
                prdDesc.getEditor().requestFocus();
                refreshSummaryData();
            }
        } catch (Exception e) {
            AlertHandler.getAlert(AlertType.ERROR, "Discount Error", null);
            e.printStackTrace();
        }
    }

// discount field enter      
    @FXML
    public void discountFieldOnAction() {

        if (totalbilldiscountCheckBox.isSelected()) {
            totalBillDiscount();
        } else {
            itemDiscountPercentage();
        }

    }
    // code field on action

    @FXML
    void getProduct() {
        code.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                try {
                    long codeText = Long.valueOf(code.getText());
                    temporyProduct = productDao.getProductById(codeText);

                    if (!temporyProduct.isStatus()) {
                        AlertHandler.getAlert(AlertType.ERROR, "This Product is Blocked", "Please Activate to Use");
                        code.requestFocus();
                        return;
                    }
                    prdDesc.getEditor().setText(temporyProduct.getP_name());
                    unitPrice.setText(String.valueOf(temporyProduct.getUnit_price()));
                    discTextField.setText(String.valueOf(0));
                    // discTextField.setText(String.valueOf(temporyProduct.getDiscount()));
                    //qty.requestFocus();
                    snField.requestFocus();

                } catch (Exception f) {
                    AlertHandler.getAlert(AlertType.ERROR, "Cannot Load Item Invalid", f.getMessage());
                    f.printStackTrace();
                }
            }
        });
    }

    // refresh summary text fields such as total Amount of bill, no of items, toal
    // discount etc
    private void refreshSummaryData() {
        billAmount = 0;
        billDicount = 0;
        billItemQty = 0;
        billItemCount = 0;
        seqNo = 0;
        grossBillAmount = 0;
        int itemCount = 0;

        for (SalesDetails e : salesDetailsList) {
            e.setSeqNo(++seqNo);
            if (e.isItemCancelStatus()) {
                continue;
            }
            itemCount++;
            if (e.getDiscount() > 0) {
                billDicount = billDicount + (e.getDiscount());
            }
            billAmount = billAmount + (e.getItemAmount());
            billItemQty = billItemQty + (e.getSalesQty());
            grossBillAmount = grossBillAmount + e.getItemGrossAmount();
        }
        billItemCount = itemCount;

        subTotal.setText(String.format("%.2f", billAmount));
        prdCount.setText(String.valueOf(billItemCount));
        discountValueField.setText(String.format("%.2f", billDicount));
        sumOfQty.setText(String.format("%.2f", billItemQty));
    }

    // when qty button clicked table will be filled
    @FXML
    void qtyAdd(ActionEvent event) {

        try {
            if (salesDetailsList.isEmpty()) {
                startTime = java.sql.Time.valueOf(LocalTime.now());
            }

            if (ValidateInputs.validatePrices(qty, "Quantity Field") & ValidateInputs.validatePrices(discTextField, "Discount Field")) {

                if (temporyProduct.getUnitOfMeasure().equals("unit")) {
                    boolean check = ValidateInputs.validateQtyForUnitItems(qty, "Quantity Field");
                    if (!check) {
                        return;
                    }
                }
                // kanishka(2024-01-07) request double 7 
                // discount taken as amount               
                double quantity = Double.valueOf(qty.getText());
//                double discountPresentage = Double.valueOf(discTextField.getText());
                double discountValue = Double.valueOf(discTextField.getText());

//                if (discountPresentage > temporyProduct.getDiscount()) {
//                    AlertHandler.getAlert(AlertType.ERROR, "Max DiscountError",
//                            "Only Allowable Max Discount is :" + temporyProduct.getDiscount());
//                    return;
//                }

                if (quantity <= 0 || (!snField.getText().isBlank() && quantity > 1)) {
                    AlertHandler.getAlert(AlertType.ERROR, "Invalid Number", null);
                    qty.getStyleClass().add("danger-for-warning");
                } else {
                    SalesDetails salesDet = new SalesDetails();
                    salesDet.setProduct(temporyProduct);
                    salesDet.setCostPrice(temporyProduct.getUnit_cost_price());
                    salesDet.setUnitPrice(temporyProduct.getUnit_price());
                    salesDet.setAverageCostPrice(temporyProduct.getUnitAverageCost());
                    salesDet.setItemVat(temporyProduct.getVat());
                    salesDet.setSalesQty(quantity);
                    salesDet.setItemGrossAmount(salesDet.getUnitPrice() * quantity);
                    salesDet.setItemCancelStatus(false);
                    
                  //31st Dec New Added Warranty and Ser No
                    
                    String warranty = warrantyDetTxtField.getText();
                    String serialNo = snField.getText();
                    
                    if (warranty.isBlank() || warranty.isEmpty()) {
                    	warranty = "";
                    }
                    
                    if (serialNo.isBlank() || serialNo.isEmpty()) {
                    	serialNo = "";
                    }
                    salesDet.setWarrantyPeriod(warranty);
                    salesDet.setSerialNo(serialNo);
                    // End 
                    
                    
                    
                    double discountPresentage = (discountValue/temporyProduct.getUnit_price())*100;
                    if (discountPresentage > 0) {
                        salesDet.setDiscountPercentage(discountPresentage);
                        double realDiscountPersentage = discountPresentage / 100;
                        double actualUnitPrice = temporyProduct.getUnit_price();
                        double itemdiscountValue = actualUnitPrice * realDiscountPersentage;
                        double discountedPrice = actualUnitPrice - itemdiscountValue;
                        salesDet.setDiscountedPrice(discountedPrice);
                        salesDet.setDiscount(itemdiscountValue * quantity);
                        salesDet.setItemAmount(discountedPrice * quantity);
                    } else {
                        salesDet.setItemAmount(salesDet.getUnitPrice() * quantity);
                        salesDet.setDiscountedPrice(salesDet.getUnitPrice());
                    }
                    salesDetailsList.add(salesDet);
                    detTable.setItems(salesDetailsList);
                    temporyProduct = null;
                    salesDet = null;
                    clearData();
                    //prdDesc.getEditor().requestFocus();
                    
                    refreshSummaryData();
                    code.requestFocus();

                }
            }

        } catch (Exception e) {
            AlertHandler.getAlert(AlertType.ERROR, "InValid Number", null);

            e.printStackTrace();
        }

    }

    private void clearData() {
        code.clear();
        prdDesc.getEditor().clear();
        qty.clear();
        unitPrice.clear();
        discTextField.clear();
        setCustomer(dummyCustomer);
        cutomerNameTxtFld.setText(dummyCustomer.getFirstName());
        snField.clear();
        warrantyDetTxtField.clear();
    }

    @FXML
    void searchCutomerBtnAction(ActionEvent event) {
        try {

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
                passLoadertoSupplierTable.setLoader(thiLoader);
                customerTableLoadStage = new Stage(StageStyle.DECORATED);
                customerTableLoadStage.initModality(Modality.WINDOW_MODAL);
                customerTableLoadStage.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
                customerTableLoadStage.setScene(scene);
                customerTableLoadStage.showAndWait();
            } else {
                customerTableLoadStage.showAndWait();
            }

        } catch (Exception e) {
            AlertHandler.getAlert(AlertType.ERROR, "Customer table cannot load", null);
            e.printStackTrace();
        }

    }

    // finalze the bill
    public void finalizeAction(double paidAmount, double balance, ObservableList<POSPayDetails> payDetails) {
        try {
            if (!salesDetailsList.isEmpty()) {

                Users user = MainController.getUserSession();
                SalesId salesId = new SalesId();
                salesId.setUser_id(user);
                salesId.setDate(java.sql.Date.valueOf(LocalDate.now()));
                salesId.setBill_no(lastBillNo);
                Sales sales = new Sales();
                sales.setCustomer(customer);
                sales.setAmountPaid(paidAmount);
                sales.setGrossBillAmount(grossBillAmount);
                sales.setBillDiscount(billDicount);
                sales.setBalance(balance);
                sales.setBillAmount(billAmount);
                sales.setCancelStatus(false);
                sales.setStartTime(startTime);
                sales.setSalesId(salesId);

                for (POSPayDetails payDets : payDetails) {

                    POSPayDetailId payDetId = new POSPayDetailId();
                    payDetId.setDocType("POSS");
                    payDetId.setSales(sales);
                    payDetId.setSeqNo(payDets.getTemporySeqNo());
                    payDets.setPosPayDetId(payDetId);

                }

                for (SalesDetails salesDet : salesDetailsList) {
                    SalesDetailId salesDetId = new SalesDetailId();
                    salesDetId.setSales(sales);
                    salesDetId.setSeqNo(salesDet.getSeqNo());
                    salesDet.setSalesDetId(salesDetId);
                }

                endTime = java.sql.Time.valueOf(LocalTime.now());
                sales.setEndTime(endTime);
                if (salesDao.saveSale(sales, salesDetailsList, payDetails)) {
                    Notifications.create().title("Save Success").text("Pay Balance :" + balance).graphic(null)
                            .hideAfter(Duration.seconds(2)).darkStyle().position(Pos.CENTER).show();

                    if (AlertHandler.getAlert(AlertType.CONFIRMATION, "Do You Want to Print This ?", "Ok or Cancel").getResult().getButtonData().equals(ButtonData.OK_DONE)) {
                    	printJasperReceiptcheck(salesDetailsList, sales, payDetails);
//                        printBillWithJavax(salesDetailsList, sales, payDetails, "Original");

                    };

                    salesDetailsList.clear();
                    detTable.getItems().clear();
                    clearData();
                    prdDesc.getEditor().requestFocus();
                    refreshSummaryData();
                    updateLastBillNo();
                    discTextField.setText(String.valueOf(0));
                    discTextField.setDisable(true);
                    discountEnableDisCheckBox.setSelected(false);
                    totalbilldiscountCheckBox.setSelected(false);
                }

            }

        } catch (Exception e) {
            AlertHandler.getAlert(AlertType.ERROR, "Cannot Update", e.getLocalizedMessage());
            e.printStackTrace();

        }
    }

    // to print jasper report using RAM data , after submitting to the database but
    // data is loaded from the memory (Random Access Memory)
    private void printJasperReceipt(ObservableList<SalesDetails> salesDetList, Sales sales,
            ObservableList<POSPayDetails> payDetails) throws JRException {
        //String loc = "resources/jasperreports/pos_bill_print.jasper";
    	//below is specifically for Double 7 Car Audio Print
		
        String loc = "resources/jasperreports/pos_bill_print-with-cc.jasper";
        
        
        
        String subReportLoc = "resources/jasperreports/PosPaySubreport.jasper";
        JRDataSource dataSource = new JRBeanCollectionDataSource(salesDetailsList);
        Map<String, Object> parameters = new HashMap();
        parameters.put("companyName", MainController.getCompanyInfoSession().getCompanyName());
        parameters.put("streetAddress", MainController.getCompanyInfoSession().getAddressLine1());
        parameters.put("addressLine", MainController.getCompanyInfoSession().getAddressLine2());
        parameters.put("city", MainController.getCompanyInfoSession().getAddressLine3());
        parameters.put("telephoneNum", MainController.getCompanyInfoSession().getTelephoneNum());
        parameters.put("billNo", Long.valueOf(sales.getSalesId().getBill_no()));
        parameters.put("customerFname", sales.getCustomer().getFirstName());
        parameters.put("customerPhone", sales.getCustomer().getCustomerMobile());
        
        //Customer Company name for double seven car studio will be Vehicle No only on front end UI
        parameters.put("cusCompanyName", sales.getCustomer().getCompanyName());
        
        //Customer telephoneNum name for double seven car studio will be Vehicle Type only in front End UI
        parameters.put("telephoneNum", sales.getCustomer().getCustomerTelephone());
        
        parameters.put("date", sales.getSalesId().getDate());
        parameters.put("userName", sales.getSalesId().getUser_id().getUserName());
        parameters.put("changeAmount", sales.getBalance());
        parameters.put("payModeDataSource", new JRBeanCollectionDataSource(payDetails));
        parameters.put("subReportPath", subReportLoc);
        JasperPrint jp = JasperFillManager.fillReport(loc, parameters, dataSource);
       JasperPrintManager.printReport(jp, false);

        //JasperViewer.viewReport(jp, false);
    }
    
    // get paymodes toString
    private String getPayModesString( ObservableList<POSPayDetails> payDetails){
       
       String paymode = "";       
       for(int i = 0; i < payDetails.size(); i++){
           POSPayDetails paydetail = payDetails.get(i);
           paymode = paymode.concat(paydetail.getPayMode().getModeId());
           
           if(i != payDetails.size()-1){
            paymode = paymode.concat("/");
           }
           
       } 
       return paymode;
    }

    private void printJasperReceiptcheck(ObservableList<SalesDetails> salesDetList, Sales sales,
            ObservableList<POSPayDetails> payDetails) throws JRException, SQLException {
            
    	java.sql.Date date = sales.getSalesId().getDate();
		String userName = sales.getSalesId().getUser_id().getUserName();
		long billNo = sales.getSalesId().getBill_no();
		Map<String, Object> parameters = new HashMap();
        parameters.put("companyName", MainController.getCompanyInfoSession().getCompanyName());
        parameters.put("streetAddress", MainController.getCompanyInfoSession().getAddressLine1());
        parameters.put("addressLine", MainController.getCompanyInfoSession().getAddressLine2());
        parameters.put("city", MainController.getCompanyInfoSession().getAddressLine3());
        parameters.put("telephoneNum", MainController.getCompanyInfoSession().getTelephoneNum());
        parameters.put("billNo", Long.valueOf(sales.getSalesId().getBill_no()));
        parameters.put("customerFname", sales.getCustomer().getFirstName());
        parameters.put("customerPhone", sales.getCustomer().getCustomerMobile());
        parameters.put("customerStreetAd", sales.getCustomer().getStreetAddress());
        parameters.put("customerAdLin2", sales.getCustomer().getAddressLine02());
        parameters.put("customerCity", sales.getCustomer().getCity());
		parameters.put("billNo", billNo);
                parameters.put("billPaymode", getPayModesString(payDetails));
		parameters.put("userName", userName);
		parameters.put("date", date);
		
		Connection connectionForReports = HibernateUtil.getSessionFactory().getSessionFactoryOptions()
				.getServiceRegistry().getService(ConnectionProvider.class).getConnection();
		
	   String reportLoc = "resources/jasperreports/pos_bill_reprint-with-cus-for New.jasper";
	   
	   JasperPrint jp = JasperFillManager.fillReport(reportLoc, parameters,connectionForReports);
	
       JasperPrintManager.printReport(jp, false);


    }
   
    
    // update last bill no data
    private void updateLastBillNo() {

        int lastNumber = SalesDaoImpl.getDao().getLastBillNo(MainController.getUserSession(),
                java.sql.Date.valueOf(LocalDate.now()));

        lastBillNo = 1 + lastNumber;

        System.out.println(lastBillNo);
        billNo.setText(String.valueOf(lastBillNo));

    }

    // load payment window for finalize
    public void loadPaymentWindow() throws IOException {

        if (!salesDetailsList.isEmpty()) {

            if (paymentStage == null) {
                String loc = "/fxml/POSPayments.fxml";
                FXMLLoader loader = new FXMLLoader(getClass().getResource(loc));

                Scene scene = new Scene(loader.load());
                PosPaymentController getControl = loader.getController();

                paymentStage = new Stage(StageStyle.DECORATED);
                paymentStage.initOwner(discTextField.getScene().getWindow());
                paymentStage.initModality(Modality.WINDOW_MODAL);
                paymentStage.setScene(scene);
                paymentStage.show();
                getControl.setBillingFXLoader(this.thiLoader);
                getControl.setBillDetails(billDicount, billAmount, billAmount);
                getControl.loadMainPayModes();
                getControl.manipulateKeyInputs();
                this.paymentFXLoader = loader;

            } else if (paymentStage.isShowing()) {
                paymentStage.toFront();

            } else {

                PosPaymentController controlMe = paymentFXLoader.getController();
                controlMe.setBillDetails(billDicount, billAmount, billAmount);
                controlMe.loadMainPayModes();
                // controlMe.manipulateKeyInputs();
                paymentStage.show();
            }
        }

    }

    // testing print with Java Swing DocFlavor
    public static void printBillWithJavax(ObservableList<SalesDetails> list, Sales sales, ObservableList<POSPayDetails> payDetails, String orgOrReprint) throws IOException {
        DocFlavor documentFlavor = DocFlavor.SERVICE_FORMATTED.PAGEABLE;
        PrintRequestAttributeSet patts = new HashPrintRequestAttributeSet();
        patts.add(Sides.DUPLEX);

        PrintService[] ps = PrintServiceLookup.lookupPrintServices(documentFlavor, patts);
        if (ps.length == 0) {
            throw new IllegalStateException("No Printer found");
        }

        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        PrintService myService = null;
        for (PrintService printService : printServices) {
            System.out.println(printService.getName());
            if (printService.getName().equals(MainController.getPrinterName())) {
                myService = printService;
                break;
            }
        }
        if (myService == null) {
            //throw new IllegalStateException("Printer not found");
            AlertHandler.getAlert(AlertType.WARNING, "Printer Not Found", "Cannot be Printed but data saved");
            return;
        }

        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        expected.writeBytes(POS.POSPrinter.Justification(POS.Justifications.Center));

        expected.writeBytes(POS.POSPrinter.SetStyles(POS.PrintStyle.Bold));
        expected.writeBytes(POS.POSPrinter.CharSize.DoubleWidth2());
        expected.writeBytes((MainController.getCompanyInfoSession().getCompanyName() + "\n").getBytes());
        expected.writeBytes(POS.POSPrinter.CharSize.Normal());
        expected.writeBytes((MainController.getCompanyInfoSession().getAddressLine1() + "\n").getBytes());
        expected.writeBytes((MainController.getCompanyInfoSession().getAddressLine2() + "\n").getBytes());
        expected.writeBytes((MainController.getCompanyInfoSession().getAddressLine3() + "\n").getBytes());
        expected.writeBytes((MainController.getCompanyInfoSession().getTelephoneNum() + "\n").getBytes());
        expected.writeBytes(POS.POSPrinter.SetStyles(POS.PrintStyle.None));
        expected.writeBytes(POS.POSPrinter.CharSize.Normal());
        expected.writeBytes(("-".repeat(48) + "\n").getBytes());
        expected.writeBytes(("Bill No: " + sales.getSalesId().getBill_no() + "    User : " + sales.getSalesId().getUser_id().getUserName() + "   Date :" + sales.getSalesId().getDate() + "\n").getBytes());
        expected.writeBytes(("-".repeat(48) + "\n").getBytes());
        expected.writeBytes(POS.POSPrinter.Justification(POS.Justifications.Left));
        expected.writeBytes("ItemId               Price     Qty  Total Value\n".getBytes());
        expected.writeBytes(("-".repeat(48) + "\n").getBytes());

        for (SalesDetails x : list) {
            // leftpadding with 00 max size of the Item code is set to 5
            String prdDesc;
            String prdAmount;
            if (x.isItemCancelStatus()) {
                prdDesc = String.format("%1$-" + 21 + "s", " Cancel :" + x.getProduct().getP_name());
                prdAmount = String.format("%1$" + 8 + "s", 0);
            } else {
                prdDesc = String.format("%1$-" + 21 + "s", x.getProduct().getP_name());
                prdAmount = String.format("%1$" + 8 + "s", String.format("%.2f", x.getItemAmount()));

            }

            expected.writeBytes(("Seq : " + x.getSalesDetId().getSeqNo() + "  " + prdDesc + "\n").getBytes());
            String prdId = String.format("%0" + 6 + "d", x.getProduct().getPrd_id());
            String prdPrice = String.format("%1$" + 8 + "s", String.format("%.2f", x.getUnitPrice()));

            String salesQty = String.format("%1$" + 8 + "s", x.getSalesQty());
            expected.writeBytes(("" + prdId + "            " + prdPrice + " " + salesQty + " " + prdAmount + "\n").getBytes());
        }
        expected.writeBytes(("-".repeat(48) + "\n").getBytes());

        expected.writeBytes(POS.POSPrinter.Justification(POS.Justifications.Right));
        expected.writeBytes(("Total Bill Value Rs:" + String.format("%.2f", sales.getGrossBillAmount()) + "\n").getBytes());
        expected.writeBytes(("Bill Discount Rs:" + String.format("%.2f", sales.getBillDiscount()) + "\n").getBytes());
        expected.writeBytes(("Net Bill Value Rs:" + String.format("%.2f", sales.getBillAmount()) + "\n").getBytes());
        expected.writeBytes(("Paid Value Rs:" + String.format("%.2f", sales.getAmountPaid()) + "\n").getBytes());
        for (POSPayDetails x : payDetails) {
            expected.writeBytes((x.getSubPayMode() + ": Rs " + String.format("%.2f", x.getAmount()) + "\n").getBytes());
        }
        expected.writeBytes(("Balance Rs :" + String.format("%.2f", sales.getBalance()) + "\n").getBytes());
        expected.writeBytes(("-".repeat(48) + "\n").getBytes());
        expected.writeBytes(("-".repeat(48) + "\n").getBytes());

        expected.writeBytes(POS.POSPrinter.SetStyles(POS.PrintStyle.None));
        expected.writeBytes(POS.POSPrinter.Justification(POS.Justifications.Center));

        expected.writeBytes(("Returns will be considered for fabric and \n").getBytes());
        expected.writeBytes(("clothing items only.All claims should be\n").getBytes());
        expected.writeBytes(("made within 3 days along with the reciept\n").getBytes());

        expected.writeBytes(("-".repeat(48) + "\n").getBytes());
        expected.writeBytes(("".repeat(48) + "\n").getBytes());

        expected.writeBytes("Thank you come again \n".getBytes());

        expected.writeBytes(("".repeat(48) + "\n").getBytes());
        expected.writeBytes(("-".repeat(48) + "\n").getBytes());

        expected.writeBytes(("Software by ProsinC Solutions \n").getBytes());
        expected.writeBytes(("Contact Us : +94 75 598 4483 \n").getBytes());
        expected.writeBytes(("".repeat(48) + "\n").getBytes());

        if (orgOrReprint.contentEquals("Original")) {
            expected.writeBytes(("Printed On : " + sales.getSalesId().getDate() + " At :" + sales.getEndTime() + "\n").getBytes());
        } else {
            expected.writeBytes(("Re Printed Bill \n").getBytes());
            expected.writeBytes(("Printed On : " + java.sql.Date.valueOf(LocalDate.now()) + " At :" + java.sql.Time.valueOf(LocalTime.now()) + "\n").getBytes());
        }

        expected.writeBytes(("".repeat(48) + "\n").getBytes());

        expected.writeBytes(POS.POSPrinter.CutPage());

        DocPrintJob job = myService.createPrintJob();
        Doc doc = new SimpleDoc(expected.toByteArray(), DocFlavor.BYTE_ARRAY.AUTOSENSE, null);

        try {
            String str = new String(expected.toByteArray());
            System.out.println(str);
            job.print(doc, new HashPrintRequestAttributeSet());

        } catch (PrintException e) {

            e.printStackTrace();
        }
    }

    // this will be called when loading the scene
    public void billWindowKeyEventHandling() {

    }

}
