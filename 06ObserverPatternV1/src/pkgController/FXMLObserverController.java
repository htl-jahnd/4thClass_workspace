package pkgController;

import java.net.URL;
import java.util.Observer;
import java.util.ResourceBundle;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.*;
import javafx.scene.control.*;
import pkgData.Database;
import pkgData.Song;

public class FXMLObserverController implements Initializable, Observer
{
    @FXML
    private ListView<Song> listViewSongs;

    @FXML
    private Label lblMessage;
    
    private ObservableList<Song> listSongs;
    private Database db;
    
    @SuppressWarnings("deprecation")
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
	listSongs = FXCollections.observableArrayList();
	listViewSongs.setItems(listSongs);
	db = Database.newInstance();
	db.addObserver(this);
	listSongs.setAll(db.getAllSongs());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void update(java.util.Observable o, Object arg)
    {
	listSongs.setAll(db.getAllSongs());
    }
}
