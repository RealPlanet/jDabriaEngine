package JDabria.SceneManager;

import Assets.Scenes.EmptyScene;
import JDabria.Window;

/**
 *  Handles all scene operations such as switching/loading/unloading/get/set.
 *  TODO :: Allows for additive scenes
 */
public class SceneManager {
    private Scene LoadedScene = null;
    private final String ScenePackageName = "Assets.Scenes.";
    public static boolean ChangingScene = false;

    public enum LoadType{
        ADDITIVE,
        SINGLE
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
            SceneToLoad = GetSceneByName(SceneName);
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

    private static Scene GetSceneByName(String SceneName) throws ClassNotFoundException{
        SceneManager Manager = Get();

        Class<?> SceneClass;

        SceneClass = Class.forName(Manager.ScenePackageName+SceneName);

        Scene Result = new EmptyScene();

        try {
            Scene LoadedScene = (Scene)(SceneClass.getDeclaredConstructor().newInstance());
            Result = LoadedScene;   //Now overwrite the empty scene
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return Result;
    }
}
