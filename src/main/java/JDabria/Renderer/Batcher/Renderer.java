package JDabria.Renderer.Batcher;

import JDabria.ECP.Components.SpriteRenderer;
import JDabria.ECP.GameObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private static final int MAX_BATCH_SIZE = 1000;
    private static final List<RenderBatch> Batches = new ArrayList<>();

    public Renderer(){
    }

    public void Add(@NotNull GameObject go){
        SpriteRenderer Spr = go.GetComponent(SpriteRenderer.class);
        if(Spr == null){
            return;
        }

        Add(Spr);
    }

    private void Add(SpriteRenderer Spr){
        boolean AddedToBatch = false;
        for(RenderBatch Batch : Batches){
            if(Batch.HasRoom()){
                Batch.AddSprite(Spr);
                AddedToBatch = true;
                break;
            }
        }

        if(AddedToBatch){
           return;
        }

        RenderBatch NewBatch = RenderBatch.CreateBatch(MAX_BATCH_SIZE);
        NewBatch.AddSprite(Spr);
        Batches.add(NewBatch);
    }

    public void Render(){
        for(RenderBatch Batch : Batches){
            Batch.Render();
        }
    }
}
