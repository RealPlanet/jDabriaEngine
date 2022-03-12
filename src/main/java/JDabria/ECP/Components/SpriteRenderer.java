package JDabria.ECP.Components;

import Commons.Color;
import JDabria.ECP.Component;

public class SpriteRenderer extends Component {
     Color Color;

    public SpriteRenderer(Commons.Color color){
        this.Color = color;
    }

    public Color getColor(){
        return this.Color;
    }

    @Override
    public void start(){

    }

    @Override
    public void update() {

    }
}
