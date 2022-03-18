package jDabria.ECP.components;

import org.jetbrains.annotations.NotNull;
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

    public Transform copy(){
        return new Transform(new Vector3f(this.position), new Vector3f(this.scale));
    }

    public void copy(@NotNull Transform to){
        to.position.set(position);
        to.scale.set(scale);
    }

    @Override
    public boolean equals(Object o){
        if(o == null || !(o instanceof Transform)){
            return false;
        }

        Transform t = (Transform)o;
        return  t.position == position &&
                t.scale == scale;
    }
}
