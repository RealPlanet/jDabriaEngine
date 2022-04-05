package engine.ecp.base;

import engine.events.imGUI.IImGUIStartFrame;
import engine.renderer.imgui.ImGUILayer;

public abstract class ImGUIComponent extends Component {
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
    protected abstract void render();
}
