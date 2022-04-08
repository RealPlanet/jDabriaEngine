package engine.scenemanager.core;

import engine.assetmanager.AssetPool;
import engine.ecp.GameObject;
import engine.ecp.components.Camera;
import engine.ecp.components.internal.MouseControls;
import engine.ecp.components.ui.leveleditor.EditorGrid;
import engine.ecp.components.ui.leveleditor.UISpritePickerWindow;
import engine.events.MouseListener;
import engine.renderer.sprite.SpriteSheet;
import engine.scenemanager.Scene;
import org.joml.Vector3f;

public class LevelEditor extends Scene {

    private transient SpriteSheet gameSprites;
    private transient MouseControls ddControls;
    private transient EditorGrid grid;

    public transient  GameObject levelEditorObject = new GameObject("LVL_EDITOR");

    public LevelEditor(){
        System.out.println("Inside Level editor!");
    }

    public MouseControls getDDControls(){
        return ddControls;
    }

    @Override
    public void onInit() {
        sceneCamera = Camera.createDefaultCamera(this);
        sceneCamera.setPosition(new Vector3f(-250, 0, 0));

        ddControls = new MouseControls(this);
        grid = new EditorGrid();
        levelEditorObject.addComponent(new UISpritePickerWindow(gameSprites));
        levelEditorObject.addComponent(ddControls);
        levelEditorObject.addComponent(grid);

        addGameObjectToScene(levelEditorObject);
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
