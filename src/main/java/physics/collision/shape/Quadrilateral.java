package physics.collision.shape;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 * <p>
 * The shape implementation of a rectangle.
 *
 * @author Juyas
 * @version 19.06.2021
 * @since 19.06.2021
 */
public class Quadrilateral extends PrimitiveShape {

    public Quadrilateral(Vector2f a, Vector2f b, Vector2f c, Vector2f d) {
        super(ShapeType.QUADRILATERAL, a, b, c, d);
        initSphere();
        init();
    }

}