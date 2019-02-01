package pkgController;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import pkgData.Customer;
import pkgData.ISubject;
import pkgMisc.ISimulationValues;

public class MainController implements ISimulationValues
{

    @FXML
    private Button btnCreate;

    @FXML
    private ListView<String> listViewActions;

    @FXML
    private Button btnStart;

    @FXML
    private Label lblMessage;

    @FXML
    private Pane paneSimulation;

    private ObservableList<String> listActions;
    private ArrayList<ISubject> subjects = new ArrayList<ISubject>();
    private ArrayList<ImageView> images = new ArrayList<>();
    private Semaphore semaIdleFree = new Semaphore(MAX_CAP_ORDER);
    private Semaphore semaCashFree = new Semaphore(MAX_CAP_CASH);
    private boolean isRunning;

    @FXML
    void initialize()
    {
	listActions = FXCollections.observableArrayList();
	listViewActions.setItems(listActions);
    }

    @FXML
    void onSelectButton(ActionEvent event)
    {
	try
	{
	    if (event.getSource().equals(btnCreate))
	    {
		if (!isRunning)
		    setImages();
		else
		{
		    stopThreads();
		    reset();
		    setImages();
		    isRunning=false;
		}
		btnStart.setDisable(false);
	    } else if (event.getSource().equals(btnStart))
	    {
		if (!isRunning)
		{
		    startThreads();
		    isRunning = true;
		}
		btnStart.setDisable(true);

	    }
	} catch (Exception e)
	{
	    log("ERROR: " + e.getMessage());
	    e.printStackTrace();
	}
    }
    
    private void reset()
    {
	subjects.clear();
	
	for(ImageView iv : images) {
	    paneSimulation.getChildren().remove(iv);
	}
	images.clear();
	listActions.clear();
    }

    public void log(String msg)
    {
	listActions.add(msg);
    }

    private void startThreads()
    {
	for (ISubject s : subjects)
	{
	    new Thread((Runnable) s).start();
	}
    }

    private void stopThreads()
    {
	for (ISubject s : subjects)
	{
	    s.setEnd();
	}
    }

    private void setImages() throws Exception
    {
	for (int i = 1; i <= MAX_CUSTOMERS; i++)
	{
	    ImageView iv = new ImageView();
	    int t = new Random().nextInt(3);
	    Customer c = null;
	    switch (t)
	    {
	    case 0:
		c = new Customer(semaIdleFree, semaCashFree, this, CustomerType.CAR);
		break;
	    case 1:
		c = new Customer(semaIdleFree, semaCashFree, this, CustomerType.BIKER);
		break;
	    default:
		c = new Customer(semaIdleFree, semaCashFree, this, CustomerType.FIREBRIGADE);
		break;
	    }
	    subjects.add(c);
	    iv.setX(START_X);
	    if (i == 1)
		iv.setY(START_Y);
	    else
		iv.setY(i * DISTANCE_Y + 15);
	    iv.xProperty().bind(c.getDp());
	    iv.visibleProperty().bind(c.getVisibility());
	    iv.setImage(c.getImage());
	    iv.setFitWidth(WIDTH);
	    iv.setFitHeight(HEIGHT);
	    images.add(iv);
	    paneSimulation.getChildren().add(iv);
	    c.setVisibility(true);
	}
    }

}
