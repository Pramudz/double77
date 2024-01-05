package invsys.controllers.dashboard;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

import com.ibm.icu.text.DateFormatSymbols;

import invsys.controllers.database.HibernateUtil;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.controllers.formvalidation.ValidateInputs;
import invsys.controllers.mainpage.MainController;
import invsys.entities.Users;
import invsys.entitiydao.ProductDao;
import invsys.entitiydao.SalesDao;
import invsys.entitiydao.impl.ProductDaoImpl;
import invsys.entitiydao.impl.SalesDaoImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class DashBoardController implements Initializable{
	
	
    @FXML
    private LineChart<String, Number> salesChart;

    @FXML
    private CategoryAxis ixAxis;

    @FXML
    private NumberAxis syAxis;

    @FXML
    private Label totalDaySaleLabel;

    @FXML
    private Label totDaySaleTitleLabel;
    
    @FXML
    private Label totalInvoicesLabel;
    
    
    @FXML
    private Label totDayInvoiceTitleLabel;

    
    @FXML
    private TableView<TopTenItem> topTenItemTableView;

    @FXML
    private TableColumn<TopTenItem , String> itemNameColumn;

    @FXML
    private TableColumn<TopTenItem , Double> salesQtyColumn;

    @FXML
    private TableColumn<TopTenItem , Double> salesValColumn;
    
    @FXML
    private Label prdCountLabel;

    @FXML
    private Label outOfStockLabel;
    
    
    //wrapper class for top 10 item table view
    
    public class TopTenItem {
    	String itemDesc;
    	double salesQty;
    	double salesValue;
		public String getItemDesc() {
			return itemDesc;
		}
		public void setItemDesc(String itemDesc) {
			this.itemDesc = itemDesc;
		}
		public double getSalesQty() {
			return salesQty;
		}
		public void setSalesQty(double salesQty) {
			this.salesQty = salesQty;
		}
		public double getSalesValue() {
			return salesValue;
		}
		public void setSalesValue(double salesValue) {
			this.salesValue = salesValue;
		}
    	
     }
    
    // end wrapper class for Top 10 item table view
    
    
    //dao handler/classes
    
    ProductDao productDao = null;
    SalesDao salesDao = null;
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	salesDao = SalesDaoImpl.getDao();
    	productDao = ProductDaoImpl.getDao();
    	loadTotalDaySales();
    	initTopTenItemTableViewAndItsData();
    	loadProductCounts();
    	loadDailyPosSalesCount();
    	
    	loadSalesChartUpdated();
		
	}

    
    
    
    
    private void initTopTenItemTableViewAndItsData() {
    	itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemDesc"));
    	salesQtyColumn.setCellValueFactory(new PropertyValueFactory<>("salesQty"));
    	salesValColumn.setCellValueFactory(new PropertyValueFactory<>("salesValue"));
    	topTenItemTableView.getItems().clear();
    
    java.sql.Date fromDate = java.sql.Date.valueOf(LocalDate.now().withDayOfMonth(1));
    java.sql.Date toDate = java.sql.Date.valueOf(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()));
      
    for(Object[] objArray : salesDao.getTopTenItemsOfTheMonth(fromDate, toDate)) {
    	
    	TopTenItem topTenItem = new TopTenItem();
    	topTenItem.setItemDesc((String) objArray[0]);
    	topTenItem.setSalesValue((Double) objArray[1]);
    	topTenItem.setSalesQty((Double) objArray[2]);
    	topTenItemTableView.getItems().add(topTenItem);
    	
    }
    
    	
    }
    
    //load total day sales value 
    
    private void loadTotalDaySales() {
    	double sale = 0;
    	if(MainController.isDashbardManager()) {
    		sale = salesDao.getDaySalesValueNativeQueryForManagers(java.sql.Date.valueOf(LocalDate.now()));
    		totalDaySaleLabel.setText(String.valueOf(sale));
    	}
    	else {
    		sale = salesDao.getDaySalesValueNativeQueryForNonManagers(java.sql.Date.valueOf(LocalDate.now()), MainController.getUserSession());
        	totDaySaleTitleLabel.setText("Total Your Day Sales:");
    		totalDaySaleLabel.setText(String.valueOf(sale));
    	}
    	
    	
    }
    
    //load all available product count and product count of the no stock available
    private void loadProductCounts() {
    	
    	prdCountLabel.setText(String.valueOf(productDao.getProductCount()+ " Products"));
    	outOfStockLabel.setText(String.valueOf(productDao.getNoStockAvailableCount()+ " Products"));  
    }
    
    
    //load all available product count and product count of the no stock available
    private void loadDailyPosSalesCount() {
    	
    	java.sql.Date todayDate = java.sql.Date.valueOf(LocalDate.now());
    	Users user= MainController.getUserSession();
    	boolean isManager = MainController.isDashbardManager();
    	
    	if(!isManager) {
    		totDayInvoiceTitleLabel.setText("Total Your Day Sales Count :");
    	}
    	
    	totalInvoicesLabel.setText(String.valueOf(salesDao.getDailyPosSalesCount(todayDate, user, isManager))+" Bills");
    }
    

    
 // Old Method Simple SQL : get Sales Chart Data to Line Chart
	/*
	 * private void loadSalesChart() throws SQLException { String[] months =
	 * DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths(); ObservableList
	 * listOfMonth = FXCollections.observableArrayList(months); XYChart.Series
	 * series = new XYChart.Series(); String query =
	 * "SELECT s.sales_date,SUM(e.qty * e.unit_price) as salesvalue from sale s inner join salesdetails e on s.sale_id = e.sales_id group by month(s.sales_date)"
	 * ; ResultSet getData = DBHandler.executeQuery(query); series.getData().add(new
	 * XYChart.Data("April", 10)); while (getData.next()) { String month =
	 * convertDate(getData.getDate("sales_date").toString()); double saleVal =
	 * getData.getDouble("salesvalue");
	 * 
	 * series.getData().add(new XYChart.Data(month, saleVal)); }
	 * 
	 * series.setName("Sales"); ixAxis.setCategories(listOfMonth);
	 * salesChart.getData().add(series); months = null; listOfMonth = null; getData
	 * = null; salesChart = null; ixAxis = null;
	 * 
	 * }
	 */

 	// New Method for Load Chart details
 	@SuppressWarnings("unchecked")
	private void loadSalesChartUpdated(){
 		String[] months = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths();
 		ObservableList listOfMonth = FXCollections.observableArrayList(months);
 		XYChart.Series<String,Number> series = new XYChart.Series();

 		
 		java.sql.Date fromDate = java.sql.Date.valueOf(LocalDate.of(LocalDate.now().getYear(), Month.JANUARY, 1));
 		java.sql.Date toDate = java.sql.Date.valueOf(LocalDate.now());
 		
 		List<Object[]> salesAndMonth = null;
 		if(MainController.isDashbardManager()) {
 			salesAndMonth = salesDao.getMonthlyTotalSalesNativeQueryForManagers(fromDate, toDate);
 		}
 		else {
 			salesAndMonth = salesDao.getMonthlyTotalSalesNativeQueryForUsers(fromDate, toDate , MainController.getUserSession().getUser_id());
 		}
 		
 		for (Object[] obj : salesAndMonth) {
 			String month = convertDate(obj[1].toString());
 			double saleVal = (double) obj[0];
 			
 			XYChart.Data<String, Number> data = new XYChart.Data(month, saleVal);
 			series.getData().add(data);
 						
 		}	

 		series.setName("Sales");
 		ixAxis.setCategories(listOfMonth);
 		salesChart.getData().add(series);
 		
 		for(final XYChart.Data<String,Number> data : series.getData()) {
 			data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, e->{
 				
 				String salesValue = ValidateInputs.getDecimalFormattedNumber(data.getYValue());
 				Tooltip.install(data.getNode(), new Tooltip("Month :"+data.getXValue()+"\nSales : " +salesValue));
 			});
 		}

 	}

 	// convert Database date to month
 	private String convertDate(String date) {

 		int d = Integer.parseInt(date.substring(5, 7));
 		return new DateFormatSymbols().getMonths()[d - 1];
 	}
 	
 	
    @FXML
    void lowStockAction() {

    }

    @FXML
    void outOfStockAction() throws JRException, SQLException {
    	
    	String reportPath = "resources/jasperreports/outOfStockReportDashBoard.jasper";
    	HashMap<String, Object> parameters = new HashMap();
    	java.sql.Date todayDate = java.sql.Date.valueOf(LocalDate.now());
    	parameters.put("date", todayDate);
    	viewReport(reportPath,parameters);

    }

    @FXML
    void topItemAction() throws JRException, SQLException {
    	
    	String reportPath = "resources/jasperreports/TopMovementCatWiseItemWiseforDashBoard.jasper";
    	HashMap<String, Object> parameters = new HashMap();
    	java.sql.Date fromDate = java.sql.Date.valueOf(LocalDate.now().withDayOfMonth(1));
    	java.sql.Date toDate = java.sql.Date.valueOf(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()));
    	parameters.put("fromDate", fromDate);
    	parameters.put("toDate", toDate);
    	parameters.put("quantity", 0.0);
		parameters.put("wherefunctionequal", "<>");
    	viewReport(reportPath,parameters);
    	
    }

    
    //total Day Bill Wise Sales
    @FXML
    void totalDayInvoicesAction() throws JRException, SQLException {
    	
    	HashMap<String, Object> parameters = new HashMap();
    	java.sql.Date todayDate = java.sql.Date.valueOf(LocalDate.now());
 		parameters.put("fromDate", todayDate);
 		parameters.put("toDate", todayDate);
 		String reportPath = "";
 		
    	if(MainController.isDashbardManager()) {
    	 	reportPath = "resources/jasperreports/daySaleBillWise.jasper";
    		viewReport(reportPath,parameters);
    		
    	}
    	else {
    		reportPath = "resources/jasperreports/daySaleBillWiseForUsers.jasper";
    		parameters.put("userId", MainController.getUserSession().getUser_id());
    		viewReport(reportPath,parameters);
    	}

    }

    
    //Total Day Category Wise Sales
    @FXML
    void totalDaysalesAction() throws JRException, SQLException {
    	HashMap<String, Object> parameters = new HashMap();
    	java.sql.Date todayDate = java.sql.Date.valueOf(LocalDate.now());
 		parameters.put("fromDate", todayDate);
 		parameters.put("toDate", todayDate);
 		parameters.put("quantity", 0.0);
		parameters.put("wherefunctionequal", "<>");
 		String reportPath = "";
 		
    	if(MainController.isDashbardManager()) {
    	 	reportPath = "resources/jasperreports/cat_itemwise_allforDashBoard.jasper";
    		viewReport(reportPath,parameters);
    		
    	}
    	else {
    		reportPath = "resources/jasperreports/cat_itemwise_all_userWise_forDashBoard.jasper";
    		parameters.put("userId", MainController.getUserSession().getUser_id());
    		viewReport(reportPath,parameters);
    	}

    }

    @FXML
    void totalProdcutsAction() throws JRException, SQLException {
    	String reportPath = "resources/jasperreports/CatWiseproductCountForDashBoard.jasper";
    	HashMap<String, Object> parameters = new HashMap();
    	viewReport(reportPath,parameters);

    }

    public void viewReport(String location , HashMap<String,Object> specificParameters) throws JRException, SQLException {
    	
    	specificParameters.put("companyName", MainController.getCompanyInfoSession().getCompanyName());
    	specificParameters.put("streetAddress", MainController.getCompanyInfoSession().getAddressLine1());
    	specificParameters.put("addressLine", MainController.getCompanyInfoSession().getAddressLine2());
    	specificParameters.put("city", MainController.getCompanyInfoSession().getAddressLine3());
    	specificParameters.put("telephoneNum", MainController.getCompanyInfoSession().getTelephoneNum());
    	specificParameters.put("userName",MainController.getUserSession().getUserName());
    	
    	Connection connectionForReports = HibernateUtil.getSessionFactory().getSessionFactoryOptions()
				.getServiceRegistry().getService(ConnectionProvider.class).getConnection();
    	JasperPrint jp = JasperFillManager.fillReport(location, specificParameters, connectionForReports);
    	
    	if(jp.getPages().isEmpty()) {
    		AlertHandler.getAlert(AlertType.ERROR, "No Data Found", null);
    	}
    	else {
    		JasperViewer.viewReport(jp, false);
    	}
		
    }
	
}
