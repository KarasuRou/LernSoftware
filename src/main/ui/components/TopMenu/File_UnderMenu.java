package ui.components.TopMenu;

import data.Database;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import logic.miscellaneous.FileSaver;
import logic.miscellaneous.Output;
import logic.startup.Launcher;
import ui.MainUI;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class File_UnderMenu {

    private final static File_UnderMenu File_UnderMenu = new File_UnderMenu();
    private final Menu root = new Menu();
    private final static String directory = Launcher.APPLICATION_PATH + Launcher.FILE_SEPARATOR + "Backup";
    private final static String currentDatabase = Launcher.APPLICATION_PATH + Launcher.FILE_SEPARATOR + "Storage.db";

    static {
        File_UnderMenu.root.setText("Datei");
        File_UnderMenu.setMenu();
    }

    private File_UnderMenu(){}

    public static File_UnderMenu getInstance() {
        return File_UnderMenu;
    }

    public Menu getFile() {
        return root;
    }

    private void setMenu() {
        MenuItem createBackupFile = getCreateBackupFileMenuItem();
        MenuItem loadBackupFile = getLoadBackupFileMenuItem();

        root.getItems().addAll(createBackupFile, loadBackupFile);
    }

    private MenuItem getCreateBackupFileMenuItem() {
        MenuItem menuItem = new MenuItem("Backup erstellen");
        menuItem.setOnAction(showCreateBackupFileMenu());
        return menuItem;
    }

    private EventHandler<ActionEvent> showCreateBackupFileMenu() {
        return event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Data Base File",".db"),
                    new FileChooser.ExtensionFilter("Everything","*")
            );
            fileChooser.setInitialDirectory(getInitialDirectory());
            fileChooser.setInitialFileName(new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime()));
            File file = MainUI.getInstance().showFileChooserSaveDialog(fileChooser);
            if (file != null) {
                try {
                    createBackupFromCurrentDatabase(file);
                } catch (Exception e) {
                    Output.exceptionWrite(e);
                }
            }
        };
    }

    private void createBackupFromCurrentDatabase(File newBackup) throws Exception {
        Output.write("Closing Database connection...");
        Database.getInstance().closeDBConnection();
        Output.write("Database connection closed!");

        Output.write("Create Backup from current Database...");
        FileSaver fileSaver = new FileSaver(newBackup.getName(), "Backup");
        fileSaver.copyFileFromOtherFile(currentDatabase,false);
        Output.write("Backup created!");

        Output.write("Reconnection Database...");
        Database.getInstance().initDBConnection();
    }

    private MenuItem getLoadBackupFileMenuItem() {
        MenuItem menuItem = new MenuItem("Backup wiederherstellen");
        menuItem.setOnAction(showLoadBackupFileMenu());
        return menuItem;
    }

    private EventHandler<ActionEvent> showLoadBackupFileMenu() {
        return event ->{
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Everything","*"),
                    new FileChooser.ExtensionFilter("Data Base File",".db")
            );
            fileChooser.setInitialDirectory(getInitialDirectory());
            File file = MainUI.getInstance().showFileChooserOpenDialog(fileChooser);
            if (file != null) {
                try {
                    replaceCurrentDatabaseWithBackup(file);
                } catch (Exception e) {
                    Output.exceptionWrite(e);
                }
            }
        };
    }

    private void replaceCurrentDatabaseWithBackup(File backupFile) throws Exception {
        Output.write("Closing Database connection...");
        Database.getInstance().closeDBConnection();
        Output.write("Database connection closed!");

        Output.write("Load Backup and replace current Database...");
        FileSaver fileSaver = new FileSaver("Storage.db", null);
        fileSaver.copyFileFromOtherFile(backupFile.getPath(), false);
        Output.write("Backup loaded!");

        Output.write("RESTARTING!");
        restart(Launcher.class);
    }

    private File getInitialDirectory() {
        checkIfInitialDirectoryIsExistent();
        return new File(directory);
    }

    private void checkIfInitialDirectoryIsExistent() {
        if (!new File(directory).exists()) {
            new File(directory).mkdir();
        }
    }

    private void restart(Class restartClass) throws IOException, URISyntaxException {
        Output.write("Restarting application...");
        String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        File currentJar = new File(restartClass.getProtectionDomain().getCodeSource().getLocation().toURI());

        if(!currentJar.getName().endsWith(".jar"))
            return;

        ArrayList<String> command = new ArrayList<>();
        command.add(javaBin);
        command.add("-jar");
        command.add(currentJar.getPath());

        ProcessBuilder builder = new ProcessBuilder(command);
        builder.start();
        System.exit(0);
    }
}