package pkgData;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import pkgMain.Controller_GUI_Main;

public class Worker extends Task<String>
{

    static final int MAX = 100;
    private final StringProperty sp = new SimpleStringProperty();
    private final DoubleProperty dp = new SimpleDoubleProperty();
    private Controller_GUI_Main controller = null;

    public Worker(Controller_GUI_Main controller)
    {
	this.controller = controller;
    }

    @Override
    protected String call() throws Exception
    {

	try
	{
	    for (int i = 1; i <= MAX && !isCancelled(); i++)
	    {
		Thread.sleep(300);
		updateValue("---" + i + "---");
		String strTemp = "==>" + i + "<==";
		Platform.runLater(() -> setSp(strTemp));
		final int tmpI = i + 20;
		Platform.runLater(() -> setDp(tmpI));
		if (i % 10 == 0)
		    Platform.runLater(() -> controller.moveCar2(dp.getValue() * 5));

	    }
	} catch (Exception ex)
	{
	    Platform.runLater(() -> controller.handleException(ex));
	} finally
	{
	    return "x-x-x-x-x"; // Wegen return type = String bzw Task<String>
	}
    }

    public StringProperty getSp()
    {
	return sp;
    }

    private void setSp(String value)
    {
	sp.set(value);
    }

    public DoubleProperty getDp()
    {
	return dp;
    }

    public void setDp(int value)
    {
	dp.set(value);
    }

}
