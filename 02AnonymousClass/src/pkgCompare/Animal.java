package pkgCompare;

public class Animal implements Comparable<Animal>
{
    private String name;
    private float weight;

    public float getWeight()
    {
	return weight;
    }

    public void setWeight(float weight)
    {
	this.weight = weight;
    }

    public String getName()
    {
	return name;
    }

    public void setName(String name)
    {
	this.name = name;
    }

    public Animal(String name, float weight)
    {
	super();
	this.name = name;
    }

    @Override
    public int compareTo(Animal o)
    {
	return name.compareTo(o.getName());
    }

}
