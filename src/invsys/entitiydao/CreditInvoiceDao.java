package invsys.entitiydao;

import java.util.List;

import invsys.entities.CreditInvoice;
import invsys.entities.CreditInvoiceDetail;
import invsys.entities.Users;
import javafx.collections.ObservableList;

public interface CreditInvoiceDao {
	
	boolean saveCreditInvoice(CreditInvoice sale, ObservableList<CreditInvoiceDetail> salesDetails);
	
	long  getLastBillNo(Users user,java.sql.Date date);
	
	List<CreditInvoiceDetail> getInvoiceDetailsByBillNoDateUser(long billNo,java.sql.Date date, Users user);
	
	boolean settleCreditInvoice(CreditInvoice settleInvoice);
}
