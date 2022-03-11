package JDabria;

import Commons.Color;
import JDabria.Events.Window.IBeginFrameListener;
import JDabria.Events.Window.IEndFrameListener;
import JDabria.Events.Window.IUpdateFrameListener;
import JDabria.SceneManager.SceneManager;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private final static Window _Window = new Window(); //Singleton

    private final int _Width, _Height;
    private final String _Title;
    private long glfwWindow; // Window mem address

    //<editor-fold desc="Singleton">
    private Window(){
        _Width = 1920;
        _Height = 1080;
        _Title = "Mario";
    }

    public static @NotNull Window GetWindow(){
        return _Window;
    }
    //</editor-fold>

    //<editor-fold desc="Window execution methods">
    public void Run(){
        System.out.println("Starting LWJG " + Version.getVersion());
        Init();
        Loop();

        // Free memory taken from window
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    private void Init() {
        // Error Callback output
        GLFWErrorCallback.createPrint(System.err).set();

        // Init our boy
        if(!glfwInit()){
            throw  new IllegalStateException("GLFW Init failed!!!!");
        }

        //<editor-fold desc="Window base settings">
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);   //      Hide window until we are ready
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);  //      Allow resize
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);  //      Start maximize
        //</editor-fold>

        glfwWindow = glfwCreateWindow(this._Width, this._Height, this._Title, NULL, NULL);
        if(glfwWindow == NULL){
            throw new IllegalStateException("Failed to create GLFW Window");
        }

        //<editor-fold desc="Window Callbacks">
        // Mouse
        glfwSetCursorPosCallback(glfwWindow, MouseListener::MousePositionCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::MouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::MouseScrollCallback);

        //Keyboard
        glfwSetKeyCallback(glfwWindow, KeyListener::KeyCallback);

        //</editor-fold>

        //<editor-fold desc="Window Finalize">
        // Set OpenGL Context
        glfwMakeContextCurrent(glfwWindow);

        // Enable v-sync
        glfwSwapInterval(1);

        // Show window to user
        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        //</editor-fold>
    }

    private void Loop() {
        SceneManager.LoadScene("LevelEditor", SceneManager.LoadType.SINGLE);
        //SceneManager.LoadScene("Debug.DebugScene", SceneManager.LoadType.ADDITIVE);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(glfwWindow) ) {

            SignalNewFrame();

            //<editor-fold desc="Begin frame operations">
            // Poll events
            glfwPollEvents();
            // clear the framebuffer
            glClear(GL_COLOR_BUFFER_BIT);
            //</editor-fold>

            SignalUpdateFrame();

            //<editor-fold desc="End frame operations">
            // swap the color buffers
            glfwSwapBuffers(glfwWindow);
            //</editor-fold>

            SignalEndFrame();
        }
    }
    //</editor-fold>

    //<editor-fold desc="Debug methods">
    public static void SetWindowClearColor(@NotNull Color ClearColor){
        glClearColor(ClearColor.GetRed(), ClearColor.GetGreen(), ClearColor.GetBlue(), ClearColor.GetAlpha());
    }
    //</editor-fold>

    //<editor-fold desc="Window Events">
    private final ArrayList<IUpdateFrameListener> UpdateFrameListeners = new ArrayList<>();
    private final ArrayList<IBeginFrameListener> BeginFrameListeners = new ArrayList<>();
    private final ArrayList<IEndFrameListener> EndFrameListeners = new ArrayList<>();

    public static void AddUpdateFrameListener(IUpdateFrameListener Listener){
        GetWindow().UpdateFrameListeners.add(Listener);
    }

    public static void RemoveUpdateFrameListener(IUpdateFrameListener Listener){
        GetWindow().UpdateFrameListeners.remove(Listener);
    }

    public static void AddBeginFrameListener(IBeginFrameListener Listener){
        GetWindow().BeginFrameListeners.add(Listener);
    }

    public static void RemoveBeginFrameListener(IBeginFrameListener Listener){
        GetWindow().BeginFrameListeners.remove(Listener);
    }

    public static void AddEndFrameListener(IEndFrameListener Listener){
        GetWindow().EndFrameListeners.add(Listener);
    }

    public static void RemoveEndFrameListener(IEndFrameListener Listener){
        GetWindow().EndFrameListeners.remove(Listener);
    }

    private void SignalNewFrame(){
        for (int i = BeginFrameListeners.size() - 1; i >= 0; i--) {
            IBeginFrameListener Listener = BeginFrameListeners.get(i);
            if (Listener == null) {
                throw new RuntimeException("OnNewFrameListener was null");
            }

            Listener.OnBeginFrame();
        }
    }

    private void SignalUpdateFrame(){
        for (int i = UpdateFrameListeners.size() - 1; i >= 0; i--) {
            IUpdateFrameListener Listener = UpdateFrameListeners.get(i);

            if (Listener == null) {
                throw new RuntimeException("OnUpdateFrameListener was null");
            }

            Listener.OnFrameUpdate();
        }
    }

    private void SignalEndFrame(){
        for (int i = EndFrameListeners.size() - 1; i >= 0; i--) {
            IEndFrameListener Listener = EndFrameListeners.get(i);

            if (Listener == null) {
                throw new RuntimeException("OnEndFrameListener was null");
            }

            Listener.OnEndFrame();
        }
    }
    //</editor-fold>
}
