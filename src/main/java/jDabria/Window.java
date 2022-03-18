package jDabria;

import commons.Color;
import jAssets.scenes.LevelEditor;
import jDabria.events.window.IBeginFrameListener;
import jDabria.events.window.IEndFrameListener;
import jDabria.events.window.IUpdateFrameListener;
import jDabria.imGUI.ImGUILayer;
import jDabria.sceneManager.Scene;
import jDabria.sceneManager.SceneManager;
import jDabria.serialization.GameObjectWR;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.glBlendFuncSeparate;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private final static Window WINDOW = new Window(); //Singleton

    // ImGUI Layer
    private ImGUILayer imGUILayer;

    private int width;
    private int height;
    private final String title;
    private long glfwWindow; // Window mem address

    //<editor-fold desc="Singleton">
    private Window(){
        width = 1920;
        height = 1080;
        title = "Dabria v0.0.1";
    }

    /**
     * Gets the window instance. If none exist it is created on the spot
     * @return The window instance
     */
    public static @NotNull Window getWindow(){
        return WINDOW;
    }
    //</editor-fold>

    // region Window getter / setter
    public static int getWidth() {
        return getWindow().width;
    }
    public static int getHeight() {
        return getWindow().width;
    }
    // endregion

    //<editor-fold desc="Window execution methods">

    /**
     * Initiates window logic for the window instance. Handles allocating the window and the think loop.
     */
    public void run(){
        System.out.println("Starting LWJG " + Version.getVersion());
        init();
        loop();

        // Free memory taken from window
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    public void stop(){
        imGUILayer.destroyImGui();
    }

    private void init() {
        // Error Callback output
        GLFWErrorCallback.createPrint(System.err).set();

        // onInit our boy
        if(!glfwInit()){
            throw  new IllegalStateException("GLFW onInit failed!!!!");
        }

        //<editor-fold desc="Window base settings">
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);   //      Hide window until we are ready
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);  //      Allow resize
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);  //      start maximize
        //</editor-fold>

        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(glfwWindow == NULL){
            throw new IllegalStateException("Failed to create GLFW Window");
        }

        //<editor-fold desc="Window Callbacks">
        // Mouse
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePositionCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);

        // Technically this isn't needed, but better safe than sorry!
        glfwSetWindowSizeCallback(glfwWindow, (w, nWidth, nHeight) ->{
            Window.setDimension(nWidth, nHeight);
        });

        //Keyboard
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

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

        // Enable alpha
        glEnable(GL_BLEND);
        glBlendFuncSeparate( GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_SRC_ALPHA, GL_ONE_MINUS_DST_ALPHA );
        //glClearColor(0.0F,0.0F,0.0F,0.0F);

        // Setup GUI
        imGUILayer = ImGUILayer.getImGUILayer(glfwWindow);

        //</editor-fold>
    }

    private static void setDimension(int nWidth, int nHeight) {
        WINDOW.width = nWidth;
        WINDOW.height = nHeight;
    }

    private void loop() {
        Window.setWindowClearColor(new Color(1, 1, 1, 1));
        SceneManager.loadScene(LevelEditor.class.getCanonicalName(), SceneManager.LoadType.SINGLE);
        Scene level = SceneManager.GetActiveScene(LevelEditor.class.getCanonicalName());
        level.clearScene();
        GameObjectWR.Read(level, LevelEditor.class.getCanonicalName());
        // run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(glfwWindow) ) {
            signalNewFrame();

            //<editor-fold desc="Begin frame operations">
            // Poll events
            glfwPollEvents();
            // clear the framebuffer
            glClear(GL_COLOR_BUFFER_BIT);
            //</editor-fold>

            signalUpdateFrame();
            //<editor-fold desc="End frame operations">
            // swap the color buffers
            glfwSwapBuffers(glfwWindow);
            //</editor-fold>

            signalEndFrame();
        }

        GameObjectWR.Write(SceneManager.GetActiveScene(LevelEditor.class.getCanonicalName()).getGameObjects(), LevelEditor.class.getCanonicalName());
    }
    //</editor-fold>

    //<editor-fold desc="Debug methods">

    /**
     * Sets the clear color this window should use when nothing is being renderer
     * @param color The color RGBA to use
     */
    public static void setWindowClearColor(@NotNull Color color){
        glClearColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
    //</editor-fold>

    //<editor-fold desc="Window Events">
    private final ArrayList<IUpdateFrameListener> updateFrameListeners = new ArrayList<>();
    private final ArrayList<IBeginFrameListener> beginFrameListeners = new ArrayList<>();
    private final ArrayList<IEndFrameListener> endFrameListeners = new ArrayList<>();

    /**
     * Adds an object which implements the IUpdateFrameListener interface to the event list
     * @param updateFrameListener The object listening for this event
     */
    public static void addUpdateFrameListener(IUpdateFrameListener updateFrameListener){
        getWindow().updateFrameListeners.add(updateFrameListener);
    }

    /**
     * Adds an object which implements the IUpdateFrameListener interface to the event list
     * @param updateFrameListener The object listening for this event
     */
    public static void removeUpdateFrameListener(IUpdateFrameListener updateFrameListener){
        getWindow().updateFrameListeners.remove(updateFrameListener);
    }

    /**
     * Adds an object which implements the IUpdateFrameListener interface to the event list
     * @param beginFrameListener The object listening for this event
     */
    public static void addBeginFrameListener(IBeginFrameListener beginFrameListener){
        getWindow().beginFrameListeners.add(beginFrameListener);
    }

    /**
     * Adds an object which implements the IUpdateFrameListener interface to the event list
     * @param beginFrameListener The object listening for this event
     */
    public static void removeBeginFrameListener(IBeginFrameListener beginFrameListener){
        getWindow().beginFrameListeners.remove(beginFrameListener);
    }

    /**
     * Adds an object which implements the IUpdateFrameListener interface to the event list
     * @param endFrameListener The object listening for this event
     */
    public static void addEndFrameListener(IEndFrameListener endFrameListener){
        getWindow().endFrameListeners.add(endFrameListener);
    }

    /**
     * Adds an object which implements the IUpdateFrameListener interface to the event list
     * @param endFrameListener The object listening for this event
     */
    public static void removeEndFrameListener(IEndFrameListener endFrameListener){
        getWindow().endFrameListeners.remove(endFrameListener);
    }

    private void signalNewFrame(){
        for (int i = beginFrameListeners.size() - 1; i >= 0; i--) {
            IBeginFrameListener beginFrameListener = beginFrameListeners.get(i);
            if (beginFrameListener == null) {
                throw new RuntimeException("OnNewFrameListener was null");
            }

            beginFrameListener.onBeginFrame();
        }
    }

    private void signalUpdateFrame(){
        for (int i = updateFrameListeners.size() - 1; i >= 0; i--) {
            IUpdateFrameListener updateFrameListener = updateFrameListeners.get(i);

            if (updateFrameListener == null) {
                throw new RuntimeException("OnUpdateFrameListener was null");
            }

            updateFrameListener.onFrameUpdate();
        }
    }

    private void signalEndFrame(){
        for (int i = endFrameListeners.size() - 1; i >= 0; i--) {
            IEndFrameListener endFrameListener = endFrameListeners.get(i);

            if (endFrameListener == null) {
                throw new RuntimeException("OnEndFrameListener was null");
            }

            endFrameListener.onEndFrame();
        }
    }
    //</editor-fold>
}
