package data;

import logic.miscellaneous.Output;
import model.Folder;
import model.Subject;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FolderData {

    private static final FolderData folderData = new FolderData();
    private final Database database = Database.getInstance();

    private FolderData(){}

    /**
     * <p>This Method is the "Constructor" for the FolderData class.</p>
     * <p>This is the only way to access the FolderData.</p>
     * @return a {@link FolderData} instance
     */
    public static FolderData getInstance(){
        return folderData;
    }

    /**
     * <p>This method will initiate the {@link FolderData} class.</p>
     * <p><b>NOTE:</b></p>
     * <p>Should always be called before using the {@link FolderData} class!</p>
     * @throws SQLException if the initiation has a problem.
     */
    public void init() throws SQLException{
        createTableIfNotExists();
    }

    /**
     * <p>Returns a ResultSet that contains an array of {@link Folder}s.</p>
     * @param ID the {@link Subject} ID
     * @return a ResultSet that contains a {@link Folder}[].
     * @throws SQLException if the Subject ID was wrong.
     */
    public ResultSet getFoldersWithSubjectID(int ID) throws SQLException {
        String sql = "SELECT * FROM Folder WHERE f_ID = " + ID;
        Output.write(sql);
        return database.executeSelectQuery(sql);
    }

    /**
     * <p>This will return the {@link Folder} count.</p>
     * @param ID the {@link Subject} ID
     * @return the {@link Folder} count.
     * @throws SQLException if the Subject ID was wrong.
     */
    public int getFolderCountWithSubjectID(int ID) throws SQLException {
        String sql = "SELECT count(*) FROM Folder WHERE f_ID = " + ID;
        Output.write(sql);
        return database.executeSelectQuery(sql).getInt(1);
    }

    /**
     * <p>This method will update the {@link Folder} name.</p>
     * @param ID the {@link Folder} ID
     * @param name the new {@link Folder} name
     * @return a boolean if the update was successful.
     * @throws SQLException if the ID was wrong.
     */
    public boolean updateFolderName(int ID, String name) throws SQLException{
        String sql = "UPDATE Folder SET Name = '" + name + "' WHERE ID = " + ID;
        database.startUpdateQuery(sql).addBatch();
        Output.write(sql);
        return database.executeUpdateQuery() == 1;
    }

    /**
     * <p>This method will create and store a {@link Folder}, inside an {@link Subject}.</p>
     * <p>Also returns the created ID.</p>
     * @param ID the {@link Subject} ID
     * @param name the {@link Folder} name
     * @return the generated ID
     * @throws SQLException if the Database had an error.
     */
    public int createFolder(int ID, String name) throws SQLException {
        String sql = "INSERT INTO Folder (f_ID, Name) VALUES (" + ID + ",'" + name + "')";
        database.startInsertQuery(sql).addBatch();
        Output.write(sql);
        return database.executeInsertQuery();
    }

    /**
     * <p>This method will delete a {@link Folder}.</p>
     * @param ID the {@link Folder} ID
     * @return a boolean if the deletion was successful.
     * @throws SQLException if the ID was wrong.
     */
    public boolean deleteFolder(int ID) throws SQLException {
        String sql = "DELETE FROM Folder WHERE ID = " + ID;
        database.startDeleteQuery(sql).addBatch();
        Output.write(sql);
        return database.executeDeleteQuery() == 1;
    }

    private void createTableIfNotExists() throws SQLException {
        database.createTableIfNotExists(
                "Folder",
                new String[]{
                        "ID INTEGER NOT NULL " +
                            "CONSTRAINT Folder_pk " +
                                "PRIMARY KEY AUTOINCREMENT, ",
                        "f_ID INTEGER NOT NULL " +
                                "CONSTRAINT Folder__Subject_fk " +
                                "REFERENCES Subject " +
                                "ON UPDATE CASCADE ON DELETE CASCADE, ",
                        "Name TEXT NOT NULL"
                }
        );
    }
}
