package jDabria.ECP.components.UI.Pieces;

import imgui.ImGui;
import jDabria.ECP.components.UI.ImGUIComponent;

/**
 * Component which allows for UI rendering using ImGUI. The onUpdate loop is not the same as standard components. This component relies on the ImGUI onUpdate loop
 */
public class UITextBox extends ImGUIComponent {

    //region Member variables
    protected String title;
    protected String contents;
    //endregion

    public UITextBox setTitle(String title){
        this.title = title;
        return this;
    }

    public UITextBox setContent(String contents){
        this.contents = contents;
        return this;
    }

    //region Protected methods
    @Override
    protected void render(){
        ImGui.begin(title);
        ImGui.text(contents);
        ImGui.end();
    }
    //endregion
}
