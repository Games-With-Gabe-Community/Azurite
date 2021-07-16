package ecs;

import graphics.Color;
import org.joml.Vector2f;
import physics.PhysicalEntity;
import physics.Transform;
import physics.TransformSensitive;
import physics.collision.Collider;
import physics.collision.CollisionHandler;
import physics.collision.CollisionUtil;
import physics.collision.Collisions;
import physics.collision.shape.PrimitiveShape;
import physics.collision.shape.Quadrilateral;
import physics.force.CombinedForce;
import physics.force.CombinedVectorFilter;
import physics.force.Force;
import physics.force.VectorFilter;
import util.Tuple;
import util.Utils;
import util.debug.DebugLine;
import util.debug.DebugPrimitive;

import java.util.Optional;

/**
 * <h1>Azurite</h1>
 * Represents a rigid body with a fixed shape but changeable position.
 * Primarily used to apply physics and check collisions.
 *
 * @author Juyas
 * @version 08.07.2021
 * @since 19.06.2021
 */
public class RigidBody extends Component implements Collider, PhysicalEntity, TransformSensitive {

    /**
     * A short representing a binary series of 0s and 1s.
     * 0 means not present, 1 means present.
     * Beeing present means, beeing present on the collision layer
     * and allow collision with all objects having a present mask on this layer.
     */
    private short collisionLayer;

    /**
     * A short representing a binary series of 0s and 1s.
     * 0 means not present, 1 means present.
     * Beeing present means, beeing able to collide with all objects present on that layer,
     * while its not required to be present on the layer.
     */
    private short collisionMask;

    /**
     * The collision shape of the collider.
     *
     * @see PrimitiveShape
     */
    private final PrimitiveShape collisionShape;

    /**
     * The physical mass of the body used for forces applied to this body.
     * Heavier objects are less effected by the same forces then lighter objects.
     */
    private float mass;

    /**
     * The current velocity of the body.
     * Each update cycle the velocity decides in which direction the object will move.
     */
    private final Vector2f velocity;

    /**
     * The combined forces acting on the body and accelerating it each update cycle.
     *
     * @see this#applyForce(Force)
     * @see this#removeForce(String)
     * @see this#getForce()
     */
    private final CombinedForce bodyForce;

    /**
     * All filters for the velocity to be applied, can deny movement in different ways.
     *
     * @see CombinedVectorFilter
     */
    private final CombinedVectorFilter vectorFilter;

    /**
     * Used to feed with objects this body is colliding with.
     * Decides how to react to a collision. default is {@link Collisions#solid()}.
     */
    private CollisionHandler collisionHandler = Collisions.solid();

    /**
     * Full creation of a rigidbody.
     *
     * @param collisionShape the shape for the collider
     * @param layers         all layers this object should be present on
     * @param maskedLayers   all layers this object should collide with
     * @param physicalMass   the physical mass of this object
     */
    public RigidBody(PrimitiveShape collisionShape, int[] layers, int[] maskedLayers, float physicalMass) {
        super(Collider.class, PhysicalEntity.class); //this class type should be unique in a gameObject
        this.collisionShape = collisionShape;
        this.collisionLayer = Utils.encode(layers);
        this.collisionMask = Utils.encode(maskedLayers);
        this.mass = physicalMass;
        this.velocity = new Vector2f();
        this.bodyForce = new CombinedForce("undefined");
        vectorFilter = new CombinedVectorFilter();
        this.order = SpriteRenderer.ORDER - 1;
    }

    /**
     * Minimal creation of a rigidbody.
     * The default mass is 1.0f.
     * There is no mask set by default.
     *
     * @param collisionShape the shape for the collider
     * @param layer          the layer this object should be present on
     */
    public RigidBody(PrimitiveShape collisionShape, int layer) {
        super(Collider.class, PhysicalEntity.class); //this class type should be unique in a gameObject
        this.collisionShape = collisionShape;
        this.collisionLayer = Utils.encode(layer);
        this.collisionMask = 0;
        this.mass = 1;
        this.velocity = new Vector2f();
        this.bodyForce = new CombinedForce("undefined");
        this.vectorFilter = new CombinedVectorFilter();
        this.order = SpriteRenderer.ORDER - 1;
    }

    /**
     * Overwrites the current collision handler
     *
     * @param collisionHandler the new collision handler replacing the old one
     * @see this#collisionHandler
     */
    public void setCollisionType(CollisionHandler collisionHandler) {
        this.collisionHandler = collisionHandler;
        this.collisionHandler.setParentComponent(this);
    }

    /**
     * @see GameObject#positionBuffer()
     */
    public Vector2f positionBuffer() {
        return gameObject.positionBuffer();
    }

    @Override
    public PrimitiveShape getCollisionShape() {
        return collisionShape;
    }

    @Override
    public Optional<Tuple<Vector2f>> doesCollideWith(Collider other) {
        return CollisionUtil.gjksmCollision(this.collisionShape, other.getCollisionShape());
    }

    @Override
    public boolean canCollideWith(Collider other) {
        return (this.collisionLayer & other.mask()) != 0 || (this.collisionMask & other.layers()) != 0;
    }

    @Override
    public short mask() {
        return collisionMask;
    }

    @Override
    public short layers() {
        return collisionLayer;
    }

    @Override
    public boolean hasMask(int layer) {
        return (Utils.encode(layer) & mask()) != 0;
    }

    @Override
    public boolean isOnLayer(int layer) {
        return (Utils.encode(layer) & layers()) != 0;
    }

    @Override
    public void setLayer(int layer, boolean active) {
        this.collisionLayer = (short) (active ? (layers() | Utils.encode(layer)) : layers() & ~Utils.encode(layer));
    }

    @Override
    public void setMask(int layer, boolean active) {
        this.collisionMask = (short) (active ? (this.collisionMask | Utils.encode(layer)) : this.collisionMask & ~Utils.encode(layer));
    }

    @Override
    public void start() {
        //make the bodyforce unique by using the gameobjects name
        this.bodyForce.setIdentifier(gameObject.name());
        this.bodyForce.setMass(this.mass);
        this.collisionHandler.setParentComponent(this);
        this.collisionShape.setPosition(gameObject.getReadOnlyTransform().getPosition());
    }

    @Override
    public void update(float dt) {
        //lets give the forces a chance to update
        bodyForce.update(dt);
        //then assume they are fine and let them accelerate our velocity
        velocity.add(bodyForce.direction());
        //filter the velocity to prevent unwanted movement
        velocity.set(vectorFilter.filter(velocity));
        //let the velocity then modify our current position - push to buffer
        gameObject.positionBuffer().add(velocity);
    }

    @Override
    public void handleCollision(Collider otherCollider, Tuple<Vector2f> gjkSimplex) {
        //static collisions should be handled by the static object
        if (otherCollider instanceof StaticCollider)
            otherCollider.handleCollision(this, gjkSimplex);
            //collisions with other rigidbodys will be handled here
        else collisionHandler.accept((RigidBody) otherCollider, gjkSimplex);
    }


    @Override
    public void resetCollision() {
    }

    /**
     * Set the mass of the {@link RigidBody}.
     * Updates the mass of {@link this#bodyForce} as well.
     *
     * @param mass the new mass
     */
    public void setMass(float mass) {
        this.mass = mass;
        bodyForce.setMass(mass);
    }

    @Override
    public float getMass() {
        return mass;
    }

    @Override
    public Force getForce() {
        return bodyForce;
    }

    @Override
    public Vector2f velocity() {
        return velocity;
    }

    @Override
    public void applyForce(Force force) {
        bodyForce.applyForce(force);
    }

    @Override
    public void removeForce(String identifier) {
        bodyForce.removeForces(identifier);
    }

    @Override
    public void addFilter(VectorFilter filter) {
        vectorFilter.addFilter(filter);
    }

    @Override
    public void removeFilters(int id) {
        vectorFilter.removeFilters(id);
    }

    @Override
    public boolean isConflictingWith(Class<? extends Component> otherComponent) {
        //there can only be one collider and only one physics calculation
        return Collider.class.isAssignableFrom(otherComponent) || PhysicalEntity.class.isAssignableFrom(otherComponent);
    }

    @Override
    public void update(Transform changedTransform) {
        //update the shape according to changes made to transform
        collisionShape.setPosition(gameObject.getReadOnlyTransform().getPosition());

        //debug only
        if (collisionShape instanceof Quadrilateral) {
            Quadrilateral rect = (Quadrilateral) collisionShape;
            Vector2f[] points = rect.getAbsolutePoints();
            for (int i = 0; i < 4; i++) {
                lines[i].start.set(points[i]);
                lines[i].end.set(points[(i + 1) % 4]);
                lines[i].markDirty();
            }
            lines[4].start.set(rect.centroid());
            lines[4].end.set(rect.centroid().add(velocity().mul(50, new Vector2f()), new Vector2f()));
            lines[4].markDirty();
        }

    }

    //debug
    DebugLine[] lines = new DebugLine[5];
    DebugPrimitive primitive = new DebugPrimitive(lines);

    @Override
    public DebugPrimitive[] debug() {
        if (collisionShape instanceof Quadrilateral) {
            Quadrilateral rect = (Quadrilateral) collisionShape;
            Vector2f[] points = rect.getAbsolutePoints();
            for (int i = 0; i < 4; i++) {
                lines[i] = new DebugLine(points[i], points[(i + 1) % 4], Color.BLUE);
            }
            lines[4] = new DebugLine(rect.centroid(), rect.centroid().add(velocity().mul(50, new Vector2f()), new Vector2f()), Color.YELLOW);
            return new DebugPrimitive[]{primitive};
        }
        return null;
    }

}