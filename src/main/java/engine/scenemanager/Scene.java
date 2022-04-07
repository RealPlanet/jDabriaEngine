package engine.scenemanager;

import engine.ecp.GameObject;
import engine.events.window.IUpdateFrameListener;
import engine.renderer.batcher.Renderer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract definition of a scene
 * TODO :: Should implement OnUnload Event to allow for unload functionality extension
 */
@SuppressWarnings("EmptyMethod")
public abstract class Scene implements IUpdateFrameListener {
    public boolean isLoaded = false;
    protected boolean isStarted = false;

    // Hidden from scenes
    private final List<GameObject> gameObjects = new ArrayList<>();

    @Nullable
    // FIXME :: Each scene should handle the camera as it wants
    protected GameObject sceneCamera = null;// Stores the location of the player camera, can be null if multiple scenes are active
    protected int sceneIndex = -1; //Store the position in the SceneManager array list of active scenes
    protected Renderer sceneRenderer = new Renderer();

    public Scene(){

    }

    //region Abstract methods
    /**
     * Called once on scene load
     */

    protected abstract void onInit();

    protected abstract void onUpdate();

    protected abstract void onUnload();

    protected abstract void onClear();

    protected abstract void loadResources();
    //endregion

    //region Public methods
    public void init(){
        loadResources();

        if(isLoaded){
            return;
        }

        onInit();
        isLoaded = true;
    }

    public void start(){
        isStarted = true;
        for(GameObject go: gameObjects) {
            go.setActive(false);
            go.setActive(true);
            sceneRenderer.add(go);
        }
    }

    public void unload(){
        for(GameObject go: gameObjects) {
            go.setActive(false);
        }
        onUnload();
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

    public void clearScene(){
        onClear();

        for (GameObject go: gameObjects) {
            go.setActive(false);
            go.delete();
        }

        gameObjects.clear();
        sceneRenderer.clear();
        sceneRenderer = new Renderer();
    }

    @Contract(" -> new")
    public ArrayList<GameObject> getGameObjects(){
        return new ArrayList<>(gameObjects);
    }

    public GameObject findGameObjectByName(String name){ return gameObjects.stream().filter(obj -> obj.getName() == name).findFirst().get();}

    @Override
    public void onFrameUpdate() {
        for ( GameObject go : gameObjects) {
            go.update();
        }

        onUpdate();
    }
    //endregion
}
