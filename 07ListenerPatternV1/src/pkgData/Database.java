package pkgData;

import java.util.ArrayList;
import java.util.Collection;

import java.util.Observable;

import pkgMisc.EventDBChanged;
import pkgMisc.EventDBChangedListener;
import pkgMisc.EventDBType;

public class Database 
{

    private static Database INSTANCE;
    private final ArrayList<Song> collSongs = new ArrayList<>();
    private final ArrayList<EventDBChangedListener> collListener = new ArrayList<>();

    public void addSong(Song s) throws Exception
    {
	collSongs.add(s);
	this.notifyListeners(EventDBType.INSERT);
    }

    public Collection<Song> getAllSongs()
    {
	return collSongs;
    }

    public void deleteFirstSong() throws Exception
    {
	if (collSongs.size() > 0)
	{
	    Song s = collSongs.get(0);
	    collSongs.remove(0);
	    this.notifyListeners(EventDBType.DELETE);
	} else
	{
	    throw new Exception("nothing to delete");
	}
    }
    
    public void addEventDBChangedListener(EventDBChangedListener o) {
	collListener.add(o);
    }
    
    private void notifyListeners(EventDBType type) {
	for(EventDBChangedListener l : collListener) {
	    l.handleEventDBChangedEvent(new EventDBChanged(this, type));
	}
    }

    private Database()
    {

    }

    public static Database newInstance()
    {
	if (INSTANCE == null)
	{
	    INSTANCE = new Database();
	}
	return INSTANCE;
    }

   
}
