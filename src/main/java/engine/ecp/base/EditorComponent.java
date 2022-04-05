package engine.ecp.base;

import commons.imgui.ImGuiUtility;
import engine.Engine;

public class EditorComponent<T> extends ImGUIComponent {
    private T ref;
    private String windowName = "";

    @Override
    public void render() {
        ImGuiUtility.drawDefaultFields(ref, windowName);
    }

    public boolean attach(T newRef){
        if(Engine.getEngine().isState(Engine.State.PLAY_MODE)){
            return false;
        }

        ref = newRef;
        return true;
    }

    public void setWindowName(String name){
        windowName = name;
    }
}
