package invsys.controllers.login;

import java.io.FileInputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;

import invsys.controllers.cashregister.CashRegisterController;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.controllers.mainpage.MainController;
import invsys.entities.CashRegister;
import invsys.entities.Company;
import invsys.entities.Role;
import invsys.entities.RoleFunctions;
import invsys.entities.Users;
import invsys.entitiydao.CashRegisterDao;
import invsys.entitiydao.CompanyDao;
import invsys.entitiydao.RoleDao;
import invsys.entitiydao.UserDao;
import invsys.entitiydao.impl.CashRegisterDaoImpl;
import invsys.entitiydao.impl.CompanyDaoImpl;
import invsys.entitiydao.impl.RoleDaoImpl;
import invsys.entitiydao.impl.UserDaoImpl;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginController implements Initializable {

    @FXML
    TextField userText;

    @FXML
    PasswordField pass;

    @FXML
    private Button btnLogin;

    @FXML
    Label companyNameLabel;

    private Stage currentStage;
    private Stage getMainPage;

    int passwordTryCount = 0;

    private Company company;

    //DAO - Classes/Interfaces
    CompanyDao companyDao = null;
    CashRegisterDao cashRegDao = null;
    RoleDao roleDao = null;
    UserDao userDao = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        roleDao = RoleDaoImpl.getDao();
        companyDao = CompanyDaoImpl.getDao();
        userDao = UserDaoImpl.getDao();

        company = companyDao.getCompanyLast();

        if (company != null) {
            companyNameLabel.setText(company.getCompanyName());
        }
        
        Platform.runLater(() -> {
          userText.requestFocus();  
        });
       
        
    }

    public void loginButtonClicked() {
        userText.getStyleClass().remove("danger-for-warning");
        pass.getStyleClass().remove("danger-for-warning");
        try {
            if (!userText.getText().isEmpty() || !pass.getText().isEmpty()) {
                String usern = userText.getText().toString();
                String passw = DigestUtils.sha1Hex(pass.getText().toString());
                Users user = userDao.getUserByNameWithRoles(usern);
                String date = null;
                if (user != null) {

                    if (user.isActiveStat() == false) {
                        AlertHandler.getAlert(AlertType.ERROR, "Your Account has been Blocked", "Contact  Your Manager or Administrator");
                        System.exit(1);
                        return;
                    }

                    if (!user.getPassword().equals(passw)) {
                        passwordTryCount++;
                        pass.getStyleClass().add("danger-for-warning");
                        AlertHandler.getAlert(AlertType.ERROR, "Invalid Password", "You Have " + (3 - passwordTryCount) + " attempts left");
                        if (passwordTryCount >= 3) {
                            user.setActiveStat(false);
                            user.setLoginStatus(false);
                            if (userDao.updateUser(user)) {
                                AlertHandler.getAlert(AlertType.ERROR, "Your Account has been Blocked", "Contact  Your Manager or Administrator");
                                System.exit(1);
                            }
                        }
                    } else {

                        cashRegDao = CashRegisterDaoImpl.getDao();

                        List<CashRegister> getSigningCashRegList = cashRegDao.getNotSignOffCashRegisters(user);

                        if (!getSigningCashRegList.isEmpty()) {
                            if (AlertHandler.getAlert(AlertType.WARNING, "Cash Registry Sign off Error", "Your Previous Registers were not signed "
                                    + "of properly please close the registries before acessing the system")
                                    .getResult().getButtonData().equals(ButtonData.OK_DONE)) {

                                for (CashRegister reg : getSigningCashRegList) {
                                    java.sql.Date dateOfRegistry = reg.getLoginDate();

                                    String loc = "/fxml/cashRegister.fxml";
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource(loc));
                                    Scene scene = new Scene(loader.load());
                                    CashRegisterController controller = loader.getController();
                                    controller.cashRegistryLoadForSignOff(user, dateOfRegistry);
                                    Stage stage = new Stage();
                                    stage.setScene(scene);
                                    stage.showAndWait();
                                }
                            }
                        }
                        Set<Role> roleList = user.getRole();
                        List<RoleFunctions> roleFunctions = new ArrayList<RoleFunctions>();
                        /*
						 * // old way of handling role access for (Role roles : roleList) {
						 * 
						 * roleFunctions.addAll(roles.getRoleFunctions());
						 * 
						 * }
                         */

                        roleFunctions.addAll(roleDao.getDistinctRoleFeatures(user.getUserName()));

                        if (!(user.getLastLogin() == null)) {
                            date = user.getLastLogin().toString();
                        }

                        //added on 07th June 2023 get printer name from property class
                        Properties prop = new Properties();
                        prop.load(new FileInputStream("resources/system.properties"));
                        String printerName = prop.getProperty("printerName");
                        String dummyCustomerId = prop.getProperty("dummyCustomerId");
                        //

                        String email = user.getUserEmail();
                        user.setLastLogin(java.sql.Date.valueOf(LocalDate.now()));
                        user.setLoginStatus(true);
                        userDao.updateUser(user);
                        System.out.println("yes");
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainPageWithMenuScroll.fxml"));
                        Scene scene = new Scene(loader.load());
                        Stage stage = new Stage(StageStyle.DECORATED);
                        stage.setTitle("Inventory Management");
                        MainController setControllers = loader.getController();
                        setControllers.setLoginUser(usern, date, email);
                        setControllers.setPrinterName(printerName);
                        setControllers.setDummyCustomer(dummyCustomerId);
                        setControllers.setUserSession(user);
                        setControllers.updateUserRolesFinal(roleFunctions);
                        setControllers.setLoader(loader);
                        stage.getIcons().add(new Image("/images/Prosinc.jpg"));
                        stage.setScene(scene);
                        setControllers.setThisStage(stage);
                        setControllers.setMyScene(scene);

                        if (company != null) {
                            setControllers.setCompanyInfoSession(company);
                            setControllers.loadDashBoardFromLogin();
                        } else {
                            AlertHandler.getAlert(AlertType.INFORMATION, "Please Update The Company Profile First", "Making Better Company profile before accessing the system is must");
                            setControllers.loadCompanyInfoWindow();
                        }

                        stage.setOnCloseRequest(e -> {

                            e.consume();

                            if (AlertHandler.getAlert(AlertType.CONFIRMATION, "Are you Sure You Want to Logout", null).getResult()
                                    .getButtonData().equals(ButtonData.OK_DONE)) {
                                user.setLoginStatus(false);
                                userDao.updateUser(user);
                                stage.close();
                                System.exit(1);
                            }

                        });
                        stage.show();

                        currentStage.setOnHidden(e -> {
                            currentStage = null;
                        });
                        currentStage.close();

                    }

                } else {
                    userText.getStyleClass().add("danger-for-warning");
                    AlertHandler.getAlert(AlertType.ERROR, "Username Does Not Exist", "Please Enter Valid User Name");
                }
            } else {
                userText.getStyleClass().add("danger-for-warning");
                pass.getStyleClass().add("danger-for-warning");
            }

        } catch (Exception e) {
            AlertHandler.getAlert(AlertType.ERROR, "Error Logged In to the System", e.getLocalizedMessage());
//			JOptionPane.showMessageDialog(null, "Error" + e.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void closeWindow() {
        currentStage.close();
    }

    public void setStage(Stage stage) {
        this.currentStage = stage;
    }

    @FXML
    void txtUserNameKeyPress(KeyEvent event) {

        if (event.getCode() == KeyCode.ENTER) {
            if (!userText.getText().trim().equals("")) {
                pass.requestFocus();
            }
        }

    }

    @FXML
    void passKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (!pass.getText().trim().equals("")) {
                btnLogin.requestFocus();
            }
        }
    }

}
