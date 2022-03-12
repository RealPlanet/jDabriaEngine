package JDabria.SceneManager;

import JDabria.AssetManager.AssetPool;
import JDabria.Renderer.Camera;
import JDabria.Window;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 *  Handles all scene operations such as switching/loading/unloading/get/set.
 */
public class SceneManager {
    private static final ArrayList<Scene> GameScenes = new ArrayList<>();
    private static final String SCENE_PACKAGE_PREFIX = "Scenes.";
    public static boolean ChangingScene = false;

    public enum LoadType{
        ADDITIVE,   // Load scene on top of already loaded scenes
        SINGLE      // Dump ALL loaded scenes and after load this scene
    }

    //<editor-fold desc="Singleton">
    /*
    private static SceneManager _Instance = null;
    private SceneManager(){

    }

    private static SceneManager get(){
        if(_Instance == null){
            _Instance = new SceneManager();
        }

        return _Instance;
    }

 */
    //</editor-fold>

    //<editor-fold desc="Scene operations">
    /**
     * Changes current Scene to a new one using Java reflection
     * @param SceneName Scene class name to load
     */
    public static void loadScene(String SceneName, LoadType Type) {
        //SceneManager Manager = get();

        // Unload all scenes if single load mode
        if(Type == LoadType.SINGLE){
            for(int i = GameScenes.size() - 1; i >= 0; i--){
                UnloadScene(GameScenes.get(i));
            }
        }

        Scene SceneToLoad;
        try {
            SceneToLoad = AssetPool.getScene(SCENE_PACKAGE_PREFIX, SceneName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        //<editor-fold desc="Scene loading">
        ChangingScene = true;

        SceneToLoad.isLoaded = false;
        SceneToLoad.sceneIndex = GameScenes.size();
        SceneToLoad.init();

        /*   TODO :: Handle scene loading   */

        //<editor-fold desc="Register scene events">
        // Once scene is loaded we assign events
        Window.addUpdateFrameListener(SceneToLoad);
        //</editor-fold>

        SceneToLoad.isLoaded = true;
        GameScenes.add(SceneToLoad);
        ChangingScene = false;
        //</editor-fold>

        SceneToLoad.start();
    }

    /**
     * Unloads the currently loaded scenes and removes registered events
     */
    public static void UnloadScene(Scene SceneToUnload){
        if(!GameScenes.contains(SceneToUnload)){
            return;
        }
        Window.removeUpdateFrameListener(SceneToUnload);
        GameScenes.remove(SceneToUnload);
    }
    //</editor-fold>

    //<editor-fold desc="Getters">

    /**
     * Returns the player camera, can be null if none is found
     * @return Camera Object
     */
    public static @Nullable Camera getActiveCamera(){
        for (Scene ActiveScene: GameScenes ) {
           if(ActiveScene.sceneCamera != null){
               return ActiveScene.sceneCamera;
           }
        }

        return null;
    }

    /**
     * Searches for a specific scene in the loaded scene array by name
     * @param SceneName Scene class name
     * @return Scene object
     */
    public static @Nullable Scene GetActiveScene(@NotNull String SceneName){
        int NameHash = SceneName.hashCode();
        for (Scene ActiveScene: GameScenes ) {
            if(ActiveScene.getClass().getName().hashCode() == NameHash){
                return ActiveScene;
            }
        }

        return null;
    }
    //</editor-fold>
}
