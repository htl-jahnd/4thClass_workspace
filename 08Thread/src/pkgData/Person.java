package pkgData;

import java.util.Random;

public class Person extends Thread
{

    private String personName;
    private int from;
    private int to;

    public Person(String name, int from, int to)
    {
	super();
	this.personName = name;
	this.from = from;
	this.to = to;
    }

    @Override
    public void run()
    {
	try
	{
	    while (from < to)
	    {
		System.out.println(this.getClass() + ":  " + personName + "   " + to);
		from++;
		Random r = new Random();
		sleep(r.nextInt(4) * 1000);
	    }
	} catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    @Override
    public String toString()
    {
	return "Person [name=" + personName + ", from=" + from + ", to=" + to + "]";
    }

    public String getPersonName()
    {
	return personName;
    }

    public void setPersonName(String personName)
    {
	this.personName = personName;
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
