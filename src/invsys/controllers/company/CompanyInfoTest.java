package invsys.controllers.company;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CompanyInfoTest extends Application {

	public static void main(String[] args) {
		launch(args);

	}
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CompanyInfo.fxml"));

		Scene scene = new Scene(loader.load());
		
		primaryStage.setScene(scene);
	
		primaryStage.show();

	}

}
