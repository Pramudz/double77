package invsys.controllers.usermaintain;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import com.jfoenix.controls.JFXTextField;

import invsys.controllers.creditsale.CreditInvoiceSettleController;
import invsys.controllers.salesrefund.SalesRefundController;
import invsys.entities.Users;
import invsys.entitiydao.UserDao;
import invsys.entitiydao.impl.UserDaoImpl;
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
import javafx.stage.Stage;

public class UserTableController implements Initializable {

	@FXML
	private TableView<Users> userTable;
	@FXML
	private TableColumn<Users, Integer> userIdCol;
	@FXML
	private TableColumn<Users, String> userNameCol;
	@FXML
	private JFXTextField searchUserTableField;

	private ObservableList<Users> userList = FXCollections.observableArrayList();
	private FilteredList<Users> filteredDataList = new FilteredList<>(userList, e -> true);

	// in order to pass UserTable data to Table View
	@FXML
	private FXMLLoader userMainStageloader;

	// in order to hide table view when pressed enter
	private Stage userTableStage;

	public void setUserTableStage(Stage stage) {
		this.userTableStage = stage;
	}

	// in order to pass UserTable data to Table View
	@FXML
	public void setUserMainStageloader(FXMLLoader userMainStageloader) {
		this.userMainStageloader = userMainStageloader;
	}

	// in order to optimse memory swap list data within the Loadtable view in
	// main form & this form
	public ObservableList<Users> getUserList() {
		return userList;
	}

	// in order to optimse memory swap list data within the Loadtable view in
	// main form & this form
	public void setUserList(ObservableList<Users> userList) {
		this.userList = userList;
	}

	//Dao handlers
	
	UserDao userDao = null;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		userDao = UserDaoImpl.getDao();
		searchUserTableField.requestFocus();
		initUserTableColumns();

		if (userList == null || userList.isEmpty()) {
			userList.addAll(userDao.getUsers());
		}

		userTable.setItems(userList);
		keyAndMouseHandling();
		searchItems();

	}

	private void initUserTableColumns() {
		userIdCol.setCellValueFactory(new PropertyValueFactory<>("user_id"));
		userNameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
	}

	
	
	private void keyAndMouseHandling() {

		userTable.setOnKeyReleased(e -> {

			if (e.getCode() == KeyCode.ENTER) {
				Users user = (Users) userTable.getSelectionModel().getSelectedItem();
				if (user == null)
					return;
				String x = String.valueOf(user.getUser_id());

				if(userMainStageloader.getController().getClass().equals(UserController.class)) {
					UserController controll = userMainStageloader.getController();
					controll.setviewItemField(x);
					controll.viewInfoClicked();
				}
				
				if(userMainStageloader.getController().getClass().equals(SalesRefundController.class)) {
					SalesRefundController controll = userMainStageloader.getController();
					controll.setUserNameFromUserTableView(user.getUserName());
				}
				
				if(userMainStageloader.getController().getClass().equals(CreditInvoiceSettleController.class)) {
					CreditInvoiceSettleController controll = userMainStageloader.getController();
					controll.setUserNameFiledFormUsertable(user.getUserName());
				}
				
				this.userTableStage.hide();
			}

		});

		searchUserTableField.setOnKeyReleased(e -> {
			if (e.getCode() == KeyCode.DOWN) {
				userTable.requestFocus();
				userTable.getSelectionModel().select(0);
			}
		});

		userTable.setOnMouseClicked(e -> {

			if (e.getClickCount() == 2) {
				Users user = (Users) userTable.getSelectionModel().getSelectedItem();
				if (user == null)
					return;
				String x = String.valueOf(user.getUser_id());

				
				if(userMainStageloader.getController().getClass().equals(UserController.class)) {
					UserController controll = userMainStageloader.getController();
					controll.setviewItemField(x);
					controll.viewInfoClicked();
				}
				
				if(userMainStageloader.getController().getClass().equals(SalesRefundController.class)) {
					SalesRefundController controll = userMainStageloader.getController();
					controll.setUserNameFromUserTableView(user.getUserName());
				}
				
				if(userMainStageloader.getController().getClass().equals(CreditInvoiceSettleController.class)) {
					CreditInvoiceSettleController controll = userMainStageloader.getController();
					controll.setUserNameFiledFormUsertable(user.getUserName());
				}
				
				this.userTableStage.hide();
			}

		});

	}

	// Search User Text Field
	public void searchItems() {
		searchUserTableField.textProperty().addListener((ObservableValue, oldValue, newValue) -> {
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

		sList.comparatorProperty().bind(userTable.comparatorProperty());

		userTable.setItems(sList);
	}
}
