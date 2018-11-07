package pkgCompare;

import java.util.Comparator;
import java.util.TreeSet;

public class Main
{

    public static void main(String[] args)
    {
	// Default sort

	TreeSet<Animal> set = new TreeSet<Animal>();
	set.add(new Animal("doggo", 12));
	set.add(new Animal("brido", 13));
	set.add(new Animal("esel", 11));
	System.out.println("Default sort");
	for (Animal a : set)
	{
	    System.out.println(a.getName());
	}

	System.out.println("--------------------");

	// Reverse sort

	// TreeSet<Animal> set1 = new TreeSet<Animal>(new AnimalComparator()); // with
	// previous defined Comparator class
	TreeSet<Animal> set1 = new TreeSet<Animal>(new Comparator<Animal>() { // with anonymous Comparator class
	    @Override
	    public int compare(Animal o1, Animal o2)
	    {
		return o2.getName().compareTo(o1.getName());
	    }
	});
	set1.addAll(set);
	System.out.println("Reverse sort");
	for (Animal a : set1)
	{
	    System.out.println(a.getName());
	}
    }

}
