package pkgController;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import pkgData.Car;
import pkgData.Database;
import pkgData.Repair;
import pkgMisc.BigDecimalFormatConverter;
import pkgMisc.DateFormatConverter;

public class FXML_MainController
{

	private Database db;

	@FXML
	private Label lblMessage;

	@FXML
	private MenuItem menuAddRepair;

	@FXML
	private TextArea txtTextRepair;

	@FXML
	private Button btnRemoveCar;

	@FXML
	private TextField txtNameCar;

	@FXML
	private MenuItem menuUpdateRepair;

	@FXML
	private MenuItem menuFromJson;

	@FXML
	private MenuItem menuFromBin;

	@FXML
	private MenuItem menuToBin;

	@FXML
	private Button btnAddCar;

	@FXML
	private TextField txtIdCar;

	@FXML
	private DatePicker dateRepairDate;

	@FXML
	private TextField txtRepairId;

	@FXML
	private TextField txtAmountRepair;

	@FXML
	private MenuItem menuToJson;

	@FXML
	private MenuItem menuDeleteRepair;

	@FXML
	private ListView<Car> lstListViewCars;

	@FXML
	private Button btnUpdateCar;

	@FXML
	private TableView<Repair> tableRepairs;

	@FXML
	private TableColumn<Repair, Integer> colId;

	@FXML
	private TableColumn<Repair, LocalDate> colDate;

	@FXML
	private TableColumn<Repair, String> colText;

	@FXML
	private TableColumn<Repair, BigDecimal> colAmount;

	private ObservableList<Car> listCars;
	private ObservableList<Repair> listRepairs;
	private Car currentCar;

	// --------------------------FXML functions----------------------------
	@FXML
	void onSelectListCars(MouseEvent event)
	{
		currentCar = lstListViewCars.getSelectionModel().getSelectedItem();
		txtNameCar.setText(currentCar.getName());
		txtIdCar.setText(String.valueOf(currentCar.getId()));
		refreshListRepairs();
	}

	@FXML
	void onSelectButton(ActionEvent event)
	{
		try
		{
			if (event.getSource().equals(btnAddCar))
			{
				doAdd();
			} else if (event.getSource().equals(btnUpdateCar))
			{
				doUpdate();
			} else if (event.getSource().equals(btnRemoveCar))
			{
				doRemove();
			}
		} catch (Exception ex)
		{
			lblMessage.setText(ex.getMessage());
			ex.printStackTrace();
		}

	}

	@FXML
	void onSelectMenuFile(ActionEvent event)
	{
		try
		{
			if (event.getSource().equals(menuToBin))
			{
				doStoreCarsBin();
			} else if (event.getSource().equals(menuFromBin))
			{
				doRestoreCarsBin();
			} else if (event.getSource().equals(menuToJson))
			{
				doStoreCarsJson();
			} else if (event.getSource().equals(menuFromJson))
			{
				doRestoreCarsJson();
			}
		} catch (Exception ex)
		{
			lblMessage.setText(ex.getMessage());
			ex.printStackTrace();
		}
	}

	@FXML
	void onSelectMenuRepair(ActionEvent event)
	{
		try
		{
			if (event.getSource().equals(menuAddRepair))
			{
				doAddRepair();
			} else if (event.getSource().equals(menuUpdateRepair))
			{
				doUpdateRepair();
			} else if (event.getSource().equals(menuDeleteRepair))
			{
				doDeleteRepair();
			}
		} catch (Exception ex)
		{
			lblMessage.setText(ex.getMessage());
			ex.printStackTrace();
		}
	}

	@FXML
	public void initialize()
	{
		db = Database.newInstance();
		// list things
		listCars = FXCollections.observableArrayList();
		listRepairs = FXCollections.observableArrayList();
		lstListViewCars.setItems(listCars);
		tableRepairs.setItems(listRepairs);
		// date things
		dateRepairDate.setPromptText(DateFormatConverter.getPattern());
		dateRepairDate.setConverter(new DateFormatConverter());
		// do not enter number
		txtAmountRepair.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
			{
				if (!newValue.matches("[0-9]*\\.?[0-9]{0,2}"))
				{
					txtAmountRepair.setText(oldValue);
					lblMessage.setText("warning: illegal number");
				} else
				{
					lblMessage.setText("");
				}
			}
		});

		// Table things
		colId.setCellValueFactory(new PropertyValueFactory<>("idRepair"));
		colDate.setCellValueFactory(new PropertyValueFactory<>("dateRepair"));
		colText.setCellValueFactory(new PropertyValueFactory<>("textRepair"));
		colAmount.setCellValueFactory(new PropertyValueFactory<>("amountRepair"));
		colAmount.setStyle("-fx-alignment: CENTER-RIGHT");

		colText.setCellFactory(TextFieldTableCell.forTableColumn());
		colAmount.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalFormatConverter()));
	}

	@FXML
	void onEditCellAmount(CellEditEvent<Repair, BigDecimal> event)
	{
		(event.getTableView().getItems().get(event.getTablePosition().getRow())).setAmountRepair(event.getNewValue());
	}

	@FXML
	void onEditCellText(CellEditEvent<Repair, String> event)
	{
		(event.getTableView().getItems().get(event.getTablePosition().getRow())).setTextRepair(event.getNewValue());
	}

	@FXML
	void onRepairSelected(MouseEvent event)
	{
		Repair tmp = tableRepairs.getSelectionModel().getSelectedItem();
		txtRepairId.setText(Integer.toString(tmp.getIdRepair()));
		txtTextRepair.setText(tmp.getTextRepair());
		txtAmountRepair.setText(tmp.getAmountRepair().toString());
		dateRepairDate.setValue(tmp.getDateRepair());
	}
	// -------------------- NO FXML functions--------------------------

	private void refreshListCars()
	{
		listCars.clear();
		listCars.addAll(db.getCollCars());
	}

	private void refreshListRepairs()
	{
		listRepairs.clear();
		listRepairs.addAll(db.getCollRepairs(currentCar));
	}

	private void doUpdateRepair() throws Exception
	{
		if (currentCar != null)
		{
			Repair r = tableRepairs.getSelectionModel().getSelectedItem();
			if (r != null)
			{
				r.setAmountRepair(new BigDecimal(txtAmountRepair.getText()));
				r.setTextRepair(txtTextRepair.getText());
				currentCar.updateRepair(r);
				refreshListRepairs();
			} else
				lblMessage.setText("No repair selected");
		} else
		{
			lblMessage.setText("No car selected");
		}
	}

	private void doAddRepair() throws Exception
	{
		if (currentCar != null)
		{
			Repair r = new Repair(dateRepairDate.getValue(), txtTextRepair.getText(),
					new BigDecimal(txtAmountRepair.getText()));
			currentCar.addRepair(r);
			refreshListRepairs();
		} else
		{
			lblMessage.setText("No car selected");
		}
	}

	private void doDeleteRepair() throws Exception
	{
		if (currentCar != null)
		{
			Repair r = tableRepairs.getSelectionModel().getSelectedItem();
			if (r != null)
			{
				currentCar.removeRepair(r);
				refreshListRepairs();
			} else
				lblMessage.setText("No repair selected");
		} else
		{
			lblMessage.setText("No car selected");
		}
	}

	private void doRestoreCarsJson() throws IOException
	{
		db.doRestoreCarsJson();
		refreshListCars();
		lblMessage.setText("Cars succesfully restored from JSON");
	}

	private void doStoreCarsJson() throws IOException
	{
		db.doStoreCarsJson();
		lblMessage.setText("Cars succesfully stored to JSON");
	}

	private void doStoreCarsBin() throws IOException
	{
		db.doStoreCarsBin();
		lblMessage.setText("Cars succesfully stored to binary");
	}

	private void doRestoreCarsBin() throws ClassNotFoundException, IOException
	{
		db.doRestoreCarsBin();
		refreshListCars();
		lblMessage.setText("Cars succesfully restored from binary");
	}

	private void doAdd() throws NumberFormatException, Exception
	{
		db.addCar(new Car(Integer.parseInt(txtIdCar.getText()), txtNameCar.getText()));
		refreshListCars();
	}

	private void doUpdate() throws NumberFormatException, Exception
	{
		db.updateCar(new Car(Integer.parseInt(txtIdCar.getText()), txtNameCar.getText()));
		refreshListCars();
	}

	private void doRemove() throws NumberFormatException, Exception
	{
		db.removeCar(new Car(Integer.parseInt(txtIdCar.getText()), txtNameCar.getText()));

		refreshListCars();
	}

}
