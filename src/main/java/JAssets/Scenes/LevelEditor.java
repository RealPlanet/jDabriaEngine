package JAssets.Scenes;

import Commons.Time;
import JDabria.ECP.Components.FontRenderer;
import JDabria.ECP.Components.SpriteRenderer;
import JDabria.ECP.GameObject;
import JDabria.Renderer.Camera;
import JDabria.Renderer.ShaderBuilder;
import JDabria.Renderer.Texture;
import JDabria.SceneManager.Scene;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditor extends Scene {

    private Texture DefaultTexture;

    //region Shader stuff
    private ShaderBuilder DefaultShader;

    private final float[] VertexArray = {
            // Position                 // Color                     //UV
            500.5f, 0.5f, 0.0f,         1.0f, 0.0f, 0.0f, 1.0f,      1, 1, //Bottom right
            0.5f, 500.5f, 0.0f,         0.0f, 1.0f, 0.0f, 1.0f,      0, 0, // Top left
            500.5f, 500.5f, 0.0f,       0.0f, 0.0f, 1.0f, 1.0f,      1, 0, // Top right
            0.5f, 0.5f, 0.0f,           1.0f, 1.0f, 0.0f, 1.0f,      0, 1, //Bottom left
    };

    //IMPORTANT: Expects counter-clockwise order
    private final int[] ElementArray = {
            2, 1, 0, // Top right triangle
            0, 1, 3, // Bottom left triangle
    };

    private int VaoID;
    //endregion

    public LevelEditor(){
        System.out.println("Inside Level editor!");
    }

    @Override
    public void Init() {
        Camera = new Camera(new Vector3f(0.0f, 0.0f, 0.0f));
        DefaultShader = new ShaderBuilder("Assets/Shaders/DefaultShaderDefinition.glsl");
        DefaultTexture = new Texture("Assets/Textures/bepu.png");

        System.out.println("Creating test gameobject");
        GameObject testObject = new GameObject("TEST");
        testObject.AddComponent(new SpriteRenderer());
        testObject.AddComponent(new FontRenderer());
        AddGameObjectToScene(testObject);

        DefaultShader.Compile();

        //Generate VAO, VBO, EBO -> Send to GPU
        VaoID = glGenVertexArrays();
        glBindVertexArray(VaoID);

        // Float buffer of vertices
        FloatBuffer VertBuffer = BufferUtils.createFloatBuffer(VertexArray.length);
        VertBuffer.put(VertexArray).flip();

        // Create VBO -> upload the vBuffer
        int vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, VertBuffer, GL_STATIC_DRAW);

        // Create the indices -> upload
        IntBuffer ElementBuffer = BufferUtils.createIntBuffer(ElementArray.length);
        ElementBuffer.put(ElementArray).flip();
        int eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, ElementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int PositionSize = 3; // x, y ,z
        int ColorSize = 4; // RGB-A
        int UVSize = 2;
        int VertexBytes = (PositionSize + ColorSize + UVSize) * Float.BYTES;

        glVertexAttribPointer(0, PositionSize, GL_FLOAT, false, VertexBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, ColorSize, GL_FLOAT, false, VertexBytes, PositionSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, UVSize, GL_FLOAT, false, VertexBytes, (PositionSize + ColorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void OnFrameUpdate() {
        assert Camera != null;
        Vector3f CamPosition = Camera.GetPosition();

        Camera.SetPosition(new Vector3f(CamPosition.x - Time.DeltaTime() * 50f, CamPosition.y, CamPosition.z));

        //Bind shader Program
        DefaultShader.Use();
        DefaultShader.UploadTexture("TEX_MAIN_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        DefaultTexture.Bind();

        DefaultShader.UploadMat4f("uProj", Camera.GetProjMatrix());
        DefaultShader.UploadMat4f("uView", Camera.GetViewMatrix());
        DefaultShader.UploadFloat("uTime", Time.Time());

        //Bind VAO
        glBindVertexArray(VaoID);

        //Enable Vert attribute ptrs
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, ElementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        DefaultShader.Detach();

        for ( GameObject go : GameObjects ) {
            go.Update();
        }
    }
}
