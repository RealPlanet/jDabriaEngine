package engine.renderer;

import commons.util.logging.EngineLogger;
import engine.assetmanager.resources.Texture;

import static org.lwjgl.opengl.GL30.*;

public class Framebuffer {
    private int fboID;
    private Texture texture;

    public Framebuffer(int width, int height){
        //Generate frabebuffer
        fboID = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fboID);

        // Create the texture to render data to -> attach to framebuffer
        texture = new Texture(width, height);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture.getTexID(), 0);

        // Create renderbuffer -> store depth
        int rboID = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, rboID);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32, width, height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboID);

        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE){
            EngineLogger.logError("Frame Buffer is not complete");
            assert false: "Error: Frame buffer is not completed";
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public int getFBOId(){
        return fboID;
    }

    public Texture getTexture(){
        return texture;
    }

    public void bindBuffer(){
        glBindFramebuffer(GL_FRAMEBUFFER, fboID);
    }

    public void unbindBuffer(){
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
}
