package Commons.Math;

public class Vector2{
    public float x = 0f;
    public float y = 0f;

    public Vector2(float _x, float _y){
        x = _x;
        y = _y;
    }

    public Vector2(){}

    public static final Vector2 Zero = new Vector2(0, 0);
    public static final Vector2 One = new Vector2(1, 1);
}
