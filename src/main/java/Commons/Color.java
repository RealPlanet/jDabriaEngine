package Commons;

import Commons.Math.GMath;
import Commons.Math.Vector3;
import org.jetbrains.annotations.NotNull;

public class Color {
    private final Vector3 Internal = Vector3.One;
    private float Alpha = 1f;

    public void SetColor(@NotNull Vector3 NewColor, float NewAlpha ){

        Internal.x = GMath.Clamp(0, 1, NewColor.x);
        Internal.y = GMath.Clamp(0, 1, NewColor.y);
        Internal.z = GMath.Clamp(0, 1, NewColor.z);
        Alpha = GMath.Clamp(0, 1, NewAlpha);
    }

    public void SetColor255(@NotNull Vector3 NewColor255, float NewAlpha){
        NewColor255.x /= 255;
        NewColor255.y /= 255;
        NewColor255.z /= 255;

        SetColor(NewColor255, NewAlpha);
    }

    public void SetColor(@NotNull Color NewColor){
        Internal.x = NewColor.R();
        Internal.y = NewColor.G();
        Internal.z = NewColor.B();
    }

    public Color(float r, float g, float b, float a){
        SetColor(new Vector3(r, g, b), a);
    }

    public Color(){}

    public float R() { return Internal.x; }
    public float G() { return Internal.y; }
    public float B() { return Internal.z; }
    public float A() { return Alpha; }
}
