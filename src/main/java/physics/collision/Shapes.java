package physics.collision;

import org.joml.Vector2f;
import physics.collision.shape.Circle;
import physics.collision.shape.ConvexPolygon;
import physics.collision.shape.Quadrilateral;
import physics.collision.shape.Triangle;

/**
 * <h1>Azurite</h1>
 * Util methods to create {@link physics.collision.shape.PrimitiveShape} on the fly.
 *
 * @author Juyas
 * @version 09.07.2021
 * @since 20.06.2021
 */
public class Shapes {

    /**
     * Create a circle.
     *
     * @param center         the absolute center of the circle
     * @param circlePosition the current absolute position of the circle's center
     * @param radius         the radius of the circle
     * @return a circle
     */
    public static Circle circle(Vector2f center, Vector2f circlePosition, float radius) {
        Circle circle = new Circle(center.sub(circlePosition, new Vector2f()), radius);
        circle.setPosition(circlePosition);
        return circle;
    }

    /**
     * Create a circle.
     *
     * @param center the relative center of the circle
     * @param radius the radius of the circle
     * @return a circle
     */
    public static Circle circle(Vector2f center, float radius) {
        return new Circle(center, radius);
    }

    /**
     * Create a circle.
     *
     * @param x the relative x coord of the circles center
     * @param y the relative y coord of the circles center
     * @param r the radius of the circle
     * @return a circle
     */
    public static Circle circle(float x, float y, float r) {
        return circle(new Vector2f(x, y), r);
    }

    /**
     * Create a convex polygon by a given set of coordinate pairs.
     *
     * @param coordPairs a dynamic array of coordinates, which must have an even size.
     * @return a {@link ConvexPolygon} over all given points or null if the coordinates have a odd size
     */
    public static ConvexPolygon convexPolygon(float... coordPairs) {
        if (coordPairs.length % 2 == 1 || coordPairs.length == 0) return null;
        Vector2f[] coords = new Vector2f[coordPairs.length / 2];
        for (int i = 0; i < coords.length; i++)
            coords[i] = new Vector2f(coordPairs[i * 2], coordPairs[i * 2 + 1]);
        return new ConvexPolygon(coords);
    }

    /**
     * Create a triangle.
     *
     * @param ax relative coordinate Ax
     * @param ay relative coordinate Ay
     * @param bx relative coordinate Bx
     * @param by relative coordinate By
     * @param cx relative coordinate Cx
     * @param cy relative coordinate Cy
     * @return a triangle
     */
    public static Triangle triangle(float ax, float ay, float bx, float by, float cx, float cy) {
        return triangle(new Vector2f(ax, ay), new Vector2f(bx, by), new Vector2f(cx, cy));
    }

    /**
     * Create a triangle.
     *
     * @param a relative point a
     * @param b relative point b
     * @param c relative point c
     * @return a triangle
     */
    public static Triangle triangle(Vector2f a, Vector2f b, Vector2f c) {
        return new Triangle(a, b, c);
    }

    /**
     * Create an axis aligned rectangle.
     * This will find min corner and max corner and generate both missing points through basic geometry
     *
     * @param ax relative coordinate Ax
     * @param ay relative coordinate Ay
     * @param bx relative coordinate Bx
     * @param by relative coordinate By
     * @return an axis aligned rectangle
     */
    public static Quadrilateral axisAlignedRectangle(float ax, float ay, float bx, float by) {
        Vector2f max, min, topMin, botMax;
        max = new Vector2f(Math.max(ax, bx), Math.max(ay, by));
        min = new Vector2f(Math.min(ax, bx), Math.min(ay, by));
        topMin = new Vector2f(min.x, max.y);
        botMax = new Vector2f(max.x, min.y);
        return quadrilateral(min, botMax, max, topMin);
    }

    /**
     * Create a quadrilateral.
     *
     * @param a relative point a
     * @param b relative point b
     * @param c relative point c
     * @param d relative point d
     * @return a quadrilateral
     */
    public static Quadrilateral quadrilateral(Vector2f a, Vector2f b, Vector2f c, Vector2f d) {
        return new Quadrilateral(a, b, c, d);
    }

    /**
     * Create an axis aligned rectangle.
     * This will find min corner and max corner and generate both missing points through basic geometry
     *
     * @param a relative point a
     * @param b relative point b
     * @return an axis aligned rectangle
     */
    public static Quadrilateral axisAlignedRectangle(Vector2f a, Vector2f b) {
        return axisAlignedRectangle(a.x, a.y, b.x, b.y);
    }

}