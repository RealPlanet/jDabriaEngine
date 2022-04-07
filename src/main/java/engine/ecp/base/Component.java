package engine.ecp.base;

import engine.ecp.GameObject;
import engine.exception.ReinitializationException;

@SuppressWarnings("EmptyMethod")
public abstract class Component{
    private static long ID_COUNT = -1;
    private long UID = -1;
    // region static methods
    public static void setIdCount(long newCount){
        if(ID_COUNT < 0){
            ID_COUNT = newCount;
            return;
        }

        throw new ReinitializationException("ID_COUNT was already set for components!");
    }
    // endregion

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
            UID = ++ID_COUNT;
        }

        throw new ReinitializationException("Component already has ID.");
    }

    public long getUID(){ return UID; }
}
