package physics.force;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 * Can be applied to a {@link physics.PhysicalEntity} and should be a unique instance for each object in most cases.
 * This class is independent from the mass of an object. Each object is
 *
 * @author Juyas
 * @version 08.07.2021
 * @since 20.06.2021
 */
public interface Force {

    /**
     * A unique identifier for this force, can be used as name as well.
     *
     * @return the unique identifier
     */
    String identifier();

    /**
     * Called with the update cycle of the related object.
     * If assigned to multiple objects, this will be called multiple times per update cycles.
     * The order in the calls happen is undefined and therefore its not recommended to assign a force,
     * which changes over time or in relation to anything else, to multiple objects.
     * Constant forces like gravity however can be reused of course.
     *
     * @param dt the delta time passed by the update cycle
     * @return whether the force has changed due to this call
     */
    boolean update(float dt);

    /**
     * The direction and strength of this force as {@link Vector2f}.
     *
     * @return the force as vector
     */
    Vector2f direction();

}