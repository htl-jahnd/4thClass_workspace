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
import pkgMisc.EventDBChanged;
import pkgMisc.EventDBChangedListener;

public class FXMLListenerController implements Initializable, EventDBChangedListener
{
    @FXML
    private ListView<Song> listViewSongs;

    @FXML
    private Label lblMessage;

    private ObservableList<Song> listSongs;
    private Database db;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
	listSongs = FXCollections.observableArrayList();
	listViewSongs.setItems(listSongs);
	db = Database.newInstance();
	db.addEventDBChangedListener(this);
	listSongs.setAll(db.getAllSongs());
    }

    @Override
    public void handleEventDBChangedEvent(EventDBChanged event)
    {
	listSongs.setAll(db.getAllSongs());
	lblMessage.setText(event.getType().toString());
    }

}
