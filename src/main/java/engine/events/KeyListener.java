package engine.events;

import org.jetbrains.annotations.NotNull;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class KeyListener {
    private static KeyListener instance;

    //Amount of keybindings that GLFW utilizes, possibly wrong!
    private final boolean[] keyPressed = new boolean[350];

    //region Singleton
    private KeyListener(){

    }

    public static @NotNull KeyListener Get(){
        if(instance == null){
            instance = new KeyListener();
        }
        return instance;
    }
    //endregion

    //region Callbacks
    public static void keyCallback(long window, int key, int scanCode, int action, int mods){
        KeyListener listener = Get();

        // Possible actions are GLFW_RELEASE and GLFW_PRESS
        listener.keyPressed[key] = action == GLFW_PRESS;
    }
    //endregion

    //region Data access methods
    public static boolean isKeyPressed(int keyCode){
        KeyListener listener = Get();
        return listener.keyPressed[keyCode];
    }
    //endregion
}
