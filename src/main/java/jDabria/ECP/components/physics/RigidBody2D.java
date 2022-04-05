package jDabria.ECP.components.physics;

import jDabria.ECP.base.Component;
import org.joml.Vector3f;

public class RigidBody2D extends Component {

    private transient RigidBody2DEditor rbEditor;

    private int colliderType = 0;
    private float friction = 0.8f;
    private boolean isKinematic = false;

    private Vector3f velocity = new Vector3f(0,0,0);

    public RigidBody2D(){
        rbEditor = new RigidBody2DEditor();
    }

    @Override
    public void start(){
        super.start();
        if(rbEditor.attach(this)){
            rbEditor.setWindowName("RigidBody2D");
            rbEditor.start();
        }
        else{
            rbEditor = null;
        }
    }

    @Override
    public void stop() {
        super.stop();
        rbEditor.stop();
    }
}
