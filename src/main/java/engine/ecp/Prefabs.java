package engine.ecp;

import engine.ecp.components.sprite.SpriteRenderer;
import engine.renderer.sprite.Sprite;

public class Prefabs {
    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY){
        GameObject obj = new GameObject("Spr Generated");
        SpriteRenderer renderer = new SpriteRenderer(sprite);
        renderer.setSpriteSize((int) sizeX, (int) sizeY);
        obj.addComponent(renderer);
        return obj;
    }
}
