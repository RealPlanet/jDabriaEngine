package engine.ecp.components.internal;

import commons.Settings;
import engine.ecp.GameObject;
import engine.ecp.base.Component;
import engine.ecp.interfaces.SingleComponent;
import engine.events.MouseListener;
import engine.scenemanager.Scene;
import engine.scenemanager.SceneManager;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControls extends Component implements SingleComponent {
    private transient GameObject holdingObject = null;
    private transient Scene parentScene = null;

    public MouseControls(Scene parentScene){
        this.parentScene = parentScene;
    }

    public void pickupObject(GameObject go){
        holdingObject = go;

        // FIXME :: Temp fix to avoid deserialization problems
        if(parentScene == null){
            parentScene = SceneManager.getActiveScenes().get(0);
        }
        parentScene.addGameObjectToScene(go);
    }

    public void placeObject(){
        holdingObject = null;
    }

    @Override
    public void update(){
        if(holdingObject != null){
            Vector2f ortho = MouseListener.getOrthoPos();
            float x = (int)(ortho.x / Settings.GRID_WIDTH) * Settings.GRID_WIDTH;
            float y = (int)(ortho.y / Settings.GRID_HEIGHT) * Settings.GRID_HEIGHT;

            holdingObject.setPosition(new Vector3f(x, y, 0));
            if(MouseListener.isButtonDown(GLFW_MOUSE_BUTTON_LEFT)){
                placeObject();
            }
        }
    }
}
