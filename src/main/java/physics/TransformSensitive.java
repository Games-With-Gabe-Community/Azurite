package physics;

/**
 * <h1>Azurite</h1>
 * An interface used to show interest as a {@link ecs.Component} in a changing transform value of a {@link ecs.GameObject} that it belongs to.
 * Is used for ordering and improving the update loop inside a gameObject and its components.
 *
 * @author Juyas
 * @version 12.07.2021
 * @see ecs.GameObject#update(float)
 * @since 22.06.2021
 */
public interface TransformSensitive {

    /**
     * Called if the transform of the corresponding parent object has changed in this update loop.
     * Its ensured to be called only a single time per update loop and only after all updates are made to the gameObjects transform.
     * Do not change the transform of the gameObject inside this method, it will be ignored!
     *
     * @param changedTransform the new and updated transform value
     */
    void update(Transform changedTransform);

}