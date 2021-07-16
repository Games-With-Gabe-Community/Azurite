package physics.collision.shape;

import org.joml.Vector2f;
import physics.collision.CollisionUtil;
import util.Utils;

/**
 * <h1>Azurite</h1>
 * <p>
 * The {@link PrimitiveShape} is implicitly described by all its support vectors.
 * A support vector is a point on the convex hull of a shape, that is furthest in the direction of a specified vector v.
 * This class may contain information about explicit points on the edge too.
 *
 * @author Juyas
 * @version 09.07.2021
 * @since 18.06.2021
 */
public abstract class PrimitiveShape {

    /**
     * The coordinates of the shape - relative to an imaginary origin near the shape
     */
    protected final Vector2f[] relatives;

    /**
     * The coordinates of the shape - absolute coordinates (position is already added to it)
     *
     * @see #getAbsolutePoints()
     */
    protected final Vector2f[] absolutes;

    /**
     * All faces of the shape
     *
     * @see #faces()
     */
    protected final Face[] faces;

    /**
     * The amount of vertices of the shape
     *
     * @see #vertices()
     */
    protected final int vertices;

    /**
     * The type of the shape as {@link ShapeType}
     *
     * @see #type()
     */
    private final ShapeType type;

    /**
     * The centroid of the shape - the relative vector to an imaginary origin near the shape
     */
    protected Vector2f relativeCentroid;

    /**
     * The centroid of the shape - the absolute vector (position is already added to it)
     *
     * @see #centroid()
     */
    protected Vector2f absoluteCentroid;

    /**
     * The boundingSphere of this shape represented by a {@link Circle}.
     * This sphere is used as broadphase for collision detection, since circle intersection checks does cost way less
     *
     * @see #boundingSphere()
     */
    protected Circle boundingSphere;

    /**
     * The current position of the shape in the global grid
     *
     * @see #setPosition(Vector2f)
     */
    private Vector2f position = new Vector2f(0, 0);

    /**
     * Create a polygon of the given coordinates.
     *
     * @param relatives all relative points on the shape
     */
    protected PrimitiveShape(Vector2f... relatives) {
        this(ShapeType.POLYGON, relatives);
    }

    /**
     * Create a shape. Generally only used as super call from other shape classes.
     *
     * @param type      the type of the target shape
     * @param relatives the points defining this shape
     */
    protected PrimitiveShape(ShapeType type, Vector2f... relatives) {
        this.type = type;
        //circles are not defined by points, therefore they stay empty
        if (type == ShapeType.CIRCLE && (relatives == null || relatives.length == 0)) {
            //implicit circles don't have any explicit points
            this.vertices = 0;
            this.relatives = new Vector2f[0];
            this.absolutes = new Vector2f[0];
            this.faces = new Face[0];
        } else {
            //ensures that all relative coords are sorted and dereferenced from the original ones
            this.relatives = Utils.copy(CollisionUtil.convexHull(relatives));
            this.vertices = relatives.length;
            this.absolutes = new Vector2f[this.vertices];
            this.faces = new Face[this.vertices];
        }
    }

    /**
     * Update the shapes position due to external changes, e.g. the related object has moved
     *
     * @param position the new position of the related object
     */
    public final void setPosition(Vector2f position) {
        this.position = position;
        adjust();
    }

    /**
     * @param x the x coord
     * @param y the y coord
     * @see #setPosition(Vector2f)
     */
    public final void setPosition(float x, float y) {
        setPosition(new Vector2f(x, y));
    }

    /**
     * @see this#position
     */
    public final Vector2f position() {
        return position;
    }

    /**
     * @see this#absolutes
     */
    public final Vector2f[] getAbsolutePoints() {
        return absolutes;
    }

    /**
     * @see this#faces
     */
    public final Face[] faces() {
        return faces;
    }

    /**
     * @see this#vertices
     */
    public final int vertices() {
        return vertices;
    }

    /**
     * Calculates a relative centroid and the boundingSphere for the shape.
     * In some special cases this might be done quicker in a different way.
     */
    protected final void initSphere() {
        this.relativeCentroid = CollisionUtil.polygonCentroid(this.relatives);
        this.boundingSphere = new Circle(relativeCentroid, CollisionUtil.boundingSphere(relativeCentroid, this.relatives));
    }

    /**
     * Requires the relativeCentroid to be set.
     * Calculates all relative faces and relative diagonals for a defined shape
     */
    protected final void init() {
        for (int i = 0; i < this.vertices; i++) {
            Vector2f line = relatives[(i + 1) % this.vertices].sub(relatives[i], new Vector2f());
            //define faces
            faces[i] = new Face(this, relatives[i], line);
        }
    }

    /**
     * Will be called by {@link this#setPosition(Vector2f)} after the new position was set.
     * Used to recalculate the absolute coordinates.
     */
    public void adjust() {
        for (int i = 0; i < this.vertices; i++) {
            absolutes[i] = position().add(relatives[i], new Vector2f());
        }
        absoluteCentroid = position().add(relativeCentroid, new Vector2f());
        this.boundingSphere.setPosition(position());
    }

    /**
     * The center point or weight point of the shape.
     *
     * @return centroid of the shape
     * @see #absoluteCentroid
     */
    public final Vector2f centroid() {
        return absoluteCentroid;
    }

    /**
     * A minimal sphere that contains the original shape.
     * The inner shape is supposed to be fixed inside the sphere and share the same centroid.
     *
     * @return the minimal sphere containing this shape
     */
    public final Circle boundingSphere() {
        return boundingSphere;
    }

    /**
     * According to GJKSM this method is supposed to calculate the point of the shape, that is most in direction of v.
     * The general rule is, the more primitive the shape is, the more efficient this method can be.
     * This method may be described as max{v*x,x element of Shape} for any complex shape.
     *
     * @param v the direction
     * @return the point of the shape that is most in the direction of v
     */
    public Vector2f supportPoint(Vector2f v) {
        return CollisionUtil.maxDotPoint(absolutes, v);
    }

    /**
     * A clean description for this shape.
     *
     * @return the {@link ShapeType} matching this {@link PrimitiveShape}
     */
    public final ShapeType type() {
        return type;
    }

}