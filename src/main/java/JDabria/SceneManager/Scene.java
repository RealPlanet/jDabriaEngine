package JDabria.SceneManager;

import JDabria.Events.Window.IUpdateFrameListener;
import JDabria.Renderer.Camera;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract definition of a scene
 * TODO :: Should implement OnUnload Event to allow for unload functionality extension
 */
public abstract class Scene implements IUpdateFrameListener {
    public boolean IsLoaded = false;

    @Nullable
    protected Camera Camera; // Stores the location of the player camera, can be null if multiple scenes are active
    protected int SceneActiveIndex = -1; //Store the position in the SceneManager array list of active scenes

    public Scene(){

    }

    /**
     * Called once on scene load
     */
    public abstract void Init();
}
