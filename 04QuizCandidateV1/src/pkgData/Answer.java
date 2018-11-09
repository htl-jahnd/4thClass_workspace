package pkgData;

public class Answer
{
    private String testId;
    private int questionId;
    private int answerId;
    private String answerText;
    private boolean isCorrect;
    private boolean isChosen;

    public Answer(String testId, int questionId, int answerId, String answerText, boolean correct)
    {
	this(testId, questionId, answerId, answerText, correct, false);
    }

    public Answer(String testId, int questionId, int answerId, String answerText, boolean correct, boolean choosen)
    {
	super();
	this.testId = testId;
	this.questionId = questionId;
	this.answerId = answerId;
	this.answerText = answerText;
	this.isCorrect = correct;
	isChosen = choosen;
    }

    @Override
    public String toString()
    {
	return "Answer [testId=" + testId + ", questionId=" + questionId + ", answerId=" + answerId + ", answerText="
		+ answerText + ", correct=" + isCorrect + "]";
    }

    public boolean isChosen()
    {
	return isChosen;
    }

    public void setChosen(boolean isChosen)
    {
	this.isChosen = isChosen;
    }

    public boolean getIsCorrect()
    {
	return isCorrect;
    }

    public void setIsCorrect(boolean correct)
    {
	this.isCorrect = correct;
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

    public String getAnswerText()
    {
	return answerText;
    }

    public void setAnswerText(String text)
    {
	this.answerText = text;
    }

}
