package JAssets.Scenes;

import Commons.Color;
import JDabria.AssetManager.AssetPool;
import JDabria.ECP.Components.Sprite.SpriteRenderer;
import JDabria.ECP.Components.UI.Pieces.UIColorPicker;
import JDabria.ECP.Components.UI.UIDrawable;
import JDabria.ECP.GameObject;
import JDabria.Events.ImGUI.IImGUIStartFrame;
import JDabria.ImGUI.ImGUILayer;
import JDabria.Renderer.Camera;
import JDabria.Renderer.Sprite.SpriteSheet;
import JDabria.SceneManager.Scene;
import JDabria.Window;
import imgui.ImGui;
import org.joml.Vector3f;

public class LevelEditor extends Scene {

    public LevelEditor(){
        System.out.println("Inside Level editor!");
    }

    private final IImGUIStartFrame LevelEditorWindow = () -> {
        ImGui.begin("Test window");
        ImGui.text("Hello world");
        ImGui.end();
    };

    @Override
    public void onInit() {
        sceneCamera = new Camera();
        Window.setWindowClearColor(new Color(1, 1, 1, 1));

        ImGUILayer.addStartFrameListener(LevelEditorWindow);


        GameObject testIMGUI = new GameObject("Test");
        testIMGUI.transform.position = new Vector3f(100, 100, 1);
        addGameObjectToScene(testIMGUI);
        UIDrawable io = new UIDrawable();
        UIColorPicker cp = new UIColorPicker();
        SpriteRenderer spr = new SpriteRenderer(Color.BLACK);

        spr.getSprite().setSize(100, 100);

        testIMGUI.addComponent(io);
        testIMGUI.addComponent(cp);
        testIMGUI.addComponent(spr);

        io.setDrawable(() -> {
            ImGui.begin("Test inspect drawable");
            ImGui.text("Hello draw");
            ImGui.end();
        });
    }

    @Override
    protected void onUpdate() {
        sceneRenderer.Render();
    }

    @Override
    protected void onUnload() {
        ImGUILayer.removeStartFrameListener(LevelEditorWindow);
    }

    @Override
    protected void loadResources() {
        AssetPool.getShader(AssetPool.DEFAULT_FALLBACK_SHADER);

        String SpriteSheetTestPath = "Assets/Textures/SpriteSheetTest.png";
        AssetPool.addSpriteSheet(SpriteSheetTestPath,
                new SpriteSheet(AssetPool.getTexture(SpriteSheetTestPath), 16, 16, 26, 0));
    }
}
