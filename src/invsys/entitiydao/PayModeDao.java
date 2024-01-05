package invsys.entitiydao;

import invsys.entities.PayModes;
import javafx.collections.ObservableList;

public interface PayModeDao {
	
	boolean savePayMode(PayModes payMode);
	PayModes getPayModeById(String id);
	ObservableList<String> getAllPayModes();
	ObservableList<String> getMainPayModes();
	ObservableList<String> getChildPayModesByMain(String mainPayMode);

}
