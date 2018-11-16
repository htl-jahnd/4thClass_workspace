package pkgController;

import java.sql.SQLException;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import pkgData.Database;
import pkgData.Producer;
import pkgData.Product;

public class Controller_Main
{

    @FXML
    private MenuItem mntmLoadProducers;

    @FXML
    private MenuItem mntmUpdateAndCommit;

    @FXML
    private MenuItem mntmAddProduct;

    @FXML
    private TextField txtConnectionString;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField pwdPassword;

    @FXML
    private ComboBox<Producer> cmbxProducer;

    @FXML
    private TableView<Product> tableProducts;

    @FXML
    private TableColumn<Product, Integer> colId;

    @FXML
    private TableColumn<Product, String> colProductName;

    @FXML
    private TableColumn<Product, Integer> colProductOnStock;

    @FXML
    private TableColumn<Product, LocalDate> colOnMarket;

    private Database db;
    private ObservableList<Producer> listProducers;
    private ObservableList<Product> listProducts;

    @FXML
    void initialize()
    {
	listProducers = FXCollections.observableArrayList();
	listProducts = FXCollections.observableArrayList();
	
	cmbxProducer.setItems(listProducers);
	tableProducts.setItems(listProducts);
    }
    
    

    @FXML
    void onEditCellProductName(ActionEvent event)
    {
	// TODO later on
    }

    @FXML
    void onEditProductOnStock(ActionEvent event)
    {
	// TODO later on
    }

    @FXML
    void onSelectMenuDatabase(ActionEvent event)
    {
	try
	{
	    Object source = event.getSource();
	    if (source.equals(mntmAddProduct))
	    {
	        // TODO
	    } else if (source.equals(mntmLoadProducers))
	    {
	       db = Database.newInstance(txtConnectionString.getText());
	       doFillListProducers();
	    } else if (source.equals(mntmUpdateAndCommit))
	    {
	        // TODO
	    }
	} catch (Exception e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    private void doFillListProducers() throws SQLException
    {
	db.selectProducers();
	listProducers.clear();
	listProducers.setAll(db.getProducers());
    }

}
