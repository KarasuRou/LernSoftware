package logic.miscellaneous;

public class FileSaver {

    // fileName overrides subjectID
    private int subjectID;
    private String fileName;
    // if it should be saved in a directory
    private boolean hasOwnDirectory = false;
    private String directory = "";

    public FileSaver() {

    }

    public FileSaver(int subjectID) {
        this.subjectID = subjectID;
    }

    public void setFileName(String name) {
        this.fileName = name;
    }

    public String savePictureLocally(String backgroundPicturePath) {
        return backgroundPicturePath;
    }
}
