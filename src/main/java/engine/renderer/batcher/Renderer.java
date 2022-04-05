package engine.renderer.batcher;

import engine.ecp.components.sprite.SpriteRenderer;
import engine.ecp.GameObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Handles the collection and rendering of sprites in single multiple batches to reduce the number of draw calls
 */
public class Renderer {
    private static final int MAX_BATCH_SIZE = 1000;
    private static final List<RenderBatch> RENDER_BATCHES = new ArrayList<>();

    public Renderer(){
    }

    /**
     * Adds a SpriteRenderer to a batch from a game object, if no batch is available a new one is created
     * @param go The game object with a SpriteRenderer component attached
     */
    public void add(@NotNull GameObject go){
        SpriteRenderer component = go.getComponent(SpriteRenderer.class);
        if(component == null){
            return;
        }

        add(component);
    }

    /**
     * Adds a SpriteRenderer to a batch, Internally called from {@link #add(GameObject)}
     * @param spriteRenderer The SpriteRenderer component
     */
    private void add(SpriteRenderer spriteRenderer){
        boolean addedToBatch = false;
        for(RenderBatch renderBatch : RENDER_BATCHES){
            addedToBatch = renderBatch.addSprite(spriteRenderer);
            if(addedToBatch){
                break;
            }
        }

        if(addedToBatch){
           return;
        }

        RenderBatch newBatch = RenderBatch.createBatch(MAX_BATCH_SIZE, (int)spriteRenderer.gameObject.getPosition().z);
        newBatch.addSprite(spriteRenderer);
        RENDER_BATCHES.add(newBatch);
        Collections.sort(RENDER_BATCHES);
    }

    /**
     * Tells the Renderer to display on the screen all the stored batches
     */
    public void Render(){
        for(RenderBatch renderBatch : RENDER_BATCHES){
            renderBatch.render();
        }
    }

    public void clear() {
        RENDER_BATCHES.clear();
    }
}
