package JDabria.ECP.Components;

import Commons.Color;
import JDabria.ECP.Component;

public class SpriteRenderer extends Component {
     Color Color;

    public SpriteRenderer(Commons.Color Color){
        this.Color = Color;
    }

    public Color GetColor(){
        return this.Color;
    }

    @Override
    public void Start(){

    }

    @Override
    public void Update() {

    }
}
