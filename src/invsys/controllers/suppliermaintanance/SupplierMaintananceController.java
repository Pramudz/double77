package invsys.controllers.suppliermaintanance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;

import org.controlsfx.control.Notifications;
import org.hibernate.engine.jdbc.BlobProxy;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import invsys.controllers.formvalidation.AlertHandler;
import invsys.controllers.formvalidation.ValidateInputEach;
import invsys.controllers.formvalidation.ValidateInputs;
import invsys.controllers.purchaseorder.GetSupplierController;
import invsys.entities.Supplier;
import invsys.entities.SupplierBrImages;
import invsys.entitiydao.SupplierDao;
import invsys.entitiydao.impl.SupplierDaoImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;

public class SupplierMaintananceController implements Initializable {

	@FXML
	private Label supDescLabel;

	@FXML
	private JFXTextField viewSupCodeField;

	@FXML
	private TextField compNameFiled;

	@FXML
	private TextField supCodeField;

	@FXML
	private TextField supNameField;

	@FXML
	private TextField stretAddressField;

	@FXML
	private TextField addressField;

	@FXML
	private TextField lastNameField;

	@FXML
	private TextField desigField;

	@FXML
	private TextField cityField;

	@FXML
	private TextField emailField;

	@FXML
	private TextField mobNumField;

	@FXML
	private TextField telephoneNumField;

	@FXML
	private TextField payPeriodField;

	@FXML
	private TextField brNumberField;

	@FXML
	private JFXButton actionButton;

	@FXML
	private JFXButton updateActionButton;

	@FXML
	private JFXButton addImageButton;

	@FXML
	private HBox fileChooserHbox;

	// to maintain drag and drop feathers for delete images
	@FXML
	private ImageView recycleBinImageView;

	@FXML
	private JFXTextField thisSearchTextField;

	@FXML
	private TableView supplierTableView;

	@FXML
	private TableColumn<Supplier, Integer> supCodeCol;

	@FXML
	private TableColumn<Supplier, String> supNameCol;

	@FXML
	private TableColumn<Supplier, String> cityCol;

	@FXML
	private TableColumn<Supplier, String> addressCol;

	@FXML
	private TableColumn<Supplier, Integer> phoneCol;

	@FXML
	private TableColumn<Supplier, String> supCreateDateCol;

	@FXML
	private VBox vboxCss;
	
	// to manage form entering error handling realtime
	@FXML
	private Label errorMessegeLabel;

	// supplier model for hibernate database supplier mode
	

	// Supplier instance for viewSupplier & delete supplier
	Supplier supplierInstance;

	// this SupplierMaintenance FXMLLoader inorder to pass throuhg the scenes

	private FXMLLoader thisMainLoader;

	// stage for supplier table view

	private Stage supplierStage;

		// to maintain a instance image to delete image from imageView sets from
	// filechooseer VBox
	private ImageView deletedImageView;
	
	//in order to maintain the updated with Images function
	private boolean updatedwithImageStatus = false;

	HashMap<ImageView, byte[]> mapForImage = new HashMap();

	//DAO handlers- Interfaces
	SupplierDao supplierDao = null;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// DBHandler.getInstance();
		// HibernateUtil.getSessionFactory();
		ValidateInputs.getInstance();
		ValidateInputEach.getInstance();
		supplierDao = SupplierDaoImpl.getDao();
		actionButton.setVisible(false);
		updateActionButton.setVisible(false);
		initSupTable();
		supCodeField.setEditable(false);
		addImageButton.setDisable(true);
		removeImages();
		validateEachField();

	}

	private void initSupTable() {
		supCodeCol.setCellValueFactory(new PropertyValueFactory<>("sup_id"));
		supNameCol.setCellValueFactory(new PropertyValueFactory<>("com_name"));
		cityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
		addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
		phoneCol.setCellValueFactory(new PropertyValueFactory<>("mobile_no"));
		supCreateDateCol.setCellValueFactory(new PropertyValueFactory<>("payment_period"));

	}

	@FXML
	void actionButtonClicked(ActionEvent event) {

		if (actionButton.getText().equals("SaveNew") && ValidateInputs.validateInputEmpty(compNameFiled , "Company Name Field")
				&& ValidateInputs.validateUserNames(supNameField , "First Name Field")
				&& ValidateInputs.validateUserNames(lastNameField , "Last Name Field")
				&& ValidateInputs.validateInputEmpty(desigField , "Designation Field")
				&& ValidateInputs.validateInputEmpty(stretAddressField , "Street Address Field")
				& ValidateInputs.validateInputEmpty(addressField , "Address Field")
				&& ValidateInputs.validateInputEmpty(cityField , "City Field")
				&& ValidateInputs.validateEmail(emailField , "Email Field")
				&& ValidateInputs.validateTelphoneNumbers(mobNumField , "Mobile Number Field")
				&& ValidateInputs.validateTelphoneNumbers(telephoneNumField ,"Telephone Number Field")
				&& ValidateInputs.validateNumbers(payPeriodField, "Payment Period Field")) {

			Supplier newSupplier = new Supplier(compNameFiled.getText(), supNameField.getText(),
					lastNameField.getText(), desigField.getText(), stretAddressField.getText(), addressField.getText(),
					cityField.getText(), emailField.getText(), Integer.parseInt(mobNumField.getText()),
					Integer.parseInt(telephoneNumField.getText()), Integer.parseInt(payPeriodField.getText()),
					brNumberField.getText());
			
			Set<SupplierBrImages> imagesSet = new HashSet<SupplierBrImages>();
			
			for(Entry<ImageView, byte[]> x : mapForImage.entrySet()) {
				SupplierBrImages brImages = new SupplierBrImages();
				brImages.setSupplier(newSupplier);
				brImages.setImage(BlobProxy.generateProxy(x.getValue()));
				imagesSet.add(brImages);
			}
			
			newSupplier.setSupplierBrImages(imagesSet);
			try {

				if (AlertHandler.getAlert(AlertType.CONFIRMATION, "Are your sure you want create this Supplier", null)
						.getResult().getButtonData().equals(ButtonData.OK_DONE)) {
					if (supplierDao.saveSupplier(newSupplier)) {
						Notifications.create().title("Save Success").graphic(null).hideAfter(Duration.seconds(1))
								.darkStyle().position(Pos.CENTER).show();
						clearDataClicked();

					}

				}

			} catch (Exception e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Cannot Insert Data");
				alert.setContentText(e.getMessage());

			}

		} else

		{
			System.out.println("Action Error");
		}
	}

	@FXML
	void clearDataClicked() {
		updateActionButton.setVisible(false);
		supCodeField.clear();
		compNameFiled.clear();
		supNameField.clear();
		emailField.clear();
		addressField.clear();
		stretAddressField.clear();
		mobNumField.clear();
		telephoneNumField.clear();
		desigField.clear();
		payPeriodField.clear();
		lastNameField.clear();
		cityField.clear();
		actionButton.setVisible(false);
		brNumberField.clear();
		fileChooserHbox.getChildren().clear();
		removeTextFieldCss();
		addOriginalCss();
		textFieldDisable();
		refreshAll();
		mapForImage.clear();
		deletedImageView= null;
		updatedwithImageStatus =false;

	}

	void clearDataClickedForCreateNew() {
		supCodeField.clear();
		compNameFiled.clear();
		supNameField.clear();
		emailField.clear();
		addressField.clear();
		stretAddressField.clear();
		mobNumField.clear();
		telephoneNumField.clear();
		desigField.clear();
		payPeriodField.clear();
		lastNameField.clear();
		cityField.clear();
		brNumberField.clear();
		actionButton.setVisible(false);
		fileChooserHbox.getChildren().clear();
		refreshAll();

	}

	private void textFieldEditable() {
		// supCodeField.setEditable(true);
		compNameFiled.setEditable(true);
		supNameField.setEditable(true);
		emailField.setEditable(true);
		addressField.setEditable(true);
		stretAddressField.setEditable(true);
		mobNumField.setEditable(true);
		telephoneNumField.setEditable(true);
		desigField.setEditable(true);
		payPeriodField.setEditable(true);
		lastNameField.setEditable(true);
		cityField.setEditable(true);
		addImageButton.setDisable(false);
		brNumberField.setEditable(true);

	}

	private void textFieldDisable() {
		supCodeField.setEditable(false);
		compNameFiled.setEditable(false);
		supNameField.setEditable(false);
		emailField.setEditable(false);
		addressField.setEditable(false);
		stretAddressField.setEditable(false);
		mobNumField.setEditable(false);
		telephoneNumField.setEditable(false);
		desigField.setEditable(false);
		payPeriodField.setEditable(false);
		lastNameField.setEditable(false);
		cityField.setEditable(false);
		addImageButton.setDisable(true);
		brNumberField.setEditable(false);

	}

	private void textFieldEditableUpdate() {
		supCodeField.setEditable(false);
		compNameFiled.setEditable(true);
		supNameField.setEditable(true);
		emailField.setEditable(true);
		addressField.setEditable(true);
		stretAddressField.setEditable(true);
		mobNumField.setEditable(true);
		telephoneNumField.setEditable(true);
		desigField.setEditable(true);
		payPeriodField.setEditable(true);
		lastNameField.setEditable(true);
		cityField.setEditable(true);
		addImageButton.setDisable(false);
		brNumberField.setEditable(true);
	}

	private void addTextFieldCss() {
		supCodeField.getStyleClass().add("itext-field");
		compNameFiled.getStyleClass().add("itext-field");
		supNameField.getStyleClass().add("itext-field");
		emailField.getStyleClass().add("itext-field");
		addressField.getStyleClass().add("itext-field");
		stretAddressField.getStyleClass().add("itext-field");
		mobNumField.getStyleClass().add("itext-field");
		telephoneNumField.getStyleClass().add("itext-field");
		desigField.getStyleClass().add("itext-field");
		payPeriodField.getStyleClass().add("itext-field");
		lastNameField.getStyleClass().add("itext-field");
		cityField.getStyleClass().add("itext-field");
		brNumberField.getStyleClass().add("itext-field");
	}

	private void addOriginalCss() {
		supCodeField.getStyleClass().add("set-not-editable-text");
		compNameFiled.getStyleClass().add("set-not-editable-text");
		supNameField.getStyleClass().add("set-not-editable-text");
		emailField.getStyleClass().add("set-not-editable-text");
		addressField.getStyleClass().add("set-not-editable-text");
		stretAddressField.getStyleClass().add("set-not-editable-text");
		mobNumField.getStyleClass().add("set-not-editable-text");
		telephoneNumField.getStyleClass().add("set-not-editable-text");
		desigField.getStyleClass().add("set-not-editable-text");
		payPeriodField.getStyleClass().add("set-not-editable-text");
		lastNameField.getStyleClass().add("set-not-editable-text");
		cityField.getStyleClass().add("set-not-editable-text");
		brNumberField.getStyleClass().add("set-not-editable-text");
	}

	private void removeTextFieldCss() {
		// supCodeField.getStyleClass().removeAll("set-not-editable-text",
		// "danger-for-warning", "itext-field");
		compNameFiled.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		supNameField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		emailField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		addressField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		stretAddressField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		mobNumField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		telephoneNumField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		desigField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		payPeriodField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		lastNameField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		cityField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		brNumberField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
	}

	@FXML
	void createNewButton(ActionEvent event) {
		clearDataClickedForCreateNew();
		removeTextFieldCss();
		addTextFieldCss();

		textFieldEditable();
		actionButton.setText("SaveNew");
		updateActionButton.setVisible(false);
		actionButton.setVisible(true);

	}

	@FXML
	void deleteButton(ActionEvent event) {

		if (supplierInstance != null) {
			if (AlertHandler.getAlert(AlertType.CONFIRMATION, "Are You Sure you want to Delete this Supplier",
					supplierInstance.getCom_name()).getResult().getButtonData().equals(ButtonData.OK_DONE)) {

				if (supplierDao.deleteSupplier(supplierInstance) == supplierInstance.getSup_id()) {
					Notifications.create().title("Delete Success").graphic(null).hideAfter(Duration.seconds(1))
							.darkStyle().position(Pos.CENTER).show();
					clearDataClicked();
					updateActionButton.setVisible(false);
					actionButton.setVisible(false);

				}
			}
		}
	}

	@FXML
	void keyEventHandling(KeyEvent event) {

	}

	ObservableList<Supplier> listofSupplier = FXCollections.observableArrayList();

	@FXML
	void loadSuppllierButton(ActionEvent event) {
		if (listofSupplier.isEmpty()) {
			listofSupplier.addAll(SupplierDaoImpl.getDao().getSuppliers());
		}

		supplierTableView.setItems(listofSupplier);
	}

	@FXML
	void qtyCellClicke(ActionEvent event) {

	}

	@FXML
	void searchItems(KeyEvent event) {

	}

	@FXML
	void updateButton(ActionEvent event) {
		if (!supCodeField.getText().isEmpty() && !actionButton.isVisible()) {
			removeTextFieldCss();
			addTextFieldCss();
			textFieldEditableUpdate();
			updateActionButton.setVisible(true);
			mapForImage.clear();
			fileChooserHbox.getChildren().clear();
			updatedwithImageStatus = false;
			
			if(AlertHandler.getAlert(AlertType.CONFIRMATION, "Do You Want to Update the Images ? ", 
					"If you select OK image files will be loaded").getResult().getButtonData().equals(ButtonData.OK_DONE)) {
				for (SupplierBrImages x : supplierDao.getImages(supplierInstance)) {
					try {
						InputStream inputStream = x.getImage().getBinaryStream();
						File file = new File(x.getImageId()+".jpg");
						FileOutputStream outputStream = new FileOutputStream(file);
						int k;
						while((k=inputStream.read()) !=-1) {
							outputStream.write(k);
						}
						outputStream.close();
						System.out.println(file.getCanonicalPath());
						Image image = new Image(file.toURI().toString(), 146, 162, true, true);
						ImageView imageView = new ImageView(image);
						imageView.setFitWidth(146);
						imageView.setFitHeight(162);
						imageView.setPreserveRatio(true);
						fileChooserHbox.getChildren().add(imageView);
						int length = (int) file.length();
						byte byteArray[] = new byte[length];
						FileInputStream fileInputStream = new FileInputStream(file);
						fileInputStream.read(byteArray);
						mapForImage.put(imageView, byteArray);
						fileInputStream.close();
						file.delete();
						imageView.setOnDragDetected(e -> {
							Dragboard dragBoard = imageView.startDragAndDrop(TransferMode.ANY);
							ClipboardContent content = new ClipboardContent();
							content.putImage(imageView.getImage());
							dragBoard.setContent(content);
							e.consume();
							deletedImageView = imageView;

						});
						
						
					} catch (Exception e) {
						
						e.printStackTrace();
					}
				}
				
				updatedwithImageStatus = true;
			}
			
		} else {
			AlertHandler.getAlert(AlertType.ERROR, "Please Selec the Supplier", null);
		}

	}

	@FXML
	void updateActionButtonClicked(ActionEvent event) {
		if (actionButton.getText().equals("SaveNew") && ValidateInputs.validateNumbers(supCodeField , "Supplier Code Field")
				&& ValidateInputs.validateInputEmpty(compNameFiled, "Company Name Field") 
				&& ValidateInputs.validateUserNames(supNameField, "First Name Field")
				&& ValidateInputs.validateUserNames(lastNameField, "Last Name Field") 
				&& ValidateInputs.validateInputEmpty(desigField ,"Designation Field")
				&& ValidateInputs.validateInputEmpty(stretAddressField, "Street Address Field")
				&& ValidateInputs.validateInputEmpty(addressField , "Address Field") 
				&& ValidateInputs.validateInputEmpty(cityField, "City Field")
				&& ValidateInputs.validateEmail(emailField , "Email Field") 
				&& ValidateInputs.validateTelphoneNumbers(mobNumField , "Mobile Number Field")
				&& ValidateInputs.validateTelphoneNumbers(telephoneNumField , "Telephone Number Field")
				&& ValidateInputs.validateNumbers(payPeriodField , "Payment Period Field")) {
			Supplier updateSupplier = new Supplier(Integer.parseInt(supCodeField.getText()), compNameFiled.getText(),
					supNameField.getText(), lastNameField.getText(), desigField.getText(), stretAddressField.getText(),
					addressField.getText(), cityField.getText(), emailField.getText(),
					Integer.parseInt(mobNumField.getText()), Integer.parseInt(telephoneNumField.getText()),
					Integer.parseInt(payPeriodField.getText()), brNumberField.getText());
			
	Set<SupplierBrImages> imagesSet = new HashSet<SupplierBrImages>();
			
			for(Entry<ImageView, byte[]> x : mapForImage.entrySet()) {
				SupplierBrImages brImages = new SupplierBrImages();
				brImages.setSupplier(updateSupplier);
				brImages.setImage(BlobProxy.generateProxy(x.getValue()));
				imagesSet.add(brImages);
			}
			
			updateSupplier.setSupplierBrImages(imagesSet);
			
			
			try {

				if (AlertHandler.getAlert(AlertType.CONFIRMATION, "Are your sure you want Update this Supplier", null)
						.getResult().getButtonData().equals(ButtonData.OK_DONE)) {
					
					if(updatedwithImageStatus) {
						if (supplierDao.updateSupplier(updateSupplier)) {
							Notifications.create().title("Update Success").graphic(null).hideAfter(Duration.seconds(1))
									.darkStyle().position(Pos.CENTER).show();
							clearDataClicked();

						}
					} else {
						if (supplierDao.updateSupplierTextOnly(updateSupplier)) {
							Notifications.create().title("Update Success").graphic(null).hideAfter(Duration.seconds(1))
									.darkStyle().position(Pos.CENTER).show();
							clearDataClicked();

						}
					}
					

				}

			} catch (Exception e) {
				AlertHandler.getAlert(AlertType.ERROR, "Cannot Update data", e.toString());
			}
		}

	}

	// this will set supplier code to the supCodefield from supplier table
	public void setTextFieldFromSupplier(String x) {
		viewSupCodeField.setText(x);
	}

	@FXML
	public void viewInfoClicked() {
		try {
			if (actionButton.isVisible() || updateActionButton.isVisible()) {
				
				updateActionButton.setVisible(false);
				removeTextFieldCss();
				addOriginalCss();
				textFieldDisable();
				actionButton.setVisible(false);
			}
			clearDataClicked();

			supplierInstance = SupplierDaoImpl.getDao().getSupplierByIdWithImages(Integer.parseInt(viewSupCodeField.getText()));

			supCodeField.setText(String.valueOf(supplierInstance.getSup_id()));
			supNameField.setText(supplierInstance.getContact_fname());
			compNameFiled.setText(String.valueOf(supplierInstance.getCom_name()));
			lastNameField.setText(String.valueOf(supplierInstance.getContact_lname()));
			emailField.setText(String.valueOf(supplierInstance.getEmail()));
			addressField.setText(String.valueOf(supplierInstance.getAddressLine01()));
			stretAddressField.setText(String.valueOf(supplierInstance.getStreetAddress()));
			mobNumField.setText("0"+String.valueOf(supplierInstance.getMobile_no()));
			telephoneNumField.setText("0"+String.valueOf(supplierInstance.getTelephone()));
			desigField.setText(String.valueOf(supplierInstance.getContact_person_desig()));
			payPeriodField.setText(String.valueOf(supplierInstance.getPayment_period()));
			cityField.setText(String.valueOf(supplierInstance.getCity()));
			brNumberField.setText(String.valueOf(supplierInstance.getBrNumber()));
			
			System.out.println(supplierInstance.getSupplierBrImages());

		} catch (

		Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Supplier Cannot Find", e.toString());
			e.printStackTrace();
		}

	}

	// setting this main loader
	public FXMLLoader getThisMainLoader() {
		return thisMainLoader;
	}

	// getting this main loader

	public void setThisMainLoader(FXMLLoader thisMainLoader) {
		this.thisMainLoader = thisMainLoader;
	}

	// load supplier table view
	@FXML
	void loadSupplierAction(ActionEvent event) throws IOException {
		if (supplierStage == null) {
			String loc = "/fxml/supplier/SupplierTable.fxml";
			FXMLLoader loader = new FXMLLoader(getClass().getResource(loc));

			Scene scene = new Scene(loader.load());
			GetSupplierController getControl = loader.getController();

			supplierStage = new Stage(StageStyle.DECORATED);
			supplierStage.initModality(Modality.APPLICATION_MODAL);
			supplierStage.setScene(scene);
			supplierStage.show();
			getControl.setLoader(thisMainLoader);

		} else if (supplierStage.isShowing()) {
			supplierStage.toFront();

		} else {
			supplierStage.show();
		}
	}

	// image choser function for
	@FXML
	public void imageChooser() {

		FileChooser imageChooser = new FileChooser();
		imageChooser.setTitle("Please Select Image");
		Window stage = actionButton.getScene().getWindow();

		FileChooser.ExtensionFilter filter = new ExtensionFilter("Image File", "*.jpg");
		FileChooser.ExtensionFilter filter2 = new ExtensionFilter("Image File", "*.png");
		imageChooser.getExtensionFilters().addAll(filter, filter2);

		File file = imageChooser.showOpenDialog(stage);

		if (file != null) {
			Image image = new Image(file.toURI().toString(), 146, 162, true, true);

			ImageView imageView = new ImageView();
			imageView.setImage(image);
			imageView.setDisable(false);
			imageView.setPickOnBounds(true);
			imageView.setSmooth(true);

			imageView.setFitWidth(146);
			imageView.setFitHeight(162);
			imageView.setPreserveRatio(true);

			fileChooserHbox.getChildren().add(imageView);

			try {

				int length = (int) file.length();
				byte byteArray[] = new byte[length];
				FileInputStream fileInputStream = new FileInputStream(file);
				fileInputStream.read(byteArray);
				mapForImage.put(imageView, byteArray);
				// SupplierBrImages images = new SupplierBrImages();
				// images.setImage(BlobProxy.generateProxy(byteArray));
				// brImageFileSet.add(images);
				fileInputStream.close();

				imageView.setOnDragDetected(e -> {
					Dragboard dragBoard = imageView.startDragAndDrop(TransferMode.ANY);
					ClipboardContent content = new ClipboardContent();
					content.putImage(imageView.getImage());
					dragBoard.setContent(content);
					e.consume();
					deletedImageView = imageView;

				});

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	// Remove Each and every Image View will be handled by this method while
	// reffering to setOnDragDetected Method in ImageFileChooser method
	private void removeImages() {

		recycleBinImageView.setOnDragDropped(e -> {
			Dragboard db = e.getDragboard();
			boolean success = false;
			if (db.hasImage()) {
				mapForImage.remove(deletedImageView);
				fileChooserHbox.getChildren().remove(deletedImageView);
				success = true;
			}
			e.setDropCompleted(success);
			e.consume();
		});

		recycleBinImageView.setOnDragOver(d -> {
			if (d.getDragboard().hasImage()) {
				d.acceptTransferModes(TransferMode.COPY_OR_MOVE);
							}
			d.consume();

		});

		deletedImageView = null;
	}

	// refresh all data
	private void refreshAll() {
		listofSupplier.clear();
		supplierStage = null;
		supplierTableView.getItems().clear();
		supplierInstance = null;
		mapForImage.clear();
		deletedImageView = null;
		updatedwithImageStatus =false;
	}
	
	//method of realtime validation of Supplier registration form on key pressed on textfield
	
	private void validateEachField() {
		
		
		compNameFiled.setOnKeyReleased(e->{
			ValidateInputEach.validateInputEmpty(compNameFiled, errorMessegeLabel);
		});
		
		supNameField.setOnKeyReleased(e->{
			ValidateInputEach.validateInputEmpty(supNameField, errorMessegeLabel);
		});
		
		lastNameField.setOnKeyReleased(e->{
			ValidateInputEach.validateUserNames(lastNameField, errorMessegeLabel);
		});
		
		emailField.setOnKeyReleased(e->{
			ValidateInputEach.validateEmail(emailField, errorMessegeLabel);
		});
		
		addressField.setOnKeyReleased(e->{
			ValidateInputEach.validateInputEmpty(addressField, errorMessegeLabel);
		});
		
		stretAddressField.setOnKeyReleased(e->{
			ValidateInputEach.validateInputEmpty(stretAddressField, errorMessegeLabel);
		});
		
		mobNumField.setOnKeyReleased(e->{
			ValidateInputEach.validateMobileNumbers(mobNumField, errorMessegeLabel);
		});
		
		telephoneNumField.setOnKeyReleased(e->{
			ValidateInputEach.validateMobileNumbers(telephoneNumField, errorMessegeLabel);
		});
		
		desigField.setOnKeyReleased(e->{
			ValidateInputEach.validateInputEmpty(desigField, errorMessegeLabel);
		});
		
		cityField.setOnKeyReleased(e->{
			ValidateInputEach.validateInputEmpty(cityField, errorMessegeLabel);
		});
		
		payPeriodField.setOnKeyReleased(e->{
			ValidateInputEach.validateNumbers(payPeriodField, errorMessegeLabel);
		});
		
		
		
	}

}
