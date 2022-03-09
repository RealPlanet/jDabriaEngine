package Commons;

import JDabria.Window;

public class Time{

    // Returns passed time in seconds since start of application
    public static float GetTimeSinceStartOfApplication(){ return (float)( (System.nanoTime() - ApplicationStartTime) * 1E-9); }

    // Time this application was started
    private static final float ApplicationStartTime = System.nanoTime();
    private static double FrameBeginTime = 0.0f;
    private static double FrameEndTime = 0.0f;

    // The interval in seconds from the last frame to the current one
    private static double _deltaTime = 0.0f;

    static{
        Window.AddBeginFrameListener(() ->{
            // Delta is calculated at the beginning, this way it properly tracks time between last frame and the new one
            _deltaTime = (FrameEndTime - FrameBeginTime) * 1E-9;

            FrameEndTime = FrameBeginTime = System.nanoTime();
        });

        Window.AddEndFrameListener(() -> {

            FrameEndTime = System.nanoTime();
        });
    }

    public static float DeltaTime(){
        return (float)_deltaTime;
    }
    public static double GetApplicationStartTime(){
        return ApplicationStartTime;
    }
}
