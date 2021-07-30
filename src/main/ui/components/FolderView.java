package ui.components;

import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class FolderView{

    private final static FolderView folderView = new FolderView();
    private final VBox root = new VBox();
    private final Property<Number> width = new SimpleDoubleProperty();
    private final Property<Number> height = new SimpleDoubleProperty();


    static {
        folderView.root.setPadding(new Insets(4, 0, 0, 0));
        folderView.root.setSpacing(8);
    }
    private FolderView(){}

    /**
     * <p>This Method is the "Constructor" for the FolderView class.</p>
     * <p>This is the only way to access the FolderView.</p>
     * @return a {@link FolderView} instance
     */
    public static FolderView getInstance() {
        return folderView;
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

        this.root.setMinWidth(80);
        this.root.setPrefWidth(this.width.getValue().doubleValue() * 0.2);
        this.root.setMaxWidth(200);
        this.width.addListener((observable, oldValue, newValue) -> this.root.setPrefWidth((Double) newValue * 0.2));

        this.root.setPrefHeight(this.height.getValue().doubleValue());
        this.height.addListener((observable, oldValue, newValue) -> this.root.setPrefHeight((Double) newValue));

        this.root.setStyle("-fx-border-color: gray;" +
                "-fx-border-style: solid;" +
                "-fx-border-width: 0.2px 0.2px 0 0;");

        root.getChildren().addAll(new Label("Deutsch", getFolderImage()),
                new Label("Englisch", getFolderImage()),
                new Label("Mathematik", getFolderImage()),
                new Label("Naturwissenschaften", getFolderImage()));
    }

    /**
     * <p>This will return view of the FolderView class.</p>
     * @return the {@link FolderView} {@link Node}.
     */
    public Node getFolderView(){
        return this.root;
    }

    private static ImageView getFolderImage() {
        ImageView imageView = new ImageView(new Image("folder.jpg"));
        imageView.setMouseTransparent(true);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        return imageView;

    }
}
