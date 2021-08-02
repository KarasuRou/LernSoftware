package ui.components;

import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Folder;

public class FolderView{

    public final Property<Number> selectedFolder = new SimpleIntegerProperty();

    private final static FolderView folderView = new FolderView();
    private final VBox root = new VBox();
    private final Property<Number> width = new SimpleDoubleProperty();
    private final Property<Number> height = new SimpleDoubleProperty();


    static {
        folderView.root.setPadding(new Insets(4, 0, 0, 0));
        folderView.root.setSpacing(8);
        Folder folder = new Folder();
        folder.setID(0);
        folder.setName("Test Ordner");
        folderView.addFolder(folder);
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

    /**
     * <p>Adds a new Folder to the GUI.</p>
     * <p>Adds automatically the ContextMenu and the EventHandler.</p>
     * @param folder requires a {@link Folder}
     */
    public void addFolder(Folder folder){
        Label label = new Label(folder.getName().getValue(), getFolderImage());
        label.setId(String.valueOf(folder.getID()));
        label.setContextMenu(getContextMenu(folder));
        label.setOnMouseClicked(getEventHandler(label));
        this.root.getChildren().add(label);
    }

    /**
     * <p>Renames the Folder in the GUI.</p>
     * @param folder requires the already updated {@link Folder}
     */
    public void renameFolder(Folder folder){
        for (Node temp : root.getChildren() ) {
            if (temp.getId().equals(String.valueOf(folder.getID()))) {
                ((Label) temp).setText(folder.getName().getValue());
                return;
            }
        }
    }

    /**
     * <p>Deletes the Folder in the GUI.</p>
     * @param folder requires a {@link Folder}
     */
    public void deleteFolder(Folder folder){
        for (Node temp : root.getChildren() ) {
            if (temp.getId().equals(String.valueOf(folder.getID()))) {
                root.getChildren().remove(temp);
                return;
            }
        }
    }

    private EventHandler<MouseEvent> getEventHandler(Label label) {
        return event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                selectedFolder.setValue(Integer.parseInt(label.getId()));
//                QuestionController.getInstance().updateCurrentContext();
            }
        };
    }

    private ContextMenu getContextMenu(Folder folder) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem rename = new MenuItem("Umbenennen");
        rename.setOnAction(event -> getRenamePopUp(folder));
        MenuItem delete = new MenuItem("Löschen");
        delete.setOnAction(event -> getDeletePopUp(folder));

        contextMenu.getItems().addAll(rename, delete);
        return contextMenu;
    }

    private void getRenamePopUp(Folder folder) {
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPrefWidth(300);
        vBox.setPadding(new Insets(20));
        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.setTitle("Ordner Umbenennen");

        Label label = new Label("Wie soll der Ordner heißen?");
        TextField textField = new TextField();

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        Button renameButton = new Button("Umbenennen");
        Button cancelButton = new Button("Abbrechen");
        hBox.getChildren().addAll(renameButton, cancelButton);

        renameButton.setOnAction(event -> {
                folder.setName(textField.getText());
                renameFolder(folder); // TODO later over Controller
                stage.close();
        });
        textField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                folder.setName(textField.getText());
                renameFolder(folder); // TODO later over Controller
                stage.close();
            }
        });
        cancelButton.setOnAction(event -> stage.close());

        vBox.getChildren().addAll(label, textField, hBox);
        stage.show();
    }

    private void getDeletePopUp(Folder folder) {
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPrefWidth(300);
        vBox.setPadding(new Insets(20));
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.setTitle("Ordner Löschen");

        Label label = new Label("Ordner wirklich löschen?");
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER);
        Button deleteButton = new Button("Löschen");
        deleteButton.setOnAction(event -> {
            deleteFolder(folder);
            stage.close();
        });
        Button cancelButton = new Button("Abbrechen");
        cancelButton.setOnAction(event -> stage.close());
        hBox.getChildren().addAll(deleteButton, cancelButton);

        vBox.getChildren().addAll(label, hBox);
        stage.show();
    }

    private static ImageView getFolderImage() {
        ImageView imageView = new ImageView(new Image("folder.jpg"));
        imageView.setMouseTransparent(true);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        return imageView;
    }
}
