package Commons;

import Commons.Math.GMath;
import org.jetbrains.annotations.NotNull;

import java.beans.ConstructorProperties;


public class Color{
    private float Red, Green, Blue;
    private float Alpha = 1f;

    public void SetColor(@NotNull Color NewColor){
        Red = NewColor.Red;
        Green = NewColor.Green;
        Blue = NewColor.Blue;
    }

    @ConstructorProperties({"red", "green", "blue", "alpha"})
    public Color(float r, float g, float b, float a){
        if(r > 1.0f){
            r /= 255f;
        }

        if(g > 1.0f){
            g /= 255f;
        }

        if(b > 1.0f){
            b /= 255f;
        }

        if(a > 1.0f){
            a /= 255f;
        }

        this.Red = GMath.Clamp(0, 1, r);
        this.Green = GMath.Clamp(0, 1, g);
        this.Blue = GMath.Clamp(0, 1, b);
        this.Alpha = GMath.Clamp(0, 1, a);
    }

    public Color(){}

    public float GetRed() { return Red; }
    public float GetGreen() { return Green; }
    public float GetBlue() { return Blue; }
    public float GetAlpha() { return Alpha; }

}
