package ui.components;

import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
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
import logic.miscellaneous.Output;
import model.Folder;
import ui.MainUI;

public class FolderView{

    public final Property<Number> selectedFolder = new SimpleIntegerProperty();

    private final static FolderView folderView = new FolderView();
    private final VBox root = new VBox();
    private final Property<Number> width = new SimpleDoubleProperty();
    private final Property<Number> height = new SimpleDoubleProperty();
    private final Property<Number> boundSubject = new SimpleIntegerProperty();


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
        SubjectView.getInstance().bindExternProperty(boundSubject);

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
                "-fx-border-width: 0.2px 0.2px 0 0;" +
                "-fx-padding: 4,0,0,0;" +
                "-fx-spacing: 8;");

//        root.getChildren().addAll(new Label("Deutsch", getFolderImage()),
//                new Label("Englisch", getFolderImage()),
//                new Label("Mathematik", getFolderImage()),
//                new Label("Naturwissenschaften", getFolderImage()));
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
        addFolder(folder, true);
    }

    public void addFolder(Folder folder, boolean outputWrite) {
        Label label = new Label(folder.getName().getValue(), getFolderImage());
        label.setId(String.valueOf(folder.getID()));
        if (outputWrite) {
            Output.write("Add Folder: " + folder.getName().getValue() + " (ID: " + folder.getID() + ")");
        }
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
                Output.write("Renaming Folder to: " + folder.getName().getValue() + " (ID: " + folder.getID() + ")");
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
                Output.write("Deleting Folder: " + folder.getName().getValue() + " (ID: " + folder.getID() + ")");
                root.getChildren().remove(temp);
                return;
            }
        }
    }

    /**
     * <p>The given {@code property} will be bound to selectedFolder property.</p>
     * @param property extern Property
     */
    public void bindExternProperty(Property<Number> property) {
        property.bind(selectedFolder);
    }

    private EventHandler<MouseEvent> getEventHandler(Label label) {
        return event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                selectedFolder.setValue(Integer.parseInt(label.getId()));
                highlightSelectedFolder();
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
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPrefWidth(300);
        vBox.setPadding(new Insets(20));
        Stage stage = getPopUpStage(vBox);
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
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPrefWidth(300);
        vBox.setPadding(new Insets(20));
        vBox.setAlignment(Pos.CENTER);
        Stage stage = getPopUpStage(vBox);
        stage.setTitle("Ordner Löschen");

        Label label = new Label("Ordner \"" + folder.getName().getValue() + "\" wirklich löschen?");
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

    private void highlightSelectedFolder() {
        for (Node label : root.getChildren()) {
            if (label.getId().equals(String.valueOf(selectedFolder.getValue().intValue()))) {
                label.setStyle("-fx-background-color: rgba(173,216,230,0.8)");
            } else {
                label.setStyle(null);
            }
        }
    }

    private static ImageView getFolderImage() {
        ImageView imageView = new ImageView(new Image("folder.jpg"));
        imageView.setMouseTransparent(true);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        return imageView;
    }

    private Stage getPopUpStage(Parent root) {
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        MainUI.getInstance().setInitOwner(stage);
        return stage;
    }
}
