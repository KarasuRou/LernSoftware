package logic.startup;

import data.Database;
import data.FolderData;
import data.QuestionData;
import data.SubjectData;
import javafx.application.Application;
import javafx.stage.Stage;
import logic.miscellaneous.Output;
import ui.MainUI;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Launcher extends Application {

    public static final String APPLICATION_PATH = System.getProperty("user.dir");
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");

    public static void main(String[] args) {
        Output.write("Application Starting...", "SYSTEM");
        createShutdownHook();
        try {
            Database.getInstance().initDBConnection();
        } catch (Exception e) {
            Output.exceptionWrite(e);
        }

        SubjectData subjectData = SubjectData.getInstance();
        ResultSet resultSet = subjectData.getAllSubjectNames();

        try {
            while (resultSet.next()) {
                String name = "Subject.Name:" + resultSet.getString(1);

                Output.write(name, "DEBUG");

            }
        } catch (SQLException e) {
            Output.exceptionWrite(e);
        }

        FolderData folderData = FolderData.getInstance();
        QuestionData questionData = QuestionData.getInstance();


//        launch();
        System.exit(1);
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

    @Override
    public void start(Stage primaryStage){
        MainUI mainUI = new MainUI(primaryStage);
        mainUI.start();
    }
}
