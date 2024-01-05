package invsys.entitiydao;

import java.util.List;

import invsys.entities.CashRegister;
import invsys.entities.Users;
import invsys.entities.compositkeys.CashRegisterId;

public interface CashRegisterDao {
	
	CashRegister getCashRegister(CashRegisterId id);
	boolean saveRegister(CashRegister cashReg);
	CashRegister updateCashRegister(CashRegister updateRegister);
	List<Object[]> getPaymentDetailsForCashRegistryNative(long userId, java.sql.Date date);
	List<CashRegister> getNotSignOffCashRegisters(Users user);

}
