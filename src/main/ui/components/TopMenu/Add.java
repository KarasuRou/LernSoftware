package ui.components.TopMenu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.SubjectController;
import model.Folder;
import ui.MainUI;
import ui.components.FolderView;

import java.io.File;

public class Add {

    private final SubjectController subjectController = SubjectController.getInstance();
    private final static Add add = new Add();
    private final Menu root = new Menu();

    static {
        add.root.setText("Hinzufügen");
        add.setMenu();
    }

    private Add() {}

    /**
     * <p>This Method is the "Constructor" for the Add class.</p>
     * <p>This is the only way to access the Add.</p>
     * @return a {@link Add} instance
     */
    public static Add getInstance() {
        return add;
    }

    public Menu getAdd() {
        return root;
    }

    private void setMenu() {
        MenuItem subjectAdd = getSubjectAddMenuItem();
        MenuItem folderAdd = getFolderAddMenuItem();
        MenuItem questionAdd = getQuestionAddMenuItem();

        root.getItems().addAll(subjectAdd, folderAdd, questionAdd);
    }

    private MenuItem getSubjectAddMenuItem() {
        MenuItem menuItem = new MenuItem("Fach(Tab) hinzufügen");
        menuItem.setOnAction(showSubjectAddMenu());
        return menuItem;
    }

    private EventHandler<ActionEvent> showSubjectAddMenu() {
        return event -> {
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            vBox.setPadding(new Insets(20));
            vBox.setSpacing(5);
            Stage stage = getPopUpStage(vBox);
            stage.setTitle("Fach hinzufügen");

            Label label = new Label("Wie soll das Fach(Tab) heißen?");
            HBox subjectInfoBox = new HBox();
            subjectInfoBox.setAlignment(Pos.CENTER);
            subjectInfoBox.setSpacing(10);
            TextField nameTextField = new TextField();
            Button button = new Button("Bild\r\nhinzufügen?");
            button.setTextAlignment(TextAlignment.CENTER);
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Alle Bilder", "*.jpg; *.jpeg; *.png"),
                    new FileChooser.ExtensionFilter("JPG", "*.jpg; *.jpeg"),
                    new FileChooser.ExtensionFilter("PNG", "*.png")
            );

            final File[] file2 = new File[1];

            button.setOnAction(event1 -> {
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                    button.setStyle("-fx-border-color: #00ff00;");
                    button.setDisable(true);
                    button.setText("Bild wurde\r\nhinzugefügt");
                    file2[0] = file;
                }
            });
            subjectInfoBox.getChildren().addAll(nameTextField, button);

            HBox hBox = new HBox();
            hBox.setSpacing(10);
            hBox.setAlignment(Pos.CENTER);
            Button createButton = new Button("Fach erstellen");

            createButton.setOnAction(event1 -> {
                if (nameTextField.getText().equals("")) {
                    nameTextField.setStyle("-fx-border-color: red;");
                    Tooltip tooltip = new Tooltip();
                    tooltip.setText("Bitte einen Namen vergeben!");
                    nameTextField.setTooltip(tooltip);
                    tooltip.show(stage);
                } else {
                    String name = nameTextField.getText();
                    String backgroundPicturePath = null;
                    if (file2[0] != null) {
                        backgroundPicturePath = file2[0].getPath();
                    }
                    subjectController.addSubject(name, backgroundPicturePath);
                    stage.close();
                }
            });
            nameTextField.setOnKeyPressed(event1 -> {
                if (event1.getCode().equals(KeyCode.ENTER)) {
                    createButton.fire();
                }
            });

            Button cancelButton = new Button("Abbrechen");
            cancelButton.setOnAction(event1 -> stage.close());
            hBox.getChildren().addAll(createButton, cancelButton);
            vBox.getChildren().addAll(label, subjectInfoBox, hBox);

            stage.show();
        };
    }

    private MenuItem getFolderAddMenuItem() {
        MenuItem menuItem = new MenuItem("Ordner hinzufügen");
        menuItem.setOnAction(showFolderAddMenu());
        return menuItem;
    }

    private EventHandler<ActionEvent> showFolderAddMenu() {
        return event -> {
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            vBox.setPadding(new Insets(20));
            vBox.setSpacing(5);

            Stage stage = getPopUpStage(vBox);
            stage.setTitle("Ordner hinzufügen");


            Label label = new Label("Wie soll der Ordner heißen?");
            TextField textField = new TextField();

            HBox hBox = new HBox();
            hBox.setSpacing(10);
            hBox.setAlignment(Pos.CENTER);
            Button createButton = new Button("Ordner erstellen");

            createButton.setOnAction(event1 -> {
                if (textField.getText().equals("")) {
                textField.setStyle("-fx-border-color: red;");
                Tooltip tooltip = new Tooltip();
                tooltip.setText("Bitte einen Namen vergeben!");
                textField.setTooltip(tooltip);
                tooltip.show(stage);
            } else {
                Folder folder = new Folder();
                folder.setName(textField.getText());
                folder.setID(0); //TODO
                FolderView.getInstance().addFolder(folder);
                stage.close();
            }
            });
            textField.setOnKeyPressed(event1 -> {
                if (event1.getCode().equals(KeyCode.ENTER)) {
                    createButton.fire();
                }
            });

            Button cancelButton = new Button("Abbrechen");
            cancelButton.setOnAction(event1 -> stage.close());
            hBox.getChildren().addAll(createButton, cancelButton);
            vBox.getChildren().addAll(label, textField, hBox);

            stage.show();
        };
    }

    private MenuItem getQuestionAddMenuItem() {
        MenuItem menuItem = new MenuItem("Frage hinzufügen");
        menuItem.setOnAction(showQuestionAddMenu());
        return menuItem;
    }

    private EventHandler<ActionEvent> showQuestionAddMenu() {
        return event -> {
            System.out.println("TODO: Add Question MenuItem");
            // TODO show Questions and add Scrollbar
        };
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
