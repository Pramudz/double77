package invsys.controllers.reports.supplierrelated;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.controlsfx.control.CheckComboBox;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import invsys.controllers.database.HibernateUtil;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.controllers.formvalidation.ValidateInputs;
import invsys.controllers.mainpage.MainController;
import invsys.entitiydao.SupplierDao;
import invsys.entitiydao.impl.SupplierDaoImpl;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class SupplierRelatedReportController {

	@FXML
	private JFXDatePicker dateField;

	@FXML
	private JFXDatePicker toDateField;

	@FXML
	private Label supLabel;

	@FXML
	private CheckComboBox<String> supCheckCombo;

	@FXML
	private JFXTextField supRetOrRonNoField;

	private String mainReportName;

	public void updateReportFromMainRepControl(String reportName) {

		ValidateInputs.getInstance();

		ValidateInputs.setDateFormatter(dateField);
		ValidateInputs.setDateFormatter(toDateField);

		if (reportName.contains("Supplier Payment History") || reportName.contains("Supplier Return History")
				|| reportName.contains("Supplier Wise Sales")) {

			supRetOrRonNoField.setVisible(false);
			SupplierDao dao = SupplierDaoImpl.getDao();
			supCheckCombo.getItems().add("All");
			supCheckCombo.getItems().addAll(dao.getSupName());
			
			// set check model for Supplier if All is selected others will be
			// unchecked
			supCheckCombo.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
				@Override
				public void onChanged(Change<? extends String> c) {

					while (c.next()) {

						if (c.wasAdded()) {
							if (c.toString().contains("All")) {
								for (int i = 1; i < supCheckCombo.getItems().size() - 1; i++) {
									if (supCheckCombo.getCheckModel().isChecked(i)) {
										supCheckCombo.getCheckModel().clearCheck(i);
									}
								}
							} else {
								if (supCheckCombo.getCheckModel().isChecked(0)) {
									supCheckCombo.getCheckModel().clearCheck("All");
								}
							}
						}
					}
				}
			});

		}
		if (reportName.contains("Supplier Payment Inquiry")) {
			supRetOrRonNoField.setPromptText("Payment No");
			dateField.setVisible(false);
			toDateField.setVisible(false);
			supLabel.setVisible(false);
			supCheckCombo.setVisible(false);
		}

		if (reportName.contains("Supplier Return Inquiry")) {
			supRetOrRonNoField.setPromptText("RON No");
			dateField.setVisible(false);
			toDateField.setVisible(false);
			supLabel.setVisible(false);
			supCheckCombo.setVisible(false);
		}

		this.mainReportName = reportName;
	}

	@FXML
	void ClearFields() {
		dateField.getEditor().clear();
		toDateField.getEditor().clear();
		supCheckCombo.getCheckModel().clearChecks();
		supRetOrRonNoField.clear();

	}

	@FXML
	void viewReportClicked() throws FileNotFoundException, SQLException, URISyntaxException, JRException {

		Map<String, Object> parameters = new HashMap();
		parameters.put("companyName", MainController.getCompanyInfoSession().getCompanyName());
		parameters.put("streetAddress", MainController.getCompanyInfoSession().getAddressLine1());
		parameters.put("addressLine", MainController.getCompanyInfoSession().getAddressLine2());
		parameters.put("city", MainController.getCompanyInfoSession().getAddressLine3());
		parameters.put("telephoneNum", MainController.getCompanyInfoSession().getTelephoneNum());
		parameters.put("printedBy", MainController.getUserSession().getUserName());
		parameters.put("documentOriginalStatus", "Duplicate");

		if (mainReportName.contains("Supplier Payment History") || mainReportName.contains("Supplier Return History")
				|| mainReportName.contains("Supplier Wise Sales")) {

			if (ValidateInputs.validateDateField(dateField, "From Date Field")
					&& ValidateInputs.validateDateField(toDateField, "To Date Field")
					&& ValidateInputs.validateInputEmptyGeneralBolean(supCheckCombo, "Supplier Field")) {

				java.sql.Date fromDate = java.sql.Date.valueOf(dateField.getValue());
				java.sql.Date toDate = java.sql.Date.valueOf(toDateField.getValue());
				
				parameters.put("fromDate", fromDate);
				parameters.put("toDate", toDate);

				if (mainReportName.contentEquals("Supplier Payment History")) {

					if(supCheckCombo.getCheckModel().getCheckedItems().contains("All")) {
						viewReport("resources/jasperreports/supplierPaymentSummaryAll.jasper", parameters);
					}
					else {
						parameters.put("supplierNameList", supCheckCombo.getCheckModel().getCheckedItems());
						viewReport("resources/jasperreports/supplierPaymentSummarySelected.jasper", parameters);
		
					}
					
					
				}

				if (mainReportName.contentEquals("Supplier Return History")) {
					
					if(supCheckCombo.getCheckModel().getCheckedItems().contains("All")) {
						viewReport("resources/jasperreports/supplierReturnSumAll.jasper", parameters);
					}
					else {
						parameters.put("supplierNameList", supCheckCombo.getCheckModel().getCheckedItems());
						viewReport("resources/jasperreports/supplierReturnSumSelected.jasper", parameters);
		
					}
					
				}

				if (mainReportName.contentEquals("Supplier Wise Sales")) {
					System.out.println("Supplier Sales");
				}

			}
		}

		if (mainReportName.contains("Supplier Payment Inquiry")) {

			if (ValidateInputs.validateNumbers(supRetOrRonNoField, supRetOrRonNoField.getPromptText() + " Field")) {

				parameters.put("supPaymentId", supRetOrRonNoField.getText());
				viewReport("resources/jasperreports/supplierPaymentInquiry.jasper", parameters);
			}

		}

		if (mainReportName.contains("Supplier Return Inquiry")) {

			if (ValidateInputs.validateNumbers(supRetOrRonNoField, supRetOrRonNoField.getPromptText() + " Field")) {

				parameters.put("supReturnId", supRetOrRonNoField.getText());

				viewReport("resources/jasperreports/supplierReturnInquiry.jasper", parameters);
			}

		}

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
