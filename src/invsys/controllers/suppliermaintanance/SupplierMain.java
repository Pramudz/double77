package invsys.controllers.suppliermaintanance;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SupplierMain extends Application {

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		/*
		 * Category category = new Category();
		 * 
		 * category.setCategoryName("Pharmacy"); category.setCategory_id("C1001");
		 * CategoryDaoImpl mod = new CategoryDaoImpl(); mod.saveCategory(category);
		 * 
		 * category = new Category(); category.setCategory_id("C1012");
		 * category.setCategoryName("Books"); mod.saveCategory(category);
		 */
		 

		String loc = "/fxml/supplier/supplierMaintanance.fxml";
		FXMLLoader loader = new FXMLLoader(getClass().getResource(loc));
		Scene scene = new Scene(loader.load());
		SupplierMaintananceController controller = loader.getController();
		controller.setThisMainLoader(loader);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

}
