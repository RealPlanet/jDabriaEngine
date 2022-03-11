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

    private SpriteRenderer[] Sprites;
    private int NumSprites = 0, VaoID, VboId, MaxBatchSize;
    private boolean HasRoom = true;
    private float[] Vertices;
    private ShaderBuilder Shader;

    //<editor-fold desc="Constructors">
    private RenderBatch(int MaxBatchSize){
        if(MaxBatchSize <= 0){
            throw new IllegalArgumentException("Batch size cannot be negative or zero");
        }


        this.MaxBatchSize = MaxBatchSize;
        this.Shader = new ShaderBuilder("Assets/Shaders/DefaultShaderDefinition.glsl");
        this.Shader.Compile();

        this.Sprites = new SpriteRenderer[MaxBatchSize];

        // 4 Vertices quads
        this.Vertices = new float[MaxBatchSize * 4 * VERTEX_SIZE];

        Init();
    }
    //</editor-fold>

    //<editor-fold desc="Private methods">
    private void Init(){
        //Generate and bind a Vertex Array Object - VAO
        VaoID = glGenVertexArrays();
        glBindVertexArray(VaoID);

        // Allocate space for vertices
        VboId = glGenBuffers();
        // GL_DYNAMIC_DRAW -> We don't have the drawing data yet
        glBindBuffer(GL_ARRAY_BUFFER, VboId);
        glBufferData(GL_ARRAY_BUFFER, Vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Create & upload indices buffer
        int EboID = glGenBuffers();
        int[] Indices = GenerateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, Indices, GL_STATIC_DRAW); // GL_STATIC_DRAW -> Never going to change indicies

        // Enable buffer pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);
    }

    private int @NotNull [] GenerateIndices(){
        // 6 Indices -> 3 per triangle (Quad)
        int[] Elements = new int[6 * MaxBatchSize];
        for(int i= 0; i < MaxBatchSize; i++){
            LoadElementIndices(Elements, i);
        }

        return Elements;
    }

    private void LoadElementIndices(int @NotNull [] Elements, int Index) {
        // 3, 2, 0, 0, 2, 1     7, 6, 4, 4, 6, 5 <-- Example
        int OffsetArrayIndex = 6 * Index;
        int Offset = 4 * Index;
        // Triangle 1
        Elements[OffsetArrayIndex] = Offset + 3;
        Elements[OffsetArrayIndex + 1] = Offset + 2;
        Elements[OffsetArrayIndex + 2] = Offset;

        // Triangle 2
        Elements[OffsetArrayIndex + 3] = Offset;
        Elements[OffsetArrayIndex + 4] = Offset + 2;
        Elements[OffsetArrayIndex + 5] = Offset + 1;
    }

    private void LoadVertexProperties(int Index){
        SpriteRenderer Sprite = Sprites[Index];

        // Determine Offset --> 4 vert per sprite
        int Offset = Index * 4 * VERTEX_SIZE;

        Color Color = Sprite.GetColor();
        // Add
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

            // Load Position
            Transform T = Sprite.GameObject.Transform;
            Vertices[Offset] = T.Position.x + (xAdd * T.Scale.x);
            Vertices[Offset + 1] = T.Position.y + (yAdd * T.Scale.y);
            Vertices[Offset + 2] = T.Position.z;

            //Load Color
            Vertices[Offset + 3] = Color.GetRed();
            Vertices[Offset + 4] = Color.GetGreen();
            Vertices[Offset + 5] = Color.GetBlue();
            Vertices[Offset + 6] = Color.GetAlpha();

            Offset += VERTEX_SIZE;
        }
    }
    //</editor-fold>

    //<editor-fold desc="Public methods">
    public void AddSprite(SpriteRenderer Spr){
        if(!HasRoom){
            return;
        }

        int Index = NumSprites;
        Sprites[Index] = Spr;
        NumSprites++;

        // Add to local array
        LoadVertexProperties(Index);
        if(NumSprites >= MaxBatchSize){
            HasRoom = false;
        }
    }

    public void Render(){
        // Re-buffer data
        glBindBuffer(GL_ARRAY_BUFFER, VboId);
        glBufferSubData(GL_ARRAY_BUFFER, 0, Vertices);

        Shader.Use();
        Shader.UploadMat4f("uProj", SceneManager.GetActiveCamera().GetProjMatrix());
        Shader.UploadMat4f("uView", SceneManager.GetActiveCamera().GetViewMatrix());

        glBindVertexArray(VaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, NumSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        Shader.Detach();
    }

    public boolean HasRoom(){
        return this.HasRoom;
    }

    //</editor-fold>

    /**
     *
     * @param MaxBatchSize
     * @return
     * @throws IllegalArgumentException
     */
    public static @NotNull RenderBatch CreateBatch(int MaxBatchSize){
        RenderBatch Batch = new RenderBatch(MaxBatchSize);
        return Batch;
    }
}
