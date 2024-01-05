package invsys.controllers.overhead;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;

import invsys.controllers.formvalidation.AlertHandler;
import invsys.controllers.formvalidation.ValidateInputs;
import invsys.controllers.mainpage.MainController;
import invsys.entities.MonthlyOverheads;
import invsys.entities.OverheadCategory;
import invsys.entities.compositkeys.MonthlyOverheadId;
import invsys.entitiydao.OverheadDao;
import invsys.entitiydao.impl.OverheadDaoImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class OverheadController implements Initializable{


		@FXML
	    private CheckBox createNewCheckBox;

	    @FXML
	    private TextField newOHeadCatField;

	    @FXML
	    private JFXComboBox<String> overheadCatCombo;

	    @FXML
	    private JFXComboBox<Short> yearCombo;

	    @FXML
	    private TextField amountField;

	    @FXML
	    private JFXComboBox<Month> monthCombo;
	    
	    @FXML
	    private JFXButton createNewOverheadButton;
	    
	    @FXML
	    private JFXRadioButton addEntryRadio;
	    
	    @FXML
	    private JFXRadioButton updateEntryRadio;
	    
	    @FXML
	    private JFXButton showButton;
	    
	    @FXML
	    private JFXButton updateButton;
	    
	    @FXML
	    private JFXButton saveButton;
	        
	    
	    //dao handlers
	    OverheadDao overheadDao = null;
	    
	    @Override
		public void initialize(URL location, ResourceBundle resources) {
	    	
	    	overheadDao = OverheadDaoImpl.getDao();
			ValidateInputs.getInstance();
		
			//button disable when first time loading
			addEntryRadio.setSelected(true);
			updateButton.setDisable(true);
			showButton.setDisable(true);
			
			ToggleGroup toogleGroup = new ToggleGroup();
			addEntryRadio.setToggleGroup(toogleGroup);
			updateEntryRadio.setToggleGroup(toogleGroup);
			
			//setting up addEntry Radio Button OnClicked
			
			addEntryRadio.setOnAction(e->{
				if(addEntryRadio.isSelected()) {
					saveButton.setDisable(false);
					updateButton.setDisable(true);
					showButton.setDisable(true);
				}
				
			});
			
			
			updateEntryRadio.setOnAction(e->{
				if(updateEntryRadio.isSelected()) {
					saveButton.setDisable(true);
					updateButton.setDisable(false);
					showButton.setDisable(false);
				}
				
			});
			
	    	createNewCheckBox.setOnAction(e->{
	    		if(createNewCheckBox.isSelected()) {
	    			newOHeadCatField.setDisable(false);
	    			createNewOverheadButton.setDisable(false);
	    		}
	    		else {
	    			newOHeadCatField.setDisable(true);
	    			createNewOverheadButton.setDisable(true);
	    		}
	    	});
	    	
	    	syncOverheadCategories();
	    	loadMonthAndYearsToCombo();
		}

	    @FXML
	    void clearButtonClicked() {
	    	
	    	monthCombo.getEditor().clear();
	    	yearCombo.getEditor().clear();
	    	createNewCheckBox.setSelected(false);
	    	newOHeadCatField.clear();
	    	overheadCatCombo.getSelectionModel().clearSelection();
	    	yearCombo.getSelectionModel().clearSelection();
	    	monthCombo.getSelectionModel().clearSelection();
	    	amountField.clear();
	    	newOHeadCatField.setDisable(true);
			createNewOverheadButton.setDisable(true);

	    }

	    @FXML
	    void createNewOheadClicked() {
	    	
	    	if(ValidateInputs.validateTextOnly(newOHeadCatField, "Overhead Category Field")){
	    		String overheadcat = newOHeadCatField.getText();
	    		if(AlertHandler.getAlert(AlertType.CONFIRMATION, "Are Your Sure ?", "You Want to Update Overhead Category : "+overheadcat).getResult().getButtonData().equals(ButtonData.OK_DONE)){
	    			
	    			OverheadCategory cat = new OverheadCategory();
	    			cat.setOverheadCategory(overheadcat);
	    			if(overheadDao.saveNewOverheadCategory(cat)) {
	    				AlertHandler.getAlert(AlertType.INFORMATION, "Success", null);
	    				clearButtonClicked();
	    			}
	    			
	    		}
	    		
	    	}
	    	

	    }

	    @FXML
	    void insertOverheadClicked() {
	    	if(validatedForm()) {
	    		try {
	    			short year = Short.valueOf(String.valueOf(yearCombo.getValue()));
	    			Month month = Month.valueOf(String.valueOf(monthCombo.getValue()));
	    			double amount = Double.parseDouble(amountField.getText());
	    			String overheadName = overheadCatCombo.getValue();
	    			OverheadCategory overHeadCat = overheadDao.getOverheadCategoryByName(overheadName);
	    			
	    			if(overHeadCat == null) {
	    				AlertHandler.getAlert(AlertType.ERROR, "Cannot Find Overhead Category", null);
	    			
	    			}else {
	    				MonthlyOverheadId monthlyOverheadId = new MonthlyOverheadId();
	    				monthlyOverheadId.setMonth(month);
	    				monthlyOverheadId.setOverheadCategory(overHeadCat);
	    				monthlyOverheadId.setYear(year);
	    				
	    				MonthlyOverheads overHead = new MonthlyOverheads();
	    				overHead.setAmount(amount);
	    				overHead.setDateOfEntry(java.sql.Date.valueOf(LocalDate.now()));
	    				overHead.setMonthlyOverheadId(monthlyOverheadId);
	    				overHead.setUser(MainController.getUserSession());
	    				
	    				if(AlertHandler.getAlert(AlertType.CONFIRMATION, "Are Your Sure ?", "You Want to Update this").getResult().getButtonData().equals(ButtonData.OK_DONE)){
	    	    				    	    			
	    	    			if(overheadDao.saveMonthlyOverhead(overHead)) {
	    	    				AlertHandler.getAlert(AlertType.INFORMATION, "Success", null);
	    	    				clearButtonClicked();
	    	    			}
	    	    			
	    	    		}
	    		
	    			}
	    			
	    		}catch(Exception e) {
	    			AlertHandler.getAlert(AlertType.ERROR, "Cannot Update Data", e.getLocalizedMessage());
	    			e.printStackTrace();
	    		}
	    	}

	    }

	    @FXML
	    void syncOverheadCategories() {
	    		    	
	    	overheadCatCombo.setItems(overheadDao.getOverheadCatNames());

	    }
	    
	    
	    @FXML
	    void updateOverheadClicked() {
	    	if(validatedForm()) {
	    		try {
	    			short year = Short.valueOf(String.valueOf(yearCombo.getValue()));
	    			Month month = Month.valueOf(String.valueOf(monthCombo.getValue()));
	    			double amount = Double.parseDouble(amountField.getText());
	    			String overheadName = overheadCatCombo.getValue();
	    			OverheadCategory overHeadCat = overheadDao.getOverheadCategoryByName(overheadName);
	    			
	    			if(overHeadCat == null) {
	    				AlertHandler.getAlert(AlertType.ERROR, "Cannot Find Overhead Category", null);
	    			
	    			}else {
	    				MonthlyOverheadId monthlyOverheadId = new MonthlyOverheadId();
	    				monthlyOverheadId.setMonth(month);
	    				monthlyOverheadId.setOverheadCategory(overHeadCat);
	    				monthlyOverheadId.setYear(year);
	    				
	    				MonthlyOverheads overHead = new MonthlyOverheads();
	    				overHead.setAmount(amount);
	    				overHead.setDateOfEntry(java.sql.Date.valueOf(LocalDate.now()));
	    				overHead.setMonthlyOverheadId(monthlyOverheadId);
	    				overHead.setUser(MainController.getUserSession());
	    				
	    				if(AlertHandler.getAlert(AlertType.CONFIRMATION, "Are Your Sure ?", "You Want to Update this").getResult().getButtonData().equals(ButtonData.OK_DONE)){
	    	    				    	    			
	    	    			if(overheadDao.updateMonthlyOverhead(overHead)) {
	    	    				AlertHandler.getAlert(AlertType.INFORMATION, "Success", null);
	    	    				clearButtonClicked();
	    	    			}
	    	    			
	    	    		}
	    		
	    			}
	    			
	    		}catch(Exception e) {
	    			AlertHandler.getAlert(AlertType.ERROR, "Cannot Update Data", e.getLocalizedMessage());
	    			e.printStackTrace();
	    		}
	    	}

	    }
	    
	    public void showExistingValue() {
	    	
	    	if(validateFormForLoadExistingAmount()) {
	    		try {
	    			short year = Short.valueOf(String.valueOf(yearCombo.getValue()));
	    			Month month = Month.valueOf(String.valueOf(monthCombo.getValue()));
	    		 	String overheadName = overheadCatCombo.getValue();
	    			OverheadCategory overHeadCat = overheadDao.getOverheadCategoryByName(overheadName);
	    			
	    			MonthlyOverheadId monthlyOverheadId = new MonthlyOverheadId();
	    			monthlyOverheadId.setMonth(month);
	    			monthlyOverheadId.setOverheadCategory(overHeadCat);
	    			monthlyOverheadId.setYear(year);
	    			
	    			MonthlyOverheads ohead = overheadDao.getMonthlyOverheadById(monthlyOverheadId);
	    			
	    			if(ohead != null) {
	    				amountField.setText(String.valueOf(ohead.getAmount()));
	    			}
	    			else {
	    				AlertHandler.getAlert(AlertType.ERROR, "Data Not Found", null);
	    			}
	    				
	    			}
	    			
	    			
	    		catch(Exception e) {
	    				AlertHandler.getAlert(AlertType.ERROR, "Cannot load Data", e.getLocalizedMessage());
		    			e.printStackTrace();
	    			}
	    		}
	    }
	    
	    private void loadMonthAndYearsToCombo() {
	    	ObservableList<Month> listOfMonth = FXCollections.observableArrayList(Month.values());
	    	monthCombo.setItems(listOfMonth);
	    	
	    	short nowYear = Short.parseShort(String.valueOf(Year.now()));
	    	ObservableList<Short> listOfyears = FXCollections.observableArrayList();
	    	listOfyears.add(nowYear);
	    	for(short i = 0; i < 10; i++) {
	    		nowYear++;
	    		listOfyears.add(nowYear);
	    	}
	    	
	    	yearCombo.setItems(listOfyears);
	    	
	    }
	    
	    private boolean validatedForm() {
	    	return ValidateInputs.validateInputEmptyComboWithoutEditorStringOnly(overheadCatCombo , "Overhead Category Field") &&
	    			ValidateInputs.validateInputEmptyComboWithoutEditorYear(yearCombo , "Year Field") &&
	    			ValidateInputs.validateInputEmptyComboWithoutEditorMonth(monthCombo , "Month Field") &&
	    			ValidateInputs.validatePrices(amountField,"Amount Field");
	    }
	    
	    private boolean validateFormForLoadExistingAmount() {
	    	return ValidateInputs.validateInputEmptyComboWithoutEditorStringOnly(overheadCatCombo , "Overhead Category Field") &&
	    			ValidateInputs.validateInputEmptyComboWithoutEditorYear(yearCombo ,"Year Field") &&
	    			ValidateInputs.validateInputEmptyComboWithoutEditorMonth(monthCombo , "Month Field");
	    }

		
	
}
