package JDabria.ECP.Components;

import org.joml.Vector3f;

/**
 * The only component ALWAYS preset on a gameobject
 */
public class Transform {
    public Vector3f Position;
    public Vector3f Scale;

    public Transform(){
        Position = new Vector3f();
        Scale = new Vector3f(1,1,1);
    }

    public Transform(Vector3f Pos){
        this();
        Position = Pos;
    }

    public Transform(Vector3f Pos, Vector3f Scl){
        this();

        Position = Pos;
        Scale = Scl;
    }
}
