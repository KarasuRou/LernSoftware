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

    private final Stage primaryStage;
    private final VBox root = new VBox();
    private final Scene scene = new Scene(root);
    private final TopMenu topMenu = new TopMenu();
    private final SubjectView subjectView = new SubjectView();
    private final FolderView folderView = new FolderView();
    private final QuestionView questionView = new QuestionView();

    public MainUI(Stage primaryStage){
        primaryStage.setTitle("Lern Software");
        this.primaryStage = primaryStage;
        this.primaryStage.setScene(this.scene);
        setUpGUI();

//        this.primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println("Weite: " + newValue);
//        });
//        this.primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println("Höhe: " + newValue);
//        });
    }

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
}
