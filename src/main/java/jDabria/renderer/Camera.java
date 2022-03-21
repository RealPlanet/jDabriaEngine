package jDabria.renderer;

import org.joml.Matrix4f;
import org.joml.Vector3f;

// TODO :: Convert to component
public class Camera {
    private final Matrix4f projectionMatrix = new Matrix4f();
    private final Matrix4f viewMatrix = new Matrix4f();
    private Vector3f position;

    public Camera(){
        this.position = new Vector3f();
        adjustWindowProjection();
    }

    public Camera(Vector3f startingCoordinates){
        this.position = startingCoordinates;
        adjustWindowProjection();
    }

    /**
     * Fix Matrix projection when the window size is changed
     */
    public void adjustWindowProjection(){
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, 32.0f * 40.f, 0.0f, 32.0f * 21.0f, 0, 100.0f);
    }

    /**
     * Resets view matrix of the camera and returns it
     * @return View Matrix 4x4
     */
    public Matrix4f getViewMatrix(){
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        viewMatrix.identity();
        viewMatrix.lookAt(  new Vector3f(position.x, position.y, 20f),
                            cameraFront.add(position.x, position.y, 0.0f),
                            cameraUp);
        return viewMatrix;
    }

    /**
     * get the projection matrix of the camera
     * @return Projection Matrix 4x4
     */
    public Matrix4f getProjMatrix(){
        return projectionMatrix;
    }

    public Vector3f getPosition(){
        return position;
    }

    public void setPosition(Integer x, Integer y, Integer z){
        position.x = x;
        position.y = y;
        position.z = z;
        getViewMatrix(); // Reset View Matrix
    }

    public void setPosition(Vector3f NewPosition){
        position = NewPosition;
        getViewMatrix(); // Reset View Matrix
    }
}
