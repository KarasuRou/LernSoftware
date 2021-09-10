package ui.components.TopMenu;

//TODO Create Menu "File"
public class File {

    private final static File file = new File();

    private File(){}

    public static File getInstance() {
        return file;
    }
}