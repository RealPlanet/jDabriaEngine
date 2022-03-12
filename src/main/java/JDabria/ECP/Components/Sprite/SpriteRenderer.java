package JDabria.ECP.Components.Sprite;

import Commons.Color;
import JDabria.AssetManager.Resources.Texture;
import JDabria.ECP.Component;
import org.joml.Vector2f;

public class SpriteRenderer extends Component {
    private Sprite sprite;
    private Color color;

    //<editor-fold desc="Constructors">
    public SpriteRenderer(Commons.Color color){
        this.color = color;
        sprite = new Sprite(null);
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
        return sprite;
    }

    public Texture getTexture(){
        return sprite.getTexture();
    }

    public Vector2f[] getTexCoords(){
        return sprite.getTexCoords();
    }
    //</editor-fold>

    @Override
    public void start(){

    }

    @Override
    public void update() {

    }
}
