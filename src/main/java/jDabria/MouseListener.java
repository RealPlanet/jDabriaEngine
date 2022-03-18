package jDabria;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class MouseListener {
    private static MouseListener instance;
    private double xScroll, yScroll;
    private double xPos, yPos, lastY, lastX;

    private final boolean[] mouseButtonPressed = new boolean[3];
    private boolean isDragging;

    //<editor-fold desc="Singleton">
    private MouseListener(){
        xScroll = yScroll = 0.0;
        xPos = yPos = 0.0;
        lastX = lastY = 0.0;
    }

    public static @NotNull MouseListener get() {
        if(instance == null){
            instance = new MouseListener();
        }

        return instance;
    }
    //</editor-fold>

    //<editor-fold desc="Mouse Callbacks">
    public static void mousePositionCallback(long window, double xPos, double yPos){
        MouseListener mouseListener = get();
        mouseListener.lastX = mouseListener.xPos;
        mouseListener.lastY = mouseListener.yPos;
        mouseListener.xPos = xPos;
        mouseListener.yPos = yPos;

        // If mouse moves and any of these buttons are pressed then user must be dragging
        mouseListener.isDragging =  mouseListener.mouseButtonPressed[0] ||
                                    mouseListener.mouseButtonPressed[1] ||
                                    mouseListener.mouseButtonPressed[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods){
        MouseListener mouseListener = get();
        if(button > mouseListener.mouseButtonPressed.length)
            return;

        // Actions can be GLFW_RELEASE or GLFW_PRESS
        boolean actionResult = action == GLFW_PRESS;
        mouseListener.mouseButtonPressed[button] = actionResult;
        mouseListener.isDragging = actionResult;
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset){
        MouseListener mouseListener = get();
        mouseListener.yScroll = mouseListener.xScroll = 0;
        mouseListener.lastX = mouseListener.xPos;
        mouseListener.lastY = mouseListener.yPos;
    }
    //</editor-fold>

    //<editor-fold desc="Data access methods">
    public static @NotNull Vector2f getDeltaPos(){
        MouseListener mouseListener = get();
        return new Vector2f((float) (mouseListener.lastX - mouseListener.xPos),
                (float) (mouseListener.lastY - mouseListener.yPos));
    }

    public static @NotNull Vector2f getPos(){
        MouseListener mouseListener = get();
        return new Vector2f((float) (mouseListener.xPos),
                (float) (mouseListener.yPos));
    }

    public static @NotNull Vector2f getScroll(){
        MouseListener mouseListener = get();
        return new Vector2f((float) (mouseListener.xScroll),
                (float) (mouseListener.yScroll));
    }

    public static boolean isDragging(){
        return get().isDragging;
    }

    public static boolean isButtonDown(int button){
        MouseListener mouseListener = get();

        if(button > mouseListener.mouseButtonPressed.length)
            return false;

        return mouseListener.mouseButtonPressed[button];
    }
    //</editor-fold>
}
