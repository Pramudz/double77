package invsys.controllers.pospaydetail;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;

import invsys.controllers.billingwindow.BillingController;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.controllers.formvalidation.ValidateInputs;
import invsys.entities.POSPayDetails;
import invsys.entities.PayModes;
import invsys.entitiydao.PayModeDao;
import invsys.entitiydao.impl.PayModeDaoImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class PosPaymentController implements Initializable {

	@FXML
	private JFXButton savePaymentButton;

	@FXML
	private JFXListView<String> listView;

	@FXML
	private TableView<POSPayDetails> payDetTableView;

	@FXML
	private TableColumn<POSPayDetails, Integer> seqNoCol;

	@FXML
	private TableColumn<POSPayDetails, String> payModCol;

	@FXML
	private TableColumn<POSPayDetails, String> cardTypeCol;

	@FXML
	private TableColumn<POSPayDetails, Double> amountCol;

	@FXML
	private TableColumn<POSPayDetails, String> slipNoCol;

	@FXML
	private TableColumn<POSPayDetails, String> refNoCol;

	@FXML
	private TextField discountField;

	@FXML
	private TextField billAMountField;

	@FXML
	private TextField toBePaidField;

	@FXML
	private TextField balanceField;

	@FXML
	private TextField payModeField;

	@FXML
	private TextField slipNoField;

	@FXML
	private TextField refNoFiled;

	@FXML
	private TextField payAmountField;

	@FXML
	private ComboBox<String> cardTypeCombo;

	@FXML
	private JFXButton addPayBtn;

	@FXML
	private JFXButton clearPayDetBtn;

	@FXML
	private JFXButton clearAllButton;

	// list for Pos Payment Details
	private ObservableList<POSPayDetails> posPayDetailList = FXCollections.observableArrayList();

	// in order to get billing window fxmlloader details to pass
	// data between scens
	private FXMLLoader billingFXLoader;

	// instance variable of the bill data

	private double billAmount;
	private double discount;
	private double toBePaid;
	private int seqNo;
	private double balance;
	private double totalPaidAmount;

	//Dao handlers/classes
	PayModeDao payModeDao = null;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		payModeDao = PayModeDaoImpl.getDao();
		ValidateInputs.getInstance();

		listView.setOnKeyPressed(e -> {
			if (!listView.getSelectionModel().getSelectedItem().isEmpty()) {

				if (e.getCode().equals(KeyCode.ENTER)) {
					getSelectedData();
				}
			}

		});

		initColumns();
	}

	// getter setter for billing loader
	public FXMLLoader getBillingFXLoader() {
		return billingFXLoader;
	}

	public void setBillingFXLoader(FXMLLoader billingFXLoader) {
		this.billingFXLoader = billingFXLoader;
	}

	// initialize tableview columns with mapping entity
	private void initColumns() {
		seqNoCol.setCellValueFactory(new PropertyValueFactory<>("temporySeqNo"));
		payModCol.setCellValueFactory(
				(TableColumn.CellDataFeatures<POSPayDetails, String> cdata) -> new SimpleStringProperty(
						String.valueOf(cdata.getValue().getPayMode().getModeId())));
		;
		cardTypeCol.setCellValueFactory(new PropertyValueFactory<>("subPayMode"));
		amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
		slipNoCol.setCellValueFactory(new PropertyValueFactory<>("slipNumber"));
		refNoCol.setCellValueFactory(new PropertyValueFactory<>("referenceNum"));

	}

	public void loadMainPayModes() {
		

		listView.setItems(payModeDao.getMainPayModes());

		listView.requestFocus();
		listView.getSelectionModel().selectFirst();

	}

	// for setting payment window bill value details
	public void setBillDetails(double discount, double billAmount, double tobePaid) {

		this.billAmount = billAmount;
		this.discount = discount;
		this.toBePaid = tobePaid;

		billAMountField.setText(String.format("%.2f",this.billAmount));
		discountField.setText(String.format("%.2f",this.discount));
		toBePaidField.setText(String.format("%.2f",this.toBePaid));

	}

	private void getSelectedData() {

		String selectedPayMode = listView.getSelectionModel().getSelectedItem();
		payModeField.setText(selectedPayMode);
		if (selectedPayMode.equals("cash")) {

			cardTypeCombo.getSelectionModel().clearSelection();
			cardTypeCombo.setDisable(true);
			slipNoField.setDisable(true);
			refNoFiled.setDisable(true);
			payAmountField.setDisable(false);
			refNoFiled.clear();
			payAmountField.clear();
			slipNoField.clear();
			payAmountField.requestFocus();

		}

		else if (selectedPayMode.equals("credit")) {
			cardTypeCombo.setDisable(false);
			slipNoField.setDisable(false);
			refNoFiled.setDisable(true);
			payAmountField.setDisable(false);
			refNoFiled.clear();
			payAmountField.clear();
			slipNoField.clear();
			cardTypeCombo.setItems(payModeDao.getChildPayModesByMain(selectedPayMode));
			cardTypeCombo.requestFocus();
			cardTypeCombo.setOnKeyPressed(e -> {

				if (e.getCode().equals(KeyCode.ENTER))
					if (!cardTypeCombo.getSelectionModel().isEmpty())
						slipNoField.requestFocus();

				slipNoField.setOnAction(d -> {
					payAmountField.requestFocus();
				});
			});

		}

		else {
			cardTypeCombo.getSelectionModel().clearSelection();
			cardTypeCombo.setDisable(true);
			slipNoField.setDisable(true);
			refNoFiled.setDisable(false);
			payAmountField.setDisable(false);
			refNoFiled.clear();
			payAmountField.clear();
			slipNoField.clear();
			refNoFiled.requestFocus();

			refNoFiled.setOnAction(e -> {
				payAmountField.requestFocus();
			});

		}

	}

	// amount field clicked
	public void addPayClicked() {

		if (ValidateInputs.validatePrices(payAmountField , "Amount Field")) {

			try {
				String typeOfPay = payModeField.getText();
				double paidAmount = Double.parseDouble(payAmountField.getText());
				String subMode = cardTypeCombo.getValue();
				String slipNumber = slipNoField.getText();
				String refNumber = refNoFiled.getText();

				POSPayDetails posPayDetails = new POSPayDetails();

				if (typeOfPay.equals("credit")) {
					if (subMode != null && !subMode.isEmpty()) {
						if (slipNoField.getText() != null && !slipNoField.getText().isEmpty()) {
							posPayDetails.setPayMode(new PayModes("credit", ""));
							posPayDetails.setSlipNumber(slipNumber);
							posPayDetails.setSubPayMode(subMode);
							posPayDetails.setReferenceNum("");

							if (paidAmount > this.toBePaid) {
								AlertHandler.getAlert(AlertType.ERROR, "Max Value Error",
										"this Pay mode not allowed to pay more than to be paid amount");
								payAmountField.requestFocus();
								return;
							}

						}

						else {
							AlertHandler.getAlert(AlertType.ERROR, "FieldEmptyError", "Slip Number Cannot Be Blanck");
							slipNoField.requestFocus();
							return;
						}

					} else {
						AlertHandler.getAlert(AlertType.ERROR, "FieldEmptyError", "CardType Cannot Be Blanck");
						cardTypeCombo.requestFocus();
						return;
					}
				} else if (typeOfPay.equals("cash")) {
					posPayDetails.setPayMode(new PayModes("cash", ""));
					posPayDetails.setSlipNumber("");
					posPayDetails.setSubPayMode("cash");
					posPayDetails.setReferenceNum("");
				} else {
					if (refNoFiled.getText() != null && !refNoFiled.getText().isBlank()) {
						posPayDetails.setPayMode(new PayModes(typeOfPay, ""));
						posPayDetails.setSlipNumber("");
						posPayDetails.setSubPayMode(typeOfPay);
						posPayDetails.setReferenceNum(refNumber);

						if (paidAmount > this.toBePaid) {
							AlertHandler.getAlert(AlertType.ERROR, "Max Value Error",
									"this Pay mode not allowed to pay more than to be paid amount");
							payAmountField.requestFocus();
							return;
						}
					} else {
						AlertHandler.getAlert(AlertType.ERROR, "FieldEmptyError", "Ref Cannot Be Blanck");
						refNoFiled.requestFocus();
						return;
					}
				}

				for (POSPayDetails dets : posPayDetailList) {
					String concat = dets.getPayMode().getModeId().concat(dets.getSubPayMode());
					String thisPayConcat = typeOfPay.concat(posPayDetails.getSubPayMode());

					if (concat.equals(thisPayConcat)) {
						AlertHandler.getAlert(AlertType.ERROR, "Duplicate Payment",
								"Same PayType are not allowed than once");
						return;

					}

				}
				posPayDetails.setAmount(paidAmount);

				posPayDetailList.add(posPayDetails);
				payDetTableView.setItems(posPayDetailList);
				clearPayDets();
				refreshSummaryData();

			} catch (Exception e) {
				AlertHandler.getAlert(AlertType.ERROR, "Error Adding Payment", e.getLocalizedMessage());
				e.printStackTrace();
			}

		}

	}

	private void refreshSummaryData() {
		double totalPaidAmount = 0;
		double totPaidCash = 0;
		this.seqNo = 0;
		this.balance = 0;
		this.totalPaidAmount = 0;
		for (POSPayDetails dets : posPayDetailList) {
			totalPaidAmount += dets.getAmount();
			dets.setTemporySeqNo(++this.seqNo);

			if (dets.getPayMode().getModeId().equals("cash")) {
				totPaidCash += dets.getAmount();
			}

		}

		System.out.println(totPaidCash);
		if (totPaidCash > this.toBePaid && totalPaidAmount > billAmount) {
			this.balance = totPaidCash - this.toBePaid;
			balanceField.setText(String.format("%.2f",this.balance));
		}

		this.toBePaid = this.billAmount - totalPaidAmount;

		if (this.toBePaid <= 0) {
			listView.setDisable(true);
			toBePaidField.setText(String.valueOf(0));
			savePaymentButton.requestFocus();
		} else {
			toBePaidField.setText(String.format("%.2f",this.toBePaid));
		}
		
		this.totalPaidAmount = totalPaidAmount;

	}
	
	
	public void saveButtonClicked() {
		
		if(this.toBePaid <= 0 && !posPayDetailList.isEmpty()) {
			
			BillingController loader = billingFXLoader.getController();
			loader.finalizeAction(totalPaidAmount, balance, posPayDetailList);
			
			clearAllDataClicked();
			discountField.getScene().getWindow().hide();
			
			}
		
		
	}

	// clear only payment pane detail
	public void clearPayDets() {
		payAmountField.setDisable(true);
		cardTypeCombo.setDisable(true);
		refNoFiled.setDisable(true);
		slipNoField.setDisable(true);
		payAmountField.clear();
		cardTypeCombo.getSelectionModel().clearSelection();
		refNoFiled.clear();
		slipNoField.clear();
		payModeField.clear();
		listView.requestFocus();
		listView.getSelectionModel().selectFirst();
	}

//	clear all including list data and tableview
	public void clearAllDataClicked() {
		payAmountField.setDisable(true);
		cardTypeCombo.setDisable(true);
		refNoFiled.setDisable(true);
		slipNoField.setDisable(true);
		listView.setDisable(false);
		payAmountField.clear();
		cardTypeCombo.getSelectionModel().clearSelection();
		refNoFiled.clear();
		balanceField.clear();
		slipNoField.clear();
		payModeField.clear();
		posPayDetailList.clear();
		payDetTableView.getItems().clear();
		seqNo = 0;
		refreshSummaryData();

	}

	public void manipulateKeyInputs() {
		discountField.getScene().addEventHandler(KeyEvent.KEY_RELEASED, e -> {
			if (e.getCode().equals(KeyCode.ESCAPE)) {
				clearAllDataClicked();
				discountField.getScene().getWindow().hide();
				e.consume();
			}
			
			
				if(e.getCode().equals(KeyCode.ENTER)) {
					saveButtonClicked();
					e.consume();
				}
			
			
		});

		payAmountField.setOnAction(e -> {
			addPayClicked();
		});

	}

}
