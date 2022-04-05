package engine.ecp.components.ui.leveleditor;

import imgui.ImGui;
import imgui.ImVec2;
import engine.ecp.base.ImGUIComponent;
import engine.renderer.sprite.Sprite;
import engine.renderer.sprite.SpriteSheet;
import org.joml.Vector2f;

public class UISpritePickerWindow extends ImGUIComponent {

    private transient SpriteSheet sprites = null;

    public UISpritePickerWindow(SpriteSheet sprites){
        this.sprites = sprites;
    }

    @Override
    protected void render() {
        ImGui.begin("Test");
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
                System.out.println("Button " + i + "clicked");
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
