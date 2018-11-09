package pkgData;

public class Quiz
{

    private String id;
    private String text;

    public Quiz(String id, String text)
    {
	super();
	this.id = id;
	this.text = text;
    }

    @Override
    public String toString()
    {
	return text;
    }

    public String getId()
    {
	return id;
    }

    public void setId(String id)
    {
	this.id = id;
    }

    public String getText()
    {
	return text;
    }

    public void setText(String text)
    {
	this.text = text;
    }
}
