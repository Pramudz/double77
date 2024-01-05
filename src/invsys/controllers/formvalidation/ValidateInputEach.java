package invsys.controllers.formvalidation;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.controlsfx.control.CheckComboBox;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ValidateInputEach {
	
	private static ValidateInputEach handler;
	
	public static ValidateInputEach getInstance() {
		if(handler == null) {
			handler = new ValidateInputEach();
		}
		return handler;
	}
	

	public static boolean validatePrices(JFXTextField getPrice , Label lable) {
		lable.setText("");
		getPrice.getStyleClass().remove("danger-for-warning");
		Pattern pattern = Pattern.compile("[0-9]*\\.?[0-9]*");
		Matcher m = pattern.matcher(getPrice.getText());
		if (m.matches() && !getPrice.getText().isEmpty()) {
			getPrice.getStyleClass().remove("danger-for-warning");
			lable.setText("");
			return true;
		} else {
			lable.setText("Number Format Error Enter Valid Decimal Number");
			getPrice.getStyleClass().add("danger-for-warning");
			getPrice.requestFocus();
			return false;
		}
	}

	// Validate Prices Method OverLoad for TextField
	public static boolean validatePrices(TextField getPrice) {
		getPrice.getStyleClass().remove("danger-for-warning");
		Pattern pattern = Pattern.compile("[0-9]*\\.?[0-9]*");
		Matcher m = pattern.matcher(getPrice.getText());
		if (m.matches() && !getPrice.getText().isEmpty()) {
			getPrice.getStyleClass().remove("danger-for-warning");
			return true;
		} else {
			AlertHandler.getAlert(AlertType.ERROR, "Number Format Error in " + getPrice.getId() + " price Field",
					"Please Enter Valid Decimal No to Field");
			getPrice.getStyleClass().add("danger-for-warning");
			getPrice.requestFocus();
			return false;
		}
	}

	// for jfx text fileds
	public static boolean validateUserNames(JFXTextField username , Label lable) {
		lable.setText("");
		username.getStyleClass().remove("danger-for-warning");
		Pattern pattern = Pattern.compile("[a-zA-Z.]+");
		Matcher matcher = pattern.matcher(username.getText());
		if (matcher.matches()) {
			lable.setText("");
			return true;
		} else {
			lable.setText("Field Input Format Error allowed only non space characters");
			username.getStyleClass().add("danger-for-warning");
			username.requestFocus();
			return false;
		}

	}

	// for texfileds
	public static boolean validateUserNames(TextField username , Label lable) {
		lable.setText("");
		username.getStyleClass().remove("danger-for-warning");
		Pattern pattern = Pattern.compile("[a-zA-Z.]+");
		Matcher matcher = pattern.matcher(username.getText());
		if (matcher.matches()) {
			lable.setText("");
			return true;
		} else {
			lable.setText("Field Input Format Error allowed only non space characters");
			username.getStyleClass().add("danger-for-warning");
			username.requestFocus();
			return false;
		}

	}
	
	// for a-z letter
		public static boolean validateTextOnly(JFXTextField field, Label label) {
			label.setText("");
			field.getStyleClass().remove("danger-for-warning");
			Pattern pattern = Pattern.compile("[a-zA-Z ]+");
			Matcher matcher = pattern.matcher(field.getText());
			if (matcher.matches()) {
				label.setText("");
				return true;
			} else {
				label.setText("Text Input Format Error");
				field.getStyleClass().add("danger-for-warning");
				field.requestFocus();
				return false;
			}

		}

	public static boolean validateNumbers(JFXTextField numField , Label label) {
		label.setText("");
		numField.getStyleClass().remove("danger-for-warning");
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher match = pattern.matcher(numField.getText());
		if (numField.getText().isEmpty() || !match.matches()) {
			numField.getStyleClass().add("danger-for-warning");
			label.setText("Number Format Error");
			numField.requestFocus();
			return false;
		} else {
			label.setText("");
			return true;
			
		}

	}

	// for text filed validate numbers
	public static boolean validateNumbers(TextField numField, Label label) {
		label.setText("");
		numField.getStyleClass().remove("danger-for-warning");
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher match = pattern.matcher(numField.getText());
		if (numField.getText().isEmpty() || !match.matches()) {
			numField.getStyleClass().add("danger-for-warning");
			label.setText("Number Format Error");
			numField.requestFocus();
			return false;
		} else {
			label.setText("");
			return true;
			
		}

	}
	
	// for text filed validate mobile numbers
		public static boolean validateMobileNumbers(TextField numField, Label label) {
			label.setText("");
			numField.getStyleClass().remove("danger-for-warning");
			Pattern pattern = Pattern.compile("\\d{10}");
			Matcher match = pattern.matcher(numField.getText());
			if (numField.getText().isEmpty() || !match.matches()) {
				numField.getStyleClass().add("danger-for-warning");
				label.setText("Number Format Error");
				numField.requestFocus();
				return false;
			} else {
				label.setText("");
				return true;
				
			}

		}

	// check the Input empty
	public static boolean validateInputEmpty(TextField field , Label label) {
		label.setText("");
		field.getStyleClass().remove("danger-for-warning");
		if (field.getText().isEmpty()) {
			label.setText("Input Empty Error");
			field.getStyleClass().add("danger-for-warning");
			field.requestFocus();
			return false;
		} else
			label.setText("");
			return true;
	}

	// check the Input emptyfield check
	public static boolean validateInputEmptyGeneralBolean(CheckComboBox field , Label label) {
		label.setText("");
		field.getStyleClass().remove("danger-for-warning");
		if (field.getCheckModel().getCheckedItems().isEmpty()) {
			label.setText("Input Empty Error");
			field.getStyleClass().add("danger-for-warning");
			field.requestFocus();
			return false;
		} else
			label.setText("");
			return true;
	}

	// check the Input empty in ComboBox
	public static boolean validateInputEmpty(JFXComboBox field , Label label) {
		label.setText("");
		try {
			field.getStyleClass().remove("danger-for-warning");
			Pattern pattern = Pattern.compile("[a-zA-Z ]*");

			Matcher match = pattern.matcher((field.getEditor().getText()));

			if (!match.matches() || field.getSelectionModel().isEmpty()) {
				label.setText("Input empty error Enter Valid Data");
				field.getStyleClass().add("danger-for-warning");
				field.requestFocus();
				return false;
			} else
				label.setText("");
				return true;

		} catch (Exception e) {
			System.out.println(e.getMessage());
			label.setText("Input empty error in Data");
			field.getStyleClass().add("danger-for-warning");
			field.requestFocus();
			return false;
		}

	}

	//validate Stricly email required
	public static boolean validateEmail(TextField emailField, Label label) {
		label.setText("");
		emailField.getStyleClass().remove("danger-for-warning");
		Pattern p = Pattern.compile(
				"^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
		Matcher matchEmail = p.matcher(emailField.getText());
		if (matchEmail.find() && matchEmail.group().equals(emailField.getText())) {
			label.setText("");
			emailField.getStyleClass().remove("danger-for-warning");
			return true;

		} else {
			label.setText("Please Enter Valid Email");
			emailField.getStyleClass().add("danger-for-warning");
			emailField.requestFocus();

			return false;
		}
	}
	
	//validate Stricly email  with empty allowed required
		public static boolean validateEmailWithEmptyAllowed(TextField emailField, Label label) {
			label.setText("");
			emailField.getStyleClass().remove("danger-for-warning");
			if(emailField.getText().isBlank() || emailField.getText().isEmpty() || emailField.getText().trim().equals("")
					||emailField.getText() == null) {
				label.setText("");
				emailField.getStyleClass().remove("danger-for-warning");
				return true;
			}
			else {
				
				Pattern p = Pattern.compile(
						"^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
				Matcher matchEmail = p.matcher(emailField.getText());
				if (matchEmail.find() && matchEmail.group().equals(emailField.getText())) {
					label.setText("");
					emailField.getStyleClass().remove("danger-for-warning");
					return true;

				} else {
					label.setText("Please Enter Valid Email");
					emailField.getStyleClass().add("danger-for-warning");
					emailField.requestFocus();
					return false;
				}
				
			}
			
		}

	
	//password field validation
	public static boolean validateNewPasswordField(JFXPasswordField passwordField , Label label) {
		label.setText("");
		String ErrorForPattern = "Password must Be Greater than 6 Charcters & must Include Numbers";

		Pattern p = Pattern.compile("((?=.*[a-zA-Z])(?=.*\\d).{6,16})");
		Matcher matchNewPassword = p.matcher(passwordField.getText());

		if (matchNewPassword.matches()) {
			passwordField.getStyleClass().remove("danger-for-warning");
			label.setText("");
			return true;
		} else {
			label.setText("Password must Be Greater than 6 Charcters & must Include Numbers");
			passwordField.getStyleClass().add("danger-for-warning");
			passwordField.requestFocus();
			return false;
		}
	}

	public static boolean validateDateField(DatePicker datePicker , Label label) {
		label.setText("");
		datePicker.getStyleClass().remove("danger-for-warning");
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			// String regex = "^(1[0-2]|0[1-9])/(3[01]" +
			// "|[12][0-9]|0[1-9])/[0-9]{4}$";

				format.setLenient(false);
				format.parse(datePicker.getEditor().getText());
				label.setText("");
				return true;

		} catch (

		Exception e) {
			label.setText("Please Enter Valid Date Ex:2020-05-20");
			datePicker.getStyleClass().add("danger-for-warning");
			return false;
		}
	}
	
	//Date of Birth Validation
	public static boolean validateDobField(DatePicker datePicker , Label label) {
		label.setText("");
		datePicker.getStyleClass().remove("danger-for-warning");
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			// String regex = "^(1[0-2]|0[1-9])/(3[01]" +
			// "|[12][0-9]|0[1-9])/[0-9]{4}$";
				
				format.setLenient(false);
				if(format.parse(datePicker.getEditor().getText()).after(Date.valueOf(LocalDate.now().minusYears(18)))) {
					label.setText("Employee age should be greater than 18");
					datePicker.getStyleClass().add("danger-for-warning");
					return false;
				}else {
					label.setText("");
					return true;
				}
			
				

		} catch (

		Exception e) {
			label.setText("Please enter Valid Date Like 2020-05-20");
			datePicker.getStyleClass().add("danger-for-warning");
			return false;
		}
	}

	// input empty with valud additon method
	public static boolean validateEmptyOnlyField(TextField field, String type ,Label label) {
		label.setText("");
		field.getStyleClass().remove("danger-for-warning");
		if (field.getText().isEmpty() || field.getText().equals("")) {
			label.setText("");
			return true;
		} else if (type == "email") {
			return validateEmail(field , label);
		} else if (type == "contactnumber") {
			return validateNumbers(field, label);
		} else {
			return true;
		}

	}
	
	
	//validate NIC field
 	public static boolean validateNicField(TextField nicField , String fieldName , Label label) {
 		label.setText("");
 		nicField.getStyleClass().remove("danger-for-warning");
		Pattern oldNicPattern = Pattern.compile("^[0-9]{9}[vVxX]$");
		Pattern newNicPattern = Pattern.compile("\\d{12}");
		
		Matcher oldNicMatcher = oldNicPattern.matcher(nicField.getText());
		Matcher newNicMacher = newNicPattern.matcher(nicField.getText());
		if (nicField.getText().isEmpty() || !(oldNicMatcher.matches() || newNicMacher.matches())) {
			label.setText("Please Enter Valid Nic to the "+fieldName);
			nicField.getStyleClass().add("danger-for-warning");
			nicField.requestFocus();
			return false;
		} else {
			label.setText("");
			return true;
		}

	}

}
