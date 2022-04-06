package engine.events;

import engine.Window;
import engine.scenemanager.SceneManager;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class MouseListener {
    private static MouseListener instance;
    private double xScroll, yScroll;
    private double xPos, yPos, lastY, lastX;

    private final boolean[] mouseButtonPressed = new boolean[9];
    private boolean isDragging;

    //region Singleton
    private MouseListener(){
        xScroll = yScroll = 0.0;
        xPos = yPos = 0.0;
        lastX = lastY = 0.0;

        Window.addEndFrameListener(() -> {
            instance.yScroll = instance.xScroll = 0;
            instance.lastX = instance.xPos;
            instance.lastY = instance.yPos;
        });
    }

    public static @NotNull MouseListener get() {
        if(instance == null){
            instance = new MouseListener();
        }

        return instance;
    }
    //endregion

    //region Mouse Callbacks
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
        if(button > instance.mouseButtonPressed.length)
            return;

        // Actions can be GLFW_RELEASE or GLFW_PRESS
        instance.mouseButtonPressed[button] = action == GLFW_PRESS;
        if(!instance.mouseButtonPressed[button]){
            instance.isDragging = false;
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset){
        instance.xScroll = xOffset;
        instance.yScroll = yOffset;
    }
    //endregion

    // region Get methods
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

    public static @NotNull Vector2f getOrthoPos(){
        Vector2f currentPosition = getPos();
        currentPosition.x = (currentPosition.x / (float)Window.getWidth()) * 2.0f - 1.0f; // Convert range from 0 to 1 to -1 to 1
        currentPosition.y = ( ((float)Window.getHeight() - currentPosition.y) / (float)Window.getHeight()) * 2.0f - 1.0f; // Convert range from 0 to 1 to -1 to 1
        Vector4f temp = new Vector4f(currentPosition.x, currentPosition.y, 0, 1);

        temp.mul(SceneManager.getActiveCamera().getInverseProjMatrix()).mul(SceneManager.getActiveCamera().getInverseViewMatrix());

        currentPosition.x = temp.x;
        currentPosition.y = temp.y;

        return currentPosition;
    }

    public static boolean isDragging(){
        return get().isDragging;
    }

    public static boolean isButtonDown(int button){
        if(button > instance.mouseButtonPressed.length)
            return false;

        return instance.mouseButtonPressed[button];
    }
    // endregion
}
