package JDabria.ECP.Components.Sprite;

import JDabria.AssetManager.Resources.Texture;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class SpriteSheet {
    private Texture parentTexture;
    private List<Sprite> sprites = new ArrayList<>();

    public SpriteSheet(Texture parentTexture, int spriteWidth, int spriteHeight, int numSprites, int spacing){
        this.parentTexture = parentTexture;
        int currentX = 0;
        int currentY = parentTexture.getHeight() - spriteHeight;

        for (int i = 0; i < numSprites; i++) {
            // Normalized coordinates
            float pHeight = (float)parentTexture.getHeight();
            float pWidth = (float)parentTexture.getWidth();
            float topY = (currentY + spriteHeight) / pHeight;
            float rightX = (currentX + spriteWidth) / pWidth;
            float leftX = currentX / pWidth;
            float bottomY = currentY / pHeight;

            Vector2f[] texCoords = {
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY)
            };

            Sprite sprite = new Sprite(parentTexture, texCoords);
            sprite.setSize((int)((topY - bottomY) * pHeight), (int)((rightX - leftX) * pWidth));

            sprites.add(sprite);
            currentX += spriteWidth + spacing;
            if(currentX >= parentTexture.getWidth()){
                currentX = 0;
                currentY -= spriteHeight + spacing;
            }
        }
    }

    public Sprite getSprite(int index){
        return sprites.get(index);
    }

}
