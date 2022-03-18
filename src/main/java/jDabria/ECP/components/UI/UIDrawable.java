package jDabria.ECP.components.UI;

import jDabria.ECP.Component;
import jDabria.events.imGUI.IImGUIDrawInspectable;
import jDabria.events.imGUI.IImGUIStartFrame;
import jDabria.imGUI.ImGUILayer;

/**
 * Component which allows for UI rendering using ImGUI. The onUpdate loop is not the same as standard components. This component relies on the ImGUI onUpdate loop
 */
public class UIDrawable extends Component {

    protected IImGUIDrawInspectable drawable = null;
    protected IImGUIStartFrame renderEvent = () -> renderUI();

    @Override
    public void start(){
        ImGUILayer.addStartFrameListener(renderEvent);
    }

    @Override
    public void stop(){
        ImGUILayer.removeStartFrameListener(renderEvent);
    }

    protected void renderUI(){
        if(drawable != null){
            drawable.draw();
        }
    }

    public void setDrawable(IImGUIDrawInspectable drawable){
        this.drawable = drawable;
    }
}
