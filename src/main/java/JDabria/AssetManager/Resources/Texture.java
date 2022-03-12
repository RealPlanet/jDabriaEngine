package JDabria.AssetManager.Resources;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private String filepath;
    private int texID, width, height;

    public Texture(String filepath){
        this.filepath = filepath;

        // Generate texture
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        // Set params - wrap image to repeat
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        // Point filter SHRINK + STRETCH
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer channelsBuffer = BufferUtils.createIntBuffer(1);
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = stbi_load(this.filepath, widthBuffer, heightBuffer, channelsBuffer, 0);
        if(image == null){
            assert false : "ERROR: (Texture) -> Could not load image '" + this.filepath + "'";
        }
        else{
            width = widthBuffer.get(0);
            height = heightBuffer.get(0);

            switch (channelsBuffer.get(0)){
                case 3:{
                    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, widthBuffer.get(0), heightBuffer.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
                    break;
                }
                case 4:{
                    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, widthBuffer.get(0), heightBuffer.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
                    break;
                }
                default:{
                    assert false : "ERROR: (Texture) -> Unexpected Texture channel count at '" + this.filepath + "' with number of channels being: '"+ channelsBuffer.get(0) +"'";
                    break;
                }

            }

        }
        stbi_image_free(image);
    }

    public void bind(){
        glBindTexture(GL_TEXTURE_2D, texID);
    }

    public void unbind(){
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
