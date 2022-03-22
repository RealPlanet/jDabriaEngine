package jDabria.ECP.components.physics;

import jDabria.ECP.components.UI.ImGUIComponent;
import org.joml.Vector3f;

public class RigidBody2D extends ImGUIComponent {

    private int colliderType = 0;
    private float friction = 0.8f;
    private boolean isKinematic = false;

    private Vector3f velocity = new Vector3f(0,0,0);

    public RigidBody2D(){
        useDefaultDrawer = true;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    protected void render() {

    }
}
