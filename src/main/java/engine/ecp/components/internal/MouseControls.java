package engine.ecp.components.internal;

import engine.ecp.GameObject;
import engine.ecp.base.Component;
import engine.ecp.interfaces.SingleComponent;
import engine.events.MouseListener;
import engine.scenemanager.Scene;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControls extends Component implements SingleComponent {
    private GameObject holdingObject = null;
    private Scene parentScene = null;
    public MouseControls(Scene parentScene){
        this.parentScene = parentScene;
    }

    public void pickupObject(GameObject go){
        holdingObject = go;
        parentScene.addGameObjectToScene(go);
    }

    public void placeObject(){
        holdingObject = null;
    }

    @Override
    public void update(){
        if(holdingObject != null){
            Vector2f ortho = MouseListener.getOrthoPos();
            holdingObject.setPosition(new Vector3f(ortho.x - 16, ortho.y - 16, 0));
            if(MouseListener.isButtonDown(GLFW_MOUSE_BUTTON_LEFT)){
                placeObject();
            }
        }
    }
}
