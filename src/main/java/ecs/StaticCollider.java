package ecs;

import graphics.Color;
import org.joml.Vector2f;
import physics.collision.Collider;
import physics.collision.CollisionHandler;
import physics.collision.CollisionUtil;
import physics.collision.Collisions;
import physics.collision.shape.PrimitiveShape;
import physics.collision.shape.Quadrilateral;
import util.Tuple;
import util.Utils;
import util.debug.DebugLine;
import util.debug.DebugPrimitive;

import java.util.Optional;

/**
 * <h1>Azurite</h1>
 * Represents a rigid body with a fixed shape and fixed position.
 * Primarily used to generate terrain and walls and fixed objects that can collide in general.
 *
 * @author Juyas
 * @version 08.07.2021
 * @since 22.06.2021
 */
public class StaticCollider extends Component implements Collider {

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
     * Used to feed with objects this body is colliding with.
     * Decides how to react to a collision. default is {@link Collisions#solid()}.
     */
    private CollisionHandler collisionHandler = Collisions.solid();

    /**
     * Full creation of a {@link StaticCollider}.
     *
     * @param collisionShape the shape for the collider
     * @param layers         all layers this object should be present on
     * @param maskedLayers   all layers this object should collide with
     */
    public StaticCollider(PrimitiveShape collisionShape, int[] layers, int[] maskedLayers) {
        super(Collider.class);
        this.collisionShape = collisionShape;
        this.collisionLayer = Utils.encode(layers);
        this.collisionMask = Utils.encode(maskedLayers);
        this.order = SpriteRenderer.ORDER - 1;
    }

    /**
     * Minimal creation of a {@link StaticCollider}.
     * There is no mask set by default.
     *
     * @param collisionShape the shape for the collider
     * @param layer          the layer this object should be present on
     */
    public StaticCollider(PrimitiveShape collisionShape, int layer) {
        super(Collider.class);
        this.collisionShape = collisionShape;
        this.collisionLayer = Utils.encode(layer);
        this.order = SpriteRenderer.ORDER - 1;
    }

    /**
     * Overwrites the current collision handler
     *
     * @param collisionHandler the new collision handler replacing the old one
     * @see this#collisionHandler
     */
    public void setCollisionHandler(CollisionHandler collisionHandler) {
        this.collisionHandler = collisionHandler;
        this.collisionHandler.setParentComponent(this);
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
        this.collisionShape.setPosition(gameObject.getReadOnlyTransform().getPosition());
        this.collisionHandler.setParentComponent(this);
    }

    @Override
    public void update(float dt) {
        //the position of the object should not change at any given time, therefore its solid
    }

    @Override
    public void handleCollision(Collider otherCollider, Tuple<Vector2f> gjkSimplex) {
        if (otherCollider instanceof RigidBody)
            collisionHandler.accept((RigidBody) otherCollider, gjkSimplex);
    }

    @Override
    public void resetCollision() {

    }

    @Override
    public boolean isConflictingWith(Class<? extends Component> otherComponent) {
        //there can only be one collider
        return Collider.class.isAssignableFrom(otherComponent);
    }

    @Override
    public DebugPrimitive[] debug() {
        if (collisionShape instanceof Quadrilateral) {
            Quadrilateral rect = (Quadrilateral) collisionShape;
            Vector2f[] points = rect.getAbsolutePoints();
            DebugLine[] lines = new DebugLine[4];
            for (int i = 0; i < 4; i++) {
                lines[i] = new DebugLine(points[i], points[(i + 1) % 4], Color.GREEN);
            }
            DebugPrimitive primitive = new DebugPrimitive(lines);
            return new DebugPrimitive[]{primitive};
        }
        return null;
    }

}

/***********************************************************************************************
 *
 *                  All rights reserved, SpaceParrots UG (c) copyright 2021
 *
 ***********************************************************************************************/