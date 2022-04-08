package engine.renderer.debug;

import commons.Color;
import commons.util.logging.EngineLogger;
import engine.Window;
import engine.assetmanager.AssetPool;
import engine.assetmanager.resources.ShaderBuilder;
import engine.renderer.ShaderConstants;
import engine.scenemanager.SceneManager;
import org.joml.Vector3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * This class handles the drawing of debug information on the screen such as lines, shapes, colors, ecc...
 * BINDING NOTICE :: Z-Index is indipendent from non-debug objects and as such debug and non-debug objects will be rendered in the order they've been created.
 */
public class DebugDraw {
    private static final int MAX_LINES = 500;
    private static final float LINE_WIDTH = 2.5f;

    private static final List<Line2D> currentLines = new ArrayList<>();

    // 6 floats & 2 verts per line then
    private static final float[] vertexArray = new float[MAX_LINES * 6 * 2];
    private static ShaderBuilder lineShader = AssetPool.getShader("assets/shader/debug/debugLine2D.glsl");

    private static int VAO_ID;
    private static int VBO_ID;

    private static boolean wasInit = false;

    //region Initialization
    public static void init(){
        EngineLogger.log("Beginning debug drawer initialization!");
        if(wasInit){
            EngineLogger.logWarning("Debug drawer was already initialized!");
            return;
        }

        // Build VAO
        VAO_ID = glGenVertexArrays();
        glBindVertexArray(VAO_ID);

        // Build VBO & allocate mem
        VBO_ID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO_ID);
        glBufferData(GL_ARRAY_BUFFER, vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES,  3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glLineWidth(LINE_WIDTH);

        Window.addUpdateFrameListener(()->{
            renderDebugLines();
        });

        wasInit = true;
        EngineLogger.log("Debug drawer has been initialized!");
    }
    //endregion

    //region Rendering
    private static void renderDebugLines(){
        Arrays.fill(vertexArray, 0);
        for(int i = 0, vertIndex = 0; i < currentLines.size(); i++){
            for(int j = 0; j < 2; j++){
                Vector3f position = (j == 0)? currentLines.get(i).getOrigin() : currentLines.get(i).getEnd();
                Color color = currentLines.get(i).getColor();
                // Position
                vertexArray[vertIndex]      = position.x;
                vertexArray[vertIndex + 1]  = position.y;
                vertexArray[vertIndex + 2]  = position.z;

                // Color
                vertexArray[vertIndex + 3] = color.getRed();
                vertexArray[vertIndex + 4] = color.getGreen();
                vertexArray[vertIndex + 5] = color.getBlue();

                vertIndex += 6;
            }
        }

        glBindBuffer(GL_ARRAY_BUFFER, VBO_ID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertexArray, 0, currentLines.size() * 6 * 2));

        // Shader use
        lineShader.use();
        lineShader.uploadMat4f(ShaderConstants.projectionMatrix, SceneManager.getActiveCamera().getProjMatrix());
        lineShader.uploadMat4f(ShaderConstants.viewMatrix, SceneManager.getActiveCamera().getViewMatrix());

        // Bind VAO
        glBindVertexArray(VAO_ID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawArrays(GL_LINES, 0, currentLines.size() * 2);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        lineShader.detach();
    }
    //endregion

    //region Line2D Drawer
    public static void drawLine2D(Vector3f from, Vector3f to){
        drawLine2D(from, to, Color.BLUE, 1);
    }

    public static void drawLine2D(Vector3f from, Vector3f to, Color color){
        drawLine2D(from, to, color, 1);
    }

    public static void drawLine2D(Vector3f from, Vector3f to, Color color, float lifetime){
        addLine2D(new Line2D(from, to, color, lifetime));
    }

    public static void drawFrameLine2D(Vector3f from, Vector3f to, Color color, int frameLifetime){
        addLine2D(new FrameLine2D(from, to, color, frameLifetime));
    }

    private static void addLine2D(Line2D line){
        if(currentLines.size() >= MAX_LINES){
            return;
        }

        currentLines.add(0, line);
    }

    protected static void removeLine2D(Line2D line){
        currentLines.remove(line);
    }
    //endregion
}
