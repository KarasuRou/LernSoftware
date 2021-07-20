package ui.components;

import javafx.scene.Node;
import javafx.scene.layout.FlowPane;

public class QuestionView {

    private FlowPane root = new FlowPane();

    public QuestionView() {

    }

    public Node getQuestionView(){
        return this.root;
    }
}
