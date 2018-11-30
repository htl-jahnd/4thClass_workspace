package pkgData;

import java.util.ArrayList;
import java.util.Collection;

import java.util.Observable;

public class Database extends Observable
{

    private static Database INSTANCE;
    private final ArrayList<Song> collSongs = new ArrayList<>();

    public void addSong(Song s) throws Exception
    {
	collSongs.add(s);
	this.setChanged();
	this.notifyObservers("Song deleted: " + s);
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
	    this.setChanged();
	    this.notifyObservers("Song deleted: " + s);
	} else
	{
	    throw new Exception("nothing to delete");
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
