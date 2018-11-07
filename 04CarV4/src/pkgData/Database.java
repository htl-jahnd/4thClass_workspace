package pkgData;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pkgMisc.LocalDateAdapter;

public class Database
{
	private ArrayList<Car> collCars;
	private static Database instance;
	private static final String CARS_FILE_PATH = "cars.bin";
	private static final String JSON_FILE_PATH = "cars.json";

	public static Database newInstance()
	{
		if (instance == null)
			return new Database();
		else
			return instance;
	}

	public void doStoreCarsBin() throws IOException
	{
		FileOutputStream fs = new FileOutputStream(CARS_FILE_PATH);
		ObjectOutputStream os = new ObjectOutputStream(fs);
		os.writeObject(collCars);
		os.flush();
		os.close();
		fs.close();
	}

	@SuppressWarnings("unchecked")
	public void doRestoreCarsBin() throws IOException, ClassNotFoundException
	{
		FileInputStream fs = new FileInputStream(CARS_FILE_PATH);
		ObjectInputStream os = new ObjectInputStream(fs);
		collCars = (ArrayList<Car>) os.readObject();
		os.close();
		fs.close();
	}

	public void doStoreCarsJson() throws IOException
	{
		try (FileWriter fw = new FileWriter(JSON_FILE_PATH))
		{
			GsonBuilder gson = new GsonBuilder().enableComplexMapKeySerialization() // treemap
					.setPrettyPrinting().registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
			gson.create().toJson(collCars, fw);
			fw.flush();
		}
	}

	public void doRestoreCarsJson() throws IOException
	{
		try (FileReader fr = new FileReader(JSON_FILE_PATH))
		{
			GsonBuilder gson = new GsonBuilder().enableComplexMapKeySerialization() // treemap
					.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
			Type collectionType = new TypeToken<ArrayList<Car>>() {
			}.getType();
			collCars = gson.create().fromJson(fr, collectionType);
			setNextRepairId();
		}
	}

	public void setNextRepairId()
	{
		for (Car car : collCars)
		{
			for (Repair repair : car.getRepairs())
			{
				if (repair.getIdRepair() > Repair.getNumberOfRepairs())
				{
					Repair.setNumberOfRepairs(repair.getIdRepair());
				}
			}
		}
	}

	private Database()
	{
		collCars = new ArrayList<Car>();
	}

	public void addCar(Car c) throws Exception
	{
		if (collCars.contains(c))
			throw new Exception("An object with this id already exists");
		collCars.add(c);
	}

	public void removeCar(Car c) throws Exception
	{
		if (!collCars.contains(c))
			throw new Exception("An object with this id does not exist");
		collCars.remove(c);
	}

	public void updateCar(Car c) throws Exception
	{
		if (!collCars.contains(c))
			throw new Exception("An object with this id does not exist");
		int idx = collCars.lastIndexOf(c);
		collCars.get(idx).setName(c.getName());
		;
	}

	public Collection<Car> getCollCars()
	{
		return collCars;
	}

	public Collection<Repair> getCollRepairs(Car c)
	{
		return c.getRepairs();
	}

	public int getNumberOfRepairs()
	{
		return Repair.getNumberOfRepairs();
	}

}
