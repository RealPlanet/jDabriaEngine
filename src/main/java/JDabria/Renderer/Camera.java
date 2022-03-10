package JDabria.Renderer;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private Matrix4f ProjectionMatrix = new Matrix4f(), ViewMatrix = new Matrix4f();
    private Vector3f Position;

    public Camera(Vector3f StartingCoordinates){
        this.Position = StartingCoordinates;
        AdjustWindowProjection();
    }

    /**
     * Fixup Matrix projection when the window size is changed
     */
    public void AdjustWindowProjection(){
        ProjectionMatrix.identity();
        ProjectionMatrix.ortho(0.0f, 32.0f * 40.f, 0.0f, 32.0f * 21.0f, 0, 100.0f);
    }

    /**
     * Resets view matrix of the camera and returns it
     * @return View Matrix 4x4
     */
    public Matrix4f GetViewMatrix(){
        Vector3f CameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f CameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        ViewMatrix.identity();
        ViewMatrix.lookAt(  new Vector3f(Position.x, Position.y, 20f),
                            CameraFront.add(Position.x, Position.y, 0.0f),
                            CameraUp);
        return ViewMatrix;
    }

    /**
     * Get the projection matrix of the camera
     * @return Projection Matrix 4x4
     */
    public Matrix4f GetProjMatrix(){
        return ProjectionMatrix;
    }

    public Vector3f GetPosition(){
        return Position;
    }

    public void SetPosition(Integer x, Integer y, Integer z){
        Position.x = x;
        Position.y = y;
        Position.z = z;
        GetViewMatrix(); // Reset View Matrix
    }

    public void SetPosition(Vector3f NewPosition){
        Position = NewPosition;
        GetViewMatrix(); // Reset View Matrix
    }
}
