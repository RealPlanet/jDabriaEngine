package commons.math;

public class GMath {
    public static float clamp(float Min, float Max, float Value){
        return Math.max(Min, Math.min(Max, Value));
    }
}
