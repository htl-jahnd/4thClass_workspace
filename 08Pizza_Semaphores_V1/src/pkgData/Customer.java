package pkgData;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Customer extends Thread implements ISubject
{
    static final int MAX_EATING_DURATION = 3000;
    String name;
    Semaphore semaBarFree;
    Semaphore semaPizzaOnBar;
    boolean isEnd;
    Bar bar;

    @Override
    public String toString()
    {
	return "Customer [name=" + name + ", semaBarFree=" + semaBarFree + ", semaPizzaOnBar=" + semaPizzaOnBar
		+ ", isEnd=" + isEnd + ", bar=" + bar + "]";
    }

    public Customer(String name, Semaphore semaBarFree, Semaphore semaPizzaOnBar, boolean isEnd, Bar bar)
    {
	super();
	this.name = name;
	this.semaBarFree = semaBarFree;
	this.semaPizzaOnBar = semaPizzaOnBar;
	this.isEnd = isEnd;
	this.bar = bar;
    }

    @Override
    public void run()
    {
	while (!isEnd)
	{
	    try
	    {

		semaPizzaOnBar.acquire();
		Pizza p = bar.getPizzas().poll();
		if (p != null)
		{
		    Random r = new Random();
		    int dur = r.nextInt(MAX_EATING_DURATION);
		    System.out.println(name + " is eating " + p.getName());
		    Thread.sleep(dur);
		    semaBarFree.release();
		}
		// Pizza.decrementNumberOfPizzas();
	    } catch (Exception ex)
	    {
		ex.printStackTrace();
	    } finally
	    {
		semaPizzaOnBar.release();
	    }

	}
    }

    @Override
    public void setEnd()
    {
	isEnd = true;

    }

    @Override
    public void start()
    {
	Thread th = new Thread(this);
	th.start();

    }

}
