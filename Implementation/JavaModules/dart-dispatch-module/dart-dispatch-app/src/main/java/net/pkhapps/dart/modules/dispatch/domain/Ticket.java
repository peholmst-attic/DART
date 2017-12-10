package net.pkhapps.dart.modules.dispatch.domain;

import net.pkhapps.dart.modules.dispatch.domain.base.AbstractEventSourcedAggregateRoot;
import net.pkhapps.dart.modules.dispatch.domain.event.TicketClosed;
import net.pkhapps.dart.modules.dispatch.domain.event.TicketOpened;
import net.pkhapps.dart.modules.dispatch.domain.event.TicketUpdated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Entity representing a ticket, which corresponds to some incident that resources must be dispatched to handle.
 * The ticket contains information about the incident, where it is, how urgent it is, what resources have been
 * dispatched and when, etc.
 */
@Document(collection = "tickets")
public class Ticket extends AbstractEventSourcedAggregateRoot {

    @DBRef
    @Indexed
    private TicketType type;

    private TicketUrgency urgency;

    @Indexed
    private TicketState state;

    private TicketAddress address;

    private String details;

    private String reporter;

    private String reporterPhone;

    private List<TicketResource> resources;

    /**
     * Opens a new ticket and initializes its fields to their initial states.
     */
    public static class Open extends AbstractAction<Ticket> {

        @Override
        protected void doPerform(@NotNull Ticket aggregateRoot) {
            aggregateRoot.state = TicketState.NEW;
            aggregateRoot.urgency = TicketUrgency.C;
            aggregateRoot.resources = new ArrayList<>();
            aggregateRoot.details = "";
            aggregateRoot.reporter = "";
            aggregateRoot.reporterPhone = "";
            aggregateRoot.registerEvent(new TicketOpened(aggregateRoot));
        }
    }

    /**
     * Base class for actions that can be performed on open tickets.
     */
    public static abstract class AbstractTicketAction extends AbstractAction<Ticket> {

        @Override
        public boolean canPerform(@NotNull AbstractEventSourcedAggregateRoot aggregateRoot) {
            return (aggregateRoot instanceof Ticket) && canPerform((Ticket) aggregateRoot);
        }

        @Override
        public boolean willChangeState(@NotNull AbstractEventSourcedAggregateRoot aggregateRoot) {
            return (aggregateRoot instanceof Ticket) && willChangeState((Ticket) aggregateRoot);
        }

        /**
         * Checks if this action will change the state of the ticket. Returns {@code true} by default.
         */
        protected boolean willChangeState(@NotNull Ticket ticket) {
            return true;
        }

        /**
         * Checks if this action can be performed on the ticket. Returns true if the ticket is not closed by default.
         */
        protected boolean canPerform(@NotNull Ticket ticket) {
            return !ticket.getState().equals(TicketState.CLOSED);
        }
    }

    /**
     * Closes the ticket.
     */
    public static class Close extends AbstractTicketAction {

        @Override
        protected void doPerform(@NotNull Ticket aggregateRoot) {
            aggregateRoot.state = TicketState.CLOSED;
            aggregateRoot.registerEvent(new TicketClosed(aggregateRoot));
        }

        @Override
        protected boolean canPerform(@NotNull Ticket ticket) {
            return ticket.getState().canTransitionTo(TicketState.CLOSED);
        }
    }

    /**
     * Puts the ticket on hold.
     */
    public static class PutOnHold extends AbstractTicketAction {

        @Override
        protected void doPerform(@NotNull Ticket aggregateRoot) {
            aggregateRoot.state = TicketState.ON_HOLD;
            aggregateRoot.registerEvent(new TicketUpdated(aggregateRoot));
        }

        @Override
        protected boolean canPerform(@NotNull Ticket ticket) {
            return ticket.getState().canTransitionTo(TicketState.ON_HOLD);
        }

        // TODO Maybe add additional checks when you can put a ticket on hold?
    }

    /**
     * Sets the {@link Ticket#getType() type} of the ticket.
     */
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
            aggregateRoot.registerEvent(new TicketUpdated(aggregateRoot));
        }

        @Override
        protected boolean willChangeState(@NotNull Ticket ticket) {
            return !Objects.equals(type, ticket.type);
        }
    }

    /**
     * Sets the {@link Ticket#getUrgency() urgency} of the ticket.
     */
    public static class SetUrgency extends AbstractTicketAction {

        private final TicketUrgency urgency;

        @PersistenceConstructor
        SetUrgency(@NotNull TicketUrgency urgency) {
            this.urgency = Objects.requireNonNull(urgency, "urgency must not be null");
        }

        @Override
        protected void doPerform(@NotNull Ticket aggregateRoot) {
            aggregateRoot.urgency = urgency;
            aggregateRoot.registerEvent(new TicketUpdated(aggregateRoot));
        }

        @Override
        protected boolean willChangeState(@NotNull Ticket ticket) {
            return !Objects.equals(urgency, ticket.getUrgency());
        }
    }

    /**
     * Sets the {@link Ticket#getDetails() details} of the ticket.
     */
    public static class SetDetails extends AbstractTicketAction {

        private final String details;

        @PersistenceConstructor
        SetDetails(@Nullable String details) {
            this.details = details == null ? "" : details;
        }

        @Override
        protected void doPerform(@NotNull Ticket aggregateRoot) {
            aggregateRoot.details = details;
            aggregateRoot.registerEvent(new TicketUpdated(aggregateRoot));
        }

        @Override
        protected boolean willChangeState(@NotNull Ticket ticket) {
            return !Objects.equals(details, ticket.details);
        }
    }

    /**
     * Sets the {@link Ticket#getReporter() reporter} of the ticket.
     */
    public static class SetReporter extends AbstractTicketAction {

        private final String reporter;

        @PersistenceConstructor
        SetReporter(@Nullable String reporter) {
            this.reporter = reporter == null ? "" : reporter;
        }

        @Override
        protected void doPerform(@NotNull Ticket aggregateRoot) {
            aggregateRoot.reporter = reporter;
            aggregateRoot.registerEvent(new TicketUpdated(aggregateRoot));
        }

        @Override
        protected boolean willChangeState(@NotNull Ticket ticket) {
            return !Objects.equals(reporter, ticket.reporter);
        }
    }

    /**
     * Sets the {@link Ticket#getReporterPhone() reporterPhone} of the ticket.
     */
    public static class SetReporterPhone extends AbstractTicketAction {

        private final String reporterPhone;

        @PersistenceConstructor
        SetReporterPhone(@Nullable String reporterPhone) {
            this.reporterPhone = reporterPhone == null ? "" : reporterPhone;
        }

        @Override
        protected void doPerform(@NotNull Ticket aggregateRoot) {
            aggregateRoot.reporterPhone = reporterPhone;
            aggregateRoot.registerEvent(new TicketUpdated(aggregateRoot));
        }

        @Override
        protected boolean willChangeState(@NotNull Ticket ticket) {
            return !Objects.equals(reporterPhone, ticket.reporterPhone);
        }
    }

    /**
     * Sets the {@link Ticket#getAddress() address} of the ticket.
     */
    public static class SetAddress extends AbstractTicketAction {

        private final TicketAddress address;

        @PersistenceConstructor
        SetAddress(@NotNull TicketAddress address) {
            this.address = Objects.requireNonNull(address, "address must not be null");
        }

        @Override
        protected void doPerform(@NotNull Ticket aggregateRoot) {
            aggregateRoot.address = address;
            aggregateRoot.registerEvent(new TicketUpdated(aggregateRoot));
        }

        @Override
        protected boolean willChangeState(@NotNull Ticket ticket) {
            return !Objects.equals(address, ticket.address);
        }
    }

    /**
     * Assigns a resource to the ticket.
     */
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
            aggregateRoot.registerEvent(new TicketUpdated(aggregateRoot));
        }

        @Override
        protected boolean willChangeState(@NotNull Ticket ticket) {
            // If a resource has already been assigned to the ticket, it can't be reassigned
            return ticket.resources.stream().noneMatch(r -> r.getCallSign().equals(callSign) && r.isAssignedToTicket());
        }
    }

    /**
     * Records an event for a resource that has been assigned to the ticket.
     */
    public static class RecordResourceEvent extends AbstractTicketAction {

        private final String callSign;
        private final TicketResourceEventType eventType;
        private final Instant instant;

        @PersistenceConstructor
        RecordResourceEvent(@NotNull String callSign, @NotNull TicketResourceEventType eventType,
                            @NotNull Instant instant) {
            this.callSign = callSign;
            this.eventType = eventType;
            this.instant = instant;
        }

        @Override
        protected void doPerform(@NotNull Ticket aggregateRoot) {
            aggregateRoot.findLatestResource(callSign).ifPresent(resource -> doPerform(aggregateRoot, resource));
            aggregateRoot.registerEvent(new TicketUpdated(aggregateRoot));
        }

        private void doPerform(Ticket ticket, TicketResource resource) {
            if (resource.setEventInstantIfPossible(eventType, instant)) {
                switch (eventType) {
                    case DISPATCHED:
                        if (ticket.getState().canTransitionTo(TicketState.DISPATCHED)) {
                            ticket.state = TicketState.DISPATCHED;
                        }
                        break;
                    case AVAILABLE:
                    case RETURNED:
                        boolean resourcesHaveBeenAtTheScene = false;
                        boolean resourcesAreStillAssigned = false;

                        for (TicketResource r : ticket.resources) {
                            if (r.getOnScene() != null) {
                                resourcesHaveBeenAtTheScene = true;
                            }
                            if (r.isAssignedToTicket()) {
                                resourcesAreStillAssigned = true;
                            }
                        }

                        if (!resourcesAreStillAssigned) {
                            if (resourcesHaveBeenAtTheScene &&
                                ticket.getState().canTransitionTo(TicketState.UNDER_OBSERVATION)) {
                                ticket.state = TicketState.UNDER_OBSERVATION;
                            } else if (ticket.getState().canTransitionTo(TicketState.ON_HOLD)) {
                                ticket.state = TicketState.ON_HOLD;
                            }
                        }
                }
            }
        }

        @Override
        protected boolean willChangeState(@NotNull Ticket ticket) {
            return ticket.findLatestResource(callSign)
                    .filter(resource -> !Objects.equals(instant, resource.getEventInstant(eventType))).isPresent();
        }

        @Override
        protected boolean canPerform(@NotNull Ticket ticket) {
            if (super.canPerform(ticket)) {
                return ticket.findLatestResource(callSign).map(resource -> resource.canSet(eventType)).orElse(false);
            } else {
                return false;
            }
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

    public @NotNull String getReporter() {
        return reporter;
    }

    public void setReporter(@Nullable String reporter) {
        performAction(new SetReporter(reporter));
    }

    public @NotNull String getReporterPhone() {
        return reporterPhone;
    }

    public void setReporterPhone(@Nullable String reporterPhone) {
        performAction(new SetReporterPhone(reporterPhone));
    }

    public @Nullable TicketAddress getAddress() {
        return address;
    }

    public void setAddress(@NotNull TicketAddress address) {
        performAction(new SetAddress(address));
    }

    public @NotNull List<TicketResource> getResources() {
        return Collections.unmodifiableList(resources);
    }

    /**
     * Finds all ticket resources with the given call sign. The same call sign can be assigned multiple times,
     * for example if it detaches from an ongoing incident to attend another incident, then comes back.
     */
    public @NotNull List<TicketResource> findResources(@NotNull String callSign) {
        Objects.requireNonNull(callSign, "callSign must not be null");
        return this.resources.stream().filter(r -> r.getCallSign().equals(callSign))
                .collect(Collectors.toList());
    }

    /**
     * Finds the latest ticket resource with the given call sign. Latest in this case has nothing to do with
     * the timestamps but the order in which {@link #assignResource(String, Instant)} has been called.
     */
    public @NotNull Optional<TicketResource> findLatestResource(@NotNull String callSign) {
        List<TicketResource> resources = findResources(callSign);
        if (resources.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(resources.get(resources.size() - 1));
        }
    }

    /**
     * Assigns the given resource to the ticket.
     */
    public void assignResource(@NotNull String callSign, @NotNull Instant assigned) {
        performAction(new AssignResource(callSign, assigned));
    }

    /**
     * Records the given resource event.
     */
    public void recordResourceEvent(@NotNull String callSign, @NotNull TicketResourceEventType eventType,
                                    @NotNull Instant instant) {
        performAction(new RecordResourceEvent(callSign, eventType, instant));
    }

    public @NotNull TicketState getState() {
        return state;
    }

    /**
     * Opens a new ticket.
     */
    public static @NotNull Ticket open() {
        Ticket ticket = new Ticket();
        ticket.performAction(new Open());
        return ticket;
    }

    /**
     * Closes the ticket.
     */
    public void close() {
        performAction(new Close());
    }

    /**
     * Puts the ticket on hold.
     */
    public void putOnHold() {
        performAction(new PutOnHold());
    }
}
