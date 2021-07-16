package physics.collision;

import ecs.RigidBody;
import org.joml.Vector2f;
import physics.force.DirectionalVectorFilter;
import util.Tuple;

import java.util.Optional;

/**
 * <h1>Azurite</h1>
 * A util class for specific {@link CollisionHandler}'s
 *
 * @author Juyas
 * @version 08.07.2021
 * @since 24.06.2021
 */
public class Collisions {

    /**
     * The ID for {@link physics.force.VectorFilter} if the collision applies a filter to prevent intersection after collision
     */
    public static final int COLLISION_FILTER = 601112109;

    /**
     * One type of bouncy collision, does solid collision as well, but adds a portion of the reflection vector to the velocity,
     * which maybe feels like its bouncing off.
     *
     * @param velocityFactor the factor multiplied by the reflection before added to the velocity.
     *                       factor of 1 means no change of the reflection.
     *                       negative factors may result in undefined behaviour.
     */
    public static CollisionHandler solidBouncy(float velocityFactor) {
        return new CollisionHandler() {
            @Override
            public void accept(RigidBody collider, Tuple<Vector2f> simplex) {
                //find penetration vector with EPA
                Optional<Vector2f> epa = CollisionUtil.expandingPolytopeAlgorithm(collider.getCollisionShape(), parentComponent.getCollisionShape(), simplex);
                Vector2f reflection = epa.get();
                //solid intersection prevention
                collider.positionBuffer().add(reflection);
                //bouncy factor applied
                collider.velocity().add(reflection.mul(velocityFactor, new Vector2f()));
                //prevents movement in the direction of the collision, therefore negative factors "should" have no impact
                addFilter(collider, reflection);
            }
        };
    }

    /**
     * Standard collision, just prevents intersection and moves object according to EPA to seperate them.
     */
    public static CollisionHandler solid() {
        return new CollisionHandler() {
            @Override
            public void accept(RigidBody collider, Tuple<Vector2f> simplex) {
                //find penetration vector with EPA
                Optional<Vector2f> epa = CollisionUtil.expandingPolytopeAlgorithm(collider.getCollisionShape(), parentComponent.getCollisionShape(), simplex);
                Vector2f reflection = epa.get();
                //solid intersection prevention
                collider.positionBuffer().add(reflection);
                //prevents movement in the direction of the collision
                addFilter(collider, reflection);
            }
        };
    }

    //helper method to create an anti-intersection vector filter
    private static void addFilter(RigidBody body, Vector2f reflection) {
        body.addFilter(new DirectionalVectorFilter(reflection.mul(-1, new Vector2f()), COLLISION_FILTER));
    }

}