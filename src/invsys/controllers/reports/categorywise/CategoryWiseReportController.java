package invsys.controllers.reports.categorywise;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.controlsfx.control.CheckComboBox;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

import com.jfoenix.controls.JFXDatePicker;

import invsys.controllers.database.HibernateUtil;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.controllers.formvalidation.ValidateInputs;
import invsys.entitiydao.CategoryDao;
import invsys.entitiydao.impl.CategoryDaoImpl;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class CategoryWiseReportController implements Initializable {

	@FXML
	private JFXDatePicker fromDate;

	@FXML
	private JFXDatePicker toDate;

	@FXML
	private CheckBox catWiseCheckBox;

	@FXML
	private CheckComboBox<String> catCheckCombo;

	@FXML
	private RadioButton summaryRadio;

	@FXML
	private RadioButton detailWiseRadio;

	@FXML
	private CheckBox movementQtyChkBox;

	@FXML
	private ComboBox<String> reportConditionsCombo;

	@FXML
	private TextField conditionedTextField;

	@FXML
	private CheckBox dateWiseChkBox;
	
	
	//category Dao for data
    CategoryDao categoryDao = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		ValidateInputs.getInstance();
		CategoryDaoImpl.getDao();
		ValidateInputs.setDateFormatter(fromDate);
		ValidateInputs.setDateFormatter(toDate);

		fromDate.setEditable(true);
		toDate.setEditable(true);

		ToggleGroup radioGroup = new ToggleGroup();
		detailWiseRadio.setToggleGroup(radioGroup);
		summaryRadio.setToggleGroup(radioGroup);

		// all check boxes and radio buttons actions initialized
		checkBoxAndRadioButtonActions();
		
		categoryDao = CategoryDaoImpl.getDao();
	}

	private void checkBoxAndRadioButtonActions() {

		// for selected category wise check box selected
		catWiseCheckBox.setOnAction(e -> {
			if (catWiseCheckBox.isSelected()) {
				catCheckCombo.setDisable(false);
				catCheckCombo.getItems().add("All");
				catCheckCombo.getItems().addAll(categoryDao.getCatNames());
				catCheckCombo.getCheckModel().checkIndices(0);
				conditionedTextField.clear();
			} else {
				catCheckCombo.setDisable(true);
				catCheckCombo.getItems().clear();
				conditionedTextField.clear();
			}
		});

		// for detail item wise radio button is selected
		detailWiseRadio.setOnAction(e -> {
			if (detailWiseRadio.isSelected()) {
				movementQtyChkBox.setDisable(false);
				conditionedTextField.clear();
			}
		});

		// for summary radio button is selected will disable all movement conditions
		summaryRadio.setOnAction(e -> {
			if (summaryRadio.isSelected()) {
				movementQtyChkBox.setDisable(true);
				conditionedTextField.setDisable(true);
				reportConditionsCombo.setDisable(true);
				reportConditionsCombo.getItems().clear();
				movementQtyChkBox.setSelected(false);
				conditionedTextField.clear();
			}
		});

		// for Movement Quantity check box is selected load all conditions to condition
		// combo and editable

		movementQtyChkBox.setOnAction(e -> {
			if (movementQtyChkBox.isSelected()) {
				conditionedTextField.setDisable(false);
				reportConditionsCombo.setDisable(false);
				reportConditionsCombo.getItems().add("=");
				reportConditionsCombo.getItems().add(">");
				reportConditionsCombo.getItems().add("<");
				reportConditionsCombo.getItems().add("<>");
				reportConditionsCombo.getItems().add("<=");
				reportConditionsCombo.getItems().add(">=");

				conditionedTextField.clear();
			} else {
				conditionedTextField.setDisable(true);
				reportConditionsCombo.setDisable(true);
				reportConditionsCombo.getItems().clear();
				conditionedTextField.clear();
			}
		});

		// check combo box selection property change

		catCheckCombo.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> c) {

				while (c.next()) {

					if (c.wasAdded()) {
						if (c.toString().contains("All")) {
							for (int i = 1; i < catCheckCombo.getItems().size(); i++) {
								if (catCheckCombo.getCheckModel().isChecked(i)) {
									catCheckCombo.getCheckModel().clearCheck(i);
								}
							}
						} else {
							if (catCheckCombo.getCheckModel().isChecked(0)) {
								catCheckCombo.getCheckModel().clearCheck("All");
							}
						}
					}
				}
			}
		});

	}

	@FXML
	void closeClicked() {

	}

	@FXML
	void viewClicked() {

		try {
			java.sql.Date fromDat = java.sql.Date.valueOf(fromDate.getValue());
			java.sql.Date toDat = java.sql.Date.valueOf(toDate.getValue());
			String userName = "Pramud";
			
			Map<String, Object> parameters = new HashMap();
			if (fromDat.after(toDat)) {
				AlertHandler.getAlert(AlertType.ERROR, "Date Range Error", "from Date should be less than to-date");
				return;
			}

			// Category Wise , Summary, Date wise
			if (catWiseCheckBox.isSelected() && summaryRadio.isSelected() && dateWiseChkBox.isSelected()) {

				// All Categories
				if (catCheckCombo.getCheckModel().getCheckedItems().contains("All")) {
					String loc = "resources/jasperreports/category_wise_all_daywise.jasper";
					parameters.put("fromDate", fromDat);
					parameters.put("toDate", toDat);
					parameters.put("printedBy", userName);
					viewReport(loc, parameters);

				}

				// Selected Categories
				else {
					String loc = "resources/jasperreports/category_wise_all_daywise_selected.jasper";
					parameters.put("fromDate", fromDat);
					parameters.put("toDate", toDat);
					parameters.put("printedBy", userName);
					parameters.put("categories", catCheckCombo.getCheckModel().getCheckedItems());
					viewReport(loc, parameters);

				}

			}

			// end Category Wise , Summary, Date wise

			// category wise, date wise
			else if (summaryRadio.isSelected() && dateWiseChkBox.isSelected()) {
				String loc = "resources/jasperreports/category_wise_all_daywise.jasper";
				parameters.put("fromDate", fromDat);
				parameters.put("toDate", toDat);
				parameters.put("printedBy", userName);
				viewReport(loc, parameters);
			}
			// end category wise date wise

			// Category Wise, Summary
			else if (catWiseCheckBox.isSelected() && summaryRadio.isSelected()) {

				// All Categories
				if (catCheckCombo.getCheckModel().getCheckedItems().contains("All")) {
					String loc = "resources/jasperreports/category_wise_all.jasper";
					parameters.put("fromDate", fromDat);
					parameters.put("toDate", toDat);
					parameters.put("printedBy", userName);
					viewReport(loc, parameters);

				}

				// Selected Categories
				else {
					String loc = "resources/jasperreports/category_wise_selected_cat.jasper";
					parameters.put("fromDate", fromDat);
					parameters.put("toDate", toDat);
					parameters.put("printedBy", userName);
					parameters.put("categories", catCheckCombo.getCheckModel().getCheckedItems());
					viewReport(loc, parameters);

				}
			}

			else if (summaryRadio.isSelected()) {

				String loc = "resources/jasperreports/category_wise_all.jasper";
				parameters.put("fromDate", fromDat);
				parameters.put("toDate", toDat);
				parameters.put("printedBy", userName);
				viewReport(loc, parameters);
			}
			// End Category Wise, Summary

			// start detail reports

			// Start Top Movement Detail ,Category Wise , Date Wise
			if (catWiseCheckBox.isSelected() && detailWiseRadio.isSelected() && dateWiseChkBox.isSelected()
					&& movementQtyChkBox.isSelected()) {

				if (ValidateInputs.validateInputEmptyComboWithoutEditorStringOnly(reportConditionsCombo , "Report Conditon Field")
						&& ValidateInputs.validatePrices(conditionedTextField ,"Report Conditon Text Field")) {
					String condition = reportConditionsCombo.getValue();
					Double conditonValue = Double.parseDouble(conditionedTextField.getText());

					// All Categories
					if (catCheckCombo.getCheckModel().getCheckedItems().contains("All")) {
						String loc = "resources/jasperreports/cat_daywise_itemwise_all.jasper";
						parameters.put("fromDate", fromDat);
						parameters.put("toDate", toDat);
						parameters.put("printedBy", userName);
						parameters.put("quantity", conditonValue);
						parameters.put("wherefunctionequal", condition);
						viewReport(loc, parameters);

					}

					// Selected Categories
					else {
						String loc = "resources/jasperreports/cat_daywise_itemwise_all_selected_cat.jasper";
						parameters.put("fromDate", fromDat);
						parameters.put("toDate", toDat);
						parameters.put("printedBy", userName);
						parameters.put("quantity", conditonValue);
						parameters.put("wherefunctionequal", condition);
						parameters.put("categories", catCheckCombo.getCheckModel().getCheckedItems());
						viewReport(loc, parameters);

					}

				}
			}

			// start Top Movement , Detail Item Wise , Date Wise (Not Selected Category)
			else if (!catWiseCheckBox.isSelected() && detailWiseRadio.isSelected() && dateWiseChkBox.isSelected()
					&& movementQtyChkBox.isSelected()) {
				if (ValidateInputs.validateInputEmptyComboWithoutEditorStringOnly(reportConditionsCombo, "Report Condition Field")
						&& ValidateInputs.validatePrices(conditionedTextField, "Report Condition Text Field")) {
					String condition = reportConditionsCombo.getValue();
					Double conditonValue = Double.parseDouble(conditionedTextField.getText());

					// All Categories

					String loc = "resources/jasperreports/cat_daywise_itemwise_all.jasper";
					parameters.put("fromDate", fromDat);
					parameters.put("toDate", toDat);
					parameters.put("printedBy", userName);
					parameters.put("quantity", conditonValue);
					parameters.put("wherefunctionequal", condition);
					viewReport(loc, parameters);

				}

			}

			// start Top Movement Selected Category , Detail Item Wise , (Not Date Wise)
			else if (catWiseCheckBox.isSelected() && detailWiseRadio.isSelected() && !dateWiseChkBox.isSelected()
					&& movementQtyChkBox.isSelected()) {

				if (ValidateInputs.validateInputEmptyComboWithoutEditorStringOnly(reportConditionsCombo , "Report Condition Field")
						&& ValidateInputs.validatePrices(conditionedTextField ,"Report Condition Text Field")) {
					String condition = reportConditionsCombo.getValue();
					Double conditonValue = Double.parseDouble(conditionedTextField.getText());

					// All Categories
					if (catCheckCombo.getCheckModel().getCheckedItems().contains("All")) {
						String loc = "resources/jasperreports/cat_itemwise_all.jasper";
						parameters.put("fromDate", fromDat);
						parameters.put("toDate", toDat);
						parameters.put("printedBy", userName);
						parameters.put("quantity", conditonValue);
						parameters.put("wherefunctionequal", condition);
						viewReport(loc, parameters);

					}

					// Selected Categories
					else {
						String loc = "resources/jasperreports/catwise_item_wise_selected_cat.jasper";
						parameters.put("fromDate", fromDat);
						parameters.put("toDate", toDat);
						parameters.put("printedBy", userName);
						parameters.put("quantity", conditonValue);
						parameters.put("wherefunctionequal", condition);
						parameters.put("categories", catCheckCombo.getCheckModel().getCheckedItems());
						viewReport(loc, parameters);

					}

				}

			}
			// End Top Movement Selected Category , Detail Item Wise , (Not Date Wise)

			// start Top Movement , Detail Item Wise , (Not Date Wise) (Not Selected
			// Category)
			else if (!catWiseCheckBox.isSelected() && detailWiseRadio.isSelected() && !dateWiseChkBox.isSelected()
					&& movementQtyChkBox.isSelected()) {

				if (ValidateInputs.validateInputEmptyComboWithoutEditorStringOnly(reportConditionsCombo , "Report Condition Field")
						&& ValidateInputs.validatePrices(conditionedTextField , "Report Condition Text Field")) {
					String condition = reportConditionsCombo.getValue();
					Double conditonValue = Double.parseDouble(conditionedTextField.getText());

					// All Categories
					String loc = "resources/jasperreports/cat_itemwise_all.jasper";
					parameters.put("fromDate", fromDat);
					parameters.put("toDate", toDat);
					parameters.put("printedBy", userName);
					parameters.put("quantity", conditonValue);
					parameters.put("wherefunctionequal", condition);
					viewReport(loc, parameters);

				}

			}

			// End Top Movement , Detail Item Wise , (Not Date Wise) (Not Selected Category)

			// Start Detail ,Category Wise , Date Wise
			else if (catWiseCheckBox.isSelected() && detailWiseRadio.isSelected() && dateWiseChkBox.isSelected()
					&& !movementQtyChkBox.isSelected()) {

				// All Categories
				if (catCheckCombo.getCheckModel().getCheckedItems().contains("All")) {
					String loc = "resources/jasperreports/cat_daywise_itemwise_all.jasper";
					parameters.put("fromDate", fromDat);
					parameters.put("toDate", toDat);
					parameters.put("printedBy", userName);
					parameters.put("quantity", 0.0);
					parameters.put("wherefunctionequal", "<>");
					viewReport(loc, parameters);

				}

				// Selected Categories
				else {
					String loc = "resources/jasperreports/cat_daywise_itemwise_all_selected_cat.jasper";
					parameters.put("fromDate", fromDat);
					parameters.put("toDate", toDat);
					parameters.put("printedBy", userName);
					parameters.put("quantity", 0.0);
					parameters.put("wherefunctionequal", "<>");
					parameters.put("categories", catCheckCombo.getCheckModel().getCheckedItems());
					viewReport(loc, parameters);

				}

			}

			// End Detail ,Category Wise , Date Wise

			// Start Detail ,Category Wise
			else if (catWiseCheckBox.isSelected() && detailWiseRadio.isSelected() && !movementQtyChkBox.isSelected()) {

				// All Categories
				if (catCheckCombo.getCheckModel().getCheckedItems().contains("All")) {
					String loc = "resources/jasperreports/cat_itemwise_all.jasper";
					parameters.put("fromDate", fromDat);
					parameters.put("toDate", toDat);
					parameters.put("printedBy", userName);
					parameters.put("quantity", 0.0);
					parameters.put("wherefunctionequal", "<>");
					viewReport(loc, parameters);

				}

				// Selected Categories
				else {
					String loc = "resources/jasperreports/catwise_item_wise_selected_cat.jasper";
					parameters.put("fromDate", fromDat);
					parameters.put("toDate", toDat);
					parameters.put("printedBy", userName);
					parameters.put("quantity", 0.0);
					parameters.put("wherefunctionequal", "<>");
					parameters.put("categories", catCheckCombo.getCheckModel().getCheckedItems());
					viewReport(loc, parameters);

				}

			}
			// End Detail ,Category Wise

			// Detail Item Wise, Date Wise
			else if (detailWiseRadio.isSelected() && dateWiseChkBox.isSelected() && !movementQtyChkBox.isSelected()) {
				String loc = "resources/jasperreports/cat_daywise_itemwise_all.jasper";
				parameters.put("fromDate", fromDat);
				parameters.put("toDate", toDat);
				parameters.put("printedBy", userName);
				parameters.put("quantity", 0.0);
				parameters.put("wherefunctionequal", "<>");
				viewReport(loc, parameters);
			}

			// End Detail Item Wise, Date Wise

			// Detail Item Wise Report Only
			else if (detailWiseRadio.isSelected() && !movementQtyChkBox.isSelected()) {

				String loc = "resources/jasperreports/cat_itemwise_all.jasper";
				parameters.put("fromDate", fromDat);
				parameters.put("toDate", toDat);
				parameters.put("printedBy", userName);
				parameters.put("quantity", 0.0);
				parameters.put("wherefunctionequal", "<>");
				viewReport(loc, parameters);

			}
			// End Detail Wise Report

			// End Detail ,Category Wise , Date Wise

		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Report Generating Error", e.getLocalizedMessage());
			e.printStackTrace();
		}

	}

	public void viewReport(String loc, Map<String, Object> parameters)
			throws SQLException, URISyntaxException, FileNotFoundException, JRException {
		Connection connectionForReports = HibernateUtil.getSessionFactory().getSessionFactoryOptions()
				.getServiceRegistry().getService(ConnectionProvider.class).getConnection();
		JasperPrint jp = JasperFillManager.fillReport(loc, parameters, connectionForReports);
		JasperViewer.viewReport(jp, false);

	}

}
