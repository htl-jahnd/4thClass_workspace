package pkgData;

public class Answer
{
	private String testId;
	private int questionId;
	private int answerId;
	private String text;
	
	public Answer(String testId, int questionId, int answerId, String text)
	{
		super();
		this.testId = testId;
		this.questionId = questionId;
		this.answerId = answerId;
		this.text = text;
	}
	@Override
	public String toString()
	{
		return "Answer [testId=" + testId + ", questionId=" + questionId + ", answerId=" + answerId + ", text=" + text
				+ "]";
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
	public int getAnswerId()
	{
		return answerId;
	}
	public void setAnswerId(int answerId)
	{
		this.answerId = answerId;
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
