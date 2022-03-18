package jDabria.renderer.batcher;

import commons.Color;
import jDabria.assetManager.AssetPool;
import jDabria.assetManager.resources.ShaderBuilder;
import jDabria.assetManager.resources.Texture;
import jDabria.ECP.components.Sprite.SpriteRenderer;
import jDabria.ECP.components.Transform;
import jDabria.sceneManager.SceneManager;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch implements Comparable<RenderBatch>{

    //<editor-fold desc="Variables">
    //  Vertex
    //  ======
    //  Pos                             Color                       Tex coordinates     Tex Id
    //  float, float, float        float, float, float, float       float,float         float
    //
    private static final int POS_SIZE = 3;
    private static final int COLOR_SIZE = 4;
    private static final int TEX_COORDS_SIZE = 2;
    private static final int TEX_ID_SIZE = 1;

    private static final int POS_OFFSET = 0;
    private static final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private static final int TEX_COORD_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private static final int TEX_ID_OFFSET = TEX_COORD_OFFSET + TEX_COORDS_SIZE * Float.BYTES;

    private static final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE + TEX_COORDS_SIZE + TEX_ID_SIZE;
    private static final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private List<Texture> textures = new ArrayList<>();
    private SpriteRenderer[] spriteRenderers;
    private int numSprites = 0, vaoID, vboID, maxBatchSize;
    private boolean hasRoom = true;
    private float[] vertices;
    private int[] texSlots = {0, 1, 2, 3, 4, 5, 6, 7};
    private ShaderBuilder shader;
    private int zIndex;
    //</editor-fold>

    //<editor-fold desc="Constructors">

    /**
     *
     * @param maxBatchSize
     */
    private RenderBatch(int maxBatchSize, int zIndex){
        if(maxBatchSize <= 0){
            throw new IllegalArgumentException("Batch size cannot be negative or zero");
        }

        this.zIndex = zIndex;
        this.maxBatchSize = maxBatchSize;
        shader = AssetPool.getShader(AssetPool.DEFAULT_FALLBACK_SHADER);

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

        glVertexAttribPointer(2, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORD_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(3);
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
        SpriteRenderer spriteRenderer = spriteRenderers[index];

        // Determine offset --> 4 vert per spriteRenderer
        int offset = index * 4 * VERTEX_SIZE;

        Color color = spriteRenderer.getColor();
        Vector2f[] coordinates = spriteRenderer.getTexCoords();

        int texID = 0;
        if(spriteRenderer.getSprite() != null){
            for(int i = 0; i < textures.size(); i++){
                if(textures.get(i) == spriteRenderer.getTexture()){
                    texID = i;
                    break;
                }
            }
        }

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
            Transform transform = spriteRenderer.gameObject.transform;
            vertices[offset] = transform.position.x + (xAdd * spriteRenderer.getSprite().getWidth() * transform.scale.x);
            vertices[offset + 1] = transform.position.y + (yAdd * spriteRenderer.getSprite().getHeight() * transform.scale.y);
            vertices[offset + 2] = transform.position.z;

            // Load color
            vertices[offset + 3] = color.getRed();
            vertices[offset + 4] = color.getGreen();
            vertices[offset + 5] = color.getBlue();
            vertices[offset + 6] = color.getAlpha();

            // Load coordinates
            vertices[offset + 7] = coordinates[i].x;
            vertices[offset + 8] = coordinates[i].y;

            // Load ID
            vertices[offset + 9] = texID;

            offset += VERTEX_SIZE;
        }
    }
    //</editor-fold>

    //<editor-fold desc="Public methods">
    public boolean addSprite(@NotNull SpriteRenderer spriteRenderer){
        // If this texture isn't preset in the batch, and we have no more room
        if(     spriteRenderer.getSprite() != null &&
                !this.hasTexture(spriteRenderer.getTexture()) &&
                !this.hasTextureRoom()){
            return false;
        }

        // If batch is full
        if(!hasRoom() || (int)spriteRenderer.gameObject.transform.position.z != zIndex){
            return false;
        }

        int Index = numSprites;
        spriteRenderers[Index] = spriteRenderer;
        numSprites++;

        if(spriteRenderer.getSprite() != null && !textures.contains(spriteRenderer.getSprite())){
            textures.add(spriteRenderer.getTexture());
        }

        // add to local array
        loadVertexProperties(Index);

        hasRoom = !(numSprites >= maxBatchSize);
        return true;
    }

    public void render(){
        boolean rebufferData = false;
        for (int i = 0; i < numSprites; i++) {
            SpriteRenderer spr = spriteRenderers[i];
            if(spr == null || !spr.isDirty()){
                continue;
            }

            loadVertexProperties(i);
            spr.setClean();
            rebufferData = true;
        }

        if(rebufferData){
            // Re-buffer data
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
        }

        shader.use();
        shader.uploadMat4F("uProj", SceneManager.getActiveCamera().getProjMatrix());
        shader.uploadMat4F("uView", SceneManager.getActiveCamera().getViewMatrix());
        for (int i = 0; i < textures.size(); i++) {
            glActiveTexture(GL_TEXTURE0 + i);
            textures.get(i).bind();
        }
        shader.uploadIntArray("uTextures", texSlots);


        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, numSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        for (int i = 0; i < textures.size(); i++) {
            textures.get(i).unbind();
        }

        shader.detach();
    }

    public boolean hasRoom(){
        return this.hasRoom;
    }

    public boolean hasTextureRoom() {return textures.size() < 8;}

    public boolean hasTexture(Texture tex) { return textures.contains(tex); }

    public int getzIndex(){
        return this.zIndex;
    }

    /**
     *
     * @param maxBatchSize
     * @return
     * @throws IllegalArgumentException
     */
    public static @NotNull RenderBatch createBatch(int maxBatchSize, int zIndex){
        RenderBatch renderBatch = new RenderBatch(maxBatchSize, zIndex);
        return renderBatch;
    }

    @Override
    public int compareTo(@NotNull RenderBatch o) {
        return Integer.compare(this.zIndex, o.getzIndex());
    }
    //</editor-fold>
}
