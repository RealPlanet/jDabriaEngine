package JDabria.ECP;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private String Name;
    private final List<Component> Components = new ArrayList<>();
    private boolean IsActive = false;

    public GameObject(String Name){
        this.Name = Name;

        EnableComponents();
    }

    private void EnableComponents(){
        for(Component Comp : Components){
            Comp.Start();
        }
    }

    private void DisableComponents(){
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
