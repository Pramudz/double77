package invsys.controllers.mainpage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.jfoenix.controls.JFXButton;

import invsys.controllers.billingwindow.BillingController;
import invsys.controllers.cashregister.CashRegisterController;
import invsys.controllers.creditsale.CreditInvoiceSettleController;
import invsys.controllers.creditsale.CreditSaleController;
import invsys.controllers.customer.CustomerController;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.controllers.goodreceived.GoodReceivedController;
import invsys.controllers.purchaseorder.PurchaseOrderController;
import invsys.controllers.reports.ReportController;
import invsys.controllers.salesrefund.SalesRefundController;
import invsys.controllers.suppliermaintanance.SupplierMaintananceController;
import invsys.controllers.supplierpayments.SupplierPaymentController;
import invsys.controllers.supplierreturn.SupplierReturnController;
import invsys.controllers.usermaintain.PasswordChangeController;
import invsys.controllers.usermaintain.UserController;
import invsys.entities.CashRegister;
import invsys.entities.Company;
import invsys.entities.Products;
import invsys.entities.RoleFunctions;
import invsys.entities.Users;
import invsys.entities.compositkeys.CashRegisterId;
import invsys.entitiydao.CashRegisterDao;
import invsys.entitiydao.UserDao;
import invsys.entitiydao.impl.CashRegisterDaoImpl;
import invsys.entitiydao.impl.UserDaoImpl;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class MainController implements Initializable {

    @FXML
    private Button menu;
    @FXML
    private VBox drawer;
    @FXML
    private HBox setOtherScene;

    @FXML
    private LineChart<String, Number> salesChart;
    @FXML
    CategoryAxis ixAxis;
    @FXML
    private BarChart<String, Double> productsChart;
    @FXML
    CategoryAxis pxAxis;

    @FXML
    private PieChart stockChart;

    @FXML
    private JFXButton buttonBilling;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label lastLoginLabel;
    @FXML
    private Label emailLabel;

    private FXMLLoader billingLoader;

    private FXMLLoader userSettingLoader;

    private FXMLLoader manageUserLoader;

    private FXMLLoader AddMedicineLoader;

    private FXMLLoader purchaseOrderLoader;

    private FXMLLoader supplierMainLoader;

    private FXMLLoader productLoader;

    private FXMLLoader reportLoader;

    private FXMLLoader RoleWindowLoader;

    private FXMLLoader goodReceivedNoteLoader;

    private FXMLLoader supplierPaymentLoader;

    private FXMLLoader dashBoardLoader;

    private FXMLLoader customerMasterLoader;

    private FXMLLoader creditInvoiceLoader;

    // stages of some direct loading FXML UIs
    private Stage priceUpdateStage;

    // catch data of the
    private Scene thisScene;
    private FXMLLoader thisMainLoader;
    private static Stage thisStage;
    // in order to Switch with Scenes node will get the scenes to Main windo
    private Node playWithMeNode;
    private static String dummyCustomer;

    // for other stages used in this main window in order to set null stage when
    // hidden
    private FXMLLoader otherSetHiddenLoader;
    private Stage otherStagesHidden;
    private Scene otherScenHidden;

    // in order to track the user session for whole application
    private static Users userSession;

    // in order to check login user has "Manager Dashboard" or Not with the function
    // of "updateUserRolesFinal"
    // if true - Manager Dash board data overall , else dashboard data as per the
    // user
    private static boolean dashbardManager = false;

    // in order to track the Company Info for whole application (Specially For
    // Reports- POSS Print, Purchase order Address)
    private static Company companyInfoSession;

    //07- June 2023 added to get Printer name from outside like database credentials
    private static String printerName;

    //Getter for Printer Name
    public static String getPrinterName() {
        return printerName;
    }

    //Setter for Printer Name - should exit and login to change
    public void setPrinterName(String printerName) {
        MainController.printerName = printerName;
    }

    // setter for user session will be set when login button is clicked
    public void setUserSession(Users user) {
        this.userSession = user;
    }

    // getter for user session
    public static Users getUserSession() {
        return userSession;
    }

    // getter for is Dashboard manager
    public static boolean isDashbardManager() {
        return dashbardManager;
    }

    // setter for dashboard manager
    public static void setDashbardManager(boolean dashbardManager) {
        MainController.dashbardManager = dashbardManager;
    }

    public static Company getCompanyInfoSession() {
        return companyInfoSession;
    }

    public void setCompanyInfoSession(Company companyInfoSession) {
        MainController.companyInfoSession = companyInfoSession;
    }

    public void setMyScene(Scene scene) {
        this.thisScene = scene;
    }

    public void setLoader(FXMLLoader loader) {
        thisMainLoader = loader;

    }

    public static String getDummyCustomer() {
        return dummyCustomer;
    }

    public void setDummyCustomer(String dummyCustomer) {
        this.dummyCustomer = dummyCustomer;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // DBHandler.getInstance();

        getMenuDrawerAction();

    }

    // load dash board
    public void loadDashBord() {

    }

    // Testing Phase
    // get Userrolefuncctions data in order to authorize role access from login
    // controller
    /*
	 * public void setRoleAcesses(List<RoleFunctions> rolefunc) {
	 * ObservableList<Node> array = formalizeVBox.getChildren(); ContextMenu
	 * contextMenu = new ContextMenu(); Node getButtonData = null; for (Node x :
	 * array) {
	 * 
	 * if (x instanceof JFXButton) { getButtonData = (JFXButton) x; } if (x
	 * instanceof MenuButton) { getButtonData = (MenuButton) x; }
	 * 
	 * x.setVisible(false); x.setManaged(false); String buttonName = ((Labeled)
	 * getButtonData).getText(); System.out.println("Main : " + buttonName); for
	 * (RoleFunctions roleF : rolefunc) {
	 * System.out.println(roleF.getRoleFunctions()); if
	 * (roleF.getRoleFunctions().equalsIgnoreCase(buttonName)) {
	 * 
	 * // experimental phase will be implemented sooon if
	 * (roleF.getRoleFunctions().equals("GRN")) { JFXButton button = (JFXButton) x;
	 * button.setOnAction(e -> { try { loadGoodReceivedNoteWindow(); } catch
	 * (IOException e1) { // TODO Auto-generated catch block e1.printStackTrace(); }
	 * }); } x.setVisible(true); x.setManaged(true); break; }
	 * 
	 * }
	 * 
	 * } }
     */
    // all the instance variables use for dynamic role base access in method
    // updateUserRolesTest
    @FXML
    private VBox formalizeVBox;
    private Multimap<String, String> subFeatureMap = ArrayListMultimap.create();
    List<JFXButton> listOfAllButtons = new ArrayList<JFXButton>();
    List<Accordion> listOfAllAccordians = new ArrayList<Accordion>();

    //in order keep the list of reports according to the user when loading the report window
    HashSet<RoleFunctions> reportSet = new HashSet<RoleFunctions>();

    /*
	 * // get UserRoles Formalize Method public void Depricated by following method
	 * updateUserRoles(List<RoleFunctions> rolefunc) {
	 * formalizeVBox.getChildren().clear();
	 * 
	 * for (RoleFunctions roleswithFunctions : rolefunc) {
	 * 
	 * if (roleswithFunctions.getMainRoleFunction() == null) { JFXButton button =
	 * new JFXButton(roleswithFunctions.getRoleFunctions());
	 * button.getStyleClass().add("menu-buttons-label"); button.setPrefWidth(250);
	 * button.setPrefHeight(50); button.setContentDisplay(ContentDisplay.LEFT);
	 * button.setAlignment(Pos.TOP_LEFT); formalizeVBox.getChildren().add(button); }
	 * 
	 * } }
     */
//	Optimize method to obtain functionalities based on user roles and role functions
    public void updateUserRolesFinal(List<RoleFunctions> rolefunc) {
        formalizeVBox.getChildren().clear();
        List<JFXButton> listofButtonsOnly = new ArrayList<JFXButton>();

        // for Button dashboard it can be accessed to every user
        JFXButton buttonDashBoard = new JFXButton("Dashboard");
        buttonDashBoard.getStyleClass().add("menu-buttons-label");
        buttonDashBoard.getStyleClass().remove("button");
        buttonDashBoard.setPrefWidth(250);
        buttonDashBoard.setContentDisplay(ContentDisplay.LEFT);
        buttonDashBoard.setAlignment(Pos.TOP_LEFT);
        listOfAllButtons.add(buttonDashBoard);
        formalizeVBox.getChildren().add(buttonDashBoard);

        // end dash board button
        for (RoleFunctions roleswithFunctions : rolefunc) {

            RoleFunctions funcNode = findNode(roleswithFunctions, "Reports");

            if (funcNode != null) {

                //since role map only support for 3 levels way can be used to derive leafnodes of the report category
                if (roleswithFunctions.getMainRoleFunction().getMainRoleFunction() != null) {
                    System.out.println("FUNC " + roleswithFunctions.getRoleFunction());
                    reportSet.add(roleswithFunctions);
                }

                continue;
            }

            if (!roleswithFunctions.getRoleFunction().equals("Bill Canceling")) {

                if (roleswithFunctions.getMainRoleFunction() != null) {

                    subFeatureMap.put(roleswithFunctions.getMainRoleFunction().getRoleFunction(),
                            roleswithFunctions.getRoleFunction());
                } else if (roleswithFunctions.getRoleFunction().equals("Manager Dashboard")) {
                    dashbardManager = true;
                } else {
                    subFeatureMap.put(roleswithFunctions.getRoleFunction(), roleswithFunctions.getRoleFunction());
                }
            }

        }

        for (String x : subFeatureMap.keySet()) {
            int countOfSubFeatures = 0;
            List<JFXButton> temporyButton = new ArrayList<JFXButton>();

            for (Entry<String, String> entry : subFeatureMap.entries()) {
                if (x.equals(entry.getKey())) {
                    countOfSubFeatures++;
                    if (!entry.getKey().equals(entry.getValue())) {
                        JFXButton button = new JFXButton(entry.getValue());
                        button.getStyleClass().add("subfeature-button");
                        button.setPrefWidth(250);
                        button.setContentDisplay(ContentDisplay.LEFT);
                        button.setAlignment(Pos.TOP_LEFT);

                        temporyButton.add(button);
                    }

                }

            }
            if (countOfSubFeatures > 1) {
                Accordion accrd = new Accordion();
                VBox menuBox = new VBox();

                menuBox.getStyleClass().add("sidebar-menu");
                menuBox.setPrefWidth(250);
                menuBox.getChildren().addAll(temporyButton);

                temporyButton.forEach(e -> {
                    VBox.setMargin(e, new Insets(0, 0, 10, 0));
                });

                TitledPane title = new TitledPane(x, menuBox);
                title.getStyleClass().add("menu-buttons-label");
                accrd.getPanes().add(title);
                listOfAllButtons.addAll(temporyButton);
                listOfAllAccordians.add(accrd);
            }
            if (countOfSubFeatures == 1) {
                JFXButton button = new JFXButton(x);
                button.getStyleClass().add("menu-buttons-label");
                button.getStyleClass().remove("button");
                button.setPrefWidth(250);
                button.setContentDisplay(ContentDisplay.LEFT);
                button.setAlignment(Pos.TOP_LEFT);
                listOfAllButtons.add(button);
                listofButtonsOnly.add(button);
            }
        }

        formalizeVBox.getChildren().addAll(listOfAllAccordians);
        formalizeVBox.getChildren().addAll(listofButtonsOnly);

        for (JFXButton x : listOfAllButtons) {

            x.setOnAction(e -> {

                try {
                    if (x.getText().equals("Dashboard")) {
                        loadDashBoard();
                    }

                    if (x.getText().equals("Create PO")) {
                        loadPurchaseOrder();
                    }
                    if (x.getText().equals("Grn Entry")) {
                        loadGoodReceivedNoteWindow();
                    }
                    if (x.getText().equals("User Maintenance")) {
                        loadUserMaintenance();
                    }
                    if (x.getText().equals("Product Maintenance")) {
                        loadProductWindow();
                    }

                    if (x.getText().equals("Billing")) {
                        loadBillingWindowFinal(e);
                    }

                    if (x.getText().equals("Reports")) {
                        loadReportWindow();
                    }

                    if (x.getText().equals("Supplier Master")) {
                        loadSupplierMaster();
                    }
                    if (x.getText().equals("User Roles")) {
                        loadRoleWindow();
                    }
                    if (x.getText().equals("Cash Registry")) {
                        loadCashRegister();
                    }

                    if (x.getText().equals("Create Category")) {
                        loadCategoryWindow();
                    }

                    if (x.getText().equals("Supplier Payments")) {
                        loadSupplierPaymentWindow();
                    }

                    if (x.getText().equals("Customer Master")) {
                        loadCustomerMasterWindow();
                    }

                    if (x.getText().equals("Credit Invoice")) {
                        loadCreditInvoice();
                    }

                    if (x.getText().equals("Price Update")) {
                        loadPriceUpdateWindow();
                    }

                    if (x.getText().equals("Expenses")) {
                        loadOverheadWindow();
                    }

                    if (x.getText().equals("Customer Refunds")) {
                        loadCustomerRefundWindow();
                    }

                    if (x.getText().equals("Company Info")) {
                        loadCompanyInfoWindow();
                    }

                    if (x.getText().equals("System Functions")) {
                        loadSystemFunctionWindow();
                    }

                    if (x.getText().equals("Supplier Return")) {
                        loadSupplierReturnWindow();
                    }

                    if (x.getText().equals("Settle Credit Invoice")) {
                        loadCreditInvoiceSettleWindow();
                    }

                } catch (Exception excp) {
                    excp.printStackTrace();
                }

            });
        }

    }

    private void loadSystemFunctionWindow() throws IOException {
        String loc = "/fxml/createRoleFeatures.fxml";

        otherSetHiddenLoader = new FXMLLoader(getClass().getResource(loc));
        otherScenHidden = new Scene(otherSetHiddenLoader.load());
        otherStagesHidden = new Stage();
        otherStagesHidden.setScene(otherScenHidden);
        otherStagesHidden.initModality(Modality.APPLICATION_MODAL);
        otherStagesHidden.setTitle("Cash Register");
        otherStagesHidden.show();

        otherStagesHidden.setOnHidden(e -> {
            otherStagesHidden = null;
            otherScenHidden = null;
            otherSetHiddenLoader = null;

        });
    }

    private RoleFunctions findNode(RoleFunctions func, String node) {
        if (func.getMainRoleFunction() == null) {
            return null;
        } else {
            if (func.getMainRoleFunction().getRoleFunction().contentEquals(node)) {
                return func;
            }
            func = findNode(func.getMainRoleFunction(), node);
            return func;
        }
    }
    // load user & Last Login data from Login Window

    public void setLoginUser(String user, String date, String email) {
        try {
            userNameLabel.setText(user);
            lastLoginLabel.setText(date);
            emailLabel.setText(email);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Getting Login Status");
            alert.setContentText(e.getLocalizedMessage());
            alert.showAndWait();
        }

    }

    // navigate menu items while drawing
    private void getMenuDrawerAction() {

        TranslateTransition openNav = new TranslateTransition(new Duration(350), drawer);
        openNav.setToX(0);

        TranslateTransition closeNav = new TranslateTransition(new Duration(350), drawer);
        menu.setOnAction(e -> {
            if (drawer.getTranslateX() != 0) {
                openNav.play();
                menu.getStyleClass().remove("hamburger-button");
                menu.getStyleClass().add("open-menu");

            } else {
                closeNav.setToX(-drawer.getWidth());
                closeNav.play();
                menu.getStyleClass().remove("open-menu");
                menu.getStyleClass().add("hamburger-button");

            }
        });
    }

    public void setLoginUser(String user, String email) {
        userNameLabel.setText(user);
        emailLabel.setText(email);

    }

    public String getUserName() {
        return this.userNameLabel.getText();
    }

    // load Billing Window
    public void loadBillingWindowFinal(ActionEvent event) throws Exception {

        CashRegisterDao cashRegDao = CashRegisterDaoImpl.getDao();
        double width = ((Node) event.getSource()).getScene().getWidth();
        double height = ((Node) event.getSource()).getScene().getHeight();
        CashRegisterDaoImpl.getDao();
        CashRegisterId regId = new CashRegisterId();
        regId.setRegistryDate(java.sql.Date.valueOf(LocalDate.now()));
        regId.setUser(userSession);
        CashRegister registry = cashRegDao.getCashRegister(regId);

        if (registry == null) {
            AlertHandler.getAlert(AlertType.ERROR, "Cash Register Error", "Please Register Cash Balance First");
            return;
        } else if (!registry.isSignInStatus()) {

            if (AlertHandler.getAlert(AlertType.CONFIRMATION, "Cash Registry Error",
                    "You Have already Registered and Closed for Today Are your Sure you want to continue with previous Registry ?")
                    .getResult().getButtonData().equals(ButtonData.OK_DONE)) {
                String loc = "/fxml/billingWindowFormalize.fxml";
                String title = "Billing";

                registry.setSignInStatus(true);
                if (cashRegDao.updateCashRegister(registry).getId().equals(regId)) {
                    if (billingLoader == null) {

                        billingLoader = new FXMLLoader(getClass().getResource(loc));
                        playWithMeNode = billingLoader.load();
                        BillingController control = billingLoader.getController();
                        control.setCashRegister(registry);
                        control.setThiLoader(billingLoader);
                        setOtherScene.setHgrow(playWithMeNode, Priority.ALWAYS);

                        setOtherScene.getChildren().setAll(playWithMeNode);
                        playWithMeNode = null;

                    } else {
                        playWithMeNode = billingLoader.getRoot();
                        setOtherScene.getChildren().setAll(playWithMeNode);
                    }
                } else {
                    AlertHandler.getAlert(AlertType.ERROR, "Fail Updating Cash Registry", "Contact Administrator");
                    return;
                }

            }
        } else {

            String loc = "/fxml/billingWindowFormalize.fxml";
            // String title = "Billing";
            if (billingLoader == null) {

                billingLoader = new FXMLLoader(getClass().getResource(loc));
                playWithMeNode = billingLoader.load();
                BillingController control = billingLoader.getController();
                control.setCashRegister(registry);
                control.setThiLoader(billingLoader);
                setOtherScene.setHgrow(playWithMeNode, Priority.ALWAYS);

                setOtherScene.getChildren().setAll(playWithMeNode);
                playWithMeNode = null;

            } else {
                playWithMeNode = billingLoader.getRoot();
                setOtherScene.getChildren().setAll(playWithMeNode);
            }

        }

        setMenuHideWhenLoadotherScreens();

    }

    // load Report Window
    public void loadReportWindow() throws IOException {

        String loc = "/fxml/reportsGui.fxml";

        if (reportLoader == null) {
            reportLoader = new FXMLLoader(getClass().getResource(loc));
            playWithMeNode = reportLoader.load();
            ReportController controller = reportLoader.getController();
            controller.setReportTreeViewWhenLoading(reportSet);
            HBox.setHgrow(playWithMeNode, Priority.ALWAYS);
            setOtherScene.getChildren().setAll(playWithMeNode);
            playWithMeNode = null;
        } else {
            playWithMeNode = reportLoader.getRoot();
            setOtherScene.getChildren().setAll(playWithMeNode);
        }
        setMenuHideWhenLoadotherScreens();

    }

    // load Grn Window
    public void loadGoodReceivedNoteWindow() throws IOException {

        String loc = "/fxml/grn/GrnGUi.fxml";

        if (goodReceivedNoteLoader == null) {
            goodReceivedNoteLoader = new FXMLLoader(getClass().getResource(loc));
            playWithMeNode = goodReceivedNoteLoader.load();
            GoodReceivedController controller = goodReceivedNoteLoader.getController();
            controller.setThisGrnLoader(goodReceivedNoteLoader);
            HBox.setHgrow(playWithMeNode, Priority.ALWAYS);
            setOtherScene.getChildren().setAll(playWithMeNode);
            playWithMeNode = null;
        } else {
            playWithMeNode = goodReceivedNoteLoader.getRoot();
            setOtherScene.getChildren().setAll(playWithMeNode);
        }
        setMenuHideWhenLoadotherScreens();

    }

    // load Supplier Payment Window
    public void loadSupplierPaymentWindow() throws IOException {

        String loc = "/fxml/supplier/SupplierPayments.fxml";

        if (supplierPaymentLoader == null) {
            supplierPaymentLoader = new FXMLLoader(getClass().getResource(loc));
            playWithMeNode = supplierPaymentLoader.load();
            SupplierPaymentController controller = supplierPaymentLoader.getController();
            controller.setThisSupplierPaymentLoader(supplierPaymentLoader);
            HBox.setHgrow(playWithMeNode, Priority.ALWAYS);
            setOtherScene.getChildren().setAll(playWithMeNode);
            playWithMeNode = null;
        } else {
            playWithMeNode = supplierPaymentLoader.getRoot();
            setOtherScene.getChildren().setAll(playWithMeNode);
        }
        setMenuHideWhenLoadotherScreens();

    }

    // load Customer Master Window
    public void loadCustomerMasterWindow() throws IOException {

        String loc = "/fxml/customer/CustomerGUI.fxml";

        if (customerMasterLoader == null) {
            customerMasterLoader = new FXMLLoader(getClass().getResource(loc));
            playWithMeNode = customerMasterLoader.load();
            CustomerController controller = customerMasterLoader.getController();
            controller.setThisMainLoader(customerMasterLoader);
            controller.setPosCustomer("Normal");
            setOtherScene.setHgrow(playWithMeNode, Priority.ALWAYS);
            setOtherScene.getChildren().setAll(playWithMeNode);
            playWithMeNode = null;
        } else {
            playWithMeNode = customerMasterLoader.getRoot();
            setOtherScene.getChildren().setAll(playWithMeNode);
        }
        setMenuHideWhenLoadotherScreens();

    }

    // load Report Window
    public void loadRoleWindow() throws IOException {

        String loc = "/fxml/RoleAndFeatures.fxml";

        if (RoleWindowLoader == null) {
            RoleWindowLoader = new FXMLLoader(getClass().getResource(loc));
            playWithMeNode = RoleWindowLoader.load();

            setOtherScene.setHgrow(playWithMeNode, Priority.ALWAYS);
            setOtherScene.getChildren().setAll(playWithMeNode);
            playWithMeNode = null;
        } else {
            playWithMeNode = RoleWindowLoader.getRoot();
            setOtherScene.getChildren().setAll(playWithMeNode);
        }
        setMenuHideWhenLoadotherScreens();

    }

    // load Supplier Window
    public void loadSupplierMaster() throws IOException {
        String loc = "/fxml/supplier/supplierMaintanance.fxml";

        if (supplierMainLoader == null) {
            supplierMainLoader = new FXMLLoader(getClass().getResource(loc));
            playWithMeNode = supplierMainLoader.load();
            setOtherScene.setHgrow(playWithMeNode, Priority.ALWAYS);
            setOtherScene.getChildren().setAll(playWithMeNode);
            playWithMeNode = null;
            SupplierMaintananceController controller = supplierMainLoader.getController();
            controller.setThisMainLoader(supplierMainLoader);
        } else {
            playWithMeNode = supplierMainLoader.getRoot();
            setOtherScene.getChildren().setAll(playWithMeNode);
        }
        setMenuHideWhenLoadotherScreens();

    }

    // load Create User Maintenance Window
    public void loadUserMaintenance() throws IOException {
        String loc = "/fxml/users/userGUI.fxml";
        if (manageUserLoader == null) {
            manageUserLoader = new FXMLLoader(getClass().getResource(loc));
            loadScenes(manageUserLoader);
            UserController control = manageUserLoader.getController();
            control.setThisMainLoader(manageUserLoader);
        } else {

            playWithMeNode = manageUserLoader.getRoot();
            setOtherScene.getChildren().setAll(playWithMeNode);
        }
        thisScene = null;
        setMenuHideWhenLoadotherScreens();

    }

    // load Purchase Order
    public void loadPurchaseOrder() throws IOException {
        String loc = "/fxml/purchase/purchase.fxml";
        if (purchaseOrderLoader == null) {
            purchaseOrderLoader = new FXMLLoader(getClass().getResource(loc));
            Products.setPurchaeOrderLoader(purchaseOrderLoader);
            loadScenes(purchaseOrderLoader);

            PurchaseOrderController controlMe = purchaseOrderLoader.getController();
            controlMe.setOrderLoader(purchaseOrderLoader);
            Products.setPurchaeOrderLoader(purchaseOrderLoader);

        } else {

            playWithMeNode = purchaseOrderLoader.getRoot();
            setOtherScene.getChildren().setAll(playWithMeNode);

        }
        thisScene = null;
        setMenuHideWhenLoadotherScreens();

    }

    // load cash Registry
    public void loadCashRegister() throws IOException {
        String loc = "/fxml/cashRegister.fxml";

        otherSetHiddenLoader = new FXMLLoader(getClass().getResource(loc));
        otherScenHidden = new Scene(otherSetHiddenLoader.load());
        CashRegisterController controller = otherSetHiddenLoader.getController();
        controller.updateUserAndDate(userSession.getUserName(), String.valueOf(LocalDate.now()));
        controller.setMainPageLoader(this.thisMainLoader);
        otherStagesHidden = new Stage();
        otherStagesHidden.setScene(otherScenHidden);
        otherStagesHidden.initModality(Modality.APPLICATION_MODAL);
        otherStagesHidden.setTitle("Cash Register");
        otherStagesHidden.show();

        otherStagesHidden.setOnHidden(e -> {
            otherStagesHidden = null;
            otherScenHidden = null;
            otherSetHiddenLoader = null;

        });

    }

    // load Category Window
    public void loadCategoryWindow() throws IOException {
        String loc = "/fxml/CategoryGUI.fxml";

        otherSetHiddenLoader = new FXMLLoader(getClass().getResource(loc));
        otherScenHidden = new Scene(otherSetHiddenLoader.load());
        otherStagesHidden = new Stage();
        otherStagesHidden.setScene(otherScenHidden);
        // otherStagesHidden.initModality(Modality.APPLICATION_MODAL);
        otherStagesHidden.setTitle("Category");
        otherStagesHidden.show();

        otherStagesHidden.setOnHidden(e -> {
            otherStagesHidden = null;
            otherScenHidden = null;
            otherSetHiddenLoader = null;

        });

    }

    // load password Change window
    public void loadPasswordChangeWindow() throws IOException {
        String loc = "/fxml/users/changePassword.fxml";

        otherSetHiddenLoader = new FXMLLoader(getClass().getResource(loc));
        otherScenHidden = new Scene(otherSetHiddenLoader.load());

        otherStagesHidden = new Stage();
        otherStagesHidden.setScene(otherScenHidden);
        otherStagesHidden.initModality(Modality.APPLICATION_MODAL);
        otherStagesHidden.initStyle(StageStyle.TRANSPARENT);
        PasswordChangeController controller = otherSetHiddenLoader.getController();
        controller.setUserDataAndMainStage(userSession, thisStage);
        // otherStagesHidden.setTitle("Change Password");
        otherStagesHidden.show();

        otherStagesHidden.setOnHidden(e -> {
            otherStagesHidden = null;
            otherScenHidden = null;
            otherSetHiddenLoader = null;

        });

    }

    // load product Maintenance Window
    public void loadProductWindow() throws IOException {
        String loc = "/fxml/productGUI.fxml";
        if (productLoader == null) {
            productLoader = new FXMLLoader(getClass().getResource(loc));
            loadScenes(productLoader);
        } else {
            playWithMeNode = productLoader.getRoot();
            setOtherScene.getChildren().setAll(playWithMeNode);
        }
        setMenuHideWhenLoadotherScreens();
    }

    // load customer credit invoice window
    private void loadCreditInvoice() throws IOException {
        String loc = "/fxml/creditInvoice.fxml";
        if (creditInvoiceLoader == null) {
            creditInvoiceLoader = new FXMLLoader(getClass().getResource(loc));
            loadScenes(creditInvoiceLoader);
            CreditSaleController controller = creditInvoiceLoader.getController();
            controller.setThiLoader(creditInvoiceLoader);
        } else {
            playWithMeNode = creditInvoiceLoader.getRoot();
            setOtherScene.getChildren().setAll(playWithMeNode);
        }
        setMenuHideWhenLoadotherScreens();

    }

    // load Main Window from Main Controller
    public void loadDashBoard() throws IOException {

        billingLoader = null;
        userSettingLoader = null;
        manageUserLoader = null;
        AddMedicineLoader = null;
        purchaseOrderLoader = null;
        supplierMainLoader = null;
        productLoader = null;
        reportLoader = null;
        RoleWindowLoader = null;
        goodReceivedNoteLoader = null;
        playWithMeNode = null;
        dashBoardLoader = null;
        String loc = "/fxml/dashboard.fxml";
        dashBoardLoader = new FXMLLoader(getClass().getResource(loc));
        playWithMeNode = dashBoardLoader.load();
        setOtherScene.setHgrow(playWithMeNode, Priority.ALWAYS);
        setOtherScene.getChildren().setAll(playWithMeNode);
        playWithMeNode = null;
        setMenuHideWhenLoadotherScreens();

    }

    // load Dashbord when login
    public void loadDashBoardFromLogin() throws IOException {

        billingLoader = null;
        userSettingLoader = null;
        manageUserLoader = null;
        AddMedicineLoader = null;
        purchaseOrderLoader = null;
        supplierMainLoader = null;
        productLoader = null;
        reportLoader = null;
        RoleWindowLoader = null;
        goodReceivedNoteLoader = null;
        playWithMeNode = null;
        dashBoardLoader = null;
        String loc = "/fxml/dashboard.fxml";
        dashBoardLoader = new FXMLLoader(getClass().getResource(loc));
        playWithMeNode = dashBoardLoader.load();
        setOtherScene.setHgrow(playWithMeNode, Priority.ALWAYS);
        setOtherScene.getChildren().setAll(playWithMeNode);
        playWithMeNode = null;
        setMenuHideWhenLoadotherScreens();

    }

    // price update stage loading method
    private void loadPriceUpdateWindow() throws IOException {
        if (priceUpdateStage == null) {
            String loc = "/fxml/priceUpdate.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(loc));

            Scene scene = new Scene(loader.load());
            priceUpdateStage = new Stage(StageStyle.DECORATED);
            priceUpdateStage.initModality(Modality.NONE);
            priceUpdateStage.setScene(scene);
            priceUpdateStage.show();

        } else if (priceUpdateStage.isShowing()) {
            priceUpdateStage.toFront();

        } else {
            priceUpdateStage.show();
        }

    }

    // load overhead window
    private void loadOverheadWindow() throws IOException {

        String loc = "/fxml/overheads.fxml";

        otherSetHiddenLoader = new FXMLLoader(getClass().getResource(loc));
        otherScenHidden = new Scene(otherSetHiddenLoader.load());
        otherStagesHidden = new Stage();
        otherStagesHidden.setScene(otherScenHidden);
        otherStagesHidden.initModality(Modality.APPLICATION_MODAL);
        otherStagesHidden.setTitle("Monthly Overheads");
        otherStagesHidden.show();
        otherStagesHidden.setOnHidden(e -> {
            otherStagesHidden = null;
            otherScenHidden = null;
            otherSetHiddenLoader = null;

        });

        setMenuHideWhenLoadotherScreens();

    }

    // load Customer Refund window
    private void loadCustomerRefundWindow() throws IOException {

        String loc = "/fxml/customerRefund.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(loc));
        Node node = loader.load();
        SalesRefundController controller = loader.getController();
        controller.setThiLoader(loader);
        setOtherScene.setHgrow(node, Priority.ALWAYS);
        setOtherScene.getChildren().setAll(node);
        setMenuHideWhenLoadotherScreens();

    }

    // load Credit Invoice Settle
    private void loadCreditInvoiceSettleWindow() throws IOException {

        String loc = "/fxml/creditInvoiceSettle.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(loc));
        Node node = loader.load();
        CreditInvoiceSettleController controller = loader.getController();
        controller.setThiLoader(loader);
        setOtherScene.setHgrow(node, Priority.ALWAYS);
        setOtherScene.getChildren().setAll(node);
        setMenuHideWhenLoadotherScreens();

    }

    // load Supplier Return Window
    private void loadSupplierReturnWindow() throws IOException {

        String loc = "/fxml/supplier/SupplierReturn.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(loc));
        Node node = loader.load();
        SupplierReturnController controller = loader.getController();
        controller.setThisSupplierReturnLoader(loader);
        setOtherScene.setHgrow(node, Priority.ALWAYS);
        setOtherScene.getChildren().setAll(node);
        setMenuHideWhenLoadotherScreens();

    }

    // load Company Info window without instance variable
    public void loadCompanyInfoWindow() throws IOException {

        String loc = "/fxml/CompanyInfo.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(loc));
        Node node = loader.load();
        setOtherScene.setHgrow(node, Priority.ALWAYS);
        setOtherScene.getChildren().setAll(node);
        setMenuHideWhenLoadotherScreens();

    }

    public void closeMe() {
        thisStage.setOnHidden(e -> {
            thisStage = null;
            System.exit(1);

        });

        if (AlertHandler.getAlert(AlertType.CONFIRMATION, "Are you Sure You Want to Logout", null).getResult()
                .getButtonData().equals(ButtonData.OK_DONE)) {
            userSession.setLoginStatus(false);
            UserDao userDao = UserDaoImpl.getDao();
            userDao.updateUser(userSession);
            thisStage.close();
        }

    }

    public static Stage getThisStage() {
        return thisStage;
    }

    public static void setThisStage(Stage thisStage) {
        MainController.thisStage = thisStage;
    }

    // in order for hide menu bar when load new Scene to Window
    public void setMenuHideWhenLoadotherScreens() {
        TranslateTransition closeNav = new TranslateTransition(new Duration(350), drawer);
        closeNav.setToX(-drawer.getWidth());
        closeNav.play();
        menu.getStyleClass().remove("open-menu");
        menu.getStyleClass().add("hamburger-button");

    }

    // for load Scene simple method use only for load create user method only
    // just for improving knowledge
    private void loadScenes(FXMLLoader loadExisting) throws IOException {

        playWithMeNode = loadExisting.load();
        setOtherScene.setHgrow(playWithMeNode, Priority.ALWAYS);
        setOtherScene.getChildren().setAll(playWithMeNode);
        playWithMeNode = null;
    }

}
