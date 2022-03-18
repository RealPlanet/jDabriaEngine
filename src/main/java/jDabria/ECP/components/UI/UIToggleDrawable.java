package jDabria.ECP.components.UI;

/**
 * Component which allows for UI rendering using ImGUI. The onUpdate loop is not the same as standard components. This component relies on the ImGUI onUpdate loop
 */
public class UIToggleDrawable extends UIDrawable {
    private boolean isInspected = false;

    protected void renderUI(){
        if(isInspected){
            super.renderUI();
        }
    }

    public void setInspect(boolean toggle){
        isInspected = toggle;
    }
}
