package invsys.controllers.supplierpayments;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SupPayTest extends Application {

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/supplier/SupplierPayments.fxml"));
		Scene scene = new Scene(loader.load());
		SupplierPaymentController controlMe = loader.getController();
		controlMe.setThisSupplierPaymentLoader(loader);
		
			
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
		
	}

}
