package engine.assetmanager.resources;

import org.jetbrains.annotations.NotNull;
import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import static org.lwjgl.opengl.GL20.*;

public class ShaderBuilder {
    //region Static properties
    private transient static final Pattern SHADER_REGEX_PATTERN = Pattern.compile("(#type)( )+([a-zA-Z]+)");
    private transient static final String INVALID_TOKEN_ERROR = "Unexpected token '%s' in '%s'";
    private transient static final String IDENTICAL_TOKENS_ERROR = "Expected two different shaders (fragment and vertex), received same tokens -> '%s' and '%s'";
    //endregion

    //region Member variables
    private transient int vertexID, fragmentID, shaderProgram;
    private String vertexSource = "", fragmentSource = "";
    private final String filepath;
    private boolean beingUsed = false;
    //endregion

    //region Constructors
    /**
     * onInit Builder and read shader file
     * @param filepath filepath to *.glsl Shader file containing BOTH fragment and vertex shader
     */
    public ShaderBuilder(String filepath){
        this.filepath = filepath;

        try{
            String source = new String(Files.readAllBytes(Paths.get(this.filepath)));
            String[ ] splitString = SHADER_REGEX_PATTERN.split(source);
            String firstPattern;
            String secondPattern;
            int index;
            int EOL;

            // TODO :: Save type in the REGEX split to remove the need to search the entire source?
            //Find first shader piece
            index = source.indexOf("#type") + 6; // +6 to skip #type and white space right after
            EOL = source.indexOf("\r\n", index);

            firstPattern = source.substring(index, EOL).trim();

            // Find second shader piece
            index = source.indexOf("#type", EOL) + 6;
            EOL = source.indexOf("\r\n", index);

            secondPattern = source.substring(index, EOL).trim();

            if(firstPattern.equalsIgnoreCase(secondPattern)){
                throw new IOException(String.format(IDENTICAL_TOKENS_ERROR, firstPattern, secondPattern));
            }

            // TODO :: Figure out how to tell regex to not create an empty string in the first element of the split string array
            //region Classify shader code based on #type --> fragment or vertex
            switch(firstPattern){
                case "fragment":{
                    fragmentSource = splitString[1];
                    break;
                }
                case "vertex":{
                    vertexSource = splitString[1];
                    break;
                }
                default:{
                    throw new IOException(String.format(INVALID_TOKEN_ERROR, firstPattern, this.filepath));
                }
            }

            switch(secondPattern){
                case "fragment":{
                    fragmentSource = splitString[2];
                    break;
                }
                case "vertex":{
                    vertexSource = splitString[2];
                    break;
                }
                default:{
                    throw new IOException(String.format(INVALID_TOKEN_ERROR, firstPattern, this.filepath));
                }
            }
            //endregion
        }
        catch(IOException ex){
            ex.printStackTrace();
            assert false : "ERROR :: Could not open shader file: '"+ this.filepath + "'";
        }
    }
    //endregion

    //region Public usage methods
    public void compile(){
        /// ===
        /// compile & Link Shaders
        /// ===
        compileVertexShader();
        compileFragmentShader();
        linkShaderProgram();
    }

    public void use(){
        if(beingUsed) {
            return;
        }
        beingUsed = true;
        glUseProgram(shaderProgram);
    }

    public void detach(){
        // Not sure if detaching unused shaders has overhead, so im putting a fail early guard here
        if(!beingUsed){
            return;
        }

        beingUsed = false;
        glUseProgram(0);
    }
    //endregion

    //region Upload to shader methods
    public void uploadMat2f(String varName, @NotNull Matrix2f matrix2f){
        use();

        int varLocation = glGetUniformLocation(shaderProgram, varName);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
        matrix2f.get(buffer);
        glUniformMatrix2fv(varLocation, false, buffer);
    }

    public void uploadVec2f(String varName, @NotNull Vector2f vector2f){
        use();

        int varLocation = glGetUniformLocation(shaderProgram, varName);
        glUniform2f(varLocation, vector2f.x, vector2f.y);
    }

    public void uploadMat3f(String varName, @NotNull Matrix3f matrix){
        use();

        int varLocation = glGetUniformLocation(shaderProgram, varName);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(9);
        matrix.get(buffer);
        glUniformMatrix3fv(varLocation, false, buffer);
    }

    public void uploadVec3f(String varName, @NotNull Vector3f Vector){
        use();

        int varLocation = glGetUniformLocation(shaderProgram, varName);
        glUniform3f(varLocation, Vector.x, Vector.y, Vector.z);
    }

    public void uploadMat4F(String varName, @NotNull Matrix4f matrix){
        use();

        int varLocation = glGetUniformLocation(shaderProgram, varName);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        matrix.get(buffer);
        glUniformMatrix4fv(varLocation, false, buffer);
    }

    public void uploadVec4f(String varName, @NotNull Vector4f Vector){
        use();

        int varLocation = glGetUniformLocation(shaderProgram, varName);
        glUniform4f(varLocation, Vector.x, Vector.y, Vector.z, Vector.w);
    }

    public void uploadFloat(String varName, Float Value){
        use();

        int varLocation = glGetUniformLocation(shaderProgram, varName);
        glUniform1f(varLocation, Value);
    }

    public void uploadInt(String varName, Integer Value){
        use();

        int varLocation = glGetUniformLocation(shaderProgram, varName);
        glUniform1i(varLocation, Value);
    }

    public void uploadTexture(String varName, Integer Value){
        uploadInt(varName, Value);
    }

    public void uploadIntArray(String varName, int[] array){
        use();

        int varLocation = glGetUniformLocation(shaderProgram, varName);
        glUniform1iv(varLocation, array);
    }

    //endregion

    //region Compile & Link Shaders methods
    private void compileVertexShader(){
        //Load and compile vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // Pass shader source to GPU
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        //Check for errors
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int Length = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.err.printf("ERROR :: File: %s.\n\tFragment shader compilation failed.%n", filepath);
            System.err.println(glGetShaderInfoLog(vertexID, Length));
            assert false : "";
        }
    }
    private void compileFragmentShader(){
        //Load and compile frag shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass shader source to GPU
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);

        //Check for errors
        int success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int Length = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.err.printf("ERROR :: File: %s.\n\tFragment shader compilation failed.%n", filepath);
            System.err.println(glGetShaderInfoLog(fragmentID, Length));
            assert false : "";
        }
    }
    private void linkShaderProgram() {
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);

        int success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if(success == GL_FALSE){
            int Length = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.err.printf("ERROR :: File: %s.\n\t Shader Link process failed!.%n", filepath);
            System.err.println(glGetProgramInfoLog(shaderProgram, Length));
            assert false : "";
        }
    }
    //endregion
}
