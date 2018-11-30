package pkgController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pkgData.Database;
import pkgData.Song;

public class FXMLAdministrationController implements Initializable{
    @FXML
    private Label lblMessage;

    @FXML
    private TextField txtTitle;

    @FXML
    private TextField txtText;

    @FXML
    private MenuItem menuCreateObserver;

    @FXML
    private MenuItem menuInsertSong;
    
    @FXML
    private MenuItem menuDeleteSong;
    
    Database db = Database.newInstance();
    private int numberOfObservers;
    
    @FXML
    void onSelectCreateObserver(ActionEvent event) {
	try
	{
	    Stage newStage = new Stage();
	    Parent newRoot = FXMLLoader.load(getClass().getResource("/pkgMain/ressources/FXML_Listener.fxml"));
	    Scene newScene = new Scene(newRoot);
	    newStage.setScene(newScene);
	    newStage.setTitle("Listener of songs #"+ ++numberOfObservers);
	    
	    newStage.initModality(Modality.NONE);
	    newStage.show();
	    lblMessage.setText("New Observer created");
	} catch (IOException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
    }

    @FXML
    void onSelectMenuSong(ActionEvent event) {
        try {
            if (event.getSource().equals(this.menuInsertSong)) {
                Song s = new Song(txtTitle.getText(), txtText.getText());
                db.addSong(s);
                lblMessage.setText("inserted: " + s);
            } else
            if (event.getSource().equals(this.menuDeleteSong)) {
                db.deleteFirstSong();
                lblMessage.setText("deleted 1st song");
            }
        } catch (Exception ex) {
            lblMessage.setText("error: " + ex.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
  
   
}
