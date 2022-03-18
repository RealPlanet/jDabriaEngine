package jDabria.ECP.components.UI;

import jDabria.ECP.Component;
import jDabria.events.imGUI.IImGUIStartFrame;
import jDabria.imGUI.ImGUILayer;

public class ImGUIComponent extends Component {
    private transient final IImGUIStartFrame renderEvent = () -> render();

    //region Base class overrides
    @Override
    public void start(){
        ImGUILayer.addStartFrameListener(renderEvent);
    }

    @Override
    public void stop(){
        ImGUILayer.removeStartFrameListener(renderEvent);
    }
    //endregion

    /**
     * Override to implement custom rendering
     */
    protected void render(){}
}
