package JDabria.AssetManager;

import JDabria.SceneManager.Scene;
import JAssets.Scenes.Core.EmptyScene;

public class AssetManager {

    // Prefix for assets which are compiled and included as a package
    private static final String ASSET_PACKAGE_PREFIX = "JAssets.";

    //<editor-fold desc="Singleton">
    private static AssetManager _Instance;
    private static AssetManager GetAssetManager(){
        if(_Instance == null){
            _Instance = new AssetManager();
        }

        return _Instance;
    }
    private AssetManager(){

    }
    //</editor-fold>

    //<editor-fold desc="Scene assets methods">
    public static Scene GetScene(String ScenePackagePath, String SceneName) throws ClassNotFoundException{
        Class<?> SceneClass;
        Scene Result = new EmptyScene();

        try {
            SceneClass = Class.forName(ASSET_PACKAGE_PREFIX + ScenePackagePath + SceneName);
            Scene LoadedScene = (Scene)(SceneClass.getDeclaredConstructor().newInstance());
            Result = LoadedScene;   //Now overwrite the empty scene
        }
        catch (Exception ex) {
            System.err.println("Scene definition requested: " + ASSET_PACKAGE_PREFIX + ScenePackagePath + SceneName);
            ex.printStackTrace();
        }
        return Result;
    }
    //</editor-fold>
}
