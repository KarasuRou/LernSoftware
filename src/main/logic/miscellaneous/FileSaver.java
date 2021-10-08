package logic.miscellaneous;

import com.sun.istack.internal.Nullable;
import logic.startup.Launcher;

import java.io.*;

/**
 * <p>This class can copy and Files to the local folder.
 * It will delete the source file if the source file is in the application directory.</p>
 */
public class FileSaver {

    private String fileName = null;
    private String directory = null;

    public FileSaver() {

    }

    /**
     * Constructor
     * @param fileName new FileName
     * @param directory (Nullable) directory to put the file in
     */
    public FileSaver(String fileName, @Nullable String directory){
        setFileName(fileName);
        if (directory != null) {
            setDirectory(directory);
        }
    }

    public void setFileName(String name) {
        this.fileName = name;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    /**
     * @param externalFilePath path to the source file
     * @param deleteIfLocallyExists true/false if the old file should be deleted, if it was locally
     * @return the new path
     * @throws Exception if no fileName ist set. (Or anything else)
     */
    public String copyFileFromOtherFile(String externalFilePath, boolean deleteIfLocallyExists) throws Exception {
        String filePath = Launcher.APPLICATION_PATH;
        if (directory != null) {
            filePath = filePath + Launcher.FILE_SEPARATOR + directory;
            checkIfDirectoryIsExistent(filePath);
        }
        if (fileName == null) {
            throw new Exception("Es wurde keine Dateiname angegeben!");
        }
        filePath = filePath + Launcher.FILE_SEPARATOR + fileName;

        File source = new File(externalFilePath);
        File destination = new File(filePath);

        copyFile(source, destination);

        if (deleteIfLocallyExists) {
            deleteOldFileIfLocally(externalFilePath);
        }

        return filePath;
    }

    private void deleteOldFileIfLocally(String filePath) {
        if (filePath.contains(Launcher.APPLICATION_PATH)) {
            new File(filePath).delete();
        }
    }

    private void copyFile(File source, File destination) throws IOException {
        InputStream in = new FileInputStream(source);
        OutputStream out = new FileOutputStream(destination);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }
        in.close();
        out.close();
    }

    private void checkIfDirectoryIsExistent(String filePath) {
        new File(filePath).mkdir();
    }
}
