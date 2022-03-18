package commons;

import commons.math.GMath;
import org.jetbrains.annotations.NotNull;

import java.beans.ConstructorProperties;


public class Color{
    //region Pre-made colors
    public transient static final Color WHITE         = new Color(1,1,1,1);
    public transient static final Color BLACK         = new Color(0,0,0,1);
    public transient static final Color RED           = new Color(1,0,0,1);
    public transient static final Color GREEN         = new Color(0,1,0,1);
    public transient static final Color BLUE          = new Color(0,0,1,1);
    public transient static final Color PURPLE        = new Color(0.75f, 0, 1, 1);
    //endregion

    //region Member variables
    private float Red, Green, Blue;
    private float Alpha = 1f;
    //endregion

    //region Constructors
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

        this.Red = GMath.clamp(0, 1, r);
        this.Green = GMath.clamp(0, 1, g);
        this.Blue = GMath.clamp(0, 1, b);
        this.Alpha = GMath.clamp(0, 1, a);
    }

    public Color(){}
    //endregion

    //region Getters
    public float getRed() { return Red; }
    public float getGreen() { return Green; }
    public float getBlue() { return Blue; }
    public float getAlpha() { return Alpha; }
    //endregion

    //region Setters
    public void setColor(@NotNull Color newColor){
        Red = newColor.Red;
        Green = newColor.Green;
        Blue = newColor.Blue;
    }
    //endregion
}
