package JDabria.Renderer.Batcher;

import JDabria.ECP.Components.SpriteRenderer;
import JDabria.ECP.GameObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private static final int MAX_BATCH_SIZE = 1000;
    private static final List<RenderBatch> RENDER_BATCHES = new ArrayList<>();

    public Renderer(){
    }

    public void add(@NotNull GameObject go){
        SpriteRenderer component = go.getComponent(SpriteRenderer.class);
        if(component == null){
            return;
        }

        add(component);
    }

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

        RenderBatch newBatch = RenderBatch.createBatch(MAX_BATCH_SIZE);
        newBatch.addSprite(spriteRenderer);
        RENDER_BATCHES.add(newBatch);
    }

    public void Render(){
        for(RenderBatch renderBatch : RENDER_BATCHES){
            renderBatch.render();
        }
    }
}
