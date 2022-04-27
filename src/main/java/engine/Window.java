package engine;

import commons.Color;
import commons.util.logging.EngineLogger;
import engine.events.GLFWEventHandler;
import engine.events.KeyListener;
import engine.events.MouseListener;
import engine.events.window.IBeginFrameListener;
import engine.events.window.IEndFrameListener;
import engine.events.window.IUpdateFrameListener;
import engine.renderer.imgui.ImGUILayer;
import engine.scenemanager.SceneManager;
import engine.scenemanager.core.LevelEditor;
import engine.scenemanager.core.LevelTestObjects;
import engine.serialization.GameSerialize;
import imgui.ImGui;
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

    // GLFW Event handler
    private GLFWEventHandler glfwEventHandler;

    private int width;
    private int height;
    private final String title;
    private long glfwWindow; // Window mem address

    private boolean wasInit = false;

    //region Singleton
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
    //endregion

    // region Window getter / setter
    public static int getWidth() {
        return getWindow().width;
    }
    public static int getHeight() {
        return getWindow().height;
    }
    public static GLFWEventHandler getGLFWEventHandler(){ return getWindow().glfwEventHandler; }
    // endregion

    // region Window execution methods

    /**
     * Initiates window logic for the window instance. Handles allocating the window and the think loop.
     */
    public void run(){
        System.out.println("Starting LWJG " + Version.getVersion());
        loop();

        stop();
    }

    public void stop(){
        imGUILayer.destroyImGui();
        // Free memory taken from window
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
        wasInit = false;
    }

    public void init() {
        if(wasInit){
            EngineLogger.logWarning("Window was already initialized!");
            return;
        }

        // Error Callback output
        GLFWErrorCallback.createPrint(System.err).set();

        // init our boy
        if(!glfwInit()){
            throw  new IllegalStateException("GLFW onInit failed!!!!");
        }

        // region Window base settings
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);   //      Hide window until we are ready
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);  //      Allow resize
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);  //      start maximize
        // endregion

        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(glfwWindow == NULL){
            throw new IllegalStateException("Failed to create GLFW Window");
        }

        glfwEventHandler = new GLFWEventHandler(glfwWindow);

        // region Window Callbacks
        glfwEventHandler.glfwSetCursorPosCallback(MouseListener::mousePositionCallback);
        glfwEventHandler.glfwSetMouseButtonCallback(MouseListener::mouseButtonCallback);
        glfwEventHandler.glfwSetScrollCallback(MouseListener::mouseScrollCallback);
        glfwEventHandler.glfwSetKeyCallback(KeyListener::keyCallback);

        // Technically this isn't needed, but better safe than sorry!
        glfwEventHandler.glfwSetWindowSizeCallback((w, nWidth, nHeight) -> Window.setDimension(nWidth, nHeight));

        // endregion

        //region Window Finalize

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

        // Setup GUI
        imGUILayer = ImGUILayer.getImGUILayer(glfwWindow);
        wasInit = true;
        //endregion
    }

    private static void setDimension(int nWidth, int nHeight) {
        WINDOW.width = nWidth;
        WINDOW.height = nHeight;
        ImGui.getIO().setDisplaySize((float)nWidth, (float)nHeight);
    }

    private void loop() {
        if(!wasInit){
            EngineLogger.logError("Window was not initialized!");
            return;
        }

        Window.setWindowClearColor(new Color(1, 1, 1, 1));
        SceneManager.loadScene(LevelEditor.class.getCanonicalName(), SceneManager.LoadType.SINGLE);

        GameSerialize gs = new GameSerialize();

        // run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(glfwWindow) ) {
            signalNewFrame();

            //region Begin frame operations
            // Poll events
            glfwPollEvents();
            // clear the framebuffer
            glClear(GL_COLOR_BUFFER_BIT);
            //endregion

            signalUpdateFrame();
            //region End frame operations
            // swap the color buffers
            glfwSwapBuffers(glfwWindow);
            //endregion

            signalEndFrame();
        }

        gs.write(SceneManager.GetActiveScene(LevelEditor.class.getCanonicalName()).getGameObjects(), LevelTestObjects.class.getCanonicalName());
    }
    // endregion

    //region Debug methods

    /**
     * Sets the clear color this window should use when nothing is being renderer
     * @param color The color RGBA to use
     */
    public static void setWindowClearColor(@NotNull Color color){
        glClearColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
    //endregion

    //region Window Events
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
    //endregion
}
