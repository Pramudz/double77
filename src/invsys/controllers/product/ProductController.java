package invsys.controllers.product;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Predicate;

import org.controlsfx.control.Notifications;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.controllers.formvalidation.ValidateInputEach;
import invsys.controllers.formvalidation.ValidateInputs;
import invsys.controllers.mainpage.MainController;
import invsys.entities.Category;
import invsys.entities.Products;
import invsys.entities.Supplier;
import invsys.entitiydao.CategoryDao;
import invsys.entitiydao.ProductDao;
import invsys.entitiydao.SupplierDao;
import invsys.entitiydao.impl.CategoryDaoImpl;
import invsys.entitiydao.impl.ProductDaoImpl;
import invsys.entitiydao.impl.SupplierDaoImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class ProductController implements Initializable {

	@FXML
	private JFXTextField viewItemField;

	@FXML
	private TextField pluCodeField;

	@FXML
	private TextField prdNameField;

	@FXML
	private JFXComboBox<String> prdCatField;

	@FXML
	private JFXComboBox<String> prdSupField;
	
	@FXML
	private JFXComboBox<String> unitOfMeasureCombo;

	@FXML
	private TextField prdPackSizeField;

	@FXML
	private TextField prdPackPriceField;

	@FXML
	private TextField prdPackCostField;

	@FXML
	private TextField prdUnitPriceField;

	@FXML
	private TextField prdReOrderLField;

	@FXML
	private TextField prdCostPriceField;

	@FXML
	private TextField prdDiscountField;

	@FXML
	private TextField prdVatField;
	
	 @FXML
	 private TextField onhQtyField;

	@FXML
	private CheckBox prdValdityCheckBox;

	@FXML
	private JFXTextField thisSearchTextField;

	@FXML
	private TableView<Products> itemTableView;

	@FXML
	private TableColumn<Products, Integer> colPluCode;

	@FXML
	private TableColumn<Products, String> colPrdDesc;

	@FXML
	private TableColumn<Products, Double> colOhQty;

	@FXML
	private TableColumn<Products, Double> colUnitPrice;

	@FXML
	private TableColumn<Products, Double> colUnitCost;

	@FXML
	private TableColumn<Products, Integer> colPackPrice;

	@FXML
	private TableColumn<Products, Double> colPackSize;

	@FXML
	private TableColumn<Products, Double> colDiscount;

	@FXML
	private TableColumn<Products, Integer> colReorderLevel;

	@FXML
	private TableColumn<Products, Double> colVat;

	@FXML
	private TableColumn<Products, String> colSuppler;

	@FXML
	private TableColumn<Products, String> colCategory;
	@FXML
	private TableColumn<Products, String> colPrdName;

	@FXML
	private JFXButton actionButton;
	
	
	@FXML
	private JFXButton createButton;
	
	@FXML
	private JFXButton updateButt;
	
	@FXML
	private JFXButton delButton;

	@FXML
	private JFXButton critSearchButton;

	@FXML
	private JFXButton updateActionButton;

	// created only for clear tableview data
	@FXML
	private JFXButton clearButton;

	//productList for TableView mapping
	private ObservableList<Products> productList = FXCollections.observableArrayList();
	//filteredListforSearchOperation
	private FilteredList<Products> filteredListForSearch = new FilteredList<>(productList, e -> true);
	SortedList<Products> sList = new SortedList<Products>(filteredListForSearch);
	
	// inorder track view info clicked & delete items
	private Products productInstance;

	HashMap<String, Object> supplierMap = new HashMap();
	HashMap<String, Long> categoryMap = new HashMap();

	// In order avoid duplicate updating bindautocomletion text as
	// "TextFields.bindAutoCompletion(prdCatField.getEditor(),
	// prdCatField.getItems());"
	Set<String> suggestionsCatHashSet = new HashSet<String>();
	SuggestionProvider<String> suggesionProviderCat = SuggestionProvider.create(suggestionsCatHashSet);
	AutoCompletionTextFieldBinding<String> textFieldsAutoCompletCategory;

	// In order avoid duplicate updating bindautocomletion text as
	// "TextFields.bindAutoCompletion(prdCatField.getEditor(),
	// prdCatField.getItems());"
	Set<String> suggestionsSupHashSet = new HashSet<String>();
	SuggestionProvider<String> suggesionProviderSup = SuggestionProvider.create(suggestionsSupHashSet);
	AutoCompletionTextFieldBinding<String> textFieldsAutoCompletSup;
	
	//category dao for data accessing
	CategoryDao categoryDao = null;
	ProductDao productDao = null;
	SupplierDao supplierDao = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		if(!MainController.isDashbardManager()) {
			createButton.setDisable(true);
			updateButt.setDisable(true);
			delButton.setDisable(true);
		}
		productDao = ProductDaoImpl.getDao();
		categoryDao = CategoryDaoImpl.getDao();
		supplierDao = SupplierDaoImpl.getDao();
		
		prdCostPriceField.setDisable(true);
		prdUnitPriceField.setDisable(true);
		prdPackPriceField.setDisable(true);
		prdPackCostField.setDisable(true);
		
		updateSuppliersAndCategoryies();
		ValidateInputs.getInstance();
		ValidateInputEach.getInstance();
		searchItems();

		actionButton.setVisible(false);
		critSearchButton.setVisible(false);
		updateActionButton.setVisible(false);
		prdValdityCheckBox.setSelected(false);
		initColumn();
		clearButton.setOnAction(e -> {
			productList.clear();
			itemTableView.getItems().clear();
			clearDataClickedWithTableView();
		});

		
		updatPackPricesAuto();
		
		unitOfMeasureCombo.getItems().add("unit");
		unitOfMeasureCombo.getItems().add("kg");
		unitOfMeasureCombo.getItems().add("litre");
		unitOfMeasureCombo.getItems().add("Meters");
		unitOfMeasureCombo.getSelectionModel().selectFirst();
	}

// whenever updates happens refreshing will be done as well as initilaization	
	@FXML
	private void updateSuppliersAndCategoryies() {
		categoryMap.clear();
		suggestionsCatHashSet.clear();
		supplierMap.clear();
		suggestionsSupHashSet.clear();
		if (textFieldsAutoCompletSup != null)
			textFieldsAutoCompletSup.dispose();
		if (textFieldsAutoCompletCategory != null)
			textFieldsAutoCompletCategory.dispose();
		suggesionProviderCat.clearSuggestions();
		suggesionProviderSup.clearSuggestions();
		prdCatField.getItems().clear();
		prdSupField.getItems().clear();

		
		categoryDao = CategoryDaoImpl.getDao();
		
		for (Object[] getCat : categoryDao.getCatIdAndName()) {
			categoryMap.put(getCat[0].toString(), (Long) getCat[1]);
			suggestionsCatHashSet.add(getCat[0].toString());

		}

		for (Object[] getSup : supplierDao.getSupIdAndName()) {
			supplierMap.put(getSup[0].toString(), getSup[1]);
			suggestionsSupHashSet.add(getSup[0].toString());
		}

		prdCatField.getItems().addAll(suggestionsCatHashSet);
		prdSupField.getItems().addAll(suggestionsSupHashSet);

		suggesionProviderSup.addPossibleSuggestions(suggestionsSupHashSet);
		textFieldsAutoCompletSup = new AutoCompletionTextFieldBinding<>(prdSupField.getEditor(), suggesionProviderSup);
		suggesionProviderCat.addPossibleSuggestions(suggestionsCatHashSet);
		textFieldsAutoCompletCategory = new AutoCompletionTextFieldBinding<>(prdCatField.getEditor(),
				suggesionProviderCat);
	}

	// coulmn initialization for Table View of Product table
	private void initColumn() {
		colPluCode.setCellValueFactory(new PropertyValueFactory<>("prd_id"));
		//colPrdDesc.setCellValueFactory(new PropertyValueFactory<>("p_desc"));
		colPrdName.setCellValueFactory(new PropertyValueFactory<>("p_name"));
		colCategory.setCellValueFactory(
				(TableColumn.CellDataFeatures<Products, String> catData) -> new SimpleStringProperty(
						catData.getValue().getCategory().getCategoryName()));
		colSuppler.setCellValueFactory(
				(TableColumn.CellDataFeatures<Products, String> supData) -> new SimpleStringProperty(
						supData.getValue().getSupplier().getCom_name()));
		colPackSize.setCellValueFactory(new PropertyValueFactory<>("pack_size"));
		colPackPrice.setCellValueFactory(new PropertyValueFactory<>("pack_price"));
		colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unit_price"));
		colUnitCost.setCellValueFactory(new PropertyValueFactory<>("unit_cost_price"));
		colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
		colReorderLevel.setCellValueFactory(new PropertyValueFactory<>("reOrderLevel"));
		colVat.setCellValueFactory(new PropertyValueFactory<>("vat"));
		colOhQty.setCellValueFactory(new PropertyValueFactory<>("onHandQty"));
	}

	@FXML
	void clearDataClicked() {
		pluCodeField.clear();
		prdNameField.clear();
		prdCatField.setValue("");
		prdSupField.setValue("");
		prdPackSizeField.clear();
		prdPackPriceField.clear();
		prdPackCostField.clear();
		prdUnitPriceField.clear();
		prdCostPriceField.clear();
		prdDiscountField.clear();
		prdVatField.clear();
		onhQtyField.clear();
		prdReOrderLField.clear();
		prdValdityCheckBox.setSelected(false);
		textFieldNotEditable();
		actionButton.setVisible(false);
		critSearchButton.setVisible(false);
		updateActionButton.setVisible(false);
		productInstance = null;
		hboxPagination.getChildren().clear();
		pageResult = 50;
		firstResult = 0;
		maxRecord = 0;
		nextResult = pageResult;
		prevResult = 0;
		unitOfMeasureCombo.getSelectionModel().clearAndSelect(0);
		
	}
	
	@FXML
	void clearDataClickedWithTableView() {
		pluCodeField.clear();
		prdNameField.clear();
		prdCatField.setValue("");
		prdSupField.setValue("");
		prdPackSizeField.clear();
		prdPackPriceField.clear();
		prdPackCostField.clear();
		prdUnitPriceField.clear();
		prdCostPriceField.clear();
		prdDiscountField.clear();
		prdVatField.clear();
		onhQtyField.clear();
		prdReOrderLField.clear();
		prdValdityCheckBox.setSelected(false);
		textFieldNotEditable();
		actionButton.setVisible(false);
		critSearchButton.setVisible(false);
		updateActionButton.setVisible(false);
		productInstance = null;
		hboxPagination.getChildren().clear();
		pageResult = 50;
		firstResult = 0;
		maxRecord = 0;
		nextResult = pageResult;
		prevResult = 0;
		filteredListForSearch.clear();
		sList.clear();
		unitOfMeasureCombo.getSelectionModel().clearAndSelect(0);

	}

	
	//set text field editble without unit price and cost price fields/
	@FXML
	void setTextFieldEditable() {
		pluCodeField.setDisable(true);
		
		prdNameField.setDisable(false);
		prdCatField.setDisable(false);
		prdSupField.setDisable(false);
		prdPackSizeField.setDisable(false);
		prdPackPriceField.setDisable(false);
		prdPackCostField.setDisable(false);
		
		prdUnitPriceField.setDisable(true);
		prdCostPriceField.setDisable(true);
		
		prdDiscountField.setDisable(false);
		prdVatField.setDisable(false);
		prdReOrderLField.setDisable(false);
		prdValdityCheckBox.setDisable(false);
		
		onhQtyField.setDisable(true);
		
		unitOfMeasureCombo.setDisable(false);
		removeTextFieldCss();
		addTextFieldCss();
	}

	
	//provide accessible text fields for Product update
	@FXML
	void setUpdateEditable() {
		pluCodeField.setDisable(true);
		
		prdNameField.setDisable(false);
		String tempVal = prdCatField.getValue();
		String tempSVal = prdSupField.getValue();
		prdCatField.setDisable(false);
		prdSupField.setDisable(false);
		prdCatField.setValue(tempVal);
		prdSupField.setValue(tempSVal);	
		prdPackSizeField.setDisable(false);
		
		prdPackPriceField.setDisable(true);
		prdPackCostField.setDisable(true);
		prdUnitPriceField.setDisable(true);
		prdCostPriceField.setDisable(true);
		
		prdDiscountField.setDisable(false);
		prdVatField.setDisable(false);
		prdReOrderLField.setDisable(false);
		prdValdityCheckBox.setDisable(false);
		
		onhQtyField.setDisable(true);
		
		unitOfMeasureCombo.setDisable(false);
		removeTextFieldCss();
		addTextFieldCss();
	}

	
	//to Disable all text fields 
	@FXML
	void textFieldNotEditable() {
		pluCodeField.setDisable(true);
		prdNameField.setDisable(true);
		prdCatField.setDisable(true);
		prdSupField.setDisable(true);
		prdPackSizeField.setDisable(true);
		prdPackPriceField.setDisable(true);
		prdPackCostField.setDisable(true);
		prdUnitPriceField.setDisable(true);
		prdCostPriceField.setDisable(true);
		prdDiscountField.setDisable(true);
		prdVatField.setDisable(true);
		prdReOrderLField.setDisable(true);
		prdValdityCheckBox.setDisable(true);
		onhQtyField.setDisable(true);
		unitOfMeasureCombo.setDisable(true);
		
		removeTextFieldCss();
		addOriginalCss();
	}

	
	//when the create new button is clicked 
	@FXML
	void createNewButton(ActionEvent event) {
		pluCodeField.setDisable(true);
		clearDataClicked();
		setTextFieldEditable();
		updateActionButton.setVisible(false);
		actionButton.setVisible(true);
	}
	
	
	//update product unit price and unit cost as per the pack size prices
	private void updatPackPricesAuto() {
    	prdPackPriceField.setOnKeyReleased(e->{
    		
    		if(actionButton.isVisible()) {
    		try {
    			
    			if(ValidateInputs.validateNumbers(prdPackSizeField, "Pack Size Field")) {
    	
    			if(actionButton.isVisible() && e.getCode().isDigitKey()) {
    				double updatePrice = Double.parseDouble(prdPackPriceField.getText());
    				double prdPackSize = Double.parseDouble(prdPackSizeField.getText());
    				double newPackPrice = updatePrice / prdPackSize;
    				prdUnitPriceField.setText(String.format("%.2f",newPackPrice));
    				
    		} else if(actionButton.isVisible() && (e.getCode().equals(KeyCode.BACK_SPACE) || e.getCode().equals(KeyCode.DELETE))) {
    				prdPackPriceField.clear();
    				prdUnitPriceField.clear();
    		}
    			
    		}
    		
    		else {
    			prdPackPriceField.clear();
				prdUnitPriceField.clear();
				
    			}
    		}catch(Exception ex) {
    			AlertHandler.getAlert(AlertType.ERROR, "Somthing Went Wrong In Prices", null);
    		}
    		}
    	});
    	
    	prdPackCostField.setOnKeyReleased(e->{
    		
    		if(actionButton.isVisible()) {
    		
    		try {
    			if(ValidateInputs.validateNumbers(prdPackSizeField, "Pack Size Field")) {
    			if(actionButton.isVisible() && e.getCode().isDigitKey()) {
    				double updatePrice = Double.parseDouble(prdPackCostField.getText());
    				double prdPackSize = Double.parseDouble(prdPackSizeField.getText());
    				double newPackPrice = updatePrice / prdPackSize;
    				prdCostPriceField.setText(String.format("%.2f",newPackPrice));
    				
    		} else if(actionButton.isVisible() && (e.getCode().equals(KeyCode.BACK_SPACE) || e.getCode().equals(KeyCode.DELETE))) {
    				prdPackCostField.clear();
    				prdCostPriceField.clear();
    		}
    			}
    			
    			else {
    				prdPackCostField.clear();
    				prdCostPriceField.clear();
    				
        			}
    		}catch(Exception ex) {
    			AlertHandler.getAlert(AlertType.ERROR, "Somthing Went Wrong In Prices", null);
    		}
    	}
    	});
    }

	@FXML
	void deleteButton(ActionEvent event) {
		if (productInstance != null) {
			if (AlertHandler.getAlert(AlertType.CONFIRMATION, "Are You Sure You want to delete this Product",
					productInstance.getP_name()).getResult().getButtonData().equals(ButtonData.OK_DONE)) {
				if (productDao.deletProduct(productInstance) == productInstance.getPrd_id()) {
					Notifications.create().title("Delete Success").graphic(null).hideAfter(Duration.seconds(1))
							.darkStyle().position(Pos.CENTER).show();
					clearDataClicked();
					updateActionButton.setVisible(false);
					actionButton.setVisible(false);
				}
			}
		} else {
			AlertHandler.getAlert(AlertType.ERROR, "No Product loaded to delete", null);
		}
	}

	@FXML
	void keyEventHandling(KeyEvent event) {

	}

	

	@FXML
	void loadItems() {
		productList.clear();
		itemTableView.getItems().clear();
		if (productList.isEmpty()) {
			paginationWithTableViewForAllResult();
		}
		

	}

	@FXML
	void qtyCellClicke(ActionEvent event) {

	}

	@FXML
	void saveButtonClicked(ActionEvent event) {
		if (validateProductForm()) {
			System.out.println("Validated");

			try {
				Supplier thisSuppler = supplierDao.getSupplierById((Integer) supplierMap.get(prdSupField.getValue()));
				Category thisCategory = categoryDao.getCategoryById((categoryMap.get(prdCatField.getValue())));
				Products saveProduct = new Products(prdNameField.getText().concat("-").concat(String.valueOf(thisSuppler.getSup_id())), thisCategory, thisSuppler,
						Double.parseDouble(prdPackSizeField.getText()), Double.parseDouble(prdPackPriceField.getText()),
						Double.parseDouble(prdPackCostField.getText()), Double.parseDouble(prdUnitPriceField.getText()),
						Double.parseDouble(prdCostPriceField.getText()), Double.parseDouble(prdDiscountField.getText()),
						Double.parseDouble(prdReOrderLField.getText()), Double.parseDouble(prdVatField.getText()),
						prdValdityCheckBox.isSelected(), 0);
				saveProduct.setUnitAverageCost(Double.parseDouble(prdCostPriceField.getText()));
				saveProduct.setUnitOfMeasure(unitOfMeasureCombo.getValue());

				if (AlertHandler.getAlert(AlertType.CONFIRMATION, "Are your sure you want create this Product", "")
						.getResult().getButtonData().equals(ButtonData.OK_DONE)) {
					if (productDao.saveProduct(saveProduct)) {
						clearDataClicked();
						AlertHandler.getAlert(AlertType.INFORMATION, "Sucess : you have created item",
								"item Code : " + saveProduct.getPrd_id());

					}

				}

			} catch (Exception e) {
				AlertHandler.getAlert(AlertType.ERROR, "Error Inserting Data", e.getMessage());
				e.printStackTrace();
			}
		}
	}

	//search method
	
	@FXML
	void searchItems() {
		thisSearchTextField.textProperty().addListener((ObservableValue, oldValue, newValue) -> {
			filteredListForSearch.setPredicate((Predicate<? super Products>) prd -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				if (String.valueOf(prd.getPrd_id()).toLowerCase().contains(lowerCaseFilter)) {
					return true;
				} else if (prd.getP_name().toLowerCase().contains(lowerCaseFilter)) {
					return true;
				}

				return false;
			});
		});

		sList = new SortedList<>(filteredListForSearch);

		sList.comparatorProperty().bind(itemTableView.comparatorProperty());
		itemTableView.setItems(sList);
	}

	
	//even though total fields of a product are captured, only limited fields will be updated due to business logic
	// below fields will not be updated 
	// setPack_price,setPack_cost , setUnit_price,setUnit_cost_price,setUnitAverageCost,setOnHQty
	//price updates will be handled by price update module
	//after editing pack size pack prices should be updated by using Price update module to maintain consistency
	@FXML
	void updateActionButtonClicked(ActionEvent event) {
		if (validateProductForm()) {
			try {
				System.out.println(prdSupField.getValue());
				Supplier thisSuppler = supplierDao.getSupplierById((Integer) supplierMap.get(prdSupField.getValue()));
				Category thisCategory = categoryDao.getCategoryById(categoryMap.get(prdCatField.getValue()));
				Products saveProduct = new Products(Integer.parseInt(pluCodeField.getText()), prdNameField.getText(),
						thisCategory, thisSuppler, Double.parseDouble(prdPackSizeField.getText()),
						Double.parseDouble(prdPackPriceField.getText()), Double.parseDouble(prdPackCostField.getText()),
						Double.parseDouble(prdUnitPriceField.getText()),
						Double.parseDouble(prdCostPriceField.getText()), Double.parseDouble(prdDiscountField.getText()),
						Double.parseDouble(prdReOrderLField.getText()), Double.parseDouble(prdVatField.getText()),
						prdValdityCheckBox.isSelected(), 0);
				saveProduct.setUnitAverageCost(Double.parseDouble(prdCostPriceField.getText()));
				saveProduct.setUnitOfMeasure(unitOfMeasureCombo.getValue());
				if (AlertHandler.getAlert(AlertType.CONFIRMATION, "Are your sure you want Update this Product", "")
						.getResult().getButtonData().equals(ButtonData.OK_DONE)) {
					if (productDao.updateProduct(saveProduct)) {
						clearDataClicked();
						AlertHandler.getAlert(AlertType.INFORMATION, "Sucess", "");
						updateActionButton.setVisible(false);

					}

				}

			} catch (Exception e) {
				AlertHandler.getAlert(AlertType.ERROR, "Error Inserting Data", e.getMessage());
				e.printStackTrace();
			}
		}
	}

	@FXML
	void updateButton(ActionEvent event) {
		if (!pluCodeField.getText().isEmpty() && !actionButton.isVisible()) {
			setUpdateEditable();
			pluCodeField.setDisable(true);

			updateActionButton.setVisible(true);
			prdPackCostField.setDisable(true);
			prdPackPriceField.setDisable(true);
		} else {
			AlertHandler.getAlert(AlertType.ERROR, "You Must Select the Product First", "");
		}

	}

	@FXML
	void viewInfoClicked(ActionEvent event) {
		try {
			if (actionButton.isVisible() || updateActionButton.isVisible()) {
				updateActionButton.setVisible(false);
				clearDataClicked();
				actionButton.setVisible(false);
			}
			prdCatField.getEditor().getStyleClass().add("set-not-editable-text");
			prdSupField.getEditor().getStyleClass().add("set-not-editable-text");
			productInstance = productDao.getProductById(Integer.parseInt(viewItemField.getText()));
			pluCodeField.setText(String.valueOf(productInstance.getPrd_id()));
			prdNameField.setText(productInstance.getP_name());
			prdPackSizeField.setText(String.valueOf(productInstance.getPack_size()));
			prdPackPriceField.setText(String.valueOf(productInstance.getPack_price()));
			prdPackCostField.setText(String.valueOf(productInstance.getPack_cost()));
			prdUnitPriceField.setText(String.valueOf(productInstance.getUnit_price()));
			prdCostPriceField.setText(String.valueOf(productInstance.getUnit_cost_price()));
			prdDiscountField.setText(String.valueOf(productInstance.getDiscount()));
			prdVatField.setText(String.valueOf(productInstance.getVat()));
			prdReOrderLField.setText(String.valueOf(productInstance.getReOrderLevel()));
			prdValdityCheckBox.setSelected(productInstance.isStatus());
			prdCatField.setValue(productInstance.getCategory().getCategoryName());
			prdSupField.setValue(productInstance.getSupplier().getCom_name());
			prdCatField.getEditor().setText(productInstance.getCategory().getCategoryName());
			prdSupField.getEditor().setText(productInstance.getSupplier().getCom_name());
			unitOfMeasureCombo.setValue(productInstance.getUnitOfMeasure());
			onhQtyField.setText(String.valueOf(productInstance.getOnHandQty()));
		} catch (Exception e) {
			AlertHandler.getAlert(AlertType.ERROR, "Product cannot Find- pls enter Prduct Code", e.getMessage());

		}

	}

	// CSS for Error Handlings adding & Removing Methods

	private void addTextFieldCss() {
		pluCodeField.getStyleClass().add("itext-field");
		prdNameField.getStyleClass().add("itext-field");
		prdCatField.getStyleClass().add("itext-field");
		prdSupField.getStyleClass().add("itext-field");
		
		prdCatField.getEditor().getStyleClass().add("itext-field");
		prdSupField.getEditor().getStyleClass().add("itext-field");
		
		prdPackSizeField.getStyleClass().add("itext-field");
		prdPackPriceField.getStyleClass().add("itext-field");
		prdPackCostField.getStyleClass().add("itext-field");
		prdUnitPriceField.getStyleClass().add("itext-field");
		prdCostPriceField.getStyleClass().add("itext-field");
		prdDiscountField.getStyleClass().add("itext-field");
		prdVatField.getStyleClass().add("itext-field");
		prdReOrderLField.getStyleClass().add("itext-field");
		prdValdityCheckBox.getStyleClass().add("itext-field");
		unitOfMeasureCombo.getStyleClass().add("itext-field");
	}

	private void addOriginalCss() {
		pluCodeField.getStyleClass().add("set-not-editable-text");
		prdNameField.getStyleClass().add("set-not-editable-text");
		prdCatField.getStyleClass().add("set-not-editable-text");
		prdSupField.getStyleClass().add("set-not-editable-text");
		prdCatField.getEditor().getStyleClass().add("set-not-editable-text");
		prdSupField.getEditor().getStyleClass().add("set-not-editable-text");
		prdVatField.getStyleClass().add("set-not-editable-text");
		prdPackSizeField.getStyleClass().add("set-not-editable-text");
		prdPackPriceField.getStyleClass().add("set-not-editable-text");
		prdPackCostField.getStyleClass().add("set-not-editable-text");
		prdUnitPriceField.getStyleClass().add("set-not-editable-text");
		prdCostPriceField.getStyleClass().add("set-not-editable-text");
		prdDiscountField.getStyleClass().add("set-not-editable-text");
		prdReOrderLField.getStyleClass().add("set-not-editable-text");
		prdValdityCheckBox.getStyleClass().add("set-not-editable-text");
		unitOfMeasureCombo.getStyleClass().add("set-not-editable-text");
	}

	private void removeTextFieldCss() {
		pluCodeField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		prdNameField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		prdCatField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		prdSupField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");	
		prdPackSizeField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		prdPackPriceField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		prdPackCostField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		prdUnitPriceField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		prdCostPriceField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		prdDiscountField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		prdVatField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		prdReOrderLField.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		prdValdityCheckBox.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
		unitOfMeasureCombo.getStyleClass().removeAll("set-not-editable-text", "danger-for-warning", "itext-field");
	}

	private boolean validateProductForm() {
		return ValidateInputs.validateInputEmpty(prdNameField , "Product Name Field")
				&& ValidateInputs.validateInputEmpty(prdCatField , "Category Field")
				&& ValidateInputs.validateInputEmpty(prdSupField , "Supplier Field") 
				&& ValidateInputs.validatePrices(prdPackSizeField , "Pack Size Field")
				&& ValidateInputs.validatePrices(prdPackPriceField,"Pack Price Field") 
				&& ValidateInputs.validatePrices(prdPackCostField, "Pack Cost Field")
				&& ValidateInputs.validatePrices(prdUnitPriceField , "Unit Price Field")
				&& ValidateInputs.validatePrices(prdCostPriceField , "Cost Price Field")
				&& ValidateInputs.validatePrices(prdDiscountField ,"Discount Field")
				&& ValidateInputs.validatePrices(prdVatField,"VAT Field")
				&& ValidateInputs.validatePrices(prdReOrderLField ,"Re Order Level Field")
				&& ValidateInputs.validateInputEmpty(unitOfMeasureCombo, "Unit of Measure Field");
	}

	


	// criteria searching
	public void searchDataClicked() {

		//itemTableView.getItems().clear();
		productList.clear();

		HashMap<String, Object> hasForSearch = new HashMap<String, Object>();

		hasForSearch.put("Plu", pluCodeField.getText());
		hasForSearch.put("prdName", prdNameField.getText());
		hasForSearch.put("prdCat", prdCatField.getEditor().getText());

		hasForSearch.put("prdSup", prdSupField.getEditor().getText());
		hasForSearch.put("packSize", prdPackSizeField.getText());
		hasForSearch.put("packPrice", prdPackPriceField.getText());

		hasForSearch.put("packCost", prdPackCostField.getText());
		hasForSearch.put("prdUnitPrice", prdUnitPriceField.getText());
		hasForSearch.put("prdUnitCost", prdCostPriceField.getText());
		hasForSearch.put("discount", prdDiscountField.getText());
		hasForSearch.put("reorder", prdReOrderLField.getText());

		HashMap<String, Object> finalHash = new HashMap<String, Object>();
		for (String x : hasForSearch.keySet()) {
			if (hasForSearch.get(x).equals(null) || hasForSearch.get(x).equals("")) {
				continue;

			}

			finalHash.put(x, hasForSearch.get(x));
		}

		finalHash.forEach((e, k) -> {
			System.out.println(e + k);
		});

		if (productList.isEmpty() && !finalHash.isEmpty()) {
			paginationWithTableViewForCriteriaResult(finalHash);
		} else {
			AlertHandler.getAlert(AlertType.ERROR, "No Criterias Found", null);
		}
		

	}

	// criteria searching
	public void criteriaInquiryButton() {
		
		productList.clear();
		itemTableView.getItems().clear();
		pluCodeField.setDisable(false);
		updateActionButton.setVisible(false);
		actionButton.setVisible(false);
		critSearchButton.setVisible(true);
		setTextFieldEditable();
		prdVatField.setDisable(true);
		prdUnitPriceField.setDisable(false);
		prdCostPriceField.setDisable(false);
	}

	//pagination idexes;
	int pageResult = 50;
	int firstResult = 0;
	long maxRecord = 0;
	int nextResult = pageResult;
	int prevResult = 0;
	
	
	//hbox for pagination buttons
	@FXML
	private HBox hboxPagination;

	public void paginationWithTableViewForAllResult() {
		actionButton.setVisible(false);
		updateActionButton.setVisible(false);
		productInstance = null;
		hboxPagination.getChildren().clear();
		pageResult = 50;
		firstResult = 0;
		maxRecord = 0;
		nextResult = pageResult;
		prevResult = 0;
		filteredListForSearch.clear();
		sList.clear();
		productList.clear();
		itemTableView.setItems(productList);
		HashMap<Long, List<Products>> map = productDao.paginationWithTableView(0, pageResult);
		int count = 1;
		for (Entry<Long, List<Products>> x : map.entrySet()) {
			//initializing next and previous buttons
			Button nextButton = new Button("Next");
			Button prevButton = new Button("Prev");
			prevButton.setDisable(true);
			
			//next button setOnAction action
			nextButton.setOnAction(e -> {

				HashMap<Long, List<Products>> maping = productDao.paginationWithTableView(nextResult, pageResult);
				for (Entry<Long, List<Products>> y : maping.entrySet()) {
					System.out.println();
					System.out.println(pageResult);
					productList.clear();
					productList.addAll(y.getValue());
					itemTableView.setItems(productList);
					prevResult = nextResult - pageResult;
					nextResult = nextResult + pageResult;

					if (nextResult >= firstResult) {
						nextButton.setDisable(true);
					} else {
						nextButton.setDisable(false);
					}
					
					if (prevResult < 0) {
						prevButton.setDisable(true);
					} else {
						prevButton.setDisable(false);
					}
					break;

				}

			});

			
			//Prev button setOnAction action
			prevButton.setOnAction(e -> {
				HashMap<Long, List<Products>> maping = productDao.paginationWithTableView(prevResult, pageResult);
				for (Entry<Long, List<Products>> y : maping.entrySet()) {
					System.out.println(pageResult);
					productList.clear();
					productList.addAll(y.getValue());
					itemTableView.setItems(productList);
					nextResult = prevResult + pageResult;
					prevResult = prevResult - pageResult;
					
					if (nextResult >= firstResult) {
						nextButton.setDisable(true);
					} else {
						nextButton.setDisable(false);
					}
					if (prevResult < 0) {
						prevButton.setDisable(true);
					} else {
						prevButton.setDisable(false);
					}
					break;

				}
			});

			//initially adding nextButton to the HBOX pagination
			hboxPagination.getChildren().add(prevButton);

			
			//clear all vales of product list and itemtableview
			productList.addAll(x.getValue());
			itemTableView.setItems(productList);
			
			//get no of record from hashmap where hasMap key will be returned as the No of Record from the ProductDaoImpl
			// using two database calls only from the initial database call;
			maxRecord = x.getKey();
			
			if(maxRecord <= pageResult) {
				nextButton.setDisable(true);
			}
			else {
				nextButton.setDisable(false);
			}
			//no of Buttons required for pagination result will be required no of Buttons 1-many
			int noOfBtns = (int) ((maxRecord + pageResult - 1) / pageResult);
			
			//iterate each and every noOfbuttons 
			for (int i = 1; i <= noOfBtns; i++) {
				
				//Initialization of pagination buttons
				Button btn = new Button(String.valueOf(i));
				
				//setting button id of each and every button where button id value will be first result of the particular database call
				//example : HashMap<Long, List<Products>> maping = ProductDaoImpl.paginationWithTableView(Integer.parseInt(btn.getId()), pageResult);
							
				btn.setId(String.valueOf(firstResult));
				firstResult = firstResult + pageResult;
				
				//pagination button setonAction method for each and every button
				btn.setOnAction(e -> {
					
					//iterate each and every buttons in the Hbox where and Hide buttons appropriately where button
					// value greater than/ or less than  + or - (specific nof buttons to show) 
					//in order to hide
					for (Node b : hboxPagination.getChildren()) {
						Button reviseButton = (Button) b;

						int currentButton = Integer.parseInt(btn.getText());
						if (!reviseButton.getText().equals("Prev") && !reviseButton.getText().equals("Next")
								&& !reviseButton.getText().equals("1")
								&& !reviseButton.getText().equals("" + noOfBtns + "")) {
							if (Integer.parseInt(reviseButton.getText()) < (currentButton - 2)
									|| Integer.parseInt(reviseButton.getText()) > (currentButton + 2)) {
								reviseButton.setVisible(false);
								reviseButton.setManaged(false);
							}

							else if (Integer.parseInt(reviseButton.getText()) < (currentButton + 3)) {
								reviseButton.setVisible(true);
								reviseButton.setManaged(true);
							}
						}

					}

					
					//finally get result from the dao class and set next Result and previous result appropriately 
					HashMap<Long, List<Products>> maping = productDao
							.paginationWithTableView(Integer.parseInt(btn.getId()), pageResult);
					for (Entry<Long, List<Products>> y : maping.entrySet()) {
						System.out.println(btn.getText());
						System.out.println(pageResult);
						productList.clear();
						productList.addAll(y.getValue());
						itemTableView.setItems(productList);
						nextResult = Integer.parseInt(btn.getId()) + pageResult;
						prevResult = Integer.parseInt(btn.getId()) - pageResult;
						if (nextResult >= firstResult) {
							nextButton.setDisable(true);
						} else {
							nextButton.setDisable(false);
						}

						if (prevResult < 0) {
							System.out.println("prev:"+prevResult);
							prevButton.setDisable(true);
						} else {
							prevButton.setDisable(false);
						}
						break;

					}
				});
				//paginate button action over
				
				// hide buttons in order to limit only three buttons to show except 1 and last
				if (Integer.parseInt(btn.getText()) > 3 && Integer.parseInt(btn.getText()) != noOfBtns) {
					btn.setVisible(false);
					btn.setManaged(false);
				}
				
				//adding paginate buuttons to the Hbox
				hboxPagination.getChildren().add(btn);
			}
		
			//adding Previos button lastly to the Hbox
			hboxPagination.getChildren().add(nextButton);

			// in order to make sure that iteration only happens once otherwise break
			if (count > 1) {
				break;
			}
			count++;

		}

	}

	public void paginationWithTableViewForCriteriaResult(HashMap<String, Object> finalHash) {
		actionButton.setVisible(false);
		updateActionButton.setVisible(false);
		productInstance = null;
		hboxPagination.getChildren().clear();
		pageResult = 50;
		firstResult = 0;
		maxRecord = 0;
		nextResult = pageResult;
		prevResult = 0;
		filteredListForSearch.clear();
		sList.clear();
		productList.clear();
		itemTableView.setItems(productList);
		HashMap<Long, List<Products>> map = productDao.getPrductsByMultipleCriteriasHashSearchWithPagination(finalHash,0, pageResult);
		int count = 1;
		
		if(map == null) {
			AlertHandler.getAlert(AlertType.ERROR, "No Data Found", null);
			return;
		}
		for (Entry<Long, List<Products>> x : map.entrySet()) {
			//initializing next and previous buttons
			Button nextButton = new Button("Next");
			Button prevButton = new Button("Prev");
			prevButton.setDisable(true);
			
			//next button setOnAction action
			nextButton.setOnAction(e -> {

				HashMap<Long, List<Products>> maping = productDao.getPrductsByMultipleCriteriasHashSearchWithPagination(finalHash,nextResult, pageResult);
				for (Entry<Long, List<Products>> y : maping.entrySet()) {
					System.out.println();
					System.out.println(pageResult);
					productList.clear();
					productList.addAll(y.getValue());
					itemTableView.setItems(productList);
					prevResult = nextResult - pageResult;
					nextResult = nextResult + pageResult;

					if (nextResult >= firstResult) {
						nextButton.setDisable(true);
					} else {
						nextButton.setDisable(false);
					}
					
					if (prevResult < 0) {
						prevButton.setDisable(true);
					} else {
						prevButton.setDisable(false);
					}
					break;

				}

			});

			
			//Prev button setOnAction action
			prevButton.setOnAction(e -> {
				HashMap<Long, List<Products>> maping = productDao.getPrductsByMultipleCriteriasHashSearchWithPagination(finalHash, prevResult, pageResult);
				for (Entry<Long, List<Products>> y : maping.entrySet()) {
					System.out.println(pageResult);
					productList.clear();
					productList.addAll(y.getValue());
					itemTableView.setItems(productList);
					nextResult = prevResult + pageResult;
					prevResult = prevResult - pageResult;
					
					if (nextResult >= firstResult) {
						nextButton.setDisable(true);
					} else {
						nextButton.setDisable(false);
					}
					if (prevResult < 0) {
						prevButton.setDisable(true);
					} else {
						prevButton.setDisable(false);
					}
					break;

				}
			});

			//initially adding nextButton to the HBOX pagination
			hboxPagination.getChildren().add(prevButton);

			
			//clear all vales of product list and itemtableview
			productList.addAll(x.getValue());
			itemTableView.setItems(productList);
			
			//get no of record from hashmap where hasMap key will be returned as the No of Record from the ProductDaoImpl
			// using two database calls only from the initial database call;
			maxRecord = x.getKey();
			
			if(maxRecord <= pageResult) {
				nextButton.setDisable(true);
			}
			else {
				nextButton.setDisable(false);
			}
			
			
			//no of Buttons required for pagination result will be required no of Buttons 1-many
			int noOfBtns = (int) ((maxRecord + pageResult - 1) / pageResult);
			
			//iterate each and every noOfbuttons 
			for (int i = 1; i <= noOfBtns; i++) {
				
				//Initialization of pagination buttons
				Button btn = new Button(String.valueOf(i));
				
				//setting button id of each and every button where button id value will be first result of the particular database call
				//example : HashMap<Long, List<Products>> maping = ProductDaoImpl.paginationWithTableView(Integer.parseInt(btn.getId()), pageResult);
							
				btn.setId(String.valueOf(firstResult));
				firstResult = firstResult + pageResult;
				
				//pagination button setonAction method for each and every button
				btn.setOnAction(e -> {
					
					//iterate each and every buttons in the Hbox where and Hide buttons appropriately where button
					// value greater than/ or less than  + or - (specific nof buttons to show) 
					//in order to hide
					for (Node b : hboxPagination.getChildren()) {
						Button reviseButton = (Button) b;

						int currentButton = Integer.parseInt(btn.getText());
						if (!reviseButton.getText().equals("Prev") && !reviseButton.getText().equals("Next")
								&& !reviseButton.getText().equals("1")
								&& !reviseButton.getText().equals("" + noOfBtns + "")) {
							if (Integer.parseInt(reviseButton.getText()) < (currentButton - 2)
									|| Integer.parseInt(reviseButton.getText()) > (currentButton + 2)) {
								reviseButton.setVisible(false);
								reviseButton.setManaged(false);
							}

							else if (Integer.parseInt(reviseButton.getText()) < (currentButton + 3)) {
								reviseButton.setVisible(true);
								reviseButton.setManaged(true);
							}
						}

					}

					
					//finally get result from the dao class and set next Result and previous result appropriately 
					HashMap<Long, List<Products>> maping = productDao
							.getPrductsByMultipleCriteriasHashSearchWithPagination(finalHash,Integer.parseInt(btn.getId()), pageResult);
					for (Entry<Long, List<Products>> y : maping.entrySet()) {
						System.out.println(btn.getText());
						System.out.println(pageResult);
						productList.clear();
						productList.addAll(y.getValue());
						itemTableView.setItems(productList);
						nextResult = Integer.parseInt(btn.getId()) + pageResult;
						prevResult = Integer.parseInt(btn.getId()) - pageResult;
						if (nextResult >= firstResult) {
							nextButton.setDisable(true);
						} else {
							nextButton.setDisable(false);
						}

						if (prevResult < 0) {
							System.out.println("prev:"+prevResult);
							prevButton.setDisable(true);
						} else {
							prevButton.setDisable(false);
						}
						break;

					}
				});
				//paginate button action over
				
				// hide buttons in order to limit only three buttons to show except 1 and last
				if (Integer.parseInt(btn.getText()) > 3 && Integer.parseInt(btn.getText()) != noOfBtns) {
					btn.setVisible(false);
					btn.setManaged(false);
				}
				
				//adding paginate buuttons to the Hbox
				hboxPagination.getChildren().add(btn);
			}
		
			//adding Previos button lastly to the Hbox
			hboxPagination.getChildren().add(nextButton);

			// in order to make sure that iteration only happens once otherwise break
			if (count > 1) {
				break;
			}
			count++;

		}

	}
}
