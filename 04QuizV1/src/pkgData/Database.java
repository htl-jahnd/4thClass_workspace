package pkgData;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.Gson;

public class Database
{
    // private static final String CONNECTION_STRING_INTERN =
    // "jdbc:oracle:thin:@192.168.128.152:1521:ora11g";
    // private static final String CONNECTION_STRING_EXTERN =
    // "dbc:oracle:thin:@212.152.179.117:1521:ora11g"; // TODO add feature to select
    // if intern or external ip is needed
    private static String connectionString;
    private static final String USER = "d4b12";
    private static final String PWD_DB = "d4b";
    private static Database instance = null;
    private static Connection conn = null;
    private static Quiz currentTest;
    private static ArrayList<Quiz> collQuiz;
    private static final String JSON_FILE_PATH = "quizzes.json";

    public void selectAllTests() throws SQLException
    {
	ArrayList<Quiz> tests = new ArrayList<Quiz>();
	String select = "SELECT * FROM test";
	PreparedStatement stmt = conn.prepareStatement(select);
	ResultSet rs = stmt.executeQuery();
	while (rs.next())
	{
	    tests.add(new Quiz(rs.getString("tid"), rs.getString("tname")));
	}
	collQuiz.clear();
	collQuiz.addAll(tests);
    }

    public ArrayList<Question> selectAllQuestions(Quiz t) throws SQLException
    {
	ArrayList<Question> tests = new ArrayList<Question>();
	String select = "SELECT * FROM fragen WHERE tid LIKE ?";
	PreparedStatement stmt = conn.prepareStatement(select);
	stmt.setString(1, t.getId());
	ResultSet rs = stmt.executeQuery();
	while (rs.next())
	{
	    tests.add(new Question(rs.getString("tid"), rs.getInt("fnr"), rs.getString("FText"), rs.getInt("anr_ok")));
	}
	return tests;
    }

    public ArrayList<Answer> selectAllAnswers(Quiz t, Question q) throws SQLException
    {
	ArrayList<Answer> tests = new ArrayList<Answer>();
	String select = "SELECT * FROM antworten WHERE tid LIKE ? AND fnr LIKE ?";
	PreparedStatement stmt = conn.prepareStatement(select);
	stmt.setString(1, t.getId());
	stmt.setInt(2, q.getQuestionId());
	ResultSet rs = stmt.executeQuery();
	while (rs.next())
	{
	    tests.add(new Answer(rs.getString("tid"), rs.getInt("FNr"), rs.getInt("ANr"), rs.getString("AText")));
	}
	return tests;
    }

    private Database()
    {
    }

    public static Database newInstance(String connectStr) throws Exception
    {
	if (instance == null)
	{
	    instance = new Database();
	    connectionString = connectStr;
	    createConnection();
	    collQuiz = new ArrayList<Quiz>();
	}
	return instance;
    }

    public Collection<Quiz> getQuizzes()
    {
	return collQuiz;
    }

    private static void createConnection() throws Exception
    {
	if (conn == null)
	{
	    DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
	}
	StringBuilder bu = new StringBuilder("jdbc:oracle:thin:@").append(connectionString).append(":1521:ora11g");
	connectionString = bu.toString();
	conn = DriverManager.getConnection(connectionString, USER, PWD_DB);
	conn.setAutoCommit(false);
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

    public Quiz getCurrentTest()
    {
	return currentTest;
    }

    public void quizToJson() throws IOException
    {
	ArrayList<Quiz> quizzes = (ArrayList<Quiz>) getQuizzes();
	Gson gson = new Gson();
	try (FileWriter fw = new FileWriter(JSON_FILE_PATH))
	{
	    gson.toJson(quizzes, fw);
	    fw.flush();
	}
    }

    public void addQuiz(Quiz q) throws SQLException
    {
	String insert = "INSERT INTO test VALUES (?,?)";
	// createConnection();
	PreparedStatement stmt = conn.prepareStatement(insert);
	stmt.setString(1, q.getId());
	System.out.println(q.getId() + "   len: " + q.getId().length());
	stmt.setString(2, q.getText());

	stmt.executeUpdate();
	// conn.close();

    }

    public void deleteQuiz(String id) throws Exception
    {
	String insert = "DELETE FROM test WHERE tid LIKE ?";
	// createConnection();
	PreparedStatement stmt = conn.prepareStatement(insert);
	stmt.setString(1, id);
	int cnt = stmt.executeUpdate();
	if (cnt < 1)
	    throw new Exception("Quiz with id " + id + " does not exist");
    }

    public void updateQuiz(Quiz q) throws Exception
    {
	String insert = "UPDATE test  SET tname = ? WHERE tid = ?";
	// createConnection();
	PreparedStatement st = conn.prepareStatement(insert, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

	st.setString(1, q.getId());
	st.setString(2, q.getText());

	int cnt = st.executeUpdate();
	if (cnt < 1)
	    throw new Exception("Quiz with id " + q.getId() + " does not exist");
    }
}
