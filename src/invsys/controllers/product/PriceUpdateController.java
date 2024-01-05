package invsys.controllers.product;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import invsys.controllers.formvalidation.AlertHandler;
import invsys.controllers.formvalidation.ValidateInputs;
import invsys.controllers.mainpage.MainController;
import invsys.entities.PriceUpdate;
import invsys.entities.Products;
import invsys.entities.Users;
import invsys.entitiydao.ProductDao;
import invsys.entitiydao.impl.ProductDaoImpl;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class PriceUpdateController implements Initializable {

	
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
	    private TextField prdPackSizeField;

	    @FXML
	    private TextField prdPackPriceField;

	    @FXML
	    private TextField prdPackCostField;

	    @FXML
	    private TextField prdUnitPriceField;

	    @FXML
	    private TextField prdCostPriceField;

	    @FXML
	    private TextField prdDiscountField;

	    @FXML
	    private TextField prdVatField;

	    @FXML
	    private TextField prdReOrderLField;

	    @FXML
	    private TextField onhQtyField;

	    @FXML
	    private CheckBox prdValdityCheckBox;

	    @FXML
	    private JFXComboBox<String> unitOfMeasureCombo;

	 // inorder track view info clicked & delete items
		private Products productInstance;
		
	//Dao Handlers/Classes
		ProductDao productDao = null;
		
		@Override
		public void initialize(URL location, ResourceBundle resources) {
			productDao = ProductDaoImpl.getDao();
			ValidateInputs.getInstance();
			ProductDaoImpl.getDao();		
			updatPackPricesAuto();
		}
		
	    @FXML
	    void clearButtonClicked() {
	    	
	    	viewItemField.clear();
	    	pluCodeField.clear();
			prdNameField.clear();
			prdPackSizeField.clear();
			prdPackPriceField.clear();
			prdPackCostField.clear();
			prdUnitPriceField.clear();
			prdCostPriceField.clear();
			prdDiscountField.clear();
			prdVatField.clear();
			prdReOrderLField.clear();
			onhQtyField.clear();
			prdValdityCheckBox.setSelected(false);
			productInstance = null;
			unitOfMeasureCombo.getSelectionModel().clearAndSelect(0);
			prdCatField.getEditor().clear();
			prdSupField.getEditor().clear();
			
	    }

	    @FXML
	    void updateButtonClicked() {
	    	if (validateProductForm()) {
				try {
					
					Products updateProduct = productInstance;
					PriceUpdate priceUpdate = new PriceUpdate();
					Users user = MainController.getUserSession();
					if(updateProduct != null && user != null) {
						
						priceUpdate.setOldCostPrice(productInstance.getUnit_cost_price());
						priceUpdate.setOldPackCostPrice(productInstance.getPack_cost());
						priceUpdate.setOldPackUnitPrice(productInstance.getPack_price());
						priceUpdate.setOldUnitPrice(productInstance.getUnit_price());
						priceUpdate.setProduct(productInstance);
						
						double newUnitPrice = Double.parseDouble(prdUnitPriceField.getText());
						double newCostPrice = Double.parseDouble(prdCostPriceField.getText());
						double newPrdPackCostPrice = Double.parseDouble(prdPackCostField.getText());
						double newPrdPackUnitPrice = Double.parseDouble(prdPackPriceField.getText());
						updateProduct.setUnit_price(newUnitPrice);
						updateProduct.setUnit_cost_price(newCostPrice);
						updateProduct.setPack_price(newPrdPackUnitPrice);
						updateProduct.setPack_cost(newPrdPackCostPrice);
						
						priceUpdate.setNewCostPrice(updateProduct.getUnit_cost_price());
						priceUpdate.setNewPackCostPrice(updateProduct.getPack_cost());
						priceUpdate.setNewPackUnitPrice(updateProduct.getPack_price());
						priceUpdate.setNewUnitPrice(updateProduct.getUnit_price());
						
						priceUpdate.setRevisionDate(java.sql.Date.valueOf(LocalDate.now()));
						priceUpdate.setUser(user);
						
					} else {
						AlertHandler.getAlert(AlertType.ERROR, "Product Entity Is Null", "Please Select Product First");
					}			
					
					if (AlertHandler.getAlert(AlertType.CONFIRMATION, "Are your sure you want Update this Product", "")
							.getResult().getButtonData().equals(ButtonData.OK_DONE)) {
						if (productDao.updateProductPrices(updateProduct,priceUpdate)) {
							clearButtonClicked();
							AlertHandler.getAlert(AlertType.INFORMATION, "Sucess", "");
							updateProduct = null;
							priceUpdate = null;
							user = null;
						}

					}

				} catch (Exception e) {
					AlertHandler.getAlert(AlertType.ERROR, "Error Inserting Data", e.getMessage());
					e.printStackTrace();
				}
			}

	    }

	    @FXML
	    void updateSuppliersAndCategoryies() {

	    }

	    @FXML
	    void viewInfoClicked() {
	    	try {
			
	    	if(ValidateInputs.validateNumbers(viewItemField, "View Item Field")) {
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
				prdCatField.getEditor().setText((productInstance.getCategory().getCategoryName()));
				prdSupField.getEditor().setText((productInstance.getSupplier().getCom_name()));
				onhQtyField.setText(String.valueOf(productInstance.getOnHandQty()));
				unitOfMeasureCombo.setValue(productInstance.getUnitOfMeasure());
				prdUnitPriceField.requestFocus();
	    	}
				
			} catch (Exception e) {
				AlertHandler.getAlert(AlertType.ERROR, "Product cannot Find- pls enter Prduct Code", e.getMessage());

			}

	    	
	    }
	    
	    //automatically updating pack cost price , pack unit price accoridngly when change
	    // unit prices
	    private void updatPackPricesAuto() {
	    	prdUnitPriceField.setOnKeyReleased(e->{
	    		try {
	    			if(productInstance != null && e.getCode().isDigitKey()) {
	    				double updatePrice = Double.parseDouble(prdUnitPriceField.getText());
	    				double prdPackSize = Double.parseDouble(prdPackSizeField.getText());
	    				double newPackPrice = updatePrice * prdPackSize;
	    				prdPackPriceField.setText(String.format("%.2f",newPackPrice));
	    				
	    		} else if(productInstance != null && (e.getCode().equals(KeyCode.BACK_SPACE) || e.getCode().equals(KeyCode.DELETE))) {
	    				prdUnitPriceField.clear();
	    				prdPackPriceField.setText(String.valueOf(productInstance.getPack_price()));
	    		}
	    		}catch(Exception ex) {
	    			AlertHandler.getAlert(AlertType.ERROR, "Somthing Went Wrong In Prices", null);
	    		}
	    		
	    	});
	    	
	    	prdCostPriceField.setOnKeyReleased(e->{
	    		try {
	    			if(productInstance != null && e.getCode().isDigitKey()) {
	    				double updatePrice = Double.parseDouble(prdCostPriceField.getText());
	    				double prdPackSize = Double.parseDouble(prdPackSizeField.getText());
	    				double newPackPrice = updatePrice * prdPackSize;
	    				prdPackCostField.setText(String.format("%.2f",newPackPrice));
	    				
	    		} else if(productInstance != null && (e.getCode().equals(KeyCode.BACK_SPACE) || e.getCode().equals(KeyCode.DELETE))) {
	    				prdCostPriceField.clear();
	    				prdPackCostField.setText(String.valueOf(productInstance.getPack_cost()));
	    		}
	    		}catch(Exception ex) {
	    			AlertHandler.getAlert(AlertType.ERROR, "Somthing Went Wrong In Prices", null);
	    		}
	    		
	    	});
	    }
	    
	    //validate key inputs product form
	    private boolean validateProductForm() {
	    	return ValidateInputs.validatePrices(prdUnitPriceField , "Unit Price Field") & ValidateInputs.validatePrices(prdCostPriceField , "Cost Price Field");
		}


		
}
