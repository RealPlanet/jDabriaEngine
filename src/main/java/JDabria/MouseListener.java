package JDabria;

import Commons.Tuple;
import org.jetbrains.annotations.NotNull;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class MouseListener {
    private static MouseListener _Instance;
    private double XScroll, YScroll;
    private double XPos, YPos, lastY, lastX;

    private boolean MouseButtonPressed[] = new boolean[3];
    private boolean IsDragging;

    //<editor-fold desc="Singleton">
    private MouseListener(){
        XScroll = YScroll = 0.0;
        XPos = YPos = 0.0;
        lastX = lastY = 0.0;
    }

    public static @NotNull MouseListener Get() {
        if(_Instance == null){
            _Instance = new MouseListener();
        }

        return _Instance;
    }
    //</editor-fold>

    //<editor-fold desc="Mouse Callbacks">
    public static void MousePositionCallback(long Window, double XPos, double YPos){
        MouseListener Listener = Get();
        Listener.lastX = Listener.XPos;
        Listener.lastY = Listener.YPos;
        Listener.XPos = XPos;
        Listener.YPos = YPos;

        // If mouse moves and any of these buttons are pressed then user must be dragging
        Listener.IsDragging =   Listener.MouseButtonPressed[0] ||
                                Listener.MouseButtonPressed[1] ||
                                Listener.MouseButtonPressed[2];
    }

    public static void MouseButtonCallback(long Window, int Button, int Action, int Mods){
        MouseListener Listener = Get();
        if(Button > Listener.MouseButtonPressed.length)
            return;

        // Actions can be GLFW_RELEASE or GLFW_PRESS
        boolean ActionResult = Action == GLFW_PRESS;
        Listener.MouseButtonPressed[Button] = ActionResult;
        Listener.IsDragging = ActionResult;
    }

    public static void MouseScrollCallback(long Window, double XOffset, double YOffset){
        MouseListener Listener = Get();
        Listener.YScroll = Listener.XScroll = 0;
        Listener.lastX = Listener.XPos;
        Listener.lastY = Listener.YPos;
    }
    //</editor-fold>

    //<editor-fold desc="Data access methods">
    public static @NotNull Tuple<Float, Float> GetDeltaPos(){
        MouseListener Listener = Get();
        Tuple<Float, Float> Result = new Tuple<Float, Float>(   (float)(Listener.lastX - Listener.XPos),
                                                                (float)(Listener.lastY - Listener.YPos));
        return Result;
    }

    public static @NotNull Tuple<Float, Float> GetPos(){
        MouseListener Listener = Get();
        Tuple<Float, Float> Result = new Tuple<Float, Float>(   (float)(Listener.XPos),
                                                                (float)(Listener.YPos));
        return Result;
    }

    public static @NotNull Tuple<Float, Float> GetScroll(){
        MouseListener Listener = Get();
        Tuple<Float, Float> Result = new Tuple<Float, Float>(   (float)(Listener.XScroll),
                                                                (float)(Listener.YScroll));
        return Result;
    }

    public static @NotNull boolean IsDragging(){
        return Get().IsDragging;
    }

    public static @NotNull boolean IsButtonDown(int Button){
        MouseListener Listener = Get();

        if(Button > Listener.MouseButtonPressed.length)
            return false;

        return Listener.MouseButtonPressed[Button];
    }
    //</editor-fold>
}
