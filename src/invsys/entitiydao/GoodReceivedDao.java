package invsys.entitiydao;

import java.util.List;

import invsys.entities.GoodReceived;
import invsys.entities.GoodReceivedDetails;
import invsys.entities.OrderItems;
import javafx.collections.ObservableList;

public interface GoodReceivedDao {
	
	//save GRN along with updating Purchase Order , On Hand Stock , Average Price of a product
	boolean saveGrn(GoodReceived grn, ObservableList<GoodReceivedDetails> grnDets , List<OrderItems> orderItems ,boolean partialDelStatus, boolean deliverStatus);
	

}
