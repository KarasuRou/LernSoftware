package ui.components;

import javafx.scene.Node;
import javafx.scene.layout.FlowPane;

public class QuestionView {

    private final static QuestionView questionView = new QuestionView();
    private final FlowPane root = new FlowPane();

    private QuestionView() {}

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
}
