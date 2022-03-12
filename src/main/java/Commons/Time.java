package Commons;

import JDabria.Window;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Time{

    //region Singleton
    private static final Time instance = new Time();

    private static Time get(){
        return instance;
    }

    private Time(){
        Window.addBeginFrameListener(() -> {
            // Delta is calculated at the beginning, this way it properly tracks time between last frame and the new one
            _deltaTime = (frameEndTime - frameBeginTime) * 1E-9;
            frameEndTime = frameBeginTime = System.nanoTime();
        });

        Window.addEndFrameListener(() -> {
            frameEndTime = System.nanoTime();
        });
    }
    //endregion

    // Time this application was started
    private double frameBeginTime = 0.0f;
    private double frameEndTime = 0.0f;

    // The interval in seconds from the last frame to the current one
    private double _deltaTime = Double.MIN_VALUE;

    public static float deltaTime(){
        return (float) get()._deltaTime;
    }

    // Returns passed time in seconds since start of application
    public static float getTime(){ return (float)( glfwGetTime() ); }
}
