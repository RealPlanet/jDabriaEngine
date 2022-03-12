package JDabria.ECP.Components;

import JDabria.ECP.Component;
import JDabria.ECP.Components.Sprite.SpriteRenderer;

public class FontRenderer extends Component {

    @Override
    public void start(){
        if(gameObject.getComponent(SpriteRenderer.class) != null){
            System.out.println("Found Sprite Renderer!");
        }
    }

    @Override
    public void update() {

    }
}
