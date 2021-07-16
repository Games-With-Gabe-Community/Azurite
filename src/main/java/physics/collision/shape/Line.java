package physics.collision.shape;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 * <p>
 * The shape implementation of a line.
 *
 * @author Juyas
 * @version 19.06.2021
 * @since 19.06.2021
 */
public class Line extends PrimitiveShape {

    public Line(Vector2f relativeA, Vector2f relativeB) {
        super(ShapeType.LINE, relativeA, relativeB);
        initSphere();
        init();
    }

    @Override
    public Vector2f supportPoint(Vector2f v) {
        return absolutes[0].dot(v) > absolutes[1].dot(v) ? absolutes[0] : absolutes[1];
    }

}