package ui.components;

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

    private QuestionView() {}

    /**
     * <p>Adds a new Question to the View.</p>
     * @param question current {@link Question}
     */
    public void addQuestion(Question question) {
        VBox questionBox = new VBox();
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
        resizeScrollbar();
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
        resizeScrollbar();
    }

    /**
     * <p>Changes the extraParameter from the given Question.</p>
     * @param question current {@link Question}
     * @param extraParameter new extraParameter
     */
    public void changeExtraParameter(Question question, Object extraParameter) {
        switch (question.getQuestionTyp()) {
            case WordsQuestion:
                changeWordsQuestionExtraParameter(question, extraParameter);
                break;
            case MultipleChoiceQuestion:
                changeMultipleChoiceQuestionExtraParameter(question, extraParameter);
                break;
        }
        resizeScrollbar();
    }

    /**
     * <p>Changes the answer from the given Question.</p>
     * @param question current {@link Question}
     * @param answer new Answer
     */
    public void changeAnswer(Question question, Object answer) {
        switch (question.getQuestionTyp()) {
            case WordsQuestion:
                changeWordsQuestionAnswer(question, answer);
                break;
            case DirectQuestion:
                changeDirectQuestionAnswer(question, answer);
                break;
            case MultipleChoiceQuestion:
                changeMultipleChoiceQuestionAnswer(question, answer);
                break;
        }
        resizeScrollbar();
    }

    /**
     * <p>Changes the questionMessage from the given Question.</p>
     * @param question current {@link Question}
     * @param questionMessage new questionMessage
     */
    public void changeQuestionMessage(Question question, Object questionMessage) {
        switch (question.getQuestionTyp()) {
            case WordsQuestion:
                changeWordsQuestionQuestionMessage(question, questionMessage);
                break;
            case DirectQuestion:
                changeDirectQuestionQuestionMessage(question, questionMessage);
                break;
            case MultipleChoiceQuestion:
                changeMultipleChoiceQuestionQuestionMessage(question, questionMessage);
                break;
        }
        resizeScrollbar();
    }

    /**
     * <p>This will clear the QuestionView.</p>
     */
    public void clearQuestions() {
        content.getChildren().removeAll(content.getChildren());
        resizeScrollbar();
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

    public void init() {
        controller = QuestionController.getInstance();
        scrollBar.setMin(0);
        root.widthProperty().addListener((observable, oldValue, newValue) -> {
            scrollBar.setPrefWidth(newValue.doubleValue() * 0.025);
            scrollBar.setLayoutX(newValue.doubleValue() * 0.975);
        });
        root.heightProperty().addListener((observable, oldValue, newValue) -> scrollBar.setPrefHeight(newValue.doubleValue()));
        scrollBar.setOrientation(Orientation.VERTICAL);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setStyle("-fx-padding: 0,0,0,20;");
        root.getChildren().addAll(content, scrollBar);
    }

    private void addDirectQuestion(Question question, VBox questionBox) {

    }

    private void addWordsQuestion(Question question, VBox questionBox) {

    }

    private void addMultipleChoiceQuestion(Question question, VBox questionBox) {

    }

    private void changeWordsQuestionExtraParameter(Question question, Object extraParameter) {

    }

    private void changeMultipleChoiceQuestionExtraParameter(Question question, Object extraParameter) {

    }

    private void changeMultipleChoiceQuestionAnswer(Question question, Object answer) {

    }

    private void changeDirectQuestionAnswer(Question question, Object answer) {

    }

    private void changeWordsQuestionAnswer(Question question, Object answer) {

    }

    private void changeMultipleChoiceQuestionQuestionMessage(Question question, Object questionMessage) {

    }

    private void changeDirectQuestionQuestionMessage(Question question, Object questionMessage) {

    }

    private void changeWordsQuestionQuestionMessage(Question question, Object questionMessage) {

    }

    private Node getQuestionHeader(Question question) {
        HBox hBox = new HBox();

        String questionMessage;
        if (question.getQuestionTyp() == QuestionTyp.MultipleChoiceQuestion) {
            questionMessage = question.getExtraParameter().toString();
        } else {
            questionMessage = question.getQuestionMessage().toString();
        }
        Label label = new Label("Frage: \"" + questionMessage + "\"");
        label.setContextMenu(getContextMenu(question));

        hBox.getChildren().add(label);
        return hBox;
    }

    private Node getQuestionFooter(Question question) {
        return null;
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
        Stage stage = getPopUpStage(vBox);
        stage.setTitle("Fragen Antwort ändern");

//        controller.changeAnswerQuestion(question, "");
//        controller.changeExtraParameterQuestion(question, "");

        stage.show();
    }

    private void getDeletePopUp(Question question) {
        VBox vBox = new VBox();
        Stage stage = getPopUpStage(vBox);
        stage.setTitle("Frage löschen");

//        controller.removeQuestion(question);

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

    private void clearNodeError(TextField textField) {
        textField.setStyle("");
        if (textField.getTooltip() != null && textField.getTooltip().isShowing()) {
            textField.getTooltip().hide();
        }
        textField.setTooltip(null);
    }

    private void resizeScrollbar() {
        scrollBar.setMax(content.getHeight());
        scrollBar.valueProperty().addListener((observable, oldValue, newValue) -> content.setLayoutX(-newValue.doubleValue()));
    }
}
