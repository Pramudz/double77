package invsys.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import invsys.controllers.purchaseorder.PurchaseOrderController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;

@Entity
@Table(name = "products")
public class Products implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "prd_id", updatable = false, nullable = false)
	private long prd_id;

	@Column(name = "p_name", unique = true)
	private String p_name;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "cat_id", nullable = false, referencedColumnName = "cat_id")
	private Category category;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "sup_id", nullable = false, referencedColumnName = "sup_id")
	private Supplier supplier;

	@Column(name = "pack_size", nullable = false)
	private double pack_size;

	@Column(name = "pack_price", nullable = false)
	private double pack_price;

	@Column(name = "pack_cost", nullable = false)
	private double pack_cost;

	@Column(name = "unit_price", nullable = false)
	private double unit_price;

	@Column(name = "unit_cost_price", nullable = false)
	private double unit_cost_price;
	
	@Column(name = "unit_average_cost" , nullable =false , scale = 2)
	private double unitAverageCost;
	
	@Column(name = "discount")
	private double discount;

	@Column(name = "re_or_level", nullable = false)
	private double reOrderLevel;

	@Column(name = "vat", nullable = false)
	private double vat;

	@Column(name = "status", nullable = false)
	private boolean status;

	@Column(name = "on_h_qty")
	private double onHandQty;
	
	
	@Column(name="unit_of_measure", length =10 , columnDefinition = "varchar(10) default 'unit'")
	private String unitOfMeasure;

	// button for purchase order maintenence usage

	@Transient
	private Button addButton;

	// in order to make action using addbutton clicked
	@Transient
	private static FXMLLoader purchaeOrderLoader;

	public Products() {

	}

	public Products(int prd_id, String p_name, Category category, Supplier supplier, double pack_size,
			double pack_price, double pack_cost, double unit_price, double unit_cost_price, double discount,
			double reOrderLevel, double vat, boolean status, double onHandQty) {
		super();

		this.prd_id = prd_id;
		this.p_name = p_name;
		this.category = category;
		this.supplier = supplier;
		this.pack_size = pack_size;
		this.pack_price = pack_price;
		this.pack_cost = pack_cost;
		this.unit_price = unit_price;
		this.unit_cost_price = unit_cost_price;
		this.discount = discount;
		this.reOrderLevel = reOrderLevel;
		this.vat = vat;
		this.status = status;
		this.onHandQty = onHandQty;
	}
	
	

	public Products(String p_name, Category category, Supplier supplier, double pack_size,
			double pack_price, double pack_cost, double unit_price, double unit_cost_price, double discount,
			double reOrderLevel, double vat, boolean status, double onHandQty) {
		super();
		this.p_name = p_name;
		
		this.category = category;
		this.supplier = supplier;
		this.pack_size = pack_size;
		this.pack_price = pack_price;
		this.pack_cost = pack_cost;
		this.unit_price = unit_price;
		this.unit_cost_price = unit_cost_price;
		this.discount = discount;
		this.reOrderLevel = reOrderLevel;
		this.vat = vat;
		this.status = status;
		this.onHandQty = onHandQty;
	}

	// this constructor specialy for purchase order control

	public Products(long prd_id, String p_name, double qty, double packSize, double costPrice, String unitOfMeasuure) {

		this.prd_id = prd_id;
		this.p_name = p_name;
		this.unit_cost_price = costPrice;
		this.onHandQty = qty;
		this.pack_size = packSize;
		this.unitOfMeasure = unitOfMeasuure;
		this.addButton = new Button("Add");

		addButton.setOnAction(e -> {
			PurchaseOrderController controller = purchaeOrderLoader.getController();
			for (Products load : controller.getLoadItemList()) {
				if (this.addButton == load.getAddButton()) {
					long prdId = load.getPrd_id();
					String prdDesc = load.getP_name();
					double price = load.getUnit_cost_price();
					TextInputDialog dialog = new TextInputDialog();
					dialog.setHeaderText("Pls Enter Qty");
					Double result = Double.parseDouble(dialog.showAndWait().get());
					double amount = price * result;
					System.out.println(prdId + prdDesc + price);
					controller.addButtonClickedFromTableView(result, price, load);
					break;

				}
			}

		});

	}

	// return button with the CSS
	public Button getAddButton() {
		addButton.setStyle("-fx-background-color:#2ecc71; -fx-text-fill:#2c3e50;");
		addButton.setMaxSize(70, 50);
		return addButton;

	}

	public static void setPurchaeOrderLoader(FXMLLoader purchaeOrderLoader) {
		Products.purchaeOrderLoader = purchaeOrderLoader;
	}


	public long getPrd_id() {
		return prd_id;
	}

	public String getP_name() {
		return p_name;
	}

	public Category getCategory() {
		return category;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public double getPack_size() {
		return pack_size;
	}

	public double getPack_price() {
		return pack_price;
	}

	public double getPack_cost() {
		return pack_cost;
	}

	public double getUnit_price() {
		return unit_price;
	}

	public double getUnit_cost_price() {
		return unit_cost_price;
	}

	public double getDiscount() {
		return discount;
	}

	public double getReOrderLevel() {
		return reOrderLevel;
	}

	public double getVat() {
		return vat;
	}

	public boolean isStatus() {
		return status;
	}

	public void setPrd_id(int prd_id) {
		this.prd_id = prd_id;
	}

	public void setP_name(String p_name) {
		this.p_name = p_name;
	}


	public void setCategory(Category category) {
		this.category = category;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public void setPack_size(double pack_size) {
		this.pack_size = pack_size;
	}

	public void setPack_price(double pack_price) {
		this.pack_price = pack_price;
	}

	public void setPack_cost(double pack_cost) {
		this.pack_cost = pack_cost;
	}

	public void setUnit_price(double unit_price) {
		this.unit_price = unit_price;
	}

	public void setUnit_cost_price(double unit_cost_price) {
		this.unit_cost_price = unit_cost_price;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public void setReOrderLevel(double reOrderLevel) {
		this.reOrderLevel = reOrderLevel;
	}

	public void setVat(double vat) {
		this.vat = vat;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	
	public double getUnitAverageCost() {
		return unitAverageCost;
	}

	public void setUnitAverageCost(double unitAverageCost) {
		this.unitAverageCost = unitAverageCost;
	}
	
	
	public double getOnHandQty() {
		return onHandQty;
	}

	public void setOnHandQty(double onHandQty) {
		this.onHandQty = onHandQty;
	}
	
	
	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}

	@Override
	public String toString() {
		return "Products [prd_id=" + prd_id + ", p_name=" + p_name + ", category=" + category
				+ ", supplier=" + supplier + ", pack_size=" + pack_size + ", pack_price=" + pack_price + ", pack_cost="
				+ pack_cost + ", unit_price=" + unit_price + ", unit_cost_price=" + unit_cost_price + ", discount="
				+ discount + ", reOrderLevel=" + reOrderLevel + ", vat=" + vat + ", status=" + status + ",onHandQty="
				+ onHandQty + "]";
	}

}