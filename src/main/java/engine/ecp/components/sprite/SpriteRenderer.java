package engine.ecp.components.sprite;

import commons.Color;
import engine.assetmanager.resources.Texture;
import engine.ecp.base.Component;
import engine.ecp.components.Transform;
import engine.renderer.sprite.Sprite;
import org.joml.Vector2f;

public class SpriteRenderer extends Component {

    //region Member variables
    private Sprite sprite;
    private Color color;
    private Transform lastTransform;
    private transient boolean isDirty;
    //endregion

    private SpriteRenderer(){
        this(null, null);
    }

    //region Constructors
    public SpriteRenderer(commons.Color color){
        this(new Sprite(), color);
    }
    //endregion

    public SpriteRenderer(Sprite sprite){
        this(sprite, Color.WHITE);
    }

    public SpriteRenderer(Sprite sprite, Color color){
        this.sprite = sprite;
        this.color = color;
        isDirty = true;
        lastTransform = null;
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
    @SuppressWarnings("UnusedReturnValue")
    public SpriteRenderer setClean() {
        this.isDirty = false;
        return this;
    }

    public SpriteRenderer setSpriteSize(int h, int w){
        sprite.setSize(h, w);
        setDirty();
        return this;
    }

    /**
     * Marks this sprite as "Dirty" prompting the renderer to re buffer its data
     */
    @SuppressWarnings("UnusedReturnValue")
    public SpriteRenderer setDirty() {
        this.isDirty = true;
        return this;
    }

    public SpriteRenderer setSprite(Sprite sprite){
        this.sprite = sprite;
        this.isDirty = true;
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
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
        lastTransform = gameObject.getTransform().copy();
    }

    @Override
    public void update() {
        if(!lastTransform.equals(gameObject.getTransform())){
            lastTransform = gameObject.getTransform().copy();
            isDirty = true;
        }
    }
    // endregion


}
