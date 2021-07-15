package ai.statemachine;

import java.util.function.Function;

/**
 * <h1>Azurite</h1>
 * Represents a transition inside a {@link StateMachine}.
 *
 * @author Juyas
 * @version 13.07.2021
 * @since 13.07.2021
 */
class Transition {

    /**
     * The state to transition to, if the condition is met.
     */
    private final String toState;
    /**
     * The method to check the condition.
     * If the condition is met, the transition will be become active and the {@link StateMachine} transitions to
     * {@link #toState}
     */
    private final Function<State, Boolean> condition;

    /**
     * Create a full transition.
     *
     * @param toState   the state to transition to
     * @param condition the condition to check
     */
    public Transition(String toState, Function<State, Boolean> condition) {
        this.toState = toState;
        this.condition = condition;
    }

    /**
     * Check if the condition is met for the given state.
     *
     * @param currentState the current state to check if it can transition this way
     * @return true if and only if the condition is met and the current state can transition to {@link #toState}
     */
    public boolean isConditionMet(State currentState) {
        return condition.apply(currentState);
    }

    /**
     * @see #toState
     */
    public String getToState() {
        return toState;
    }

}