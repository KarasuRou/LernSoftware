package data;

import logic.miscellaneous.Output;
import model.Subject;

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
     */
    public void init(){}

    /**
     * <p>This method will update and store the Subject-name.</p>
     * @param name new {@link Subject} name
     * @param ID Subject ID
     * @return a boolean if the update was successful.
     * @throws SQLException if the ID was wrong.
     */
    public boolean updateSubjectName(String name, int ID) throws SQLException{
        String sql = "UPDATE Subject SET Name = '" + name + "' WHERE ID = " + ID;
        database.startUpdateQuery(sql).addBatch();
        Output.write(sql);
        return database.executeUpdateQuery() == 1;
    }

    /**
     * <p>This method will update and store the Subject-picturePath.</p>
     * @param picturePath new picturePath for the {@link Subject}
     * @param ID Subject ID
     * @return a boolean if the update was successful.
     * @throws SQLException if the ID was wrong.
     */
    public boolean updateSubjectPicturePath(String picturePath, int ID) throws SQLException {
        String picture = "'" + picturePath + "'";
        if (picturePath == null) {
            picture = "null";
        }
        String sql = "UPDATE Subject SET PicturePath = " + picture + " WHERE ID = " + ID;
        database.startUpdateQuery(sql).addBatch();
        Output.write(sql);
        return database.executeUpdateQuery() == 1;
    }

    /**
     * <p>Returns a ResultSet that contains an array of {@link Subject}s</p>
     * @return a ResultSet that contains a {@link Subject}[].
     * @throws SQLException if anything gone wrong with the Subject data.
     */
    public ResultSet getAllSubjects() throws SQLException {
        String sql = "SELECT * FROM Subject;";
        Output.write(sql);
        return database.executeSelectQuery(sql);
    }

    /**
     * <p>This will return the {@link Subject} count.</p>
     * @return the {@link Subject} count.
     * @throws SQLException if anything gone wrong with the Subject data
     */
    public int getSubjectCount() throws SQLException{
        String sql = "SELECT count(*) FROM Subject";
        Output.write(sql);
        return database.executeSelectQuery(sql).getInt(1);
    }

    /**
     * <p>This method will create and store a Subject, that contains a name and picturePath.</p>
     * <p>Also returns the created ID.</p>
     * @param name the Subject name
     * @param picturePath the background-picture path
     * @return the generated ID
     * @throws SQLException if the Database had an error.
     */
    public int createSubject(String name, String picturePath) throws SQLException{
        String sql = "INSERT INTO Subject (Name, PicturePath) VALUES ('" + name + "','" + picturePath + "')";
        database.startInsertQuery(sql).addBatch();
        Output.write(sql);
        return database.executeInsertQuery();
    }

    /**
     * <p>This method will create and store a Subject, that only contains a name.</p>
     * <p>The picturePath is null, so the default background is used.</p>
     * <p>Also returns the created ID.</p>
     * @param name the Subject name
     * @return the generated ID
     * @throws SQLException if the Database had an error.
     */
    public int createSubject(String name) throws SQLException{
        String sql = "INSERT INTO Subject (Name) VALUES ('" + name + "')";
        database.startInsertQuery(sql).addBatch();
        Output.write(sql);
        return database.executeInsertQuery();
    }

    /**
     * <p>This method will delete a Subject.</p>
     * @param ID the Subject ID
     * @return a boolean if the deletion as successful.
     * @throws SQLException if the ID was wrong
     */
    public boolean deleteSubject(int ID) throws SQLException{
        String sql = "DELETE FROM Subject WHERE ID = " + ID;
        database.startDeleteQuery(sql).addBatch();
        Output.write(sql);
        return database.executeDeleteQuery() == 1;
    }
}
