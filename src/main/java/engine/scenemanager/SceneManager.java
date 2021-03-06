package engine.scenemanager;

import commons.util.StringUtils;
import commons.util.logging.EngineLogger;
import engine.Window;
import engine.assetmanager.AssetPool;
import engine.ecp.components.Camera;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 *  Handles all scene operations such as switching/loading/unloading/get/set.
 */
public class SceneManager {
    private static final ArrayList<Scene> gameScenes = new ArrayList<>();
    private static final String SCENE_PACKAGE_PREFIX = "scenes.";
    public static boolean changingScene = false;

    /**
     * Specifies the loading operation type for a specific scene
     */
    public enum LoadType{
        ADDITIVE,   // Load scene on top of already loaded scenes
        SINGLE      // Dump ALL loaded scenes and after load this scene
    }

    //region Scene operations
    /**
     * Changes current Scene to a new one using Java reflection
     * @param sceneName Scene class name to load
     */
    public static void loadScene(String sceneName, LoadType type) {
        loadScene(sceneName, type, false);
    }

    public static void loadScene(String sceneName, LoadType loadType, boolean checkFile){
        // Unload all scenes if single load mode
        if(loadType == LoadType.SINGLE){
            for(int i = gameScenes.size() - 1; i >= 0; i--){
                UnloadScene(gameScenes.get(i));
            }
        }

        Scene SceneToLoad;
        SceneToLoad = AssetPool.getScene(sceneName);

        //region Scene loading
        changingScene = true;

        SceneToLoad.sceneIndex = gameScenes.size();

        SceneToLoad.init();

        //region Register scene events
        // Once scene is loaded we assign events
        Window.addUpdateFrameListener(SceneToLoad);
        //endregion

        gameScenes.add(SceneToLoad);

        changingScene = false;
        //endregion

        SceneToLoad.start();
    }

    /**
     * Unloads the currently loaded scenes and removes registered events
     */
    public static void UnloadScene(Scene SceneToUnload){
        if(!gameScenes.contains(SceneToUnload)){
            return;
        }

        Window.removeUpdateFrameListener(SceneToUnload);
        SceneToUnload.unload();
        gameScenes.remove(SceneToUnload);
    }
    //endregion

    //region Getters

    /**
     * Returns the player camera, can be null if none is found
     * @return Camera Object
     */
    public static @Nullable Camera getActiveCamera(){
        int cameraCount = 0;
        Camera activeCam = null;
        for (Scene ActiveScene: gameScenes) {
           if(ActiveScene.sceneCamera != null){
               activeCam = ActiveScene.sceneCamera.getComponent(Camera.class);
               cameraCount++;
           }
        }

        if(cameraCount > 1){
            EngineLogger.logWarning(StringUtils.format("Multiple cameras detected [{0}] using latest found", cameraCount));
        }

        return activeCam;
    }

    /**
     * Searches for a specific scene in the loaded scene array by name
     * @param SceneName Scene class name
     * @return Scene object
     */
    public static @Nullable Scene GetActiveScene(@NotNull String SceneName){
        int NameHash = SceneName.hashCode();
        for (Scene ActiveScene: gameScenes) {
            if(ActiveScene.getClass().getName().hashCode() == NameHash){
                return ActiveScene;
            }
        }

        return null;
    }

    @Contract(" -> new")
    public static @NotNull ArrayList<Scene> getActiveScenes(){
        return new ArrayList<>(gameScenes);
    }
    //endregion
}
