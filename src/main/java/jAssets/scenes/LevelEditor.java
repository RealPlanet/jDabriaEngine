package jAssets.scenes;

import commons.Color;
import jDabria.ECP.GameObject;
import jDabria.ECP.components.Sprite.SpriteRenderer;
import jDabria.ECP.components.UI.Pieces.UIColorPicker;
import jDabria.ECP.components.physics.RigidBody2D;
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

        UIColorPicker cp1 = new UIColorPicker();
        UIColorPicker cp2 = new UIColorPicker();
        UIColorPicker cp3 = new UIColorPicker();
        SpriteRenderer spr = new SpriteRenderer(Color.BLACK);
        SpriteRenderer bepu_spr = new SpriteRenderer(new Sprite(AssetPool.getTexture("Assets/Textures/bepu.png")));
        SpriteRenderer spr1 = new SpriteRenderer(Color.BLACK);

        spr.getSprite().setSize(100, 100);
        spr1.getSprite().setSize(100, 100);
        bepu_spr.getSprite().setSize(128, 128);

        GameObject bepu_love = new GameObject("bepu", new Vector3f(150, 300, 1), new Vector3f(1,1,1));
        GameObject deez = new GameObject("Deez");
        GameObject testIMGUI = new GameObject("Test");


        bepu_love.addComponent(bepu_spr);
        bepu_love.addComponent(cp1);
        bepu_love.addComponent(new RigidBody2D());

        testIMGUI.setPosition( new Vector3f(100, 100, 1) );
        testIMGUI.addComponent(cp2);
        testIMGUI.addComponent(spr);
        deez.setPosition(new Vector3f(200, 200, 1));
        deez.addComponent(cp3);
        deez.addComponent(spr1);


        addGameObjectToScene(bepu_love);
        addGameObjectToScene(testIMGUI);
        addGameObjectToScene(deez);
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
        String SpriteSheetTestPath = "Assets/Textures/SpriteSheetTest.png";
        new SpriteSheet(AssetPool.getTexture(SpriteSheetTestPath), 16, 16, 26, 0);
    }
}
