package pkgData;

import java.util.ArrayList;

public class Participant
{
    private int participantId;
    private String participantName;
    private String participantSchoolType;
    private ArrayList<Quiz> collQuizzes;

    public Participant(int participantId, String participantName, String participantSchoolType)
    {
	super();
	this.participantId = participantId;
	this.participantName = participantName;
	this.participantSchoolType = participantSchoolType;
    }

    @Override
    public String toString()
    {
	return "Participant [participantId=" + participantId + ", participantName=" + participantName
		+ ", participantSchoolType=" + participantSchoolType + "]";
    }

    public ArrayList<Quiz> getCollQuizzes()
    {
	return collQuizzes;
    }

    public void setCollQuizzes(ArrayList<Quiz> collQuizzes)
    {
	this.collQuizzes = collQuizzes;
    }

    public int getParticipantId()
    {
	return participantId;
    }

    public void setParticipantId(int participantId)
    {
	this.participantId = participantId;
    }

    public String getParticipantName()
    {
	return participantName;
    }

    public void setParticipantName(String participantName)
    {
	this.participantName = participantName;
    }

    public String getParticipantSchoolType()
    {
	return participantSchoolType;
    }

    public void setParticipantSchoolType(String participantSchoolType)
    {
	this.participantSchoolType = participantSchoolType;
    }

}
