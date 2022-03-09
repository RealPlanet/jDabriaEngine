package JAssets.Scenes;

import JDabria.SceneManager.Scene;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditor extends Scene {

    //region Shader stuff
    // TODO :: Move to shader manager + read shader code from glsl
    private String VertexShaderSrc = "#version 330 core\n" +
            "// aSomething -> is an attribute\n" +
            "// fSomething -> used by fragment shader\n" +
            "\n" +
            "layout (location=0) in vec3 aPos;\n" +
            "layout (location=1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main(){\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}";

    private String FragmentShaderSrc = "#version 330\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main(){\n" +
            "    color = fColor;\n" +
            "}";

    private int VertexID, FragmentID, ShaderProgram;
    private float[] VertexArray = {
            // Position                 // Color
            0.5f, -0.5f, 0.0f,      1.0f, 0.0f, 0.0f, 1.0f, //Bottom right
            -0.5f, 0.5f, 0.0f,      0.0f, 1.0f, 0.0f, 1.0f, // Top left
            0.5f, 0.5f, 0.0f,       0.0f, 0.0f, 1.0f, 1.0f, // Top right
            -0.5f, -0.5f, 0.0f,     1.0f, 1.0f, 0.0f, 1.0f, //Bottom left
    };

    //IMPORTANT: Expects counter-clockwise order
    private int[] ElementArray = {
            2, 1, 0, // Top right triangle
            0, 1, 3, // Bottom left triangle
    };

    private int VaoID, VboID, EboID;
    //endregion

    public LevelEditor(){
        System.out.println("Inside Level editor!");
    }

    @Override
    public void OnFrameUpdate() {
        //Bind shader Program
        glUseProgram(ShaderProgram);

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
        glUseProgram(0);
    }

    // TODO :: Move to shader manager + read shader code from glsl
    @Override
    public void Init() {
        /// ===
        /// Compile & Link Shaders
        /// ===
        LoadVertexShader();
        LoadFragmentShader();
        LinkShaderProgram();

        //Generate VAO, VBO, EBO -> Send to GPU
        VaoID = glGenVertexArrays();
        glBindVertexArray(VaoID);

        // Float buffer of vertices
        FloatBuffer VertBuffer = BufferUtils.createFloatBuffer(VertexArray.length);
        VertBuffer.put(VertexArray).flip();

        // Create VBO -> upload the vBuffer
        VboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VboID);
        glBufferData(GL_ARRAY_BUFFER, VertBuffer, GL_STATIC_DRAW);

        // Create the indices -> upload
        IntBuffer ElementBuffer = BufferUtils.createIntBuffer(ElementArray.length);
        ElementBuffer.put(ElementArray).flip();
        EboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, ElementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int PositionSize = 3; // x, y ,z
        int ColorSize = 4; // RGB-A
        int FloatBytes = 4;
        int VertexBytes = (PositionSize + ColorSize) * FloatBytes;
        glVertexAttribPointer(0, PositionSize, GL_FLOAT, false, VertexBytes, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, ColorSize, GL_FLOAT, false, VertexBytes, PositionSize * FloatBytes);
        glEnableVertexAttribArray(1);
    }

    private void LinkShaderProgram() {
        ShaderProgram = glCreateProgram();
        glAttachShader(ShaderProgram, VertexID);
        glAttachShader(ShaderProgram, FragmentID);
        glLinkProgram(ShaderProgram);

        int Success = glGetProgrami(ShaderProgram, GL_LINK_STATUS);
        if(Success == GL_FALSE){
            int Length = glGetProgrami(ShaderProgram, GL_INFO_LOG_LENGTH);
            System.err.println("ERROR :: defaultshader.glsl.\n\tShader LINK failed.");
            System.err.println(glGetProgramInfoLog(ShaderProgram, Length));
            assert false : "";
        }
    }

    private void LoadVertexShader(){
        //Load and compile vertex shader
        VertexID = glCreateShader(GL_VERTEX_SHADER);
        // Pass shader source to GPU
        glShaderSource(VertexID, VertexShaderSrc);
        glCompileShader(VertexID);

        //Check for errors
        int Success = glGetShaderi(VertexID, GL_COMPILE_STATUS);
        if(Success == GL_FALSE){
            int Length = glGetShaderi(VertexID, GL_INFO_LOG_LENGTH);
            System.err.println("ERROR :: defaultshader.glsl.\n\tVertex shader compilation failed.");
            System.err.println(glGetShaderInfoLog(VertexID, Length));
            assert false : "";
        }
    }

    private void LoadFragmentShader(){
        //Load and compile frag shader
        FragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass shader source to GPU
        glShaderSource(FragmentID, FragmentShaderSrc);
        glCompileShader(FragmentID);

        //Check for errors
        int Success = glGetShaderi(FragmentID, GL_COMPILE_STATUS);
        if(Success == GL_FALSE){
            int Length = glGetShaderi(FragmentID, GL_INFO_LOG_LENGTH);
            System.err.println("ERROR :: defaultshader.glsl.\n\tFragment shader compilation failed.");
            System.err.println(glGetShaderInfoLog(FragmentID, Length));
            assert false : "";
        }
    }
}
