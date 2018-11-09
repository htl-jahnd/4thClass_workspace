package pkgData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class Database
{

    private static String connectionString;
    private static final String USER = "d4b12";
    private static final String PWD_DB = "d4b";
    private static Database instance = null;
    private static Connection conn = null;
    private static boolean isConnectionSet = false;
    private static ArrayList<Question> collQuestions;
    private static ArrayList<Answer> collAnswers;

    public Collection<Quiz> selectAllQuizzes() throws SQLException
    {
	ArrayList<Quiz> tests = new ArrayList<Quiz>();
	String select = "SELECT t.tid tid, t.tname tname FROM test t";
	PreparedStatement stmt = conn.prepareStatement(select);
	ResultSet rs = stmt.executeQuery();
	while (rs.next())
	{
	    tests.add(new Quiz(rs.getString("tid"), rs.getString("tname")));
	}
	return tests;
    }

    public void selectAllQuestions(Quiz t) throws SQLException
    {
	ArrayList<Question> questions = new ArrayList<Question>();
	String select = "SELECT * FROM frage WHERE tid LIKE ?";
	PreparedStatement stmt = conn.prepareStatement(select);
	stmt.setString(1, t.getId());
	ResultSet rs = stmt.executeQuery();
	while (rs.next())
	{
	    questions.add(new Question(rs.getString("tid"), rs.getInt("fnr"), rs.getString("FText")));
	}
	collQuestions = questions;
    }

    public void selectAllAnswers(Question q) throws SQLException
    {
	ArrayList<Answer> answers = new ArrayList<Answer>();
	String select = "select a.tid, a.fnr, a.anr, a.atext, f.anrOk from antwort a inner join frage f on a.fnr = f.fnr where a.tid LIKE ? and f.tid LIKE ? AND f.fnr = ?";
	PreparedStatement stmt = conn.prepareStatement(select);
	stmt.setString(1, q.getTestId());
	stmt.setString(2, q.getTestId());
	stmt.setInt(3, q.getQuestionId());
	ResultSet rs = stmt.executeQuery();
	while (rs.next())
	{
	    int corrAnsw = rs.getInt("anrOk");
	    int answNr = rs.getInt("anr");
	    answers.add(new Answer(rs.getString("tid"), rs.getInt("FNr"), answNr, rs.getString("AText"),
		    (corrAnsw == answNr)));
	}
	collAnswers.clear();
	collAnswers.addAll(answers);
	Question tmpQ = collQuestions.stream().filter(c -> c.getQuestionId() == q.getQuestionId()).findAny().get();
	tmpQ.setCollAnswers(collAnswers);
	q.setCollAnswers(collAnswers);
    }

    public int selectMaxParticipantId() throws SQLException
    {
	String select = "SELECT MAX(kid) FROM kandidat";
	int ret = 0;
	PreparedStatement stmt = conn.prepareStatement(select);
	ResultSet rs = stmt.executeQuery();
	while (rs.next())
	{
	    ret = rs.getInt(1);
	}
	return ret + 1;
    }

    public void addParticipant(Participant p) throws SQLException
    {
	String insert = "INSERT INTO kandidat VALUES (?,?,?)";
	PreparedStatement stmt = conn.prepareStatement(insert);
	stmt.setInt(1, p.getParticipantId());
	stmt.setString(2, p.getParticipantName());
	stmt.setString(3, p.getParticipantName());

	stmt.executeUpdate();
    }

    public Question getNextQuestion(Question curr)
    {
	if (curr != null)
	{
	    Question tmpQ = collQuestions.stream().filter(c -> c.getQuestionId() == curr.getQuestionId() + 1).findAny()
		    .get();
	    return tmpQ;
	} else
	    return collQuestions.get(0);
    }
    
    
    public void insertAnswer(Answer a, Participant p) throws SQLException {
	String insert = "INSERT INTO testantwort VALUES (?,?,?,?)";
	PreparedStatement stmt = conn.prepareStatement(insert);
	stmt.setString(1, a.getTestId());
	stmt.setInt(2, a.getQuestionId());
	stmt.setInt(3, a.getAnswerId());
	stmt.setInt(4, p.getParticipantId());
	stmt.executeUpdate();
    }
    
    public void insertTeilnahme(Quiz q, Participant p) throws SQLException {
	String insert = "INSERT INTO teilnahme VALUES (?,?,SYSDATE)";
	PreparedStatement stmt = conn.prepareStatement(insert);
	stmt.setString(1, q.getId());
	stmt.setInt(2, p.getParticipantId());
	stmt.executeUpdate();
    }

    public Collection<Answer> getAnswers()
    {
	return collAnswers;
    }

    private Database()
    {

    }

    public static Database newInstance(String ipAddres) throws Exception // TODO könnte mit "reconnect" zusammengefasst
    // werden
    {
	if (instance == null)
	{
	    instance = new Database();
	    connectionString = ipAddres;
	    createConnection();
	}
	return instance;
    }

    public static Database reconnect(String ipAddres) throws Exception
    {
	if (instance == null)
	{
	    instance = new Database();
	}
	connectionString = ipAddres;
	createConnection();
	return instance;
    }

    private static void createConnection() throws Exception
    {
	if (conn == null)
	{
	    DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
	}
	StringBuilder bu = new StringBuilder("jdbc:oracle:thin:@").append(connectionString).append(":1521:ora11g");
	connectionString = bu.toString();
	DriverManager.setLoginTimeout(3);
	conn = DriverManager.getConnection(connectionString, USER, PWD_DB);
	conn.setAutoCommit(true);
	isConnectionSet = true;
	collQuestions = new ArrayList<Question>();
	collAnswers = new ArrayList<Answer>();
    }

    public void commit() throws Exception
    {
	conn.commit();
    }

    public void rollback() throws Exception
    {
	conn.rollback();
    }

    public void closeConnection() throws Exception
    {
	conn.close();
    }

}
