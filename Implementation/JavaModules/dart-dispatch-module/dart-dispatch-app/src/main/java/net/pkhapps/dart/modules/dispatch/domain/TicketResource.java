package net.pkhapps.dart.modules.dispatch.domain;

import net.pkhapps.dart.base.domain.AbstractLocalEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Objects;

/**
 * Local entity representing a resource assignment for a ticket. The entity is used to track the state changes
 * of a resource.
 */
public class TicketResource extends AbstractLocalEntity {

    private String callSign;
    private Instant assigned;
    private Instant dispatched;
    private Instant enRoute;
    private Instant onScene;
    private Instant available;
    private Instant returned;

    /**
     * Used by Spring Data only.
     */
    @SuppressWarnings("unused")
    private TicketResource() {
    }

    TicketResource(@NotNull Ticket aggregateRoot, @NotNull String callSign, @NotNull Instant assigned) {
        super(aggregateRoot);
        this.callSign = Objects.requireNonNull(callSign, "callSign must not be null");
        this.assigned = Objects.requireNonNull(assigned, "assigned must not be null");
    }

    public TicketResource(@NotNull TicketResource original) {
        super(original);
        this.callSign = original.callSign;
        this.assigned = original.assigned;
        this.dispatched = original.dispatched;
        this.enRoute = original.enRoute;
        this.onScene = original.onScene;
        this.available = original.available;
        this.returned = original.returned;
    }

    public @NotNull String getCallSign() {
        return callSign;
    }

    /**
     * Returns the instant when this resource was assigned to the ticket.
     *
     * @see TicketResourceEventType#ASSIGNED
     */
    public @NotNull Instant getAssigned() {
        return assigned;
    }

    /**
     * Returns the instant when this resource was dispatched.
     *
     * @see TicketResourceEventType#DISPATCHED
     */
    public @Nullable Instant getDispatched() {
        return dispatched;
    }

    /**
     * Returns the instant when this resource responded after being dispatched.
     *
     * @see TicketResourceEventType#EN_ROUTE
     */
    public @Nullable Instant getEnRoute() {
        return enRoute;
    }

    /**
     * Returns the instant when this resource arrived on scene.
     *
     * @see TicketResourceEventType#ON_SCENE
     */
    public @Nullable Instant getOnScene() {
        return onScene;
    }

    /**
     * Returns the instant when this resource became available for another assignment again.
     *
     * @see TicketResourceEventType#AVAILABLE
     */
    public @Nullable Instant getAvailable() {
        return available;
    }

    /**
     * Returns the instant when this resources returned to where it was when it became dispatched.
     *
     * @see TicketResourceEventType#RETURNED
     */
    public @Nullable Instant getReturned() {
        return returned;
    }

    /**
     * Returns the instant for the given event type. This is a way of getting all the instants through a single method
     * instead of having to invoke the individual getters.
     */
    public @Nullable Instant getEventInstant(@NotNull TicketResourceEventType eventType) {
        Objects.requireNonNull(eventType, "eventType must not be null");
        switch (eventType) {
            case ASSIGNED:
                return assigned;
            case DISPATCHED:
                return dispatched;
            case EN_ROUTE:
                return enRoute;
            case ON_SCENE:
                return onScene;
            case AVAILABLE:
                return available;
            case RETURNED:
                return returned;
            default:
                return null;
        }
    }

    /**
     * Checks if the instant for the given event type can be set for this particular resource assignment. A
     * timestamp can only be set once. This method is designed to be used by {@link Ticket} only, which is why it is
     * package protected.
     */
    boolean canSet(@NotNull TicketResourceEventType eventType) {
        Objects.requireNonNull(eventType, "eventType must not be null");
        switch (eventType) {
            case ASSIGNED:
                if (assigned != null) {
                    return false;
                }
            case DISPATCHED:
                if (dispatched != null) {
                    return false;
                }
            case EN_ROUTE:
                if (enRoute != null) {
                    return false;
                }
            case ON_SCENE:
                if (onScene != null) {
                    return false;
                }
            case AVAILABLE:
                if (available != null) {
                    return false;
                }
            case RETURNED:
                if (returned != null) {
                    return false;
                }
            default:
                return true;
        }
    }

    /**
     * Sets the instant for the given event type if it can be set. This method is designed to be used by
     * {@link Ticket} only, which is why it is
     * package protected.
     *
     * @param eventType the event type
     * @param instant   the instant.
     * @return true if the instant was set, false if it was not.
     */
    boolean setEventInstantIfPossible(@NotNull TicketResourceEventType eventType, @NotNull Instant instant) {
        if (!canSet(eventType)) {
            return false;
        }
        Objects.requireNonNull(instant, "instant must not be null");
        switch (eventType) {
            case ASSIGNED:
                assigned = instant;
                return true;
            case DISPATCHED:
                dispatched = instant;
                return true;
            case EN_ROUTE:
                enRoute = instant;
                return true;
            case ON_SCENE:
                onScene = instant;
                return true;
            case AVAILABLE:
                available = instant;
                return true;
            case RETURNED:
                returned = instant;
                return true;
            default:
                return false;
        }
    }

    /**
     * Returns whether this resource is still assigned to the ticket or not.
     */
    public boolean isAssignedToTicket() {
        return available == null && returned == null;
    }
}
