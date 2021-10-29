package ui.components;

import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.QuestionController;
import model.question.Question;
import model.question.QuestionTyp;
import ui.MainUI;

public class QuestionView {

    private final static QuestionView questionView = new QuestionView();
    private QuestionController controller;
    private final Pane root = new Pane();
    private final VBox content = new VBox();
    private final ScrollBar scrollBar = new ScrollBar();
    private final Property<Number> visibleContentHeight = new SimpleDoubleProperty();
    private final Property<Number> completeContentHeight = new SimpleDoubleProperty();

    private QuestionView() {}

    /**
     * <p>Adds a new Question to the View.</p>
     * @param question current {@link Question}
     */
    public void addQuestion(Question question) {
        VBox questionBox = new VBox();
        questionBox.setSpacing(2);
        questionBox.setId(String.valueOf(question.getID()));
        questionBox.getChildren().add(getQuestionHeader(question));
        switch (question.getQuestionTyp()) {
            case DirectQuestion:
                addDirectQuestion(question, questionBox);
                break;
            case WordsQuestion:
                addWordsQuestion(question, questionBox);
                break;
            case MultipleChoiceQuestion:
                addMultipleChoiceQuestion(question, questionBox);
                break;
        }
        content.getChildren().add(questionBox);
    }

    /**
     * <p>Removes a Question from the View.</p>
     * @param question current {@link Question}
     */
    public void removeQuestion(Question question) {
        for (Node node : content.getChildren()) {
            if (node.getId().equals(String.valueOf(question.getID()))) {
                content.getChildren().remove(node);
                return;
            }
        }
    }

    /**
     * <p>Changes the extraParameter from the given Question.</p>
     * @param question current {@link Question}
     * @param extraParameter new extraParameter
     */
    public void changeExtraParameter(Question question, Object extraParameter) {
        switch (question.getQuestionTyp()) {
            case WordsQuestion:
            case DirectQuestion:
                break;
            case MultipleChoiceQuestion:
                changeMultipleChoiceQuestionExtraParameter(question, extraParameter);
                break;
        }
    }

    private void changeMultipleChoiceQuestionExtraParameter(Question question, Object extraParameter) {
        for (Node questionBox : content.getChildren()) { // questionHeader -> Label ("Frage: "\"extraParameter\")
            if (question.getID() == Integer.parseInt(questionBox.getId())) {
                ((Label) ((HBox) ((VBox) questionBox).getChildren().get(0)).getChildren().get(0)).setText("Frage: \"" + extraParameter + "\"");
            }
        }
    }

    /**
     * <p>Changes the answer from the given Question.</p>
     * @param question current {@link Question}
     * @param answer new Answer
     */
    public void changeAnswer(Question question, Object answer) {
        switch (question.getQuestionTyp()) { // Nothing to-do here
            case WordsQuestion:
            case DirectQuestion:
            case MultipleChoiceQuestion:
                break;
        }
    }

    /**
     * <p>Changes the questionMessage from the given Question.</p>
     * @param question current {@link Question}
     * @param questionMessage new questionMessage
     */
    public void changeQuestionMessage(Question question, Object questionMessage) {
        switch (question.getQuestionTyp()) {
            case WordsQuestion:
            case DirectQuestion:
                changeWordsQuestionMessage_AND_changeDirektQuestionMessage(question, questionMessage); // TODO Add/Remove CheckBox/Label ?
                break;
            case MultipleChoiceQuestion:
                changeMultipleChoiceQuestionMessage(question, questionMessage);
                break;
        }
    }

    private void changeWordsQuestionMessage_AND_changeDirektQuestionMessage(Question question, Object questionMessage) {
        for (Node questionBox : content.getChildren()) { // questionHeader -> Label ("Frage: "\"questionMessage\")
            if (question.getID() == Integer.parseInt(questionBox.getId())) {
                ((Label) ((HBox) ((VBox) questionBox).getChildren().get(0)).getChildren().get(0)).setText("Frage: \"" + questionMessage + "\"");
            }
        }
    }

    private void changeMultipleChoiceQuestionMessage(Question question, Object questionMessage) {
        for (Node questionBox : content.getChildren()) { // questionBox -> VBox (1) -> HBox (1-5) -> Label (1)
            if (question.getID() == Integer.parseInt(questionBox.getId())) {
                String[] questionMessages = ((String[]) questionMessage);
                int answerSize = ((VBox)((VBox) questionBox).getChildren().get(1)).getChildren().size();
                for (int i = 1; i < answerSize; i++) {
                    Label label = (Label)((HBox)((VBox)((VBox) questionBox).getChildren().get(1)).getChildren().get(i)).getChildren().get(1);
                    if (!label.getText().equals(questionMessages[i - 1])) {
                        label.setText(questionMessages[i - 1]);
                    }
                }
            }
        }
    }

    /**
     * <p>This will clear the QuestionView.</p>
     */
    public void clearQuestions() {
        content.getChildren().removeAll(content.getChildren());
        scrollBar.setValue(0);
    }

    /**
     * <p>This Method is the "Constructor" for the QuestionView class.</p>
     * <p>This is the only way to access the QuestionView.</p>
     * @return a {@link QuestionView} instance
     */
    public static QuestionView getInstance() {
        return questionView;
    }

    /**
     * <p>This will return view of the QuestionView class.</p>
     * @return the {@link QuestionView} {@link Node}.
     */
    public Node getQuestionView(){
        return this.root;
    }

    /**
     * <p>This method will bind the scene width/height into this class.</p>
     * @param width current scene widthProperty
     * @param height current scene heightProperty
     */
    public void transferSizeProperty(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height) {
        visibleContentHeight.bind(height);
    }

    /**
     * <p>This method will Initiate the class.</p>
     * <p>It is required to call this method, to properly use the Application.</p>
     */
    public void init() {
        controller = QuestionController.getInstance();
        scrollBar.setMin(0);
        scrollBar.setMax(100);
        root.widthProperty().addListener((observable, oldValue, newValue) -> {
            scrollBar.setPrefWidth(newValue.doubleValue() * 0.025);
            scrollBar.setLayoutX(newValue.doubleValue() * 0.975);
        });
        root.heightProperty().addListener((observable, oldValue, newValue) -> scrollBar.setPrefHeight(newValue.doubleValue()));
        scrollBar.setOrientation(Orientation.VERTICAL);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setStyle("-fx-padding: 20 0 0 20;" +
                "-fx-spacing: 30;");
        root.getChildren().addAll(content, scrollBar);


        completeContentHeight.bind(content.heightProperty());
        completeContentHeight.addListener((observable, oldValue, newValue) -> resizeScrollbar());
        visibleContentHeight.addListener((observable, oldValue, newValue) -> resizeScrollbar());

        scrollBar.valueProperty().addListener((observable, oldValue, newValue) -> resizeScrollbar(newValue));

        SubjectView.getInstance().setScrollEvent(event -> {
            if (event.getDeltaY() < 0) {
                if (scrollBar.getValue() + 4 > 100) {
                    scrollBar.setValue(100);
                } else {
                    scrollBar.setValue(scrollBar.getValue() + 4);
                }
            } else {
                if (scrollBar.getValue() - 4 < 0) {
                    scrollBar.setValue(0);
                } else {
                    scrollBar.setValue(scrollBar.getValue() - 4);
                }
            }
        });
    }

    private void addDirectQuestion(Question question, VBox questionBox) {
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        TextField textField = new TextField();
        Label statusLabel = new Label();
        hBox.getChildren().addAll(new Label("Antwort: "), textField, statusLabel);
        Property<Boolean> answered = new SimpleBooleanProperty(false);

        EventHandler<ActionEvent> checkEvent = event -> {
                if (!answered.getValue()) {
                    String answer = textField.getText();
                    if (controller.checkIfAnswerIsCorrect(question, answer)) {
                        textField.setStyle("-fx-text-fill: green;");
                        textField.setEditable(false);
                        statusLabel.setStyle("-fx-text-fill: green;");
                        statusLabel.setText("Richtig!");
                    } else {
                        textField.setStyle("-fx-text-fill: red;");
                        textField.setEditable(false);
                        textField.setText(question.getAnswer().toString());
                        statusLabel.setStyle("-fx-text-fill: red;");
                        statusLabel.setText("Falsch!");
                    }
                }
                answerQuestion(answered);
        };

        textField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                checkEvent.handle(new ActionEvent());
            }
        });

        HBox buttonBox = (HBox) getQuestionFooter(
                checkEvent,
                showEvent -> {
                    if (!answered.getValue()) {
                        textField.setEditable(false);
                        textField.setText(question.getAnswer().toString());
                        answerQuestion(answered);
                        statusLabel.setText("Musterlösung angezeigt!");
                    }
                }
        );

        questionBox.getChildren().addAll(hBox, buttonBox);
    }

    private void addWordsQuestion(Question question, VBox questionBox) {
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        Label statusLabel = new Label();
        TextField textField = new TextField();
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.contains(" ")) {
                textField.setText(newValue.replace(" ", ""));
            }
        });
        Property<Boolean> answered = new SimpleBooleanProperty(false);
        hBox.getChildren().addAll(new Label("Antwort: "), textField, statusLabel);

        EventHandler<ActionEvent> checkEvent = event -> {
            if (!answered.getValue()) {
                String answer = textField.getText();
                if (controller.checkIfAnswerIsCorrect(question, answer)) {
                    textField.setStyle("-fx-text-fill: green;");
                    textField.setEditable(false);
                    statusLabel.setStyle("-fx-text-fill: green;");
                    statusLabel.setText("Richtig!");
                } else {
                    textField.setStyle("-fx-text-fill: red;");
                    textField.setEditable(false);
                    textField.setText(question.getAnswer().toString());
                    statusLabel.setStyle("-fx-text-fill: red;");
                    statusLabel.setText("Falsch!");
                }
            }
            answerQuestion(answered);
        };

        HBox buttonBox = (HBox) getQuestionFooter(
                checkEvent,
                event -> {
                    if (!answered.getValue()) {
                        textField.setEditable(false);
                        textField.setText(question.getAnswer().toString());
                        answerQuestion(answered);
                        statusLabel.setText("Musterlösung angezeigt!");
                    }
                }
        );

        questionBox.getChildren().addAll(hBox, buttonBox);
    }

    private void addMultipleChoiceQuestion(Question question, VBox questionBox) {
        questionBox.setSpacing(5);
        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(0, 0, 0, 10));
        HBox hBox = new HBox();
        Label statusLabel = new Label();
        hBox.getChildren().addAll(new Label("Antwortmöglichkeiten: "), statusLabel);
        vBox.getChildren().add(hBox);
        CheckBox[] checkBoxes = new CheckBox[5];
        Label[] labels = new Label[5];

        Property<Boolean> answered = new SimpleBooleanProperty(false);

        for (int i = 0; i < 5; i++) {
            String currentText = ((String[]) question.getQuestionMessage())[i];
            if (!currentText.equals("")) {
                HBox possibleAnswers = new HBox();
                possibleAnswers.setSpacing(5);
                checkBoxes[i] = new CheckBox();
                labels[i] = new Label(currentText);
                possibleAnswers.getChildren().addAll(checkBoxes[i], labels[i]);
                vBox.getChildren().add(possibleAnswers);
            }
        }

        EventHandler<ActionEvent> checkAnswerEvent = event -> {
            if (!answered.getValue()) {
                answerQuestion(answered);
                boolean[] currentAnswers = new boolean[5];

                for (int i = 0; i < 5; i++) {
                    if (checkBoxes[i] != null) {
                        currentAnswers[i] = checkBoxes[i].isSelected();
                    } else {
                        currentAnswers[i] = false;
                    }
                }
                if (controller.checkIfAnswerIsCorrect(question, currentAnswers)) {
                    for (CheckBox checkBox : checkBoxes) {
                        if (checkBox != null) {
                            checkBox.setDisable(true);
                        }
                    }
                    statusLabel.setStyle("-fx-text-fill: green;");
                    statusLabel.setText("Richtig!");
                } else {
                    int i = 0;
                    for (CheckBox checkBox : checkBoxes) {
                        if (checkBox != null) {
                            boolean rightAnswer = ((boolean[]) question.getAnswer())[i++];
                            if (checkBox.isSelected() != rightAnswer) {
                                checkBox.setSelected(rightAnswer);
                                checkBox.setDisable(true);
                                checkBox.setStyle("-fx-border-style: solid;" +
                                        "-fx-border-color: red;");
                            } else {
                                checkBox.setDisable(true);
                            }
                        }
                        statusLabel.setStyle("-fx-text-fill: red;");
                        statusLabel.setText("Falsch!");
                    }
                }
            }
        };

        HBox buttonBox = (HBox) getQuestionFooter(
                checkAnswerEvent,
                showRightAnswersEvent -> {
                    if (!answered.getValue()) {
                        for (int i = 0; i < 5; i++) {
                            if (checkBoxes[i] != null) {
                                checkBoxes[i].setSelected(((boolean[]) question.getAnswer())[i]);
                                checkBoxes[i].setDisable(true);
                                answerQuestion(answered);
                                statusLabel.setText("Musterlösung angezeigt!");
                            }
                        }
                    }
                }
        );

        questionBox.getChildren().addAll(vBox, buttonBox);
    }

    private void answerQuestion(Property<Boolean> answered) {
        answered.setValue(true);
    }

    private Node getQuestionHeader(Question question) {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(0, 0, 2.5, 0));
        hBox.setSpacing(20);

        String questionMessage;
        if (question.getQuestionTyp() == QuestionTyp.MultipleChoiceQuestion) {
            questionMessage = question.getExtraParameter().toString();
        } else {
            questionMessage = question.getQuestionMessage().toString();
        }
        Label label = new Label("Frage: \"" + questionMessage + "\"");
        label.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.SEMI_BOLD, Font.getDefault().getSize() + 1));
        label.setContextMenu(getContextMenu(question));

        Label label2 = new Label();
        switch (question.getQuestionTyp()) {
            case WordsQuestion:
                label2.setText("Einwort Frage");
                break;
            case DirectQuestion:
                label2.setText("Normale Frage");
                break;
            case MultipleChoiceQuestion:
                label2.setText("MultipleChoice Frage");
                break;
        }
        label2.setTextFill(Paint.valueOf("red"));
        label2.setFont(Font.font(Font.getDefault().getFamily(), Font.getDefault().getSize() * 0.75));
        hBox.getChildren().addAll(label, label2);
        label2.setOpacity(0);

        label.setOnMouseEntered(event -> label2.setOpacity(0.5));
        label.setOnMouseExited(event -> label2.setOpacity(0));
        return hBox;
    }

    private Node getQuestionFooter(EventHandler<ActionEvent> answerQuestionButtonEvent, EventHandler<ActionEvent> showRightAnswerButtonEvent) {
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        Button answerQuestionButton = new Button("Antwort überprüfen");
        answerQuestionButton.setOnAction(answerQuestionButtonEvent);
        Button showRightAnswerButton = new Button("Musterlösung anzeigen");
        showRightAnswerButton.setOnAction(showRightAnswerButtonEvent);
        hBox.getChildren().addAll(answerQuestionButton, showRightAnswerButton);
        return hBox;
    }

    private ContextMenu getContextMenu(Question question) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem changeQuestionMessage = new MenuItem("Frage ändern");
        changeQuestionMessage.setOnAction(event -> getQuestionMessagePopUp(question));
        MenuItem changeAnswer = new MenuItem("Antworten ändern");
        changeAnswer.setOnAction(event -> getAnswerPopUp(question));
        MenuItem deleteQuestion = new MenuItem("Frage löschen");
        deleteQuestion.setOnAction(event -> getDeletePopUp(question));

        contextMenu.getItems().addAll(changeQuestionMessage, changeAnswer, deleteQuestion);
        return contextMenu;
    }

    private void getQuestionMessagePopUp(Question question) {
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(20));
        Stage stage = getPopUpStage(vBox);
        stage.setTitle("Fragestellung ändern");

        Label label = new Label("Wie soll die Fragestellung jetzt lauten?");
        TextField textField = new TextField();

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        Button changeQuestionButton = new Button("Fragestellung ändern");
        Button cancelQuestionButton = new Button("Abbrechen");
        cancelQuestionButton.setOnAction(event -> stage.close());
        buttonBox.getChildren().addAll(changeQuestionButton, cancelQuestionButton);

        if (question.getQuestionTyp() == QuestionTyp.DirectQuestion || question.getQuestionTyp() == QuestionTyp.WordsQuestion) {
            textField.setText(question.getQuestionMessage().toString());
            changeQuestionButton.setOnAction(event -> {
                clearNodeError(textField);
                if (textField.getText().equals("") || textField.getText().equals(question.getQuestionMessage())) {
                    getNodeError(textField, "Bitte einen (anderen) Wert eingeben!", stage);
                }
                else {
                    controller
                            .changeQuestionMessageQuestion(
                                    question,
                                    textField.getText()
                            );
                    stage.close();
                }
            });
        }
        else if (question.getQuestionTyp() == QuestionTyp.MultipleChoiceQuestion) {
            textField.setText(question.getExtraParameter().toString());
            changeQuestionButton.setOnAction(event -> {
                clearNodeError(textField);
                if (textField.getText().equals("") || textField.getText().equals(question.getExtraParameter())) {
                    getNodeError(textField, "Bitte einen (anderen) Wert eingeben!", stage);
                } else {
                    controller
                            .changeExtraParameterQuestion(question, textField.getText());
                    stage.close();
                }
            });
        }
        textField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                changeQuestionButton.fire();
            }
        });

        vBox.getChildren().addAll(label, textField, buttonBox);
        stage.show();
    }

    private void getAnswerPopUp(Question question) {
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(20));

        Stage stage = getPopUpStage(vBox);
        stage.setTitle("Antwort ändern");

        Label label = new Label("Wie soll die Antwort jetzt lauten?");
        vBox.getChildren().addAll(getQuestionHeader(question), label);
        if (question.getQuestionTyp() == QuestionTyp.WordsQuestion || question.getQuestionTyp() == QuestionTyp.DirectQuestion) {
            getAnswerPopUpWordsAndDirect(question, vBox, stage);
        } else if (question.getQuestionTyp() == QuestionTyp.MultipleChoiceQuestion) {
            getAnswerPopUpMultiple(question, vBox, stage);
        }

        stage.show();
    }
    private void getAnswerPopUpMultiple(Question question, VBox vBox, Stage stage) {

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(5);
        Button changeAnswerButton = new Button("Antwort(en) ändern");
        Button cancelButton = new Button("Abbrechen");
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(changeAnswerButton, cancelButton);

        CheckBox[] checkBoxes = new CheckBox[5];
        TextField[] textFields = new TextField[5];
        boolean[] booleans = (boolean[]) question.getAnswer();
        String[] strings = (String[]) question.getQuestionMessage();

        for (int i = 0; i < 5; i++) {
            int finalI = i;

            HBox hBox = new HBox();
            hBox.setSpacing(5);
            checkBoxes[i] = new CheckBox();
            checkBoxes[i].setSelected(booleans[i]);
            checkBoxes[i].selectedProperty().addListener((observable1, oldValue, newValue) ->{
                if (newValue) {
                    for (CheckBox checkBox : checkBoxes) {
                        clearNodeError(checkBox);
                    }
                }
            });

            textFields[i] = new TextField();
            textFields[i].setText(strings[i]);
            textFields[i].setOnKeyPressed(event -> {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    changeAnswerButton.fire();
                }
            });
            textFields[i].textProperty().addListener((observable1, oldValue, newValue) -> {
                if (finalI == 0 && !newValue.equals("")) {
                    textFields[finalI + 1].setDisable(false);
                    checkBoxes[finalI + 1].setDisable(false);
                } else if (finalI != 4 && !newValue.equals("")) {
                    textFields[finalI + 1].setDisable(false);
                    checkBoxes[finalI + 1].setDisable(false);
                } else if (finalI != 4) {
                    textFields[finalI + 1].setDisable(true);
                    checkBoxes[finalI + 1].setDisable(true);
                }
            });
            if (i != 0 && !checkBoxes[i - 1].isSelected() && textFields[i - 1].getText().equals("") &&
                    !checkBoxes[i].isSelected() && textFields[i].getText().equals("")) {
                checkBoxes[i].setDisable(true);
                textFields[i].setDisable(true);
            }

            hBox.getChildren().addAll(new Label("Möglichkeit " + (i + 1) + ":"), checkBoxes[i], textFields[i]);
            vBox.getChildren().add(hBox);
        }

        vBox.getChildren().add(buttonBox);

        changeAnswerButton.setOnAction(event -> {
            for (int i = 0; i < 5; i++) {
                clearNodeError(textFields[i]);
                clearNodeError(checkBoxes[i]);
            }

            boolean answerCanBeChanged = true;
            boolean nothingChecked = true;
            for (CheckBox checkBox : checkBoxes) {
                if (checkBox.isSelected()) {
                    nothingChecked = false;
                }
            }
            if (nothingChecked) {
                for (CheckBox checkBox : checkBoxes) {
                    getNodeError(checkBox, "Mindestens eine CheckBox abhaken!", stage);
                    answerCanBeChanged = false;
                    break;
                }
            } else {
                for (int i = 0; i < 5; i++) {
                    if (checkBoxes[i].isSelected() && textFields[i].getText().equals("")) {
                        getNodeError(textFields[i], "Bitte gib eine Antwort ein!\r\nOder entfern den Haken..", stage);
                        textFields[i].requestFocus();
                        answerCanBeChanged = false;
                        break;
                    }
                }
            }

            if (answerCanBeChanged) {
                boolean[] currentAnswers = {false,false,false,false,false};
                String[] questionMessages = {"","","","",""};
                for (int i = 0; i < 5; i++) {
                    if (!textFields[i].isDisabled()) {
                        if (i == 4 || !textFields[i + 1].isDisabled()) {
                            questionMessages[i] = textFields[i].getText();
                            currentAnswers[i] = checkBoxes[i].isSelected();
                        }
                    }
                }
                if (!currentAnswers.equals(question.getAnswer())) {
                    controller.changeAnswerQuestion(question, currentAnswers);
                }
                if (!questionMessages.equals(question.getQuestionMessage())) {
                    controller.changeQuestionMessageQuestion(question, questionMessages);
                }

                stage.close();
            }
        });
        cancelButton.setOnAction(event -> stage.close());
    }

    private void getAnswerPopUpWordsAndDirect(Question question, VBox vBox, Stage stage) {
        TextField textField = new TextField(question.getAnswer().toString());

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(5);
        Button changeAnswerButton = new Button("Antwort ändern");
        Button cancelButton = new Button("Abbrechen");
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(changeAnswerButton, cancelButton);

        if (question.getQuestionTyp() == QuestionTyp.WordsQuestion) {
            textField.textProperty().addListener((observable, oldValue, newValue) ->{
                if (newValue.contains(" ")) {
                    textField.setText(newValue.replace(" ", ""));
                }
            });
        }

        textField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                changeAnswerButton.fire();
            }
        });
        changeAnswerButton.setOnAction(event -> {
            clearNodeError(textField);
            if (textField.getText().equals(question.getAnswer().toString()) ||
                    textField.getText().equals("")) {
                getNodeError(textField, "Bitte einen (anderen) Wert eingeben!", stage);
            } else {
                controller.changeAnswerQuestion(question, textField.getText());
                stage.close();
            }
        });
        cancelButton.setOnAction(event -> stage.close());


        vBox.getChildren().addAll(textField, buttonBox);
    }

    private void getDeletePopUp(Question question) {
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(20));
        vBox.setAlignment(Pos.CENTER);

        Stage stage = getPopUpStage(vBox);
        stage.setTitle("Frage löschen");

        Label label = new Label("Frage: \"$PLACEHOLDER$\", \r\nwirklich löschen?");
        label.setTextAlignment(TextAlignment.CENTER);

        if (question.getQuestionTyp() == QuestionTyp.WordsQuestion || question.getQuestionTyp() == QuestionTyp.DirectQuestion) {
            label.setText(label.getText().replace("$PLACEHOLDER$",question.getQuestionMessage().toString()));
        } else if (question.getQuestionTyp() == QuestionTyp.MultipleChoiceQuestion) {
            label.setText(label.getText().replace("$PLACEHOLDER$",question.getExtraParameter().toString()));
        }

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(10);
        Button changeQuestionButton = new Button("Frage löschen");
        changeQuestionButton.setOnAction(event -> {
            controller.removeQuestion(question);
            stage.close();
        });
        Button cancelQuestionButton = new Button("Abbrechen");
        cancelQuestionButton.setOnAction(event -> stage.close());

        buttonBox.getChildren().addAll(changeQuestionButton, cancelQuestionButton);

        vBox.getChildren().addAll(label, buttonBox);
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

    private void resizeScrollbar() {
        if (visibleContentHeight.getValue().doubleValue() * 0.9 - 40 < completeContentHeight.getValue().doubleValue()) {
            scrollBar.setVisible(true);
            resizeScrollbar(scrollBar.getValue());
        } else {
            scrollBar.setVisible(false);
        }
    }
    private void resizeScrollbar(Number newValue) {
        double canBeMoved = completeContentHeight.getValue().doubleValue() - (visibleContentHeight.getValue().doubleValue() * 0.9 - 40);
        content.setLayoutY(-(canBeMoved * (newValue.doubleValue() / 100)));
    }
}
