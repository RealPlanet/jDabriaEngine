package jDabria.ECP;

import commons.StringUtils;
import jDabria.ECP.base.Component;
import jDabria.ECP.base.RequiredComponent;
import jDabria.ECP.base.SingleComponent;
import jDabria.ECP.components.Transform;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;

public class GameObject{
    private String name;
    private final ArrayList<Component> components = new ArrayList<>(2);
    private transient Transform transform;
    private boolean isActive = false;

    //region Constructors
    public GameObject(){
        this(String.valueOf(Math.random()));
    }

    public GameObject(String name){
        this(name, new Vector3f(0,0,0), new Vector3f(1,1,1));
    }

    public GameObject(String name, @NotNull Vector3f position){
        this(name, position, new Vector3f(1,1,1));
    }

    public GameObject(String name, @NotNull Vector2f position){
        this(name, position, new Vector3f(1,1,1));
    }

    public GameObject(String name, @NotNull Vector3f position, Vector3f scale){
        this.name = name;
        Transform tmp = new Transform(new Vector3f(position), new Vector3f(scale));
        this.addComponent(tmp);
        this.transform = tmp;

        enableComponents();
    }

    public GameObject(String name, @NotNull Vector2f position, Vector3f scale){
        this(name, new Vector3f(position.x, position.y, 0), scale);
    }

    public GameObject(String name, @NotNull Transform transform) {
        this(name, transform.position, transform.scale);
    }
    //endregion

    //region Private methods
    private void enableComponents(){
        isActive = true;
        for(Component component : components){
            component.start();
        }
    }

    private void disableComponents(){
        isActive = false;
        for(Component Comp : components){
            Comp.stop();
        }
    }
    //endregion

    //region Public methods
    public void update() {
        if(!isActive){
            return;
        }

        for(Component component : components){
            component.update();
        }
    }

    public void delete() {
        for (Component c: components ) {
            c.delete();
        }
    }
    //endregion

    //region Setters
    public void setActive(boolean active){
        isActive = active;

        if (isActive){
            enableComponents();
            return;
        }
        disableComponents();
    }

    public void setName(String name){
        this.name = name;
    }

    public void setTransform(@NotNull Transform transform){
        if(transform.gameObject != null){
            throw new MultipleOwnerException("Cannot assign transform already owned by another game object");
        }

        this.transform = transform;
        transform.gameObject = this;
        removeComponent(Transform.class);
        if(components.size() > 0){
            components.set(0, transform);
        }

        components.add(0, transform);
    }

    public void setPosition(Vector3f position){
        transform.position = position;
    }

    public void setScale(Vector3f scale){
        transform.scale = scale;
    }
    //endregion

    //region Getters
    public boolean isActive(){
        return isActive;
    }

    public String getName(){
        return name;
    }

    public Transform getTransform(){
        return transform.copy();
    }

    public Vector3f getPosition(){
        return transform.position;
    }

    public Vector3f getScale(){
        return transform.scale;
    }
    //endregion

    // region Component operations
    public <T extends  Component> T getComponent(Class<T> componentClass){
        for(Component component : components){
            if(componentClass.isAssignableFrom(component.getClass())){
                try{
                    return componentClass.cast(component);
                }
                catch(ClassCastException e){
                    e.printStackTrace();
                    assert false : "ERROR (Component) -> Casting Component";
                }
            }
        }

        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass){
        if(componentClass.isAssignableFrom(RequiredComponent.class)){
            throw new RequiredComponentException(StringUtils.format("Cannot remove this component from game-object --> {0}", componentClass));
        }

        for(int i = 0; i < components.size(); i++){
            Component component = components.get(i);
            if(componentClass.isAssignableFrom(component.getClass())){
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component component){
        if(component instanceof SingleComponent){
            if(component instanceof Transform){
                setTransform((Transform) component);
                return;
            }

            if(components.contains(component)){
                throw new SingleComponentException("Multiple components are not allowed");
            }
        }

        components.add(component);
        component.gameObject = this;

        if(isActive()){
            component.start();
        }
    }
    // endregion

    //region Exceptions
    public class SingleComponentException extends RuntimeException{
        public SingleComponentException(String message) {
            super(message);
        }
    }

    public class RequiredComponentException extends RuntimeException{
        public RequiredComponentException(String message) {
            super(message);
        }
    }

    public class MultipleOwnerException extends RuntimeException{
        public MultipleOwnerException(String message) {
            super(message);
        }
    }
    //endregion
}
