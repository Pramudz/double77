package invsys.controllers.purchaseorder;

import invsys.entities.Products;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PurchaseOrderMain extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/purchase/purchase.fxml"));
		Scene scene = new Scene(loader.load());
		PurchaseOrderController passLoader = loader.getController();

		// in order for LoadData Class

		Products.setPurchaeOrderLoader(loader);

		primaryStage.setScene(scene);
		passLoader.setCurrentStage(primaryStage);
		passLoader.setOrderLoader(loader);
		primaryStage.show();

	}

}
