package net.pkhapps.dart.modules.dispatch.domain;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Enumeration of ticket states.
 */
public enum TicketState {
    /**
     * The ticket is closed. No more changes can be made to it.
     */
    CLOSED,
    /**
     * Resources have been dispatched.
     */
    DISPATCHED(CLOSED),
    /**
     * The ticket is on hold, waiting for resources to become available.
     */
    ON_HOLD(DISPATCHED, CLOSED),
    /**
     * The ticket has just been opened. This is the first state.
     */
    NEW(DISPATCHED, ON_HOLD, CLOSED);

    final Set<TicketState> allowedTransitions;

    TicketState(TicketState... allowedTransitions) {
        this.allowedTransitions = new HashSet<>(Arrays.asList(allowedTransitions));
    }

    /**
     * Checks if this state can transfer to the next state.
     */
    public boolean canTransitionTo(@NotNull TicketState nextState) {
        Objects.requireNonNull(nextState, "nextState must not be null");
        return this.allowedTransitions.contains(nextState);
    }
}
