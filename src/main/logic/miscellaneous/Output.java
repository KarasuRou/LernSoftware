package logic.miscellaneous;

import logic.startup.Launcher;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

//TODO JAVADOC
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

    //TODO JAVADOC
    public static void write(String output){
        int maxLength = Thread.currentThread().getStackTrace()[2].getClassName().split("\\.").length-1;
        String source = Thread.currentThread().getStackTrace()[2].getClassName().split("\\.")[maxLength];
        write(output,source);
    }
    //TODO JAVADOC
    public static void write(String output, String source){
        write(output,source,System.out);
    }

    //TODO JAVADOC
    public static void errorWrite(String output){
        int maxLength = Thread.currentThread().getStackTrace()[2].getClassName().split("\\.").length-1;
        String source = Thread.currentThread().getStackTrace()[2].getClassName().split("\\.")[maxLength];
        errorWrite(output, source);
    }
    //TODO JAVADOC
    public static void errorWrite(String output, String source){
        write(output, source, System.err);
    }

    //TODO JAVADOC
    public static void exceptionWrite(Exception exception){
        privateExceptionWrite(exception,"");
    }
    //TODO JAVADOC
    public static void exceptionWrite(Exception exception, String cause) {
        privateExceptionWrite(exception,cause);
    }

    //TODO JAVADOC
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
