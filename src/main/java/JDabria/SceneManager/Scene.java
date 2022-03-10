package JDabria.SceneManager;

import JDabria.Events.Window.IUpdateFrameListener;
import JDabria.Renderer.Camera;

/**
 * Abstract definition of a scene
 * TODO :: Should implement OnUnload Event to allow for unload functionality extension
 */
public abstract class Scene implements IUpdateFrameListener {
    public boolean IsLoaded = false;
    protected Camera Camera;

    public Scene(){

    }

    /**
     * Called once on scene load
     */
    public abstract void Init();
}
