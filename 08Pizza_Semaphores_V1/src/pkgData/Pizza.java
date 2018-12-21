package pkgData;

public class Pizza
{

    private static int numberOfPizza;
    private String name;
    
    public String getName()
    {
        return name;
    }

    public Pizza(String name)
    {
	super();
	synchronized(getClass() ) {
	    numberOfPizza++;
	    this.name = name+"_"+numberOfPizza;
	}
    }
    
    @Override
    public String toString()
    {
	return "Pizza [name=" + name + "]";
    }
    


}
