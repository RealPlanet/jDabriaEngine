package engine.renderer.debug;

import commons.Color;
import commons.Time;
import engine.Window;
import engine.events.window.IUpdateFrameListener;
import org.joml.Vector3f;

/**
 * A 2D line which is rendered within the debug loop and is indipendent by "standard" objects.
 */
public class Line2D implements IUpdateFrameListener {
    private Vector3f origin;
    private Vector3f end;
    private Color color;
    protected float lifetime;

    public Line2D(Vector3f origin, Vector3f end, Color color, float lifetime) {
        this.origin = origin;
        this.end = end;
        this.color = color;
        this.lifetime = lifetime;

        Window.addUpdateFrameListener(this);
    }

    public void destroyLine(){
        Window.removeUpdateFrameListener(this);
        DebugDraw.removeLine2D(this);
    }

    //region Getters
    public Vector3f getOrigin() {
        return origin;
    }

    public Vector3f getEnd() {
        return end;
    }

    public Color getColor() {
        return color;
    }

    //endregion

    @Override
    public void onFrameUpdate() {
        tickLifetime();

        if(lifetime <= 0){
            destroyLine();
            return;
        }
    }

    protected void tickLifetime(){
        lifetime -= Time.deltaTime();
    }
}
