package invsys.controllers.cashregister;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CashRegisterTest extends Application {

	public static void main(String[] args) {
		launch(args);
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/cashRegister.fxml"));
		Scene scene = new  Scene(loader.load());
		
		CashRegisterController controller = loader.getController();
		controller.updateUserAndDate("Pramud", "2020-05-20");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

}
