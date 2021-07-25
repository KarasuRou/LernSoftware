package data;

import logic.miscellaneous.Output;
import model.Subject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SubjectData {

    private static final SubjectData subjectData = new SubjectData();
    private final Database database = Database.getInstance();

    private SubjectData() {}

    /**
     * <p>This Method is the "Constructor" for the SubjectData class.</p>
     * <p>This is the only way to access the SubjectData.</p>
     * @return a {@link SubjectData} instance
     */
    public static SubjectData getInstance() {
        return subjectData;
    }

    /**
     * <p>This method will initiate the {@link SubjectData} class.</p>
     * <p><b>NOTE:</b></p>
     * <p>Should always be called before using the {@link SubjectData} class!</p>
     * @throws SQLException if the initiation has a problem.
     */
    public void init() throws SQLException{
        createTableIfNotExists();
    }

    /**
     * <p>This method will update and store the Subject-name.</p>
     * @param name new {@link Subject} name
     * @param id Subject ID
     * @return a boolean if the update was successful.
     */
    public boolean updateSubjectName(String name, int id) {
        try {
            Output.write("UPDATE Subject SET Name = '" + name + "' WHERE ID =  +" + id + "+");
            PreparedStatement preparedStatement = database.startUpdateQuery("UPDATE Subject SET Name = ? WHERE ID = ?");
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, id);
            preparedStatement.addBatch();
            return database.executeUpdateQuery() == 1;
        } catch (SQLException e) {
            Output.exceptionWrite(e);
        }
        return false;
    }

    /**
     * <p>This method will update and store the Subject-picturePath.</p>
     * @param picturePath new picturePath
     * @param id Subject ID
     * @return a boolean if the update was successful.
     */
    public boolean updateSubjectPicturePath(String picturePath, int id) {
        try {
            Output.write("UPDATE Subject SET PicturePath = '" + picturePath + "' WHERE ID =  +" + id + "+");
            PreparedStatement preparedStatement = database.startUpdateQuery("UPDATE Subject SET PicturePath = ? WHERE ID = ?");
            preparedStatement.setString(1, picturePath);
            preparedStatement.setInt(2, id);
            preparedStatement.addBatch();
            return database.executeUpdateQuery() == 1;
        } catch (SQLException e) {
            Output.exceptionWrite(e);
        }
        return false;
    }

    /**
     * <p>Returns a ResultSet that contains an array of {@link Subject}s</p>
     * @return a ResultSet that contains a {@link Subject}[].
     */
    public ResultSet getAllSubjects(){
        ResultSet resultSet = null;
        try {
            resultSet = database.executeSelectQuery("SELECT * FROM Subject;");
        } catch (SQLException e) {
            Output.exceptionWrite(e);
        }
        return resultSet;
    }

    /**
     * <p>This method will create and store a Subject, that contains a name and picturePath.</p>
     * <p>Also returns the created ID.</p>
     * @param name the Subject name
     * @param picturePath the background-picture path
     * @return the generated ID
     */
    public int createSubject(String name, String picturePath){
        try {
            PreparedStatement preparedStatement = database.startInsertQuery("INSERT INTO Subject (Name, PicturePath) VALUES (?,?)");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, picturePath);
            preparedStatement.addBatch();
            return database.executeInsertQuery();
        } catch (SQLException e) {
            Output.exceptionWrite(e);
        }
        return -1;
    }

    /**
     * <p>This method will create and store a Subject, that only contains a name.</p>
     * <p>The picturePath is null, so the default background is used.</p>
     * <p>Also returns the created ID.</p>
     * @param name the Subject name
     * @return the generated ID
     */
    public int createSubject(String name){
        try {
            PreparedStatement preparedStatement = database.startInsertQuery("INSERT INTO Subject (Name) VALUES (?)");
            preparedStatement.setString(1, name);
            preparedStatement.addBatch();
            return database.executeInsertQuery();
        } catch (SQLException e) {
            Output.exceptionWrite(e);
        }
        return -1;
    }

    /**
     * <p>This method will delete a Subject.</p>
     * @param ID the Subject ID
     * @return a boolean if the deletion as successful.
     */
    public boolean deleteSubject(int ID){
        try {
            PreparedStatement preparedStatement = database.startDeleteQuery("DELETE FROM Subject WHERE ID = ?");
            preparedStatement.setInt(1, ID);
            preparedStatement.addBatch();
            return database.executeDeleteQuery() == 1;
        } catch (SQLException e) {
            Output.exceptionWrite(e);
        }
        return false;
    }

    private void createTableIfNotExists() throws SQLException {
        database.createTableIfNotExists(
            "Subject",
            new String[]{
                "ID INTEGER NOT NULL " +
                    "CONSTRAINT Subject_pk " +
                        "PRIMARY KEY autoincrement, ",
                    "Name TEXT NOT NULL, ",
                    "PicturePath TEXT"
            }
        );
        database.createUniqueIndexIfNotExists("Subject_Name_uindex", "Subject", "Name");
    }
}
