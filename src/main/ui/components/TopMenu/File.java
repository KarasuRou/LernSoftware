package ui.components.TopMenu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

//TODO Create Menu "File"
public class File {

    private final static File file = new File();
    private final Menu root = new Menu();

    static {
        file.root.setText("Datei");
        file.setMenu();
    }

    private File(){}

    public static File getInstance() {
        return file;
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