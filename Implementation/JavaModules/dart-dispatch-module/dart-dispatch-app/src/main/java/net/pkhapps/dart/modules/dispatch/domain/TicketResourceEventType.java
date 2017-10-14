package net.pkhapps.dart.modules.dispatch.domain;

/**
 * Enumeration of resource state change events that are of interest to this module and needs to be recorded in a
 * {@link TicketResource}.
 */
public enum TicketResourceEventType {
    /**
     * @see TicketResource#getAssigned()
     */
    ASSIGNED,
    /**
     * @see TicketResource#getDispatched()
     */
    DISPATCHED,
    /**
     * @see TicketResource#getEnRoute()
     */
    EN_ROUTE,
    /**
     * @see TicketResource#getOnScene()
     */
    ON_SCENE,
    /**
     * @see TicketResource#getAvailable()
     */
    AVAILABLE,
    /**
     * @see TicketResource#getReturned()
     */
    RETURNED
}
