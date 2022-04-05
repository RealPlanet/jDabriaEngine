package engine.scenemanager.core;

import engine.scenemanager.Scene;
import engine.ecp.GameObject;
import engine.ecp.components.ui.leveleditor.UISpritePickerWindow;
import engine.assetmanager.AssetPool;
import engine.renderer.Camera;
import engine.renderer.sprite.SpriteSheet;

public class LevelEditor extends Scene {

    private SpriteSheet gameSprites;

    public LevelEditor(){
        System.out.println("Inside Level editor!");
    }

    @Override
    public void onInit() {
        sceneCamera = new Camera();

        GameObject LevelEditor = new GameObject("LVL_EDITOR");
        LevelEditor.addComponent(new UISpritePickerWindow(gameSprites));


        addGameObjectToScene(LevelEditor);
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
        AssetPool.addSpriteSheet("Assets/Textures/decorationsAndBlocks.png",
                new SpriteSheet(AssetPool.getTexture("Assets/Textures/decorationsAndBlocks.png"), 16, 16, 81, 0));

        gameSprites = AssetPool.getSpriteSheet("Assets/Textures/decorationsAndBlocks.png");
    }
}
