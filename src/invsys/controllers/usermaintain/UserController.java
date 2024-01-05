package invsys.controllers.usermaintain;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Predicate;

import org.apache.commons.codec.digest.DigestUtils;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.Notifications;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import invsys.controllers.formvalidation.AlertHandler;
import invsys.controllers.formvalidation.ValidateInputEach;
import invsys.controllers.formvalidation.ValidateInputs;
import invsys.entities.Role;
import invsys.entities.Users;
import invsys.entitiydao.RoleDao;
import invsys.entitiydao.UserDao;
import invsys.entitiydao.impl.RoleDaoImpl;
import invsys.entitiydao.impl.UserDaoImpl;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.StringConverter;

public class UserController implements Initializable {

	@FXML
	private JFXTextField viewItemField;

	@FXML
	private TextField userIdField;

	@FXML
	private TextField firstNameField;

	@FXML
	private TextField lastNameField;

	@FXML
	private TextField contactNumField;

	@FXML
	private TextField emailField;

	@FXML
	private TextField userNameField;

	@FXML
	private TextField lastLogField;

	@FXML
	private CheckBox userActiveCheckBox;

	@FXML
	private DatePicker dobDatePicker;

	@FXML
	private TextField addressField;

	@FXML
	private CheckComboBox<String> roleField;

	@FXML
	private PasswordField passwordField;

	@FXML
	private JFXTextField thisSearchTextField;

	@FXML
	private TableView<Users> itemTableView;

	@FXML
	private TableColumn<Users, Integer> colUserID;

	@FXML
	private TableColumn<Users, String> colFirstName;

	@FXML
	private TableColumn<Users, String> colLastName;

	@FXML
	private TableColumn<Users, java.sql.Date> colDob;

	@FXML
	private TableColumn<Users, String> colAddress;

	@FXML
	private TableColumn<Users, String> colContactNum;

	@FXML
	private TableColumn<Users, String> colEmail;

	@FXML
	private TableColumn<Users, String> colUserName;

	@FXML
	private TableColumn<String, String> colRole;

	@FXML
	private TableColumn<Users, java.sql.Date> colLastLogin;

	@FXML
	private TableColumn<Users, Boolean> colActiveStatus;

	@FXML
	private JFXButton actionButton;

	@FXML
	private JFXButton updateActionButton;
	
	
	@FXML
	private VBox tableDrawerVBox;
	
	@FXML
	private JFXButton buttonTableDrawer;
	
		@FXML
	    private Label fnameLabel;

	    @FXML
	    private Label lnameLabel;

	    @FXML
	    private Label doblabel;

	    @FXML
	    private Label addressLabel;

	    @FXML
	    private Label contactNumLabel;

	    @FXML
	    private Label emailLabel;

	    @FXML
	    private Label userNameLabel;

	    @FXML
	    private Label rolesLabel;

	private HashMap<String, Integer> roleMap;

	private ObservableList<String> roleList = FXCollections.observableArrayList();

	// specially created for update due to validate username update where same
	// username canbe updated
	private Users userInstance;

	// userload table related variables
	private Stage userLoadTableStage;

	// main loader for pass within the Scene
	@FXML
	private FXMLLoader thisMainLoader;

	// get loader details from MainController class in order pass data between
	// scenes
	public void setThisMainLoader(FXMLLoader thisMainLoader) {
		this.thisMainLoader = thisMainLoader;
	}

	// in order to swap user list within table view of main window & the select
	// user window to optimise data
	private ObservableList<Users> userListMain = FXCollections.observableArrayList();
	private FilteredList<Users> filteredDataList = new FilteredList<>(userListMain, e -> true);

	// in order to swap user list within table view of main window & the select
	// user window to optimise data
	public ObservableList<Users> getUserListMain() {
		return userListMain;
	}

	// in order to swap user list within table view of main window & the select
	// user window to optimise data
	public void setUserListMain(ObservableList<Users> userListMain) {
		this.userListMain = userListMain;
	}

	// setText Method from Usertable view to this Text field
	public void setviewItemField(String x) {
		this.viewItemField.setText(x);
	}

	//DAO - handler/Classes/Interfaces
	RoleDao roleDao = null;
	UserDao userDao = null;
	// initializing method 
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tableDrawerVBox.setVisible(false);
		
		ValidateInputEach.getInstance();
		
		ValidateInputs.getInstance();
		roleField.setDisable(true);

		roleMap = new HashMap<>();
		
		roleDao = RoleDaoImpl.getDao();
		userDao = UserDaoImpl.getDao();
		
		for (Object[] obj : roleDao.getRoleIdAndNames()) {
			roleMap.put(obj[0].toString(), (Integer) obj[1]);
			roleList.add(obj[0].toString());
			

		}
		roleField.getItems().setAll(roleList);
		lastLogField.setDisable(true);
		userIdField.setDisable(true);
		passwordField.setDisable(true);
		
			
		actionButton.setDisable(true);
		updateActionButton.setDisable(true);
		userActiveCheckBox.setSelected(false);
		initColumns();
		setDateFormatter();
		
		validateEachField();
		
		getTableDrawer();
	}

	private void initColumns() {
		colUserID.setCellValueFactory(new PropertyValueFactory<>("user_id"));
		colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		colDob.setCellValueFactory(new PropertyValueFactory<>("date"));
		colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
		colContactNum.setCellValueFactory(new PropertyValueFactory<>("contactNum"));
		colUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
		colEmail.setCellValueFactory(new PropertyValueFactory<>("userEmail"));
		colLastLogin.setCellValueFactory(new PropertyValueFactory<>("lastLogin"));
		colActiveStatus.setCellValueFactory(new PropertyValueFactory<>("activeStat"));
		colRole.setCellValueFactory(new PropertyValueFactory<>("rolesToTableView"));
	}

	@FXML
	void clearDataClicked() {
		userIdField.clear();
		viewItemField.clear();
		firstNameField.clear();
		dobDatePicker.getEditor().clear();
		dobDatePicker.setValue(null);
		dobDatePicker.setDisable(true);
		roleField.getCheckModel().clearChecks();
		roleField.setDisable(true);
		lastNameField.clear();
		contactNumField.clear();
		emailField.clear();
		userNameField.clear();
		lastLogField.clear();
		addressField.clear();
		userActiveCheckBox.setSelected(false);
		textFieldNotEditable();
		actionButton.setDisable(true);
		refreshTableData();
		userInstance = null;
		passwordField.clear();
		clearErrorLables();
	}
	
	@FXML
	void clearTextFieldsOnly() {
		userIdField.clear();
		firstNameField.clear();
		dobDatePicker.getEditor().clear();
		dobDatePicker.setValue(null);
		dobDatePicker.setDisable(true);
		roleField.getCheckModel().clearChecks();
		roleField.setDisable(true);
		lastNameField.clear();
		contactNumField.clear();
		emailField.clear();
		userNameField.clear();
		lastLogField.clear();
		addressField.clear();
		userActiveCheckBox.setSelected(false);
		actionButton.setDisable(true);
		passwordField.clear();

	}

	@FXML
	void setTextFieldEditable() {
		firstNameField.setEditable(true);
		lastNameField.setEditable(true);
		dobDatePicker.setEditable(true);
		dobDatePicker.setDisable(false);
		addressField.setEditable(true);
		contactNumField.setEditable(true);
		emailField.setEditable(true);
		userNameField.setEditable(true);
		// roleField.setEditable(true);
		roleField.setDisable(false);
		userActiveCheckBox.setDisable(false);
		removeTextFieldCss();
		addTextFieldCss();
	}

	@FXML
	void textFieldNotEditable() {
		firstNameField.setEditable(false);
		lastNameField.setEditable(false);
		dobDatePicker.setEditable(false);
		addressField.setEditable(false);
		contactNumField.setEditable(false);
		emailField.setEditable(false);
		userNameField.setEditable(false);
		// roleField.setEditable(false);
		userActiveCheckBox.setDisable(true);
		removeTextFieldCss();
		addOriginalCss();
	}

	// CSS for Error Handlings adding & Removing Methods

	private void addTextFieldCss() {
		lastLogField.getStyleClass().add("itext-field");
		userIdField.getStyleClass().add("itext-field");
		passwordField.getStyleClass().add("itext-field");
		firstNameField.getStyleClass().add("itext-field");
		lastNameField.getStyleClass().add("itext-field");
		addressField.getStyleClass().add("itext-field");
		dobDatePicker.getStyleClass().add("itext-field");
		contactNumField.getStyleClass().add("itext-field");
		emailField.getStyleClass().add("itext-field");
		userNameField.getStyleClass().add("itext-field");
		roleField.getStyleClass().add("itext-field");
		userActiveCheckBox.getStyleClass().add("itext-field");

	}

	private void addOriginalCss() {
		lastLogField.getStyleClass().add("set-not-editable-text");
		userIdField.getStyleClass().add("set-not-editable-text");
		passwordField.getStyleClass().add("set-not-editable-text");
		firstNameField.getStyleClass().add("set-not-editable-text");
		lastNameField.getStyleClass().add("set-not-editable-text");
		addressField.getStyleClass().add("set-not-editable-text");
		dobDatePicker.getStyleClass().add("set-not-editable-text");
		contactNumField.getStyleClass().add("set-not-editable-text");
		emailField.getStyleClass().add("set-not-editable-text");
		userNameField.getStyleClass().add("set-not-editable-text");
		roleField.getStyleClass().add("set-not-editable-text");
		userActiveCheckBox.getStyleClass().add("set-not-editable-text");

	}

	private void removeTextFieldCss() {
		lastLogField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		userIdField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		passwordField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		firstNameField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		lastNameField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		addressField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		dobDatePicker.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		contactNumField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		emailField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		userNameField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		roleField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		userActiveCheckBox.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");

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
					tableDrawerVBox.setVisible(false);
					buttonTableDrawer.setText("Open Table");
				}
			});
		}

	@FXML
	void createNewButton(ActionEvent event) {
		clearDataClicked();
		setTextFieldEditable();
		updateActionButton.setDisable(true);
		actionButton.setDisable(false);
		userIdField.setText(String.valueOf(userDao.getLastIndex() + 1));
		passwordField.setText(DigestUtils.sha1Hex("12345678"));
		
		roleMap.clear();
		roleField.getItems().clear();
		roleList.clear();
		for (Object[] obj : roleDao.getRoleIdAndNames()) {
			roleMap.put(obj[0].toString(), (Integer) obj[1]);
			roleList.add(obj[0].toString());
			

		}
		roleField.getItems().setAll(roleList);

	}

	// delete entity method
	@FXML
	void deleteButton(ActionEvent event) {
		if (userInstance != null) {
			
			if(!userInstance.isLoginStatus()) {
				
			
			if (AlertHandler.getAlert(AlertType.CONFIRMATION, "Are You Sure you want to Delete this",
					userInstance.getUserName()).getResult().getButtonData().equals(ButtonData.OK_DONE)) {
				
				

				if (userDao.deleteUser(userInstance) == userInstance.getUser_id()) {
					Notifications.create().title("Delete Success").graphic(null).hideAfter(Duration.seconds(1))
							.darkStyle().position(Pos.CENTER).show();
							clearDataClicked();
							updateActionButton.setDisable(true);
							actionButton.setDisable(true);

				}
			}
			}
			
			else {
				AlertHandler.getAlert(AlertType.ERROR, "this User Is Currently Logged In", "You Cannot Delete logged in user pls ask him to logout first");
			}
		}

		
	}

	@FXML
	void keyEventHandling(KeyEvent event) {

	}

	@FXML
	void loadItems(ActionEvent event) {
		if (userListMain == null || userListMain.isEmpty()) {
			userListMain.addAll(userDao.getUsersWithRoles());
		}

		// first while loop for user untill user is there end
		int i = 0;
		// second while loop unill users->role is there end
		int f = 0;

		while (i < userListMain.size()) {
			Users user = (Users) userListMain.toArray()[i];
			String x = "";

			// second while will restart from zero once the first loop is end
			// because need to get text in the same line
			f = 0;
			while (f < user.getRole().size()) {
				Role role = (Role) user.getRole().toArray()[f];

				x += role.getRoleName() + ",";
				f++;

			}

			user.setRolesToTableView(x);
			System.out.println(x);
			i++;
		}

		itemTableView.setItems(userListMain);
	}

	@FXML
	void qtyCellClicke(ActionEvent event) {

	}

	@FXML
	void loadUserAction(ActionEvent event) throws IOException {
		if (userLoadTableStage == null) {
			String loc = "/fxml/users/userTable.fxml";
			FXMLLoader loader = new FXMLLoader(getClass().getResource(loc));

			Scene scene = new Scene(loader.load());
			UserTableController getControl = loader.getController();

			if (!userListMain.isEmpty()) {

				getControl.setUserList(userListMain);
			}

			userLoadTableStage = new Stage(StageStyle.DECORATED);
			userLoadTableStage.initModality(Modality.APPLICATION_MODAL);
			userLoadTableStage.setScene(scene);
			userLoadTableStage.show();
			getControl.setUserMainStageloader(thisMainLoader);
			getControl.setUserTableStage(userLoadTableStage);
			System.out.println(thisMainLoader);

		} else if (userLoadTableStage.isShowing()) {
			userLoadTableStage.toFront();

		} else {
			userLoadTableStage.show();
		}
	}

	// when clicked the save button form will be updated
	@FXML
	void saveButtonClicked(ActionEvent event) {
		if (validateUserForm()) {
			System.out.println("Validated");

			try {
				Set<Role> tempRolesSet = new HashSet<>();
				for (String x : roleField.getCheckModel().getCheckedItems()) {
					int y = roleMap.get(x);
					Role role = roleDao.getRoleByIdAndName(y, x);
					tempRolesSet.add(role);
				}

				java.sql.Date gotBirthday = java.sql.Date.valueOf(dobDatePicker.getEditor().getText());
						
				java.sql.Date setLastLog = java.sql.Date.valueOf(LocalDate.now());

				int contactNum = 0;
				String email = null;
				
				if (!contactNumField.getText().isEmpty() || !contactNumField.getText().equals("")) {
					contactNum = Integer.parseInt(contactNumField.getText());
				}

				if (!emailField.getText().isEmpty() || !emailField.getText().trim().equals("") || !emailField.getText().isBlank()) {
					email = emailField.getText();
				}
				

				Users user = new Users(firstNameField.getText(), lastNameField.getText(), userNameField.getText(),
						gotBirthday, addressField.getText(), contactNum, email, tempRolesSet,
						passwordField.getText(), setLastLog, userActiveCheckBox.isSelected());

				List<Users> usersDuplicateCheckList = userDao.getUserForDuplicateInsertionCheck(userNameField.getText(),
						emailField.getText().trim());

				if (!usersDuplicateCheckList.isEmpty()) {
					AlertHandler.getAlert(AlertType.ERROR, "Duplicate Entry Error", ""
							+ "You Have Entered Duplicate Entries to the User Form");
					for(Users x : usersDuplicateCheckList) {
						if(x.getUserName().equals(userNameField.getText())) {
							userNameField.getStyleClass().add("danger-for-warning");
							userNameLabel.setText(x.getUserName()+" is occupied by another User");
						}
							
						if(x.getUserEmail() != null) {
							if(x.getUserEmail().equals(emailField.getText())) {
								emailField.getStyleClass().add("danger-for-warning");
								emailLabel.setText(x.getUserEmail()+" email has been occupied by other user");
							}
								
						}					
					}
					return;
										
				}

				else {
					if (AlertHandler
							.getAlert(AlertType.CONFIRMATION, "Confirmation",
									"Are you sure you want to create this user")
							.getResult().getButtonData().equals(ButtonData.OK_DONE)) {
						int userId = userDao.saveUserReturnLastIndex(user);
						Notifications.create().title("Update Success").graphic(null).hideAfter(Duration.seconds(1))
						.darkStyle().position(Pos.CENTER).show();
						clearDataClicked();
						userIdField.setText(String.valueOf(userId));
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	private void refreshTableData() {

		userListMain.clear();
		itemTableView.getItems().clear();
		userLoadTableStage = null;

	}

	public int getCurrentUserId() {
		return 0;
	}

	@FXML
	void searchItems(KeyEvent event) {
		thisSearchTextField.textProperty().addListener((ObservableValue, oldValue, newValue) -> {
			filteredDataList.setPredicate((Predicate<? super Users>) user -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				if (String.valueOf(user.getUser_id()).toLowerCase().contains(lowerCaseFilter)) {
					return true;
				} else if (user.getUserName().toLowerCase().contains(lowerCaseFilter)) {
					return true;
				}

				return false;
			});
		});

		SortedList<Users> sList = new SortedList<>(filteredDataList);

		sList.comparatorProperty().bind(itemTableView.comparatorProperty());

		itemTableView.setItems(sList);

	}

	@FXML
	void updateActionButtonClicked(ActionEvent event) {

		if (validateUserForm()) {
			System.out.println("Validated");

			try {
				Set<Role> tempRolesSet = new HashSet<>();
				for (String x : roleField.getCheckModel().getCheckedItems()) {
					int y = roleMap.get(x);
					Role role = roleDao.getRoleByIdWithoutJoins(y, x);
					tempRolesSet.add(role);
				}

				java.sql.Date gotBirthday = java.sql.Date.valueOf(dobDatePicker.getEditor().getText());
				java.sql.Date setLastLog = java.sql.Date.valueOf(LocalDate.now());

				int contactNum = 0;
				String email = null;

				if (!contactNumField.getText().isEmpty() || !contactNumField.getText().equals("")) {
					contactNum = Integer.parseInt(contactNumField.getText());
				}

				if (!emailField.getText().isEmpty() || !emailField.getText().trim().equals("") || !emailField.getText().isBlank()) {
					email = emailField.getText();
				}
				

				Users user = new Users(Integer.parseInt(userIdField.getText()), firstNameField.getText(),
						lastNameField.getText(), userNameField.getText(), gotBirthday, addressField.getText(),
						contactNum, email, tempRolesSet, passwordField.getText(), setLastLog,
						userActiveCheckBox.isSelected());
				
				
				List<Users> usersDuplicateCheckList = userDao.getUserForDuplicateUpdationCheck(Integer.parseInt(userIdField.getText()),
						userNameField.getText(),
						emailField.getText().trim());

				if (!usersDuplicateCheckList.isEmpty()) {
					AlertHandler.getAlert(AlertType.ERROR, "Duplicate Entry Error", ""
							+ "You Have Entered Duplicate Entries to the User Form");
					for(Users x : usersDuplicateCheckList) {
						if(x.getUserName().equals(userNameField.getText())) {
							userNameField.getStyleClass().add("danger-for-warning");
							userNameLabel.setText(x.getUserName()+" is occupied by another User");
						}
							
						if(x.getUserEmail() != null) {
							if(x.getUserEmail().equals(emailField.getText())) {
								emailField.getStyleClass().add("danger-for-warning");
								emailLabel.setText(x.getUserEmail()+" email has been occupied by other user");
							}
								
						}					
					}
					return;
					
				}

				else {
					if (AlertHandler
							.getAlert(AlertType.CONFIRMATION, "Confirmation",
									"Are you sure you want to Update this user")
							.getResult().getButtonData().equals(ButtonData.OK_DONE)) {
						if (userDao.updateUser(user)) {
							Notifications.create().title("Update Success").graphic(null).hideAfter(Duration.seconds(1))
									.darkStyle().position(Pos.CENTER).show();

							clearDataClicked();
						}

					}

				}

			} catch (Exception e) {
				e.printStackTrace();
				AlertHandler.getAlert(AlertType.ERROR, "You Updation Failed - Somthing Went Wrong in your Form", e.getLocalizedMessage());
			}

		}

	}

	@FXML
	void updateButton(ActionEvent event) {
		if (!viewItemField.getText().isEmpty() && actionButton.isDisable()) {
			setTextFieldEditable();
			updateActionButton.setDisable(false);
			passwordField.setText(DigestUtils.sha1Hex("12345678"));
		} else {
			AlertHandler.getAlert(AlertType.ERROR, "You Must Select the User First", "");
		}
	}

	@FXML
	public void viewInfoClicked() {
		try {
			if (actionButton.isVisible() || updateActionButton.isVisible()) {
				updateActionButton.setDisable(true);

				actionButton.setDisable(true);
			}

			clearTextFieldsOnly();
			dobDatePicker.setDisable(true);
			roleField.getCheckModel().clearChecks();
			roleField.setDisable(true);
			removeTextFieldCss();
			addOriginalCss();
			textFieldNotEditable();

			userInstance = userDao.getUserByIdWithRoles(Integer.parseInt(viewItemField.getText()));

			userIdField.setText(String.valueOf(userInstance.getUser_id()));
			userNameField.setText(userInstance.getUserName());
			firstNameField.setText(String.valueOf(userInstance.getFirstName()));
			lastNameField.setText(String.valueOf(userInstance.getLastName()));
			if(userInstance.getUserEmail() != null) {
				emailField.setText(String.valueOf(userInstance.getUserEmail()));
			}
			addressField.setText(String.valueOf(userInstance.getAddress()));
			passwordField.setText(String.valueOf(userInstance.getPassword()));
			dobDatePicker.setValue(userInstance.getDate().toLocalDate());
			// to both when dobDate picker is null then this
			dobDatePicker.getEditor().setText(userInstance.getDate().toLocalDate().toString());
			contactNumField.setText("0"+String.valueOf(userInstance.getContactNum()));
			lastLogField.setText(String.valueOf(userInstance.getLastLogin()));
			userActiveCheckBox.setSelected(userInstance.isActiveStat());

			userInstance.getRole().forEach(e -> {
				roleField.getCheckModel().check(e.getRoleName());

			});

		} catch (

		Exception e) {
			clearDataClicked();
			AlertHandler.getAlert(AlertType.ERROR, "User cannot Find- pls enter valid User Id",
					e.getLocalizedMessage());
			e.printStackTrace();

		}

	}

	// date formatter for date picker
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

		dobDatePicker.setConverter(converter);

	}

	private boolean validateUserForm() {
		return ValidateInputs.validateUserNames(firstNameField, "First Name Field")
				& ValidateInputs.validateUserNames(lastNameField, "Last Name Field")
				& ValidateInputs.validateDobField(dobDatePicker , "Date Birth Field") &
				ValidateInputs.validateInputEmpty(addressField,"City Field")
				& ValidateInputs.validateEmptyOnlyField(contactNumField, "contactnumber" , "Contact Number Field")
				& ValidateInputs.validateEmailEmptyAllowed(emailField, "Email Field")
				& ValidateInputs.validateUserNames(userNameField, "User Name Field")
				& ValidateInputs.validateInputEmptyGeneralBolean(roleField , "User Role Field");

	}
	
	
	// Validate Text Fields by Each for user Creation
	private void validateEachField() {
		
		firstNameField.setOnKeyReleased(e->{
			ValidateInputEach.validateUserNames(firstNameField, fnameLabel);
		});
		
		lastNameField.setOnKeyReleased(e->{
			ValidateInputEach.validateUserNames(lastNameField, lnameLabel);
		});
		
		dobDatePicker.setOnKeyReleased(e->{
			ValidateInputEach.validateDobField(dobDatePicker, doblabel);
		});
		
		dobDatePicker.setOnAction(e->{
			ValidateInputEach.validateDobField(dobDatePicker, doblabel);
		});
		
		addressField.setOnKeyReleased(e->{
			ValidateInputEach.validateInputEmpty(addressField, addressLabel);
		});
		
		emailField.setOnKeyReleased(e->{
			ValidateInputEach.validateEmailWithEmptyAllowed(emailField, emailLabel);
		});
		
		userNameField.setOnKeyReleased(e->{
			ValidateInputEach.validateUserNames(userNameField, userNameLabel);
		});
		
		contactNumField.setOnKeyReleased(e->{
			if(!contactNumField.getText().isBlank()) {
				ValidateInputEach.validateMobileNumbers(contactNumField, contactNumLabel);
			}
			
		});
		
		
		roleField.setOnKeyPressed(e->{
			ValidateInputEach.validateInputEmptyGeneralBolean(roleField, rolesLabel);
		});
	}

	
	// clear error fields
	private void clearErrorLables() {
		fnameLabel.setText("");
		lnameLabel.setText("");
		doblabel.setText("");
		addressLabel.setText("");
		emailLabel.setText("");
		userNameLabel.setText("");
		rolesLabel.setText("");
		contactNumLabel.setText("");
	}
	
	//Password Reset Button -Pramud - 12th June 2023
	
	@FXML
	void resetPassword() {
		if (userInstance != null) {
		if (validateUserForm()) {
			System.out.println("Validated");

			try {
				
				Users user = new Users();
				user.setUser_id(Integer.parseInt(userIdField.getText()));
				
					if (AlertHandler
							.getAlert(AlertType.CONFIRMATION, "Confirmation",
									"Are you sure you want to Reset Password of this user")
							.getResult().getButtonData().equals(ButtonData.OK_DONE)) {
						if (userDao.resetUserPassword(user)) {
							Notifications.create().title("User Reset Success").graphic(null).hideAfter(Duration.seconds(1))
									.darkStyle().position(Pos.CENTER).show();

							clearDataClicked();
						}

					}

				

			} catch (Exception e) {
				e.printStackTrace();
				AlertHandler.getAlert(AlertType.ERROR, "You Updation Failed - Somthing Went Wrong in your Form", e.getLocalizedMessage());
			}

		}
		
	} 
		else {
			AlertHandler.getAlert(AlertType.ERROR, "Error Resseting Password" ,"User Instance is Null Please select the User First to Reset Password");
		}
}
}
