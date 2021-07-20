package model;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import model.question.Question;

/**
 * <p>The Folder class is a model for Folders.</p><br/>
 * <p>Folders contains {@linkplain Question}s wich are stored in a {@code Question array Property}.<br/>A Folder has a name property.</p>
 * <p>Folders can be stored in {@linkplain Subject}s.</p>
 */
public class Folder {

    private int ID;
    private final Property<Question[]> questions = new SimpleObjectProperty<>();
    private final Property<String> name = new SimpleStringProperty();

    public Folder(){}

    /**
     * @return Property&lt;String&gt; - Folder name
     */
    public Property<String> getName() {
        return name;
    }

    /**
     * @param name String (Folder name)
     */
    public void setName(String name){
        this.name.setValue(name);
    }

    /**
     * @return Property&lt;{@linkplain Question}[]&gt; - Folder questions
     */
    public Property<Question[]> getQuestions() {
        return questions;
    }

    /**
     * @param questions {@linkplain Question}[] (Folder questions)
     */
    public void setQuestions(Question[] questions){
        this.questions.setValue(questions);
    }

    /**
     * Add a new Question to the Question property, also returns the value of the property.
     * @param question add a Question to the Folder
     * @return {@linkplain Question}[] - whole Question array from the property
     */
    public Question[] addQuestion(Question question){
        int length = this.questions.getValue().length+1;
        Question[] questions = new Question[length];
        for (int i = 0; i < length - 1; i++) {
            questions[i] = this.questions.getValue()[i];
        }
        questions[length - 1] = question;
        setQuestions(questions);
        return getQuestions().getValue();
    }

    /**
     * Add new {@linkplain Question}s to the Question property, also returns the value of the property.
     * @param questions add Questions to the Folder
     * @return {@linkplain Question}[] - whole Question array from the property
     */
    public Question[] addQuestions(Question[] questions) {
        int length = this.questions.getValue().length + questions.length;
        Question[] tempQuestions = new Question[length];
        for (int i = 0; i < length - questions.length; i++) {
            tempQuestions[i] = this.questions.getValue()[i];
        }
        int y = 0;
        for (int i = length - questions.length; i < length; i++) {
            tempQuestions[i] = questions[y++];
        }
        setQuestions(tempQuestions);
        return getQuestions().getValue();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
