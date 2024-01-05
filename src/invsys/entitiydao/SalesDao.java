package invsys.entitiydao;

import java.util.List;

import invsys.entities.POSPayDetails;
import invsys.entities.Sales;
import invsys.entities.SalesDetails;
import invsys.entities.Users;
import javafx.collections.ObservableList;

public interface SalesDao {

	
	 boolean saveSale(Sales sale, ObservableList<SalesDetails> salesDetails,
				ObservableList<POSPayDetails> payDetailList);
	 
	 boolean cancelFullBill(Sales sale, ObservableList<SalesDetails> salesDetails);
	 
	 int getLastBillNo(Users user, java.sql.Date date);
	 
	 List<Object[]> getSalesValueByMonth();
	 
	// get valid sales from sales by bill no, date, cashier id
	List<SalesDetails> getSaleDetailsByBillNoDateUser(long billNo, java.sql.Date date, Users user);
	 
	// get daily sales count based on dashboard manager
	long getDailyPosSalesCount(java.sql.Date date, Users user , boolean isManager);
	
	// get total Day Sale value for dash board manager roles(Overall Figures) to
		// dash board - uses native query due to complexity
	double getDaySalesValueNativeQueryForManagers(java.sql.Date date) ;
	
	// get monthly sales values for xy chart manager roles(Overall Figures) to
			// dash board - uses native query due to complexity
	List<Object[]> getMonthlyTotalSalesNativeQueryForManagers(java.sql.Date fromDate , java.sql.Date toDate);
	
	
	List<Object[]> getMonthlyTotalSalesNativeQueryForUsers(java.sql.Date fromDate , java.sql.Date toDate, int userId);
	
	// get total Day Sale value for users wise figures to dash board - uses native
		// query due to complexity
	double getDaySalesValueNativeQueryForNonManagers(java.sql.Date date, Users user);
	
	
	// get Top 10 item of the month for the dashboard using native queryies due to
		// complexity
	List<Object[]> getTopTenItemsOfTheMonth(java.sql.Date fromDate, java.sql.Date toDate);
	
	//to get Pos printer details
	ObservableList<POSPayDetails> getPosPaymentforReprint(long billNo, Users user,java.sql.Date date);
	
	
	
}
