package jDabria.ECP.components.ui;

import jDabria.ECP.base.Component;
import jDabria.events.imGUI.IImGUIStartFrame;
import jDabria.imGUI.ImGUILayer;

public class ImGUIComponent extends Component {
    private transient final IImGUIStartFrame renderEvent = this::internalRender;
    protected transient boolean useDefaultDrawer = false;

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

    private void internalRender(){
        render();
    }

    /**
     * Override to implement custom rendering
     */
    protected void render(){}
}
