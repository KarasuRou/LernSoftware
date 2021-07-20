package ui.components;

import javafx.scene.Node;
import javafx.scene.control.TabPane;

public class SubjectView {

    private TabPane root = new TabPane();

    public SubjectView(){

    }

    public Node getSubjectView(){
        return this.root;
    }
}
