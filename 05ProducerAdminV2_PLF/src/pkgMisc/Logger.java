package pkgMisc;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Logger implements IStaticStrings
{
    static void doWriteLogFile(Exception e)
    {
	try
	{
	    File f = new File(LOG_FILE_PATH);
	    if (!f.exists())
		f.createNewFile();
	    BufferedWriter out = new BufferedWriter(new FileWriter(LOG_FILE_PATH, true));
	    StackTraceElement[] ste = e.getStackTrace();
	    out.write(LocalDateTime.now() + ": Message: " + e.getMessage() + ", Trace: ");
	    for (int i = 0; i < 5; i++)
	    {
		out.write("    " + ste[i] + "\n");
	    }
	    out.flush();
	    out.close();
	} catch (IOException ex)
	{
	    Alert alert = new Alert(AlertType.ERROR);
	    alert.setTitle("Error");
	    alert.setHeaderText("And unexpected error occured");
	    alert.setContentText(ex.getMessage());
	    alert.showAndWait();
	}
    }

    public static void doClearLogFile()
    {
	try
	{
	    BufferedWriter out = new BufferedWriter(new FileWriter(LOG_FILE_PATH));
	    out.write("");
	    out.flush();
	    out.close();
	} catch (Exception ex)
	{
	    ExceptionHandler.hanldeUnexpectedException(ex);
	}
    }

    public static void viewLogFile() throws IOException
    {
	File f = new File(LOG_FILE_PATH);
	if (!f.exists())
	    throw new FileNotFoundException("File " + LOG_FILE_PATH + " does not exist");
	else if (!f.isFile())
	    throw new FileNotFoundException("File " + LOG_FILE_PATH + " is no file");
	Desktop.getDesktop().open(new File(LOG_FILE_PATH));
    }
}
