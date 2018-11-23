package pkgController;

import java.sql.SQLException;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;
import pkgData.Database;
import pkgData.Producer;
import pkgData.Product;
import pkgMisc.ExceptionHandler;
import pkgMisc.ProductStates;

public class Controller_Main
{

    @FXML
    private MenuItem mntmLoadProducers;

    @FXML
    private MenuItem mntmUpdateAndCommit;

    @FXML
    private MenuItem mntmAddProduct;

    @FXML
    private ComboBox<String> cmbxConnectionString;

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

    @FXML
    private VBox paneProductInformation;

    @FXML
    private VBox paneProducers;

    @FXML
    private Label lblMessage;

    private Database db;
    private Producer currentProducer;
    private ObservableList<Producer> listProducers;
    private ObservableList<Product> listProducts;
    private ObservableList<String> listConnectionStrings;

    @FXML
    void initialize()
    {
	listProducers = FXCollections.observableArrayList();
	listProducts = FXCollections.observableArrayList();
	listConnectionStrings = FXCollections.observableArrayList();

	cmbxProducer.setItems(listProducers);
	tableProducts.setItems(listProducts);
	listConnectionStrings.add(Database.CONNECTION_STRING_EXTERN);
	listConnectionStrings.add(Database.CONNECTION_STRING_INTERN);
	cmbxConnectionString.setItems(listConnectionStrings);

	colId.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
	colProductName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
	colOnMarket.setCellValueFactory(new PropertyValueFactory<Product, LocalDate>("onMarket"));
	colProductOnStock.setCellValueFactory(new PropertyValueFactory<Product, Integer>("onStock"));
	colProductName.setCellFactory(TextFieldTableCell.forTableColumn());
	colProductOnStock.setCellFactory(TextFieldTableCell.forTableColumn((new IntegerStringConverter())));
    }

    @FXML
    void onEditCellProductName(CellEditEvent<Product, String> event)
    {
	Product update = (event.getTableView().getItems().get(event.getTablePosition().getRow()));

	update.setName(event.getNewValue());
	if (update.getState().equals(ProductStates.NOT_CHANGED))
	{
	    update.setState(ProductStates.UPDATED);
	}
	db.updateProduct(update);
    }

    @FXML
    void onEditProductOnStock(CellEditEvent<Product, Integer> event)
    {
	Product update = (event.getTableView().getItems().get(event.getTablePosition().getRow()));

	update.setOnStock(event.getNewValue());
	if (update.getState().equals(ProductStates.NOT_CHANGED))
	{
	    update.setState(ProductStates.UPDATED);
	}
	db.updateProduct(update);
    }

    @FXML
    void onSelectMenuDatabase(ActionEvent event)
    {
	try
	{
	    Object source = event.getSource();
	    if (source.equals(mntmAddProduct))
	    {
		Product newP = new Product(db.getNextProductId(), "New Product", 0, LocalDate.now(),
			currentProducer.getId(), ProductStates.ADDED);
		db.addProduct(newP, currentProducer);
		doRefreshTableProducts();
	    } else if (source.equals(mntmLoadProducers))
	    {
		db = Database.newInstance(cmbxConnectionString.getValue());
		doFillListProducers();
		paneProducers.setDisable(false);
	    } else if (source.equals(mntmUpdateAndCommit))
	    {
		for (Producer prd : Database.getCollProducers())
		{
		    for (Product pro : prd.getCollProducts())
		    {
			try
			{
			    if (pro.getState().equals(ProductStates.ADDED))
			    {
			        db.insertProductInDatabase(pro);
			    } else if (pro.getState().equals(ProductStates.UPDATED))
			    {
			        System.out.println(pro.toString());
			        db.updateProductInDatabase(pro);
			    }
			} catch (SQLException e)
			{
			    db.rollback();
			    doHandleUnexpectedException(e);
			}
		    }
		}
	    }
	} catch (Exception e)
	{
	    doHandleUnexpectedException(e);
	}
    }

    private void doRefreshTableProducts()
    {
	listProducts.clear();
	listProducts.addAll(Database.getProducts());
    }

    @FXML
    void onSelectComboBox(ActionEvent event)
    {
	try
	{
	    if (event.getSource().equals(cmbxProducer))
	    {
		currentProducer = cmbxProducer.getValue();
		db.selectProducts(currentProducer);
		doRefreshTableProducts();
		paneProductInformation.setDisable(false);
	    }
	} catch (SQLException e)
	{
	    doHandleUnexpectedException(e);
	}
    }

    private void doFillListProducers() throws SQLException
    {
	db.selectProducers();
	listProducers.clear();
	listProducers.setAll(Database.getProducers());
    }

    private void doHandleExpectedException(String msg, Exception ex)
    {
	lblMessage.setText(msg);
	ExceptionHandler.hanldeExpectedException(msg, ex);
    }

    private void doHandleUnexpectedException(Exception ex)
    {
	lblMessage.setText(ex.getMessage());
	ExceptionHandler.hanldeUnexpectedException(ex);
    }

}
