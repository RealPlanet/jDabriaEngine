package JDabria.AssetManager;

import JDabria.SceneManager.Scene;
import JAssets.Scenes.Core.EmptyScene;

public class AssetManager {

    // Prefix for assets which are compiled and included as a package
    private static final String ASSET_PACKAGE_PREFIX = "JAssets.";

    //<editor-fold desc="Singleton">
    private static AssetManager instance;
    private static AssetManager getAssetManager(){
        if(instance == null){
            instance = new AssetManager();
        }

        return instance;
    }
    private AssetManager(){

    }
    //</editor-fold>

    //<editor-fold desc="Scene assets methods">
    public static Scene getScene(String scenePackagePath, String sceneName) throws ClassNotFoundException{
        Class<?> sceneClass;
        Scene result = new EmptyScene();

        try {
            sceneClass = Class.forName(ASSET_PACKAGE_PREFIX + scenePackagePath + sceneName);
            Scene scene = (Scene)(sceneClass.getDeclaredConstructor().newInstance());
            result = scene;   //Now overwrite the empty scene
        }
        catch (Exception ex) {
            System.err.println("Scene definition requested: " + ASSET_PACKAGE_PREFIX + scenePackagePath + sceneName);
            ex.printStackTrace();
        }
        return result;
    }
    //</editor-fold>
}
