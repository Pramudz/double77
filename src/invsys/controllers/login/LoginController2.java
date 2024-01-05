package invsys.controllers.login;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import invsys.controllers.database.DBHandler;
import invsys.controllers.database.HibernateUtil;
import invsys.controllers.mainpage.MainController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginController2 implements Initializable {

	@FXML
	TextField user;

	@FXML
	PasswordField pass;

	private Stage currentStage;
	private Stage getMainPage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		DBHandler.getInstance();
		HibernateUtil.getSessionFactory();

	}

	public void loginButtonClicked() {
		user.getStyleClass().remove("danger-for-warning");
		pass.getStyleClass().remove("danger-for-warning");
		try {
			if (!user.getText().isEmpty() || !pass.getText().isEmpty()) {
				String usern = user.getText().toString();
				String passw = pass.getText().toString();
				String qu = "select * from user where user_name ='" + usern + "'";
				String date = null;

				ResultSet getUserData = DBHandler.executeQuery(qu);
				if (getUserData.first()) {

					String email = getUserData.getString("email");
					if (!getUserData.getString("password").equals(passw)) {

						pass.getStyleClass().add("danger-for-warning");
					}

					else {
						date = String.valueOf(getUserData.getDate("last_login"));
						System.out.println("yes");
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainPageFormalize.fxml"));
						Scene scene = new Scene(loader.load());
						Stage stage = new Stage(StageStyle.DECORATED);
						stage.setTitle("PharmacyManagement");
						MainController setControllers = loader.getController();
						setControllers.setLoginUser(usern, date, email);

						setControllers.setLoader(loader);

						stage.setScene(scene);
						setControllers.setThisStage(stage);
						setControllers.setMyScene(scene);
						stage.show();

						currentStage.setOnHidden(e -> {
							currentStage = null;
						});
						currentStage.close();

					}

				} else {
					user.getStyleClass().add("danger-for-warning");
				}
			} else {
				user.getStyleClass().add("danger-for-warning");
				pass.getStyleClass().add("danger-for-warning");
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error" + e.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void closeWindow() {
		currentStage.close();
	}

	public void setStage(Stage stage) {
		this.currentStage = stage;
	}

}
