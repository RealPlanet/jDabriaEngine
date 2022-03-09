package JDabria.SceneManager;

import JDabria.AssetManager.AssetManager;
import JDabria.Window;

/**
 *  Handles all scene operations such as switching/loading/unloading/get/set.
 *  TODO :: Allows for additive scenes
 */
public class SceneManager {
    private Scene LoadedScene = null;
    private static final String SCENE_PACKAGE_PREFIX = "Scenes.";
    public static boolean ChangingScene = false;

    public enum LoadType{
        ADDITIVE,   // Load scene on top of already loaded scenes
        SINGLE      // Dump ALL loaded scenes and after load this scene
    }

    //<editor-fold desc="Singleton">
    private static SceneManager _Instance = null;
    private SceneManager(){

    }

    private static SceneManager Get(){
        if(_Instance == null){
            _Instance = new SceneManager();
        }

        return _Instance;
    }
    //</editor-fold>

    /**
     * Changes current Scene to a new one using Java reflection
     * @param SceneName Scene class name to load
     */
    public static void ChangeScene(String SceneName) {
        SceneManager Manager = Get();

        Scene SceneToLoad;
        try {
            SceneToLoad = AssetManager.GetScene(SCENE_PACKAGE_PREFIX, SceneName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        //<editor-fold desc="Scene loading">
        ChangingScene = true;
        SceneToLoad.IsLoaded = false;

        // If present unload current scene
        UnloadCurrentScene();
        SceneToLoad.Init();

        /*   TODO :: Handle scene loading   */

        //<editor-fold desc="Register scene events">
        // Once scene is loaded we assign events
        Window.AddNewFrameListener(SceneToLoad);
        //</editor-fold>

        SceneToLoad.IsLoaded = true;
        ChangingScene = false;
        Manager.LoadedScene = SceneToLoad;
        //</editor-fold>
    }

    /**
     * Unloads the currently loaded scenes and removes registered events
     */
    public static void UnloadCurrentScene(){
        SceneManager Manager = Get();

        if(Manager.LoadedScene == null){
            return;
        }

        Window.RemoveNewFrameListener(Manager.LoadedScene);
        Manager.LoadedScene = null;
    }

    public static Scene GetCurrentScene(){
        return Get().LoadedScene;
    }

}
