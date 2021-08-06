package ui.components.TopMenu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Folder;
import model.Subject;
import ui.MainUI;
import ui.components.FolderView;
import ui.components.SubjectView;

import java.io.File;

public class Add {

    private final static Add add = new Add();
    private final Menu root = new Menu();

    static {
        add.root.setText("Hinzufügen");
        add.setMenu();
    }

    private Add() {}

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
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            MainUI.getInstance().setInitOwner(stage);
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            vBox.setPadding(new Insets(20));
            vBox.setSpacing(5);

            Scene scene = new Scene(vBox);
            stage.setTitle("Fach hinzufügen");
            MainUI.getInstance().setInitOwner(stage);
            stage.setScene(scene);


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
                    Subject subject = new Subject();
                    subject.setName(nameTextField.getText());
                    subject.setID(0); //TODO
                    if (file2[0] != null) {
                        subject.setBackgroundPicturePath(file2[0].getPath());
                    }
                    SubjectView.getInstance().addSubjectTab(subject);
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
            Stage stage = new Stage();
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            vBox.setPadding(new Insets(20));
            vBox.setSpacing(5);

            Scene scene = new Scene(vBox);
            stage.setTitle("Ordner hinzufügen");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            MainUI.getInstance().setInitOwner(stage);


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
}
