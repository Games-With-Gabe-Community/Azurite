package physics.collision.shape;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 * The shape implementation of a triangle.
 *
 * @author Juyas
 * @version 19.06.2021
 * @since 19.06.2021
 */
public class Triangle extends PrimitiveShape {

    public Triangle(Vector2f relativeA, Vector2f relativeB, Vector2f relativeC) {
        super(ShapeType.TRIANGLE, relativeA, relativeB, relativeC);
        initSphere();
        init();
    }

}