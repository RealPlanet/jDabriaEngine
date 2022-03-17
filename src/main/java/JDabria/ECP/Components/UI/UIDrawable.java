package JDabria.ECP.Components.UI;

import JDabria.ECP.Component;
import JDabria.Events.ImGUI.IImGUIDrawInspectable;
import JDabria.Events.ImGUI.IImGUIStartFrame;
import JDabria.ImGUI.ImGUILayer;

/**
 * Component which allows for UI rendering using ImGUI. The onUpdate loop is not the same as standard components. This component relies on the ImGUI onUpdate loop
 */
public class UIDrawable extends Component {

    protected IImGUIStartFrame renderEvent = new IImGUIStartFrame(){
            @Override
            public void render(){
                renderUI();
            }
    };
    protected IImGUIDrawInspectable drawable = null;

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
