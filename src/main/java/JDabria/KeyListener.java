package JDabria;

import org.jetbrains.annotations.NotNull;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class KeyListener {
    private static KeyListener _Instance;

    //Amount of keybindings that GLFW utilizes, possibly wrong!
    private final boolean[] KeyPressed = new boolean[350];

    //<editor-fold desc="Singleton">
    private KeyListener(){

    }

    public static @NotNull KeyListener Get(){
        if(_Instance == null){
            _Instance = new KeyListener();
        }
        return _Instance;
    }
    //</editor-fold>

    //<editor-fold desc="Callbacks">
    public static void KeyCallback(long Window, int Key, int ScanCode, int Action, int Mods){
        KeyListener Listener = Get();

        // PERHAPS THROWING AN EXCEPTION MIGHT BE BETTER
        // Just in case check for array size
        //if(KeyCode > Listener.KeyPressed.length)
        //    return false;

        // Possible actions are GLFW_RELEASE and GLFW_PRESS
        Listener.KeyPressed[Key] = Action == GLFW_PRESS;
    }
    //</editor-fold>

    //<editor-fold desc="Data access methods">
    public static boolean IsKeyPressed(int KeyCode){
        KeyListener Listener = Get();

        // PERHAPS THROWING AN EXCEPTION MIGHT BE BETTER
        // Just in case check for array size
        //if(KeyCode > Listener.KeyPressed.length)
        //    return false;

        return Listener.KeyPressed[KeyCode];
    }
    //</editor-fold>
}
