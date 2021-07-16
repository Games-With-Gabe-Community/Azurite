package physics.collision.shape;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 * <p>
 * A basic polygon shape.
 * It does not ensure the points to be part of the convex hull, so this object may contain points for a concave shape.
 * If that is the case, this shape won't work correctly for GJKSM collision detection.
 *
 * @author Juyas
 * @version 19.06.2021
 * @since 19.06.2021
 */
public class ConvexPolygon extends PrimitiveShape {


    public ConvexPolygon(Vector2f... relatives) {
        super(ShapeType.POLYGON, relatives);
        initSphere();
        init();
    }

}