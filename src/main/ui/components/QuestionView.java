package ui.components;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.QuestionController;
import model.question.Question;
import ui.MainUI;

public class QuestionView {

    private final static QuestionView questionView = new QuestionView();
    private final QuestionController controller = QuestionController.getInstance();
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

    private ContextMenu getContextMenu(Question question) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem changeQuestionMessage = new MenuItem("Frage ändern");
        changeQuestionMessage.setOnAction(event -> getQuestionMessagePopUp(question));
        MenuItem changeAnswer = new MenuItem("Antworten ändern");
        changeAnswer.setOnAction(event -> getAnswerPopUp(question));
        MenuItem deleteQuestion = new MenuItem("Frage löschen");
        deleteQuestion.setOnAction(event -> getDeletePopUp(question));

        contextMenu.getItems().addAll(changeQuestionMessage, changeAnswer);
        return contextMenu;
    }

    private void getQuestionMessagePopUp(Question question) {
        VBox vBox = new VBox();
        Stage stage = getPopUpStage(vBox);
        stage.setTitle("Fragen Frage ändern");

        stage.show();
    }

    private void getAnswerPopUp(Question question) {
        VBox vBox = new VBox();
        Stage stage = getPopUpStage(vBox);
        stage.setTitle("Fragen Antwort ändern");

        stage.show();
    }

    private void getDeletePopUp(Question question) {
        VBox vBox = new VBox();
        Stage stage = getPopUpStage(vBox);
        stage.setTitle("Frage löschen");

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

    private void resizeScrollbar() {
        scrollBar.setMax(content.getHeight());
        scrollBar.valueProperty().addListener((observable, oldValue, newValue) -> content.setLayoutX(-newValue.doubleValue()));
    }
}
