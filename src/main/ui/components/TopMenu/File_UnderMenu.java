package ui.components.TopMenu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

//TODO Create Menu "File"
public class File_UnderMenu {

    private final static File_UnderMenu File_UnderMenu = new File_UnderMenu();
    private final Menu root = new Menu();

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
            System.out.println("Create");
        };
    }

    private MenuItem getLoadBackupFileMenuItem() {
        MenuItem menuItem = new MenuItem("Backup wiederherstellen");
        menuItem.setOnAction(showLoadBackupFileMenu());
        return menuItem;
    }

    private EventHandler<ActionEvent> showLoadBackupFileMenu() {
        return event ->{
            System.out.println("Load");
        };
    }
}