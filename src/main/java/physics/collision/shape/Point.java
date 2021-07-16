package physics.collision.shape;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 * <p>
 * The GJKSM shape implementation of a single point.
 *
 * @author Juyas
 * @version 19.06.2021
 * @since 19.06.2021
 */
public class Point extends PrimitiveShape {

    public Point(Vector2f relativePoint) {
        super(ShapeType.POINT, relativePoint);
        this.relativeCentroid = relativePoint;
        this.boundingSphere = new Circle(relativePoint, Float.MIN_VALUE);
        init();
    }

    @Override
    public Vector2f supportPoint(Vector2f v) {
        return absolutes[0];
    }

}