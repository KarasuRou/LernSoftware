package logic;

import data.FolderData;
import ui.components.FolderView;

import java.sql.SQLException;

public class FolderController {

    private final static FolderController folderController = new FolderController();
    private final FolderData folderData = FolderData.getInstance();
    private final FolderView folderView = FolderView.getInstance();

    private FolderController(){}

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
//        folderView.init();
    }
}
