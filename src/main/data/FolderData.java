package data;

import logic.miscellaneous.Output;

import java.sql.SQLException;

public class FolderData {

    private static final FolderData folderData = new FolderData();
    private final Database database = Database.getInstance();

    static{
        try {
            folderData.createTableIfNotExists();
        } catch (Exception e) {
            Output.exceptionWrite(e);
        }
    }

    private FolderData(){}

    public static FolderData getInstance(){
        return folderData;
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
