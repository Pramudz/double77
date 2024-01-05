package invsys.controllers.reports.stockreports;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import invsys.controllers.database.HibernateUtil;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.controllers.formvalidation.ValidateInputs;
import invsys.controllers.mainpage.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class StockReportController {

	@FXML
	private JFXDatePicker dateField;

	@FXML
	private JFXDatePicker toDateField;
	@FXML
	private JFXTextField itemIdField;

	String mainReportName;

	public void updateStockReportUiFromMain(String reportName) {
		ValidateInputs.getInstance();
		mainReportName = reportName;
		ValidateInputs.setDateFormatter(dateField);
		ValidateInputs.setDateFormatter(toDateField);
		

		if (reportName.contentEquals("Category Wise Stock Reports") || reportName.contentEquals("Out of Stock Reports")
				|| reportName.contentEquals("Item Wise Stock Report")) {

			dateField.setDisable(true);
			toDateField.setVisible(false);
			dateField.setPromptText("");
			dateField.setValue(LocalDate.now());
		}

		if (!reportName.contentEquals("Bincard Summary")) {
			itemIdField.setDisable(true);
			itemIdField.setVisible(false);
		}

	}

	@FXML
	void ClearFields() {
		dateField.getEditor().clear();
		toDateField.getEditor().clear();
		itemIdField.clear();
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

		if (mainReportName.contentEquals("GRN Summary Reports") || mainReportName.contentEquals("PO Summary Reports")) {

			if (ValidateInputs.validateDateField(dateField, "From Date Field")
					&& ValidateInputs.validateDateField(toDateField, "To Date Field")) {

				java.sql.Date fromDate = java.sql.Date.valueOf(dateField.getValue());
				java.sql.Date toDate = java.sql.Date.valueOf(toDateField.getValue());

				parameters.put("fromDate", fromDate);
				parameters.put("toDate", toDate);

				if (mainReportName.contentEquals("GRN Summary Reports")) {
					viewReport("resources/jasperreports/grn_summary.jasper", parameters);
				}

				if (mainReportName.contentEquals("PO Summary Reports")) {
					viewReport("resources/jasperreports/po_summary.jasper", parameters);
				}
			}

		}

		// Category Wise Report View
		if (mainReportName.contentEquals("Category Wise Stock Reports")) {
			parameters.put("toDate", java.sql.Date.valueOf(LocalDate.now()));
			viewReport("resources/jasperreports/category_wise_stock_summary.jasper", parameters);
		}

		// item wise stock report
		if (mainReportName.contentEquals("Item Wise Stock Report")) {
			parameters.put("toDate", java.sql.Date.valueOf(LocalDate.now()));
			viewReport("resources/jasperreports/itemWiseStockReport.jasper", parameters);
		}

		// Out of Stock report View
		if (mainReportName.contentEquals("Out of Stock Reports")) {
			parameters.put("toDate", java.sql.Date.valueOf(LocalDate.now()));
			viewReport("resources/jasperreports/outOfStockReport.jasper", parameters);
		}

		// Category Wise Report View
		if (mainReportName.contentEquals("Bincard Summary")) {
			if (ValidateInputs.validateDateField(dateField, "From Date Field")
					&& ValidateInputs.validateDateField(toDateField, "To Date Field") && ValidateInputs.validateNumbers(itemIdField, "Item Id Field")) {

				java.sql.Date fromDate = java.sql.Date.valueOf(dateField.getValue());
				java.sql.Date toDate = java.sql.Date.valueOf(toDateField.getValue());
				long prdId = Long.parseLong(itemIdField.getText());
				parameters.put("prdId", prdId);
				parameters.put("fromDate", fromDate);
				parameters.put("toDate", toDate);
			viewReport("resources/jasperreports/bincardSummary.jasper", parameters);
		}

	}
	}

	private void viewReport(String loc, Map<String, Object> parameters)
			throws SQLException, URISyntaxException, FileNotFoundException, JRException {
		Connection connectionForReports = HibernateUtil.getSessionFactory().getSessionFactoryOptions()
				.getServiceRegistry().getService(ConnectionProvider.class).getConnection();
		JasperPrint jp = JasperFillManager.fillReport(loc, parameters, connectionForReports);
		if (jp.getPages().isEmpty()) {
			AlertHandler.getAlert(AlertType.ERROR, "No Data Found", null);
		} else {
			JasperViewer.viewReport(jp, false);
		}

	}
}
