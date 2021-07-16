package physics;

import org.joml.Vector2f;

/**
 * Represents the position in X and Y coordinates, and the scale (width and height) of a gameObject
 * TODO: change to a component? - no don't do that
 */
public class Transform {

    public static final Vector2f ZERO = new Vector2f(0, 0);

    private Vector2f position;
    public Vector2f scale;
    private Vector2f positionBuffer;

    /**
     * Used in constructors to initialize the object.
     *
     * @param position
     * @param scale
     */
    private void init(Vector2f position, Vector2f scale) {
        this.position = position;
        this.scale = scale;
        this.positionBuffer = new Vector2f();
    }

    /**
     * Creates a new empty transform.
     */
    public Transform() {
        init(new Vector2f(), new Vector2f());
    }

    /**
     * If only the position is passed, the scale is not, scale is created as an empty Vector2f.
     *
     * @param position X and Y coordinates as a Vector2f
     */
    public Transform(Vector2f position) {
        init(position, new Vector2f());
    }

    /**
     * @param position X and Y coordinates as a Vector2f
     * @param scale    scale (width and height) of the object as a Vector2f
     */
    public Transform(Vector2f position, Vector2f scale) {
        init(position, scale);
    }

    /**
     * @param x X coordinate of the object
     * @param y Y coordinate of the object
     * @param w width of the object
     * @param h height of the object
     */
    public Transform(float x, float y, float w, float h) {
        init(new Vector2f(x, y), new Vector2f(w, h));
    }

    /**
     * @return Returns a new transform which is identical to this object, can be used to copy into a new physics.Transform object.
     */
    public Transform copy() {
        return new Transform(new Vector2f(this.position), new Vector2f(this.scale));
    }

    /**
     * Takes a reference to an external transform, and copies this physics.Transform to it.
     *
     * @param to The physics.Transform to be changed.
     */
    public void copy(Transform to) {
        to.position.set(this.position);
        to.scale.set(this.scale);
    }

    /**
     * Checks to see if to Transforms are equal.
     *
     * @param o physics.Transform to be checked for equality against this instance of physics.Transform.
     * @return Returns true if instances are the same, otherwise returns false.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Transform)) {
            return false;
        }
        Transform t = (Transform) o;
        return t.position.equals(this.position) && t.scale.equals(this.scale);
    }

    public void resetPositionBuffer() {
        this.positionBuffer = new Vector2f();
    }

    public boolean applyPositionBuffer() {
        if (!positionBuffer.isFinite() || positionBuffer.equals(ZERO)) return false;
        this.position.add(positionBuffer);
        return true;
    }

    public Vector2f positionBuffer() {
        return positionBuffer;
    }

    /*
     * I don't think that these need any explanation...
     */
    public float getX() {
        return this.position.x;
    }

    public float getY() {
        return this.position.y;
    }

    public void addY(float y) {
        positionBuffer.add(0, y);
    }

    public float getWidth() {
        return this.scale.x;
    }

    public float getHeight() {
        return this.scale.y;
    }

    public Vector2f getPosition() {
        return position;
    }

    //this method is dangerous, use it with caution. it can break physics when called at the wrong time
    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public Vector2f getScale() {
        return scale;
    }

    public void setScale(Vector2f scale) {
        this.scale = scale;
    }

}
