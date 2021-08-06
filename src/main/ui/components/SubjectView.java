package ui.components;

import com.sun.istack.internal.Nullable;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import logic.miscellaneous.Output;
import model.Subject;

import java.io.File;

public class SubjectView {

    private final static SubjectView subjectView = new SubjectView();
    private final TabPane root = new TabPane();
    private final Property<Number> selectedSubject = new SimpleIntegerProperty();
    private final Property<Number> width = new SimpleDoubleProperty();
    private final Property<Number> height = new SimpleDoubleProperty();
    private final QuestionView questionView = QuestionView.getInstance(); // For question content

    static {
        subjectView.selectedSubject.bind(subjectView.root.getSelectionModel().selectedIndexProperty());

        Subject subject = new Subject();
        subject.setName("Deutsch");
        subject.setID(0);
        Subject subject1 = new Subject();
        subject1.setName("Englisch");
        subject1.setID(1);
        Subject subject2 = new Subject();
        subject2.setName("Mathematik");
        subject2.setID(2);
        subjectView.addSubjectTab(subject);
        subjectView.addSubjectTab(subject1);
        subjectView.addSubjectTab(subject2);


        subjectView.root.getTabs().get(0).setContent(subjectView.questionView.getQuestionView());
        subjectView.root.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            subjectView.root.getTabs().get(oldValue.intValue()).setContent(null);
            String content_ = "";
            for (int i = 0; i < 10; i++) {
                content_ +="Das ist der Text in dem Fach: " + subjectView.root.getTabs().get(newValue.intValue()).getText() + "\n" +
                        "Der vorherige Text war aus: " + subjectView.root.getTabs().get(oldValue.intValue()).getText() + "\n\n";
            }
            subjectView.root.getTabs().get(newValue.intValue()).setContent(new Label(content_));
            subjectView.changeBackground((String) subjectView.root.getTabs().get(newValue.intValue()).getUserData());
            System.out.println("From: " +subjectView.root.getTabs().get(oldValue.intValue()).getText() + " (" + oldValue + ") " +
                    "to: " +subjectView.root.getTabs().get(newValue.intValue()).getText() + " (" + newValue + ")");
        });
        subjectView.root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        subjectView.root.heightProperty().addListener((observable, oldValue, newValue) -> {
        });
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
        this.height.addListener((observable, oldValue, newValue) -> this.root.setPrefHeight((Double) newValue * 0.9));

        this.root.setStyle(
                "-fx-border-color: gray;" +
                "-fx-border-style: solid;" +
                "-fx-border-width: 0.2px 0 0 0.2px;"
        );
    }

    /**
     * <p>Adds a new Subject Tab, without content.</p>
     * @param subject required {@link Subject}
     */
    public void addSubjectTab(Subject subject){
        Output.write("Add Subject: " + subject.getName().getValue() + " (ID: " + subject.getID() + ")");
        Tab tab = new Tab(subject.getName().getValue());
        tab.setId(String.valueOf(subject.getID()));
        tab.setUserData(subject.getBackgroundPicturePath());
//        if (subject.getBackgroundPicturePath() != null && !subject.getBackgroundPicturePath().equals("")) {
//            changeBackground(subject.getBackgroundPicturePath()); // TODO setSubject()
//        }
        this.root.getTabs().add(tab);
    }

    /**
     * <p>Changes the Background for the current Subject.</p>
     * @param path relativ system path
     */
    public void changeBackground(@Nullable String path) {
        if (path == null) {
            this.root.setBackground(null);
        } else {
            Image image = new Image(new File(path).toURI().toString());
            BackgroundPosition backgroundPosition = new BackgroundPosition(Side.LEFT, 100.0, true, Side.TOP, 100.0, true);
            BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
            Background background = new Background(new BackgroundImage(image, null, null, backgroundPosition, backgroundSize));
            this.root.setBackground(background);
        }
    }

    /**
     * <p>Renames the Subject Tab.</p>
     * @param subject required {@link Subject}
     */
    public void renameSubjectTab(Subject subject){
        for (Tab tab : this.root.getTabs()) {
            if (tab.getId().equals(String.valueOf(subject.getID()))) {
                Output.write("Renaming Subject to: " + subject.getName().getValue() + " (ID: " + subject.getID() + ")");
                tab.setText(subject.getName().getValue());
                return;
            }
        }
    }

    /**
     * <p>Deletes the Subject Tab.</p>
     * @param subject required {@link Subject}
     */
    public void deleteSubjectTab(Subject subject){
        this.root.getTabs().removeIf(tab -> tab.getId().equals(String.valueOf(subject.getID())));
    }

    /**
     * <p>The given {@code property} will be bound to selectedSubject property.</p>
     * @param property extern Property
     */
    public void bindExternProperty(Property<Number> property) {
        property.bind(selectedSubject);
    }

    /**
     * <p>This will return view of the SubjectView class.</p>
     * @return the {@link SubjectView} {@link Node}.
     */
    public Node getSubjectView(){
        return this.root;
    }
}
