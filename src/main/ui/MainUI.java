package ui;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.miscellaneous.Output;
import ui.components.FolderView;
import ui.components.QuestionView;
import ui.components.SubjectView;
import ui.components.TopMenu.TopMenu;

import java.io.File;
import java.util.List;

public class MainUI{

//    public final static double MAX_HEIGHT = Screen.getPrimary().getBounds().getHeight();
//    public final static double MAX_WIDTH = Screen.getPrimary().getBounds().getHeight();
    private final static int START_HEIGHT = 400;
    private final static int START_WIDTH = 850;

    private final static MainUI mainUI = new MainUI();
    private Stage primaryStage;
    private final VBox root = new VBox();
    private final Scene scene = new Scene(root, START_WIDTH, START_HEIGHT);
    private final TopMenu topMenu = TopMenu.getInstance();
    private final SubjectView subjectView = SubjectView.getInstance();
    private final FolderView folderView = FolderView.getInstance();
    private final QuestionView questionView = QuestionView.getInstance();

    private final static ReadOnlyDoubleProperty CURRENT_HEIGHT = mainUI.scene.heightProperty();
    private final static ReadOnlyDoubleProperty CURRENT_WIDTH = mainUI.scene.widthProperty();

    private MainUI(){}


    /**
     * <p>This Method is the "Constructor" for the MainUI class.</p>
     * <p>This is the only way to access the MainUI.</p>
     * @return a {@link MainUI} instance
     */
    public static MainUI getInstance() {
        return mainUI;
    }

    /**
     * <p>This method will initiate the {@link MainUI}, if not called, the {@link Stage} is not set!</p>
     * @param primaryStage needs the primaryStage.
     */
    public void init(Stage primaryStage) {
        primaryStage.setTitle("Lern Software");
        this.primaryStage = primaryStage;
        this.primaryStage.setScene(this.scene);
        debug(false);
        topMenu.transferSizeProperty(CURRENT_WIDTH, CURRENT_HEIGHT);
        folderView.transferSizeProperty(CURRENT_WIDTH, CURRENT_HEIGHT);
        subjectView.transferSizeProperty(CURRENT_WIDTH, CURRENT_HEIGHT);
        questionView.transferSizeProperty(CURRENT_WIDTH, CURRENT_HEIGHT);
        setUpGUI();

    }

    /**
     * Set's the owner of the given Stage.
     * @param stage given Stage
     */
    public void setInitOwner(Stage stage) {
        stage.initOwner(this.primaryStage);
    }

    /**
     * <p>This method will simply start/show the {@link MainUI}.</p>
     */
    public void start() {
        primaryStage.show();
    }

    /**
     * @param fileChooser the FileChooser
     * @return the selected file or null if no file has been selected
     */
    public File showFileChooserOpenDialog(FileChooser fileChooser){
        return fileChooser.showOpenDialog(primaryStage);
    }

    /**
     * @param fileChooser the FileChooser
     * @return the selected file or null if no file has been selected
     */
    public File showFileChooserSaveDialog(FileChooser fileChooser){
        return fileChooser.showSaveDialog(primaryStage);
    }

    /**
     * @param fileChooser the FileChooser
     * @return the selected files or null if no file has been selected
     */
    public List<File> showFileChooserOpenMultipleDialog(FileChooser fileChooser) {
        return fileChooser.showOpenMultipleDialog(primaryStage);
    }

    private void setUpGUI(){
        HBox hBox = new HBox();
        hBox.getChildren().addAll(
                folderView.getFolderView(),
                subjectView.getSubjectView()
        );
        root.getChildren().addAll(
                topMenu.getTopMenu(),
                hBox
        );
    }

    private void debug(boolean status) {
        if (status) {
            this.primaryStage.widthProperty().addListener((observable, oldValue, newValue) ->
                Output.write("Weite: " + newValue)
            );
            this.primaryStage.heightProperty().addListener((observable, oldValue, newValue) ->
                Output.write("H??he: " + newValue)
            );
        }
    }
}
