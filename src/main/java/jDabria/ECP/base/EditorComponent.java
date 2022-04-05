package jDabria.ECP.base;

import commons.imgui.ImGuiUtility;
import jDabria.engine.Engine;
import jDabria.events.imGUI.IImGUIStartFrame;
import jDabria.imGUI.ImGUILayer;

public class EditorComponent<T> extends Component implements IImGUIStartFrame {
    private T ref;
    private String windowName = "";

    @Override
    public void start() {
        ImGUILayer.addStartFrameListener(this);
    }

    @Override
    public void stop() {
        ImGUILayer.removeStartFrameListener(this);
    }

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
