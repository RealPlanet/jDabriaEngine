package jDabria.sceneManager;

import jDabria.ECP.GameObject;
import jDabria.events.window.IUpdateFrameListener;
import jDabria.renderer.Camera;
import jDabria.renderer.batcher.Renderer;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

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
    private final List<GameObject> gameObjects = new ArrayList<>();

    @Nullable
    protected Camera sceneCamera = new Camera(new Vector3f(0,0,0)); // Stores the location of the player camera, can be null if multiple scenes are active
    protected int sceneIndex = -1; //Store the position in the SceneManager array list of active scenes
    protected Renderer sceneRenderer = new Renderer();

    public Scene(){

    }

    //<editor-fold desc="Abstract methods">
    /**
     * Called once on scene load
     */
    protected abstract void onInit();

    protected abstract void onUpdate();

    protected abstract void onUnload();

    protected abstract void loadResources();
    //</editor-fold>

    //<editor-fold desc="Public methods">
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
        for (GameObject go: gameObjects) {
            go.setActive(false);
            go.delete();
        }

        gameObjects.clear();
        sceneRenderer.clear();
        sceneRenderer = new Renderer();
    }

    public List<GameObject> getGameObjects(){
        return new ArrayList<>(gameObjects);
    }

    @Override
    public void onFrameUpdate() {
        for ( GameObject go : gameObjects) {
            go.update();
        }

        onUpdate();
    }
    //</editor-fold>
}
