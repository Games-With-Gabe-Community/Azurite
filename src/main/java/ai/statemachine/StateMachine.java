package ai.statemachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

/**
 * <h1>Azurite</h1>
 * A flexible implementation of a statemachine.
 * This class can represent a deterministic or non-deterministic statemachine,
 * however the actual transitions will never be random, therefore all statemachines are predictable in their transitions.
 * It involves an unlimited number of states and transitions between them, which can be automated by adding conditions to them.
 *
 * @author Juyas
 * @version 13.07.2021
 * @since 13.07.2021
 */
public class StateMachine {

    //contains all names mapped to their states
    private final HashMap<String, State> states;
    //contains all state names mapped to their transitions
    private final HashMap<String, List<Transition>> transitions;

    /**
     * The name of the current state of the machine.
     * Is null, if the machine is not started yet.
     */
    private String currentState;

    /**
     * Create a statemachine with no states or transitions.
     */
    public StateMachine() {
        this.states = new HashMap<>();
        this.transitions = new HashMap<>();
        this.currentState = null;
    }

    private void checkConditionalTransitions() {
        List<Transition> transitions = this.transitions.get(currentState);
        for (Transition transition : transitions) {
            if (transition.isConditionMet(getCurrentState())) {
                if (doTransition(transition.getToState()))
                    return;
            }
        }
    }

    /**
     * @see #currentState
     */
    public String getCurrentStateName() {
        return currentState;
    }

    /**
     * @return the current {@link State} of the machine
     */
    public State getCurrentState() {
        return states.get(currentState);
    }

    /**
     * Check if there is transition from a given state to a given state by their names.
     *
     * @param fromState the name of the state to start
     * @param toState   the name of the state to potentially transition to
     * @return true if and only if there is at least one know transition
     */
    public boolean hasTransition(String fromState, String toState) {
        return this.transitions.containsKey(fromState)
                && this.transitions.get(fromState).stream()
                .anyMatch(transition -> transition.getToState().equals(toState));
    }

    /**
     * Check if there is any known state that can transition to the given state.
     *
     * @param state the name of the state to potentially transition to
     * @return true if and only if there is any state that has a transition to the given one
     */
    public boolean canTransitionTo(String state) {
        return transitions.values().stream().anyMatch(trans -> trans.stream().anyMatch(t -> t.getToState().equals(state)));
    }

    /**
     * Check if there is any known transition outgoing from the given state.
     *
     * @param state the name of the state to transition from
     * @return true if and only if there is any transition starting at the given state
     */
    public boolean hasTransition(String state) {
        return transitions.containsKey(state);
    }

    /**
     * Check if a state is known by its name.
     *
     * @param name the name of the state
     * @return true if and only if there is a state by this name
     */
    public boolean hasState(String name) {
        return states.containsKey(name);
    }

    /**
     * Adds a new state to the machine.
     *
     * @param name  the name of the new state
     * @param state the new state
     * @return false if the state could not be added or a state with identical name already existed
     */
    public boolean addState(String name, State state) {
        if (!hasState(name)) {
            this.states.put(name, state);
            return true;
        }
        return false;
    }

    /**
     * Add a new transition.
     *
     * @return false, if the states are unknown
     * @see Transition#Transition(String, Function)
     */
    public boolean addTransition(String fromState, String toState, Function<State, Boolean> condition) {
        if (!hasState(fromState) || !hasState(toState)) return false;
        Transition transition = new Transition(toState, condition);
        if (this.transitions.containsKey(fromState)) {
            this.transitions.get(fromState).add(transition);
        } else {
            ArrayList<Transition> transitions = new ArrayList<>();
            transitions.add(transition);
            this.transitions.put(fromState, transitions);
        }
        return true;
    }

    /**
     * Attempts to start the state machine.
     * This is required to set the starting/current state of the machine and enable the update loop.
     * The method {@link #doTransition(String)} will fail as well, before this method is called.
     *
     * @param startState the state to start from
     * @return false, if the the statemachine could not be started, because
     * - startState is null
     * - startState is not known to the machine
     * - currentState is already not null, so machine already started
     */
    public boolean startMachine(String startState) {
        if (currentState == null && startState != null && states.containsKey(startState)) {
            currentState = startState;
            return true;
        }
        return false;
    }

    /**
     * Attempts to transition to the given state.
     *
     * @param toState the state to transition to
     * @return false, if and only if the given stte is unknown or null
     * @throws NullPointerException if the machine has not beeing started yet
     */
    public boolean doTransition(String toState) {
        if (toState == null || !states.containsKey(toState)) return false;
        getCurrentState().exitState();
        this.currentState = toState;
        getCurrentState().enterState();
        return true;
    }

    /**
     * Validate whether the state machine does not contain unreachable or deadlock states.
     *
     * @return false if and only if there are states, that do not transition or are orphaned
     */
    public boolean validate() {
        //State should have a transition to elsewhere and should be transitionable to
        for (String state : states.keySet()) {
            if (!hasTransition(state) || !canTransitionTo(state))
                return false;
        }
        return true;
    }

    /**
     * Update loop method.
     * Will do nothing, if the machine is not started.
     *
     * @param dt the delta time
     * @see #startMachine(String)
     */
    public void update(float dt) {
        if (currentState == null) return;
        checkConditionalTransitions();
        getCurrentState().updateState(dt);
    }

}