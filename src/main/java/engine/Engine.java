package engine;

import commons.logging.EngineLogger;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Engine {

    public void setState(State newState) {
        state = newState;
    }

    public enum State{
        ERR,
        EDITOR_MODE,
        PLAY_MODE
    }

    private static Engine instance = null;
    private static Thread engineThread = null;

    private Window gameWindow;
    private State state = State.ERR;

    private Engine(){
        gameWindow = Window.getWindow();
    }

    public static @NotNull Engine getEngine(){
        if(instance == null){
            instance = new Engine();
        }

        return instance;
    }

    public void run(){
        engineThread = new Thread(() -> {
            gameWindow.run();
        });
        engineThread.setUncaughtExceptionHandler((myThread, e) -> {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            EngineLogger.logError("[[ "+myThread.getName()+" ]] :: has uncaught exception:\n" + e + "\n" + stringWriter.toString());
        });
        // This is intention for now. I want to handle uncaught exceptions and stop the execution of this method
        engineThread.start();
        try {
            engineThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public State getState(){
        return state;
    }

    public boolean isState(@NotNull State other){
        return other == state;
    }
}
