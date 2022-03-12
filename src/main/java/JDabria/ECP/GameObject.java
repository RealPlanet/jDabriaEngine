package JDabria.ECP;

import JDabria.ECP.Components.Transform;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private String name;
    private final List<Component> components = new ArrayList<>();
    public final Transform transform = new Transform();

    private boolean isActive = false;

    public GameObject(String name){
        this.name = name;

        enableComponents();
    }

    @ConstructorProperties({"name", "position"})
    public GameObject(String name, @NotNull Vector3f position){
        this.name = name;
        transform.position = position;

        enableComponents();
    }

    @ConstructorProperties({"name", "position"})
    public GameObject(String name, @NotNull Vector2f position){
        this.name = name;
        transform.position.x = position.x;
        transform.position.y = position.y;
        transform.position.z = 0f;

        enableComponents();
    }

    @ConstructorProperties({"name", "position", "scale"})
    public GameObject(String name, @NotNull Vector3f position, Vector3f scale){
        this(name, position);
        transform.scale = scale;
    }

    @ConstructorProperties({"name", "position", "scale"})
    public GameObject(String name, @NotNull Vector2f position, Vector3f scale){
        this(name, position);
        transform.scale = scale;
    }

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

    public void setActive(boolean active){
        isActive = active;

        if (isActive){
            enableComponents();
            return;
        }
        disableComponents();
    }

    public boolean isActive(){
        return isActive;
    }

    public void setName(String name){
        this.name = name;
    }

    public String GetName(){
        return name;
    }

    //<editor-fold desc="Component operations">
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
        for(int i = 0; i < components.size(); i++){
            Component component = components.get(i);
            if(componentClass.isAssignableFrom(component.getClass())){
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component component){
        components.add(component);
        component.gameObject = this;
    }
    //</editor-fold>

    public void update() {
        if(!isActive){
            return;
        }

        for(Component component : components){
            component.update();
        }
    }
}
