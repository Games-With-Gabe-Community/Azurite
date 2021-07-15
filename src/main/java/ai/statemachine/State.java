package ai.statemachine;

/**
 * <h1>Azurite</h1>
 * Defines a state inside a {@link StateMachine} and can basically do anything inside its boundaries.
 *
 * @author Juyas
 * @version 13.07.2021
 * @since 13.07.2021
 */
public interface State {

    /**
     * Called if the current state gets transitioned to.
     */
    void enterState();

    /**
     * Update loop call from the object using the {@link StateMachine}
     *
     * @param dt the delta time value
     */
    void updateState(float dt);

    /**
     * Called if the current state transitioned to another.
     * Call happens before update loop call and before the next states {@link #enterState()}
     */
    void exitState();

}