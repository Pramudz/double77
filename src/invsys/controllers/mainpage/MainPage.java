package invsys.controllers.mainpage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainPage extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainPageFormalize.fxml"));

		Scene scene = new Scene(loader.load());
		MainController controlMain = loader.getController();
		controlMain.setLoader(loader);
		
		

		primaryStage.setScene(scene);
		primaryStage.setTitle("Welcome to Pharmacy System");
		controlMain.setThisStage(primaryStage);
		primaryStage.show();

	}

}
