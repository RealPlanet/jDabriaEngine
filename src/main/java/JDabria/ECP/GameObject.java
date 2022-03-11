package JDabria.ECP;

import JDabria.ECP.Components.Transform;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private String Name;
    private final List<Component> Components = new ArrayList<>();
    public final Transform Transform = new Transform();

    private boolean IsActive = false;

    public GameObject(String Name){
        this.Name = Name;

        EnableComponents();
    }

    @ConstructorProperties({"Name", "Position"})
    public GameObject(String Name, @NotNull Vector3f Position){
        this.Name = Name;
        Transform.Position = Position;

        EnableComponents();
    }

    @ConstructorProperties({"Name", "Position"})
    public GameObject(String Name, @NotNull Vector2f Position){
        this.Name = Name;
        Transform.Position.x = Position.x;
        Transform.Position.y = Position.y;
        Transform.Position.z = 0f;

        EnableComponents();
    }

    @ConstructorProperties({"Name", "Position", "Scale"})
    public GameObject(String Name, @NotNull Vector3f Position, Vector3f Scale){
        this(Name, Position);
        Transform.Scale = Scale;
    }

    @ConstructorProperties({"Name", "Position", "Scale"})
    public GameObject(String Name, @NotNull Vector2f Position, Vector3f Scale){
        this(Name, Position);
        Transform.Scale = Scale;
    }

    private void EnableComponents(){
        IsActive = true;
        for(Component Comp : Components){
            Comp.Start();
        }
    }

    private void DisableComponents(){
        IsActive = false;
        for(Component Comp : Components){
            Comp.Stop();
        }
    }

    public void SetActive(boolean Toggle){
        IsActive = Toggle;

        if (IsActive){
            EnableComponents();
            return;
        }
        DisableComponents();
    }

    public boolean IsActive(){
        return IsActive;
    }

    public void SetName(String Name){
        this.Name = Name;
    }

    public String GetName(){
        return Name;
    }

    //<editor-fold desc="Component operations">
    public <T extends  Component> T GetComponent(Class<T> ComponentClass){
        for(Component Comp : Components){
            if(ComponentClass.isAssignableFrom(Comp.getClass())){
                try{
                    return ComponentClass.cast(Comp);
                }
                catch(ClassCastException e){
                    e.printStackTrace();
                    assert false : "ERROR (Component) -> Casting Component";
                }
            }
        }

        return null;
    }

    public <T extends Component> void RemoveComponent(Class<T> ComponentClass){
        for(int i = 0; i < Components.size(); i++){
            Component Comp = Components.get(i);
            if(ComponentClass.isAssignableFrom(Comp.getClass())){
                Components.remove(i);
                return;
            }
        }
    }

    public void AddComponent(Component Comp){
        Components.add(Comp);
        Comp.GameObject = this;
    }
    //</editor-fold>

    public void Update() {
        if(!IsActive){
            return;
        }

        for(Component Comp : Components){
            Comp.Update();
        }
    }

}
