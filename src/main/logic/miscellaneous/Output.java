package logic.miscellaneous;

import logic.startup.Launcher;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * <p>The Output class will output the given text into the console and writes it to a log file.</p>
 * <p>Can be used to replace {@code System.out.println()} and {@code Exception.printStackTrace()}.</p>
 * <p>Just use on of these methods: {@linkplain #write} / {@linkplain #errorWrite} / {@linkplain #exceptionWrite} .</p>
 */
public class Output {

    private static final String PATH = Launcher.APPLICATION_PATH + Launcher.FILE_SEPARATOR + "logs";
    private static final String FILE_PATH = PATH + Launcher.FILE_SEPARATOR +
            "stdout_" + new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime()) + ".log";
    private static Writer writer;


    static {
        try {
            new File(PATH).mkdir();
            writer = new FileWriter(FILE_PATH,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Output(){}

    /**
     * <p>This method will output the {@code output} text + the class called name as source.</p>
     * @param output output text
     */
    public static void write(String output){
        int maxLength = Thread.currentThread().getStackTrace()[2].getClassName().split("\\.").length-1;
        String source = Thread.currentThread().getStackTrace()[2].getClassName().split("\\.")[maxLength];
        write(output,source);
    }
    /**
     * <p>This method will output the {@code output} text + the replaced class name as {@code source}.</p>
     * @param output output text
     * @param source replaces the class name with another source name
     */
    public static void write(String output, String source){
        write(output,source,System.out);
    }

    /**
     * <p>This method will {@code output} an error text, with the class called name as source.</p>
     * @param output output text
     */
    public static void errorWrite(String output){
        int maxLength = Thread.currentThread().getStackTrace()[2].getClassName().split("\\.").length-1;
        String source = Thread.currentThread().getStackTrace()[2].getClassName().split("\\.")[maxLength];
        errorWrite(output, source);
    }
    /**
     * <p>This method will {@code output} an error text, with the replaced class name as {@code source}.</p>
     * @param output output text
     * @param source replaces the class name with another source name
     */
    public static void errorWrite(String output, String source){
        output = "ERROR: " + output;
        write(output, source, System.err);
    }

    /**
     * <p>Replaces the {@code Exception.printStackTrace()}.</p>
     * <p>This method will output the exception in the Console and Logfile.</p>
     * @param exception the given exception
     */
    public static void exceptionWrite(Exception exception){
        privateExceptionWrite(exception,"");
    }
    /**
     * <p>Replaces the {@code Exception.printStackTrace()}.</p>
     * <p>This method will output the exception in the Console and Logfile.</p>
     * <p>Also outputs a cause in addition to the given exception.</p>
     * @param exception the given exception
     * @param cause additional cause
     */
    public static void exceptionWrite(Exception exception, String cause) {
        privateExceptionWrite(exception,cause);
    }


    /**
     * Closes the Output class, cleaner Shutdown
     */
    public static void close() {
        try {
            writer.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static void write(String output,String source, PrintStream stream){
        try {
            output = currentTime() + " " + source + ": " + output;
            stream.println(output);
            writer.write(output+"\r\n");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static void privateExceptionWrite(Exception exception, String cause) {
        StringBuilder out = new StringBuilder(exception.toString());
        if (!cause.equals("")) {
            out.append(": ").append(cause);
        }
        for (StackTraceElement s : exception.getStackTrace()){
            out.append("\r\n\tat ").append(s.toString());
        }
        write(out.toString(), "EXCEPTION", System.err);

    }

    private static String currentTime(){
        return new SimpleDateFormat("[dd.MM.yyyy][HH:mm:ss]").format(Calendar.getInstance().getTime());
    }
}
