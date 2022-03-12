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
    public boolean isLoaded = false;
    protected boolean isStarted = false;

    // Hidden from scenes
    private List<GameObject> gameObjects = new ArrayList<>();

    @Nullable
    protected Camera sceneCamera; // Stores the location of the player camera, can be null if multiple scenes are active
    protected int sceneIndex = -1; //Store the position in the SceneManager array list of active scenes
    protected Renderer sceneRenderer = new Renderer();

    public Scene(){

    }

    //<editor-fold desc="Abstract methods">
    /**
     * Called once on scene load
     */
    public abstract void init();

    protected abstract void update();

    protected abstract void loadResources();
    //</editor-fold>

    //<editor-fold desc="Public methods">
    public void start(){
        isStarted = true;

        for(GameObject go: gameObjects) {
            go.setActive(true);
            sceneRenderer.add(go);
        }
    }

    public void addGameObjectToScene(GameObject go){
        gameObjects.add(go);

        if(!isStarted){
            go.setActive(false);
            return;
        }

        go.setActive(true);
        sceneRenderer.add(go);
    }

    @Override
    public void onFrameUpdate() {
        for ( GameObject go : gameObjects) {
            go.update();
        }

        update();
    }
    //</editor-fold>
}
