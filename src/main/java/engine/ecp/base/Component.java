package engine.ecp.base;

import engine.ecp.GameObject;

@SuppressWarnings("EmptyMethod")
public abstract class Component{
    private transient static long ID_COUNT = 0;
    private long UID = -1;

    public Component(){
        UID = -1;
    }

    // This field is marked as transient to avoid a circular reference when serializing the component.
    // This means that serializing a component will lose the game object reference!
    public transient GameObject gameObject = null;

    // region empty methods
    public void start(){}

    public void stop(){}

    public void update(){}

    public void delete(){}
    // endregion

    public void generateID(){
        if(UID < 0){
            UID = ID_COUNT++;
        }
    }

    public long getUID(){ return UID; }
}
