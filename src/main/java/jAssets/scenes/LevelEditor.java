package jAssets.scenes;

import commons.Color;
import jDabria.ECP.GameObject;
import jDabria.ECP.components.Sprite.SpriteRenderer;
import jDabria.ECP.components.UI.Pieces.UIColorPicker;
import jDabria.ECP.components.UI.Pieces.UITextBox;
import jDabria.assetManager.AssetPool;
import jDabria.renderer.Camera;
import jDabria.renderer.sprite.Sprite;
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

        UIColorPicker cp = new UIColorPicker();

        GameObject bepu_love = new GameObject("bepu", new Vector3f(150, 300, 1), new Vector3f(1,1,1));
        SpriteRenderer bepu_spr = new SpriteRenderer(new Sprite(AssetPool.getTexture("Assets/Textures/bepu.png")));
        bepu_spr.getSprite().setSize(128, 128);
        bepu_love.addComponent(bepu_spr);
        bepu_love.addComponent(cp);
        addGameObjectToScene(bepu_love);

        GameObject testIMGUI = new GameObject("Test");
        testIMGUI.setPosition( new Vector3f(100, 100, 1) );
        addGameObjectToScene(testIMGUI);

        UITextBox io = new UITextBox().setContent("Test").setTitle("Hi");

        SpriteRenderer spr = new SpriteRenderer(Color.BLACK);

        cp = new UIColorPicker();
        SpriteRenderer spr1 = new SpriteRenderer(Color.BLACK);

        spr.getSprite().setSize(100, 100);
        spr1.getSprite().setSize(100, 100);

        testIMGUI.addComponent(io);
        testIMGUI.addComponent(cp);
        testIMGUI.addComponent(spr);

        GameObject deez = new GameObject("Deez");
        deez.setPosition(new Vector3f(200, 200, 1));
        addGameObjectToScene(deez);
        cp = new UIColorPicker();
        deez.addComponent(cp);
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
