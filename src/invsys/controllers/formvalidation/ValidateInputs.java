package invsys.controllers.formvalidation;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.controlsfx.control.CheckComboBox;

import com.ibm.icu.text.DecimalFormat;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class ValidateInputs {

	private static ValidateInputs handler;

	public static ValidateInputs getInstance() {
		if (handler == null) {
			handler = new ValidateInputs();
		}
		return handler;
	}

	public static boolean validatePrices(JFXTextField getPrice , String fieldName) {
		getPrice.getStyleClass().remove("danger-for-warning");
		Pattern pattern = Pattern.compile("[0-9]*\\.?[0-9]*");
		Matcher m = pattern.matcher(getPrice.getText());
		if (m.matches() && !getPrice.getText().isEmpty()) {
			getPrice.getStyleClass().remove("danger-for-warning");
			return true;
		} else {
			AlertHandler.getAlert(AlertType.ERROR, "Number Format Error in " +fieldName,
					"Please Enter Valid Decimal Number to "+fieldName);

			getPrice.getStyleClass().add("danger-for-warning");
			getPrice.requestFocus();
			return false;
		}
	}

	// Validate Prices Method OverLoad for TextField
	public static boolean validatePrices(TextField getPrice , String fieldName) {
		getPrice.getStyleClass().remove("danger-for-warning");
		Pattern pattern = Pattern.compile("[0-9]*\\.?[0-9]*");
		Matcher m = pattern.matcher(getPrice.getText());
		if (m.matches() && !getPrice.getText().isEmpty()) {
			getPrice.getStyleClass().remove("danger-for-warning");
			return true;
		} else {
			AlertHandler.getAlert(AlertType.ERROR, "Number Format Error in " + fieldName,
					"Please Enter Valid Decimal Number to " + fieldName);
			getPrice.getStyleClass().add("danger-for-warning");
			getPrice.requestFocus();
			return false;
		}
	}

	// for jfx text fileds
	public static boolean validateUserNames(JFXTextField username , String fieldName) {
		username.getStyleClass().remove("danger-for-warning");
		Pattern pattern = Pattern.compile("[a-zA-Z.]+");
		Matcher matcher = pattern.matcher(username.getText());
		if (matcher.matches()) {
			return true;
		} else {
			AlertHandler.getAlert(AlertType.ERROR, "Text Input Format Error in " + fieldName,
					"Please Enter Valid a-z  to "+fieldName);
			username.getStyleClass().add("danger-for-warning");
			username.requestFocus();
			return false;
		}

	}

	// for texfileds
	public static boolean validateUserNames(TextField username , String fieldName) {
		username.getStyleClass().remove("danger-for-warning");
		Pattern pattern = Pattern.compile("[a-zA-Z.]+");
		Matcher matcher = pattern.matcher(username.getText());
		if (matcher.matches()) {
			return true;
		} else {
			AlertHandler.getAlert(AlertType.ERROR, "Text Input Format Error in " + fieldName,
					"Please Enter Valid a-z  to "+fieldName);
			username.getStyleClass().add("danger-for-warning");
			username.requestFocus();
			return false;
		}

	}

	// for a-z letter
	public static boolean validateTextOnly(JFXTextField field , String fieldName) {
		field.getStyleClass().remove("danger-for-warning");
		Pattern pattern = Pattern.compile("[a-zA-Z ]+");
		Matcher matcher = pattern.matcher(field.getText());
		if (matcher.matches()) {
			return true;
		} else {
			AlertHandler.getAlert(AlertType.ERROR, "Text Input Format Error in " +fieldName,
					"Please Enter Valid a-z  to "+fieldName);
			field.getStyleClass().add("danger-for-warning");
			field.requestFocus();
			return false;
		}

	}

	// for a-z letter
	public static boolean validateTextOnly(TextField field , String fieldName) {
		field.getStyleClass().remove("danger-for-warning");
		Pattern pattern = Pattern.compile("[a-zA-Z ]+");
		Matcher matcher = pattern.matcher(field.getText());
		if (matcher.matches()) {
			return true;
		} else {
			AlertHandler.getAlert(AlertType.ERROR, "Text Input Format Error in " + fieldName,
					"Please Enter Valid a-z  to "+fieldName);
			field.getStyleClass().add("danger-for-warning");
			field.requestFocus();
			return false;
		}

	}

	public static boolean validateNumbers(JFXTextField numField , String fieldName) {
		numField.getStyleClass().remove("danger-for-warning");
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher match = pattern.matcher(numField.getText());
		if (numField.getText().isEmpty() || !match.matches()) {
			AlertHandler.getAlert(AlertType.ERROR, "Number Format Error in " + fieldName,
					"Please Enter Valid 0-9 No to "+fieldName);
			numField.getStyleClass().add("danger-for-warning");
			numField.requestFocus();
			return false;
		} else {
			return true;
		}

	}

	// validate quantity of the unit measure type "Unit" to make sure entering
	// data is not decimal
	public static boolean validateQtyForUnitItems(TextField numField , String fieldName) {
		numField.getStyleClass().remove("danger-for-warning");
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher match = pattern.matcher(numField.getText());
		if (numField.getText().isEmpty() || !match.matches()) {
			AlertHandler.getAlert(AlertType.ERROR, "Invalid Quntity Error in " + fieldName,
					"this item does not allow decimal quantity values");
			numField.getStyleClass().add("danger-for-warning");
			numField.requestFocus();
			return false;
		} else {
			return true;
		}

	}

	// validate quantity of the unit measure type "Unit" to make sure entering
	// data is not decimal
	public static boolean validateQtyForUnitItemsWithString(String value , String fieldName) {

		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher match = pattern.matcher(value);
		if (value.isEmpty() || !match.matches()) {
			AlertHandler.getAlert(AlertType.ERROR, "Invalid Quntity Error in "+fieldName,
					"this item does not allow decimal quantity values");
			return false;
		} else {
			return true;
		}

	}

	// for text filed validate numbers
	public static boolean validateNumbers(TextField numField , String fieldName) {
		numField.getStyleClass().remove("danger-for-warning");
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher match = pattern.matcher(numField.getText());
		if (numField.getText().isEmpty() || !match.matches()) {
			AlertHandler.getAlert(AlertType.ERROR, "Number Format Error in" + fieldName,
					"Please Enter Valid 0-9 No to "+fieldName);
			numField.getStyleClass().add("danger-for-warning");
			numField.requestFocus();
			return false;
		} else {
			return true;
		}

	}

	public static boolean validateTelphoneNumbers(TextField numField , String fieldName) {
		numField.getStyleClass().remove("danger-for-warning");
		Pattern pattern = Pattern.compile("\\d{10}");
		Matcher match = pattern.matcher(numField.getText());
		if (numField.getText().isEmpty() || !match.matches()) {
			AlertHandler.getAlert(AlertType.ERROR, "Number Format Error in " + fieldName,
					"Please Enter Valid 0-9 No to "+fieldName);
			numField.getStyleClass().add("danger-for-warning");
			numField.requestFocus();
			return false;
		} else {
			return true;
		}

	}

	// check the Input empty
	public static boolean validateInputEmpty(TextField field , String fieldName) {
		field.getStyleClass().remove("danger-for-warning");
		if (field.getText().isEmpty()) {
			AlertHandler.getAlert(AlertType.ERROR, "Input Empty Error in " + fieldName,
					"Please Enter Valid details to "+fieldName);
			field.getStyleClass().add("danger-for-warning");
			field.requestFocus();
			return false;
		} else
			return true;
	}

	// check the Input emptyfield check
	public static boolean validateInputEmptyGeneralBolean(CheckComboBox field , String fieldName) {
		field.getStyleClass().remove("danger-for-warning");
		if (field.getCheckModel().getCheckedItems().isEmpty()) {
			AlertHandler.getAlert(AlertType.ERROR, "Input Empty Error in " + fieldName,
					"Please Enter Valid details to "+fieldName);
			field.getStyleClass().add("danger-for-warning");
			field.requestFocus();
			return false;
		} else
			return true;
	}

	// check the Input empty in ComboBox
	public static boolean validateInputEmpty(JFXComboBox field , String fieldName) {

		try {
			field.getStyleClass().remove("danger-for-warning");
			Pattern pattern = Pattern.compile("[a-zA-Z ]*");

			Matcher match = pattern.matcher((field.getEditor().getText()));

			if (!match.matches() || field.getSelectionModel().isEmpty()) {
				AlertHandler.getAlert(AlertType.ERROR, "Input Empty Error in " + fieldName,
						"Please Enter Valid details to "+fieldName);
				field.getStyleClass().add("danger-for-warning");
				field.requestFocus();
				return false;
			} else
				return true;

		} catch (Exception e) {
			System.out.println(e.getMessage());
			AlertHandler.getAlert(AlertType.ERROR, "Input Empty Error in " + fieldName,
					"Please Enter Valid details to "+fieldName);
			field.getStyleClass().add("danger-for-warning");
			field.requestFocus();
			return false;
		}

	}

	// check the Input empty in ComboBox withoutEditorChecing String Only
	public static boolean validateInputEmptyComboWithoutEditorStringOnly(ComboBox<String> field , String fieldName) {

		try {
			field.getStyleClass().remove("danger-for-warning");
			if (field.getValue().isBlank() || field.getValue().isEmpty()) {
				AlertHandler.getAlert(AlertType.ERROR, "Input Empty Error in " + fieldName,
						"Please Select Valid details to ComboBox "+fieldName);
				field.getStyleClass().add("danger-for-warning");
				field.requestFocus();
				return false;
			} else
				field.getStyleClass().remove("danger-for-warning");
			return true;

		} catch (Exception e) {
			System.out.println(e.getMessage());
			AlertHandler.getAlert(AlertType.ERROR, "Input Empty Error in " + fieldName,
					"Please Enter Valid details to "+fieldName);
			field.getStyleClass().add("danger-for-warning");
			field.requestFocus();
			return false;
		}

	}

	// check the Input empty in ComboBox withoutEditorChecing Short type year Only
	public static boolean validateInputEmptyComboWithoutEditorYear(JFXComboBox<Short> field , String fieldName) {

		try {
			field.getStyleClass().remove("danger-for-warning");
			if (field.getValue() == null) {
				AlertHandler.getAlert(AlertType.ERROR, "Input Empty Error in " + fieldName,
						"Please Select Valid details to ComboBox");
				field.getStyleClass().add("danger-for-warning");
				field.requestFocus();
				return false;
			} else
				field.getStyleClass().remove("danger-for-warning");
			return true;

		} catch (Exception e) {
			System.out.println(e.getMessage());
			AlertHandler.getAlert(AlertType.ERROR, "Input Empty Error in " +fieldName,
					"Please Enter Valid details to "+fieldName);
			field.getStyleClass().add("danger-for-warning");
			field.requestFocus();
			return false;
		}

	}

	// check the Input empty in ComboBox withoutEditorChecing Month Only
	public static boolean validateInputEmptyComboWithoutEditorMonth(JFXComboBox<Month> field , String fieldName) {

		try {
			field.getStyleClass().remove("danger-for-warning");
			if (field.getValue() == null) {
				AlertHandler.getAlert(AlertType.ERROR, "Input Empty Error in " + fieldName,
						"Please Select Valid details to ComboBox "+fieldName);
				field.getStyleClass().add("danger-for-warning");
				field.requestFocus();
				return false;
			} else
				field.getStyleClass().remove("danger-for-warning");
			return true;

		} catch (Exception e) {
			System.out.println(e.getMessage());
			AlertHandler.getAlert(AlertType.ERROR, "Input Empty Error in " +fieldName,
					"Please Enter Valid details to "+fieldName);
			field.getStyleClass().add("danger-for-warning");
			field.requestFocus();
			return false;
		}

	}

	// validate Stric Email required
	public static boolean validateEmail(TextField emailField , String fieldName) {
		Pattern p = Pattern.compile(
				"^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
		Matcher matchEmail = p.matcher(emailField.getText());
		if (matchEmail.find() && matchEmail.group().equals(emailField.getText())) {
			emailField.getStyleClass().remove("danger-for-warning");
			return true;

		} else {
			AlertHandler.getAlert(AlertType.ERROR, "Email Validation Error in " + fieldName,
					"Please Enter Valid Email Address to "+fieldName);
			emailField.getStyleClass().add("danger-for-warning");
			emailField.requestFocus();

			return false;
		}
	}

	// validate Empty Allowed Email Field
	public static boolean validateEmailEmptyAllowed(TextField emailField , String fieldName) {
		emailField.getStyleClass().remove("danger-for-warning");
		if (emailField.getText().isBlank() || emailField.getText().isEmpty() || emailField.getText().trim().equals("")
				|| emailField.getText() == null) {
			emailField.getStyleClass().remove("danger-for-warning");
			return true;
		} else {

			Pattern p = Pattern.compile(
					"^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
			Matcher matchEmail = p.matcher(emailField.getText());
			if (matchEmail.find() && matchEmail.group().equals(emailField.getText())) {
				emailField.getStyleClass().remove("danger-for-warning");
				return true;

			} else {
				AlertHandler.getAlert(AlertType.ERROR, "Email Validation Error in " + fieldName,
						"Please Enter Valid Email Address to "+fieldName);
				emailField.getStyleClass().add("danger-for-warning");
				emailField.requestFocus();

				return false;
			}
		}
	}

	
	//validate new password field
	public static boolean validateNewPasswordField(JFXPasswordField passwordField , String fieldName) {

		String ErrorForPattern = "Password must Be Greater than 6 Charcters & must Include Numbers";

		Pattern p = Pattern.compile("((?=.*[a-zA-Z])(?=.*\\d).{6,16})");
		Matcher matchNewPassword = p.matcher(passwordField.getText());

		if (matchNewPassword.matches()) {
			passwordField.getStyleClass().remove("danger-for-warning");
			return true;
		} else {
			AlertHandler.getAlert(AlertType.ERROR, "Password Validation Error in " + fieldName,
					ErrorForPattern);
			passwordField.getStyleClass().add("danger-for-warning");
			passwordField.requestFocus();
			return false;
		}
	}

	
	//validate date field
	public static boolean validateDateField(DatePicker datePicker , String fieldName) {
		datePicker.getStyleClass().remove("danger-for-warning");
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			// String regex = "^(1[0-2]|0[1-9])/(3[01]" +
			// "|[12][0-9]|0[1-9])/[0-9]{4}$";

			format.setLenient(false);
			format.parse(datePicker.getEditor().getText());

			return true;

		} catch (

		Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Date Validation Error in " +fieldName,
					"pls Enter Valid Date Type Like 2020-10-20 to "+fieldName);
			datePicker.getStyleClass().add("danger-for-warning");
			return false;
		}
	}
	
	public static boolean validateDateField(JFXDatePicker datePicker , String fieldName) {
		datePicker.getStyleClass().remove("danger-for-warning");
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			// String regex = "^(1[0-2]|0[1-9])/(3[01]" +
			// "|[12][0-9]|0[1-9])/[0-9]{4}$";

			format.setLenient(false);
			format.parse(datePicker.getEditor().getText());

			return true;

		} catch (

		Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Date Validation Error in "+fieldName,
					"pls Enter Valid Date Type Like 2020-10-20 to "+fieldName);
			datePicker.getStyleClass().add("danger-for-warning");
			return false;
		}
	}

	public static boolean validateDobField(DatePicker datePicker , String fieldName) {
		datePicker.getStyleClass().remove("danger-for-warning");
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			// String regex = "^(1[0-2]|0[1-9])/(3[01]" +
			// "|[12][0-9]|0[1-9])/[0-9]{4}$";

			format.setLenient(false);
			if (format.parse(datePicker.getEditor().getText()).after(Date.valueOf(LocalDate.now().minusYears(18)))) {
				AlertHandler.getAlert(AlertType.ERROR, "Employee age should be greater than 18", "Please Enter valid Date to "+fieldName);
				datePicker.getStyleClass().add("danger-for-warning");
				return false;
			} else {
				return true;
			}

		} catch (

		Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Date Validation Error",
					"pls Enter Valid Date Type Like 25-12-2018 to "+fieldName);
			datePicker.getStyleClass().add("danger-for-warning");
			return false;
		}
	}
	
	
	public static boolean validateDateWithFutureDays(DatePicker datePicker , String fieldName , long noFutureDaysToCheck) {
		datePicker.getStyleClass().remove("danger-for-warning");
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			// String regex = "^(1[0-2]|0[1-9])/(3[01]" +
			// "|[12][0-9]|0[1-9])/[0-9]{4}$";

			format.setLenient(false);
			if (format.parse(datePicker.getEditor().getText()).before(Date.valueOf(LocalDate.now().plusDays(noFutureDaysToCheck)))) {
				AlertHandler.getAlert(AlertType.ERROR, "Date Validation Error",fieldName+" Date must be atleast "+noFutureDaysToCheck+" days greater than Today date");
				datePicker.getStyleClass().add("danger-for-warning");
				return false;
			} else {
				return true;
			}

		} catch (

		Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Date Validation Error",
					"pls Enter Valid Date Type Like 25-12-2018 to "+fieldName);
			datePicker.getStyleClass().add("danger-for-warning");
			return false;
		}
	}

	// input empty with valud additon method
	public static boolean validateEmptyOnlyField(TextField field, String type , String fieldName) {
		field.getStyleClass().remove("danger-for-warning");
		if (field.getText().isEmpty() || field.getText().equals("")) {
			return true;
		} else if (type == "email") {
			return validateEmail(field,fieldName);
		} else if (type == "contactnumber") {
			return validateNumbers(field, fieldName);
		} else {
			return true;
		}

	}
	
	// date formatter for date picker
	 	public static void setDateFormatter(JFXDatePicker piicker) {

	 		// Converter
	 		javafx.util.StringConverter<LocalDate> converter = new javafx.util.StringConverter<LocalDate>() {
	 			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	 			@Override
	 			public String toString(LocalDate date) {
	 				if (date != null) {
	 					return dateFormatter.format(date);
	 				} else {
	 					return "";
	 				}
	 			}

	 			@Override
	 			public LocalDate fromString(String string) {
	 				if (string != null && !string.isEmpty()) {
	 					return LocalDate.parse(string, dateFormatter);
	 				} else {
	 					return null;
	 				}
	 			}
	 		};

	 		piicker.setConverter(converter);
	 		
	 	}
	 	
	 	//validate NIC field
	 	public static boolean validateNicField(TextField nicField , String fieldName) {
	 		nicField.getStyleClass().remove("danger-for-warning");
			Pattern oldNicPattern = Pattern.compile("^[0-9]{9}[vVxX]$");
			Pattern newNicPattern = Pattern.compile("\\d{12}");
			
			Matcher oldNicMatcher = oldNicPattern.matcher(nicField.getText());
			Matcher newNicMacher = newNicPattern.matcher(nicField.getText());
			if (nicField.getText().isEmpty() || !(oldNicMatcher.matches() || newNicMacher.matches())) {
				AlertHandler.getAlert(AlertType.ERROR, "NIC Format Error In " + fieldName,
						"Please Enter Valid NIC No to "+fieldName);
				nicField.getStyleClass().add("danger-for-warning");
				nicField.requestFocus();
				return false;
			} else {
				return true;
			}

		}
	 	
	 	//Decimal Value formatter for Numbers
	 	public static String getDecimalFormattedNumber(Number doubleValue) {
	 		
	 		String pattern = "###,###.##";
	 		DecimalFormat formatter = new DecimalFormat(pattern);
	 		String output = formatter.format(doubleValue);
	 		return output;
	 	}

}
