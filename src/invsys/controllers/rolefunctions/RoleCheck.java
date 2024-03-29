package invsys.controllers.rolefunctions;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RoleCheck extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RoleAndFeatures.fxml"));

		Scene scene = new Scene(loader.load());
		primaryStage.setScene(scene);
		primaryStage.setTitle("Welcome to Pharmacy System");
		primaryStage.show();

	}

}
