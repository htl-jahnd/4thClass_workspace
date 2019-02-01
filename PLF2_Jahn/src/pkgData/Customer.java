package pkgData;

import java.util.Random;
import java.util.concurrent.Semaphore;

import javax.imageio.ImageIO;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import pkgController.MainController;
import pkgMisc.ISimulationValues;

public class Customer extends Thread implements ISubject, ISimulationValues
{

    private Semaphore freeIdle;
    private Semaphore freeCash;
    boolean isEnd = false;
    private DoubleProperty dp = new SimpleDoubleProperty();
    private MainController controller;
    private CustomerType type;
    private final BooleanProperty visibility = new SimpleBooleanProperty();
    private double CustomerCooX = 0;
    private static int counter = 0;
    private int id;

    public Customer(Semaphore freeIdle, Semaphore freeCash, MainController controller, CustomerType type)
    {
	super();
	this.freeIdle = freeIdle;
	this.freeCash = freeCash;
	this.controller = controller;
	this.type = type;
	id = counter;
	counter++;
    }

    public Image getImage() throws Exception
    {
	switch (type)
	{
	case BIKER:
	    return SwingFXUtils.toFXImage(ImageIO.read(getClass().getResourceAsStream("/pkgMain/ressources/bike.png")),
		    null);

	case CAR:
	    return SwingFXUtils.toFXImage(ImageIO.read(getClass().getResourceAsStream("/pkgMain/ressources/car.png")),
		    null);
	case FIREBRIGADE:
	    return SwingFXUtils.toFXImage(
		    ImageIO.read(getClass().getResourceAsStream("/pkgMain/ressources/firebrigade.png")), null);
	}
	throw new Exception("Image not found");

    }

    @Override
    public void run()
    {
	try
	{
	    Random r = new Random();
	    if (type != CustomerType.BIKER)
	    {
		int hungry = r.nextInt((MAX_DURATION_HUNGRY + MIN_DURATION_HUNGRY) + 1) + MIN_DURATION_HUNGRY; // gets
		// hungry
		Thread.sleep(hungry);
	    }
	    logInGUI("Customer " + id + " got hungry");
	    animateCustomerForward(1);
	    freeIdle.acquire();
	    animateCustomerForward(1);
	    int placeOrder = r.nextInt((MAX_DURATION_ORDER - MIN_DURATION_ORDER) + 1) + MIN_DURATION_ORDER;
	    logInGUI("Customer " + id + " places order");
	    Thread.sleep(placeOrder);
	    freeIdle.release();
	    animateCustomerForward(1);
	    if (type != CustomerType.FIREBRIGADE)
	    {
		freeCash.acquire();
		animateCustomerForward(1);
		int pay = r.nextInt((MAX_DURATION_CASH - MIN_DURATION_CASH) + 1) + MIN_DURATION_CASH;
		logInGUI("Customer " + id + " is paying");
		Thread.sleep(pay);
		freeCash.release();
		animateCustomerForward(1);
	    } else
	    {
		animateCustomerForward(2);
	    }
	    int food = r.nextInt((MAX_DURATION_COOK - MIN_DURATION_COOK) + 1) + MIN_DURATION_COOK;
	    logInGUI("Customer " + id + " is eating");
	    Thread.sleep(food);
	    animateCustomerForward(1);
	    this.isEnd = true;
	    logInGUI("Customer " + id + " is finished");
	} catch (Exception ex)
	{
	    Platform.runLater(() -> controller.log("ERROR: " + ex.getMessage()));
	    ex.printStackTrace();
	}
    }

    private void logInGUI(String string)
    {
	Platform.runLater(() -> controller.log(string));

    }

    private void animateCustomerForward(int times) throws InterruptedException
    {
	for (double px = 0; px < (DISTANCE_X * times) / px; px++)
	{
	    CustomerCooX += px;
	    Platform.runLater(() -> setDp(CustomerCooX));
	    Thread.sleep(100);
	}
    }

    @Override
    public void setDp(double prop)
    {
	this.dp.set(prop);

    }

    @Override
    public DoubleProperty getDp()
    {
	return dp;
    }

    @Override
    public void setVisibility(boolean prop)
    {
	this.visibility.set(prop);

    }

    @Override
    public BooleanProperty getVisibility()
    {
	return this.visibility;
    }

    @Override
    public void setEnd()
    {
	this.isEnd = true;

    }

}
