package ui.components;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class QuestionView {

    private final static QuestionView questionView = new QuestionView();
    private final VBox root = new VBox();
    private final Property<Number> boundFolder = new SimpleIntegerProperty();
    private int maxHeight = 0;

    static {
        FolderView.getInstance().bindExternProperty(questionView.boundFolder);

        Label label = new Label("This is here!");

        questionView.root.getChildren().addAll(
                label,
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"),
                new Label("ASDKAKJSDKLASJDLASLDJALSDJLJSDA"));
    }

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
