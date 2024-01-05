package invsys.controllers.formvalidation;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;


public class AlertHandler extends Alert{

	public AlertHandler(AlertType arg0) {
		super(arg0);
		
	}

	private static AlertHandler alert;
	
	private static AlertHandler yesNoAlert;
	

	public static Alert getAlert(AlertType type, String headerText, String contentText) {
		if (alert == null) {
			alert =  new AlertHandler(Alert.AlertType.NONE);
			alert.initModality(Modality.APPLICATION_MODAL);
			System.out.println("Alert was Null Re initiated");
		}
		
		alert.setAlertType(type);
		alert.setHeaderText(headerText);
		alert.setResizable(true);
		alert.setContentText(contentText);
		alert.showAndWait();
		return alert;
	}
	
	public static Alert getAlertYesNo(AlertType type, String headerText, String contentText) {
		if (yesNoAlert == null) {
			yesNoAlert =  new AlertHandler(Alert.AlertType.NONE);
			yesNoAlert.initModality(Modality.WINDOW_MODAL);
			System.out.println("Alert was Null Re initiated");
		}
			
		yesNoAlert.setAlertType(type);
		ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
		ButtonType NoButton = new ButtonType("No", ButtonBar.ButtonData.NO);
		yesNoAlert.getButtonTypes().setAll(yesButton,NoButton);
		yesNoAlert.setHeaderText(headerText);
		yesNoAlert.setResizable(true);
		yesNoAlert.setContentText(contentText);
		yesNoAlert.showAndWait();
		return yesNoAlert;
	}
	

}
