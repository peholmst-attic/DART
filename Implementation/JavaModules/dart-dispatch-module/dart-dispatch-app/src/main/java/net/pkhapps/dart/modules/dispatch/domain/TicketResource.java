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

    public TicketResource(@NotNull Ticket aggregateRoot, @NotNull String callSign, @NotNull Instant assigned) {
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
     */
    public @NotNull Instant getAssigned() {
        return assigned;
    }

    /**
     * Returns the instant when this resource was dispatched.
     */
    public @Nullable Instant getDispatched() {
        return dispatched;
    }

    /**
     * Returns the instant when this resource responded after being dispatched.
     */
    public @Nullable Instant getEnRoute() {
        return enRoute;
    }

    /**
     * Returns the instant when this resource arrived on scene.
     */
    public @Nullable Instant getOnScene() {
        return onScene;
    }

    /**
     * Returns the instant when this resource became available for another assignment again.
     */
    public @Nullable Instant getAvailable() {
        return available;
    }

    /**
     * Returns the instant when this resources returned to where it was when it became dispatched.
     */
    public @Nullable Instant getReturned() {
        return returned;
    }

    /**
     * Returns whether this resource is still assigned to the ticket or not.
     */
    public boolean isAssignedToTicket() {
        return available == null && returned == null;
    }
}
