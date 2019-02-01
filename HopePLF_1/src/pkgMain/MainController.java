package pkgMain;

import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import pkgData.Database;
import pkgData.Order;
import pkgData.Product;

public class MainController
{

    @FXML
    private ListView<Product> listProducts;

    @FXML
    private ListView<Order> listOrders;

    @FXML
    private Label lblMessage;

    @FXML
    private TextField txtOrderNumber;

    @FXML
    private TextField txtOrderQuantity;

    @FXML
    private Button btnLoad;

    @FXML
    private Button btnInsert;

    @FXML
    private Button btnExecute;

    private ObservableList<Product> lstProducts;
    private ObservableList<Order> lstOrder;
    private Database db;

    @FXML
    void initialize()
    {
	lstProducts = FXCollections.observableArrayList();
	lstOrder = FXCollections.observableArrayList();
	listOrders.setItems(lstOrder);
	listProducts.setItems(lstProducts);
    }

    @FXML
    void onSelectButton(ActionEvent event)
    {
	try
	{
	    if (event.getSource().equals(btnExecute))
	    {
		db.commit();
		doFillLists();
	    } else if (event.getSource().equals(btnLoad))
	    {
		db = Database.newInstance(Database.CONNECTION_STRING_INTERN);
		doFillLists();
	    } else if (event.getSource().equals(btnInsert))
	    {
		doInsert();
		doUpdateOrdersList();
	    }
	} catch (Exception e)
	{
	    lblMessage.setText(e.getMessage());
	    e.printStackTrace();
	}
    }

    private void doFillLists() throws SQLException
    {
	db.selectProducts();
	this.lstProducts.clear();
	this.lstProducts.addAll(db.getProducts());
	db.doSelectAllOrders();
	this.lstOrder.clear();
	this.lstOrder.addAll(db.getOrders());
    }

    private void doUpdateOrdersList()
    {
	ArrayList<Order> tmp = new ArrayList<Order>();
	tmp.addAll(Database.getOrders());
	tmp.addAll(Database.getOrdersToCommit());
	lstOrder.setAll(tmp);
    }

    private void doInsert() throws Exception
    {
	Product tmp = listProducts.getSelectionModel().getSelectedItem();
	if (tmp == null)
	    throw new Exception("Please select a product");
	else if (txtOrderNumber.getText().trim().isEmpty())
	    throw new Exception("Please enter an order number");
	else if (txtOrderQuantity.getText().trim().isEmpty())
	    throw new Exception("Please enter an order quantity");

	int qty = Integer.parseInt(txtOrderQuantity.getText());
	int orderNr = Integer.parseInt(txtOrderNumber.getText());
	Order od = new Order(orderNr, tmp, qty);
	db.addOrderToCommit(od);
	db.insertOrder(od);
	doClearTextFields();
    }

    private void doClearTextFields()
    {
	txtOrderNumber.clear();
	txtOrderQuantity.clear();

    }

}
