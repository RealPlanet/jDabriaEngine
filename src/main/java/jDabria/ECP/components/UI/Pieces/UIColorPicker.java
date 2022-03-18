package jDabria.ECP.components.UI.Pieces;

import commons.Color;
import imgui.ImGui;
import jDabria.ECP.components.Sprite.SpriteRenderer;
import jDabria.ECP.components.UI.ImGUIComponent;

/**
 * If a sprite renderer is present on the game-object a color wheel is created to edit it's color
 */
public class UIColorPicker extends ImGUIComponent {

    private transient SpriteRenderer OwnerSpriteRenderer = null;

    /**
     * Initializes the component and setups "color wheel <-> sprite" connection. Will not work if {@link SpriteRenderer} is not found on the parent game object.
     */
    @Override
    public void render(){
        if(OwnerSpriteRenderer == null){
            OwnerSpriteRenderer = gameObject.getComponent(SpriteRenderer.class);

            if(OwnerSpriteRenderer == null){
                assert false : "ERROR (UIColorPicker) -> Cannot find sprite renderer!";
                return;
            }
        }

        Color color = OwnerSpriteRenderer.getColor();
        float[] imColor = {color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()};
        ImGui.begin(gameObject.getName());
        if(ImGui.colorPicker4("Color Picker: ", imColor)){
            OwnerSpriteRenderer.setColor(new Color(imColor[0], imColor[1], imColor[2], imColor[3]));
        }

        ImGui.end();
    }
}
