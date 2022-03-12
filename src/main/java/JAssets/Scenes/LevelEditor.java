package JAssets.Scenes;

import JDabria.AssetManager.AssetPool;
import JDabria.ECP.Components.Sprite.Sprite;
import JDabria.ECP.Components.Sprite.SpriteRenderer;
import JDabria.ECP.Components.Sprite.SpriteSheet;
import JDabria.ECP.GameObject;
import JDabria.Renderer.Camera;
import JDabria.SceneManager.Scene;
import org.joml.Vector3f;

public class LevelEditor extends Scene {

    public LevelEditor(){
        System.out.println("Inside Level editor!");
    }

    @Override
    public void onInit() {
        sceneCamera = new Camera();
        SpriteSheet TestSprites = AssetPool.getSpriteSheet("Assets/Textures/SpriteSheetTest.png");

        GameObject obj_1 = new GameObject("pepu_test");
        obj_1.transform.position = new Vector3f(100, 100, 0);
        obj_1.transform.scale = new Vector3f(0.5f, 0.5f, 0);
        obj_1.addComponent(
                new SpriteRenderer(
                        new Sprite(AssetPool.getTexture("Assets/Textures/bepu.png"))));

        addGameObjectToScene(obj_1);


        GameObject obj_2 = new GameObject("pepu_test");
        obj_2.transform.position = new Vector3f(400, 100, 0);
        obj_2.transform.scale = new Vector3f(10f, 10f, 0);
        obj_2.addComponent(
                new SpriteRenderer(
                        TestSprites.getSprite(0)));

        addGameObjectToScene(obj_2);

        GameObject obj_3 = new GameObject("pepu_test");
        obj_3.transform.position = new Vector3f(600, 100, 0);
        obj_3.transform.scale = new Vector3f(10f, 10f, 0);
        obj_3.addComponent(
                new SpriteRenderer(
                        TestSprites.getSprite(10)));

        addGameObjectToScene(obj_3);
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
