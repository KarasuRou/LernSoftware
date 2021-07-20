package model;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import model.question.Question;

/**
 * <p>The Subject class is a model for Subjects.</p><br/>
 * <p>Subjects can store {@linkplain Folder}s and a name, both are property's. The {@code Folder array Property} can contain {@linkplain Question}s.</p>
 */
public class Subject {

    private int ID;
    private final Property<Folder[]> folders = new SimpleObjectProperty<>();
    private final Property<String> name = new SimpleStringProperty();
    private String backgroundPicturePath;

    public Subject(){}

    /**
     * @return Property&lt;String&gt; - Subject name
     */
    public Property<String> getName() {
        return name;
    }

    /**
     * @param name String (Subject name)
     */
    public void setName(String name){
        this.name.setValue(name);
    }

    /**
     * @return Property&lt;{@linkplain Folder}[]&gt; - Folders for the Subject
     */
    public Property<Folder[]> getFolders() {
        return folders;
    }

    /**
     * @param folders {@linkplain Folder}[] (Folders for the Subject)
     */
    public void setFolders(Folder[] folders){
        this.folders.setValue(folders);
    }

    /**
     * Add a new Folder to the Folder property, also returns the value of the property.
     * @param folder add a Folder to the Subject
     * @return {@linkplain Folder}[] - whole Folder array from the property
     */
    public Folder[] addFolder(Folder folder) {
        int length = this.folders.getValue().length + 1;
        Folder[] folders = new Folder[length];

        for (int i = 0; i < length - 1; i++) {
            folders[i] = this.folders.getValue()[i];
        }
        folders[length - 1] = folder;

        setFolders(folders);
        return getFolders().getValue();
    }

    /**
     * Add new Folders to the Folder property, also returns the value of the property.
     * @param folders add Folders to the Subject
     * @return {@linkplain Folder}[] - whole Folder array from the property
     */
    public Folder[] addFolders(Folder[] folders) {
        int length = this.folders.getValue().length + folders.length;
        Folder[] tempFolders = new Folder[length];

        for (int i = 0; i < length - folders.length; i++) {
            tempFolders[i] = this.folders.getValue()[i];
        }
        int y = 0;
        for (int i = length - folders.length; i < length; i++) {
            tempFolders[i] = folders[y++];
        }

        setFolders(tempFolders);
        return getFolders().getValue();
    }

    public String getBackgroundPicturePath() {
        return backgroundPicturePath;
    }

    public void setBackgroundPicturePath(String backgroundPicturePath) {
        this.backgroundPicturePath = backgroundPicturePath;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
