package Commons.Math;

public class Vector3 {
    public float x;
    public float y;
    public float z;

    public Vector3(float _x, float _y, float _z){
        x = _x;
        y = _y;
        z = _z;
    }

    public static final Vector3 One = new Vector3(1, 1, 1);
    public static final Vector3 Zero = new Vector3(0, 0, 0);
}
