package data;

import com.sun.istack.internal.Nullable;
import logic.miscellaneous.Output;
import model.Folder;
import model.question.Question;
import model.question.QuestionTyp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuestionData {

    private static final QuestionData questionData = new QuestionData();
    private final Database database = Database.getInstance();

    private QuestionData() {}

    /**
     * <p>This Method is the "Constructor" for the QuestionData class.</p>
     * <p>This is the only way to access the QuestionData.</p>
     * @return a {@link QuestionData} instance
     */
    public static QuestionData getInstance(){
        return questionData;
    }

    /**
     * <p>This method will initiate the {@link QuestionData} class.</p>
     * <p><b>NOTE:</b></p>
     * <p>Should always be called before using the {@link QuestionData} class!</p>
     */
    public void init(){}

    /**
     * <p>Returns a ResultSet that contains an array of {@link Question}(s).</p>
     * @param ID is the {@link Folder} ID
     * @return a ResultSet that contains a {@link Question}[].
     * @throws SQLException if the {@code ID} was wrong.
     */
    public ResultSet getQuestionsWithFolderID(int ID) throws SQLException{
        String sql = "SELECT Q.ID as ID, Q.QuestionType, Qp.type AS typ, Qp.value FROM Question_Params AS Qp " +
                "INNER JOIN Question Q on Q.ID = Qp.f_ID " +
                "INNER JOIN Folder F on F.ID = Q.f_ID WHERE F.ID = " + ID + " " +
                "ORDER BY ID";
        Output.write(sql);
        return database.executeSelectQuery(sql);
    }

    /**
     * <p>Return the amount of the {@link Question}(s) that are in the {@link Folder}.</p>
     * @param ID is the {@link Folder} ID
     * @return the {@link Question} amount.
     * @throws SQLException if the ID was wrong.
     */
    public int getQuestionsCountWithFolderID(int ID) throws SQLException{
        String sql = "SELECT count(*) FROM Question WHERE f_ID = " + ID;
        Output.write(sql);
        return database.executeSelectQuery(sql).getInt(1);
    }

    /**
     * <p>Returns a true if the deletion was successful, a false if the Question-deletion was wrongly executed.</p>
     * @param ID is the {@link Question} ID
     * @return a boolean that shows if the deletion was successful.
     * @throws SQLException if the ID was wrong.
     */
    public boolean deleteQuestionWithQuestionID(int ID) throws SQLException {
        String sql = "DELETE FROM Question WHERE ID = " + ID;
        database.startDeleteQuery(sql).addBatch();
        Output.write(sql);
        int results = database.executeDeleteQuery();
        return resultCheck(results);
    }

    /**
     * <p>Returns a true if the deletion was successful, a false if not all Questions (and Folder) was deleted.</p>
     * @param ID is the {@link Folder} ID
     * @return a boolean that shows if the deletion was successful.
     * @throws SQLException if the ID was wrong.
     */
    public boolean deleteALLQuestionsAndTheFolderWithFolderID(int ID) throws SQLException {
        String sql = "DELETE FROM Folder WHERE ID = " + ID;
        database.startDeleteQuery(sql).addBatch();
        Output.write(sql);
        int results = database.executeDeleteQuery();
        return resultCheck(results);
    }

    /**
     * <p>Update the answerParameter from a Question.</p>
     * @param ID is the {@link Question} ID
     * @param newAnswerParameter is the answerParameter, accordingly to the questionTyp.
     * @param questionTyp is the already set questionTyp.
     * @return a boolean that shows if the update was successful.
     * @throws SQLException if any parameter was wrong.
     */
    public boolean updateAnswerParameterWithQuestionID(int ID, Object newAnswerParameter, @Nullable Object oldAnswerParameter, QuestionTyp questionTyp) throws SQLException {
        String sql = "UPDATE Question_Params Set value = ? WHERE type LIKE 'answer' AND f_ID = " + ID;
        PreparedStatement preparedStatement;
        switch (questionTyp) {
            case UNSET:
                return false;
            case MultipleChoiceQuestion:
                sql = "UPDATE Question_Params Set value = ? WHERE type LIKE 'answer' AND f_ID = " + ID + " AND ID = ?";
                preparedStatement = database.startUpdateQuery(sql);
                boolean[] oldBooleans = (boolean[]) oldAnswerParameter;
                boolean[] newBooleans = (boolean[]) newAnswerParameter;
                int[] ids = getExtraParamsIDs(ID, "answer");
                for (int i = 0; i < 5; i++) {
                    if (oldBooleans[i] != newBooleans[i]) {
                        sql = "UPDATE Question_Params Set value = ? WHERE type LIKE 'answer' AND f_ID = " + ID + " AND ID = " + ids[i];
                        sql = sql.replace("?", String.valueOf(newBooleans[i]));
                        preparedStatement.setBoolean(1, newBooleans[i]);
                        preparedStatement.setInt(2, ids[i]);
                        preparedStatement.addBatch();
                        Output.write(sql);
                    }
                }
                break;
            case DirectQuestion:
            case WordsQuestion:
                preparedStatement = database.startUpdateQuery(sql);
                sql = sql.replace("?", (String) newAnswerParameter);
                preparedStatement.setString(1, (String) newAnswerParameter);
                preparedStatement.addBatch();
                Output.write(sql);
                break;
        }
        int results = database.executeUpdateQuery();
        return resultCheck(results);
    }

    /**
     * <p>Update the questionMessage from a Question.</p>
     * @param ID is the {@link Question} ID
     * @param newQuestionMessage is the questionMessage, accordingly to the questionTyp
     * @param questionTyp is the already set questionTyp
     * @return a boolean that shows if the update was successful.
     * @throws SQLException if any parameter was wrong.
     */
    public boolean updateQuestionMessageParameterWithQuestionID(int ID, Object newQuestionMessage, @Nullable Object oldAnswerParameter, QuestionTyp questionTyp) throws SQLException {
        String sql = "UPDATE Question_Params Set value = ? WHERE type LIKE 'questionMessage' AND f_ID = " + ID;
        PreparedStatement preparedStatement;
        switch (questionTyp) {
            case UNSET:
                return false;
            case MultipleChoiceQuestion:
                sql = "UPDATE Question_Params Set value = ? WHERE type LIKE 'questionMessage' AND f_ID = " + ID + " AND ID = ?";
                preparedStatement = database.startUpdateQuery(sql);
                String[] newStrings = (String[]) newQuestionMessage;
                String[] oldStrings = (String[]) oldAnswerParameter;
                int[] ids = getExtraParamsIDs(ID, "questionMessage");
                for (int i = 0; i < 5; i++) {
                    if (!newStrings[i].equals(oldStrings[i])) {
                        sql = "UPDATE Question_Params Set value = ? WHERE type LIKE 'questionMessage' AND f_ID = " + ID + " AND ID = " + ids[i];
                        sql = sql.replace("?", newStrings[i]);
                        preparedStatement.setString(1, newStrings[i]);
                        preparedStatement.setInt(2, ids[i]);
                        preparedStatement.addBatch();
                        Output.write(sql);
                    }
                }
                break;
            case DirectQuestion:
            case WordsQuestion:
                preparedStatement = database.startUpdateQuery(sql);
                sql = sql.replace("?",(String) newQuestionMessage);
                preparedStatement.setString(1, (String) newQuestionMessage);
                preparedStatement.addBatch();
                Output.write(sql);
                break;
        }
        int results = database.executeUpdateQuery();
        return resultCheck(results);
    }

    /**
     * <p>Update the extraParameter from a Question.</p>
     * @param ID is the {@link Question} ID
     * @param extraParameter is the extraParameter, accordingly to the questionTyp
     * @param questionTyp is the already set questionTyp
     * @return a boolean that shows if the update was successful.
     * @throws SQLException if any parameter was wrong.
     */
    public boolean updateExtraParameterParameterWithQuestionID(int ID, Object extraParameter, QuestionTyp questionTyp) throws SQLException {
        String sql = "UPDATE Question_Params Set value = ? WHERE type LIKE 'extraParameter' AND f_ID = " + ID;
        PreparedStatement preparedStatement = database.startUpdateQuery(sql);
        switch (questionTyp) {
            case UNSET:
            case DirectQuestion:
                break;
            case MultipleChoiceQuestion:
                sql = sql.replace("?", (String) extraParameter);
                preparedStatement.setString(1, (String) extraParameter);
                preparedStatement.addBatch();
                break;
            case WordsQuestion:
                sql = sql.replace("?",String.valueOf((double) extraParameter));
                preparedStatement.setDouble(1, (Double) extraParameter);
                preparedStatement.addBatch();
                break;

        }
        Output.write(sql);
        int results = database.executeUpdateQuery();
        return resultCheck(results);
    }

    /**
     * <p>This method will create and store a Question. BUT ONLY if all parameters were set rightly.</p>
     * <p>Also returns the created ID.</p>
     * @param question is the {@link Question}
     * @param folderID is the {@link Folder} ID
     * @return the created Question ID.
     * @throws SQLException if any parameter was wrong.
     */
    public int createQuestion(Question question, int folderID) throws SQLException{
        String sql = "INSERT INTO Question (f_ID, QuestionType) VALUES (" + folderID + ",'" + question.getQuestionTyp().toString() + "');";
        PreparedStatement preparedStatement =
                database.startInsertQuery("INSERT INTO Question (f_ID, QuestionType) VALUES (?,?);");
        preparedStatement.setInt(1, folderID);
        preparedStatement.setString(2, question.getQuestionTyp().toString());
        preparedStatement.addBatch();
        Output.write(sql);
        int id = database.executeInsertQuery();
        question.setID(id);
        preparedStatement = database.startInsertQuery("INSERT INTO Question_Params (f_ID, type, value) VALUES (?,?,?)");
        switch (question.getQuestionTyp()) {
            case UNSET:
                Output.errorWrite(sql);
                deleteQuestionWithQuestionID(id);
                return -1;
            case MultipleChoiceQuestion:
                for (boolean rightPositions : (boolean[]) question.getAnswer()) {
                    sql = "INSERT INTO Question_Params (f_ID, type, value) VALUES " +
                            "(" + id + ",'" + "answer" + "'," + rightPositions + ")";
                    Output.write(sql);
                    preparedStatement.setInt(1, id);
                    preparedStatement.setString(2, "answer");
                    preparedStatement.setBoolean(3, rightPositions);
                    preparedStatement.addBatch();
                }
                for (String possibilities : (String[]) question.getQuestionMessage()) {
                    sql = "INSERT INTO Question_Params (f_ID, type, value) VALUES " +
                            "(" + id + ",'" + "questionMessage" + "','" + possibilities + "')";
                    Output.write(sql);
                    preparedStatement.setInt(1, id);
                    preparedStatement.setString(2, "questionMessage");
                    preparedStatement.setString(3, possibilities);
                    preparedStatement.addBatch();
                }
                sql = "INSERT INTO Question_Params (f_ID, type, value) VALUES " +
                        "(" + id + ",'" + "extraParameter" + "','" + question.getExtraParameter() + "')";
                Output.write(sql);
                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, "extraParameter");
                preparedStatement.setString(3, (String) question.getExtraParameter());
                preparedStatement.addBatch();
                break;
            case WordsQuestion:
                sql = "INSERT INTO Question_Params (f_ID, type, value) VALUES " +
                        "(" + id + ",'" + "questionMessage" + "','" + question.getQuestionMessage() + "')";
                Output.write(sql);
                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, "questionMessage");
                preparedStatement.setString(3, question.getQuestionMessage().toString());
                preparedStatement.addBatch();

                sql = "INSERT INTO Question_Params (f_ID, type, value) VALUES " +
                        "(" + id + ",'" + "answer" + "','" + question.getAnswer() + "')";
                Output.write(sql);
                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, "answer");
                preparedStatement.setString(3, question.getAnswer().toString());
                preparedStatement.addBatch();

                sql = "INSERT INTO Question_Params (f_ID, type, value) VALUES " +
                        "(" + id + ",'" + "extraParameter" + "','" + question.getExtraParameter() + "')";
                Output.write(sql);
                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, "extraParameter");
                preparedStatement.setDouble(3, (Double) question.getExtraParameter());
                preparedStatement.addBatch();
                break;
            case DirectQuestion:
                sql = "INSERT INTO Question_Params (f_ID, type, value) VALUES " +
                        "(" + id + ",'" + "questionMessage" + "','" + question.getQuestionMessage() + "')";
                Output.write(sql);
                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, "questionMessage");
                preparedStatement.setString(3, question.getQuestionMessage().toString());
                preparedStatement.addBatch();

                sql = "INSERT INTO Question_Params (f_ID, type, value) VALUES " +
                        "(" + id + ",'" + "answer" + "','" + question.getAnswer() + "')";
                Output.write(sql);
                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, "answer");
                preparedStatement.setString(3, question.getAnswer().toString());
                preparedStatement.addBatch();
                break;
        }
        return database.executeInsertQuery();
    }



    private boolean resultCheck(int results) {
        switch (results) {
            case -1:
            case 0:
                return false;
            default:
                return true;
        }
    }

    private int[] getExtraParamsIDs(int id, String questionTyp) throws SQLException {
        int[] ids = new int[5];
        ResultSet resultSet = database
                .executeSelectQuery(
                        "SELECT ID FROM Question_Params WHERE f_ID = " + id + " " +
                                "AND type LIKE '" + questionTyp + "'");
        for (int i = 0; i < 5; i++) {
            resultSet.next();
            ids[i] = resultSet.getInt(1);
        }
        return ids;
    }
}
