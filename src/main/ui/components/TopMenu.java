package ui.components;

import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class TopMenu {

    private final MenuBar root = new MenuBar();

    public TopMenu(){
        Menu menu = new Menu("Test Menu");
        menu.getItems().add(new MenuItem("Test Item"));
        root.getMenus().addAll(menu);
    }

    public Node getTopMenu(){
        return this.root;
    }
}
