package JDabria.ECP.Components;

import JDabria.ECP.Component;

public class FontRenderer extends Component {

    @Override
    public void Start(){
        if(GameObject.GetComponent(SpriteRenderer.class) != null){
            System.out.println("Found Sprite Renderer!");
        }
    }

    @Override
    public void Update() {

    }
}
