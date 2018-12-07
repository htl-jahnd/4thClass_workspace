package pkgData;

import java.util.Random;

public abstract class Animal extends Thread
{
    private String animalName;
    private int from;
    private int to;

    public Animal(String name, int from, int to)
    {
	super();
	this.animalName = name;
	this.from = from;
	this.to = to;
    }

    @Override
    public String toString()
    {
	return "Animal [name=" + animalName + ", from=" + from + ", to=" + to + "]";
    }

    @Override
    public void run() {
	try
	{
	    while (to<from) {
	        System.out.println(this.getClass()+":  "+animalName+ "   "+to);
	        to++;
	        Random r = new Random();
	        sleep(r.nextInt(2)*1000);
	    }
	} catch (Exception e)
	{
	    e.printStackTrace();
	}
    }
    
    public String getAnimalName()
    {
	return animalName;
    }

    public void setAnimalName(String animalName)
    {
	this.animalName = animalName;
    }

    public int getFrom()
    {
	return from;
    }

    public void setFrom(int from)
    {
	this.from = from;
    }

    public int getTo()
    {
	return to;
    }

    public void setTo(int to)
    {
	this.to = to;
    }

}
