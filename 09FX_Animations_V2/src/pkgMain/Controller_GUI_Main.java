package pkgMain;

import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.util.Duration;
import pkgData.Worker;

public class Controller_GUI_Main {

    @FXML
    private Button btnStart;

    @FXML
    private Button btnShowResult;

    @FXML
    private Button btnStop;

    @FXML
    private Label lblMessage;

    @FXML
    private Label lblMessage2;

    @FXML
    private ImageView imgCar1;

    private Worker worker;
    private static final double HEIGHT = 50;
    private static final double WIDTH=100;
    private static final double X_COO = 100;
    private static final double Y_COO = 350;
    private static final double ANIMATION_DURATION = 1000;
    private double oldXCoo = X_COO;
    private ImageView iv = null;
    private static Stage stage;
    
    @FXML
    void onSelectButton(ActionEvent event) {
	if(event.getSource().equals(btnStart)) {
	    worker = new Worker(this);
	    lblMessage2.textProperty().bind(worker.getSp());
	    imgCar1.xProperty().bind(worker.getDp());
	    createCar2();
	    
	    new Thread(worker).start();
	    lblMessage.setText("worker started");

	}else if(event.getSource().equals(btnStop)) {
	    worker.cancel();
	    lblMessage.setText("worker stopped with value: "+worker.getValue());
	}else if(event.getSource().equals(btnShowResult)) {
	    lblMessage.setText("   "+worker.getValue());
	}
    }

    private void createCar2()
    {
	Pane myPane =(Pane) (stage.getScene().getRoot());
	iv= new ImageView();
	iv.setImage(new Image(getClass().getResourceAsStream("/ressources/car2.png")));
	iv.setFitHeight(HEIGHT);
	iv.setFitWidth(WIDTH);
	iv.setX(X_COO);
	iv.setY(Y_COO);
	myPane.getChildren().add(iv);
    }
    
    public void moveCar2(double newXCoo) {
	Path path = new Path();
	path.getElements().add(new MoveTo(oldXCoo, Y_COO));
	path.getElements().add(new HLineTo(newXCoo));
	oldXCoo = newXCoo;
	PathTransition ptr = new PathTransition();
	ptr.setDuration(Duration.millis(ANIMATION_DURATION));
	ptr.setPath(path);
	ptr.setNode(iv);
	ptr.play();
	
    }

    public static Stage getStage()
    {
        return stage;
    }

    public static void setStage(Stage stage)
    {
        Controller_GUI_Main.stage = stage;
    }

    public void handleException(Exception ex) {
	lblMessage.setText(ex.getMessage());
	ex.printStackTrace();
    }
    
}
