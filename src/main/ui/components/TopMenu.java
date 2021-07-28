package ui.components;

import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class TopMenu {

    private final static TopMenu topMenu = new TopMenu();
    private final MenuBar root = new MenuBar();

    static {
        Menu menu = new Menu("Test Menu");
        menu.getItems().add(new MenuItem("Test Item"));
        topMenu.root.getMenus().addAll(menu);
    }
    private TopMenu(){}

    /**
     * <p>This Method is the "Constructor" for the TopMenu class.</p>
     * <p>This is the only way to access the TopMenu.</p>
     * @return a {@link TopMenu} instance
     */
    public static TopMenu getInstance() {
        return topMenu;
    }

    /**
     * <p>This will return view of the TopMenu class.</p>
     * @return the {@link TopMenu} {@link Node}.
     */
    public Node getTopMenu(){
        return this.root;
    }
}
