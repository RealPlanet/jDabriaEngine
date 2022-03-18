package JDabria.ECP.Components.UI.Pieces;

import Commons.Color;
import JDabria.ECP.Components.Sprite.SpriteRenderer;
import JDabria.ECP.Components.UI.UIDrawable;
import JDabria.Events.ImGUI.IImGUIDrawInspectable;
import imgui.ImGui;

/**
 * If a sprite renderer is present on the game-object a color wheel is created to edit it's color
 */
public class UIColorPicker extends UIDrawable {
    private SpriteRenderer OwnerSpriteRenderer = null;

    public UIColorPicker(){
        this.drawable = () -> {
            if(OwnerSpriteRenderer == null){
                OwnerSpriteRenderer = gameObject.getComponent(SpriteRenderer.class);

                if(OwnerSpriteRenderer == null){
                    assert false : "ERROR (UIColorPicker) -> Cannot find sprite renderer!";
                    return;
                }
            }

            Color color = OwnerSpriteRenderer.getColor();
            float[] imColor = {color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()};
            if(ImGui.colorPicker4("Color Picker: ", imColor)){
                OwnerSpriteRenderer.setColor(new Color(imColor[0], imColor[1], imColor[2], imColor[3]));
                OwnerSpriteRenderer.setDirty();
            }
        };
    }

    @Override
    public void setDrawable(IImGUIDrawInspectable drawable){

    }
}
