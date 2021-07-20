package data;

import logic.miscellaneous.Output;
import model.question.QuestionTyp;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QuestionData {

    private static final QuestionData questionData = new QuestionData();
    private final Database database = Database.getInstance();

    static{
        try {
            questionData.createTableIfNotExists();
        } catch (Exception e) {
            Output.exceptionWrite(e);
        }
    }

    private QuestionData() {}

    public static QuestionData getInstance(){
        return questionData;
    }

    public ResultSet getQuestion() {
        return null; //TODO
    }

    private void createTableIfNotExists() throws SQLException {
        createQuestionTable();
        createQuestionParamsTable();
    }
    private void createQuestionTable() throws SQLException{
        database.createTableIfNotExists(
                "Question",
                new String[]{
                        "ID INTEGER NOT NULL " +
                                "CONSTRAINT Question_pk " +
                                "PRIMARY KEY AUTOINCREMENT, ",
                        "f_ID INTEGER NOT NULL " +
                                "CONSTRAINT Question__Folder_fk " +
                                "REFERENCES Folder " +
                                "ON UPDATE CASCADE ON DELETE CASCADE, ",
                        "QuestionType TEXT NOT NULL check(" +
                                "QuestionType = \"" + QuestionTyp.UNSET + "\" " +
                                "OR QuestionType = \"" + QuestionTyp.DirectQuestion + "\" " +
                                "OR QuestionType = \"" + QuestionTyp.WordsQuestion + "\" "+
                                "OR QuestionType = \"" + QuestionTyp.MultipleChoiceQuestion +"\")"
                }
        );
    }
    private void createQuestionParamsTable() throws SQLException{
        database.createTableIfNotExists(
                "Question_Params",
                new String[]{
                        "ID INTEGER NOT NULL " +
                                "CONSTRAINT Question_Params_pk " +
                                "PRIMARY KEY AUTOINCREMENT, ",
                        "f_ID INTEGER NOT NULL " +
                                "CONSTRAINT Question_Params__Question_fk " +
                                "REFERENCES Question " +
                                "ON UPDATE CASCADE ON DELETE CASCADE, ",
                        "type Text NOT NULL " +
                                "check(" +
                                "type = \"questionMessage\" " +
                                "OR type = \"answer\" " +
                                "OR type = \"extraParameter\"" +
                                "), ",
                        "value NOT NULL"
                }
        );
    }
}
