package physics.force;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 * A {@link VectorFilter} can filter any given vector to produce a limited or changed result.
 *
 * @author Juyas
 * @version 08.07.2021
 * @since 05.07.2021
 */
public interface VectorFilter {

    /**
     * A unique id for this filter.
     * Required to remove filters by their id.
     *
     * @return the unique id of this filter
     * @see CombinedVectorFilter#removeFilters(int)
     * @see ecs.RigidBody#removeFilters(int)
     */
    int id();

    /**
     * Filters a given vector by a fixed filtering method.
     * If the filter is invalid, it should just return the given vector with no changes made.
     *
     * @param vector the given vector
     * @return the filtered vector
     */
    Vector2f filter(Vector2f vector);

    /**
     * Determines if this filter has become invalid.
     * Invalid filter should no longer be applied and should never become valid again.
     *
     * @return true, if and only if the filter has become invalid
     */
    boolean isInvalid();

    /**
     * Stacks another filter on top of this filter.
     * The given filter will filter the input before the current one.
     *
     * @param vectorFilter the given filter
     * @return a filter combining the new one with the current one
     * - new id will be the id of vectorFilter and it will become invalid as soon as either of them is invalid
     */
    default VectorFilter andThen(VectorFilter vectorFilter) {
        return new VectorFilter() {
            @Override
            public int id() {
                return vectorFilter.id();
            }

            @Override
            public Vector2f filter(Vector2f vector) {
                return vectorFilter.filter(VectorFilter.this.filter(vector));
            }

            @Override
            public boolean isInvalid() {
                return vectorFilter.isInvalid() || VectorFilter.this.isInvalid();
            }
        };
    }

}