package invsys.controllers.billingwindow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BillingMain extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			
			
			
			FXMLLoader loader = new FXMLLoader(
					(getClass().getResource("/fxml/billingWindowFormalize.fxml")));
			Scene scene = new Scene(loader.load());
			
			
			primaryStage.setScene(scene);
		
			primaryStage.setTitle("Pharmacy System");
			
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	public static void main(String[] args) {
		launch(args);		
	}
}
