package invsys.controllers.rolefunctions;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;

import org.controlsfx.control.CheckTreeView;
import org.controlsfx.control.Notifications;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import invsys.controllers.formvalidation.AlertHandler;
import invsys.controllers.formvalidation.ValidateInputs;
import invsys.entities.Role;
import invsys.entities.RoleFunctions;
import invsys.entitiydao.RoleDao;
import invsys.entitiydao.impl.RoleDaoImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class RoleFunctionController implements Initializable {

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

	@FXML
	private GridPane gridChildsCheckBoxes;

	private List<CheckBox> checkedList = new ArrayList<CheckBox>();

	private CheckBoxTreeItem<String> root;

	// this for left side form for updating and creating roles
	private HashMap<String, Integer> roleMap;

	// in order to avoid database call keep role features in map
	private HashMap<String, RoleFunctions> roleMaps;

	// in order set all feature data to the Tree view when initializing can call
	// seperately
	// when needed, this method uses multimap from goova map
	private Multimap<String, String> subFeatureMap = ArrayListMultimap.create();

	// to add data into the role name combo box when updating and load existing
	// roles
	private ObservableList<String> roleNameList = FXCollections.observableArrayList();

	List<Object[]> listOfLeafNodesFuntion;

	List<RoleFunctions> listOfAllFuntions;

	List<CheckBoxTreeItem<String>> listofTrees = new ArrayList<CheckBoxTreeItem<String>>();

	HashSet<CheckBoxTreeItem<String>> set = new HashSet<CheckBoxTreeItem<String>>(listofTrees);
	//Dao handlers/Classes/Interface

	RoleDao roleDao = null;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		roleDao = RoleDaoImpl.getDao();
		roleMaps = new HashMap<String, RoleFunctions>();
		 
		listOfLeafNodesFuntion = roleDao.getRoleFeaturesForTreeView();
		listOfAllFuntions = roleDao.getRoleFeatures();


		ValidateInputs.getInstance();
		//setTreeViewWhenInitializing();
		createRoleButton.setDisable(true);
		updateRoleButton.setDisable(true);

		ToggleGroup radioGroup = new ToggleGroup();
		createRoleRadio.setToggleGroup(radioGroup);
		updateRoleRadio.setToggleGroup(radioGroup);

		radioActionForCreateUpdate();
		getRoleIdAndNametoMap();
		setTreeViewWhenInitializingUpdated();
	}

	// set all feature data to the Tree view when initializing can call seperately
	// when needed, this method uses multimap from goova map
	// 11-12-2020
	private void setTreeViewWhenInitializingPrevious() {
		root = new CheckBoxTreeItem<String>("root");

		List<CheckBoxTreeItem<String>> listofTrees = new ArrayList<CheckBoxTreeItem<String>>();

		for (RoleFunctions features : roleDao.getRoleFeatures()) {
			if (features.getMainRoleFunction() != null) {
				subFeatureMap.put(features.getMainRoleFunction().getRoleFunction(), features.getRoleFunction());

			} else {
				CheckBoxTreeItem<String> x = new CheckBoxTreeItem<String>(features.getRoleFunction());
				listofTrees.add(x);
			}

			roleMaps.put(features.getRoleFunction(), features);
		}

		for (Entry<String, String> obj : subFeatureMap.entries()) {
			CheckBoxTreeItem<String> parentTree;

			for (CheckBoxTreeItem<String> checkBoxTreeItem : listofTrees) {
				if (obj.getKey().equals(checkBoxTreeItem.getValue().toString())) {

					CheckBoxTreeItem<String> tempItem = new CheckBoxTreeItem<String>(obj.getValue());
					checkBoxTreeItem.getChildren().add(tempItem);
					checkBoxTreeItem.setExpanded(true);
					checkBoxTreeItem.setIndependent(true);
				}

			}

		}

		root.getChildren().addAll(listofTrees);
		root.setExpanded(true);

		root.setIndependent(true);
		root.setIndeterminate(true);
		treeview.setRoot(root);
	}

	// 03-April-2021 Testing
	// funtionId, function Name , parentId, JoinedLeafNodeId, LeafName
	private void setTreeViewWhenInitializingUpdated() {
		roleMaps = new HashMap<String, RoleFunctions>();
		
		for(RoleFunctions func : listOfAllFuntions) {
			roleMaps.put(func.getRoleFunction(), func);
		}
			
		root = new CheckBoxTreeItem<String>("root");
		for (Object[] x : listOfLeafNodesFuntion) {
			String functionName = (String) x[1];
			Integer parentId = (Integer) x[2];
			// System.out.println(functionName);
			CheckBoxTreeItem<String> leafNode = new CheckBoxTreeItem<String>(functionName);
			CheckBoxTreeItem<String> parentView = getParentNodes(parentId, leafNode, leafNode);
			parentView.setExpanded(true);
			parentView.setIndependent(true);
			parentView.setIndeterminate(false);
			
			listofTrees.remove(parentView);
			listofTrees.add(parentView);
			}
		root.getChildren().setAll(listofTrees);
		root.setExpanded(true);
		root.setIndependent(true);
		treeview.setRoot(root);

	}

	
	//recursive function for deriving parent node from the given parentId from leafnode. support only untill 3 levels
	// report main parent has to be checked always if assigning sub reports
	private CheckBoxTreeItem<String> getParentNodes(Integer parentId, CheckBoxTreeItem<String> intermediateParent,
			CheckBoxTreeItem<String> leafNode) {
		if (parentId == null) {
			return leafNode;
		}
		for (RoleFunctions f : listOfAllFuntions) {
			if (parentId == f.getId()) {
				String temporyParentFunName = f.getRoleFunction();
				CheckBoxTreeItem<String> checkExistTreeItem = findNodeExistList(listofTrees, temporyParentFunName);	
				CheckBoxTreeItem<String> upperIntermediateNode = null;
				if (checkExistTreeItem != null) {
					System.out.println(temporyParentFunName + " : " + checkExistTreeItem.getValue());
					intermediateParent = checkExistTreeItem;		
					if (leafNode != null) {
						intermediateParent.getChildren().add(leafNode);
					}
					intermediateParent.setExpanded(true);
					intermediateParent.setIndependent(true);
					intermediateParent.setIndeterminate(false);
				}
				else {
					intermediateParent = new CheckBoxTreeItem(temporyParentFunName);
					if (leafNode != null) {
						intermediateParent.getChildren().add(leafNode);
					}	
				}
				if (f.getMainRoleFunction() != null) {
					upperIntermediateNode = getParentNodes(f.getMainRoleFunction().getId(), intermediateParent, null);
					System.out.println(upperIntermediateNode.getValue());
					upperIntermediateNode.getChildren().remove(intermediateParent);
					upperIntermediateNode.getChildren().add(intermediateParent);
					intermediateParent = upperIntermediateNode;		
					intermediateParent.setExpanded(true);
					intermediateParent.setIndependent(true);
					intermediateParent.setIndeterminate(false);
					
				}
				break;
			}
		}

		
		return intermediateParent;

	}

	
	//recursive search to check given tree item is exist or not and immediately return particular item if exist
	private CheckBoxTreeItem<String> findNodeExist(CheckBoxTreeItem<String> item, String toBeFinedTreeITem) {
		if (item == null) {
			return null;
		}
		if (item.getValue().toString().equalsIgnoreCase(toBeFinedTreeITem)) {
			return item;
		}
		else {
			/*
			 * ObservableList<TreeItem<String>> listOfNewSets =
			 * FXCollections.observableArrayList(); listOfNewSets.clear();
			 * listOfNewSets.addAll(item.getChildren()); int i = 0; for(i=0; i <
			 * listOfNewSets.size(); i++) { TreeItem<String> child = listOfNewSets.get(i);
			 * item = findNodeExist((CheckBoxTreeItem<String>) child, toBeFinedTreeITem);
			 * if(item != null) { return item;
			 * 
			 * } }
			 */
			for (TreeItem<String> child : item.getChildren()) {
				item = findNodeExist((CheckBoxTreeItem<String>) child, toBeFinedTreeITem);
				if (item != null) {
					return item;
				}
			}
			return null;
		}

	}

	
	//recursive search to check given parent tree view exist or not and immediately return particular parent tree view if exist
	private CheckBoxTreeItem<String> findNodeExistList(List<CheckBoxTreeItem<String>> listOfTrees,
			String toBeFinedTreeITem) {
		CheckBoxTreeItem<String> item = null;
		int i = 0;
		for (i = 0; i <listOfTrees.size(); i++) {
			item = findNodeExist(listOfTrees.get(i), toBeFinedTreeITem);
			if (item != null) {	
				break;
			}
		}
		return item;
	}

	// clear text fields and checked fileds
	@FXML
	void clearRoleButtonClicked() {
		createRoleButton.setDisable(true);
		updateRoleButton.setDisable(true);
		roleNameField.clear();
		createRoleRadio.setSelected(false);
		updateRoleRadio.setSelected(false);
		roleCombo.getEditor().clear();
		removeCheckedItemFromTheTreeView((CheckBoxTreeItem<String>) root);
		getRoleIdAndNametoMap();
	}

	// create & update radio button set on action function when initialize the
	// controller this will call
	private void radioActionForCreateUpdate() {
		createRoleRadio.setOnAction(e -> {
			if (createRoleRadio.isSelected()) {
				createRoleButton.setDisable(false);
				updateRoleButton.setDisable(true);
				roleNameField.setDisable(false);
				roleCombo.setDisable(true);
				removeCheckedItemFromTheTreeView((CheckBoxTreeItem<String>) root);

			}
		});

		updateRoleRadio.setOnAction(e -> {
			if (updateRoleRadio.isSelected()) {
				updateRoleButton.setDisable(false);
				createRoleButton.setDisable(true);
				roleNameField.setDisable(true);
				roleCombo.setDisable(false);
				getRoleIdAndNametoMap();
				roleNameField.clear();
				removeCheckedItemFromTheTreeView((CheckBoxTreeItem<String>) root);

			}
		});

	}

	// create role function
	@FXML
	void createRoleButtonClicked(ActionEvent event) {

		try {

			if (ValidateInputs.validateUserNames(roleNameField, "Role Name Field")) {
				String roleName = roleNameField.getText();
				Role role = new Role();
				role.setRoleName(roleName);
				Set<RoleFunctions> featuresSet = new HashSet<RoleFunctions>();
				findCheckedItem((CheckBoxTreeItem<String>) root, featuresSet);	
				role.setRoleFunctions(featuresSet);

				if (AlertHandler.getAlert(AlertType.CONFIRMATION, "Are You sure You want to Create This", null)
						.getResult().getButtonData().equals(ButtonData.OK_DONE)) {
					if (roleDao.saveRole(role)) {
						Notifications.create().title("Update Success").graphic(null).hideAfter(Duration.seconds(1))
						.darkStyle().position(Pos.CENTER).show();
						clearRoleButtonClicked();
					}
				}
			}
		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Cannot Insert Data", e.getMessage());
			e.printStackTrace();
		}

	}

	// recursive function in order to find the checkitem from the tree view and
	// added it to Set of Rolefuntion
	private void findCheckedItem(CheckBoxTreeItem<String> item, Set<RoleFunctions> setofRoleFunc) {

		if (item.isSelected() || item.isIndeterminate()) {
			setofRoleFunc.add(roleMaps.get(item.getValue()));
			System.out.println(item.getValue());
		}
		for (TreeItem<?> child : item.getChildren()) {
			findCheckedItem((CheckBoxTreeItem<String>) child, setofRoleFunc);
		}

	}

	// delete role Button clicked function

	public void deleteRoleButtonClicked() {
		try {
			removeCheckedItemFromTheTreeView((CheckBoxTreeItem<String>) root);
			String x = roleCombo.getValue();
			Role role = roleDao.getRoleByIdAndName(roleMap.get(x), x);
			Set<RoleFunctions> featuresSet = new HashSet<RoleFunctions>();
			findCheckedItem((CheckBoxTreeItem<String>) root, featuresSet);
			role.setRoleFunctions(featuresSet);
			if (AlertHandler.getAlert(AlertType.CONFIRMATION, "Are You sure You Delete This", null).getResult()
					.getButtonData().equals(ButtonData.OK_DONE)) {
				if (roleDao.deleteRole(role)) {
					Notifications.create().title("Delete Success").graphic(null).hideAfter(Duration.seconds(1))
					.darkStyle().position(Pos.CENTER).show();
					clearRoleButtonClicked();

				}
			}

		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Error Deleting Role", e.getMessage());
			e.printStackTrace();
		}
	}

	// update role function
	@FXML
	void updateRoleButtonClicked() {
		try {

			if (!roleCombo.getValue().isEmpty()) {

				String roleName = roleCombo.getValue();
				int roleId = roleMap.get(roleName);
				Role role = roleDao.getRoleByIdAndName(roleId, roleName);
				Set<RoleFunctions> featuresSet = new HashSet<RoleFunctions>();
				findCheckedItem((CheckBoxTreeItem<String>) root, featuresSet);
				role.setRoleFunctions(featuresSet);

				if (AlertHandler.getAlert(AlertType.CONFIRMATION, "Are You sure You want to Create This", null)
						.getResult().getButtonData().equals(ButtonData.OK_DONE)) {
					if (roleDao.updateRole(role)) {
						Notifications.create().title("Update Success").graphic(null).hideAfter(Duration.seconds(1))
						.darkStyle().position(Pos.CENTER).show();
						clearRoleButtonClicked();
					}
				}
			}
		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Cannot get Data", e.getMessage());
			e.printStackTrace();
		}
	}

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
				// System.out.println(obj[0]);
				roleNameList.add((String) obj[0]);
			}
			roleCombo.getItems().addAll(roleNameList);
		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Cannot Load  Data", e.getLocalizedMessage());
		}

	}

	// function for load existing role access functions from the given role name
	// this will check the all check boxes when true of the relevant functionalty
	@FXML
	public void loadExitingButtonClicked() {
		removeCheckedItemFromTheTreeView((CheckBoxTreeItem<String>) root);
		try {
			String getUpdateRole = roleCombo.getValue();
			int roleId = roleMap.get(getUpdateRole);
			Role role = roleDao.getRoleByIdAndName(roleId, getUpdateRole);
			Set<RoleFunctions> roleFunctionsSet = role.getRoleFunctions();
			setCheckedItemsOfTreeView((CheckBoxTreeItem<String>) root, roleFunctionsSet);

		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Cannot Load  Data", e.getLocalizedMessage());
			e.printStackTrace();
		}

	}

	// recursive function in order to set the checkitem from existing Rolename to
	// use with loadExistingButton Clicked Function
	private void setCheckedItemsOfTreeView(CheckBoxTreeItem<String> item, Set<RoleFunctions> setOfRoleFunc) {

		for (RoleFunctions roleFunc : setOfRoleFunc) {
			if (roleFunc.getRoleFunction().equalsIgnoreCase(item.getValue())) {
				item.setSelected(true);
				break;
			}
		}
		for (TreeItem<?> child : item.getChildren()) {
			setCheckedItemsOfTreeView((CheckBoxTreeItem<String>) child, setOfRoleFunc);
		}

	}

	// recursive function in order to remove the checkitem from existing Rolename to
	// use with loadExistingButton Clicked Function
	private void removeCheckedItemFromTheTreeView(CheckBoxTreeItem<String> item) {
		item.setSelected(false);
		item.setIndeterminate(false);
		for (TreeItem<?> child : item.getChildren()) {
			removeCheckedItemFromTheTreeView((CheckBoxTreeItem<String>) child);
		}

	}

}
