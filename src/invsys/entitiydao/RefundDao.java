package invsys.entitiydao;

import invsys.entities.CreditInvoiceVoid;
import invsys.entities.CreditInvoiceVoidDetail;
import invsys.entities.CustomerRefundDetail;
import invsys.entities.CustomerRefunds;
import javafx.collections.ObservableList;

public interface RefundDao {
	
	
	// save poss bill refund and update stock , update sale refund status to true
	boolean savePossBillRefundAndUpdateStock(CustomerRefunds refund,
			ObservableList<CustomerRefundDetail> refunDetailList);
	
	
	boolean saveCreditInvoiceRefundAndUpdateStock(CreditInvoiceVoid refund,
			ObservableList<CreditInvoiceVoidDetail> refunDetailList);

}
