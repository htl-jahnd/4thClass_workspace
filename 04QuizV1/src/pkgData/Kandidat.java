package pkgData;

public class Kandidat
{
    private int number;
    private String name;
    private String school;

    public Kandidat(int number, String name, String school)
    {
	super();
	this.number = number;
	this.name = name;
	this.school = school;
    }

    public int getNumber()
    {
	return number;
    }

    public void setNumber(int number)
    {
	this.number = number;
    }

    public String getName()
    {
	return name;
    }

    public void setName(String name)
    {
	this.name = name;
    }

    public String getSchool()
    {
	return school;
    }

    public void setSchool(String school)
    {
	this.school = school;
    }

    @Override
    public String toString()
    {
	return "Kandidat [number=" + number + ", name=" + name + ", school=" + school + "]";
    }

}
