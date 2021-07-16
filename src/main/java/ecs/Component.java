package ecs;

import org.joml.Vector2f;
import util.debug.DebugPrimitive;

import java.util.Arrays;

/**
 * Abstract structure for ECS Components.
 * It is highly recommended to use this when implementing any system that can/should be applied to a GameObject.
 */

public abstract class Component implements Comparable<Component> {

    /**
     * Parent GameObject
     */
    public GameObject gameObject = null;

    /**
     * A list of conflicting class.
     * A component that does conflict with an existing component can not be added.
     */
    protected Class<?>[] conflicts;

    /**
     * Used to define a set order of components and their update cycle.
     * Required to maintain consistency in the update cycle.
     */
    protected int order = 0;

    public Component() {
        this.conflicts = new Class<?>[0];
    }

    public Component(Class<?>... conflicts) {
        this.conflicts = conflicts;
    }

    /**
     * Called once on Component initialization.
     */
    public void start() {
    }

    /**
     * Called once per frame for each Component
     *
     * @param dt Engine.deltaTime
     */
    public void update(float dt) {
    }

    /**
     * Get an array of debug lines to be rendered in debug mode
     *
     * @return an array of lines to be rendered in debug mode
     */
    public DebugPrimitive[] debug() {
        return null;
    }

    /**
     * Override to define conflicting component classes.
     * If a component is conflicting with another, an exception will be thrown.
     *
     * @param otherComponent the other component to be tested
     * @return true if and only if this component is conflicting with another
     */
    public boolean isConflictingWith(Class<? extends Component> otherComponent) {
        return conflicts.length > 0 && Arrays.stream(conflicts).anyMatch(conflict -> conflict.isAssignableFrom(otherComponent));
    }

    /**
     * Shortcut to get the current position of the parent gameobject.
     *
     * @return the current position of the parent gameobject
     */
    protected Vector2f position() {
        return gameObject.getReadOnlyTransform().getPosition();
    }

    //this method is primarily used to keep all components in order to update them properly
    @Override
    public int compareTo(Component o) {
        return order - o.order;
    }

    /**
     * Should be true, if this components will change the position of the gameobject somehow.
     *
     * @return false, if and only if the component will never modify the position of the gameobject in any way
     * @see GameObject#positionBuffer()
     */
    public boolean transformingObject() {
        return true;
    }

}
