package logic.startup;

import logic.miscellaneous.Output;
import model.error.updater.*;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * The Updater class can check the availability for a new update, update the application and restart the application.
 */
public class Updater {

    private final static Updater updater = new Updater();
    private boolean available = false;
    private double newVersion, oldVersion;
    private String newPatchNotes = "", oldPatchNotes = "";
    private final String newVersionURLString = "http://Rouven-ra-ro.de/LernSoftware/";

    static {
        try {
            updater.checkUpdateAvailability();
            if (updater.available) {
                updater.loadPatchNotes();
            }
//        updater.debugOutput();
        } catch (UpdaterException | IOException e) {
            Output.exceptionWrite(e);
        }
    }

    /**
     * <p>Constructor for the Updater class.</p>
     * <p>Checks for an update and load the new Patchnotes, if a new version is available.</p>
     */
    public static Updater getInstance() {
        return updater;
    }

    private Updater(){}

    /**
     * <p>This method will load the new update and restart the Application.</p>
     * <p>The Application will only restart if:
     * <ol>
     *     <li>there is an update available</li>
     *     <li>the update was loaded successfully,</li>
     * </ol>
     * in any other case this method will return false.
     * </p>
     * @param restartClass - Class where the main Method is included
     * @return a false, if the Update couldn't be loaded. Else the Application will restart, if an Update is available.
     */
    public boolean loadUpdateAndRestartApplication(Class restartClass) throws Exception{
        if (loadZip() && this.available) {
            processLocal();
            restartApplication(restartClass);
            return true;
        } else {
            return false;
        }
    }

    public boolean isAvailable(){
        return this.available;
    }

    public double getNewVersion() {
        return newVersion;
    }

    public double getCurrentVersion() {
        return oldVersion;
    }

    public String getNewPatchNotes() {
        return newPatchNotes;
    }

    public String getCurrentPatchNotes() {
        return oldPatchNotes;
    }


    private void checkUpdateAvailability() throws UpdaterException, IOException {
        File oldVersionFile = new File("version.txt");
        BufferedReader input = new BufferedReader(
                new InputStreamReader(
                        new URL(newVersionURLString+"version.txt").openStream()));
        String inputLine, newVersion="", oldVersion="";
        while ((inputLine = input.readLine()) != null) {
            newVersion += inputLine;
        }
        input.close();

        Scanner scanner = new Scanner(oldVersionFile);
        while (scanner.hasNext()){
            oldVersion += scanner.nextLine();
        }
        scanner.close();

        if (oldVersion.equals("")) {
            throw new ConfigurationException("Old Version can not be read! Configuration problem?");
        }
        if (newVersion.equals("")) {
            throw new ConfigurationException("New Version can not be read! Configuration problem?");
        }
        this.newVersion = Double.parseDouble(newVersion);
        this.oldVersion = Double.parseDouble(oldVersion);
        if (this.newVersion > this.oldVersion) {
            available = true;
            Output.write("UPDATE AVAILABLE!");
        } else {
            available = false;
        }
    }

    private void loadPatchNotes() throws UpdaterException, IOException{
        Output.write("Loading patchnotes...");
        File oldPatchNotesFile = new File("patchnotes.txt");
        BufferedReader input = new BufferedReader(
                new InputStreamReader(
                        new URL(newVersionURLString + this.newVersion + "/patchnotes.txt").openStream()));
        String inputLine;

        while ((inputLine = input.readLine()) != null) {
            this.newPatchNotes += inputLine;
        }
        input.close();

        Scanner scanner = new Scanner(oldPatchNotesFile);
        while (scanner.hasNext()){
            this.oldPatchNotes += scanner.nextLine();
        }
        scanner.close();

        if (this.newPatchNotes.equals("")) {
            throw new ConfigurationException("No Patchnotes are available! Configuration Problem?");
        }
        if (this.oldPatchNotes.equals("")) {
            throw new ConfigurationException("Saved Patchnotes are not available! Configuration Problem?");
        }
        Output.write("Patchnotes loaded!");
    }

    private boolean loadZip(){
        Output.write("Downloading update files...");
        try {
            BufferedInputStream in = new BufferedInputStream(
                    new URL(newVersionURLString + newVersion + "/LernSoftware.zip").openStream());
            FileOutputStream fileOutputStream = new FileOutputStream("LernSoftware.zip");
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            fileOutputStream.close();
            in.close();
            Output.write("Download complete!");
            return true;
        } catch (IOException e) {
            Output.exceptionWrite(e,"Update Couldn't be loaded!");
        }
        return false;
    }

    private void restartApplication(Class restartClass) throws Exception{
        Output.write("Restarting application...");
        String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        File currentJar = new File(restartClass.getProtectionDomain().getCodeSource().getLocation().toURI());

        if(!currentJar.getName().endsWith(".jar"))
            return;

        ArrayList<String> command = new ArrayList<>();
        command.add(javaBin);
        command.add("-jar");
        command.add(currentJar.getPath());

        ProcessBuilder builder = new ProcessBuilder(command);
        builder.start();
        System.exit(0);
    }

    private void debugOutput(){
        Output.write("Neues Update verfügbar? -> " + isAvailable());
        Output.write("Neue Version: " + getNewVersion());
        Output.write("Momentane Version: " + getCurrentVersion());
        Output.write("Neue Patchnotes: \r\n" + getNewPatchNotes() + "\r\n");
        Output.write("Momentane Patchnotes: \r\n" + getCurrentPatchNotes() + "\r\n");
    }

    private void processLocal() {
        try {
            Output.write("Processing files locally...");
            String zipFile = "LernSoftware.zip";
            File destDir = new File(Launcher.APPLICATION_PATH+Launcher.FILE_SEPARATOR);
            byte[] buffer = new byte[1024];
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry zipEntry = zis.getNextEntry();

            while (zipEntry != null) {
                File newFile = newFile(destDir, zipEntry);

                if (zipEntry.isDirectory()) {
                    if (!newFile.isDirectory() && !newFile.mkdirs()) {
                        throw new IOException("Failed to create directory " + newFile);
                    }
                } else {
                    // fix for Windows-created archives
                    File parent = newFile.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("Failed to create directory " + parent);
                    }

                    // write file content
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                }
                zipEntry = zis.getNextEntry();
            }
            zis.close();
        } catch (Exception e) {
            Output.exceptionWrite(e);
        }
        Output.write("File processing successful!");
        cleanUp();
    }

    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        if (destFile.isDirectory()) {
            Output.write("Writing Directory: " + destFile.getAbsolutePath());
        } else {
            Output.write("Writing File: " + destFile.getAbsolutePath());
        }

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    private void cleanUp() {
        Output.write("Cleaning the update-files...");

        File file = new File("LernSoftware.zip");
        if(file.delete())
        {
            Output.write(file.getAbsolutePath() + " deleted successfully");
        }
        else
        {
            Output.errorWrite("Failed to delete " + file.getName());
        }
        Output.write("Clean up complete!");
    }
}
