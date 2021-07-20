package logic;

public class FolderController {

    private final static FolderController folderController = new FolderController();

    private FolderController(){}

    public static FolderController getInstance() {
        return folderController;
    }
}
