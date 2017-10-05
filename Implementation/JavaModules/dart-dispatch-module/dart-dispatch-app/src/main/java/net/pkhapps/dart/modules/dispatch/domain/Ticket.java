package net.pkhapps.dart.modules.dispatch.domain;

import net.pkhapps.dart.modules.dispatch.domain.base.AbstractEventSourcedAggregateRoot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * TODO Document and complete me
 */
@Document
public class Ticket extends AbstractEventSourcedAggregateRoot {

    @DBRef
    private TicketType type;

    private TicketUrgency urgency;

    private TicketState state;

    private TicketAddress address;

    private String details;

    private String reporter;

    private String reporterPhone;

    private List<TicketResource> resources;

    public static class Open extends AbstractAction<Ticket> {

        @Override
        protected void doPerform(@NotNull Ticket aggregateRoot) {
            aggregateRoot.state = TicketState.NEW;
            aggregateRoot.urgency = TicketUrgency.C;
            aggregateRoot.resources = new ArrayList<>();
            aggregateRoot.details = "";
            aggregateRoot.reporter = "";
            aggregateRoot.reporterPhone = "";
        }
    }

    public static class Close extends AbstractAction<Ticket> {

        @Override
        protected void doPerform(@NotNull Ticket aggregateRoot) {
            aggregateRoot.state = TicketState.CLOSED;
        }
    }

    public static abstract class AbstractTicketAction extends AbstractAction<Ticket> {

        @Override
        public boolean canPerform(@NotNull AbstractEventSourcedAggregateRoot aggregateRoot) {
            return (aggregateRoot instanceof Ticket) && !((Ticket) aggregateRoot).getState().equals(TicketState.CLOSED);
        }
    }

    public static class SetType extends AbstractTicketAction {

        @DBRef
        private final TicketType type;

        @PersistenceConstructor
        SetType(@NotNull TicketType type) {
            this.type = Objects.requireNonNull(type, "type must not be null");
        }

        @Override
        protected void doPerform(@NotNull Ticket aggregateRoot) {
            aggregateRoot.type = type;
        }
    }

    public static class SetUrgency extends AbstractTicketAction {

        private final TicketUrgency urgency;

        @PersistenceConstructor
        SetUrgency(@NotNull TicketUrgency urgency) {
            this.urgency = Objects.requireNonNull(urgency, "urgency must not be null");
        }

        @Override
        protected void doPerform(@NotNull Ticket aggregateRoot) {
            aggregateRoot.urgency = urgency;
        }
    }

    public static class AssignResource extends AbstractTicketAction {

        private final String callSign;
        private final Instant assigned;

        @PersistenceConstructor
        AssignResource(@NotNull String callSign, @NotNull Instant assigned) {
            this.callSign = Objects.requireNonNull(callSign, "callSign must not be null");
            this.assigned = Objects.requireNonNull(assigned, "assigned must not be null");
        }

        @Override
        protected void doPerform(@NotNull Ticket aggregateRoot) {
            aggregateRoot.resources.add(new TicketResource(aggregateRoot, callSign, assigned));
        }
    }

    public static class SetDetails extends AbstractTicketAction {

        private final String details;

        @PersistenceConstructor
        SetDetails(@Nullable String details) {
            this.details = details == null ? "" : details;
        }

        @Override
        protected void doPerform(@NotNull Ticket aggregateRoot) {
            aggregateRoot.details = details;
        }
    }

    /**
     * Used by Spring Data only.
     */
    @SuppressWarnings("unused")
    private Ticket() {
    }

    public Ticket(@NotNull Ticket original) {
        super(original);
    }

    public @Nullable TicketType getType() {
        return type;
    }

    public void setType(@NotNull TicketType type) {
        performAction(new SetType(type));
    }

    public @NotNull TicketUrgency getUrgency() {
        return urgency;
    }

    public void setUrgency(@NotNull TicketUrgency urgency) {
        performAction(new SetUrgency(urgency));
    }

    public @NotNull String getDetails() {
        return details;
    }

    public void setDetails(@Nullable String details) {
        performAction(new SetDetails(details));
    }

    public @NotNull List<TicketResource> getResources() {
        return Collections.unmodifiableList(resources);
    }

    public void assignResource(@NotNull String callSign, @NotNull Instant assigned) {
        // If a resource has already been assigned to the ticket, it can't be reassigned
        if (resources.stream().noneMatch(r -> r.getCallSign().equals(callSign) && r.isAssignedToTicket())) {
            performAction(new AssignResource(callSign, assigned));
        }
    }

    // TODO Methods for chaning the timestamps (status) of the resources

    public @NotNull TicketState getState() {
        return state;
    }

    /**
     * Opens a new ticket.
     */
    public static Ticket open() {
        Ticket ticket = new Ticket();
        ticket.performAction(new Open());
        return ticket;
    }

    /**
     * Closes the ticket.
     */
    public void close() {
        if (getState().canTransitionTo(TicketState.CLOSED)) {
            performAction(new Close());
        }
    }
}
