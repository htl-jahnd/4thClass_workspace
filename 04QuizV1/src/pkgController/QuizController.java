package pkgController;

import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import pkgData.Database;
import pkgData.Quiz;

public class QuizController
{

	@FXML
	private TextField txtQuizName;

	@FXML
	private Label lblMessage;

	@FXML
	private MenuItem mntmUpdate;

	@FXML
	private MenuItem mntmDelete;

	@FXML
	private MenuItem mntmSetDatabase;

	@FXML
	private TextField txtServerIp;

	@FXML
	private ListView<Quiz> lstQuiz;

	@FXML
	private MenuItem mntmAdd;

	@FXML
	private MenuItem mntmReload;

	@FXML
	private MenuItem mntmQuizToJson;

	@FXML
	private TextField txtQuizId;
	private Database db;
	private ObservableList<Quiz> listQuizzes;

	@FXML
	void onSelectMenu(ActionEvent event)
	{
		try
		{
			if (event.getSource().equals(mntmSetDatabase))
			{
				doSetDatabase();
			} else if (event.getSource().equals(mntmQuizToJson))
			{
				db.quizToJson();
			} else if (event.getSource().equals(mntmAdd))
			{
				doAddQuiz();
			} else if (event.getSource().equals(mntmDelete))
			{
				doDeleteQuiz();
			} else if (event.getSource().equals(mntmReload))
			{
				doFillListQuiz();
			} else if (event.getSource().equals(mntmUpdate))
			{
				doUpdateQuiz();
			}
		} catch (Exception e)
		{
			lblMessage.setText(e.getMessage());
			e.printStackTrace();
		}

	}

	private void doUpdateQuiz() throws Exception
	{
		String id = txtQuizId.getText();
		String name = txtQuizName.getText();
		if (id.isEmpty() || name.isEmpty())
			throw new Exception("Please enter all data for updating a quiz");
		Quiz tmp = new Quiz(id, name);
		db.updateQuiz(tmp);
		doFillListQuiz();
	}

	private void doDeleteQuiz() throws Exception
	{
		String id = txtQuizId.getText();
		if (id.isEmpty())
			throw new Exception("Id mustnt be null at delete");
		db.deleteQuiz(id);
		doFillListQuiz();
	}

	private void doAddQuiz() throws Exception
	{
		String id = txtQuizId.getText();
		String name = txtQuizName.getText();
		if (id.isEmpty() || name.isEmpty())
			throw new Exception("Please enter all data for adding a quiz");
		Quiz tmp = new Quiz(id, name);
		db.addQuiz(tmp);
		doFillListQuiz();
	}

	@FXML
	void onMouseClickedList(MouseEvent event)
	{
		if (event.getSource().equals(lstQuiz))
		{
			doFillTextFields();
		}
	}

	private void doFillTextFields()
	{
		Quiz tmp = lstQuiz.getSelectionModel().getSelectedItem();
		if (tmp != null)
		{
			System.out.println("in");
			txtQuizName.setText(tmp.getText());
			txtQuizId.setText(String.valueOf(tmp.getId()));
		}
	}

	private void doSetDatabase() throws Exception
	{
		if (txtServerIp.getText().trim().equals(""))
		{
			throw new Exception("Pleaser enter a server ip");
		}
		db = Database.newInstance(txtServerIp.getText());
		doFillListQuiz();
	}

	private void doFillListQuiz() throws SQLException
	{
		db.selectAllTests();
		listQuizzes.clear();
		listQuizzes.addAll(db.getQuizzes());
	}

	@FXML
	void initialize()
	{
		listQuizzes = FXCollections.observableArrayList();
		lstQuiz.setItems(listQuizzes);
	}

}
