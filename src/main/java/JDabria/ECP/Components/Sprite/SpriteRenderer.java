package JDabria.ECP.Components.Sprite;

import Commons.Color;
import JDabria.AssetManager.Resources.Texture;
import JDabria.ECP.Component;
import JDabria.ECP.Components.Transform;
import org.joml.Vector2f;

public class SpriteRenderer extends Component {
    private Sprite sprite;
    private Color color;
    private Transform parentTransform;
    private boolean isDirty = false;

    //<editor-fold desc="Constructors">
    public SpriteRenderer(Commons.Color color){
        this.color = color;
        sprite = new Sprite();
    }

    public SpriteRenderer(Sprite sprite){
        this.sprite = sprite;
        this.color = Color.WHITE;
    }
    //</editor-fold>

    //<editor-fold desc="Public methods">
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

    public void setSprite(Sprite sprite){
        this.sprite = sprite;
        this.isDirty = true;
    }

    public void setColor(Color color){
        if(this.color == color){
            return;
        }

        this.color = color;
        this.isDirty = true;
    }
    //</editor-fold>

    @Override
    public void start(){
        parentTransform = gameObject.transform.copy();
    }

    @Override
    public void update() {
        if(parentTransform != gameObject.transform){
            parentTransform = gameObject.transform.copy();
            isDirty = true;
        }
    }

    public boolean isDirty() {
        return this.isDirty;
    }

    public void setClean() {
        this.isDirty = false;
    }
}
