package invsys.entitiydao;

import invsys.entities.Category;
import javafx.collections.ObservableList;

public interface CategoryDao {

	
	
	
	abstract boolean saveCategory(Category category);
	abstract boolean updateCategory(Category category);
	abstract boolean deleteCategory(Category category);
	abstract Category getCategoryById(long id);
	abstract Category getCategoryByName(String catName);
	abstract ObservableList<String> getCatNames();
	abstract ObservableList<String> getMainCategories();
	abstract ObservableList<Object[]> getCatIdAndName();
	
	
	
}
