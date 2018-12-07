package pkgMain;

import java.util.ArrayList;

import pkgData.Animal;
import pkgData.Dog;
import pkgData.Person;

public class Main_Appl
{

    private static ArrayList<Person> listThreadsPerson = new ArrayList<Person>();
    private static ArrayList<Animal> listThreadsAnimal = new ArrayList<Animal>();

    public static void main(String[] args)
    {
	try
	{
	    listThreadsPerson.add(new Person("Person 1", 1, 4));
	    listThreadsPerson.add(new Person("Person 2", 4, 8));
	    listThreadsPerson.add(new Person("Person 3", 8, 12));

	    listThreadsAnimal.add(new Dog("Animal 1", 7, 3));
	    listThreadsAnimal.add(new Dog("Animal 2", 9, 5));
	    startThread();
	    awaitThreads();
	    System.out.println("Threads finished");
	} catch (InterruptedException e)
	{
	    e.printStackTrace();
	}
    }

    private static void startThread()
    {
	for (Person p : listThreadsPerson)
	{
	    p.start();
	}
	for (Animal d : listThreadsAnimal)
	{
	    d.start();
	}
    }

    private static void awaitThreads() throws InterruptedException
    {
	for (Person p : listThreadsPerson)
	{
	    p.join();
	}
	for (Animal d : listThreadsAnimal)
	{
	    d.join();
	}
    }

}
