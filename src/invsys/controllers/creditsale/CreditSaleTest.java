package invsys.controllers.creditsale;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CreditSaleTest extends Application {
	
	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		String loc = "/fxml/creditInvoice.fxml";
		FXMLLoader loader = new FXMLLoader(getClass().getResource(loc));
		Scene scene = new Scene(loader.load());

		CreditSaleController controlme = loader.getController();
		controlme.setThiLoader(loader);
		primaryStage.setScene(scene);
		
		primaryStage.show();

		
	}
}
