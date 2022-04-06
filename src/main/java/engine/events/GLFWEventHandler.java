package engine.events;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.*;

import java.util.ArrayList;

public class GLFWEventHandler {
    private final ArrayList<GLFWKeyCallbackI>             setKeyListeners = new ArrayList<>();
    private final ArrayList<GLFWCursorPosCallbackI>       cursorPosListeners = new ArrayList<>();
    private final ArrayList<GLFWMouseButtonCallbackI>     mouseButtonListeners = new ArrayList<>();
    private final ArrayList<GLFWScrollCallbackI>          mouseScrollListeners = new ArrayList<>();
    private final ArrayList<GLFWWindowSizeCallbackI>      windowSizeListeners = new ArrayList<>();
    private final ArrayList<GLFWCharCallbackI>            charCallbacks = new ArrayList<>();

    private final long windowPtr;

    public GLFWEventHandler(long windowPtr){
        this.windowPtr = windowPtr;

        GLFW.glfwSetKeyCallback(this.windowPtr, (window, key, scanCode, action, mods) ->{
            if(windowPtr != window){
                return; // Not sure if callbacks for different windows are set to all listeners.
            }

            for(GLFWKeyCallbackI listeners : setKeyListeners){
                listeners.invoke(window, key, scanCode, action, mods);
            }
        });

        GLFW.glfwSetCursorPosCallback(this.windowPtr, (window, xPos, yPos) ->{
            if(windowPtr != window){
                return; // Not sure if callbacks for different windows are set to all listeners.
            }

            for(GLFWCursorPosCallbackI listeners : cursorPosListeners){
                listeners.invoke(window, xPos, yPos);
            }
        });

        GLFW.glfwSetMouseButtonCallback(this.windowPtr, (window, button, action, mods) ->{
            if(windowPtr != window){
                return; // Not sure if callbacks for different windows are set to all listeners.
            }

            for(GLFWMouseButtonCallbackI listeners : mouseButtonListeners){
                listeners.invoke(window, button, action, mods);
            }
        });

        GLFW.glfwSetScrollCallback(this.windowPtr, (window, xOffset, yOffset)->{
            if(windowPtr != window){
                return; // Not sure if callbacks for different windows are set to all listeners.
            }

            for(GLFWScrollCallbackI listeners : mouseScrollListeners){
                listeners.invoke(window, xOffset, yOffset);
            }
        });

        GLFW.glfwSetWindowSizeCallback(this.windowPtr, (window, nWidth, nHeight)->{
            if(windowPtr != window){
                return; // Not sure if callbacks for different windows are set to all listeners.
            }

            for(GLFWWindowSizeCallbackI listeners : windowSizeListeners){
                listeners.invoke(window, nWidth, nHeight);
            }
        });

        GLFW.glfwSetCharCallback(this.windowPtr, (window, nChar)->{
            if(windowPtr != window){
                return; // Not sure if callbacks for different windows are set to all listeners.
            }

            for(GLFWCharCallbackI listeners : charCallbacks){
                listeners.invoke(window, nChar);
            }
        });
    }

    public boolean glfwSetKeyCallback(@Nullable GLFWKeyCallbackI callback){
        if(setKeyListeners.contains(callback)){
            setKeyListeners.remove(callback);
            return false;
        }

        setKeyListeners.add(callback);
        return true;
    }

    public boolean glfwSetCursorPosCallback(@Nullable GLFWCursorPosCallbackI callback){
        if(cursorPosListeners.contains(callback)){
            cursorPosListeners.remove(callback);
            return false;
        }

        cursorPosListeners.add(callback);
        return true;
    }

    public boolean glfwSetMouseButtonCallback(@Nullable GLFWMouseButtonCallbackI callback){
        if(mouseButtonListeners.contains(callback)){
            mouseButtonListeners.remove(callback);
            return false;
        }

        mouseButtonListeners.add(callback);
        return true;
    }

    public boolean glfwSetScrollCallback(@Nullable GLFWScrollCallbackI callback){
        if(mouseScrollListeners.contains(callback)){
            mouseScrollListeners.remove(callback);
            return false;
        }

        mouseScrollListeners.add(callback);
        return true;
    }

    public boolean glfwSetWindowSizeCallback(@Nullable GLFWWindowSizeCallbackI callback){
        if(windowSizeListeners.contains(callback)){
            windowSizeListeners.remove(callback);
            return false;
        }

        windowSizeListeners.add(callback);
        return true;
    }

    public boolean glfwSetCharCallback(@Nullable GLFWCharCallbackI callback){
        if(charCallbacks.contains(callback)){
            charCallbacks.remove(callback);
            return false;
        }

        charCallbacks.add(callback);
        return true;
    }
}
