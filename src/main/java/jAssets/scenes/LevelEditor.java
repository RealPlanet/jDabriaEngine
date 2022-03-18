package jAssets.scenes;

import commons.Color;
import jDabria.ECP.GameObject;
import jDabria.ECP.components.Sprite.SpriteRenderer;
import jDabria.ECP.components.UI.Pieces.UIColorPicker;
import jDabria.ECP.components.UI.Pieces.UITextBox;
import jDabria.assetManager.AssetPool;
import jDabria.renderer.Camera;
import jDabria.renderer.sprite.SpriteSheet;
import jDabria.sceneManager.Scene;
import org.joml.Vector3f;

public class LevelEditor extends Scene {

    public LevelEditor(){
        System.out.println("Inside Level editor!");
    }

    @Override
    public void onInit() {
        sceneCamera = new Camera();
        if(isLoaded){
            return;
        }

        GameObject testIMGUI = new GameObject("Test");
        testIMGUI.transform.position = new Vector3f(100, 100, 1);
        addGameObjectToScene(testIMGUI);

        UITextBox io = new UITextBox().setContent("Test").setTitle("Hi");
        UIColorPicker cp = new UIColorPicker();
        SpriteRenderer spr = new SpriteRenderer(Color.BLACK);

        UIColorPicker cp1 = new UIColorPicker();
        SpriteRenderer spr1 = new SpriteRenderer(Color.BLACK);

        spr.getSprite().setSize(100, 100);
        spr1.getSprite().setSize(100, 100);

        testIMGUI.addComponent(io);
        testIMGUI.addComponent(cp);
        testIMGUI.addComponent(spr);

        GameObject deez = new GameObject("Deez");
        deez.transform.position = new Vector3f(200, 200, 1);
        addGameObjectToScene(deez);

        deez.addComponent(cp1);
        deez.addComponent(spr1);
    }

    @Override
    protected void onUpdate() {
        sceneRenderer.Render();
    }

    @Override
    protected void onUnload() {

    }

    @Override
    protected void onClear() {

    }

    @Override
    protected void loadResources() {
        AssetPool.getShader(AssetPool.DEFAULT_FALLBACK_SHADER);

        String SpriteSheetTestPath = "Assets/Textures/SpriteSheetTest.png";
        AssetPool.addSpriteSheet(SpriteSheetTestPath,
                new SpriteSheet(AssetPool.getTexture(SpriteSheetTestPath), 16, 16, 26, 0));
    }
}
