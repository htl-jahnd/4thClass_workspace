package pkgData;

import java.util.ArrayList;

public class Quiz
{

    private String id;
    private String text;
    private ArrayList<Question> collQuestions;

    public Quiz(String id, String text)
    {
	super();
	this.id = id;
	this.text = text;
	this.collQuestions = new ArrayList<Question>();
    }

    @Override
    public String toString()
    {
	StringBuilder sb = new StringBuilder(id);
	sb.append("...").append(text);
	return sb.toString();
    }

    public ArrayList<Question> getCollQuestions()
    {
	return collQuestions;
    }

    public void setCollQuestions(ArrayList<Question> collQuestions)
    {
	this.collQuestions = collQuestions;
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
