package JDabria.SceneManager;

import JDabria.Events.OnNewFrame;
import JDabria.Window;

/**
 * Abstract definition of a scene
 * TODO :: Should implement OnUnload Event to allow for unload functionality extension
 */
public abstract class Scene implements OnNewFrame {
    public boolean IsLoaded = false;

    public Scene(){
        Window.AddNewFrameListener(this);
    }

    /**
     * Called once on scene load
     */
    public abstract void Init();

    public final void Unload(){
        Window.RemoveNewFrameListener(this);
    }
}
