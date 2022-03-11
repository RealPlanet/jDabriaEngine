package JDabria.SceneManager;

import JDabria.ECP.GameObject;
import JDabria.Events.Window.IUpdateFrameListener;
import JDabria.Renderer.Camera;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract definition of a scene
 * TODO :: Should implement OnUnload Event to allow for unload functionality extension
 */
public abstract class Scene implements IUpdateFrameListener {
    public boolean IsLoaded = false;
    protected boolean IsStarted = false;
    protected List<GameObject> GameObjects = new ArrayList<>();

    @Nullable
    protected Camera Camera; // Stores the location of the player camera, can be null if multiple scenes are active
    protected int SceneActiveIndex = -1; //Store the position in the SceneManager array list of active scenes

    public Scene(){

    }

    /**
     * Called once on scene load
     */
    public abstract void Init();

    public void Start(){
        IsStarted = true;

        for(GameObject go: GameObjects) {
            go.SetActive(true);
        }
    }

    public void AddGameObjectToScene(GameObject go){
        GameObjects.add(go);
        if(!IsStarted){
            go.SetActive(false);
            return;
        }

        go.SetActive(true);
    }
}
