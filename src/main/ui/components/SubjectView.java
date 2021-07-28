package ui.components;

import javafx.scene.Node;
import javafx.scene.control.TabPane;

public class SubjectView {

    private final static SubjectView subjectView = new SubjectView();
    private final TabPane root = new TabPane();

    private SubjectView(){}


    /**
     * <p>This Method is the "Constructor" for the SubjectView class.</p>
     * <p>This is the only way to access the SubjectView.</p>
     * @return a {@link SubjectView} instance
     */
    public static SubjectView getInstance() {
        return subjectView;
    }


    /**
     * <p>This will return view of the SubjectView class.</p>
     * @return the {@link SubjectView} {@link Node}.
     */
    public Node getSubjectView(){
        return this.root;
    }
}
