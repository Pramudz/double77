/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package invsys.controllers.overhead;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.controllers.formvalidation.ValidateInputs;
import invsys.controllers.mainpage.MainController;
import invsys.entities.OverheadCategory;
import invsys.entities.OverheadCategory_;
import invsys.entities.compositkeys.DailyOverheadId;
import invsys.entitiydao.DailyOverhead;
import invsys.entitiydao.impl.DailyOverheadImpli;
import invsys.entitiydao.impl.OverheadDaoImpl;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.util.StringConverter;

/**
 *
 * @author Kanishka
 */
public class DailyOverheadController implements Initializable {

    @FXML
    private JFXComboBox<String> overheadCatCombo;

    @FXML
    private DatePicker entryDateField;

    @FXML
    private TextField amountField;

    @FXML
    private TextField descrptionField;

    @FXML
    private JFXButton createNewOverheadButton;

    @FXML
    private JFXRadioButton addEntryRadio;

    @FXML
    private JFXRadioButton updateEntryRadio;

    @FXML
    private JFXButton saveButton;
    
     @FXML
    private JFXButton clearButton;

    private DailyOverhead _dailyOverhead;
    
    private final String datePattern = "yyyy-MM-dd";
    
    ToggleGroup toggleGroup;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        _dailyOverhead = DailyOverheadImpli.getDao();
        ValidateInputs.getInstance();
        toggleGroup = new ToggleGroup();
        addEntryRadio.setToggleGroup(toggleGroup);
        updateEntryRadio.setToggleGroup(toggleGroup);
        addEntryRadio.setSelected(true);
        syncOverheadCategories();
      
        entryDateField.setConverter(
                new StringConverter<>() {
            final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);

            @Override
            public String toString(LocalDate date) {
                return (date != null) ? dateFormatter.format(date) : "";
            }
            @Override
            public LocalDate fromString(String string) {
                return (string != null && !string.isEmpty())
                        ? LocalDate.parse(string, dateFormatter)
                        : null;
            }
        });
    }

    @FXML
    void insertOverheadClicked() {
        if (validateForm()) {
            try {
                String overheadName = overheadCatCombo.getValue();
                OverheadCategory ovheadCat = _dailyOverhead.getOverheadCategoryByName(overheadName);
                java.sql.Date entryDate = java.sql.Date.valueOf(entryDateField.getValue());
                String description = descrptionField.getText();
                double amount = Double.valueOf(amountField.getText());
                
                if(ovheadCat == null){
                    throw new Exception("Category Not Found");
                }
                
                DailyOverheadId id = new DailyOverheadId(ovheadCat, entryDate);            
                invsys.entities.DailyOverhead dailyOverhead = new invsys.entities.DailyOverhead(id, amount, description, MainController.getUserSession());
       
                if (addEntryRadio.isSelected()) {
                    if (_dailyOverhead.saveDailyOverhead(dailyOverhead)) {
                        AlertHandler.getAlert(Alert.AlertType.INFORMATION, "Success", null);
                        clearButtonClicked();
                    } else {
                        throw new Exception("Error occured passing entry");
                    }
                }

                if (updateEntryRadio.isSelected()) {
                    if (_dailyOverhead.updateDailyOverhead(dailyOverhead)) {
                        AlertHandler.getAlert(Alert.AlertType.INFORMATION, "Success", null);
                        clearButtonClicked();
                    } else {
                        throw new Exception("Error occured updating entry");
                    }
                }
            } catch (Exception e) {
                AlertHandler.getAlert(Alert.AlertType.ERROR, e.getMessage(), e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    void getEntryAmount() {
        try {
            if (updateEntryRadio.isSelected()) {
                String overheadName = overheadCatCombo.getValue();
                java.sql.Date entryDate = java.sql.Date.valueOf(entryDateField.getValue());
                OverheadCategory ovheadCat = _dailyOverhead.getOverheadCategoryByName(overheadName);
                DailyOverheadId dailyOverheadId = new DailyOverheadId(ovheadCat, entryDate);
                invsys.entities.DailyOverhead dailyOverhead = _dailyOverhead.getDailyOverheadById(dailyOverheadId);

                if (dailyOverhead == null) {
                    throw new Exception("No Entry for given category for the date "+entryDateField.getValue());
                }
                
                descrptionField.setText(dailyOverhead.getDescription());
                amountField.setText(String.valueOf(dailyOverhead.getAmount()));
            }
        } catch (Exception e) {
            AlertHandler.getAlert(Alert.AlertType.ERROR, e.getMessage(), e.getLocalizedMessage());
            clearButtonClicked();
        }

    }
    
    @FXML
    void clearButtonClicked(){
        overheadCatCombo.getSelectionModel().clearSelection();
        descrptionField.clear();
        amountField.clear(); 
    }

    private boolean validateForm() {
        return ValidateInputs.validateInputEmptyComboWithoutEditorStringOnly(overheadCatCombo, "Overhead Category Field")
                && ValidateInputs.validateDateField(entryDateField, "Date Field")
                && ValidateInputs.validateTextOnly(descrptionField, "Description Field")
                && ValidateInputs.validatePrices(amountField, "Amount Field");
    }
   
    private void syncOverheadCategories() {
        overheadCatCombo.setItems(_dailyOverhead.getOverheadCatNames());
    }
}
