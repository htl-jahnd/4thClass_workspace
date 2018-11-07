package pkgData;

public class Question
{
	private String testId;
	private int questionId;
	private String text;
	private int rightAnswerId;
	
	
	
	public Question(String testId, int frageId, String text, int rightAnswerId)
	{
		super();
		this.testId = testId;
		this.questionId = frageId;
		this.text = text;
		this.rightAnswerId = rightAnswerId;
	}
	@Override
	public String toString()
	{
		return "Question [testId=" + testId + ", frageId=" + questionId + ", text=" + text + ", rightAnswerId="
				+ rightAnswerId + "]";
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
	public int getRightAnswerId()
	{
		return rightAnswerId;
	}
	public void setRightAnswerId(int rightAnswerId)
	{
		this.rightAnswerId = rightAnswerId;
	}
}
