package engine.scenemanager.core;

import engine.assetmanager.AssetPool;
import engine.ecp.GameObject;
import engine.ecp.components.internal.MouseControls;
import engine.ecp.components.ui.leveleditor.UISpritePickerWindow;
import engine.events.MouseListener;
import engine.renderer.sprite.SpriteSheet;
import engine.scenemanager.Scene;
import org.joml.Vector3f;

public class LevelEditor extends Scene {

    private transient SpriteSheet gameSprites;
    private transient MouseControls ddControls;

    public LevelEditor(){
        System.out.println("Inside Level editor!");
    }

    public MouseControls getDDControls(){
        return ddControls;
    }

    @Override
    public void onInit() {
        sceneCamera.setPosition(new Vector3f(-250, 0, 0));
        GameObject LevelEditor = new GameObject("LVL_EDITOR");

        LevelEditor.addComponent(new UISpritePickerWindow(gameSprites));

        ddControls = new MouseControls(this);
        LevelEditor.addComponent(ddControls);

        addGameObjectToScene(LevelEditor);
    }

    @Override
    protected void onUpdate() {
        MouseListener.getOrthoPos();
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
