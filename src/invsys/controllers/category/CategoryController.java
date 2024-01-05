package invsys.controllers.category;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import invsys.controllers.database.HibernateUtil;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.controllers.formvalidation.ValidateInputs;
import invsys.entities.Category;
import invsys.entitiydao.CategoryDao;
import invsys.entitiydao.impl.CategoryDaoImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class CategoryController implements Initializable {

	@FXML
	private JFXTextField catNameField;

	@FXML
	private JFXTextField catDesField;
	
	@FXML
	private JFXComboBox<String> mainCatCombo;
	
	@FXML
	private JFXComboBox<String> categoryTypeCombo;

	// category Dao
	CategoryDao categoryDao = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		categoryDao = CategoryDaoImpl.getDao();
		ValidateInputs.getInstance();
		mainCatCombo.getItems().addAll(categoryDao.getMainCategories());
		categoryTypeCombo.getItems().addAll("main","sub");
		
		categoryTypeCombo.setOnAction(e->{
			if(categoryTypeCombo.getSelectionModel().getSelectedItem().contentEquals("main")) {
				
				mainCatCombo.setDisable(true);
			}
			else {
				mainCatCombo.setDisable(false);
			}
			mainCatCombo.getSelectionModel().clearSelection();
			mainCatCombo.getStyleClass().remove("danger-for-warning");
			categoryTypeCombo.getStyleClass().remove("danger-for-warning");
		});
		
		mainCatCombo.setOnAction(e->{
			
			mainCatCombo.getStyleClass().remove("danger-for-warning");
		});
	}

	@FXML
	void clear() {
		catNameField.clear();
		catDesField.clear();
		categoryTypeCombo.getSelectionModel().clearSelection();
		mainCatCombo.getSelectionModel().clearSelection();
		
	}

	@FXML
	void createCategory() {
		if (ValidateInputs.validateTextOnly(catNameField ,"Category Name Field") &&  ValidateInputs.validateInputEmptyComboWithoutEditorStringOnly(categoryTypeCombo , "Category Type") && validateMainCatType()) {

			Category category = new Category();
			category.setCategoryName(catNameField.getText());
			category.setCategoryType(categoryTypeCombo.getSelectionModel().getSelectedItem());
			
			
			if(categoryTypeCombo.getValue().contentEquals("sub")) {
				Category mainCat = categoryDao.getCategoryByName(mainCatCombo.getSelectionModel().getSelectedItem());
				category.setMainCategory(mainCat);
			} else {
				category.setMainCategory(category);
			}
			
			category.setCategoryDescription(catDesField.getText());
			if (categoryDao.saveCategory(category)) {
				AlertHandler.getAlert(AlertType.INFORMATION, "Update Suceess", null);
				clear();
				mainCatCombo.getItems().setAll(categoryDao.getMainCategories());
			}

		}

	}

	@FXML
	void updateCategory(ActionEvent event) {

	}

	public void viewReport() throws SQLException, URISyntaxException, FileNotFoundException, JRException {
		Connection connectionForReports = HibernateUtil.getSessionFactory().getSessionFactoryOptions()
				.getServiceRegistry().getService(ConnectionProvider.class).getConnection();
		String loc = "resources/jasperreports/category1.jasper";
		Map<String, Object> parameters = new HashMap();
		JasperPrint jp = JasperFillManager.fillReport(loc, parameters, connectionForReports);
		JasperViewer.viewReport(jp, false);

	}
	
	
	private boolean validateMainCatType() {
		
		if(categoryTypeCombo.getValue().equals("sub")) {
			return ValidateInputs.validateInputEmptyComboWithoutEditorStringOnly(mainCatCombo , "Main Category Field");
		}
		else {
		 return true;
		}
	}

}
