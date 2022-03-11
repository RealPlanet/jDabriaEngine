package JDabria.AssetManager.Resources;

import org.jetbrains.annotations.NotNull;
import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class ShaderBuilder {
    private static final Pattern SHADER_REGEX_PATTERN = Pattern.compile("(#type)( )+([a-zA-Z]+)");
    private static final String INVALID_TOKEN_ERROR = "Unexpected token '%s' in '%s'";
    private static final String IDENTICAL_TOKENS_ERROR = "Expected two different shaders (fragment and vertex), received same tokens -> '%s' and '%s'";

    private int VertexID, FragmentID, ShaderProgram;
    private String VertexSource = "", FragmentSource = "";
    private final String Filepath;
    private boolean BeingUsed = false;

    /**
     * Init Builder and read shader file
     * @param _Filepath Filepath to *.glsl Shader file containing BOTH fragment and vertex shader
     */
    public ShaderBuilder(String _Filepath){
        Filepath = _Filepath;

        try{
            String Source = new String(Files.readAllBytes(Paths.get(Filepath)));
            String[ ] SplitString = SHADER_REGEX_PATTERN.split(Source);
            String FirstPattern;
            String SecondPattern;
            int Index;
            int EOL;

            // TODO :: Save type in the REGEX split to remove the need to search the entire source?
            //Find first shader piece
            Index = Source.indexOf("#type") + 6; // +6 to skip #type and white space right after
            EOL = Source.indexOf("\r\n", Index);

            FirstPattern = Source.substring(Index, EOL).trim();

            // Find second shader piece
            Index = Source.indexOf("#type", EOL) + 6;
            EOL = Source.indexOf("\r\n", Index);

            SecondPattern = Source.substring(Index, EOL).trim();

            if(FirstPattern.equalsIgnoreCase(SecondPattern)){
                throw new IOException(String.format(IDENTICAL_TOKENS_ERROR, FirstPattern, SecondPattern));
            }

            // TODO :: Figure out how to tell regex to not create an empty string in the first element of the split string array
            //region Classify shader code based on #type --> fragment or vertex
            switch(FirstPattern){
                case "fragment":{
                    FragmentSource = SplitString[1];
                    break;
                }
                case "vertex":{
                    VertexSource = SplitString[1];
                    break;
                }
                default:{
                    throw new IOException(String.format(INVALID_TOKEN_ERROR, FirstPattern, Filepath));
                }
            }

            switch(SecondPattern){
                case "fragment":{
                    FragmentSource = SplitString[2];
                    break;
                }
                case "vertex":{
                    VertexSource = SplitString[2];
                    break;
                }
                default:{
                    throw new IOException(String.format(INVALID_TOKEN_ERROR, FirstPattern, Filepath));
                }
            }
            //endregion
        }
        catch(IOException ex){
            ex.printStackTrace();
            assert false : "ERROR :: Could not open shader file: '"+ Filepath + "'";
        }
    }

    public void Compile(){
        /// ===
        /// Compile & Link Shaders
        /// ===
        CompileVertexShader();
        CompileFragmentShader();
        LinkShaderProgram();
    }

    public void Use(){
        if(BeingUsed) {
            return;
        }
        BeingUsed = true;
        glUseProgram(ShaderProgram);
    }

    public void Detach(){
        // Not sure if detaching unused shaders has overhead, so im putting a fail early guard here
        if(!BeingUsed){
            return;
        }

        BeingUsed = false;
        glUseProgram(0);
    }

    //region Upload to shader methods
    public void UploadMat2f(String VarName, @NotNull Matrix2f Matrix){
        Use();

        int VarLocation = glGetUniformLocation(ShaderProgram, VarName);
        FloatBuffer Buffer = BufferUtils.createFloatBuffer(4);
        Matrix.get(Buffer);
        glUniformMatrix2fv(VarLocation, false, Buffer);
    }

    public void UploadVec2f(String VarName, @NotNull Vector2f Vector){
        Use();

        int VarLocation = glGetUniformLocation(ShaderProgram, VarName);
        glUniform2f(VarLocation, Vector.x, Vector.y);
    }

    public void UploadMat3f(String VarName, @NotNull Matrix3f Matrix){
        Use();

        int VarLocation = glGetUniformLocation(ShaderProgram, VarName);
        FloatBuffer Buffer = BufferUtils.createFloatBuffer(9);
        Matrix.get(Buffer);
        glUniformMatrix3fv(VarLocation, false, Buffer);
    }

    public void UploadVec3f(String VarName, @NotNull Vector3f Vector){
        Use();

        int VarLocation = glGetUniformLocation(ShaderProgram, VarName);
        glUniform3f(VarLocation, Vector.x, Vector.y, Vector.z);
    }

    public void UploadMat4f(String VarName, @NotNull Matrix4f Matrix){
        Use();

        int VarLocation = glGetUniformLocation(ShaderProgram, VarName);
        FloatBuffer Buffer = BufferUtils.createFloatBuffer(16);
        Matrix.get(Buffer);
        glUniformMatrix4fv(VarLocation, false, Buffer);
    }

    public void UploadVec4f(String VarName, @NotNull Vector4f Vector){
        Use();

        int VarLocation = glGetUniformLocation(ShaderProgram, VarName);
        glUniform4f(VarLocation, Vector.x, Vector.y, Vector.z, Vector.w);
    }

    public void UploadFloat(String VarName, Float Value){
        Use();

        int VarLocation = glGetUniformLocation(ShaderProgram, VarName);
        glUniform1f(VarLocation, Value);
    }

    public void UploadInt(String VarName, Integer Value){
        Use();

        int VarLocation = glGetUniformLocation(ShaderProgram, VarName);
        glUniform1i(VarLocation, Value);
    }

    public void UploadTexture(String VarName, Integer Value){
        UploadInt(VarName, Value);
    }
    //endregion

    //region Compile & Link Shaders
    private void CompileVertexShader(){
        //Load and compile vertex shader
        VertexID = glCreateShader(GL_VERTEX_SHADER);
        // Pass shader source to GPU
        glShaderSource(VertexID, VertexSource);
        glCompileShader(VertexID);

        //Check for errors
        int Success = glGetShaderi(VertexID, GL_COMPILE_STATUS);
        if(Success == GL_FALSE){
            int Length = glGetShaderi(VertexID, GL_INFO_LOG_LENGTH);
            System.err.printf("ERROR :: File: %s.\n\tFragment shader compilation failed.%n", Filepath);
            System.err.println(glGetShaderInfoLog(VertexID, Length));
            assert false : "";
        }
    }
    private void CompileFragmentShader(){
        //Load and compile frag shader
        FragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass shader source to GPU
        glShaderSource(FragmentID, FragmentSource);
        glCompileShader(FragmentID);

        //Check for errors
        int Success = glGetShaderi(FragmentID, GL_COMPILE_STATUS);
        if(Success == GL_FALSE){
            int Length = glGetShaderi(FragmentID, GL_INFO_LOG_LENGTH);
            System.err.printf("ERROR :: File: %s.\n\tFragment shader compilation failed.%n", Filepath);
            System.err.println(glGetShaderInfoLog(FragmentID, Length));
            assert false : "";
        }
    }
    private void LinkShaderProgram() {
        ShaderProgram = glCreateProgram();
        glAttachShader(ShaderProgram, VertexID);
        glAttachShader(ShaderProgram, FragmentID);
        glLinkProgram(ShaderProgram);

        int Success = glGetProgrami(ShaderProgram, GL_LINK_STATUS);
        if(Success == GL_FALSE){
            int Length = glGetProgrami(ShaderProgram, GL_INFO_LOG_LENGTH);
            System.err.printf("ERROR :: File: %s.\n\t Shader Link process failed!.%n", Filepath);
            System.err.println(glGetProgramInfoLog(ShaderProgram, Length));
            assert false : "";
        }
    }
    //endregion
}
