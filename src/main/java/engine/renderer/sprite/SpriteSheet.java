package engine.renderer.sprite;

import commons.util.StringUtils;
import engine.assetmanager.AssetPool;
import engine.assetmanager.resources.Texture;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SpriteSheet {
    @SuppressWarnings("FieldCanBeLocal")
    private final Texture parentTexture;
    private final List<Sprite> sprites = new ArrayList<>();

    public SpriteSheet(@NotNull Texture parentTexture, int spriteWidth, int spriteHeight, int numSprites, int spacing){
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
            sprite.setSize(spriteWidth, spriteHeight);

            sprites.add(sprite);
            currentX += spriteWidth + spacing;
            if(currentX >= parentTexture.getWidth()){
                currentX = 0;
                currentY -= spriteHeight + spacing;
            }
        }

        AssetPool.addSpriteSheet(parentTexture.getFilepath(), this);
    }

    @SuppressWarnings("unused")
    public Sprite getSprite(int index){
        return sprites.get(index);
    }

    @SuppressWarnings("unused")
    public String getFilepath() {return parentTexture.getFilepath();}

    public String getParentTextureName(){
        return StringUtils.getFileName(Paths.get(parentTexture.getFilepath()).toFile());
    }

    public int size(){
        return sprites.size();
    }
}
