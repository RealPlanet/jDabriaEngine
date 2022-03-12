package JDabria;

import org.jetbrains.annotations.NotNull;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class keyListener {
    private static keyListener instance;

    //Amount of keybindings that GLFW utilizes, possibly wrong!
    private final boolean[] keyPressed = new boolean[350];

    //<editor-fold desc="Singleton">
    private keyListener(){

    }

    public static @NotNull keyListener Get(){
        if(instance == null){
            instance = new keyListener();
        }
        return instance;
    }
    //</editor-fold>

    //<editor-fold desc="Callbacks">
    public static void keyCallback(long window, int key, int scanCode, int action, int mods){
        keyListener listener = Get();

        // Possible actions are GLFW_RELEASE and GLFW_PRESS
        listener.keyPressed[key] = action == GLFW_PRESS;
    }
    //</editor-fold>

    //<editor-fold desc="Data access methods">
    public static boolean isKeyPressed(int keyCode){
        keyListener listener = Get();
        return listener.keyPressed[keyCode];
    }
    //</editor-fold>
}
