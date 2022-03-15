package JAssets.Scenes;

import Commons.Color;
import JDabria.AssetManager.AssetPool;
import JDabria.ECP.Components.Sprite.Sprite;
import JDabria.ECP.Components.Sprite.SpriteRenderer;
import JDabria.ECP.Components.Sprite.SpriteSheet;
import JDabria.ECP.GameObject;
import JDabria.Renderer.Camera;
import JDabria.SceneManager.Scene;
import JDabria.Window;
import org.joml.Vector3f;

public class LevelEditor extends Scene {

    public LevelEditor(){
        System.out.println("Inside Level editor!");
    }

    private GameObject obj_2;
    private GameObject obj_1;
    private  SpriteSheet TestSpriteSheet;

    @Override
    public void onInit() {
        sceneCamera = new Camera();
        Window.setWindowClearColor(new Color(1, 1, 1, 1));

        TestSpriteSheet = AssetPool.getSpriteSheet("Assets/Textures/SpriteSheetTest.png");

        // Green
        obj_2 = new GameObject("obj_2");
        obj_2.transform.position = new Vector3f(400, 100, 4);
        obj_2.transform.scale = new Vector3f(1f, 1f, 0);
        obj_2.addComponent(
                new SpriteRenderer(
                        new Sprite(
                                AssetPool.getTexture("Assets/Textures/blendImage2.png"))));

        addGameObjectToScene(obj_2);

        obj_1 = new GameObject("obj_1");
        obj_1.transform.position = new Vector3f(200, 100, 2);
        obj_1.transform.scale = new Vector3f(1f, 1f, 0);
        obj_1.addComponent(
                new SpriteRenderer(
                        new Sprite(
                                AssetPool.getTexture("Assets/Textures/blendImage1.png"))));

        addGameObjectToScene(obj_1);


    }

    @Override
    protected void update() {
        sceneRenderer.Render();
    }

    @Override
    protected void loadResources() {
        AssetPool.getShader(AssetPool.DEFAULT_FALLBACK_SHADER);

        String SpriteSheetTestPath = "Assets/Textures/SpriteSheetTest.png";
        AssetPool.addSpriteSheet(SpriteSheetTestPath,
                new SpriteSheet(AssetPool.getTexture(SpriteSheetTestPath), 16, 16, 26, 0));
    }
}
