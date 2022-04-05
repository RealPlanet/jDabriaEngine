package engine.ecp.base;

import engine.ecp.GameObject;

@SuppressWarnings("EmptyMethod")
public abstract class Component{
    // This field is marked as transient to avoid a circular reference when serializing the component.
    // This means that serializing a component will lose the game object reference!
    public transient GameObject gameObject = null;

    public void start(){}

    public void stop(){}

    public void update(){}

    public void delete(){}
}
