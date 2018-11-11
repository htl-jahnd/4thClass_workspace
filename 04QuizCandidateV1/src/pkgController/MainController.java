package pkgController;

import java.sql.SQLException;

import Exceptions.AnswerSelectionException;
import Exceptions.NameCastException;
import Exceptions.NotConnectedException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import pkgData.Answer;
import pkgData.Database;
import pkgData.Participant;
import pkgData.Question;
import pkgData.Quiz;
import pkgMisc.ExceptionHandler;

public class MainController
{

    @FXML
    private ComboBox<String> cmbxDatabaseIp;

    @FXML
    private TextField txtCandidatesName;

    @FXML
    private RadioButton rdbtnSchoolTypeNMS;

    @FXML
    private ToggleGroup groupSchoolType;

    @FXML
    private RadioButton rdbtnSchoolTypeAHS;

    @FXML
    private RadioButton rdbtnSchoolTypeOther;

    @FXML
    private Button btnCaniddateOk;

    @FXML
    private ComboBox<Quiz> cmbxQuizzes;

    @FXML
    private Button btnQuizOk;

    @FXML
    private TextArea txtQuestionText;

    @FXML
    private ListView<Answer> lstAnswers;

    @FXML
    private Button btnConfirmAnswer;

    @FXML
    private Label lblMessage;

    @FXML
    private VBox vboxCandidateInfo;

    @FXML
    private VBox vboxQuizInfo;

    @FXML
    private AnchorPane paneQuestionAndAnswers;

    // ---------------------------------------------------------------------------
    // ---------------------------- NO FXML VARIABLES ----------------------------
    // ---------------------------------------------------------------------------

    private ObservableList<Answer> listAnswers;
    private ObservableList<Quiz> listQuizzes;
    private ObservableList<String> listDatabaseIps;
    private Database db;
    private Quiz currentQuiz;
    private Question currentQuestion;
    private Participant currentParticipant;

    // ---------------------------------------------------------------------------
    // ------------------------------ FXML METHODS -------------------------------
    // ---------------------------------------------------------------------------

    @FXML
    void initialize()
    {
	listAnswers = FXCollections.observableArrayList();
	lstAnswers.setItems(listAnswers);

	listQuizzes = FXCollections.observableArrayList();
	cmbxQuizzes.setItems(listQuizzes);

	listDatabaseIps = FXCollections.observableArrayList();
	listDatabaseIps.add("192.168.128.152"); // internal IP
	listDatabaseIps.add("212.152.179.117"); // external IP
	cmbxDatabaseIp.setItems(listDatabaseIps);
    }

    @FXML
    void onSelectButton(ActionEvent event)
    {
	try
	{
	    if (event.getSource().equals(btnCaniddateOk))
	    {
		if (txtCandidatesName.getText().length() < 3)
		    throw new NameCastException("The entered participate name has to be longer than 2 characters.");
		else if (!Database.isConnectionSet())
		    throw new NotConnectedException("A connection to the database has to be accomplished. "
			    + "Please enter an IP addres or check your network connection.");
		doLoadQuizzes();

	    } else if (event.getSource().equals(btnConfirmAnswer))
	    {
		if (lstAnswers.getSelectionModel().getSelectedItem() == null)
		    throw new AnswerSelectionException(
			    "An answer in the list has to be selected to continue with the next question in the quiz."
				    + "Click on the right one and then try again.");
		doDisplayNextQuestion();
	    } else if (event.getSource().equals(btnQuizOk))
	    {
		doLoadQuestion();
	    }
	} catch (NameCastException ne)
	{
	    doHandleExpectedException("Participates name is too short", ne);
	} catch (NotConnectedException nce)
	{
	    doHandleExpectedException("Database Connection not established", nce);
	} catch (AnswerSelectionException asw)
	{
	    doHandleExpectedException("An answer has to be selected", asw);
	} catch (Exception e)
	{
	    doHandleUnexpectedException(e);
	}
    }

    // ---------------------------------------------------------------------------
    // ----------------------------- NO FXML METHODS -----------------------------
    // ---------------------------------------------------------------------------

    private void doHandleExpectedException(String message, Exception ex)
    {
	lblMessage.setText(ex.getMessage());
	ExceptionHandler.hanldeExpectedException(message, ex);

    }

    private void doLoadQuestion() throws SQLException
    {
	currentQuiz = cmbxQuizzes.getValue();
	db.insertTeilnahme(currentQuiz, currentParticipant);
	db.selectAllQuestions(currentQuiz);
	currentQuestion = db.getNextQuestion(null);
	db.selectAllAnswers(currentQuestion);
	listAnswers.clear();
	listAnswers.setAll(db.getAnswers());
	txtQuestionText.setText(currentQuestion.getText());
	vboxQuizInfo.setDisable(true);
	paneQuestionAndAnswers.setDisable(false);

    }

    private void doDisplayQuizEnd()
    {
	// TODO Auto-generated method stub

    }

    private void doLoadQuizzes() throws Exception
    {
	db = Database.reconnect(cmbxDatabaseIp.getValue());
	RadioButton selectedRadioButton = (RadioButton) groupSchoolType.getSelectedToggle();
	String toogleGroupValue = selectedRadioButton.getText();
	int id = db.selectMaxParticipantId();
	currentParticipant = new Participant(id, txtCandidatesName.getText(), toogleGroupValue);
	db.addParticipant(currentParticipant);
	listQuizzes.clear();
	listQuizzes.addAll(db.selectAllQuizzes());
	vboxCandidateInfo.setDisable(true);
	vboxQuizInfo.setDisable(true);
	vboxQuizInfo.setDisable(false);

	cmbxQuizzes.getSelectionModel().select(listQuizzes.get(0));
    }

    private void doDisplayNextQuestion() throws SQLException
    {
	db.insertAnswer(lstAnswers.getSelectionModel().getSelectedItem(), currentParticipant);
	currentQuestion = db.getNextQuestion(currentQuestion);
	if (currentQuestion != null)
	{
	    db.selectAllAnswers(currentQuestion);
	    listAnswers.clear();
	    listAnswers.setAll(db.getAnswers());
	    txtQuestionText.setText(currentQuestion.getText());
	} else
	{
	    doDisplayQuizEnd();
	}

    }

    private void doHandleUnexpectedException(Exception ex)
    {
	lblMessage.setText(ex.getMessage());
	ExceptionHandler.hanldeUnexpectedException(ex);
    }

}
