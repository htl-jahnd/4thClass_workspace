package pkgController;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import pkgData.Answer;
import pkgData.Database;
import pkgData.Participant;
import pkgData.Question;
import pkgData.Quiz;
import pkgMisc.ExceptionHandler;
import pkgMisc.Logger;

public class QuizController
{
    @FXML
    private TextField txtQuizName;

    @FXML
    private Label lblMessage;

    @FXML
    private MenuItem mntmQuizUpdate;

    @FXML
    private MenuItem mntmQuizDelete;

    @FXML
    private MenuItem mntmSetDatabase;

    @FXML
    private ListView<Quiz> lstQuiz;

    @FXML
    private MenuItem mntmQuizAdd;

    @FXML
    private MenuItem mntmQuizReload;

    @FXML
    private MenuItem mntmCurrentQuizToJson;

    @FXML
    private MenuItem mntmCompleteQuizzesToJson;

    @FXML
    private TextField txtQuizId;

    @FXML
    private TextField txtQuestionId;

    @FXML
    private TextField txtQuestionName;

    @FXML
    private ListView<Question> lstQuestions;

    @FXML
    private MenuItem mntmQuestionAdd;

    @FXML
    private MenuItem mntmQuestionUpdate;

    @FXML
    private MenuItem mntmQuestionDelete;

    @FXML
    private ComboBox<String> cmbxDatabaseIp;

    @FXML
    private MenuItem mntmShowLogFile;

    @FXML
    private MenuItem mntmClearLogFile;

    @FXML
    private MenuItem mntmCommit;

    @FXML
    private TableView<Answer> tableAnswers;

    @FXML
    private MenuItem mntmRollback;

    @FXML
    private TableColumn<Answer, Integer> colAnswerId;

    @FXML
    private TableColumn<Answer, String> colAnswerText;

    @FXML
    private MenuItem mntmAnswerAdd;

    @FXML
    private MenuItem mntmAnswerUpdate;

    @FXML
    private MenuItem mntmAnswerDelete;

    @FXML
    private MenuItem mntmAsignCorrectAnswer;

    @FXML
    private Label lblCurrentUser;

    @FXML
    private MenuItem mntmParticipantAdd;

    @FXML
    private MenuItem mntmParticipantUpdate;

    @FXML
    private MenuItem mntmParticipantDelete;

    @FXML
    private TableView<Participant> tableParticipants;

    @FXML
    private TableColumn<Participant, Integer> colParticipantId;

    @FXML
    private TableColumn<Participant, String> colParticipantName;

    @FXML
    private TableColumn<Participant, String> colParticipantType;

    @FXML
    private RadioButton rdbtnShowAllQuizzes;

    @FXML
    private ToggleGroup groupShowQuizzes;

    @FXML
    private RadioButton rdbtnShowSpecificQuizzes;

    @FXML
    private TableColumn<Participant, Boolean> colAnswerCorrect;

    @FXML
    private AnchorPane anchorPane;

    // -------------------------------------------------------------------------------------------------------------------------
    // ----------------------------------------------------- NON FXML Variables
    // -------------------------------------------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------------------------------------------

    private Database db;
    private ObservableList<Participant> listParticipants;
    private ObservableList<Quiz> listQuizzes;
    private ObservableList<Question> listQuestions;
    private ObservableList<String> listDatabaseIps;
    private ObservableList<Answer> listAnswers;
    private Participant currentParticipant;
    private Quiz currentQuiz;
    private Question currentQuestion;
    private Answer currentAnswer;
    private TableViewSelectionModel<Participant> tableParticipantsModel;

    // -------------------------------------------------------------------------------------------------------------------------
    // ---------------------------------------------------- FXML METHODS
    // -------------------------------------------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------------------------------------------

    @FXML
    void onSelectMenuLogFile(ActionEvent event)
    {
	try
	{
	    if (event.getSource().equals(mntmClearLogFile))
	    {
		Logger.doClearLogFile();
	    } else if (event.getSource().equals(mntmShowLogFile))
	    {
		Logger.viewLogFile();
	    }
	} catch (Exception ex)
	{
	    doHandleException(ex);
	}
    }

    @FXML
    void onSelectMenuParticipant(ActionEvent event)
    {
	try
	{
	    if (event.getSource().equals(mntmParticipantAdd))
	    {
		doAddParticipant();
	    } else if (event.getSource().equals(mntmParticipantUpdate))
	    {
		doUpdateParticipant();
	    } else if (event.getSource().equals(mntmParticipantDelete))
	    {
		doDeleteParticipant();
	    }
	} catch (Exception e)
	{
	    doHandleException(e);
	}
    }

    @FXML
    void onSelectMenuQuiz(ActionEvent event)
    {
	try
	{
	    if (event.getSource().equals(mntmQuizAdd))
	    {
		doAddQuiz();
	    } else if (event.getSource().equals(mntmQuizDelete))
	    {
		doDeleteQuiz();
	    } else if (event.getSource().equals(mntmQuizReload))
	    {
		doRefreshListQuiz();
	    } else if (event.getSource().equals(mntmQuizUpdate))
	    {
		doUpdateQuiz();
	    }
	} catch (Exception e)
	{
	    doHandleException(e);
	}

    }

    @FXML
    void onSelectMenuQuestion(ActionEvent event)
    {
	try
	{
	    if (event.getSource().equals(mntmQuestionAdd))
	    {
		doAddQuestion();
	    } else if (event.getSource().equals(mntmQuestionDelete))
	    {
		doDeleteQuestion();
	    } else if (event.getSource().equals(mntmQuestionUpdate))
	    {
		doUpdateQuestion();
	    }
	} catch (Exception e)
	{
	    doHandleException(e);
	}
    }

    @FXML
    void onSelectMenuFile(ActionEvent event)
    {
	try
	{
	    if (event.getSource().equals(mntmSetDatabase))
	    {
		doSetDatabase();
	    } else if (event.getSource().equals(mntmCurrentQuizToJson))
	    {
		doSaveQuizToJson();
	    } else if (event.getSource().equals(mntmCompleteQuizzesToJson))
	    {
		doSaveAllQuizzesToJson();
	    }
	} catch (Exception e)
	{
	    doHandleException(e);
	}
    }

    @FXML
    void onSelectMenuAnswer(ActionEvent event)
    {
	try
	{
	    if (event.getSource().equals(mntmAnswerAdd))
	    {
		doAddAnswer();
		doRefreshTableAnswers();
	    } else if (event.getSource().equals(mntmAnswerUpdate))
	    {
		doUpdateAnswer();
		doRefreshTableAnswers();
	    } else if (event.getSource().equals(mntmAnswerDelete))
	    {
		doDeleteAnswer();
		doRefreshTableAnswers();
	    } else if (event.getSource().equals(mntmAsignCorrectAnswer))
	    {
		doAsignCorrectAnswer();
		doRefreshTableAnswers();
	    }
	} catch (Exception e)
	{
	    doHandleException(e);
	}
    }

    @FXML
    void onSelectRadioButtonShowQuizzes(ActionEvent event)
    {
	try
	{
	    doRefreshTableParticipants();
	    doRefreshListQuiz(event.getSource().equals(rdbtnShowAllQuizzes));
	} catch (Exception e)
	{
	    doHandleException(e);
	}
    }

    @FXML
    void onParticipantsClicked(MouseEvent event)
    {
	try
	{
	    if (!rdbtnShowAllQuizzes.isSelected())
	    {
		if (!Database.isConnectionSet())
		    throw new Exception("An database conenction has to be established to perform this operation");
		currentParticipant = tableParticipants.getSelectionModel().getSelectedItem();
		if (currentParticipant != null)
		{
		    db.selectAllTests(currentParticipant);
		    currentQuiz = null;
		    currentQuestion = null;
		    currentAnswer = null;
		    listQuizzes.clear();
		    listQuizzes.addAll(db.getQuizzes());
		    listQuestions.clear();
		    listAnswers.clear();
		    txtQuestionId.clear();
		    txtQuestionName.clear();
		    txtQuizId.clear();
		    txtQuizName.clear();
		}

	    }
	} catch (Exception ex)
	{
	    doHandleException(ex);
	}
    }

    @FXML
    void onQuizClicked(MouseEvent event)
    {
	try
	{
	    if (!Database.isConnectionSet())
		throw new Exception("An database conenction has to be established to perform this operation");
	    currentQuiz = lstQuiz.getSelectionModel().getSelectedItem();
	    if (currentQuiz != null)
	    {
		currentQuestion = null;
		currentAnswer = null;
		doFillTextFieldsQuiz();
		doRefreshListQuestions();
	    }
	} catch (Exception ex)
	{
	    doHandleException(ex);
	}
    }

    @FXML
    void onQuestionClicked(MouseEvent event)
    {
	try
	{
	    if (!Database.isConnectionSet())
		throw new Exception("An database conenction has to be established to perform this operation");
	    currentQuestion = lstQuestions.getSelectionModel().getSelectedItem();
	    doFillTextFieldsQuestion();
	    if (currentQuestion != null)
	    {
		if (rdbtnShowAllQuizzes.isSelected())
		    db.selectAllAnswers(currentQuestion);
		else
		{
		    db.selectAllAnswers(currentQuestion, currentParticipant);
		    doPaintCells();
		}

		currentAnswer = null;
		listAnswers.clear();
		listAnswers.addAll(db.getAnswers());
	    }
	} catch (Exception e)
	{
	    doHandleException(e);
	}
    }

    private void doPaintCells()
    {
	tableAnswers.setRowFactory(tableView -> new TableRow<Answer>() {
	    @Override
	    protected void updateItem(Answer ans, boolean empty)
	    {
		super.updateItem(ans, empty);
		if (empty)
		    setStyle("");
		else if ((ans.isChosen() && ans.getIsCorrect()) || (ans.getIsCorrect() && !ans.isChosen()))
		    // TODO schriftfarbe beim anklciken auf schwarz setzen --> bessere lesbarkeit
		    // beim bearbeiten
		    setStyle("-fx-background-color:lightgreen;"); // GREEN
		else if (ans.isChosen())
		    setStyle("-fx-background-color:#FF6666;"); // RED

	    }
	});

    }

    @FXML
    void onAnswersClicked(MouseEvent event)
    {
	try
	{
	    if (!Database.isConnectionSet())
		throw new Exception("An database conenction has to be established to perform this operation");
	    currentAnswer = tableAnswers.getSelectionModel().getSelectedItem();
	} catch (Exception ex)
	{
	    doHandleException(ex);
	}
    }

    @FXML
    void onSelectMenuTransaction(ActionEvent event)
    {
	try
	{
	    if (event.getSource().equals(mntmCommit))
	    {
		doCommit();
	    } else if (event.getSource().equals(mntmRollback))
	    {
		doRollback();
	    }
	} catch (Exception e)
	{
	    doHandleException(e);
	}
    }

    @FXML
    void onEditCellAnswerText(CellEditEvent<Answer, String> event)
    {
	(event.getTableView().getItems().get(event.getTablePosition().getRow())).setAnswerText(event.getNewValue());
    }

    @FXML
    void onEditCellParticipantName(CellEditEvent<Participant, String> event)
    {
	(event.getTableView().getItems().get(event.getTablePosition().getRow()))
		.setParticipantName(event.getNewValue());
    }

    @FXML
    void onEditCellParticipantType(CellEditEvent<Participant, String> event)
    {
	(event.getTableView().getItems().get(event.getTablePosition().getRow()))
		.setParticipantSchoolType(event.getNewValue());
    }

    @FXML
    void initialize()
    {
	// initializing the observable list of quizzes
	listQuizzes = FXCollections.observableArrayList();
	lstQuiz.setItems(listQuizzes);
	// initializing the observable list of questions
	listQuestions = FXCollections.observableArrayList();
	lstQuestions.setItems(listQuestions);
	// initializing the observable list of ip adddresses and adding internal and
	// external server ip
	listDatabaseIps = FXCollections.observableArrayList();
	listDatabaseIps.add("192.168.128.152"); // internal IP
	listDatabaseIps.add("212.152.179.117"); // external IP
	cmbxDatabaseIp.setItems(listDatabaseIps);

	// Table things
	// Answers table
	listAnswers = FXCollections.observableArrayList();
	tableAnswers.setItems(listAnswers);
	colAnswerId.setCellValueFactory(new PropertyValueFactory<>("answerId"));
	colAnswerText.setCellValueFactory(new PropertyValueFactory<>("answerText"));
	colAnswerCorrect.setCellValueFactory(new PropertyValueFactory<>("isCorrect"));
	colAnswerText.setCellFactory(TextFieldTableCell.forTableColumn());
	colAnswerId.setStyle("-fx-alignment: CENTER");

	// Participants table
	listParticipants = FXCollections.observableArrayList();
	tableParticipants.setItems(listParticipants);
	colParticipantId.setCellValueFactory(new PropertyValueFactory<Participant, Integer>("participantId"));
	colParticipantName.setCellValueFactory(new PropertyValueFactory<Participant, String>("participantName"));
	colParticipantType.setCellValueFactory(new PropertyValueFactory<Participant, String>("participantSchoolType"));
	colParticipantName.setCellFactory(TextFieldTableCell.forTableColumn());
	colParticipantType.setCellFactory(TextFieldTableCell.forTableColumn());
	tableParticipantsModel = tableParticipants.getSelectionModel();
	rdbtnShowAllQuizzes.setDisable(true);
	rdbtnShowSpecificQuizzes.setDisable(true);
    }

    // -------------------------------------------------------------------------------------------------------------------------
    // ---------------------------------------- NON FXML METHODS
    // -------------------------------------------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------------------------------------------

    private void doSaveAllQuizzesToJson() throws IOException
    {
	FileChooser fileChooser = new FileChooser();
	fileChooser.setTitle("Open Resource File");
	fileChooser.getExtensionFilters().addAll(new ExtensionFilter("JSON Files", "*.json"),
		new ExtensionFilter("All Files", "*.*"));
	Stage mainStage = (Stage) anchorPane.getScene().getWindow();
	File selectedFile = fileChooser.showSaveDialog(mainStage);
	if (selectedFile != null)
	{
	    db.allQuizzesToJson(selectedFile);
	}

    }

    private void doSaveQuizToJson() throws IOException
    {
	FileChooser fileChooser = new FileChooser();
	fileChooser.setTitle("Open Resource File");
	fileChooser.getExtensionFilters().addAll(new ExtensionFilter("JSON Files", "*.json"),
		new ExtensionFilter("All Files", "*.*"));
	Stage mainStage = (Stage) anchorPane.getScene().getWindow();
	File selectedFile = fileChooser.showSaveDialog(mainStage);
	if (selectedFile != null)
	{
	    db.quizToJson(currentQuiz, selectedFile);
	}
    }

    private void doRollback() throws Exception
    {
	if (!Database.isConnectionSet())
	    throw new Exception("An database conenction has to be established to perform this operation");
	db.rollback();
    }

    private void doCommit() throws Exception
    {
	if (!Database.isConnectionSet())
	    throw new Exception("An database conenction has to be established to perform this operation");
	db.commit();
    }

    private void doDeleteParticipant() throws Exception
    {
	if (!Database.isConnectionSet())
	    throw new Exception("An database conenction has to be established to perform this operation");
	if (currentParticipant == null)
	    throw new Exception("A participant has to be selected to perform this operation");
	else
	{
	    db.deleteParticipant(currentParticipant);
	    doRefreshTableParticipants();
	}

    }

    private void doUpdateParticipant() throws Exception
    {
	if (!Database.isConnectionSet())
	    throw new Exception("An database conenction has to be established to perform this operation");
	if (currentParticipant == null)
	    throw new Exception("A participant has to be selected to perform this operation");
	else
	{
	    db.updateParticipant(currentParticipant);
	    doRefreshTableParticipants();
	}

    }

    private void doAddParticipant() throws Exception
    {
	if (!Database.isConnectionSet())
	    throw new Exception("An database conenction has to be established to perform this operation");
	Participant newP = new Participant(db.selectMaxParticipantId(), "Default", "Default");
	db.addParticipant(newP);
	doRefreshTableParticipants();
    }

    private void doAsignCorrectAnswer() throws Exception
    {
	if (!Database.isConnectionSet())
	    throw new Exception("An database conenction has to be established to perform this operation");
	if (currentParticipant == null && rdbtnShowSpecificQuizzes.isSelected())
	    throw new Exception("A participant has to be selected to perform this operation");
	if (currentQuiz == null)
	    throw new Exception("A quiz has to be selected to perform this operation");
	if (currentQuestion == null)
	    throw new Exception("A question has to be selected to perform this operation");
	if (currentAnswer == null)
	    throw new Exception("An answer has to be selected to perform this operation");
	else
	{
	    db.asignCorrectAnswer(currentAnswer);
	}
    }

    private void doUpdateAnswer() throws Exception
    {
	if (!Database.isConnectionSet())
	    throw new Exception("An database conenction has to be established to perform this operation");
	if (currentParticipant == null && rdbtnShowSpecificQuizzes.isSelected())
	    throw new Exception("A participant has to be selected to perform this operation");
	if (currentQuiz == null)
	    throw new Exception("A quiz has to be selected to perform this operation");
	if (currentQuestion == null)
	    throw new Exception("A question has to be selected to perform this operation");
	if (currentAnswer == null)
	    throw new Exception("An answer has to be selected to perform this operation");
	else
	{
	    db.updateAnswer(currentAnswer);
	}
    }

    private void doDeleteAnswer() throws Exception
    {
	if (!Database.isConnectionSet())
	    throw new Exception("An database conenction has to be established to perform this operation");
	if (currentParticipant == null && rdbtnShowSpecificQuizzes.isSelected())
	    throw new Exception("A participant has to be selected to perform this operation");
	if (currentQuiz == null)
	    throw new Exception("A quiz has to be selected to perform this operation");
	if (currentQuestion == null)
	    throw new Exception("A question has to be selected to perform this operation");
	if (currentAnswer == null)
	    throw new Exception("An answer has to be selected to perform this operation");
	else
	{
	    db.deleteAnswer(currentAnswer);
	}
    }

    private void doAddAnswer() throws Exception
    {
	if (!Database.isConnectionSet())
	    throw new Exception("An database conenction has to be established to perform this operation");
	if (currentParticipant == null && rdbtnShowSpecificQuizzes.isSelected())
	    throw new Exception("A participant has to be selected to perform this operation");
	if (currentQuiz == null)
	    throw new Exception("A quiz has to be selected to perform this operation");
	if (currentQuestion == null)
	    throw new Exception("A question has to be selected to perform this operation");
	else
	{
	    Answer newAnswer = new Answer(currentQuiz.getId(), currentQuestion.getQuestionId(),
		    db.selectMaxAnswerId(currentQuestion), "------", false);
	    db.addAnswer(newAnswer);

	}
    }

    private void doRefreshTableAnswers() throws Exception
    {
	if (!Database.isConnectionSet())
	    throw new Exception("An database conenction has to be established to perform this operation");
	if (currentQuestion == null && rdbtnShowSpecificQuizzes.isSelected())
	    throw new Exception("A question has to be selected to perform this operation");
	if (currentParticipant == null)
	    db.selectAllAnswers(currentQuestion);
	else
	    db.selectAllAnswers(currentQuestion, currentParticipant);
	currentAnswer = null;
	listAnswers.clear();
	listAnswers.addAll(db.getAnswers());

    }

    private void doAddQuestion() throws Exception
    {
	if (!Database.isConnectionSet())
	    throw new Exception("An database conenction has to be established to perform this operation");
	if (currentParticipant == null && rdbtnShowSpecificQuizzes.isSelected())
	    throw new Exception("A participant has to be selected to perform this operation");
	if (currentQuiz == null)
	    throw new Exception("A quiz has to be selected to perform this operation");
	else
	{
	    String id = txtQuestionId.getText();
	    String text = txtQuestionName.getText();
	    if (id.isEmpty() || text.isEmpty())
		throw new Exception("Please enter all data for updating a quiz");
	    Question tmp = new Question(currentQuiz.getId(), Integer.valueOf(id), text);
	    db.addQuestion(tmp);
	    doRefreshListQuestions();
	}
    }

    private void doUpdateQuestion() throws Exception
    {
	if (!Database.isConnectionSet())
	    throw new Exception("An database conenction has to be established to perform this operation");
	if (currentParticipant == null && rdbtnShowSpecificQuizzes.isSelected())
	    throw new Exception("A participant has to be selected to perform this operation");
	if (currentQuiz == null)
	    throw new Exception("A quiz has to be selected to perform this operation");
	else
	{
	    String id = txtQuestionId.getText();
	    String text = txtQuestionName.getText();
	    if (id.isEmpty() || text.isEmpty())
		throw new Exception("Please enter all data for updating a question");
	    Question tmp = new Question(currentQuiz.getId(), Integer.valueOf(id), text);
	    db.updateQuestion(tmp);
	    doRefreshListQuestions();
	}
    }

    private void doDeleteQuestion() throws Exception
    {
	if (!Database.isConnectionSet())
	    throw new Exception("An database conenction has to be established to perform this operation");
	if (currentParticipant == null && rdbtnShowSpecificQuizzes.isSelected())
	    throw new Exception("A participant has to be selected to perform this operation");
	if (currentQuiz == null)
	    throw new Exception("A quiz has to be selected to perform this operation");
	if (currentQuestion == null)
	    throw new Exception("A question has to be selected to perform this operation");
	else
	{
	    db.deleteQuestion(currentQuestion);
	    doRefreshListQuestions();
	}
    }

    private void doFillTextFieldsQuestion()
    {
	if (currentQuestion != null)
	{
	    txtQuestionId.setText(Integer.toString(currentQuestion.getQuestionId()));
	    txtQuestionName.setText(currentQuestion.getText());
	}

    }

    private void doUpdateQuiz() throws Exception
    {
	if (!Database.isConnectionSet())
	    throw new Exception("An database conenction has to be established to perform this operation");
	String id = txtQuizId.getText();
	String name = txtQuizName.getText();
	if (id.isEmpty() || name.isEmpty())
	    throw new Exception("Please enter all data for updating a quiz");
	Quiz tmp = new Quiz(id, name);
	db.updateQuiz(tmp);
	doRefreshListQuiz();
    }

    private void doDeleteQuiz() throws Exception
    {
	if (!Database.isConnectionSet())
	    throw new Exception("An database conenction has to be established to perform this operation");
	if (currentQuiz == null)
	    throw new Exception("A quiz has to be selected to perform this operation");
	else
	{
	    if (rdbtnShowSpecificQuizzes.isSelected())
	    {
		throw new Exception("It's impossible to delete a done quiz from a participant");
	    } else
	    {
		db.deleteQuiz(currentQuiz);
	    }
	    doRefreshListQuiz();
	}

    }

    private void doAddQuiz() throws Exception
    {
	if (!Database.isConnectionSet())
	    throw new Exception("An database conenction has to be established to perform this operation");
	String id = txtQuizId.getText();
	String name = txtQuizName.getText();
	if (id.isEmpty() || name.isEmpty())
	    throw new Exception("Please enter all data for adding a quiz");
	Quiz tmp = new Quiz(id, name);
	if (!rdbtnShowSpecificQuizzes.isSelected())
	{
	    db.addQuiz(tmp);
	} else
	{
	    throw new Exception("It's impossible to add a undone quiz to a participant");
	}
	doRefreshListQuiz();
    }

    private void doRefreshListQuestions() throws Exception
    {
	if (!Database.isConnectionSet())
	    throw new Exception("An database conenction has to be established to perform this operation");
	if (currentQuiz == null)
	    throw new Exception("A quiz has to be selected to perform this operation");
	db.selectAllQuestions(currentQuiz);
	txtQuestionId.clear();
	txtQuestionName.clear();
	currentQuestion = null;
	listQuestions.clear();
	listQuestions.addAll(db.getQuestions());

	listAnswers.clear();
    }

    private void doFillTextFieldsQuiz() throws Exception
    {
	if (currentQuiz != null)
	{
	    txtQuizName.setText(currentQuiz.getText());
	    txtQuizId.setText(String.valueOf(currentQuiz.getId()));
	}
    }

    private void doSetDatabase() throws Exception
    {
	if (cmbxDatabaseIp.getValue() == null || cmbxDatabaseIp.getValue().isEmpty())
	{
	    throw new Exception("Pleaser enter a server ip");
	}
	db = Database.reconnect(cmbxDatabaseIp.getValue());
	doRefreshTableParticipants();
	lblCurrentUser.setText(Database.getUser());
	rdbtnShowAllQuizzes.setDisable(false);
	rdbtnShowSpecificQuizzes.setDisable(false);
    }

    private void doRefreshTableParticipants() throws SQLException
    {
	db.selectAllParticipants();
	listParticipants.clear();
	listParticipants.addAll(db.getParticipants());
    }

    private void doRefreshListQuiz() throws Exception
    {
	listQuizzes.clear();
	if (currentParticipant == null && rdbtnShowSpecificQuizzes.isSelected())
	    throw new Exception("A participant has to be selected to perform this operation");
	if (rdbtnShowSpecificQuizzes.isSelected())
	{
	    db.selectAllTests(currentParticipant);
	} else
	{
	    db.selectAllTests();
	}
	listQuizzes.addAll(db.getQuizzes());
    }

    private void doRefreshListQuiz(boolean showAllQuizzesIsCliked) throws Exception
    {
	listQuizzes.clear();
	if (currentParticipant == null && !showAllQuizzesIsCliked)
	    throw new Exception("A participant has to be selected to perform this operation");
	if (!showAllQuizzesIsCliked)
	{
	    db.selectAllTests(currentParticipant);
	} else
	{
	    db.selectAllTests();
	}
	listQuizzes.addAll(db.getQuizzes());
    }

    private void doHandleException(Exception ex)
    {
	lblMessage.setText(ex.getMessage());
	ExceptionHandler.hanldeUnexpectedException(ex);
    }
}
