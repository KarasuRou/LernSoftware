package logic.startup;

import data.Database;
import data.DatabaseMigration;
import javafx.application.Application;
import javafx.stage.Stage;
import logic.FolderController;
import logic.QuestionController;
import logic.SubjectController;
import logic.miscellaneous.Output;
import ui.MainUI;

public class Launcher extends Application {

    public static final String APPLICATION_PATH = System.getProperty("user.dir");
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");

    public static void main(String[] args) {
        createShutdownHook();
        try {
            Output.write("Preparing...", "SYSTEM");
            Database.getInstance().initDBConnection();
            DatabaseMigration.getInstance().start();
            Output.write("Preparing successful!", "SYSTEM");

            Output.write("Starting Initiation...", "SYSTEM");
            SubjectController.getInstance().init();
            FolderController.getInstance().init();
            QuestionController.getInstance().init();
            Output.write("Initiation successful!", "SYSTEM");

            Output.write("Application Starting...", "SYSTEM");
            launch();
        } catch (Exception e) {
            Output.exceptionWrite(e);
        }

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
