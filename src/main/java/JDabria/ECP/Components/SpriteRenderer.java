package JDabria.ECP.Components;

import JDabria.ECP.Component;

public class SpriteRenderer extends Component {
    @Override
    public void Start(){
        System.out.println("I'm starting!!!");
    }

    private boolean HasPrinted = false;
    @Override
    public void Update() {
        if(!HasPrinted){
            HasPrinted = true;
            System.out.println("I'm updating!!!");
        }
    }
}
