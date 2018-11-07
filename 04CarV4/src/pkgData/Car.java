package pkgData;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.TreeMap;

public class Car implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private TreeMap<LocalDate, Repair> collRepairs;

    public TreeMap<LocalDate, Repair> getCollRepairs()
    {
	return collRepairs;
    }

    public void setCollRepairs(TreeMap<LocalDate, Repair> collRepairs)
    {
	this.collRepairs = collRepairs;
    }

    public void addRepair(Repair r) throws Exception
    {
	if (collRepairs.containsKey(r.getDateRepair()))
	{
	    throw new Exception("Date " + r.getDateRepair() + " already used");
	} else
	    collRepairs.put(r.getDateRepair(), r);
    }

    public void removeRepair(Repair r) throws Exception
    {
	if (!collRepairs.containsKey(r.getDateRepair()))
	{
	    throw new Exception("Date " + r.getDateRepair() + " not used");
	} else
	{
	    collRepairs.remove(r.getDateRepair(), r);
	}
    }

    public void updateRepair(Repair r) throws Exception
    {
	if (!collRepairs.containsKey(r.getDateRepair()))
	    throw new Exception("Date " + r.getDateRepair() + " not used");
	else
	{
	    collRepairs.get(r.getDateRepair()).setTextRepair(r.getTextRepair());
	    collRepairs.get(r.getDateRepair()).setAmountRepair(r.getAmountRepair());
	}
    }

    public Collection<Repair> getRepairs()
    {
	return collRepairs.values();
    }

    public Car()
    {
	this(-99, "no car");
    }

    public Car(int id, String name)
    {
	super();
	this.id = id;
	this.name = name;
	collRepairs = new TreeMap<>();
    }

    public int getId()
    {
	return id;
    }

    public void setId(int id)
    {
	this.id = id;
    }

    public String getName()
    {
	return name;
    }

    public void setName(String name)
    {
	this.name = name;
    }

    @Override
    public int hashCode()
    {
	final int prime = 31;
	int result = 1;
	result = prime * result + id;
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj)
    {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Car other = (Car) obj;
	if (id != other.id)
	    return false;

	return true;
    }

    @Override
    public String toString()
    {
	return "Car [id=" + id + ", name=" + name + "]";
    }

}
