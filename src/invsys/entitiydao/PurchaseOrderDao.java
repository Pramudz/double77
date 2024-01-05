package invsys.entitiydao;

import java.util.List;

import invsys.entities.OrderItems;
import invsys.entities.PurchaseOrder;
import javafx.collections.ObservableList;

public interface PurchaseOrderDao {
	
	
	boolean saveOrder(PurchaseOrder purchaseOrder, ObservableList<OrderItems> orderItems);
	
	//specifically for GRN
	// this method is to obtain list of purchase orders of the select purchase order button clicked on GRN GUI where POs will be loaded
	//based on the validity of Purchase order and deleted status
	ObservableList<PurchaseOrder> getPurchaseOrderListForGrn();
	
	
	// load purchase order by order id with complex join
	List<OrderItems> getOrderDetailsById(long orderId);
	
	// load purchase order by order id with complex join
	boolean updateExpireDate(PurchaseOrder order);


}
