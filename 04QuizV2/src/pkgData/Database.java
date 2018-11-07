package pkgData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.GsonBuilder;

import pkgMisc.IStaticStrings;

public class Database implements IStaticStrings
{
    // "jdbc:oracle:thin:@192.168.128.152:1521:ora11g";
    // "dbc:oracle:thin:@212.152.179.117:1521:ora11g";

    // -------------------------------------------------------------------------------------------------------------------------
    // ---------------------------------------- PRIVATE VARIABLES
    // -------------------------------------------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------------------------------------------

    private static String connectionString;
    private static final String USER = "d4b12";
    private static final String PWD_DB = "d4b";
    private static Database instance = null;
    private static Connection conn = null;
    private static ArrayList<Quiz> collQuiz;
    private static ArrayList<Question> collQuestions;
    private static ArrayList<Answer> collAnswers;
    private static ArrayList<Participant> collParticipants;
    private static boolean isConnectionSet = false;

    // -------------------------------------------------------------------------------------------------------------------------
    // ---------------------------------------- GETTER AND SETTER AND CONSTRUCTOR
    // -------------------------------------------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------------------------------------------

    public static boolean isConnectionSet()
    {
	return isConnectionSet;
    }

    public static ArrayList<Answer> getCollAnswers()
    {
	return collAnswers;
    }

    public static void setCollAnswers(ArrayList<Answer> collAnswers)
    {
	Database.collAnswers = collAnswers;
    }

    public static ArrayList<Question> getCollQuestions()
    {
	return collQuestions;
    }

    public static ArrayList<Participant> getCollParticipants()
    {
	return collParticipants;
    }

    public static void setCollParticipants(ArrayList<Participant> collParticipants)
    {
	Database.collParticipants = collParticipants;
    }

    public Collection<Answer> getAnswers()
    {
	return collAnswers;
    }

    public Collection<Participant> getParticipants()
    {
	return collParticipants;
    }

    public Collection<Quiz> getQuizzes()
    {
	return collQuiz;
    }

    public Collection<Question> getQuestions()
    {
	return collQuestions;
    }

    private Database()
    {
    }

    public static String getUser()
    {
	return USER;
    }

    // -------------------------------------------------------------------------------------------------------------------------
    // ---------------------------------------- ALL DATABASE SELECTS
    // -------------------------------------------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------------------------------------------

    public void selectAllParticipants() throws SQLException
    {
	ArrayList<Participant> participants = new ArrayList<Participant>();
	String select = "SELECT * FROM kandidat";
	PreparedStatement stmt = conn.prepareStatement(select);
	ResultSet rs = stmt.executeQuery();
	while (rs.next())
	{
	    participants.add(new Participant(rs.getInt("kid"), rs.getString("kname"), rs.getString("kSchultyp")));
	}
	collParticipants.clear();
	collParticipants.addAll(participants);
    }

    public void selectAllTests(Participant p) throws SQLException
    {
	ArrayList<Quiz> tests = new ArrayList<Quiz>();
	String select = "SELECT t.tid tid, t.tname tname FROM test t inner join teilnahme tl on tl.kid = ? AND tl.tid LIKE t.tid";
	PreparedStatement stmt = conn.prepareStatement(select);
	stmt.setInt(1, p.getParticipantId());
	ResultSet rs = stmt.executeQuery();
	while (rs.next())
	{
	    tests.add(new Quiz(rs.getString("tid"), rs.getString("tname")));
	}
	collQuiz.clear();
	collQuiz.addAll(tests);
	Participant tmp = collParticipants.stream().filter(c -> c.getParticipantId() == p.getParticipantId()).findAny()
		.get();
	tmp.setCollQuizzes(tests);
	p.setCollQuizzes(tests);
    }

    public void selectAllTests() throws SQLException
    {
	ArrayList<Quiz> tests = new ArrayList<Quiz>();
	String select = "SELECT tid, tname FROM test";
	PreparedStatement stmt = conn.prepareStatement(select);
	ResultSet rs = stmt.executeQuery();
	while (rs.next())
	{
	    tests.add(new Quiz(rs.getString("tid"), rs.getString("tname")));
	}
	collQuiz.clear();
	collQuiz.addAll(tests);
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
	collQuestions.clear();
	collQuestions.addAll(questions);
	Quiz tmp = collQuiz.stream().filter(c -> c.getId().equals(t.getId())).findAny().get();
	tmp.setCollQuestions(questions);
	t.setCollQuestions(collQuestions);
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

    public void selectAllAnswers(Question q, Participant p) throws SQLException
    {
	ArrayList<Answer> answers = new ArrayList<Answer>();
	String select = "select distinct a.tid, a.fnr, a.anr, a.atext, f.anrOk, ta.anr testantwort from antwort a inner join frage f on a.fnr = f.fnr "
		+ " inner join testantwort ta on ta.fnr = f.fnr "
		+ " where a.tid LIKE ? and f.tid LIKE ? AND f.fnr = ? AND ta.tid = ? AND ta.knr = ?";
	PreparedStatement stmt = conn.prepareStatement(select);
	stmt.setString(1, q.getTestId());
	stmt.setString(2, q.getTestId());
	stmt.setInt(3, q.getQuestionId());
	stmt.setString(4, q.getTestId());
	stmt.setInt(5, p.getParticipantId());
	ResultSet rs = stmt.executeQuery();
	while (rs.next())
	{
	    int corrAnsw = rs.getInt("anrOk");
	    int answNr = rs.getInt("anr");
	    int chAnsw = rs.getInt("testantwort");
	    answers.add(new Answer(rs.getString("tid"), rs.getInt("FNr"), answNr, rs.getString("AText"),
		    (corrAnsw == answNr), (chAnsw == answNr)));
	}
	collAnswers.clear();
	collAnswers.addAll(answers);
	Question tmpQ = collQuestions.stream().filter(c -> c.getQuestionId() == q.getQuestionId()).findAny().get();
	tmpQ.setCollAnswers(collAnswers);
	q.setCollAnswers(collAnswers);
    }

    // -------------------------------------------------------------------------------------------------------------------------
    // ---------------------------------------- SAVE TO JSON
    // -------------------------------------------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------------------------------------------

    public void quizToJson(Quiz currentQuiz, File fileToSave) throws IOException
    {
	try (FileWriter fw = new FileWriter(fileToSave))
	{
	    GsonBuilder gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting();
	    gson.create().toJson(currentQuiz, fw);
	    fw.flush();
	}
    }

    public void allQuizzesToJson(File fileToSave) throws IOException
    {
	try (FileWriter fw = new FileWriter(fileToSave))
	{
	    GsonBuilder gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting();
	    gson.create().toJson(collQuiz, fw);
	    fw.flush();
	}
    }

    // -------------------------------------------------------------------------------------------------------------------------
    // ---------------------------------------- PARTICIPANT THINGS
    // -------------------------------------------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------------------------------------------

    public void addParticipant(Participant p) throws SQLException
    {
	String insert = "INSERT INTO kandidat VALUES (?,?,?)";
	PreparedStatement stmt = conn.prepareStatement(insert);
	stmt.setInt(1, p.getParticipantId());
	stmt.setString(2, p.getParticipantName());
	stmt.setString(3, p.getParticipantName());

	stmt.executeUpdate();
    }

    public void updateParticipant(Participant p) throws Exception
    {
	String update = "UPDATE kandidat SET kname = ?, kschultyp = ? WHERE kid = ?";
	PreparedStatement st = conn.prepareStatement(update, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

	st.setString(1, p.getParticipantName());
	st.setString(2, p.getParticipantSchoolType());
	st.setInt(3, p.getParticipantId());

	int cnt = st.executeUpdate();
	if (cnt < 1)
	    throw new Exception("Participant with id " + p.getParticipantId() + " does not exist");
    }

    public void deleteParticipant(Participant p) throws Exception
    {
	String delete = "DELETE FROM testantwort WHERE knr LIKE ?";
	PreparedStatement stmt = conn.prepareStatement(delete);
	stmt.setInt(1, p.getParticipantId());
	int cnt = stmt.executeUpdate();

	delete = "DELETE FROM teilnahme WHERE kid LIKE ?";
	stmt = conn.prepareStatement(delete);
	stmt.setInt(1, p.getParticipantId());
	cnt = stmt.executeUpdate();

	delete = "DELETE FROM kandidat WHERE kid LIKE ?";
	stmt = conn.prepareStatement(delete);
	stmt.setInt(1, p.getParticipantId());
	cnt = stmt.executeUpdate();
	if (cnt < 1)
	    throw new Exception("Participant with id " + p.getParticipantId() + " does not exist");
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

    // -------------------------------------------------------------------------------------------------------------------------
    // ---------------------------------------- QUIZ THINGS
    // -------------------------------------------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------------------------------------------

    public void addQuiz(Quiz q) throws SQLException
    {
	// ES KANN NUR EIN QUIZ GEMACHT WERDEN, NICHT EINFACH SO HINZUGEFÜGT WERDEN -->
	// ADD IN TEILANHME SINNLOS
	String insert = "INSERT INTO test VALUES (?,?)";
	PreparedStatement stmt = conn.prepareStatement(insert);
	stmt.setString(1, q.getId());
	stmt.setString(2, q.getText());
	stmt.executeUpdate();
    }

    // public void deleteQuiz(Quiz q, Participant p) throws Exception
    // {
    // String delete = "DELETE FROM testantwort WHERE tid LIKE ? AND knr = ?";
    // PreparedStatement stmt = conn.prepareStatement(delete);
    // stmt.setString(1, q.getId());
    // stmt.setInt(2, p.getParticipantId());
    // int cnt = stmt.executeUpdate();
    // if (cnt < 1)
    // throw new Exception("Quiz with id " + q.getId() + " does not exist or
    // Participant with id "
    // + p.getParticipantId() + " doesnt exist");
    //
    // delete = "DELETE FROM teilnahme WHERE tid LIKE ? AND kid = ?";
    // stmt = conn.prepareStatement(delete);
    // stmt.setString(1, q.getId());
    // stmt.setInt(2, p.getParticipantId());
    // cnt = stmt.executeUpdate();
    // if (cnt < 1)
    // throw new Exception("Quiz with id " + q.getId() + " does not exist or
    // Participant with id "
    // + p.getParticipantId() + " doesnt exist");
    //
    // delete = "DELETE FROM test WHERE tid LIKE ?";
    // stmt = conn.prepareStatement(delete);
    // stmt.setString(1, q.getId());
    // cnt = stmt.executeUpdate();
    // if (cnt < 1)
    // throw new Exception("Quiz with id " + q.getId() + " does not exist");
    //
    // deleteAllQuestions(q);
    // }

    public void deleteQuiz(Quiz q) throws Exception
    {
	String delete = "DELETE FROM testantwort WHERE tid LIKE ?";
	PreparedStatement stmt = conn.prepareStatement(delete);
	stmt.setString(1, q.getId());
	int cnt = stmt.executeUpdate();

	delete = "DELETE FROM teilnahme WHERE tid LIKE ?";
	stmt = conn.prepareStatement(delete);
	stmt.setString(1, q.getId());
	cnt = stmt.executeUpdate();

	delete = "DELETE FROM test WHERE tid LIKE ?";
	stmt = conn.prepareStatement(delete);
	stmt.setString(1, q.getId());
	cnt = stmt.executeUpdate();
	if (cnt < 1)
	    throw new Exception("Quiz with id " + q.getId() + " does not exist");

	deleteAllQuestions(q);
    }

    public void updateQuiz(Quiz q) throws Exception
    {
	String update = "UPDATE test SET tname = ? WHERE tid = ?";
	PreparedStatement st = conn.prepareStatement(update, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

	st.setString(1, q.getText());
	st.setString(2, q.getId());

	int cnt = st.executeUpdate();
	if (cnt < 1)
	    throw new Exception("Quiz with id " + q.getId() + " does not exist");
    }

    private void deleteAllQuizzes(Participant p) throws Exception
    {
	String delete = "delete FROM test where tid in ( select t.tid from test t inner join teilnahme tl on tl.kid = ?  AND tl.tid LIKE t.tid)";
	PreparedStatement stmt = conn.prepareStatement(delete);
	stmt.setInt(1, p.getParticipantId());
	int cnt = stmt.executeUpdate();
	// if (cnt < 1)
	// throw new Exception("Quiz with id " + q + " does not exist");
	for (Quiz q1 : p.getCollQuizzes())
	{
	    deleteAllQuestions(q1);
	}

    }

    // -------------------------------------------------------------------------------------------------------------------------
    // ---------------------------------------- QUESTION THINGS
    // -------------------------------------------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------------------------------------------

    public void addQuestion(Question q) throws SQLException
    {
	String insert = "INSERT INTO frage VALUES (?,?,?,null)";
	PreparedStatement stmt = conn.prepareStatement(insert);
	stmt.setString(1, q.getTestId());
	stmt.setInt(2, q.getQuestionId());
	stmt.setString(3, q.getText());

	stmt.executeUpdate();
    }

    public void deleteQuestion(Question q) throws Exception
    {
	String delete = "DELETE FROM frage WHERE tid LIKE ? AND fnr = ?";
	PreparedStatement stmt = conn.prepareStatement(delete);
	stmt.setString(1, q.getTestId());
	stmt.setInt(2, q.getQuestionId());
	int cnt = stmt.executeUpdate();
	if (cnt < 1)
	    throw new Exception(
		    "Question with id " + q.getQuestionId() + " in Quiz with id: " + q.getTestId() + " does not exist");
	deleteAllAnswers(q);
    }

    private void deleteAllQuestions(Quiz q) throws Exception
    {

	String delete = "DELETE FROM frage WHERE tid LIKE ?";
	PreparedStatement stmt = conn.prepareStatement(delete);
	stmt.setString(1, q.getId());
	stmt.executeUpdate();
	for (Question q1 : q.getCollQuestions())
	{
	    deleteAllAnswers(q1);
	}
    }

    public void updateQuestion(Question q) throws Exception
    {
	String update = "UPDATE frage SET ftext = ? WHERE tid = ? AND fnr = ?";
	PreparedStatement st = conn.prepareStatement(update, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
	st.setString(1, q.getText());
	st.setString(2, q.getTestId());
	st.setInt(3, q.getQuestionId());
	int cnt = st.executeUpdate();
	if (cnt < 1)
	    throw new Exception(
		    "Question with id " + q.getQuestionId() + " in Quiz with id: " + q.getTestId() + " does not exist");
    }

    // -------------------------------------------------------------------------------------------------------------------------
    // ---------------------------------------- ANSWER THINGS
    // -------------------------------------------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------------------------------------------

    public void addAnswer(Answer a) throws SQLException
    {
	String insert = "INSERT INTO antwort VALUES (?,?,?,?)";
	PreparedStatement stmt = conn.prepareStatement(insert);
	stmt.setString(1, a.getTestId());
	stmt.setInt(2, a.getQuestionId());
	stmt.setInt(3, a.getAnswerId());
	stmt.setString(4, a.getAnswerText());
	stmt.executeUpdate();
    }

    public void deleteAnswer(Answer a) throws Exception
    {
	String delete = "DELETE FROM antwort WHERE tid LIKE ? AND fnr = ? AND anr = ?";
	PreparedStatement stmt = conn.prepareStatement(delete);
	stmt.setString(1, a.getTestId());
	stmt.setInt(2, a.getQuestionId());
	stmt.setInt(3, a.getAnswerId());
	int cnt = stmt.executeUpdate();
	if (cnt < 1)
	    throw new Exception("Answer with id " + a.getAnswerId() + " in Question with id: " + a.getQuestionId()
		    + " does not exist");
    }

    private void deleteAllAnswers(Question q) throws Exception
    {
	String delete = "DELETE FROM antwort WHERE tid LIKE ? AND fnr = ? ";
	PreparedStatement stmt = conn.prepareStatement(delete);
	stmt.setString(1, q.getTestId());
	stmt.setInt(2, q.getQuestionId());
	int cnt = stmt.executeUpdate();
	if (cnt < 1)
	    throw new Exception("Quiz with id " + q + " does not exist");
    }

    public void updateAnswer(Answer a) throws Exception
    {
	String update = "UPDATE antwort SET atext = ? WHERE tid = ? AND fnr = ? AND anr = ?";
	PreparedStatement st = conn.prepareStatement(update, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
	st.setString(1, a.getAnswerText());
	st.setString(2, a.getTestId());
	st.setInt(3, a.getQuestionId());
	st.setInt(4, a.getAnswerId());

	int cnt = st.executeUpdate();
	if (cnt < 1)
	    throw new Exception("Answer with id " + a.getAnswerId() + " in Question with id: " + a.getQuestionId()
		    + " does not exist");
    }

    public int selectMaxAnswerId(Question q) throws SQLException
    {
	String select = "SELECT MAX(anr) FROM antwort WHERE tid LIKE ? AND fnr = ?";
	int ret = 0;
	PreparedStatement stmt = conn.prepareStatement(select);
	stmt.setString(1, q.getTestId());
	stmt.setInt(2, q.getQuestionId());
	ResultSet rs = stmt.executeQuery();
	while (rs.next())
	{
	    ret = rs.getInt(1);
	}
	return ret + 1;
    }

    public void asignCorrectAnswer(Answer a) throws Exception
    {
	String update = "UPDATE frage SET anrOk = ? WHERE tid = ? AND fnr = ?";
	PreparedStatement st = conn.prepareStatement(update, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
	st.setInt(1, a.getAnswerId());
	st.setString(2, a.getTestId());
	st.setInt(3, a.getQuestionId());

	int cnt = st.executeUpdate();
	if (cnt < 1)
	    throw new Exception(
		    "Question with id " + a.getQuestionId() + " in Quiz with id: " + a.getTestId() + " does not exist");
    }

    // -------------------------------------------------------------------------------------------------------------------------
    // ---------------------------------------- MISCELLANIUOS THINGS
    // -------------------------------------------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------------------------------------------

    public static Database newInstance(String ipAddres) throws Exception // TODO könnte mit "reconnect" zusammengefasst
									 // werden
    {
	if (instance == null)
	{
	    instance = new Database();
	    connectionString = ipAddres;
	    createConnection();
	    collParticipants = new ArrayList<Participant>();
	    collQuiz = new ArrayList<Quiz>();
	    collQuestions = new ArrayList<Question>();
	    collAnswers = new ArrayList<Answer>();
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
	collParticipants = new ArrayList<Participant>();
	collQuiz = new ArrayList<Quiz>();
	collQuestions = new ArrayList<Question>();
	collAnswers = new ArrayList<Answer>();
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
	conn.setAutoCommit(false);
	isConnectionSet = true;
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
