package jDabria.ECP.components;

import jDabria.ECP.base.Component;
import jDabria.ECP.base.RequiredComponent;
import jDabria.ECP.base.SingleComponent;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

/**
 * The only component ALWAYS preset on a gameobject
 */
public class Transform extends Component implements SingleComponent, RequiredComponent {
    public Vector3f position= new Vector3f();
    public Vector3f scale = new Vector3f(1,1,1);

    public Transform(){}

    public Transform(Vector3f pos){
        position = pos;
    }

    public Transform(Vector3f pos, Vector3f scl){
        position = pos;
        scale = scl;
    }

    public Transform(@NotNull Transform transform) {
        position = new Vector3f(transform.position);
        scale = new Vector3f(transform.scale);
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
        if(!(o instanceof Transform)){
            return false;
        }

        Transform t = (Transform)o;
        return  t.position.equals(position) &&
                t.scale.equals(scale);
    }
}
