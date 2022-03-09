package JDabria;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private static Window _Window = null; //Singleton

    private final int _Width, _Height;
    private final String _Title;
    private long glfwWindow; // Window mem address

    //<editor-fold desc="Singleton">
    private Window(){
        _Width = 1440;
        _Height = 960;
        _Title = "Mario";
    }

    public static @NotNull Window Get(){
        if(_Window == null){
            _Window = new Window();
        }

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
        glfwSetErrorCallback(null).free();
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
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);  //      Start maximize
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
        //</editor-fold>
    }

    private void Loop() {
        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(glfwWindow) ) {
            // Poll events
            glfwPollEvents();
            glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

            // clear the framebuffer
            glClear(GL_COLOR_BUFFER_BIT);

            if(KeyListener.IsKeyPressed(GLFW_KEY_SPACE))
                System.out.println("Space is pressed!!");

            // swap the color buffers
            glfwSwapBuffers(glfwWindow);
        }
    }
    //</editor-fold>
}
