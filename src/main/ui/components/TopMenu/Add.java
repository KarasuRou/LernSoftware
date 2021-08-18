package ui.components.TopMenu;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.FolderController;
import logic.QuestionController;
import logic.SubjectController;
import model.question.Question;
import model.question.QuestionTyp;
import ui.MainUI;

import java.io.File;

public class Add {

    private final SubjectController subjectController = SubjectController.getInstance();
    private final FolderController folderController = FolderController.getInstance();
    private final QuestionController questionController = QuestionController.getInstance();
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
                    getNodeError(nameTextField,"Bitte einen Namen vergeben!", stage);
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
                    folderController.addFolder(textField.getText());
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
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            vBox.setPadding(new Insets(20));
            vBox.setSpacing(15);
            vBox.setPrefWidth(460);
            vBox.setPrefHeight(400);
            Stage stage = getPopUpStage(vBox);
            stage.setTitle("Frage hinzufügen");

            VBox choiceBox = new VBox();
            choiceBox.setSpacing(10);
            choiceBox.setAlignment(Pos.CENTER_LEFT);
            ToggleGroup toggleGroup = new ToggleGroup();
            RadioButton wordsQuestionButton = new RadioButton("Normale Frage");
            wordsQuestionButton.setToggleGroup(toggleGroup);
            RadioButton directQuestionButton = new RadioButton("Einwort Frage");
            directQuestionButton.setToggleGroup(toggleGroup);
            RadioButton multipleChoiceQuestionButton = new RadioButton("MultipleChoice Frage");
            multipleChoiceQuestionButton.setToggleGroup(toggleGroup);
            choiceBox.getChildren().addAll(wordsQuestionButton, directQuestionButton, multipleChoiceQuestionButton);

            VBox infoBox = new VBox();
            infoBox.setStyle("-fx-border-color: black;" +
                    "-fx-border-width: 0.2px;" +
                    "-fx-border-style: solid;" +
                    "-fx-spacing: 5");


            toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) ->{
                infoBox.getChildren().removeAll(infoBox.getChildren());
                switch (((RadioButton) newValue).getText()) {
                    case "Normale Frage":
                        getDirectQuestion(infoBox, stage, observable);
                        infoBox.setAlignment(Pos.TOP_LEFT);
                        break;
                    case "Einwort Frage":
                        getWordsQuestion(infoBox, stage, observable);
                        infoBox.setAlignment(Pos.TOP_LEFT);
                        break;
                    case "MultipleChoice Frage":
                        getMultipleChoiceQuestion(infoBox, stage, observable);
                        infoBox.setAlignment(Pos.TOP_LEFT);
                        break;
                    default:
                        Label label = new Label("ERROR");
                        label.setFont(Font.font(label.getFont().getSize() * 2));
                        label.setTextFill(Paint.valueOf("red"));
                        infoBox.getChildren().add(label);
                        infoBox.setAlignment(Pos.CENTER);
                        break;
                }

            });


            if (questionController.folderIsSelected()) {
                vBox.getChildren().addAll(choiceBox, infoBox);
            } else {
                Label label = new Label("Bitte einen Ordner auswählen!");
                vBox.getChildren().add(label);
            }
            stage.show();
        };
    }

    private void getDirectQuestion(VBox vBox, Stage stage, ObservableValue<? extends Toggle> observable){
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(5);
        gridPane.setHgap(10);

        Label questionMessageLabel = new Label("Wie lautet die Frage?");
        gridPane.add(questionMessageLabel, 0, 0);
        TextField questionMessageTextField = new TextField();
        gridPane.add(questionMessageTextField, 1, 0);

        Label answerLabel = new Label("Wie ist die richtige Antwort?");
        gridPane.add(answerLabel, 0, 1);
        TextField answerTextField = new TextField();
        gridPane.add(answerTextField, 1, 1);

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(5);
        Button createQuestionButton = new Button("Frage erstellen");
        Button cancelButton = new Button("Abbrechen");
        buttonBox.getChildren().addAll(createQuestionButton, cancelButton);
        gridPane.add(buttonBox, 0, 2, 2, 1);


        answerTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                createQuestionButton.fire();
            }
        });
        questionMessageTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                createQuestionButton.fire();
            }
        });
        createQuestionButton.setOnAction(event -> {
            clearNodeError(answerTextField);
            clearNodeError(questionMessageTextField);
            if (questionMessageTextField.getText().equals("")) {
                getNodeError(questionMessageTextField, "Bitte eine Frage angeben!", stage);
            } else if (answerTextField.getText().equals("")) {
                getNodeError(answerTextField, "Bitte eine Antwort angeben!", stage);
            } else {
                questionController.addQuestion(QuestionTyp.DirectQuestion, questionMessageTextField.getText(), answerTextField.getText(), null);
                stage.close();
            }

        });
        cancelButton.setOnAction(event -> stage.close());

        vBox.getChildren().add(gridPane);

        observable.addListener((observable1, oldValue, newValue) -> {
            clearNodeError(answerTextField);
            clearNodeError(questionMessageTextField);
        });
    }

    private void getWordsQuestion(VBox vBox, Stage stage, ObservableValue<? extends Toggle> observableValue) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(5);

        Label questionMessageLabel = new Label("Wie lautet die Frage?");
        TextField questionMessageTextField = new TextField();
        gridPane.add(questionMessageLabel, 0, 0);
        gridPane.add(questionMessageTextField, 1, 0);

        Label answerLabel = new Label("Wie lautet das richtige Wort?");
        TextField answerTextField = new TextField();
        gridPane.add(answerLabel, 0, 1);
        gridPane.add(answerTextField, 1, 1);

        Label extraParameterLabel = new Label(
                "Wie hoch soll die Genauigkeit sein?\r\n" +
                        "(Standard: " + (int) (Question.DEFAULT_EXTRA_PARAMETER * 100) + "% / mind. 1 Buchstabe)");
        TextField extraParameterTextField = new TextField(String.valueOf((int) (Question.DEFAULT_EXTRA_PARAMETER*100)));
        gridPane.add(extraParameterLabel, 0, 2);
        gridPane.add(extraParameterTextField, 1, 2);
        gridPane.add(new Label("%"), 2, 2);

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(5);
        buttonBox.setAlignment(Pos.CENTER);
        Button cancelButton = new Button("Abbrechen");
        Button createQuestionButton = new Button("Frage erstellen");
        buttonBox.getChildren().addAll(createQuestionButton, cancelButton);
        gridPane.add(buttonBox, 0, 3, 2, 1);

        answerTextField.textProperty().addListener((observable, oldValue, newValue) ->{
            if (newValue.contains(" ")) {
                answerTextField.setText(newValue.replace(" ", ""));
            }
        });
        questionMessageTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                createQuestionButton.fire();
            }
        });
        answerTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                createQuestionButton.fire();
            }
        });
        extraParameterTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                createQuestionButton.fire();
            }
        });
        createQuestionButton.setOnAction(event -> {
            clearNodeError(answerTextField);
            clearNodeError(questionMessageTextField);
            clearNodeError(extraParameterTextField);
            if (questionMessageTextField.getText().equals("")) {
                getNodeError(questionMessageTextField, "Bitte eine Frage angeben!", stage);
            } else if (answerTextField.getText().equals("")) {
                getNodeError(answerTextField, "Bite eine Antwort angeben!", stage);
            } else if (extraParameterTextField.getText().equals("")) {
                getNodeError(extraParameterTextField, "Bitte eine Genauigkeit angeben!", stage);
            } else {
                questionController
                        .addQuestion(
                                QuestionTyp.WordsQuestion,
                                questionMessageTextField.getText(),
                                answerTextField.getText(),
                                Double.parseDouble(extraParameterTextField.getText())
                        );
                stage.close();
            }
        });
        cancelButton.setOnAction(event -> stage.close());

        vBox.getChildren().add(gridPane);

        observableValue.addListener((observable1, oldValue, newValue) -> {
            clearNodeError(answerTextField);
            clearNodeError(questionMessageTextField);
            clearNodeError(extraParameterTextField);
        });
    }

    private void getMultipleChoiceQuestion(VBox vBox, Stage stage, ObservableValue<? extends Toggle> observable) {
        HBox extraParameterBox = new HBox();
        extraParameterBox.setSpacing(5);
        Label extraParameterQuestionLabel = new Label("Wie lautet die Frage?");
        TextField extraParameterQuestionTextField = new TextField();
        extraParameterQuestionTextField.requestFocus();
        extraParameterBox.getChildren().addAll(extraParameterQuestionLabel, extraParameterQuestionTextField);
        vBox.getChildren().add(extraParameterBox);

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(5);
        Button createQuestionButton = new Button("Frage erstellen");
        Button cancelButton = new Button("Abbrechen");
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(createQuestionButton, cancelButton);

        TextField[] textFields = new TextField[5];
        CheckBox[] checkBoxes = new CheckBox[5];

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            HBox hBox = new HBox();

            checkBoxes[i] = new CheckBox();
            checkBoxes[i].selectedProperty().addListener((observable1, oldValue, newValue) ->{
                if (newValue) {
                    for (CheckBox checkBox : checkBoxes) {
                        clearNodeError(checkBox);
                    }
                }
            });
            textFields[i] = new TextField();
            textFields[i].setOnKeyPressed(event -> {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    createQuestionButton.fire();
                }
            });
            textFields[i].textProperty().addListener((observable1, oldValue, newValue) -> {
                if (finalI != 4 && !newValue.equals("")) {
                    textFields[finalI + 1].setDisable(false);
                    checkBoxes[finalI + 1].setDisable(false);
                } else {
                    textFields[finalI + 1].setDisable(true);
                    checkBoxes[finalI + 1].setDisable(true);
                }
            });
            if (i != 0) {
                textFields[i].setDisable(true);
                checkBoxes[i].setDisable(true);
            }

            hBox.getChildren().addAll(new Label("Möglichkeit " + (i + 1) + ":"), checkBoxes[i], textFields[i]);
            vBox.getChildren().add(hBox);

        }

        extraParameterQuestionTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                createQuestionButton.fire();
            }
        });
        createQuestionButton.setOnAction(event -> {
            clearNodeError(extraParameterQuestionTextField);
            for (int i = 0; i < 5; i++) {
                clearNodeError(textFields[i]);
                clearNodeError(checkBoxes[i]);
            }

            boolean questionCanBeCreated = true;
            if (extraParameterQuestionTextField.getText().equals("")) {
                getNodeError(extraParameterQuestionTextField, "Bitte gib eine Frage ein!", stage);
                extraParameterQuestionTextField.requestFocus();
                questionCanBeCreated = false;
            } else {
                boolean nothingChecked = true;
                for (CheckBox checkBox : checkBoxes) {
                    if (checkBox.isSelected()) {
                        nothingChecked = false;
                    }
                }
                if (nothingChecked) {
                    for (CheckBox checkBox : checkBoxes) {
                        getNodeError(checkBox, "Mindestens eine CheckBox abhaken!", stage);
                        questionCanBeCreated = false;
                        break;
                    }
                } else {
                    for (int i = 0; i < 5; i++) {
                        if (checkBoxes[i].isSelected() && textFields[i].getText().equals("")) {
                            getNodeError(textFields[i], "Bitte gib eine Antwort ein!\r\nOder entfern den Haken..", stage);
                            textFields[i].requestFocus();
                            questionCanBeCreated = false;
                            break;
                        }
                    }
                }
            }
            if (questionCanBeCreated) {
                boolean[] answers = {false,false,false,false,false};
                String[] questionMessages = {"","","","",""};
                for (int i = 0; i < 5; i++) {
                    if (!textFields[i].isDisabled()) {
                        if (i != 4 && !textFields[i + 1].isDisabled()) {
                            questionMessages[i] = textFields[i].getText();
                            answers[i] = checkBoxes[i].isSelected();
                        }
                    }
                }
                questionController
                        .addQuestion(
                                QuestionTyp.MultipleChoiceQuestion,
                                questionMessages,
                                answers,
                                extraParameterQuestionTextField.getText()
                );
                stage.close();
            }
        });
        cancelButton.setOnAction(event -> stage.close());

        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);
        observable.addListener((observable1, oldValue, newValue) ->{
            clearNodeError(extraParameterQuestionTextField);
            for (CheckBox checkBox : checkBoxes) {
                clearNodeError(checkBox);
            }
            for (TextField textField : textFields) {
                clearNodeError(textField);
            }
        });
        vBox.getChildren().add(buttonBox);
    }

    private void getNodeError(TextField textField, String text, Stage stage) {
        if (!textField.isDisabled()) {
            textField.setStyle("-fx-border-color: red;");
            Tooltip tooltip = new Tooltip();
            tooltip.setText(text);
            textField.setTooltip(tooltip);
            tooltip.show(stage);
        }
    }
    private void getNodeError(CheckBox checkBox, String text, Stage stage) {
        if (!checkBox.isDisabled()) {
            checkBox.setStyle("-fx-border-color: red;");
            Tooltip tooltip = new Tooltip();
            tooltip.setText(text);
            checkBox.setTooltip(tooltip);
            tooltip.show(stage);
        }
    }

    private void clearNodeError(TextField textField) {
        textField.setStyle("");
        if (textField.getTooltip() != null && textField.getTooltip().isShowing()) {
            textField.getTooltip().hide();
        }
        textField.setTooltip(null);
    }
    private void clearNodeError(CheckBox checkBox) {
        checkBox.setStyle("");
        if (checkBox.getTooltip() != null && checkBox.getTooltip().isShowing()) {
            checkBox.getTooltip().hide();
        }
        checkBox.setTooltip(null);
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
