package invsys.controllers.pospaydetail;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PosPaymentTest extends Application {

	public static void main(String[] args) {
	
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
	
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/POSPayments.fxml"));

		Scene scene = new Scene(loader.load());
		primaryStage.setScene(scene);
		primaryStage.setTitle("Finalize");
		primaryStage.show();
		
	}

}
