package invsys.controllers.company;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;
import org.hibernate.engine.jdbc.BlobProxy;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import invsys.controllers.formvalidation.AlertHandler;
import invsys.controllers.formvalidation.ValidateInputEach;
import invsys.controllers.formvalidation.ValidateInputs;
import invsys.entities.Company;
import invsys.entitiydao.CompanyDao;
import invsys.entitiydao.impl.CompanyDaoImpl;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;
import javafx.util.Duration;

public class CompanyInfoController implements Initializable {
	
	
	 	@FXML
	    private JFXTextField companyNameField;

	    @FXML
	    private JFXTextField streetAddressField;

	    @FXML
	    private JFXTextField addrLineField;

	    @FXML
	    private JFXTextField cityField;

	    @FXML
	    private JFXComboBox<String> provinceField;

	    @FXML
	    private JFXTextField telNoField;

	    @FXML
	    private JFXTextField faxNoField;

	    @FXML
	    private JFXTextField emailField;

	    @FXML
	    private JFXTextField regNoField;

	    @FXML
	    private ImageView imageViewLogo;

	    @FXML
	    private JFXButton addImageButton;

	    @FXML
	    private JFXButton saveButton;
	    
	    //instance variables
	    private Company companyInstance;
	    private byte imageByteArray[];
	    
	    //Company Dao
	    CompanyDao companyDao = null;
	    
	    @Override
		public void initialize(URL location, ResourceBundle resources) {
	    	ValidateInputs.getInstance();
			ValidateInputEach.getInstance();
			companyDao = CompanyDaoImpl.getDao();
			updateLatestCompanyData();
			
			provinceField.getItems().add("Western Province");
			
		}

	    @FXML
	    void clearClicked() {
	    	companyNameField.clear();
	    	streetAddressField.clear();
	    	addrLineField.clear();
			emailField.clear();
			cityField.clear();
			provinceField.getSelectionModel().clearSelection();
			telNoField.clear();
			faxNoField.clear();
			emailField.clear();
			regNoField.clear();
			imageViewLogo.setImage(null);;
			imageByteArray = null;
	    }

	    @FXML
	    void imageChooser() {
	    	
	    	FileChooser imageChooser = new FileChooser();
	    	imageChooser.setTitle("Please Select Logo");
	    	
	    	FileChooser.ExtensionFilter filter = new ExtensionFilter("Image File", "*.jpg");
			FileChooser.ExtensionFilter filter2 = new ExtensionFilter("Image File", "*.png");
			imageChooser.getExtensionFilters().addAll(filter,filter2);
			Window stage = saveButton.getScene().getWindow();
			File file = imageChooser.showOpenDialog(stage);
			
			if(file != null) {
				Image image = new Image(file.toURI().toString(), 181, 170, true, true);
				imageViewLogo.setImage(image);
				try {
					int length = (int) file.length();
					byte byteArray[] = new byte[length];
					FileInputStream fileInputStream = new FileInputStream(file);
					fileInputStream.read(byteArray);
					imageByteArray = byteArray;
					fileInputStream.close();
				}catch(Exception e) {
					AlertHandler.getAlert(AlertType.ERROR, "Error Loading Image", e.getLocalizedMessage());
					e.printStackTrace();
					imageViewLogo.setImage(null);
					imageByteArray= null;
				}
				
			}
	    	

	    }

	    @FXML
	    void saveClicked() {
	    	
	    	if(validateForm()) {
	    		String compName = companyNameField.getText();
	    		String streetAddr = streetAddressField.getText();
	    		String addrLine = addrLineField.getText();
	    		String city = cityField.getText();
	    		String province = provinceField.getValue();
	    		String telNo = telNoField.getText();
	    		String faxNo = faxNoField.getText();
	    		String email = emailField.getText();
	    		
	    	
	    		Company company = new Company();
	    		company.setAddressLine1(streetAddr);
	    		company.setAddressLine2(addrLine);
	    		company.setAddressLine3(city);
	    		company.setCompanyEmail(email);
	    		company.setProvince(province);
	    		company.setCompanyName(compName);
	    		company.setFaxNumber(faxNo);
	    		company.setTelephoneNum(telNo);
	    		
	    		if(imageByteArray != null) {
	    			company.setCompanyLogo(BlobProxy.generateProxy(imageByteArray));
	    		}
	    		
	    		if(!regNoField.getText().isBlank() || !regNoField.getText().isEmpty()) {
		    		company.setCompanyRegNum(regNoField.getText());
	    		}
	    		
	    			    		
	    		if(saveButton.getText().contentEquals("Save")) {
	    			
	    			try {
	    				
	    				if(AlertHandler.getAlert(AlertType.CONFIRMATION, "Are You Sure ?", "Are these details correct ?").getResult().getButtonData().equals(ButtonData.OK_DONE)) {
	    					if(companyDao.saveCompanyInfo(company)) {
	    						Notifications.create().title("Save Success").graphic(null).hideAfter(Duration.seconds(1))
								.darkStyle().position(Pos.CENTER).show();
	    						clearClicked();
	    						
	    					}
	    				}
	    				
	    			}catch(Exception e) {
	    				AlertHandler.getAlert(AlertType.ERROR, "Error Saving Data", e.getLocalizedMessage());
	    				e.printStackTrace();
	    			}
	    			
	    			
	    		}
	    		
	    		if(saveButton.getText().contentEquals("Update")) {
	    			
	    			try {
	    				company.setCompId(companyInstance.getCompId());
	    				if(AlertHandler.getAlert(AlertType.CONFIRMATION, "Are You Sure ?", "You want to Update").getResult().getButtonData().equals(ButtonData.OK_DONE)) {
	    					if(companyDao.updateCompanyInfo(company)) {
	    						Notifications.create().title("Save Success").graphic(null).hideAfter(Duration.seconds(1))
								.darkStyle().position(Pos.CENTER).show();
	    						clearClicked();
	    						
	    					}
	    				}
	    				
	    			}catch(Exception e) {
	    				AlertHandler.getAlert(AlertType.ERROR, "Error Saving Data", e.getLocalizedMessage());
	    				e.printStackTrace();
	    			}
	    			
	    			
	    		}
	    	}

	    }
	    
	    private boolean validateForm() {
			return ValidateInputs.validateTextOnly(companyNameField ,"Company Name Field") &&
					ValidateInputs.validateInputEmpty(streetAddressField , "Street Address Field") &&
					ValidateInputs.validateInputEmpty(addrLineField ,"Address Field") &&
					ValidateInputs.validateTextOnly(cityField , "City Field") &&
					ValidateInputs.validateInputEmptyComboWithoutEditorStringOnly(provinceField , "Province Field") &&
					ValidateInputs.validateTelphoneNumbers(telNoField ,"Telephone Number Field") &&
					ValidateInputs.validateTelphoneNumbers(faxNoField ,"Fax Number Field") &&
					ValidateInputs.validateEmail(emailField , "Email Field");
	    	
	    }

	    
	    public void updateLatestCompanyData() {
	    	
	    	companyInstance = companyDao.getCompanyLast();
	    	
	    	if(companyInstance == null) {
	    		System.out.println("null value");
	    		saveButton.setText("Save");
	    		
	    	}
	    	
	    	else {
	    		saveButton.setText("Update");
	    		companyNameField.setText(companyInstance.getCompanyName());
		    	streetAddressField.setText(companyInstance.getAddressLine1());
	    		addrLineField.setText(companyInstance.getAddressLine2());
	    		cityField.setText(companyInstance.getAddressLine3());
	    		provinceField.setValue(companyInstance.getProvince());
	    		telNoField.setText(companyInstance.getTelephoneNum());
	    		faxNoField.setText(companyInstance.getFaxNumber());
	    		emailField.setText(companyInstance.getCompanyEmail());
	    		
	    		if(companyInstance.getCompanyLogo() != null) {
	    			try {
						InputStream inputStream = companyInstance.getCompanyLogo().getBinaryStream();
						File file = new File(companyInstance.getCompId()+".ico");
						FileOutputStream outputStream = new FileOutputStream(file);
						int k;
						while((k=inputStream.read()) !=-1) {
							outputStream.write(k);
						}
						outputStream.close();
						System.out.println(file.getCanonicalPath());
						Image image = new Image(file.toURI().toString(), 181, 170, true, true);
						imageViewLogo.setImage(image);
						int length = (int) file.length();
						byte byteArray[] = new byte[length];
						FileInputStream fileInputStream = new FileInputStream(file);
						fileInputStream.read(byteArray);
						imageByteArray = byteArray;
						fileInputStream.close();
						file.delete();
	    			}
						catch (Exception e) {
							System.out.println("error Loading Image");
							e.printStackTrace();
						}
	    		
	    		}
	    		
	    		if(companyInstance.getCompanyRegNum() != null) {
	    			regNoField.setText(companyInstance.getCompanyRegNum());
	    		}
	    	}
	    	
	    
    		
	    }
	    
	    
	    public void removeImage() {
	    	imageViewLogo.setImage(null);
	    	imageByteArray = null;
	    }
	    	
	    
		

}
