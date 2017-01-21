package net.pkhapps.dart.tickets.enums;

/**
 * Enumeration of ticket priorities.
 */
public enum TicketPriority {

    /**
     * Highest priority
     */
    A(0),

    B(1),

    C(2),

    /**
     * Lowest priority
     */
    D(3);

    final int priority;

    TicketPriority(int priority) {
        this.priority = priority;
    }

    /**
     * Returns a numerical value that can be used to compare priorities to each other. The smaller the value, the
     * higher the priority.
     */
    public int getPriority() {
        return priority;
    }
}
