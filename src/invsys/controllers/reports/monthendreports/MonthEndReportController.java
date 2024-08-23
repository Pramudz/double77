package invsys.controllers.reports.monthendreports;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Month;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;

import org.controlsfx.control.CheckComboBox;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

import com.ibm.icu.util.Calendar;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;

import invsys.controllers.database.HibernateUtil;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.controllers.formvalidation.ValidateInputs;
import invsys.controllers.mainpage.MainController;
import invsys.entitiydao.OverheadDao;
import invsys.entitiydao.impl.OverheadDaoImpl;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class MonthEndReportController {

	@FXML
	private JFXDatePicker dateField;

	@FXML
	private CheckComboBox<String> overheadCatCombo;

	@FXML
	private JFXDatePicker toDateField;

	@FXML
	private JFXComboBox<Short> yearCombo;

	@FXML
	private CheckComboBox<String> monthCombo;

	@FXML
	private Label ocatLabel;

	@FXML
	private Label monthLabel;

	OverheadDao overheadDao = null;

	String mainReportName = null;

	@FXML
	void ClearFields() {
		dateField.getEditor().clear();
		toDateField.getEditor().clear();
		yearCombo.getSelectionModel().clearSelection();
		
		monthCombo.getCheckModel().clearChecks();
		overheadCatCombo.getCheckModel().clearChecks();

	}

	// update MonthEnd Report Form from Main report as per the report type
	public void updateMonthEndreportFromMainReport(String reportName) {
		ValidateInputs.getInstance();
		if (reportName.contentEquals("Expenses Report")) {
			dateField.setVisible(false);
			toDateField.setVisible(false);
			overheadCatCombo.setVisible(true);
			yearCombo.setVisible(true);
			monthCombo.setVisible(true);
			monthLabel.setVisible(true);
			ocatLabel.setVisible(true);
			overheadDao = OverheadDaoImpl.getDao();
			overheadCatCombo.getItems().add("All");
			overheadCatCombo.getItems().addAll(overheadDao.getOverheadCatNames());
			loadMonthAndYearsToCombo();

			// set check model for Overhead category if All is selected others will be
			// unchecked
			overheadCatCombo.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
				@Override
				public void onChanged(Change<? extends String> c) {

					while (c.next()) {

						if (c.wasAdded()) {
							if (c.toString().contains("All")) {
								for (int i = 1; i < overheadCatCombo.getItems().size() - 1; i++) {
									if (overheadCatCombo.getCheckModel().isChecked(i)) {
										overheadCatCombo.getCheckModel().clearCheck(i);
									}
								}
							} else {
								if (overheadCatCombo.getCheckModel().isChecked(0)) {
									overheadCatCombo.getCheckModel().clearCheck("All");
								}
							}
						}
					}
				}
			});

		}

		if (reportName.contentEquals("Profit & Loss Report")) {
			dateField.setVisible(true);
			toDateField.setVisible(true);
			overheadCatCombo.setVisible(false);
			yearCombo.setVisible(false);
			monthCombo.setVisible(false);
			monthLabel.setVisible(false);
			ocatLabel.setVisible(false);
			
			ValidateInputs.setDateFormatter(dateField);
			ValidateInputs.setDateFormatter(toDateField);
		}

		mainReportName = reportName;
	}

	// update month and year combo box when initializing the form specially for
	// Overhead Report
	private void loadMonthAndYearsToCombo() {

		ObservableList<String> months = FXCollections.observableArrayList();
		for (Month x : Month.values()) {
			months.add(x.toString());
		}

		monthCombo.getItems().add("All");
		monthCombo.getItems().addAll(months);

		// set check model for Month Combo if All is selected others will be unchecked
		monthCombo.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> c) {

				while (c.next()) {

					if (c.wasAdded()) {
						if (c.toString().contains("All")) {
							for (int i = 1; i < monthCombo.getItems().size(); i++) {
								if (monthCombo.getCheckModel().isChecked(i)) {
									monthCombo.getCheckModel().clearCheck(i);
								}
							}
						} else {
							if (monthCombo.getCheckModel().isChecked(0)) {
								monthCombo.getCheckModel().clearCheck("All");
							}
						}
					}
				}
			}
		});

		short nowYear = Short.parseShort(String.valueOf(Year.now()));
		ObservableList<Short> listOfyears = FXCollections.observableArrayList();
		listOfyears.add(nowYear);
		for (short i = 0; i < 5; i++) {
			nowYear--;
			listOfyears.add(nowYear);
		}

		yearCombo.setItems(listOfyears);

	}

	// method to view report when click the view button
	@FXML
	void viewReportClicked() throws FileNotFoundException, SQLException, URISyntaxException, JRException {

		

		Map<String, Object> parameters = new HashMap();
		parameters.put("companyName", MainController.getCompanyInfoSession().getCompanyName());
		parameters.put("streetAddress", MainController.getCompanyInfoSession().getAddressLine1());
		parameters.put("addressLine", MainController.getCompanyInfoSession().getAddressLine2());
		parameters.put("city", MainController.getCompanyInfoSession().getAddressLine3());
		parameters.put("telephoneNum", MainController.getCompanyInfoSession().getTelephoneNum());
		parameters.put("printedBy", MainController.getUserSession().getUserName());

		String reportLoc = "";

		ObservableList<String> listOfOverheadCategories = FXCollections.observableArrayList();
		ObservableList<Integer> listOfMonths = FXCollections.observableArrayList();

		
		// for Expenses Report
		if (mainReportName.contentEquals("Expenses Report")) {

			if (ValidateInputs.validateInputEmptyComboWithoutEditorYear(yearCombo, "Year Field")
					&& ValidateInputs.validateInputEmptyGeneralBolean(monthCombo, "Month Field")
					&& ValidateInputs.validateInputEmptyGeneralBolean(overheadCatCombo, "Overhead Category Field")) {

				parameters.put("year", yearCombo.getValue());

				if (monthCombo.getCheckModel().getCheckedItems().contains("All")
						&& overheadCatCombo.getCheckModel().getCheckedItems().contains("All")) {

					listOfOverheadCategories.setAll(overheadCatCombo.getItems());

					// since month is enamurated in Hibernate as all months are selected this is the
					// easy ways to get 12 days
					
					for (int i = 1; i <= 12; i++) {
						listOfMonths.add(i);
					}

				}
				// check not contains all in overhead category - selected but all months
				if (monthCombo.getCheckModel().getCheckedItems().contains("All")
						&& !overheadCatCombo.getCheckModel().getCheckedItems().contains("All")) {
					
					for (int i = 1; i <= 12; i++) {
						listOfMonths.add(i);
					}

					listOfOverheadCategories.setAll(overheadCatCombo.getCheckModel().getCheckedItems());

				}

				// check not contains all in Months but all overhead categories
				if (!monthCombo.getCheckModel().getCheckedItems().contains("All")
						&& overheadCatCombo.getCheckModel().getCheckedItems().contains("All")) {

					for (String x : monthCombo.getCheckModel().getCheckedItems()) {

						// get month Instance from the String
						Month month = Month.valueOf(x);

						// get integer value of the month from the Month instance
						int imonth = month.getValue();

						listOfMonths.add(imonth);

					}

					listOfOverheadCategories.setAll(overheadCatCombo.getItems());
				}

				// both months and and overhead categories not selected all
				if (!monthCombo.getCheckModel().getCheckedItems().contains("All")
						&& !overheadCatCombo.getCheckModel().getCheckedItems().contains("All")) {

					for (String x : monthCombo.getCheckModel().getCheckedItems()) {

						// get month Instance from the String
						Month month = Month.valueOf(x);

						// get integer value of the month from the Month instance
						int imonth = month.getValue();

						listOfMonths.add(imonth);

					}

					listOfOverheadCategories.setAll(overheadCatCombo.getCheckModel().getCheckedItems());
				}

				parameters.put("overheadcategorylist", listOfOverheadCategories);
				parameters.put("monthList", listOfMonths);

				reportLoc = "resources/jasperreports/expensesreport.jasper";

				viewReport(reportLoc, parameters);
			}

		}
		
		if (mainReportName.contentEquals("Profit & Loss Report")) {
			
			if(ValidateInputs.validateDateField(dateField, "From Date Field") && 
					ValidateInputs.validateDateField(toDateField, "To Date Field")) {
				java.sql.Date fromdate = java.sql.Date.valueOf(dateField.getValue());
				java.sql.Date toDate = java.sql.Date.valueOf(toDateField.getValue());
				
				
				Calendar cal = Calendar.getInstance();
				cal.setTime(fromdate);
				
				int fromMonth = cal.get(Calendar.MONTH)+1;
				int fromYear = cal.get(Calendar.YEAR);
				
				cal.setTime(toDate);
				
				int toMonth = cal.get(Calendar.MONTH)+1;
				int toYear = cal.get(Calendar.YEAR);
				
				
				System.out.println(fromMonth);
				System.out.println(toMonth);
				System.out.println(fromYear);
				System.out.println(toYear);
				parameters.put("fromDate", fromdate);
				parameters.put("toDate", toDate);
				parameters.put("fromMonth", fromMonth);
				parameters.put("toMonth", toMonth);
				parameters.put("fromYear", fromYear);
				parameters.put("toYear", toYear);
				
				reportLoc = "resources/jasperreports/profit_and_loss.jasper";

				viewReport(reportLoc, parameters);
				
			}
			
		}
		
		//for Profit and Loss Report
		

	}

	private void viewReport(String loc, Map<String, Object> parameters)
			throws SQLException, URISyntaxException, FileNotFoundException, JRException {
		Connection connectionForReports = HibernateUtil.getSessionFactory().getSessionFactoryOptions()
				.getServiceRegistry().getService(ConnectionProvider.class).getConnection();
		JasperPrint jp = JasperFillManager.fillReport(loc, parameters, connectionForReports);
		if(jp.getPages().isEmpty()) {
			AlertHandler.getAlert(AlertType.ERROR, "No Data Found", null);
		}
		else {
			JasperViewer.viewReport(jp, false);
		}

	}
}
