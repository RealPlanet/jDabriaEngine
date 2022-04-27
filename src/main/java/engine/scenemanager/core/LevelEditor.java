package engine.scenemanager.core;

import commons.Color;
import commons.Time;
import engine.assetmanager.AssetPool;
import engine.ecp.GameObject;
import engine.ecp.components.Camera;
import engine.ecp.components.internal.MouseControls;
import engine.ecp.components.ui.leveleditor.EditorGrid;
import engine.ecp.components.ui.leveleditor.UISpritePickerWindow;
import engine.events.MouseListener;
import engine.renderer.debug.DebugDraw;
import engine.renderer.sprite.SpriteSheet;
import engine.scenemanager.Scene;
import org.joml.Vector2f;
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

    float rotation = 30f, x = 0f, y = 0f;
    @Override
    protected void onUpdate() {
        MouseListener.getOrthoPos();
        DebugDraw.drawBox2D(new Vector3f(400, 200, 0), new Vector2f(64, 32), rotation, Color.GREEN);
        DebugDraw.drawCircle2D(new Vector3f(x + 50, y + 50, 0), 64);
        rotation += Time.deltaTime() * 30;
        x += 50f * Time.deltaTime();
        y += 50f * Time.deltaTime();

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
