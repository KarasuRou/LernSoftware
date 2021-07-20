package data;

import logic.miscellaneous.Output;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SubjectData {

    private static final SubjectData subjectData = new SubjectData();
    private final Database database = Database.getInstance();

    static{
        try {
            subjectData.createTableIfNotExists();
        } catch (Exception e) {
            Output.exceptionWrite(e);
        }
    }

    private SubjectData() {}

    public static SubjectData getInstance() {
        return subjectData;
    }

    public ResultSet getSubjectInformationWithID(int ID) {
        return executeSelectQuery("SELECT * FROM Subject WHERE ID = " + ID);
    }

    public ResultSet getAllSubjectInformation(){
        return executeSelectQuery("SELECT * FROM Subject;");
    }

    public ResultSet getAllSubjectNames(){
        return executeSelectQuery("SELECT Name FROM Subject;");
    }



    private ResultSet executeSelectQuery(String sql) {
        ResultSet resultSet = null;
        try {
            resultSet = database.executeSelectQuery(sql);
        } catch (SQLException e) {
            Output.exceptionWrite(e);
        }
        return resultSet;
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
