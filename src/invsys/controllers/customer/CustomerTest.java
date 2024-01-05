package invsys.controllers.customer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CustomerTest extends Application {

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		String loc = "/fxml/customer/CustomerGUI.fxml";
		FXMLLoader loader = new FXMLLoader(getClass().getResource(loc));
		Scene scene = new Scene(loader.load());

		CustomerController control = loader.getController();
		control.setThisMainLoader(loader);
		primaryStage.setScene(scene);
		
		primaryStage.show();

		
	}

}
