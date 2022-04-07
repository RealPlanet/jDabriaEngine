package engine.ecp.base;

import engine.ecp.GameObject;

import java.util.UUID;

@SuppressWarnings("EmptyMethod")
public abstract class Component{
    private transient static long ID_COUNT = 0;
    private UUID UID;

    public Component(){
        UID = null;
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
        if(UID == null){
            UID = UUID.randomUUID();
        }
    }

    public UUID getUID(){ return UID; }
}
