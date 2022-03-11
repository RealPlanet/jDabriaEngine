package JDabria.SceneManager;

import JDabria.Renderer.Batcher.Renderer;
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
    protected List<GameObject> _GameObjects = new ArrayList<>();

    @Nullable
    protected Camera _Camera; // Stores the location of the player camera, can be null if multiple scenes are active
    protected int _SceneActiveIndex = -1; //Store the position in the SceneManager array list of active scenes
    protected Renderer _Renderer = new Renderer();

    public Scene(){

    }

    /**
     * Called once on scene load
     */
    public abstract void Init();

    public void Start(){
        IsStarted = true;

        for(GameObject go: _GameObjects) {
            go.SetActive(true);
            _Renderer.Add(go);
        }
    }

    public void AddGameObjectToScene(GameObject go){
        _GameObjects.add(go);

        if(!IsStarted){
            go.SetActive(false);
            return;
        }

        go.SetActive(true);
        _Renderer.Add(go);
    }

    @Override
    public void OnFrameUpdate() {
        for ( GameObject go : _GameObjects) {
            go.Update();
        }

        Update();
    }

    protected abstract void Update();
}
