package invsys.controllers.formvalidation;

import java.util.Optional;

import invsys.entities.Users;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class DialogHandler extends Dialog<Users> {

	private static DialogHandler dialog;

	static Label label1 = null;
	private static Label label2 = null;
	private static TextField text1 = null;
	private static PasswordField text2 = null;

	static ButtonType buttonTypeOk = null;
	static ButtonType buttonTypeCancel = null;

	public static Users getDialog() {

		GridPane grid = new GridPane();
		if (dialog == null) {
			System.out.println("Dialog Is Null");
			dialog = new DialogHandler();
			label1 = new Label("Name: ");
			label2 = new Label("Password: ");
			text1 = new TextField();
			text2 = new PasswordField();

			grid = new GridPane();
			grid.add(label1, 1, 1);
			grid.add(text1, 2, 1);
			grid.add(label2, 1, 2);
			grid.add(text2, 2, 2);
			dialog.getDialogPane().setContent(grid);
			buttonTypeOk = new ButtonType("OK", ButtonData.OK_DONE);
			buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
			dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, buttonTypeCancel);

		}

		text1.clear();
		text2.clear();
		dialog.setHeaderText("Supervisor Password Required");

		dialog.setResultConverter(new Callback<ButtonType, Users>() {

			@Override
			public Users call(ButtonType arg0) {
				if (arg0 == buttonTypeOk) {
					Users user = new Users();
					user.setUserName(text1.getText());
					user.setPassword(text2.getText().toString());
					return user;
				} else {
					return null;
				}
			}
		});

		Optional<Users> result = dialog.showAndWait();
		Users userFinal = null;
		
		if(result.isPresent()) {
			userFinal = result.get();
		}
		
		return userFinal;

	}

}
