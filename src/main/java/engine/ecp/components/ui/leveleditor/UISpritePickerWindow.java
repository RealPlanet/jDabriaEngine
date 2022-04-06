package engine.ecp.components.ui.leveleditor;

import engine.ecp.GameObject;
import engine.ecp.Prefabs;
import engine.ecp.base.ImGUIComponent;
import engine.ecp.components.internal.MouseControls;
import engine.renderer.sprite.Sprite;
import engine.renderer.sprite.SpriteSheet;
import engine.scenemanager.SceneManager;
import engine.scenemanager.core.LevelEditor;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.enums.ImGuiCond;
import org.joml.Vector2f;

public class UISpritePickerWindow extends ImGUIComponent {

    private transient SpriteSheet sprites = null;
    private transient MouseControls parentControls = null;

    public UISpritePickerWindow(SpriteSheet sprites){
        this.sprites = sprites;
    }

    @Override
    protected void render() {
        String windowName = "Sprite Palette :: " + sprites.getParentTextureName();

        ImGui.begin(windowName);
        ImGui.setWindowSize(1024, 128, ImGuiCond.FirstUseEver);

        ImVec2 windowPosition = new ImVec2();
        ImVec2 windowSize = new ImVec2();
        ImVec2 itemSpacing = new ImVec2();

        ImGui.getWindowPos(windowPosition);
        ImGui.getWindowSize(windowSize);
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPosition.x + windowSize.x;
        for(int i=0; i < sprites.size(); i++){

            Sprite sprite = sprites.getSprite(i);
            float spriteWidth = sprite.getWidth() * 4;
            float spriteHeight = sprite.getHeight() * 4;
            int id = sprite.getTexture().getTexID();
            Vector2f[] texCoords = sprite.getTexCoords();

            ImGui.pushID(i);
            if(ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y) ){
                if(parentControls == null){
                    parentControls =  ((LevelEditor)SceneManager.GetActiveScene(LevelEditor.class.getCanonicalName())).getDDControls();
                }

                // copy is needed, otherwise generateSpriteObject will change the sprite pallet sprite size once it calls setSprite
                Sprite copySprite = new Sprite(sprite.getTexture(), sprite.getTexCoords());
                GameObject obj = Prefabs.generateSpriteObject(copySprite, spriteWidth, spriteHeight);
                parentControls.pickupObject(obj);
            }
            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
            if(i + 1 < sprites.size() && nextButtonX2 < windowX2){
                ImGui.sameLine();
            }
        }

        ImGui.end();
    }
}
