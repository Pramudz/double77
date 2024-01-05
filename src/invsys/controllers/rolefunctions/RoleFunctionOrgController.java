package invsys.controllers.rolefunctions;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.CheckTreeView;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.TextFields;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import invsys.controllers.formvalidation.AlertHandler;
import invsys.entities.Role;
import invsys.entitiydao.RoleDao;
import invsys.entitiydao.impl.RoleDaoImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class RoleFunctionOrgController implements Initializable {

	@FXML
	private JFXComboBox<String> roleCombo;
	
	@FXML
	private CheckTreeView treeview;

	@FXML
	private JFXTextField roleNameField;

	@FXML
	private JFXButton loadExistingButton;

	@FXML
	private RadioButton updateRoleRadio;

	@FXML
	private RadioButton createRoleRadio;

	@FXML
	private JFXButton createRoleButton;

	@FXML
	private JFXButton updateRoleButton;

	@FXML
	private CheckBox billingCheck;

	@FXML
	private CheckBox itemMainCheck;

	@FXML
	private CheckBox reportCheck;

	@FXML
	private CheckBox userCheck;

	@FXML
	private CheckBox supplierCheck;
	
	private CheckBoxTreeItem<String> root;
	
	@FXML
	private GridPane gridChildsCheckBoxes;

	List<CheckBox> checkedList = new ArrayList<CheckBox>();

	HashMap<String, Integer> roleMap;
	ObservableList<String> roleNameList = FXCollections.observableArrayList();
	
	
    //Dao handlers/Classes/Interface
     RoleDao roleDao = null;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		roleDao = RoleDaoImpl.getDao();
		// get all checkeboxes in the grid pane to checkedlist from the fxml document
		for(Node x : gridChildsCheckBoxes.getChildren()) {
			
			if(x instanceof CheckBox) {
				CheckBox box = (CheckBox)x;
				checkedList.add(box);
			}
			
			
		}
		roleCombo.setEditable(true);
		getRoleIdAndNametoMap();

		createRoleButton.setDisable(true);
		updateRoleButton.setDisable(true);
		

		ToggleGroup radioGroup = new ToggleGroup();
		createRoleRadio.setToggleGroup(radioGroup);
		updateRoleRadio.setToggleGroup(radioGroup);

		radioActionForCreateUpdate();
		// commented due to new way of doing is found
		//addCheckboxesToList();

	}

	// clear text fields and checked fileds
	@FXML
	void clearRoleButtonClicked() {
		createRoleButton.setDisable(true);
		updateRoleButton.setDisable(true);
		checkedList.forEach(e -> {
			e.setSelected(false);
		});

		roleNameField.clear();
		createRoleRadio.setSelected(false);
		updateRoleRadio.setSelected(false);
		roleCombo.getEditor().clear();

	}

	// add all check boxes to the checked list when the initialization this method
	// will call
	// currently this has been depricated by me refer global instance from GridPane
	private void addCheckboxesToList() {
		checkedList.add(billingCheck);
		checkedList.add(itemMainCheck);
		checkedList.add(reportCheck);
		checkedList.add(userCheck);
		checkedList.add(supplierCheck);
	}

	// create & update radio button set on action function when initialize the
	// controller this will call
	private void radioActionForCreateUpdate() {
		createRoleRadio.setOnAction(e -> {
			if (createRoleRadio.isSelected()) {

				createRoleButton.setDisable(false);
				updateRoleButton.setDisable(true);
				
				roleNameField.setDisable(false);
				
				checkedList.forEach(f -> {
					f.setSelected(false);
				});
			}
		});

		updateRoleRadio.setOnAction(e -> {
			if (updateRoleRadio.isSelected()) {

				updateRoleButton.setDisable(false);
				createRoleButton.setDisable(true);
				
				roleNameField.setDisable(true);
				
				getRoleIdAndNametoMap();
				roleNameField.clear();
				checkedList.forEach(f -> {
					f.setSelected(false);
				});
			}
		});

	}

	// create role function
//	@FXML
//	void createRoleButtonClicked(ActionEvent event) {
//
//		try {
//
//			String roleName = roleNameField.getText();
//			Role role = new Role();
//			role.setRoleName(roleName);
//			ObservableList<RoleFunctions> roleFunctionsList = FXCollections.observableArrayList();
//			for (CheckBox x : checkedList) {
//				RoleFunctions roleFunc = new RoleFunctions(role, x.getText(), x.isSelected());
//				roleFunctionsList.add(roleFunc);
//			}
//			role.setRoleFunctions(roleFunctionsList);
//			if (AlertHandler.getAlert(AlertType.CONFIRMATION, "Are You sure You want to Create This", null).getResult()
//					.getButtonData().equals(ButtonData.OK_DONE)) {
//				if (RoleDaoImpl.saveRole(role)) {
//					Notifications.create().title("Update Success").graphic(null).hideAfter(Duration.seconds(1))
//							.darkStyle().position(Pos.CENTER).show();
//							clearRoleButtonClicked();
//				}
//			}
//
//		} catch (Exception e) {
//			AlertHandler.getAlert(AlertType.ERROR, "Cannot get Data", e.getMessage());
//			e.printStackTrace();
//		}
//
//	}
//	
	// delete role Button clicked function
	
	public void deleteRoleButtonClicked() {
		try {
			String x = roleCombo.getValue();
			Role role = roleDao.getRoleByIdAndName(roleMap.get(x), x);
			if (AlertHandler.getAlert(AlertType.CONFIRMATION, "Are You sure You Delete This", role.getRoleName()).getResult()
					.getButtonData().equals(ButtonData.OK_DONE)) {
				if(roleDao.deleteRole(role)) {
					Notifications.create().title("Delete Success").graphic(null).hideAfter(Duration.seconds(1))
					.darkStyle().position(Pos.CENTER).show();
					clearRoleButtonClicked();
					
				}
			}
			
		}catch(Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Error Deleting Role", e.getMessage());
			e.printStackTrace();
		}
	}

	// update role function
//	@FXML
//	void updateRoleButtonClicked() {
//		try {
//
//			String getRoletext = roleCombo.getEditor().getText();
//			int roleIdFromMap = roleMap.get(getRoletext);
//			Role role = RoleDaoImpl.getRoleByIdAndName(roleIdFromMap, getRoletext);
//
//			List<RoleFunctions> roleFunctionsList = role.getRoleFunctions();
//			int i =0;
//			// nested loop for each and every checked in check boxes for every check boxes
//		for (CheckBox x : checkedList) {
//
//				for (RoleFunctions roleFu : roleFunctionsList) {
//					if (x.getText().equals(roleFu.getRoleFunctions())) {
//						roleFu.setRoleAcess(x.isSelected());
//						System.out.println(i++);
//						break;
//						
//					}
//				}
//
//			}
//			role.setRoleFunctions(roleFunctionsList);
//			if (AlertHandler.getAlert(AlertType.CONFIRMATION, "Are You sure You Update This", null).getResult()
//					.getButtonData().equals(ButtonData.OK_DONE)) {
//				if (RoleDaoImpl.updateRole(role)) {
//					Notifications.create().title("Update Success").graphic(null).hideAfter(Duration.seconds(1))
//							.darkStyle().position(Pos.CENTER).show();
//							clearRoleButtonClicked();
//				}
//			}
//
//		} catch (Exception e) {
//			AlertHandler.getAlert(AlertType.ERROR, "Cannot get Data", e.getMessage());
//			e.printStackTrace();
//		}
//
//	}

	// this function will load list of roles when loading and select update radio
	// button
	private void getRoleIdAndNametoMap() {
		roleMap = new HashMap<String, Integer>();
		roleMap.clear();
		roleNameList.clear();
		roleCombo.getItems().clear();

		try {
			ObservableList<Object[]> array = roleDao.getRoleIdAndNames();
			for (Object[] obj : array) {
				roleMap.put(obj[0].toString(), (Integer) obj[1]);
				System.out.println(obj[0]);
				roleNameList.add((String) obj[0]);
			}
			roleCombo.getItems().addAll(roleNameList);
			TextFields.bindAutoCompletion(roleCombo.getEditor(), roleCombo.getItems());
		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Cannot Load Category Data", e.getLocalizedMessage());
		}

	}

	// function for load existing role access functions from the given role name
	// this will check the all check boxes when true of the relevant functionalty
//	@FXML
//	public void loadExitingButtonClicked() {
//
//		try {
//			String getRoletext = roleCombo.getEditor().getText();
//			int roleIdFromMap = roleMap.get(getRoletext);
//			Role role = RoleDaoImpl.getRoleByIdAndName(roleIdFromMap, getRoletext);
//			List<RoleFunctions> roleFunctionsList = role.getRoleFunctions();
//
//			// nested loop for each and every checked in check boxes from rolefunctionlist
//			int i=0;
//			for (CheckBox chk : checkedList) {
//				boolean status = false;
//				
//				innerloop:
//				for (RoleFunctions rolf : roleFunctionsList) {
//					if (chk.getText().equals(rolf.getRoleFunctions())) {
//						status = rolf.isRoleAcess();
//						System.out.println(i++);
//						break;
//					}
//										
//						
//				}
//				
//				chk.setSelected(status);
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			AlertHandler.getAlert(AlertType.ERROR, "cannot Load Existing Data", e.getLocalizedMessage());
//		}
//
//	}
}
