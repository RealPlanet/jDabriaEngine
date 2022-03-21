package commons;

import jDabria.Window;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Time{

    //region Singleton
    private transient static final Time instance = new Time();

    @SuppressWarnings("SameReturnValue")
    private static Time get(){
        return instance;
    }

    private Time(){
        Window.addBeginFrameListener(() -> {
            // Delta is calculated at the beginning, this way it properly tracks time between last frame and the new one
            _deltaTime = (frameEndTime - frameBeginTime);
            frameEndTime = frameBeginTime = getTime();
        });

        Window.addEndFrameListener(() -> frameEndTime = getTime());
    }
    //endregion

    // Time this application was started
    private transient double frameBeginTime = 0.0f;
    private transient double frameEndTime = 0.0f;

    // The interval in seconds from the last frame to the current one
    private double _deltaTime = Double.MIN_VALUE;

    public static float deltaTime(){
        return (float) get()._deltaTime;
    }

    // Returns passed time in seconds since start of application
    public static float getTime(){ return (float)( glfwGetTime() ); }
}
