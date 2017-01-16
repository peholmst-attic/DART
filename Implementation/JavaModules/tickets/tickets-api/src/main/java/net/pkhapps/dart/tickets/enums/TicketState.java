package net.pkhapps.dart.tickets.enums;

/**
 * Enumeration of ticket states.
 */
public enum TicketState {
    /**
     * The ticket has been created but not yet dispatched.
     */
    CREATED(true),

    /**
     * The ticket has been dispatched to responders.
     */
    DISPATCHED(true),

    /**
     * The ticket has been put on hold.
     */
    ON_HOLD(true),

    /**
     * The ticket has been closed. No further changes can be made to it.
     */
    CLOSED(false);

    private final boolean open;

    TicketState(boolean open) {
        this.open = open;
    }

    /**
     * Returns whether the ticket is open or closed. A closed ticket can not be modified.
     */
    public boolean isOpen() {
        return open;
    }
}
