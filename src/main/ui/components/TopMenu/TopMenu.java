package ui.components.TopMenu;

import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import logic.miscellaneous.Output;

public class TopMenu {

    private final static TopMenu topMenu = new TopMenu();
    private final MenuBar root = new MenuBar();
    private final Property<Number> width = new SimpleDoubleProperty();
    private final Property<Number> height = new SimpleDoubleProperty();

    static {
        topMenu.root.getMenus().addAll(
                File.getInstance().getFile(),
                Add.getInstance().getAdd(),
                Help.getInstance().getHelp());
//        topMenu.debug();
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
     * <p>This method will bind the scene width/height into this class.</p>
     * @param width current scene widthProperty
     * @param height current scene heightProperty
     */
    public void transferSizeProperty(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height) {
        if (this.width.isBound()) {
            this.width.unbind();
        }
        this.width.bind(width);
        if (this.height.isBound()) {
            this.height.unbind();
        }
        this.height.bind(height);

        this.root.setPrefWidth(this.width.getValue().doubleValue());
        this.width.addListener((observable, oldValue, newValue) -> this.root.setPrefWidth((Double) newValue));

        this.root.setMinHeight(36);
    }


    private void debug() {
        for (int i = 0; i < root.getMenus().size(); i++) {
            int finalI = i;
            root.getMenus().get(i).setOnAction(event -> {
                MenuItem menuItem = (MenuItem) event.getTarget();
                String output = "Item: \"" + menuItem.getText() + "\" (Index: " + menuItem.getParentMenu().getItems().indexOf(menuItem) + ")" +
                        "  pressed in Menu: \"" + root.getMenus().get(finalI).getText() + "\" (Index: " + finalI + ")";
                Output.write(output);
            });
        }
    }
    /**
     * <p>This will return view of the TopMenu class.</p>
     * @return the {@link TopMenu} {@link Node}.
     */
    public Node getTopMenu(){
        return this.root;
    }
}
