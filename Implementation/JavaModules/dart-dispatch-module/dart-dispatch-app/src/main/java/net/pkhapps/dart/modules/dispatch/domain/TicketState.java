package net.pkhapps.dart.modules.dispatch.domain;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Enumeration of ticket states.
 */
public enum TicketState {
    /**
     * The ticket is closed. No more changes can be made to it.
     */
    CLOSED,
    /**
     * Resources have been dispatched and left the scene, but will return later for checkups etc.
     */
    UNDER_OBSERVATION,
    /**
     * Resources have been dispatched.
     */
    DISPATCHED,
    /**
     * The ticket is on hold, waiting for resources to become available. No resource have yet arrived at the scene.
     */
    ON_HOLD,
    /**
     * The ticket has just been opened. This is the first state.
     */
    NEW;

    static Map<TicketState, Set<TicketState>> TRANSITION_RULES;

    static {
        TRANSITION_RULES = new HashMap<>();
        TRANSITION_RULES.put(CLOSED, Collections.emptySet());
        TRANSITION_RULES.put(UNDER_OBSERVATION, set(CLOSED, DISPATCHED));
        TRANSITION_RULES.put(DISPATCHED, set(UNDER_OBSERVATION, ON_HOLD, CLOSED));
        TRANSITION_RULES.put(ON_HOLD, set(DISPATCHED, CLOSED));
        TRANSITION_RULES.put(NEW, set(DISPATCHED, ON_HOLD, CLOSED));
    }

    private static <T> Set<T> set(T... items) {
        return new HashSet<>(Arrays.asList(items));
    }

    /**
     * Checks if this state can transfer to the next state.
     */
    public boolean canTransitionTo(@NotNull TicketState nextState) {
        Objects.requireNonNull(nextState, "nextState must not be null");
        return TRANSITION_RULES.get(this).contains(nextState);
    }
}
