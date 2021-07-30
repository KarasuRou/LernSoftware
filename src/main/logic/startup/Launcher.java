package logic.startup;

import data.Database;
import data.FolderData;
import data.QuestionData;
import data.SubjectData;
import javafx.application.Application;
import javafx.stage.Stage;
import logic.miscellaneous.Output;
import ui.MainUI;

public class Launcher extends Application {

    public static final String APPLICATION_PATH = System.getProperty("user.dir");
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");

    public static void main(String[] args) {
        Output.write("Application Starting...", "SYSTEM");
        createShutdownHook();
        try {
            Database.getInstance().initDBConnection();
            SubjectData.getInstance().init();
            FolderData.getInstance().init();
            QuestionData.getInstance().init();
        } catch (Exception e) {
            Output.exceptionWrite(e);
        }

        launch();
        System.exit(1);
    }

    @Override
    public void start(Stage primaryStage){
        MainUI mainUI = MainUI.getInstance();
        mainUI.init(primaryStage);
        mainUI.start();
    }

    private static void createShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                Database.getInstance().closeDBConnection();
                Output.write("Application Stopping...","SYSTEM");
                Output.close();
            } catch (Exception exception) {
                Output.exceptionWrite(exception);
            }
        }));
    }
}
