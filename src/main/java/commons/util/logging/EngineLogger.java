package commons.util.logging;

import commons.util.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

public class EngineLogger {
    private static final Logger LOGGER = Logger.getLogger("DABRIA LOG");
    private static final String LOG_DIR = System.getProperty("user.dir") + "\\EngineLogs\\";
    private static final boolean LOG_AS_XML = false;
    private static boolean isInit = false;

    public static void log(String message){
        checkInit();
        LOGGER.log(Level.INFO, formatLogWithCalleeInfo(message));
    }

    public static void logWarning(String message){
        checkInit();
        LOGGER.log(Level.WARNING, formatLogWithCalleeInfo(message));
    }

    public static void logError(String message){
        checkInit();
        LOGGER.log(Level.SEVERE, formatLogWithCalleeInfo(message));
    }

    private static @NotNull String formatLogWithCalleeInfo(String message){
        // 0 is getStackTrace() call
        // 1 is this method
        // 2 is prev method, which should only be log methods
        // 3 is the method which asked for logging
        final int STACK_INDEX = 3;

        StackTraceElement[] trace = Thread.currentThread().getStackTrace();

        int lineNumber = trace[STACK_INDEX].getLineNumber();
        String className = trace[STACK_INDEX].getClassName();
        String methodName = trace[STACK_INDEX].getMethodName();

        return StringUtils.format("{0}. From: {1}::{2} at line number {3}",message, className, methodName, lineNumber);
    }

    public static void close(){
        for(Handler handler : LOGGER.getHandlers()){
            handler.close();
        }
    }

    public static void open() {
        try {
            //noinspection ResultOfMethodCallIgnored
            new File(LOG_DIR).mkdir();

            LogManager.getLogManager().readConfiguration(EngineLogger.class.getClassLoader().getResourceAsStream("Logging.prop"));
            FileHandler handler = new FileHandler(LOG_DIR + "LatestEngineLog.txt");

            if(!LOG_AS_XML){
                handler.setFormatter( new SimpleFormatter()); // Make output plain text instead of xml
            }
            LOGGER.addHandler(handler);
        } catch (IOException e) {
            e.printStackTrace();
        }

        isInit = true;
    }

    private static void checkInit(){
        if(!isInit){
            throw new NotInitializedException("Must call \"open\" method before using EngineLogger");
        }
    }

    public static class NotInitializedException extends RuntimeException{
        NotInitializedException(String m){
            super(m);
        }
    }
}
