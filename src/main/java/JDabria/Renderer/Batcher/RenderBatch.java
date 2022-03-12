package JDabria.Renderer.Batcher;

import Commons.Color;
import JDabria.AssetManager.Resources.ShaderBuilder;
import JDabria.ECP.Components.SpriteRenderer;
import JDabria.ECP.Components.Transform;
import JDabria.SceneManager.SceneManager;
import org.jetbrains.annotations.NotNull;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch {
    //  Vertex
    //  ======
    //  Pos                 Color
    //  float, float, float        float, float, float, float
    //
    private static final int POS_SIZE = 3;
    private static final int COLOR_SIZE = 4;
    private static final int POS_OFFSET = 0;
    private static final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE;

    private static final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private static final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRenderer[] spriteRenderers;
    private int numSprites = 0, vaoID, vboID, maxBatchSize;
    private boolean hasRoom = true;
    private float[] vertices;
    private ShaderBuilder shader;

    //<editor-fold desc="Constructors">
    private RenderBatch(int maxBatchSize){
        if(maxBatchSize <= 0){
            throw new IllegalArgumentException("Batch size cannot be negative or zero");
        }


        this.maxBatchSize = maxBatchSize;
        this.shader = new ShaderBuilder("Assets/Shaders/DefaultShaderDefinition.glsl");
        this.shader.compile();

        this.spriteRenderers = new SpriteRenderer[maxBatchSize];

        // 4 vertices quads
        this.vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];

        init();
    }
    //</editor-fold>

    //<editor-fold desc="Private methods">
    private void init(){
        //Generate and bind a Vertex Array Object - VAO
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Allocate space for vertices
        vboID = glGenBuffers();
        // GL_DYNAMIC_DRAW -> We don't have the drawing data yet
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Create & upload indices buffer
        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW); // GL_STATIC_DRAW -> Never going to change indicies

        // Enable buffer pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);
    }

    private int @NotNull [] generateIndices(){
        // 6 Indices -> 3 per triangle (Quad)
        int[] elements = new int[6 * maxBatchSize];
        for(int i = 0; i < maxBatchSize; i++){
            loadElementIndices(elements, i);
        }

        return elements;
    }

    private void loadElementIndices(int @NotNull [] elements, int index) {
        // 3, 2, 0, 0, 2, 1     7, 6, 4, 4, 6, 5 <-- Example
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;
        // Triangle 1
        elements[offsetArrayIndex] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset;

        // Triangle 2
        elements[offsetArrayIndex + 3] = offset;
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;
    }

    private void loadVertexProperties(int index){
        SpriteRenderer sprite = spriteRenderers[index];

        // Determine offset --> 4 vert per sprite
        int offset = index * 4 * VERTEX_SIZE;

        Color color = sprite.getColor();
        // add
        float xAdd = 1.0f, yAdd = 1.0f;
        for(int i = 0; i < 4; i++){

            if(i == 1){
                yAdd = 0.0f;
            }
            else if (i == 2){
                xAdd = 0.0f;
            }
            else if(i == 3){
                yAdd = 1.0f;
            }

            // Load position
            Transform transform = sprite.gameObject.transform;
            vertices[offset] = transform.position.x + (xAdd * transform.scale.x);
            vertices[offset + 1] = transform.position.y + (yAdd * transform.scale.y);
            vertices[offset + 2] = transform.position.z;

            //Load color
            vertices[offset + 3] = color.getRed();
            vertices[offset + 4] = color.getGreen();
            vertices[offset + 5] = color.getBlue();
            vertices[offset + 6] = color.getAlpha();

            offset += VERTEX_SIZE;
        }
    }
    //</editor-fold>

    //<editor-fold desc="Public methods">
    public void addSprite(SpriteRenderer spriteRenderer){
        if(!hasRoom){
            return;
        }

        int Index = numSprites;
        spriteRenderers[Index] = spriteRenderer;
        numSprites++;

        // add to local array
        loadVertexProperties(Index);
        if(numSprites >= maxBatchSize){
            hasRoom = false;
        }
    }

    public void render(){
        // Re-buffer data
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        shader.use();
        shader.uploadMat4F("uProj", SceneManager.getActiveCamera().getProjMatrix());
        shader.uploadMat4F("uView", SceneManager.getActiveCamera().getViewMatrix());

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, numSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        shader.detach();
    }

    public boolean hasRoom(){
        return this.hasRoom;
    }

    //</editor-fold>

    /**
     *
     * @param maxBatchSize
     * @return
     * @throws IllegalArgumentException
     */
    public static @NotNull RenderBatch createBatch(int maxBatchSize){
        RenderBatch renderBatch = new RenderBatch(maxBatchSize);
        return renderBatch;
    }
}
