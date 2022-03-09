package Commons.Math;

public class GMath {
    public static float Clamp(float Min, float Max, float Value){
        return Math.max(Min, Math.min(Max, Value));
    }
}
