package Commons;

import JDabria.Window;

public class Time{

    //region Singleton
    private static final Time _Instance = new Time();

    private static Time Get(){
        return _Instance;
    }

    private Time(){
        Window.AddBeginFrameListener(() -> {
            // Delta is calculated at the beginning, this way it properly tracks time between last frame and the new one
            _deltaTime = (FrameEndTime - FrameBeginTime) * 1E-9;
            FrameEndTime = FrameBeginTime = System.nanoTime();
        });

        Window.AddEndFrameListener(() -> {
            FrameEndTime = System.nanoTime();
        });
    }
    //endregion

    // Time this application was started
    private static final float ApplicationStartTime = System.nanoTime();
    private double FrameBeginTime = 0.0f;
    private double FrameEndTime = 0.0f;

    // The interval in seconds from the last frame to the current one
    private double _deltaTime = Double.MIN_VALUE;


    public static float DeltaTime(){
        return (float)Get()._deltaTime;
    }

    public static double GetApplicationStartTime(){
        return ApplicationStartTime;
    }
    // Returns passed time in seconds since start of application
    public static float Time(){ return (float)( (System.nanoTime() - ApplicationStartTime) * 1E-9); }
}
