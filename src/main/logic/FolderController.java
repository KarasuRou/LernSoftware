package logic;

import com.sun.istack.internal.Nullable;
import data.FolderData;
import data.QuestionData;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import logic.miscellaneous.Output;
import model.Folder;
import ui.components.FolderView;
import ui.components.SubjectView;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FolderController {

    private final static FolderController folderController = new FolderController();
    private final FolderData folderData = FolderData.getInstance();
    private final FolderView folderView = FolderView.getInstance();
    private final Property<Number> selectedSubjectID = new SimpleIntegerProperty();

    private FolderController(){}

    /**
     * <p>This method will create and add a Folder to the current selectedSubject.</p>
     * @param name the Folder name
     */
    public void addFolder(String name){
        try {
            Folder folder = new Folder();
            folder.setName(name);
            int id = folderData.createFolder(selectedSubjectID.getValue().intValue(), name);
            folder.setID(id);
            Output.write("Add Folder: " + folder.getName().getValue() + " (ID: " + folder.getID() + ")");
            folderView.addFolder(folder);
        } catch (SQLException e) {
            Output.exceptionWrite(e, "Failed to addFolder");
        }
    }

    /**
     * <p>This method will rename a Folder.</p>
     * @param folder the current Folder
     * @param newName new name
     */
    public void renameFolder(Folder folder, String newName){
        try {
            folderData.updateFolderName(folder.getID(), newName);
            Output.write("Renaming Folder to: " + newName + " (ID: " + folder.getID() + ")");
            folderView.renameFolder(folder, newName);
            folder.setName(newName);
        } catch (SQLException e) {
            Output.exceptionWrite(e, "Failed to renameFolder");
        }
    }

    /**
     * <p>This method will delete the Folder.</p>
     * @param folder the current Folder
     */
    public void deleteFolder(Folder folder){
        try {
            folderData.deleteFolder(folder.getID());
            QuestionData.getInstance().deleteALLQuestionsAndTheFolderWithFolderID(folder.getID());
            Output.write("Deleting Folder: " + folder.getName().getValue() + " (ID: " + folder.getID() + ")");
            folderView.deleteFolder(folder);
        } catch (SQLException e) {
            Output.exceptionWrite(e, "Failed to deleteFolder");
        }
    }

    /**
     * <p>This Method is the "Constructor" for the FolderController class.</p>
     * <p>This is the only way to access the FolderController.</p>
     * @return a {@link FolderController} instance
     */
    public static FolderController getInstance() {
        return folderController;
    }

    /**
     * <p>This method will initiate the Connections between the layers.</p>
     * <p><b>NOTE:</b></p>
     * <p>Should always be called before using any Folder class!</p>
     * @throws SQLException if the SQL-initiation has a problem.
     */
    public void init() throws SQLException {
        folderData.init();
        folderView.init();

        selectedSubjectID.addListener((observable, oldValue, newValue) -> setUp_Folders(oldValue.intValue(), newValue.intValue()));
        SubjectView.getInstance().bindExternProperty_ID(selectedSubjectID);
    }

    private void setUp_Folders(@Nullable int oldID, int newID) {
        if (oldID == -1) {
            return;
        } else {
            try {
                folderView.clearFolders();
                addFolders(newID);
            } catch (SQLException e) {
                Output.exceptionWrite(e);
            }
        }
    }

    private void addFolders(int id) throws SQLException{
        Folder[] folders = new Folder[folderData.getFolderCountWithSubjectID(id)];
        ResultSet resultSet = folderData.getFoldersWithSubjectID(id);
        for (Folder folder : folders) {
            resultSet.next();
            folder = new Folder();
            folder.setID(resultSet.getInt(1));
            folder.setName(resultSet.getString(3));
            folderView.addFolder(folder);
        }
    }
}
