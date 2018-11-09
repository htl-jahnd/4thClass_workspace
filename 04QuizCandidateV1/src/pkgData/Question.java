package pkgData;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String testId;
    private int questionId;
    private String text;
    private ArrayList<Answer> collAnswers;
    
    public Question(String testId, int frageId, String text)
    {
	super();
	this.testId = testId;
	this.questionId = frageId;
	this.text = text;
    }



    @Override
    public String toString()
    {
	return questionId + "...." + text;
    }
    
    public ArrayList<Answer> getCollAnswers()
    {
        return collAnswers;
    }

    public void setCollAnswers(ArrayList<Answer> collAnswers)
    {
        this.collAnswers = collAnswers;
    }

    public String getTestId()
    {
	return testId;
    }

    public void setTestId(String testId)
    {
	this.testId = testId;
    }

    public int getQuestionId()
    {
	return questionId;
    }

    public void setQuestionId(int questionId)
    {
	this.questionId = questionId;
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
