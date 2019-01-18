package pkgMain;

import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.VLineTo;
import javafx.util.Duration;

public class Controller_GUI_Main
{

    @FXML
    private Button btnStart;

    @FXML
    private Button btnStop;

    @FXML
    private ImageView imgCar;

    @FXML
    private Label lblMessage;

    /**************************************
     ********** NON FX-VARIABLES **********
     **************************************/

    private FadeTransition ft = null;
    private PathTransition pathTransition = null;
    private static final double START_X = 20d;
    private static final double START_Y = 20d;
    private static final double WIDTH = 600d;
    private static final double HEIGHT = 250d;
    private static final double WIDTH_RTN = 400d;
    private static final double HEIGHT_RTN = 200d;

    /**************************************
     ************ FX-FUNCTIONS ************
     **************************************/

    @FXML
    void onSelectButton(ActionEvent event)
    {
	try
	{
	    if (event.getSource().equals(btnStart))
	    {
		doAnimationFading();
		doAnimationMoving();
	    } else if (event.getSource().equals(btnStop))
	    {
		doAnimationStop();
	    }
	} catch (Exception e)
	{
	    doHandleException(e);
	}
    }

    /**************************************
     ********** NON FX-FUNCTIONS **********
     **************************************/

    private void doAnimationFading() throws Exception
    {
	ft = new FadeTransition(Duration.millis(3000), imgCar);
	ft.setFromValue(1.0);
	ft.setToValue(0.1);
	ft.setCycleCount(Timeline.INDEFINITE);
	ft.setAutoReverse(true);
	ft.play();
    }

    private void doAnimationMoving() throws Exception
    {
	Path path = new Path();
	path.getElements().add(new MoveTo(START_X, START_Y));
	path.getElements().add(new HLineTo(START_X + WIDTH));
	path.getElements().add(new VLineTo(START_Y + HEIGHT));
	path.getElements().add(new HLineTo(START_X + WIDTH - WIDTH_RTN));
	path.getElements().add(new ArcTo(1d, 1d, 0d, START_X, START_Y + HEIGHT_RTN, false, false));
	path.getElements().add(new VLineTo(START_Y));
	if (pathTransition == null)
	{
	    pathTransition = new PathTransition();
	    pathTransition.setDuration(Duration.millis(4000));
	    pathTransition.setPath(path);
	    pathTransition.setNode(imgCar);
	    pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
	    pathTransition.setCycleCount(Timeline.INDEFINITE);
	    pathTransition.setAutoReverse(false);
	  
	}
	  pathTransition.play();
    }

    private void doHandleException(Exception ex)
    {
	lblMessage.setText(ex.getMessage());
	ex.printStackTrace();
    }

    private void doAnimationStop()
    {
	ft.stop();
	pathTransition.stop();
    }

}
