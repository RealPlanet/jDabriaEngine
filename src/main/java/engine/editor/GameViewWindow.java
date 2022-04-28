package engine.editor;

import engine.Window;
import engine.events.imGUI.IImGUIStartFrame;
import engine.renderer.imgui.ImGUILayer;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import imgui.internal.ImGui;
import org.jetbrains.annotations.NotNull;

public class GameViewWindow implements IImGUIStartFrame {

    public static void init(){
        if(_instance == null){
            _instance = new GameViewWindow();
        }
    }

    private static GameViewWindow _instance;
    private GameViewWindow(){
        ImGUILayer.addStartFrameListener(this);
    }

    @Override
    public void render() {
        ImGui.begin("Game viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse);

        ImVec2 windowSize = getLargestSizeForViewport();
        ImVec2 windowPos = getCenteredPositionForViewport(windowSize);

        ImGui.setCursorPos(windowPos.x, windowPos.y);
        int texId = Window.getFramebuffer().getTexture().getTexID();
        ImGui.image(texId, windowSize.x, windowSize.y, 0,1,1,0);

        ImGui.end();
    }

    private @NotNull ImVec2 getLargestSizeForViewport() {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float aspectWidth = windowSize.x;
        float aspectHeight = aspectWidth / Window.getTargetAspectRatio();
        if(aspectHeight > windowSize.y){
            // Pillarbox
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * Window.getTargetAspectRatio();
        }
        // Override values to avoid allocating a new ImVec2
        windowSize.x = aspectWidth;
        windowSize.y = aspectHeight;

        return windowSize;
    }

    private @NotNull ImVec2 getCenteredPositionForViewport(@NotNull ImVec2 aspectSize) {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float viewPortX = (windowSize.x / 2.0f) - (aspectSize.x / 2.0f);
        float viewPortY = (windowSize.y / 2.0f) - (aspectSize.y / 2.0f);

        // Override values to avoid allocating a new ImVec2
        windowSize.x = viewPortX + ImGui.getCursorPosX();
        windowSize.y = viewPortY + ImGui.getCursorPosY();
        return windowSize;
    }
}
