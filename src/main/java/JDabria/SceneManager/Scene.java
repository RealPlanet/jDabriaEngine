package JDabria.SceneManager;

import JDabria.Events.Window.IUpdateFrameListener;

/**
 * Abstract definition of a scene
 * TODO :: Should implement OnUnload Event to allow for unload functionality extension
 */
public abstract class Scene implements IUpdateFrameListener {
    public boolean IsLoaded = false;

    public Scene(){

    }

    /**
     * Called once on scene load
     */
    public abstract void Init();
}
