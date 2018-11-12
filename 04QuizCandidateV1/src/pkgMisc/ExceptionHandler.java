package pkgMisc;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ExceptionHandler implements IStaticStrings
{

    private static Boolean debug = true;

    public static Boolean getDebug()
    {
	return debug;
    }

    public static void setDebug(Boolean debug)
    {
	ExceptionHandler.debug = debug;
    }

    public static void hanldeUnexpectedException(Exception ex)
    {
	Alert alert = new Alert(AlertType.ERROR);
	alert.setTitle("Error");
	alert.setHeaderText("And unexpected error occured");
	alert.setContentText(ex.getClass() + ": " + ex.getMessage());
	alert.showAndWait();

	Logger.doWriteLogFile(ex);
	if (debug)
	    ex.printStackTrace();
    }

    public static void hanldeExpectedException(String title, Exception ex)
    {
	Alert alert = new Alert(AlertType.ERROR);
	alert.setTitle("Error");
	alert.setHeaderText(title);
	alert.setContentText(ex.getMessage());
	alert.showAndWait();

	Logger.doWriteLogFile(ex);
	if (debug)
	    ex.printStackTrace();
    }

}
