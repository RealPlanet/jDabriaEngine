package JAssets.Scenes;

import JDabria.AssetManager.AssetPool;
import JDabria.ECP.Components.SpriteRenderer;
import JDabria.ECP.GameObject;
import JDabria.Renderer.Camera;
import JDabria.SceneManager.Scene;
import org.joml.Vector3f;

public class LevelEditor extends Scene {

    public LevelEditor(){
        System.out.println("Inside Level editor!");
    }

    @Override
    public void init() {
        sceneCamera = new Camera();
        GameObject obj_1 = new GameObject("pepu_test");
        obj_1.transform.position = new Vector3f(100, 100, 0);
        obj_1.transform.scale = new Vector3f(256, 256, 0);
        obj_1.addComponent(new SpriteRenderer(AssetPool.getTexture("Assets/Textures/bepu.png")));
        addGameObjectToScene(obj_1);


        GameObject obj_2 = new GameObject("pepu_test");
        obj_2.transform.position = new Vector3f(400, 100, 0);
        obj_2.transform.scale = new Vector3f(256, 256, 0);
        obj_2.addComponent(new SpriteRenderer(AssetPool.getTexture("Assets/Textures/evil_dev.png")));
        addGameObjectToScene(obj_2);
    }

    @Override
    protected void update() {
        sceneRenderer.Render();
    }

    @Override
    protected void loadResources() {
        AssetPool.getShader(AssetPool.DEFAULT_FALLBACK_SHADER);
    }
}
