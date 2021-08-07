package logic;

import data.QuestionData;
import ui.components.QuestionView;

import java.sql.SQLException;

public class QuestionController {

    private final static QuestionController questionController = new QuestionController();
    private final QuestionData questionData = QuestionData.getInstance();
    private final QuestionView questionView = QuestionView.getInstance();

    private QuestionController(){}

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
//        questionView.init();
    }
}
