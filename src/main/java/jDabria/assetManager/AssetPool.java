package jDabria.assetManager;

import commons.StringUtils;
import commons.logging.EngineLogger;
import jDabria.assetManager.resources.ShaderBuilder;
import jDabria.assetManager.resources.Texture;
import jDabria.renderer.sprite.SpriteSheet;
import jDabria.sceneManager.Scene;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    //region Engine asset pools
    private transient static final Map<String, ShaderBuilder> engineShaders = new HashMap<>();
    private transient static final Map<String, Texture> engineTextures = new HashMap<>();
    private transient static final Map<String, Constructor<?>> engineScenes = new HashMap<>();
    private transient static final Map<String, SpriteSheet> engineSpriteSheet = new HashMap<>();
    //endregion

    static{
        AssetPool.getShader(AssetPool.DEFAULT_FALLBACK_SHADER);
    }

    // Prefix for assets which are compiled and included as a package
    private static final String ASSET_PACKAGE_PREFIX = "jAssets.";
    public static final String DEFAULT_FALLBACK_SHADER = "assets/shaders/defShader.glsl";

    /**
     * Looks up a shader in the engine asset pools, if not found a new one is created, added and returned.
     * Shaders are compiled at runtime with this!
     * @param resourceName the shader full path from the current working directory
     * @return The compiled shader
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
     * Looks up a texture in the engine asset pools, if not found a new one is created, added and returned.
     * Textures are streamed in if not found!
     * @param resourceName the texture full path from the current working directory, including the file extension.
     * @return The loaded texture
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

    /**
     * Sprite sheets need to created manually, the constructor takes care of registering one to the asset pool.
     * @param resourceName Spritesheet's parent texture full path from the current working directory, including the file extension.
     * @param sheet The sprite sheet to add
     */
    public static void addSpriteSheet(String resourceName, SpriteSheet sheet){
        File file = new File(resourceName);
        if(!engineSpriteSheet.containsKey(file.getAbsolutePath())){
            engineSpriteSheet.put(file.getAbsolutePath(), sheet);
        }
    }

    /**
     * Looks up a sprite sheet in the engine asset pools, if not found an exception is thrown
     * @param resourceName Spritesheet's parent texture full path from the current working directory, including the file extension.
     * @return The loaded sprite sheet
     */
    public static SpriteSheet getSpriteSheet(String resourceName){
        File file = new File(resourceName);

        if (!engineSpriteSheet.containsKey(file.getAbsolutePath())){
            String ErrorMessage = StringUtils.format("Tried to access sprite-sheet '{0}' but it has not been added to asset pool", resourceName);
            EngineLogger.logError(ErrorMessage);
            throw new AssertionError(ErrorMessage);
        }

        return engineSpriteSheet.getOrDefault(file.getAbsolutePath(), null);
    }

    //<editor-fold desc="Scene assets methods">

    /**
     * Gets a new instance of a scene
     * @param sceneName The scene full canonical name.
     * @return A new instance of the scene, created using reflection.
     */
    public static Scene getScene(String sceneName){
        try {

            // Avoid reflection if possible
            if(engineScenes.containsKey(sceneName)){
                return (Scene)engineScenes.get(sceneName).newInstance();
            }

            Class<?> sceneClass = Class.forName(sceneName);
            Constructor<?> sceneConstructor = sceneClass.getDeclaredConstructor();
            engineScenes.put(sceneName, sceneConstructor);
            return (Scene)(sceneConstructor.newInstance());   //Now overwrite the empty scene
        }
        catch (Exception ex) {
            EngineLogger.logError(StringUtils.format("Could not find Scene class for '{0}' with exception {1}", sceneName, ex));
        }

        return null;
    }
    //</editor-fold>
}
