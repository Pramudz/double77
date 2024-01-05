package invsys.controllers.supplierreturn;

import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import invsys.controllers.database.HibernateUtil;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.controllers.formvalidation.ValidateInputs;
import invsys.controllers.mainpage.MainController;
import invsys.controllers.purchaseorder.GetSupplierController;
import invsys.entities.Products;
import invsys.entities.Supplier;
import invsys.entities.SupplierReturn;
import invsys.entities.SupplierReturnDetail;
import invsys.entities.Users;
import invsys.entitiydao.ProductDao;
import invsys.entitiydao.SupplierDao;
import invsys.entitiydao.SupplierReturnDao;
import invsys.entitiydao.impl.ProductDaoImpl;
import invsys.entitiydao.impl.SupplierDaoImpl;
import invsys.entitiydao.impl.SupplierReturnDaoImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.converter.DoubleStringConverter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

public class SupplierReturnController implements Initializable{
	
	
		@FXML
	    private JFXTextField supIdField;

	    @FXML
	    private Label supplierLabel;

	    @FXML
	    private AnchorPane anchor;

	    @FXML
	    private JFXComboBox<String> itemDescCombo;

	    @FXML
	    private TextField itemCodeField;

	    @FXML
	    private TextField costPriceField;

	    @FXML
	    private TextField returnQtyField;

	    @FXML
	    private JFXComboBox<String> reasonCombo;

	    @FXML
	    private TableView<SupplierReturnDetail> returnTable;

	    @FXML
	    private TableColumn<SupplierReturnDetail, Integer> snoCol;

	    @FXML
	    private TableColumn<SupplierReturnDetail, String> itemCodeCol;

	    @FXML
	    private TableColumn<SupplierReturnDetail, String> descCol;

	    @FXML
	    private TableColumn<SupplierReturnDetail, Double> returnQtyCol;

	    @FXML
	    private TableColumn<SupplierReturnDetail, Double> costPriceCol;

	    @FXML
	    private TableColumn<SupplierReturnDetail, Double> amountCol;

	    @FXML
	    private TableColumn<SupplierReturnDetail, String> reasonCol;

	    @FXML
	    private TextField totReturnItemCountField;

	    @FXML
	    private TextField totalReturnQtyField;

	    @FXML
	    private TextField totalReturnAmountFiield;
	    
	    
	    //supplier return detail list
	    private ObservableList<SupplierReturnDetail> supplierReturnDetailList = FXCollections.observableArrayList();
	    
	    // in order to prevent from opening more than one stage for supplier getting
		// table
	 	private Stage supplierStage;
	 	
	    //this FXMLloader when loading this class inorder to pass data between
		// Scenes
		private FXMLLoader thisSupplierReturnLoader;
		
	
	    // tempory product variable to keep track the setCombo box on clicked function
		// pressed
		private Products temporyProduct;
		
		
		//To hold Supplier instance through out the saving method
		private Supplier supplierEntity;
	    
	    //dao handler/classes
		ProductDao productDao = null;
		SupplierDao supplierDao = null;
		SupplierReturnDao supplierReturnDao = null;
		
	    @Override
		public void initialize(URL location, ResourceBundle resources) {
	    	
	    	ValidateInputs.getInstance();
	    	
	    	
	    	disableFields();
	    	
	    	productDao = ProductDaoImpl.getDao();
	    	supplierDao = SupplierDaoImpl.getDao();
	    	supplierReturnDao = SupplierReturnDaoImpl.getDao();
	    	setReturnReasons();		
	    	initTableView();
	    	onPrdDescComboAction();
	    	
	    	
	    	returnQtyField.setOnKeyReleased(e -> {
				if (e.getCode().equals(KeyCode.ESCAPE)) {
					clearTextFields();
				}
			});
	    		    	
	    	reasonCombo.setOnKeyPressed(e -> {
				if (e.getCode().equals(KeyCode.ESCAPE)) {
					clearTextFields();
				}
				
				if (e.getCode().equals(KeyCode.ENTER) && !reasonCombo.getValue().isBlank()) {
					returnQtyField.requestFocus();
				}
			});
	    	
	    	
		}
	    
	    
	    //getter for This Loader
	    public FXMLLoader getThisSupplierReturnLoader() {
			return thisSupplierReturnLoader;
		}

	    
	  //Setter for This Loader
		public void setThisSupplierReturnLoader(FXMLLoader thisSupplierReturnLoader) {
			this.thisSupplierReturnLoader = thisSupplierReturnLoader;
		}



		//initialize table view
	    private void initTableView() {
	    	snoCol.setCellValueFactory(new PropertyValueFactory<>("seqNo"));
	    	itemCodeCol.setCellValueFactory((TableColumn.CellDataFeatures<SupplierReturnDetail,String> data) -> new SimpleStringProperty(
	    			String.valueOf(data.getValue().getProduct().getPrd_id())));
	    	
	    	descCol.setCellValueFactory((TableColumn.CellDataFeatures<SupplierReturnDetail,String> data) -> new SimpleStringProperty(
	    			String.valueOf(data.getValue().getProduct().getP_name())));
	    	returnQtyCol.setCellValueFactory(new PropertyValueFactory<>("returnQty"));
	    	costPriceCol.setCellValueFactory(new PropertyValueFactory<>("prductCost"));
	    	amountCol.setCellValueFactory(new PropertyValueFactory<>("prdAmount"));
	    	reasonCol.setCellValueFactory(new PropertyValueFactory<>("reason"));
	    	
	    	
	    	returnTable.setEditable(true);
			returnQtyCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
	    }
	    
	    //update return Reasons combo manually
	    private void setReturnReasons() {
	    	reasonCombo.getItems().add("Product Defect");
	    	reasonCombo.getItems().add("Damage");
	    	reasonCombo.getItems().add("Spoiled");
	    	reasonCombo.getItems().add("rat Bite");
	    }
	    
	    
		// load supplier table view
	    @FXML
	    void loadSupplierTableAction() throws IOException {
	    	if (supplierReturnDetailList.isEmpty()) {
				if (supplierStage == null) {
					String loc = "/fxml/supplier/SupplierTable.fxml";
					FXMLLoader loader = new FXMLLoader(getClass().getResource(loc));
					Scene scene = new Scene(loader.load());
					GetSupplierController passLoadertoSupplierTable = loader.getController();
					passLoadertoSupplierTable.setLoader(thisSupplierReturnLoader);

					supplierStage = new Stage(StageStyle.DECORATED);
					supplierStage.setScene(scene);

					supplierStage.show();
				} else if (supplierStage.isShowing()) {
					supplierStage.toFront();

				} else {
					supplierStage.show();
				}

			} else {
				AlertHandler.getAlert(AlertType.WARNING, "Supplier Details Cannot be loaded",
						"Make Sure that Loaded Return Details are cleared Or Finalized");
			}
			   	
	    	
	    }
	    
	  //setdata to thisScene from SupplierTable View 
	    // setTemporySupplierEntity
	    //set SupplierName for Label
	     public void setSupplierDetailsToMainScene(Supplier supplier) {
	    	clearTextFields();
	    	supplierEntity = supplier;
	    	supIdField.setText(String.valueOf(supplierEntity.getSup_id()));
	    	supplierLabel.setText(supplier.getCom_name());
	    	enableFields();
	    	itemCodeField.requestFocus();
	    		    	
	    }
	    //similar to above method setSupplierDetailsToMainScene but getting supplier details directly from SupplierCod
	    @FXML
	    public void supCodFieldSetOnAction() {
	    	try {
	    		clearTextFields();
	    		if(supplierReturnDetailList.isEmpty()) {
	    			int supId = Integer.parseInt(supIdField.getText());
	        		Supplier supplier = supplierDao.getSupplierById(supId);
	        		supplierEntity = supplier;
	        		supplierLabel.setText(supplier.getCom_name());
	        		enableFields();
	        		
	    		}
	    		else {
	    			AlertHandler.getAlert(AlertType.WARNING, "Supplier Details Cannot be loaded",
	    					"Make Sure that Loaded Return Details are cleared Or Finalized");
	    		}
	    		
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    		AlertHandler.getAlert(AlertType.ERROR, "Cannot Load Data", e.getCause().toString());
	    	}
	    
	    	
	    }
	    
	    //disable some text fields in order to validate
	    private void disableFields() {
	    	itemCodeField.setEditable(false);
			itemDescCombo.getEditor().setEditable(false);
			returnQtyField.setEditable(false);
			reasonCombo.getEditor().setEditable(false);
	    }
	    
	    //disable some text fields in order to validate
	    private void enableFields() {
	    	itemCodeField.setEditable(true);
			itemDescCombo.getEditor().setEditable(true);
			returnQtyField.setEditable(true);
			reasonCombo.getEditor().setEditable(true);
	    }

	    
	    @FXML
	    void ClearAllFields() {
	    	
	    	supplierEntity = null;
	    	temporyProduct = null;
	    	supIdField.clear();
	    	totalReturnAmountFiield.clear();
	    	supplierReturnDetailList.clear();
	    	totalReturnQtyField.clear();
	    	totReturnItemCountField.clear();
			supplierLabel.setText("");
			clearTextFields();
			returnTable.getItems().clear();
			disableFields();
			
	    }

	  

	    @FXML
	    void clearTextFields() {
	    	itemCodeField.clear();
			itemDescCombo.getEditor().clear();
			itemDescCombo.getSelectionModel().clearSelection();
			itemDescCombo.getItems().clear();
			costPriceField.clear();
			returnQtyField.clear();
			reasonCombo.getEditor().clear();
			itemDescCombo.getEditor().requestFocus();
			reasonCombo.getSelectionModel().clearSelection();
	    }

	    
	    
	    @FXML
	    void itemCodeFieldEnter() {
	    	try {
				long codeText = Long.valueOf(itemCodeField.getText());
				temporyProduct = productDao.getPrductsByIdAndSupplier(codeText,supplierEntity.getSup_id());
				itemDescCombo.getEditor().setText(temporyProduct.getP_name());
				costPriceField.setText(String.valueOf(temporyProduct.getUnit_cost_price()));
				reasonCombo.requestFocus();

			} catch (Exception f) {
				AlertHandler.getAlert(AlertType.ERROR, "Cannot Load Item Invalid", f.getMessage());
				clearTextFields();
				f.printStackTrace();
			}

	    }
	    
	 // get product entity from product description  from combo and populate
		// other
		// data to the other text box such as price,etc...
		private void onPrdDescComboAction() {

			itemDescCombo.getEditor().setOnKeyReleased(e -> {

				if (e.getCode() == KeyCode.ENTER) {

					if (itemDescCombo.getEditor().getText().isEmpty()) {
						return;
					} else {
						try {
							String prd = itemDescCombo.getEditor().getText();

							temporyProduct = productDao.getPrductByNameAndSupplier(prd, supplierEntity.getSup_id());
							itemCodeField.setText(String.valueOf(temporyProduct.getPrd_id()));
							costPriceField.setText(String.valueOf(temporyProduct.getUnit_cost_price()));
							reasonCombo.requestFocus();
							itemDescCombo.getSelectionModel().clearSelection();
							// in order to mapp searching operation in combo box
							itemDescCombo.getEditor().setText(prd);

						} catch (Exception f) {
							AlertHandler.getAlert(AlertType.ERROR, "Cannot Find or Load Item Invalid",
									f.getLocalizedMessage());
							f.printStackTrace();
						}
					}

				}

				if (e.getCode().isLetterKey()) {

					String text = itemDescCombo.getEditor().getText();
					ObservableList<String> prdDescriptionList = productDao.getPrductsLikeProductNameAndSupplier(text , supplierEntity.getSup_id());
					for (String x : prdDescriptionList) {
						System.out.println(x);
					}
					itemDescCombo.setItems(prdDescriptionList);

					if (!itemDescCombo.getItems().isEmpty())
						itemDescCombo.show();

				}
				if (e.getCode().equals(KeyCode.BACK_SPACE) || e.getCode().equals(KeyCode.DELETE)) {
					itemDescCombo.getSelectionModel().clearSelection();
				}

				if (itemDescCombo.getItems().size() == 1) {
					if (e.getCode().equals(KeyCode.DOWN))
						itemDescCombo.getSelectionModel().selectFirst();
				}

			});

		}

		
		  @FXML
		    void addReturnQtyMethod() {
			  
			  try {
				  
				  if( ValidateInputs.validateInputEmptyComboWithoutEditorStringOnly(reasonCombo, "Return Reason Field") && ValidateInputs.validatePrices(returnQtyField ,"Return Quantity Field")) {
					  
					  if(temporyProduct.getUnitOfMeasure().equals("unit")) {
							boolean check = ValidateInputs.validateQtyForUnitItems(returnQtyField , "Return Quantity Field");
							if (!check)
								return;
						}
					  
					  double quantity = Double.valueOf(returnQtyField.getText());
					  String returnReason = reasonCombo.getValue();
					  
					  
					  for(SupplierReturnDetail x : supplierReturnDetailList) {
							if(x.getProduct().getPrd_id() == temporyProduct.getPrd_id()) {
								AlertHandler.getAlert(AlertType.WARNING, "Duplicate Entry : You Have already Entered this Item",
										"if you want to modify you can edit it using table view");
								clearTextFields();
								return;
							}
						}
					  
					  if (quantity <= 0) {
							AlertHandler.getAlert(AlertType.ERROR, "Invalid Number", null);
							returnQtyField.getStyleClass().add("danger-for-warning");
							return;
						} else {
							
							SupplierReturnDetail supReturnDet = new SupplierReturnDetail();
							supReturnDet.setProduct(temporyProduct);
							supReturnDet.setProductName(temporyProduct.getP_name());
							supReturnDet.setReason(returnReason);
							supReturnDet.setReturnQty(quantity);
							supReturnDet.setPrductCost(temporyProduct.getUnit_cost_price());
							supReturnDet.setPrdAmount(quantity * temporyProduct.getUnit_cost_price());
							supReturnDet.setItemVat(temporyProduct.getVat());
							
							supplierReturnDetailList.add(supReturnDet);
							returnTable.setItems(supplierReturnDetailList);
							temporyProduct = null;
							supReturnDet = null;
							clearTextFields();
							refreshSummaryData();
							
							
						}
					  
				  }
				  
				  
			  }catch(Exception e) {
				  AlertHandler.getAlert(AlertType.ERROR, "Error Adding Data to Table", e.getLocalizedMessage());
			  }
			  
			  
		    }
	

	    private void refreshSummaryData() {
		
	    	double totalReturnAmount = 0;
			double totalReturnQty  = 0;
			int seqNo = 0;
			
			for (SupplierReturnDetail e : supplierReturnDetailList) {
				e.setSeqNo(++seqNo);
				totalReturnAmount = totalReturnAmount + (e.getPrdAmount());
				totalReturnQty = totalReturnQty + (e.getReturnQty());
			
			}
			
			totalReturnAmountFiield.setText(String.valueOf(totalReturnAmount));
			totalReturnQtyField.setText(String.valueOf(totalReturnQty));
			totReturnItemCountField.setText(String.valueOf(seqNo));
			
	    }


		@FXML
	    void removeItemFromTableView() {
			
			SupplierReturnDetail selecteRowData = (SupplierReturnDetail) returnTable.getSelectionModel().getSelectedItem();
			if (!supplierReturnDetailList.isEmpty() && !returnTable.getSelectionModel().getSelectedItems().isEmpty()) {
				supplierReturnDetailList.remove(selecteRowData);
				refreshSummaryData();
				returnTable.setItems(supplierReturnDetailList);
			} else {
				AlertHandler.getAlert(AlertType.WARNING, "Data Error",
						"No data to Delete");
			}

	    }
		
		//edit qty in table view
		@FXML
		void editQtyButtonClicked(CellEditEvent<SupplierReturnDetail, Double> event) {
			SupplierReturnDetail tempData;

			tempData = (SupplierReturnDetail) returnTable.getSelectionModel().getSelectedItem();
			
			try {
				if (supplierReturnDetailList.isEmpty()) {
					System.out.println("No Data to Edit");
				} else {
					
					if(tempData.getProduct().getUnitOfMeasure().equals("unit")) {
						String checkValue = event.getNewValue().toString();
						checkValue = checkValue.contains(".") ? checkValue.replaceAll("0*$","").replaceAll("\\.$","") : checkValue;
						boolean check = ValidateInputs.validateQtyForUnitItemsWithString(checkValue, "Return Quanitity Field");
						if(!check) {
							event.getTableView().getItems().set(event.getTablePosition().getRow(), tempData);
							return;
						}
						
					}
					
					double qty = event.getNewValue();
					tempData.setReturnQty(qty);
					tempData.setPrdAmount(qty * tempData.getPrductCost());
					refreshSummaryData();
					event.getTableView().getItems().set(event.getTablePosition().getRow(), tempData);
				}
				
			}catch(Exception e) {
				AlertHandler.getAlert(AlertType.ERROR, "Cell Edit Event Error", e.getLocalizedMessage());
				event.getTableView().getItems().set(event.getTablePosition().getRow(), tempData);
				e.printStackTrace();
			}
			
		}

	    @FXML
	    void saveReturnClicked() {
	    
	    	try {
	    		if(!supplierReturnDetailList.isEmpty() && supplierEntity!=null) {
	    			
	    			Users user = MainController.getUserSession();
	    			
	    			SupplierReturn supReturn = new SupplierReturn();
	    			supReturn.setDocumentType("RON");
	    			supReturn.setReturnDate(java.sql.Date.valueOf(LocalDate.now()));
	    			supReturn.setSupplier(supplierEntity);
	    			supReturn.setUser(user);			
	    			for(SupplierReturnDetail dets : supplierReturnDetailList) {
	    				dets.setSupplierReturn(supReturn);
	    			}
	    			supReturn.setReturnAmount(Double.valueOf(totalReturnAmountFiield.getText()));
	    			
	    			
	    			if(AlertHandler.getAlert(AlertType.CONFIRMATION, "Are You Sure ?", "You Want to Save this Data").getResult().getButtonData().equals(ButtonData.OK_DONE)) {
	    				SupplierReturnDaoImpl.getDao();
	    				Long returnNumb = supplierReturnDao.saveSupplierReturnWithReturnId(supReturn, supplierReturnDetailList);
	    				if(returnNumb != null && returnNumb != 0) {
	    					AlertHandler.getAlert(AlertType.INFORMATION, "Update Success", "RON Number Is :"+returnNumb);
	    					viewReport(returnNumb);
	    					ClearAllFields();
	    				}
	    			}
	    			
	    			
	    		}
	    		
	    	}catch(Exception e) {
	    		AlertHandler.getAlert(AlertType.ERROR, "Error Saving Supplier Return", e.getLocalizedMessage());
	    		e.printStackTrace();
	    	}

	    }

	    
	 // view GRN Report
	 		public void viewReport(long supRetNo) throws JRException, IOException, SQLException {

	 						
	 				String loc= "resources/jasperreports/supplierReturnInquiry.jasper";
	 				
	 				Connection connectionForReports = HibernateUtil.getSessionFactory().getSessionFactoryOptions()
	 						.getServiceRegistry().getService(ConnectionProvider.class).getConnection();

	 				Map<String, Object> parameters = new HashMap();
	 				parameters.put("companyName", MainController.getCompanyInfoSession().getCompanyName());
	 				parameters.put("streetAddress", MainController.getCompanyInfoSession().getAddressLine1());
	 				parameters.put("addressLine", MainController.getCompanyInfoSession().getAddressLine2());
	 				parameters.put("city", MainController.getCompanyInfoSession().getAddressLine3());
	 				parameters.put("telephoneNum", MainController.getCompanyInfoSession().getTelephoneNum());
	 				parameters.put("supReturnId", supRetNo);
	 				parameters.put("documentOriginalStatus", "Original");
	 				parameters.put("printedBy", MainController.getUserSession().getUserName());
	 				JasperPrint jp = JasperFillManager.fillReport(loc, parameters, connectionForReports);

	 				ByteArrayOutputStream output = new ByteArrayOutputStream();
	 				JasperExportManager.exportReportToPdfStream(jp, output);
	 				final String PDF_FILE = "resources/pdfreports/supreturn.pdf";
	 				OutputStream pdfFile = new FileOutputStream(new File(PDF_FILE));
	 				pdfFile.write(output.toByteArray());
	 				pdfFile.flush();
	 				pdfFile.close();
	 				Desktop.getDesktop().open(new File(PDF_FILE));
	 			
	 		}

		

}
