package JAssets.Scenes;

import Commons.Color;
import Commons.Time;
import JDabria.AssetManager.AssetPool;
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
    private GameObject obj_3;
    private  SpriteSheet TestSpriteSheet;

    @Override
    public void onInit() {
        sceneCamera = new Camera();
        Window.setWindowClearColor(new Color(0.9f, 0, 0, 1));
        TestSpriteSheet = AssetPool.getSpriteSheet("Assets/Textures/SpriteSheetTest.png");

        obj_2 = new GameObject("mario_0");
        obj_2.transform.position = new Vector3f(400, 100, 10);
        obj_2.transform.scale = new Vector3f(10f, 10f, 0);
        obj_2.addComponent(
                new SpriteRenderer(
                        TestSpriteSheet.getSprite(0)));

        addGameObjectToScene(obj_2);

        obj_3 = new GameObject("mario_1");
        obj_3.transform.position = new Vector3f(600, 100, -10);
        obj_3.transform.scale = new Vector3f(10f, 10f, 0);
        obj_3.addComponent(
                new SpriteRenderer(
                        TestSpriteSheet.getSprite(10)));

        addGameObjectToScene(obj_3);
    }

    private int spriteIndex = 0;
    private float spriteFlipTime = 0.2f;
    private float spriteFLipTimeLeft = 0.0f;

    @Override
    protected void update() {

        spriteFLipTimeLeft -= Time.deltaTime();
        if(spriteFLipTimeLeft <= 0){
            spriteFLipTimeLeft = spriteFlipTime;
            spriteIndex++;
            if(spriteIndex > 4){
                spriteIndex = 0;
            }

            obj_2.getComponent(SpriteRenderer.class).setSprite(TestSpriteSheet.getSprite(spriteIndex));
        }
        obj_2.transform.position.x += 10 * Time.deltaTime();
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
