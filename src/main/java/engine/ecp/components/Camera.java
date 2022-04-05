package engine.ecp.components;

import engine.ecp.GameObject;
import engine.ecp.base.Component;
import engine.ecp.interfaces.UniqueComponent;
import engine.scenemanager.Scene;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera extends Component implements UniqueComponent {
    private final Matrix4f projectionMatrix = new Matrix4f();
    private final Matrix4f viewMatrix = new Matrix4f();

    private final Matrix4f inverseProjMatrix = new Matrix4f();
    private final Matrix4f inverseViewMatrix = new Matrix4f();

    private transient Transform parentTransform = null;
    private transient Vector3f lastPosition = new Vector3f();

    public static @NotNull GameObject createDefaultCamera(@NotNull Scene scene){
        GameObject holder = new GameObject("CameraObj");
        holder.setActive(true);
        Camera camera = new Camera();
        holder.addComponent(camera);
        camera.setPosition(new Vector3f(0f, 0f, 0f));

        scene.addGameObjectToScene(holder);
        return holder;
    }

    public Camera(){}

    @Override
    public void start() {
        parentTransform = gameObject.getTransform();
        adjustWindowProjection();
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public void update() {
        if(lastPosition != parentTransform.position){
            lastPosition = parentTransform.position;
            getViewMatrix();
        }
        super.update();
    }

    @Override
    public void delete() {
        super.delete();
    }

    /**
     * Fix Matrix projection when the window size is changed
     */
    public void adjustWindowProjection(){
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, 32.0f * 40.f, 0.0f, 32.0f * 21.0f, 0, 100.0f);
        projectionMatrix.invert(inverseProjMatrix);
    }

    /**
     * Resets view matrix of the camera and returns it
     * @return View Matrix 4x4
     */
    public Matrix4f getViewMatrix(){
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        viewMatrix.identity();
        viewMatrix.lookAt(  new Vector3f(parentTransform.position.x, parentTransform.position.y, 20f),
                cameraFront.add(parentTransform.position.x, parentTransform.position.y, 0.0f),
                cameraUp);

        viewMatrix.invert(inverseViewMatrix);
        return viewMatrix;
    }

    /**
     * get the projection matrix of the camera
     * @return Projection Matrix 4x4
     */
    public Matrix4f getProjMatrix(){
        return projectionMatrix;
    }

    public Matrix4f getInverseProjMatrix() {return inverseProjMatrix;}

    public Matrix4f getInverseViewMatrix() {return inverseViewMatrix;}

    public Vector3f getPosition(){
        return parentTransform.position;
    }

    public void setPosition(Vector3f newPosition){
        parentTransform.position = newPosition;
        getViewMatrix(); // Reset View Matrix
    }
}
