package JDabria.ECP.Components;

import Commons.Color;
import JDabria.AssetManager.Resources.Texture;
import JDabria.ECP.Component;
import org.joml.Vector2f;

public class SpriteRenderer extends Component {
    private Color color;
    private Vector2f[] texCoords;
    private Texture sprite;

    //<editor-fold desc="Constructors">
    public SpriteRenderer(Commons.Color color){
        this.color = color;
    }

    public SpriteRenderer(Texture sprite){
        this.sprite = sprite;
        this.color = Color.WHITE;
    }
    //</editor-fold>

    //<editor-fold desc="Public methods">
    public Color getColor(){
        return this.color;
    }

    public Texture getSprite(){
        return this.sprite;
    }

    public Vector2f[] getTexCoords(){
        Vector2f[] texCoords = {
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        };
        return texCoords;
    }
    //</editor-fold>

    @Override
    public void start(){

    }

    @Override
    public void update() {

    }
}
