package pkgMain;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import pkgData.Bar;
import pkgData.Cook;
import pkgData.Customer;
import pkgData.ISubject;

public class Pizza_MainAppl
{


    public static void main(String[] args)
    {
	Semaphore onBar = new Semaphore(0);
	Semaphore barFree = new Semaphore(3);
	
	Bar b = Bar.newInstance();
	ArrayList<ISubject> collSubjects = new ArrayList<>();
	collSubjects.add(new Cook("Cook 1", barFree, onBar, false, b));
	collSubjects.add(new Cook("Cook 2", barFree, onBar, false, b));
	
	collSubjects.add(new Customer("Customer 1", barFree, onBar, false, b));
	collSubjects.add(new Customer("Customer 2", barFree, onBar, false, b));

	
	for(ISubject s : collSubjects) {
	    s.start();
	}
	
	for(ISubject s : collSubjects) {
	    s.setEnd();
	}
    }

}
