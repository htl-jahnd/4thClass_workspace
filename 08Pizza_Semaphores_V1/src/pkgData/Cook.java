package pkgData;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Cook extends Thread implements ISubject
{

    static final int MAX_COOKING_DURATION = 1500;
    String name;
    Semaphore semaBarFree;
    Semaphore semaPizzaOnBar;
    boolean isEnd;
    Bar bar;

    public Cook(String name, Semaphore semaBarFree, Semaphore semaPizzaOnBar, boolean isEnd, Bar bar)
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
		 
		semaBarFree.acquire();
		Random r = new Random();
		 int dur = r.nextInt(MAX_COOKING_DURATION);
		Pizza p = new Pizza("Pizza");
		System.out.println(name + " is cooking " + p.getName());
		bar.addPizza(p);
		Thread.sleep(dur);
		semaPizzaOnBar.release();
		
	    } catch (Exception ex)
	    {
		ex.printStackTrace();
	    } finally
	    {
	    }
	}

    }

    @Override
    public String toString()
    {
	return "Cook [name=" + name + ", semaBarFree=" + semaBarFree + ", semaPizzaOnBar=" + semaPizzaOnBar + ", isEnd="
		+ isEnd + ", bar=" + bar + "]";
    }

    @Override
    public void setEnd()
    {
	isEnd = true;
	semaBarFree.release(2);

    }

    @Override
    public void start()
    {
	Thread th = new Thread(this);
	th.start();

    }

}
