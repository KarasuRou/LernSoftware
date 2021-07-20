package logic.startup;

import logic.miscellaneous.Output;
import model.error.updater.*;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

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
//        debugOutput();
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
     * <p>The {@linkplain Updater#loadUpdateAndRestartApplication} method will load the new update and restart the Application.</p>
     * <p>The Application will only restart if:
     * <ol>
     *     <li>there is an update available</li>
     *     <li>the update was loaded successfully,</li>
     * </ol>
     * in any other case the {@linkplain Updater#loadUpdateAndRestartApplication} method will return false.
     * </p>
     * @param restartClass - Class where the main Method is included
     * @return a false, if the Update couldn't be loaded. Else the Application will restart, if an Update is available.
     */
    public boolean loadUpdateAndRestartApplication(Class restartClass) throws Exception{
        if (loadUpdate() && this.available) {
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

    public double getOldVersion() {
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
        } else {
            available = false;
        }
    }

    private void loadPatchNotes() throws UpdaterException, IOException{
        File oldPatchNotesFile = new File("patchnotes.txt");
        BufferedReader input = new BufferedReader(
                new InputStreamReader(
                        new URL(newVersionURLString + this.newVersion + "/patchnotes.txt").openStream()));
        String inputLine = "";

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
    }

    private boolean loadUpdate(){
        try {
            BufferedInputStream in = new BufferedInputStream(
                    new URL(newVersionURLString + newVersion + "/LernSoftware.jar").openStream());
            FileOutputStream fileOutputStream = new FileOutputStream("LernSoftware.jar");
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            fileOutputStream.close();
            in.close();
            return true;
        } catch (IOException e) {
            Output.exceptionWrite(e,"Update Couldn't be loaded!");
        }
        return false;
    }

    private void restartApplication(Class restartClass) throws Exception{
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
        Output.write("Alte Version: " + getOldVersion());
        Output.write("Neue Patchnotes: \r\n" + getNewPatchNotes());
        Output.write("Momentane Patchnotes: \r\n" + getCurrentPatchNotes());
    }
}
