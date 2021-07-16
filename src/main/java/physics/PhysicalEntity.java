package physics;

import org.joml.Vector2f;
import physics.force.Force;
import physics.force.VectorFilter;

/**
 * <h1>Azurite</h1>
 * Every class implementing PhysicalEntity will be able to have a mass and velocity and can be targeted by forces.
 *
 * @author Juyas
 * @version 07.07.2021
 * @since 20.06.2021
 */
public interface PhysicalEntity {

    /**
     * The absolute mass of the physical entity in space.
     *
     * @return the mass of the entity
     */
    float getMass();

    /**
     * The force currently effecting the physical entity.
     * Can consist of multiple forces added together.
     *
     * @return the force effecting the entity
     */
    Force getForce();

    /**
     * The current directional velocity of the physical entity.
     *
     * @return the current directional velocity
     */
    Vector2f velocity();

    /**
     * Applies another force to the physical entity that will be added to the physical entities effective force.
     *
     * @param force a new force effecting this entity
     */
    void applyForce(Force force);

    /**
     * Removes all forces matching a given identifier.
     * They will no longer effect the force of the entity.
     *
     * @param identifier the identifier to find all matching forces
     */
    void removeForce(String identifier);

    /**
     * Adds a filter to ensure the velocity is inside specified borders.
     * Can be used for speed or directional limitations.
     *
     * @param filter the new velocity filter
     */
    void addFilter(VectorFilter filter);

    /**
     * Removes all filters matching a given id.
     *
     * @param id the id
     */
    void removeFilters(int id);

}