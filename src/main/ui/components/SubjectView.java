package ui.components;

import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class SubjectView {

    private final static SubjectView subjectView = new SubjectView();
    private final TabPane root = new TabPane();
    private final Property<Number> width = new SimpleDoubleProperty();
    private final Property<Number> height = new SimpleDoubleProperty();
    private final QuestionView questionView = QuestionView.getInstance(); // For question content

    static {
        subjectView.root.getTabs().addAll(new Tab("Deutsch"),
                new Tab("Englisch"),
                new Tab("Mathematik")
        );
        String content = "";
        for (int i = 0; i < 10; i++) {
            content += "Das ist der Text in dem Fach: " + subjectView.root.getTabs().get(0).getText() + "\n";
        }
        subjectView.root.getTabs().get(0).setContent(subjectView.questionView.getQuestionView());
        subjectView.root.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            subjectView.root.getTabs().get(oldValue.intValue()).setContent(null);
            String content_ = "";
            for (int i = 0; i < 10; i++) {
                content_ +="Das ist der Text in dem Fach: " + subjectView.root.getTabs().get(newValue.intValue()).getText() + "\n" +
                        "Der vorherige Text war aus: " + subjectView.root.getTabs().get(oldValue.intValue()).getText() + "\n\n";
            }
            subjectView.root.getTabs().get(newValue.intValue()).setContent(new Label(content_));
            System.out.println("From: " +subjectView.root.getTabs().get(oldValue.intValue()).getText() + " (" + oldValue + ") " +
                    "to: " +subjectView.root.getTabs().get(newValue.intValue()).getText() + " (" + newValue + ")");
        });
        subjectView.root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
    }

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
     * <p>This method will bind the scene width/height into this class.</p>
     * @param width current scene widthProperty
     * @param height current scene heightProperty
     */
    public void transferSizeProperty(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height) {
        if (this.width.isBound()) {
            this.width.unbind();
        }
        this.width.bind(width);
        if (this.height.isBound()) {
            this.height.unbind();
        }
        this.height.bind(height);

        this.root.setPrefWidth(this.width.getValue().doubleValue() - 160); // 160 -> FolderView width
        this.width.addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() >= 800) {
                this.root.setPrefWidth((Double) newValue - 160);
            } else {
                this.root.setPrefWidth((Double) newValue * 0.8);
            }
        });

        this.root.setTabMinHeight(40);
        this.root.setMinHeight(120);
        this.root.setPrefHeight(this.height.getValue().doubleValue() * 0.9);
        this.height.addListener((observable, oldValue, newValue) -> {
            this.root.setPrefHeight((Double) newValue * 0.9);
        });

        this.root.setStyle("-fx-border-color: gray;" +
                "-fx-border-style: solid;" +
                "-fx-border-width: 0.2px 0 0 0.2px;");
    }

    /**
     * <p>This will return view of the SubjectView class.</p>
     * @return the {@link SubjectView} {@link Node}.
     */
    public Node getSubjectView(){
        return this.root;
    }
}
