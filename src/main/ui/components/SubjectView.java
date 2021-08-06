package ui.components;

import com.sun.istack.internal.Nullable;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.miscellaneous.Output;
import model.Subject;
import ui.MainUI;

import java.io.File;

public class SubjectView {

    private final static SubjectView subjectView = new SubjectView();
    private final TabPane root = new TabPane();
    private final Property<Number> selectedSubject = new SimpleIntegerProperty();
    private final Property<Number> width = new SimpleDoubleProperty();
    private final Property<Number> height = new SimpleDoubleProperty();
    private final QuestionView questionView = QuestionView.getInstance(); // For question content

    static {
        subjectView.selectedSubject.bind(subjectView.root.getSelectionModel().selectedIndexProperty());

        Subject subject = new Subject();
        subject.setName("Deutsch");
        subject.setID(0);
        Subject subject1 = new Subject();
        subject1.setName("Englisch");
        subject1.setID(1);
        Subject subject2 = new Subject();
        subject2.setName("Mathematik");
        subject2.setID(2);
        subjectView.addSubjectTab(subject);
        subjectView.addSubjectTab(subject1);
        subjectView.addSubjectTab(subject2);


        subjectView.root.getTabs().get(0).setContent(subjectView.questionView.getQuestionView());
        subjectView.root.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            subjectView.root.getTabs().get(oldValue.intValue()).setContent(null);
            String content_ = "";
            for (int i = 0; i < 10; i++) {
                content_ +="Das ist der Text in dem Fach: " + subjectView.root.getTabs().get(newValue.intValue()).getText() + "\n" +
                        "Der vorherige Text war aus: " + subjectView.root.getTabs().get(oldValue.intValue()).getText() + "\n\n";
            }
            subjectView.root.getTabs().get(newValue.intValue()).setContent(new Label(content_));
            subjectView.changeCurrentBackground((String) subjectView.root.getTabs().get(newValue.intValue()).getUserData());
            System.out.println("From: " +subjectView.root.getTabs().get(oldValue.intValue()).getText() + " (" + oldValue + ") " +
                    "to: " +subjectView.root.getTabs().get(newValue.intValue()).getText() + " (" + newValue + ")");
        });
        subjectView.root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        subjectView.root.heightProperty().addListener((observable, oldValue, newValue) -> {
        });
    }

    private SubjectView(){}


    /**
     * <p>This Method is the "Constructor" for the SubjectView class.</p>
     * <p>This is the only way to access the SubjectView.</p>
     * @return a {@link SubjectView} instance
     */
    public static SubjectView getInstance() {
        return subjectView;
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

        this.root.setPrefWidth(this.width.getValue().doubleValue() - 160); // 160 -> FolderView width
        this.width.addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() >= 800) {
                this.root.setPrefWidth((Double) newValue - 160);
            } else {
                this.root.setPrefWidth((Double) newValue * 0.8);
            }
        });

        this.root.setTabMinHeight(40);
        this.root.setMinHeight(120);
        this.root.setPrefHeight(this.height.getValue().doubleValue() * 0.9);
        this.height.addListener((observable, oldValue, newValue) -> this.root.setPrefHeight((Double) newValue * 0.9));

        this.root.setStyle(
                "-fx-border-color: gray;" +
                "-fx-border-style: solid;" +
                "-fx-border-width: 0.2px 0 0 0.2px;"
        );
    }

    /**
     * <p>Adds a new Subject Tab, without content.</p>
     * @param subject required {@link Subject}
     */
    public void addSubjectTab(Subject subject){
        Output.write("Add Subject: " + subject.getName().getValue() + " (ID: " + subject.getID() + ")");
        Tab tab = new Tab(subject.getName().getValue());
        tab.setId(String.valueOf(subject.getID()));
        tab.setUserData(subject.getBackgroundPicturePath());
//        if (subject.getBackgroundPicturePath() != null && !subject.getBackgroundPicturePath().equals("")) {
//            changeBackground(subject.getBackgroundPicturePath()); // TODO setSubject()
//        }
        tab.setContextMenu(getTabContextMenu(subject));
        this.root.getTabs().add(tab);
        System.out.println(this.root.getTabs().indexOf(tab));
    }

    /**
     * <p>Changes the Background for the current Subject.</p>
     * @param path relativ system path
     */
    public void changeCurrentBackground(@Nullable String path) {
        if (path == null) {
            this.root.setBackground(null);
        } else {
            Image image = new Image(new File(path).toURI().toString());
            BackgroundPosition backgroundPosition = new BackgroundPosition(Side.LEFT, 100.0, true, Side.TOP, 100.0, true);
            BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
            Background background = new Background(new BackgroundImage(image, null, null, backgroundPosition, backgroundSize));
            this.root.setBackground(background);
        }
    }

    public void changeBackgroundFromSubjectTab(Subject subject) {
        for (Tab tab : this.root.getTabs()) {
            if (tab.getId().equals(String.valueOf(subject.getID()))) {
                Output.write("Changing Subject Background from: " + subject.getName().getValue() + " (ID: " + subject.getID() + ")");
                tab.setUserData(subject.getBackgroundPicturePath());
                return;
            }
        }
    }

    /**
     * <p>Renames the Subject Tab.</p>
     * @param subject required {@link Subject}
     */
    public void renameSubjectTab(Subject subject){
        for (Tab tab : this.root.getTabs()) {
            if (tab.getId().equals(String.valueOf(subject.getID()))) {
                Output.write("Renaming Subject to: " + subject.getName().getValue() + " (ID: " + subject.getID() + ")");
                tab.setText(subject.getName().getValue());
                return;
            }
        }
    }

    /**
     * <p>Deletes the Subject Tab.</p>
     * @param subject required {@link Subject}
     */
    public void deleteSubjectTab(Subject subject){
        for (Tab tab : this.root.getTabs()) {
            if (tab.getId().equals(String.valueOf(subject.getID()))) {
                Output.write("Delete Subject: " + subject.getName().getValue() + " (ID: " + subject.getID() + ")");
                this.root.getTabs().remove(tab);
                return;
            }
        }
    }

    /**
     * <p>The given {@code property} will be bound to selectedSubject property.</p>
     * @param property extern Property
     */
    public void bindExternProperty(Property<Number> property) {
        property.bind(selectedSubject);
    }

    /**
     * <p>This will return view of the SubjectView class.</p>
     * @return the {@link SubjectView} {@link Node}.
     */
    public Node getSubjectView(){
        return this.root;
    }

    private ContextMenu getTabContextMenu(Subject subject) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem rename = new MenuItem("Umbenennen");
        rename.setOnAction(event -> getRenamePopUp(subject));
        MenuItem changeBackground = new MenuItem("Hintergrund ändern");
        changeBackground.setOnAction(event -> getChangeBackgroundPopUp(subject));
        MenuItem delete = new MenuItem("Löschen");
        delete.setOnAction(event -> getDeletePopUp(subject));

        contextMenu.getItems().addAll(rename, changeBackground, delete);
        return contextMenu;
    }

    private void getRenamePopUp(Subject subject) {
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(20));
        Stage stage = getPopUpStage(vBox);
        stage.setTitle("Fach(Tab) umbenennen");

        Label label = new Label("Wie soll das Fach(Tab) heißen?");
        TextField textField = new TextField();

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        Button renameButton = new Button("Umbenennen");
        renameButton.setOnAction(event -> {
            if (!textField.getText().equals("")) {
                subject.setName(textField.getText());
                renameSubjectTab(subject);
                stage.close();
            } else {
                textField.setStyle("-fx-border-color: red;");
                Tooltip tooltip = new Tooltip("Bitte Namen angeben!");
                tooltip.setAutoHide(false);
                textField.setTooltip(tooltip);
                tooltip.show(stage);
            }
        });
        textField.setOnKeyReleased(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                renameButton.fire();
            }
        });
        Button cancelButton = new Button("Abbrechen");
        cancelButton.setOnAction(event -> stage.close());
        hBox.getChildren().addAll(renameButton, cancelButton);


        vBox.getChildren().addAll(label, textField, hBox);
        stage.show();
    }

    private void getChangeBackgroundPopUp(Subject subject) {
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(20));
        Stage stage = getPopUpStage(vBox);
        stage.setTitle("Fach(Tab) hintergrund ändern");

        Label label = new Label("Hintergrundbild entfernen oder neues Hintergrundbild auswählen?");

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER);
        Button deleteBackground = new Button("Entfernen");
        deleteBackground.setOnAction(event -> {
            subject.setBackgroundPicturePath(null);
            changeBackgroundFromSubjectTab(subject);
            stage.close();
        });
        Button addNewBackground = new Button("Neuen Hintergrund auswählen");
        addNewBackground.setOnAction(event -> {

            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                subject.setBackgroundPicturePath(file.getPath());
                changeBackgroundFromSubjectTab(subject);
                stage.close();
            }
        });
        hBox.getChildren().addAll(deleteBackground, addNewBackground);


        vBox.getChildren().addAll(label, hBox);
        stage.show();
    }

    private void getDeletePopUp(Subject subject) {
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(20));
        Stage stage = getPopUpStage(vBox);
        stage.setTitle("Fach(Tab) löschen");

        Label label = new Label("Fach(Tab) \"" + subject.getName().getValue() + "\" wirklich löschen?");

        HBox hBox = new HBox();
        hBox.setSpacing(10);

        Button deleteButton = new Button("Löschen");
        deleteButton.setOnAction(event -> {
            deleteSubjectTab(subject);
            stage.close();
        });
        Button cancelButton = new Button("Abbrechen");
        cancelButton.setOnAction(event -> stage.close());

        hBox.getChildren().addAll(deleteButton, cancelButton);

        vBox.getChildren().addAll(label, hBox);
        stage.show();
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
