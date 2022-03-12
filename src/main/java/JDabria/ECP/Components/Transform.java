package JDabria.ECP.Components;

import org.joml.Vector3f;

/**
 * The only component ALWAYS preset on a gameobject
 */
public class Transform {
    public Vector3f position;
    public Vector3f scale;

    public Transform(){
        position = new Vector3f();
        scale = new Vector3f(1,1,1);
    }

    public Transform(Vector3f pos){
        this();
        position = pos;
    }

    public Transform(Vector3f pos, Vector3f scl){
        this();

        position = pos;
        scale = scl;
    }
}
