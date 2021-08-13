package ui.components;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.question.Question;

public class QuestionView {

    private final static QuestionView questionView = new QuestionView();
    private final Pane root = new Pane();
    private final VBox content = new VBox();
    private final ScrollBar scrollBar = new ScrollBar();

    private QuestionView() {}

    // TODO ADD
    public void addQuestion(Question question) {
        resizeScrollbar();
    }

    // TODO ADD
    public void removeQuestion(Question question) {
        resizeScrollbar();
    }

    // TODO ADD
    public void changeExtraParameter(Question question, Object extraParameter) {
        resizeScrollbar();
    }

    // TODO ADD
    public void changeAnswer(Question question, Object answer) {
        resizeScrollbar();
    }

    // TODO ADD
    public void changeQuestionMessage(Question question, Object questionMessage) {
        resizeScrollbar();
    }

    // TODO ADD
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

    private void resizeScrollbar() {
        scrollBar.setMax(content.getHeight());
        scrollBar.valueProperty().addListener((observable, oldValue, newValue) -> content.setLayoutX(-newValue.doubleValue()));
    }
}
