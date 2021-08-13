package logic;

import com.sun.istack.internal.Nullable;
import data.QuestionData;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import logic.miscellaneous.Output;
import model.error.question.IllegalAnswerTypeException;
import model.error.question.IllegalExtraParameterException;
import model.error.question.IllegalQuestionMessageTypeException;
import model.error.question.QuestionException;
import model.question.Question;
import model.question.QuestionTyp;
import ui.components.FolderView;
import ui.components.QuestionView;
import ui.components.SubjectView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionController {

    private final static QuestionController questionController = new QuestionController();
    private final QuestionData questionData = QuestionData.getInstance();
    private final QuestionView questionView = QuestionView.getInstance();
    private final Property<Number> selectedFolder = new SimpleIntegerProperty();

    private List<Object> questionMessage = new ArrayList<>();
    private List<Object> answer = new ArrayList<>();
    private List<Object> extraParameter = new ArrayList<>();

    private QuestionController(){}

    /**
     * <p>This method will create and store a Question. (for the selected Folder)</p>
     * @param questionTyp the questionType
     * @param questionMessage the questionMessage (Question)
     * @param answer the correct answer
     * @param extraParameter an extraParameter (if needed)
     */
    public void addQuestion(QuestionTyp questionTyp,
                            Object questionMessage,
                            Object answer,
                            @Nullable Object extraParameter) {
        try {
            Question question = new Question();
            question.setQuestionType(questionTyp);
            question.setQuestionMessage(questionMessage);
            question.setAnswer(answer);
            if (extraParameter != null) {
                question.setExtraParameter(extraParameter);
            }
            int id = questionData.createQuestion(question, selectedFolder.getValue().intValue());
            question.setID(id);

            questionView.addQuestion(question);
        } catch (SQLException | QuestionException e) {
            Output.exceptionWrite(e);
        }
    }

    /**
     * <p>This method change/modify the answer from a question.</p>
     * @param question the current question
     * @param answer the new/modified answer
     */
    public void changeAnswerQuestion(Question question,
                                     Object answer) {
        try {
            questionData
                    .updateAnswerParameterWithQuestionID(
                            question.getID(),
                            answer,
                            question.getQuestionTyp()
                    );
            questionView.changeAnswer(question, answer);
            question.setAnswer(answer);
        } catch (SQLException | IllegalAnswerTypeException e) {
            Output.exceptionWrite(e);
        }
    }

    /**
     * <p>This method change/modify the questionMessage from a question.</p>
     * @param question the current question
     * @param questionMessage the new/modified questionMessage
     */
    public void changeQuestionMessageQuestion(Question question,
                                              Object questionMessage) {
        try {
            questionData
                    .updateQuestionMessageParameterWithQuestionID(
                            question.getID(),
                            questionMessage,
                            question.getQuestionTyp()
                    );
            questionView.changeQuestionMessage(question, questionMessage);
            question.setQuestionMessage(questionMessage);
        } catch (SQLException | IllegalQuestionMessageTypeException e) {
            Output.exceptionWrite(e);
        }
    }

    /**
     * <p>This method change/modify the extraParameter from a question.</p>
     * @param question the current Question
     * @param extraParameter the new/modified extra Parameter
     */
    public void changeExtraParameterQuestion(Question question,
                                             Object extraParameter) {
        try {
            questionData
                    .updateExtraParameterParameterWithQuestionID(
                            question.getID(),
                            extraParameter,
                            question.getQuestionTyp()
                    );
            questionView.changeExtraParameter(question, extraParameter);
            question.setExtraParameter(extraParameter);
        } catch (SQLException | IllegalExtraParameterException e) {
            Output.exceptionWrite(e);
        }
    }

    /**
     * <p>This method will delete a question.</p>
     * @param question the Question
     */
    public void removeQuestion(Question question) {
        try {
            questionData.deleteQuestionWithQuestionID(question.getID());
            questionView.removeQuestion(question);
        } catch (SQLException e) {
            Output.exceptionWrite(e);
        }
    }

    /**
     * <p>This method will tell you if a folder is currently selected.</p>
     * @return a boolean if a folder is currently selected.
     */
    public boolean folderIsSelected() {
        return selectedFolder.getValue().intValue() != -1;
    }

    /**
     * <p>This Method is the "Constructor" for the QuestionController class.</p>
     * <p>This is the only way to access the QuestionController.</p>
     * @return a {@link QuestionController} instance
     */
    public static QuestionController getInstance() {
        return questionController;
    }

    /**
     * <p>This method will initiate the Connections between the layers.</p>
     * <p><b>NOTE:</b></p>
     * <p>Should always be called before using any Question class!</p>
     * @throws SQLException if the SQL-initiation has a problem.
     */
    public void init() throws SQLException{
        questionData.init();
        questionView.init();

        selectedFolder.addListener((observable, oldValue, newValue) -> setUp_Questions(newValue.intValue()));
        FolderView.getInstance().bindExternProperty(selectedFolder);
    }

    private void setUp_Questions(int newId) {
        if (newId == -1) {
            return;
        }
        try {
            questionView.clearQuestions();
            addQuestions(newId);
            SubjectView.getInstance().setContent(questionView.getQuestionView());
        } catch (SQLException | QuestionException e) {
            Output.exceptionWrite(e);
        }
    }

    private void addQuestions(int id) throws SQLException, QuestionException {
        Question question = null;
        ResultSet resultSet = questionData.getQuestionsWithFolderID(id);
        int lastQuestionID = -1;
        while (resultSet.next()) {
            int questionID = resultSet.getInt(1);
            if (newQuestion(lastQuestionID, questionID)) {
                if (question != null) {
                    questionView.addQuestion(question);
                    resetQuestionCache();
                }
                question = getQuestion(resultSet);
            }
            getExtraParams(question, resultSet);
            lastQuestionID = questionID;
        }
        questionView.addQuestion(question);
        resetQuestionCache();
    }

    private boolean newQuestion(int lastQuestionID, int questionID) {
        return lastQuestionID != questionID;
    }

    private Question getQuestion(ResultSet resultSet) throws SQLException{
        Question question = new Question();
        question.setID(resultSet.getInt(1));
        QuestionTyp questionTyp;
        String typ = resultSet.getString(2);
        switch (typ) {
            case "MultipleChoiceQuestion":
                questionTyp = QuestionTyp.MultipleChoiceQuestion;
                break;
            case "DirectQuestion":
                questionTyp = QuestionTyp.DirectQuestion;
                break;
            case "WordsQuestion":
                questionTyp = QuestionTyp.WordsQuestion;
                break;
            default:
                questionTyp = QuestionTyp.UNSET;
                break;
        }
        question.setQuestionType(questionTyp);
        return question;
    }

    private void getExtraParams(Question question, ResultSet resultSet) throws SQLException, QuestionException {
        String paramTyp = resultSet.getString(3);
        switch (paramTyp) {
            case "questionMessage":
                getQuestionMessage(question, resultSet);
                break;
            case "answer":
                getAnswer(question, resultSet);
                break;
            case "extraParameter":
                getExtraParameter(question, resultSet);
                break;
            default:
                throw new QuestionException("Unknown Parameter-Typ! (" + paramTyp + ")");
        }
    }

    private void getQuestionMessage(Question question, ResultSet resultSet) throws SQLException, IllegalQuestionMessageTypeException {
        questionMessage.add(resultSet.getObject(4));
        if (questionMessage.size() > 1) {
            question.setQuestionMessage(questionMessage.toArray());
        } else {
            question.setQuestionMessage(questionMessage.get(0));
        }
    }

    private void getAnswer(Question question, ResultSet resultSet) throws SQLException, IllegalAnswerTypeException {
        answer.add(resultSet.getObject(4));
        if (answer.size() > 1) {
            question.setAnswer(answer.toArray());
        } else {
            question.setAnswer(answer.get(0));
        }
    }

    private void getExtraParameter(Question question, ResultSet resultSet) throws SQLException, IllegalExtraParameterException {
        extraParameter.add(resultSet.getObject(4));
        if (extraParameter.size() > 1) {
            question.setExtraParameter(extraParameter.toArray());
        } else {
            question.setExtraParameter(extraParameter.get(0));
        }
    }

    private void resetQuestionCache() {
        questionMessage = new ArrayList<>();
        answer = new ArrayList<>();
        extraParameter = new ArrayList<>();
    }
}
