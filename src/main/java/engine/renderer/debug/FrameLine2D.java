package engine.renderer.debug;

import commons.Color;
import org.joml.Vector3f;

/**
 * This implementation
 */
public class FrameLine2D extends Line2D{
    public FrameLine2D(Vector3f origin, Vector3f end, Color color, float lifetime) {
        super(origin, end, color, lifetime);
    }

    @Override
    protected void tickLifetime(){
        lifetime--;
    }
}
