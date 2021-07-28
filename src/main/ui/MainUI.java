package ui;

import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ui.components.FolderView;
import ui.components.QuestionView;
import ui.components.SubjectView;
import ui.components.TopMenu;

public class MainUI{

    private final static MainUI mainUI = new MainUI();
    private Stage primaryStage;
    private final VBox root = new VBox();
    private final Scene scene = new Scene(root);
    private final TopMenu topMenu = TopMenu.getInstance();
    private final SubjectView subjectView = SubjectView.getInstance();
    private final FolderView folderView = FolderView.getInstance();
    private final QuestionView questionView = QuestionView.getInstance();

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
        setUpGUI();

    }

    /**
     * <p>This method will simply start/show the {@link MainUI}.</p>
     */
    public void start() {
        primaryStage.show();
    }

    private void setUpGUI(){
        HBox hBox = new HBox();
        hBox.getChildren().addAll(
                folderView.getFolderView(),
                questionView.getQuestionView()
        );
        root.getChildren().addAll(
                topMenu.getTopMenu(),
                subjectView.getSubjectView(),
                hBox
        );
    }
    private void debug(boolean status) {
        if (status) {
            this.primaryStage.widthProperty().addListener((observable, oldValue, newValue) ->
                System.out.println("Weite: " + newValue)
            );
            this.primaryStage.heightProperty().addListener((observable, oldValue, newValue) ->
                System.out.println("Höhe: " + newValue)
            );
        }
    }
}
