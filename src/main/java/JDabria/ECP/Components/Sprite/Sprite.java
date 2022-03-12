package JDabria.ECP.Components.Sprite;

import JDabria.AssetManager.Resources.Texture;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

/**
 * Holds all the information required to render an image
 * It stores a texture and allows dynamic resizing of it's size without touching other objects that share the texture
 */
public class Sprite {
    private int height, width;

    private Texture texture;
    private Vector2f[] texCoords = {
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),
            new Vector2f(0, 1)
    };

    /**
     * Creates a sprite with the given texture, by default it's size will be the same as the texture in pixel
     * @param texture the texture this sprite will use
     */
    public Sprite(@NotNull Texture texture){
        setSize(texture.getHeight(), texture.getWidth());
        this.texture = texture;
    }

    /**
     * Creates a sprite with the given texture, by default it's size will be the same as the texture in pixel.
     * In addition it allows to specify custom texture coordinates.
     * @param texture the texture this sprite will use
     * @param texCoords the coordinates this sprite will use on the texture
     */
    public Sprite(Texture texture, Vector2f[] texCoords){
        this(texture);
        this.texCoords = texCoords;
    }

    /**
     * Gets the texture associated with this sprite
     * @return Texture
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * Gets the coordinates associated with this sprite
     * @return The texture coordinates
     */
    public Vector2f[] getTexCoords() {
        return texCoords;
    }

    /**
     * Gets the sprite height
     * @return The height in pixels
     */
    public int getHeight(){
        return height;
    }

    /**
     * Gets the sprite width
     * @return The width in pixels
     */
    public int getWidth(){
        return width;
    }

    /**
     * Reset the sprite size to match the texture
     */
    public void resetSize(){
        height = getTexture().getHeight();
        width = getTexture().getWidth();
    }

    /**
     * Warps the stripe to match the new size
     * @param h Height dimension in pixels
     * @param w Width dimension in pixels
     */
    public void setSize(int h, int w){
        height = h;
        width = w;
    }
}
