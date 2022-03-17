package JDabria.AssetManager;

import JAssets.Scenes.Core.EmptyScene;
import JDabria.AssetManager.Resources.ShaderBuilder;
import JDabria.AssetManager.Resources.Texture;
import JDabria.Renderer.Sprite.SpriteSheet;
import JDabria.SceneManager.Scene;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    private static final Map<String, ShaderBuilder> engineShaders = new HashMap<>();
    private static final Map<String, Texture> engineTextures = new HashMap<>();
    private static final Map<String, Scene> engineScenes = new HashMap<>();
    private static final Map<String, SpriteSheet> engineSpriteSheet = new HashMap<>();

    // Prefix for assets which are compiled and included as a package
    private static final String ASSET_PACKAGE_PREFIX = "JAssets.";
    public static final String DEFAULT_FALLBACK_SHADER = "Assets/Shaders/DefaultShaderDefinition.glsl";

    /**
     *
     * @param resourceName
     * @return
     */
    public static ShaderBuilder getShader(String resourceName){
        File file = new File(resourceName);
        if(engineShaders.containsKey(file.getAbsolutePath())){
            return engineShaders.get(file.getAbsolutePath());
        }

        ShaderBuilder shader = new ShaderBuilder(resourceName);
        shader.compile();
        engineShaders.put(file.getAbsolutePath(), shader);
        return shader;
    }

    /**
     *
     * @param resourceName
     * @return
     */
    public static Texture getTexture(String resourceName){
        File file = new File(resourceName);
        if(engineTextures.containsKey(file.getAbsolutePath())){
            return engineTextures.get(file.getAbsolutePath());
        }

        Texture texture = new Texture(resourceName);
        engineTextures.put(file.getAbsolutePath(), texture);
        return texture;
    }

    public static void addSpriteSheet(String resourceName, SpriteSheet sheet){
        File file = new File(resourceName);
        if(!engineSpriteSheet.containsKey(file.getAbsolutePath())){
            engineSpriteSheet.put(file.getAbsolutePath(), sheet);
        }
    }

    public static SpriteSheet getSpriteSheet(String resourceName){
        File file = new File(resourceName);
        if(!engineSpriteSheet.containsKey(file.getAbsolutePath())){
            assert false : "ERROR (AssetPool) --> Tried to access sprite-sheet '" + resourceName + "' and it has not been added to asset pool";
        }

        return engineSpriteSheet.getOrDefault(file.getAbsolutePath(), null);
    }

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
