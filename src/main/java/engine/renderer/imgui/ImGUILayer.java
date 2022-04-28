package engine.renderer.imgui;

import commons.Time;
import engine.Window;
import engine.events.imGUI.IImGUIStartFrame;
import engine.events.window.IUpdateImGUIListener;
import imgui.*;
import imgui.callback.ImStrConsumer;
import imgui.callback.ImStrSupplier;
import imgui.flag.*;
import imgui.gl3.ImGuiImplGl3;
import imgui.type.ImBoolean;

import java.io.File;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class ImGUILayer implements IUpdateImGUIListener {
    //region ImGUI variables
    private static final String glslVersion = "#version 330 core";

    // Mouse cursors provided by GLFW
    private final long[] mouseCursors = new long[ImGuiMouseCursor.COUNT];

    // LWJGL3 renderer (SHOULD be initialized)
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private final long glfwWindow;
    //endregion

    //region Singleton
    private static ImGUILayer _instance = null;
    public static void destroyImGUILayer(){
        if(_instance == null){
            return;
        }

        _instance.destroyImGui();
        _instance = null;
    }

    public static ImGUILayer getImGUILayer(long glfwWindow){
        if(_instance == null){
            _instance = new ImGUILayer(glfwWindow);
            _instance.initImGui();
        }

        return _instance;
    }

    private ImGUILayer(long glfwWindow) {
        this.glfwWindow = glfwWindow;
    }
    //endregion

    // region Frame methods
    // Start/End methods are called in onUpdate to avoid cleanup/init operations happening too late/ too early
    private void startFrame() {
        double[] mousePosX = {0};
        double[] mousePosY = {0};
        int[] winWidth = {Window.getWidth()};
        int[] winHeight = {Window.getHeight()};

        // Get window properties and mouse position
        glfwGetWindowSize(glfwWindow, winWidth, winHeight);
        glfwGetFramebufferSize(glfwWindow, winWidth, winHeight);
        glfwGetCursorPos(glfwWindow, mousePosX, mousePosY);

        // We SHOULD call those methods to Update Dear ImGui state for the current frame
        ImGui.setNextWindowSize(winWidth[0], winHeight[0]);
        final ImGuiIO io = ImGui.getIO();
        io.setDisplaySize(winWidth[0], winHeight[0]);
        io.setDisplayFramebufferScale(1f, 1f);
        io.setMousePos((float) mousePosX[0], (float) mousePosY[0]);
        io.setDeltaTime(Time.deltaTime());

        // Update the mouse cursor
        final int imguiCursor = ImGui.getMouseCursor();
        glfwSetCursor(glfwWindow, mouseCursors[imguiCursor]);
        glfwSetInputMode(glfwWindow, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }

    @Override
    public void onImGUIUpdate() {
        startFrame();
        // Any IMGUI code should go between newFrame and render methods
        ImGui.newFrame();

        setupDockSpace();
        signalStartFrameListeners();
        endDockSpace();

        // After Dear ImGui prepared a draw data, we use it in the LWJGL3 renderer.
        // At that moment ImGui will be rendered to the current OpenGL context.
        ImGui.render();

        endFrame();
    }

    private void setupDockSpace() {
        int windowFlags =   ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking | ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize |
                            ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;

        ImGui.setNextWindowPos(0.0f, 0.0f, ImGuiCond.Always);
        ImGui.setNextWindowSize(Window.getWidth(), Window.getHeight(), ImGuiCond.Always);

        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);

        ImGui.begin("Dockspace Demo", new ImBoolean(true), windowFlags);
        ImGui.popStyleVar(2);

        // DockSpace
        ImGui.dockSpace(ImGui.getID("Dockspace"));
    }

    private void endDockSpace(){
        ImGui.end();
    }

    private void endFrame() {
        //glBindFramebuffer(GL_FRAMEBUFFER, 0);
        //glViewport(0, 0, Window.getWidth(), Window.getHeight());
        //glClearColor(1, 1, 1, 1);
        //glClear(GL_COLOR_BUFFER_BIT);

        imGuiGl3.renderDrawData(ImGui.getDrawData());

        long backupWindowPtr = glfwGetCurrentContext();
        //ImGui.updatePlatformWindows();
        //ImGui.renderPlatformWindowsDefault();
        glfwMakeContextCurrent(backupWindowPtr);
    }
    // endregion

    //region Init/Stop methods
    // Initialize Dear ImGui.
    private void initImGui() {
        // IMPORTANT!!
        // This line is critical for Dear ImGui to work.
        ImGui.createContext();

        //region Keybindings
        // ------------------------------------------------------------
        // Initialize ImGuiIO config
        final ImGuiIO io = ImGui.getIO();

        io.setIniFilename("ImGUI.ini"); // We don't want to save .ini file
        io.setConfigFlags(ImGuiConfigFlags.NavEnableKeyboard); // Navigation with keyboard
        io.setBackendFlags(ImGuiBackendFlags.HasMouseCursors); // Mouse cursors to display while resizing windows etc.
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
        io.setBackendPlatformName("imgui_java_impl_glfw");
        // ------------------------------------------------------------
        // Keyboard mapping. ImGui will use those indices to peek into the io.KeysDown[] array.

        final int[] keyMap = new int[ImGuiKey.COUNT];
        keyMap[ImGuiKey.Tab] = GLFW_KEY_TAB;
        keyMap[ImGuiKey.LeftArrow] = GLFW_KEY_LEFT;
        keyMap[ImGuiKey.RightArrow] = GLFW_KEY_RIGHT;
        keyMap[ImGuiKey.UpArrow] = GLFW_KEY_UP;
        keyMap[ImGuiKey.DownArrow] = GLFW_KEY_DOWN;
        keyMap[ImGuiKey.PageUp] = GLFW_KEY_PAGE_UP;
        keyMap[ImGuiKey.PageDown] = GLFW_KEY_PAGE_DOWN;
        keyMap[ImGuiKey.Home] = GLFW_KEY_HOME;
        keyMap[ImGuiKey.End] = GLFW_KEY_END;
        keyMap[ImGuiKey.Insert] = GLFW_KEY_INSERT;
        keyMap[ImGuiKey.Delete] = GLFW_KEY_DELETE;
        keyMap[ImGuiKey.Backspace] = GLFW_KEY_BACKSPACE;
        keyMap[ImGuiKey.Space] = GLFW_KEY_SPACE;
        keyMap[ImGuiKey.Enter] = GLFW_KEY_ENTER;
        keyMap[ImGuiKey.Escape] = GLFW_KEY_ESCAPE;
        keyMap[ImGuiKey.KeyPadEnter] = GLFW_KEY_KP_ENTER;
        keyMap[ImGuiKey.A] = GLFW_KEY_A;
        keyMap[ImGuiKey.C] = GLFW_KEY_C;
        keyMap[ImGuiKey.V] = GLFW_KEY_V;
        keyMap[ImGuiKey.X] = GLFW_KEY_X;
        keyMap[ImGuiKey.Y] = GLFW_KEY_Y;
        keyMap[ImGuiKey.Z] = GLFW_KEY_Z;
        io.setKeyMap(keyMap);
        //endregion

        // region Mouse Buttons
        // ------------------------------------------------------------
        // Mouse cursors mapping
        mouseCursors[ImGuiMouseCursor.Arrow] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.TextInput] = glfwCreateStandardCursor(GLFW_IBEAM_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeAll] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNS] = glfwCreateStandardCursor(GLFW_VRESIZE_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeEW] = glfwCreateStandardCursor(GLFW_HRESIZE_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNESW] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNWSE] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.Hand] = glfwCreateStandardCursor(GLFW_HAND_CURSOR);
        mouseCursors[ImGuiMouseCursor.NotAllowed] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);

        // endregion

        //region Callbacks
        // ------------------------------------------------------------
        // GLFW callbacks to handle user input

        Window.getGLFWEventHandler().glfwSetKeyCallback((w, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS) {
                io.setKeysDown(key, true);
            } else if (action == GLFW_RELEASE) {
                io.setKeysDown(key, false);
            }

            io.setKeyCtrl(io.getKeysDown(GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW_KEY_RIGHT_CONTROL));
            io.setKeyShift(io.getKeysDown(GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW_KEY_RIGHT_SHIFT));
            io.setKeyAlt(io.getKeysDown(GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW_KEY_RIGHT_ALT));
            io.setKeySuper(io.getKeysDown(GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW_KEY_RIGHT_SUPER));
        });

        Window.getGLFWEventHandler().glfwSetCharCallback((w, c) -> {
            if (c != GLFW_KEY_DELETE) {
                io.addInputCharacter(c);
            }
        });

        Window.getGLFWEventHandler().glfwSetMouseButtonCallback((w, button, action, mods) -> {
            final boolean[] mouseDown = new boolean[5];

            mouseDown[0] = button == GLFW_MOUSE_BUTTON_1 && action != GLFW_RELEASE;
            mouseDown[1] = button == GLFW_MOUSE_BUTTON_2 && action != GLFW_RELEASE;
            mouseDown[2] = button == GLFW_MOUSE_BUTTON_3 && action != GLFW_RELEASE;
            mouseDown[3] = button == GLFW_MOUSE_BUTTON_4 && action != GLFW_RELEASE;
            mouseDown[4] = button == GLFW_MOUSE_BUTTON_5 && action != GLFW_RELEASE;

            io.setMouseDown(mouseDown);

            if(!io.getWantCaptureMouse() && mouseDown[1]){
                ImGui.setWindowFocus(null);
            }

            if (!io.getWantCaptureMouse() && mouseDown[1]) {
                ImGui.setWindowFocus(null);
            }
        });

        Window.getGLFWEventHandler().glfwSetScrollCallback((w, xOffset, yOffset) -> {
            io.setMouseWheelH(io.getMouseWheelH() + (float) xOffset);
            io.setMouseWheel(io.getMouseWheel() + (float) yOffset);
        });

        io.setSetClipboardTextFn(new ImStrConsumer() {
            @Override
            public void accept(final String s) {
                glfwSetClipboardString(glfwWindow, s);
            }
        });

        io.setGetClipboardTextFn(new ImStrSupplier() {
            @Override
            public String get() {
                final String clipboardString = glfwGetClipboardString(glfwWindow);
                if (clipboardString != null) {
                    return clipboardString;
                } else {
                    return "";
                }
            }
        });
        //endregion

        // region Fonts
        // ------------------------------------------------------------
        // Fonts configuration
        // read: https://raw.githubusercontent.com/ocornut/imgui/master/docs/FONTS.txt
        final String defaultFontPath = "assets/fonts/segoeui.ttf";
        if (new File(defaultFontPath).isFile()) {
            final ImFontAtlas fontAtlas = io.getFonts();
            final ImFontConfig fontConfig = new ImFontConfig(); // Natively allocated object, should be explicitly destroyed

            // Glyphs could be added per-font as well as per config used globally like here
            fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesDefault());

            // Fonts merge example
            fontConfig.setPixelSnapH(true);
            fontAtlas.addFontFromFileTTF(defaultFontPath, 32, fontConfig);
            fontConfig.destroy(); // After all fonts were added we don't need this config more
        }

        // endregion

        // Method initializes LWJGL3 renderer.
        // This method SHOULD be called after you've initialized your ImGui configuration (fonts and so on).
        // ImGui context should be created as well.
        imGuiGl3.init(glslVersion);
        // Add to onUpdate loop only after init
        Window.addImGUIUpdateFrameListener(this);
    }

    // If you want to clean a room after yourself - do it by yourself
    public void destroyImGui() {
        Window.removeImGUIUpdateFrameListener(this);
        imGuiGl3.dispose();
        ImGui.destroyContext();
    }
    //endregion

    //region Static Events
    public static float getImGUIScale(){
        return 1f;
    }

    private static final ArrayList<IImGUIStartFrame> imGUIStartFrameListeners = new ArrayList<>();
    private void signalStartFrameListeners(){
        for (int i = imGUIStartFrameListeners.size() - 1; i >= 0; i--)
        {
            IImGUIStartFrame listener = imGUIStartFrameListeners.get(i);
            listener.render();
        }
    }

    public static void addStartFrameListener(IImGUIStartFrame listener){
        if(!imGUIStartFrameListeners.contains(listener)){
            imGUIStartFrameListeners.add(listener);
        }
    }

    public static void removeStartFrameListener(IImGUIStartFrame listener){
        imGUIStartFrameListeners.remove(listener);
    }
    //endregion
}


