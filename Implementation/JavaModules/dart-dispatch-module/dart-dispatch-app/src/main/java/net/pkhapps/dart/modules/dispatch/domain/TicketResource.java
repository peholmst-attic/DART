package net.pkhapps.dart.modules.dispatch.domain;

import net.pkhapps.dart.modules.dispatch.domain.base.AbstractLocalEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Objects;

/**
 * TODO Document me
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
     * TODO Document me!
     *
     * @param eventType
     * @return
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
     * TODO Document me!
     *
     * @param eventType
     * @param instant
     */
    void setEventInstantIfNotSet(@NotNull TicketResourceEventType eventType, @NotNull Instant instant) {
        Objects.requireNonNull(eventType, "eventType must not be null");
        Objects.requireNonNull(instant, "instant must not be null");
        switch (eventType) {
            case ASSIGNED:
                assigned = newValueIfOldValueIsNull(assigned, instant);
                break;
            case DISPATCHED:
                dispatched = newValueIfOldValueIsNull(dispatched, instant);
                break;
            case EN_ROUTE:
                enRoute = newValueIfOldValueIsNull(enRoute, instant);
                break;
            case ON_SCENE:
                onScene = newValueIfOldValueIsNull(onScene, instant);
                break;
            case AVAILABLE:
                available = newValueIfOldValueIsNull(available, instant);
                break;
            case RETURNED:
                returned = newValueIfOldValueIsNull(returned, instant);
                break;
        }
    }

    private static <T> T newValueIfOldValueIsNull(T oldValue, T newValue) {
        return oldValue != null ? oldValue : newValue;
    }

    /**
     * Returns whether this resource is still assigned to the ticket or not.
     */
    public boolean isAssignedToTicket() {
        return available == null && returned == null;
    }
}
