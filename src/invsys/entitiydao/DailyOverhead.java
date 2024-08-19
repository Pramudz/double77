/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package invsys.entitiydao;

import invsys.entities.OverheadCategory;
import invsys.entities.compositkeys.DailyOverheadId;
import java.sql.Date;
import javafx.collections.ObservableList;

/**
 *
 * @author Kanishka
 */
public interface DailyOverhead {
   	
	//Save Monthly Overhead values
	boolean saveDailyOverhead(invsys.entities.DailyOverhead overhead);
	
	invsys.entities.DailyOverhead getDailyOverheadById(DailyOverheadId id);
	
	boolean updateDailyOverhead(invsys.entities.DailyOverhead overhead);
	
	OverheadCategory getOverheadCategoryByName(String overhead);
	
	ObservableList<String> getOverheadCatNames();
}
