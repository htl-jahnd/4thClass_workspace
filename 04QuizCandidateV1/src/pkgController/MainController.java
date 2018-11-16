package pkgController;

import java.net.UnknownHostException;
import java.sql.SQLException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.google.gson.GsonBuilder;

import Exceptions.AnswerSelectionException;
import Exceptions.EmptyIpException;
import Exceptions.InvalidEmailException;
import Exceptions.NameCastException;
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
import pkgMisc.GMailer;
import pkgMisc.AddressFormatValidator;

public class MainController // TODO resize dass ganze fenster wenn fertig damit ned so leer ausschaut
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

    @FXML
    private AnchorPane paneRedoQuiz;

    @FXML
    private Label lblCorrectAnswers;

    @FXML
    private Button btnRedoQuiz;

    @FXML
    private Label lblWrongAnswers;

    @FXML
    private Button btnSendResults;

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
		checkInputBeforeStart();
		doLoadQuizzes();
	    } else if (event.getSource().equals(btnConfirmAnswer))
	    {
		if (lstAnswers.getSelectionModel().getSelectedItem() == null)
		    throw new AnswerSelectionException(
			    "An answer in the list has to be selected to continue with the next question in the quiz."
				    + " Click on the right one and then try again.");
		doDisplayNextQuestion();
	    } else if (event.getSource().equals(btnQuizOk))
	    {
		doLoadQuestion();
	    } else if (event.getSource().equals(btnRedoQuiz))
	    {
		clearAllFieldsAndVariables();

		paneRedoQuiz.setVisible(false);
		paneQuestionAndAnswers.setVisible(true);
		paneQuestionAndAnswers.setDisable(true);

		vboxCandidateInfo.setDisable(false);
		vboxQuizInfo.setDisable(true);
	    }

	    else if (event.getSource().equals(btnSendResults))
	    {
		String text = GMailer.showEmailInputDialog();
		if (text != null)
		{
		    formatAndSendResultsAsMail(text);
		}
	    }
	} catch (NameCastException ne)
	{
	    doHandleExpectedException("Participates name is too short", ne);
	} catch (EmptyIpException eie)
	{
	    doHandleExpectedException("Database IP address not entered", eie);
	} catch (UnknownHostException nce)
	{
	    doHandleExpectedException("Invalid IP address", nce);
	} catch (AnswerSelectionException asw)
	{
	    doHandleExpectedException("An answer has to be selected", asw);
	} catch (InvalidEmailException iee)
	{
	    doHandleExpectedException("Invalid email address", iee);
	} catch (Exception e)
	{
	    doHandleUnexpectedException(e);
	}
    }

    private void formatAndSendResultsAsMail(String address)
	    throws InvalidEmailException, AddressException, MessagingException, SQLException
    {
	if (!AddressFormatValidator.isValidEmailAddress(address))
	    throw new InvalidEmailException("Please enter a valid email address");
	String[] addresses = { address };
	int correct = db.selectCorretAnswers(currentParticipant, currentQuiz);
	int wrongAnswers = Database.getCollQuestions().size() - correct;
	String heading = "<h1>Results for Quiz " + currentQuiz.getText() + "</h1>";
	String breaks = "<br>";
	String correctAnswers = "<h2>Correct Answers: " + correct + ".</h2>";
	String inCorrectAnswers = "<h2>Incorrect Answers: " + wrongAnswers + ".</h2>";
	StringBuilder answersInDetails = new StringBuilder();
	answersInDetails.append("<h2>Answers in detail: </h2>");
	answersInDetails.append("<ul>");
	for (Question q : Database.getCollQuestions())
	{
	    answersInDetails.append("<li>" + q.getText() + "\t");
	    for (Answer a : q.getCollAnswers())
	    {
		answersInDetails.append("<ul>");
		if (a.isChosen() && a.getIsCorrect())
		{
		    answersInDetails.append("<li>Choosen and correct: ").append(a.getAnswerText()).append("</li>");
		} else
		{
		    if (a.isChosen())
		    {
			answersInDetails.append("<li>Choosen: ").append(a.getAnswerText()).append("</li>");
		    }
		    if (a.getIsCorrect())
		    {
			answersInDetails.append("<li>Correct: ").append(a.getAnswerText()).append("</li>");
		    }
		}
		answersInDetails.append("</ul>");
	    }
	    answersInDetails.append("</li>");
	}
	answersInDetails.append("<ul>");

	StringBuilder all = new StringBuilder(heading).append(breaks).append(breaks).append(correctAnswers)
		.append(breaks).append(inCorrectAnswers);
	all.append(breaks).append(breaks).append(answersInDetails.toString());
	GMailer g = new GMailer("email.spam.konto@gmail.com", "SafeTestPwd", addresses, "Quiz Results", all.toString());
	g.sendMail();

    }

    // ---------------------------------------------------------------------------
    // ----------------------------- NO FXML METHODS -----------------------------
    // ---------------------------------------------------------------------------

    private void clearAllFieldsAndVariables() throws Exception
    {
	db.closeConnection();
	currentParticipant = null;
	currentQuestion = null;
	currentQuiz = null;
	listAnswers.clear();
	listQuizzes.clear();
	txtCandidatesName.clear();
	txtQuestionText.clear();
    }

    private void checkInputBeforeStart() throws NameCastException, EmptyIpException, UnknownHostException
    {
	if (txtCandidatesName.getText().length() < 3)
	    throw new NameCastException("The entered participate name has to be longer than 2 characters.");
	else if (cmbxDatabaseIp.getValue().trim().isEmpty())
	    throw new EmptyIpException(
		    "An IP address for the database has to be entered. It can be either be selected by the provided"
			    + " dropdown list or entered manually.");
	else if (!AddressFormatValidator.isValidateIPAddress(cmbxDatabaseIp.getValue()))
	{
	    throw new UnknownHostException(
		    "\"" + cmbxDatabaseIp.getValue() + "\" could not be parsed. Please enter a valid IPv4 address. "
			    + "Be sure it has the format xxx.xxx.xxx.xxx");
	}
    }

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

    private void doDisplayQuizEnd() throws SQLException
    {
	int correct = db.selectCorretAnswers(currentParticipant, currentQuiz);
	int wrongAnswers = Database.getCollQuestions().size() - correct;
	paneQuestionAndAnswers.setVisible(false);
	paneRedoQuiz.setVisible(true);
	lblCorrectAnswers.setText("Correct Answers: " + correct);
	lblWrongAnswers.setText("Wrong Answers: " + wrongAnswers);
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
