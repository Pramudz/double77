package invsys.controllers.reports;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashSet;

import com.jfoenix.controls.JFXTreeView;

import invsys.controllers.reports.dailyreports.DailyReportController;
import invsys.controllers.reports.monthendreports.MonthEndReportController;
import invsys.controllers.reports.stockreports.StockReportController;
import invsys.controllers.reports.supplierrelated.SupplierRelatedReportController;
import invsys.entities.RoleFunctions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;

public class ReportController {

	@FXML
	JFXTreeView<String> treeView;

	// main Trees items
	TreeItem<String> root = new TreeItem<String>("Reports");

	ObservableList<TreeItem<String>> listOfTrees = FXCollections.observableArrayList();

	// sup reports stage
	// for other stages used in this main window in order to set null stage when
	// hidden
	private FXMLLoader otherSetHiddenLoader;
	private Stage otherStagesHidden;
	private Scene otherScenHidden;

	private TreeItem<String> findReportExist(TreeItem<String> treeNode, String nodeValuetoFind) {

		if (treeNode == null) {
			return null;
		}

		if (treeNode.getValue().contentEquals(nodeValuetoFind)) {
			return treeNode;
		} else {
			for (TreeItem<String> str : treeNode.getChildren()) {
				treeNode = findReportExist(str, nodeValuetoFind);
			}

			return null;
		}
	}

	private TreeItem<String> findNodeExistList(ObservableList<TreeItem<String>> list, String tobeFinedItem) {
		TreeItem<String> item = null;
		int i = 0;
		for (i = 0; i < list.size(); i++) {
			item = findReportExist(listOfTrees.get(i), tobeFinedItem);
			if (item != null) {
				break;
			}
		}
		return item;
	}

	public void setReportTreeViewWhenLoading(HashSet<RoleFunctions> reportSet) {

		for (RoleFunctions report : reportSet) {

			String reportName = report.getRoleFunction();

			TreeItem<String> leafNode = new TreeItem<String>(reportName);
			TreeItem<String> parentNode = getParentNodes(report, leafNode, leafNode);

			parentNode.setExpanded(true);
			SortedList<TreeItem<String>> hList = parentNode.getChildren().sorted();
			parentNode.getChildren().setAll(hList);
			listOfTrees.remove(parentNode);
			listOfTrees.add(parentNode);

		}
		SortedList<TreeItem<String>> sList = listOfTrees.sorted();
		root.getChildren().setAll(sList);
		root.setExpanded(true);
		treeView.setRoot(root);

	}

	private TreeItem<String> getParentNodes(RoleFunctions parent, TreeItem<String> intermediateParent,
			TreeItem<String> leafNode) {

		if (parent == null) {
			return leafNode;
		}

		TreeItem<String> upperNode = null;
		TreeItem<String> checkExistNode = findNodeExistList(listOfTrees,
				parent.getMainRoleFunction().getRoleFunction());

		if (checkExistNode != null) {
			intermediateParent = checkExistNode;
			if (leafNode != null) {
				intermediateParent.getChildren().add(leafNode);
			}
			return intermediateParent;
		}
		// travese untill Report node found to stop
		if (parent.getMainRoleFunction().getRoleFunction().contentEquals("Reports")) {
			intermediateParent = new TreeItem<String>(parent.getRoleFunction());
			if (leafNode != null)
				intermediateParent.getChildren().add(leafNode);

		}
		// travese untill Report node found to stop
		if (!parent.getMainRoleFunction().getRoleFunction().contentEquals("Reports")) {
			upperNode = getParentNodes(parent.getMainRoleFunction(), intermediateParent, null);
			upperNode.getChildren().remove(intermediateParent);
			upperNode.getChildren().add(intermediateParent);
			intermediateParent = upperNode;
			return intermediateParent;
		}

		return intermediateParent;
	}

	@FXML
	public void treeItemAction(MouseEvent event) throws IOException, SQLException, URISyntaxException, JRException {
		TreeItem<String> treeItem = treeView.getSelectionModel().getSelectedItem();

		//Category Wise Sales Report
		if (treeItem.getValue().contentEquals("Category Wise")) {
			loadCategoryWiseSalesReportForm("/fxml/subreports/categoryWiseSales.fxml");
		}

		//daily Sales Reports
		if (treeItem.getParent().getValue().contentEquals("Daily Reports")) {
			loadDailyReportForm("/fxml/subreports/dailyreports/dailyReportsGui.fxml", treeItem.getValue());
		}

		//Month End Reports
		if (treeItem.getValue().contentEquals("Expenses Report")
				|| treeItem.getValue().contains("Profit & Loss Report")) {
			loadMonthEndReportForm("/fxml/subreports/monthendreports/monthendreport.fxml", treeItem.getValue());
		}
		
		
		// Supplier Related Reports
		if (treeItem.getParent().getValue().contentEquals("Supplier Related")) {
			loadSupRelatedForm("/fxml/subreports/supplierrelated/supplierRelatedRep.fxml", treeItem.getValue());
		}
		
	
		// Stock Reports
		if(treeItem.getParent().getValue().contains("Stock Reports")) {
			loadStockReportRelatedFrom("/fxml/subreports/stockreports/stockReportUI.fxml", treeItem.getValue());
	}
	
		
		
	}

	void loadCategoryWiseSalesReportForm(String loc) throws IOException {

		otherSetHiddenLoader = new FXMLLoader(getClass().getResource(loc));
		otherScenHidden = new Scene(otherSetHiddenLoader.load());
		otherStagesHidden = new Stage();
		otherStagesHidden.setScene(otherScenHidden);
		otherStagesHidden.initModality(Modality.APPLICATION_MODAL);
		otherStagesHidden.setTitle("Category Wise Sales");
		otherStagesHidden.show();

		otherStagesHidden.setOnHidden(e -> {
			otherStagesHidden = null;
			otherScenHidden = null;
			otherSetHiddenLoader = null;

		});

	}

	// GRN and PO Inquiry report form Loading
	void loadDailyReportForm(String loc, String reportName) throws IOException {

		otherSetHiddenLoader = new FXMLLoader(getClass().getResource(loc));
		otherScenHidden = new Scene(otherSetHiddenLoader.load());
		DailyReportController controller = otherSetHiddenLoader.getController();
		controller.updateGrnPoFieldFromReport(reportName);
		otherStagesHidden = new Stage();
		otherStagesHidden.setScene(otherScenHidden);
		otherStagesHidden.initModality(Modality.APPLICATION_MODAL);
		otherStagesHidden.setTitle(reportName);
		otherStagesHidden.show();

		otherStagesHidden.setOnHidden(e -> {
			otherStagesHidden = null;
			otherScenHidden = null;
			otherSetHiddenLoader = null;

		});

	}

	// GRN and PO Inquiry report form Loading
	void loadMonthEndReportForm(String loc, String reportName) throws IOException {

		otherSetHiddenLoader = new FXMLLoader(getClass().getResource(loc));
		otherScenHidden = new Scene(otherSetHiddenLoader.load());
		MonthEndReportController controller = otherSetHiddenLoader.getController();
		controller.updateMonthEndreportFromMainReport(reportName);
		otherStagesHidden = new Stage();
		otherStagesHidden.setScene(otherScenHidden);
		otherStagesHidden.initModality(Modality.APPLICATION_MODAL);
		otherStagesHidden.setTitle(reportName);
		otherStagesHidden.show();

		otherStagesHidden.setOnHidden(e -> {
			otherStagesHidden = null;
			otherScenHidden = null;
			otherSetHiddenLoader = null;

		});

	}

	// GRN and PO Inquiry report form Loading
	void loadSupRelatedForm(String loc, String reportName) throws IOException {

		otherSetHiddenLoader = new FXMLLoader(getClass().getResource(loc));
		otherScenHidden = new Scene(otherSetHiddenLoader.load());
		SupplierRelatedReportController controller = otherSetHiddenLoader.getController();
		controller.updateReportFromMainRepControl(reportName);
		otherStagesHidden = new Stage();
		otherStagesHidden.setScene(otherScenHidden);
		otherStagesHidden.initModality(Modality.APPLICATION_MODAL);
		otherStagesHidden.setTitle(reportName);
		otherStagesHidden.show();

		otherStagesHidden.setOnHidden(e -> {
			otherStagesHidden = null;
			otherScenHidden = null;
			otherSetHiddenLoader = null;

		});

	}
	
	
	//Stock Report Related Form
		void loadStockReportRelatedFrom(String loc, String reportName) throws IOException {

			otherSetHiddenLoader = new FXMLLoader(getClass().getResource(loc));
			otherScenHidden = new Scene(otherSetHiddenLoader.load());
			StockReportController controller = otherSetHiddenLoader.getController();
			controller.updateStockReportUiFromMain(reportName);
			otherStagesHidden = new Stage();
			otherStagesHidden.setScene(otherScenHidden);
			otherStagesHidden.initModality(Modality.APPLICATION_MODAL);
			otherStagesHidden.setTitle(reportName);
			otherStagesHidden.show();

			otherStagesHidden.setOnHidden(e -> {
				otherStagesHidden = null;
				otherScenHidden = null;
				otherSetHiddenLoader = null;

			});

		}
	

}
