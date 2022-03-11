package JDabria.Renderer;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

public class Texture {
    private String Filepath;
    private int TexID;

    public Texture(String _Filepath){
        Filepath = _Filepath;

        // Generate texture
        TexID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, TexID);

        // Set params - wrap image to repeat
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        // Point filter SHRINK + STRETCH
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        IntBuffer Width = BufferUtils.createIntBuffer(1);
        IntBuffer Height = BufferUtils.createIntBuffer(1);
        IntBuffer Channels = BufferUtils.createIntBuffer(1);

        ByteBuffer Image = stbi_load(Filepath, Width, Height, Channels, 0);
        if(Image == null){
            assert false : "ERROR: (Texture) -> Could not load image '" + Filepath + "'";
        }
        else{
            switch (Channels.get(0)){
                case 3:{
                    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, Width.get(0), Height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, Image);
                    break;
                }
                case 4:{
                    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, Width.get(0), Height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, Image);
                    break;
                }
                default:{
                    assert false : "ERROR: (Texture) -> Unexpected Texture channel count at '" + Filepath + "' with number of channels being: '"+ Channels.get(0) +"'";
                    break;
                }

            }

        }
        stbi_image_free(Image);
    }

    public void Bind(){
        glBindTexture(GL_TEXTURE_2D, TexID);
    }

    public void Unbind(){
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
