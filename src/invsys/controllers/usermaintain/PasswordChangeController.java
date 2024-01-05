package invsys.controllers.usermaintain;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.codec.digest.DigestUtils;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import invsys.controllers.formvalidation.AlertHandler;
import invsys.controllers.formvalidation.ValidateInputs;
import invsys.entities.Users;
import invsys.entitiydao.UserDao;
import invsys.entitiydao.impl.UserDaoImpl;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class PasswordChangeController implements Initializable {
	
	
	   	@FXML
	    private JFXTextField userNameField;

	    @FXML
	    private JFXPasswordField currentPasswordField;

	    @FXML
	    private JFXPasswordField newPasswordField;

	    @FXML
	    private JFXPasswordField confirmPassowrdField;
	    
	    @FXML
	    private JFXButton updateButton;
	    
	    private Users user;
	    
	    
	    
	    //check current password verification not more than 3 times
	    //other wise account will be locked
	    private int tryCount;
	    
	    //current password match status
	    private boolean currentPasswordMatchStatus;
	    
	    private Stage mainControllerStage;
	    
	    //DAO Handlers
	    
	    UserDao userDao = null;
	    
	    
	    @Override
		public void initialize(URL location, ResourceBundle resources) {
	    	
			tryCount =0;
			userDao = UserDaoImpl.getDao();
			ValidateInputs.getInstance();
			newPasswordField.setDisable(true);
			confirmPassowrdField.setDisable(true);
		
			currentPasswordAction();
			
		}

	  
	    @FXML
	    public void cancelButtonAction() {
	    	
	    	userNameField.getScene().getWindow().hide();

	    }
  
	    
	    private void currentPasswordAction() {
	    	
	    	currentPasswordField.setOnAction(e->{
	    		currentPasswordMatchStatus = false;
	    		String curPassword = DigestUtils.sha1Hex(currentPasswordField.getText());
	    	
	    		if(!curPassword.equals(user.getPassword())) {
	    			++tryCount;
	    			if(tryCount >= 3) {
	    			user.setActiveStat(false);
	    			user.setLoginStatus(false);
	    			if(userDao.updateUser(user)) {
	    				AlertHandler.getAlert(AlertType.ERROR,"Your Account has been Blocked", "Contact  Your Manager or Administrator");
	    				cancelButtonAction();
	    				mainControllerStage.close();
	    				System.exit(1);
	    			}
	    		}
	    			else {
	    				AlertHandler.getAlert(AlertType.ERROR, "Wrong Password", "Password Invalid");
	    		}
	    			
	    			
	    		} else {
	    			
	    			
	    			newPasswordField.setDisable(false);
	    			
	    			newPasswordField.requestFocus();
	    			currentPasswordMatchStatus = true;
	    			newPasswordField.setOnAction(x->{
	    	    		if(ValidateInputs.validateNewPasswordField(newPasswordField ,"New Password Field")){
	    	    			
	    	    		if(curPassword.contentEquals(DigestUtils.sha1Hex(newPasswordField.getText()))) {
	    	    			AlertHandler.getAlert(AlertType.WARNING, "Same Password Warining", "Consider entering different password if you really want change your password");
	    	    		}
	    	    		
	    	    		confirmPassowrdField.setDisable(false);
	    	    		newPasswordField.setDisable(true);
	    	    		confirmPassowrdField.requestFocus();
	    	    	}
	    	    	});
	    	    	
	    			    			
	    			
	    		}
	    	});
	    	
	    }
	    
	 
	   
	   
	    //confirm password after saving the new password
	    @FXML
	    public void confirmPasswordAction() {
	    	confirmPassowrdField.getStyleClass().remove("danger-for-warning");
	    	try {
	    		
	    		if(!currentPasswordMatchStatus) {
	    					AlertHandler.getAlert(AlertType.ERROR, "Current Password Error","Current Password Does Not Match");
	    		}
	    		else {
	    		
	    		if(ValidateInputs.validateNewPasswordField(newPasswordField, "New Password Field")) {
	    			
	    		
	    		String newPassword = newPasswordField.getText();
	    		String confirmPassword = confirmPassowrdField.getText();
	    		
	    		
	    		
	    		if(newPassword.equals(confirmPassword)) {
	    			user.setPassword(confirmPassword);
	    		
	    		if(userDao.updateUserPassword(user)) {
	    		AlertHandler.getAlert(AlertType.INFORMATION, "Sucess", "Your Password Has Been changed Please login again");
	    		cancelButtonAction();
	    		mainControllerStage.close();
	    		System.exit(1);
	    		}
	    		} else {
	    			AlertHandler.getAlert(AlertType.ERROR, "Password Does Matched", "Please Re enter confirm password");
	    			confirmPassowrdField.getStyleClass().add("danger-for-warning");
	    			confirmPassowrdField.requestFocus();
	    			
	    		}
	    		
	    		}
 		}
	    	}catch(Exception e) {
	    		AlertHandler.getAlert(AlertType.ERROR, "Exeption Caugh in Confirm Action Method", e.getLocalizedMessage());
	    		e.printStackTrace();
	    	}
	   	    	
	    }
	    
	    //get user data from maincontroller to update password
	    public void setUserDataAndMainStage(Users user, Stage stage) {
	    	this.user = user;
	    	userNameField.setText(this.user.getUserName());
	    	this.mainControllerStage = stage;
	    }

		
}


