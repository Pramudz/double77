package invsys.controllers.goodreceived;



import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GrnTest extends Application{

	public static void main(String[] args) {
		
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/grn/GrnGUi.fxml"));
		Scene scene = new Scene(loader.load());
		GoodReceivedController grnController = loader.getController();
		
		grnController.setThisGrnLoader(loader);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
