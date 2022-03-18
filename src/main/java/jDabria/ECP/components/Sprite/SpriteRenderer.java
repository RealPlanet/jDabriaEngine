package jDabria.ECP.components.Sprite;

import commons.Color;
import jDabria.assetManager.resources.Texture;
import jDabria.ECP.base.Component;
import jDabria.ECP.components.Transform;
import jDabria.renderer.sprite.Sprite;
import org.joml.Vector2f;

public class SpriteRenderer extends Component {

    //region Member variables
    private Sprite sprite = new Sprite();
    private Color color;
    private Transform parentTransform;
    private boolean isDirty = true;
    //endregion

    //<editor-fold desc="Constructors">
    public SpriteRenderer(commons.Color color){
        this.color = color;
        sprite = new Sprite();
    }
    //endregion

    public SpriteRenderer(Sprite sprite){
        this.sprite = sprite;
        this.color = Color.WHITE;
    }

    public boolean isDirty(){
        return this.isDirty;
    }

    public Color getColor(){
        return this.color;
    }

    public Sprite getSprite(){
        return this.sprite;
    }

    public Texture getTexture(){
        return sprite.getTexture();
    }

    public Vector2f[] getTexCoords(){
        return sprite.getTexCoords();
    }
    //endregion

    //region Setters
    /**
     * Marks this sprite as "Clean", will not be re-buffered by the renderer
     */
    public SpriteRenderer setClean() {
        this.isDirty = false;
        return this;
    }

    /**
     * Marks this sprite as "Dirty" prompting the renderer to re buffer its data
     */
    public SpriteRenderer setDirty() {
        this.isDirty = true;
        return this;
    }

    public SpriteRenderer setSprite(Sprite sprite){
        this.sprite = sprite;
        this.isDirty = true;
        return this;
    }

    public SpriteRenderer setColor(Color color){
        if(this.color == color){
            return this;
        }

        this.color = color;
        this.isDirty = true;
        return this;
    }
    //endregion

    //endregion

    // region Component overrides
    @Override
    public void start(){
        parentTransform = gameObject.getTransform();
    }

    @Override
    public void update() {
        Transform latestTransform = gameObject.getTransform();
        if(parentTransform != latestTransform){
            parentTransform = latestTransform;
            isDirty = true;
        }
    }
    // endregion


}
