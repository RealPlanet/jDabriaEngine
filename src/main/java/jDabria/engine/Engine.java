package jDabria.engine;

import jDabria.Window;
import org.jetbrains.annotations.NotNull;

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
        gameWindow.run();
    }

    public int getState(){
        return state.hashCode();
    }

    public boolean isState(@NotNull State other){
        return other.hashCode() == state.hashCode();
    }
}