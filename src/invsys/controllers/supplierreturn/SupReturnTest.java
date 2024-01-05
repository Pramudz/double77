package invsys.controllers.supplierreturn;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SupReturnTest extends Application {

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/supplier/SupplierReturn.fxml"));
		Scene scene = new Scene(loader.load());
		SupplierReturnController controlMe = loader.getController();
		controlMe.setThisSupplierReturnLoader(loader);
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

}
