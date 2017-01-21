package net.pkhapps.dart.tickets.dto;

import com.fasterxml.jackson.annotation.*;
import net.pkhapps.dart.common.location.Location;
import net.pkhapps.dart.tickets.enums.TicketPriority;
import net.pkhapps.dart.tickets.enums.TicketState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * TODO document me
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIdentityInfo(property = "id", generator = ObjectIdGenerators.PropertyGenerator.class)
public class Ticket {

    // TODO Not sure if the subtickets should be included inside their parent or kept as separate tickets.

    @JsonProperty(required = true)
    private long id;

    @JsonProperty(required = true)
    private TicketState state;

    @JsonProperty(required = true)
    private Instant opened;

    @JsonProperty
    private Instant closed;

    @JsonProperty
    private TicketType type;

    @JsonProperty
    private TicketPriority priority;

    @JsonBackReference
    private Ticket parentTicket;

    @JsonProperty
    @JsonManagedReference
    private Set<Ticket> subtickets;

    @JsonProperty
    private Location location;

    @JsonProperty
    private String details;

    @JsonProperty
    private String reporterName;

    @JsonProperty
    private String reporterPhone;

    /**
     * Used by Jackson only.
     */
    private Ticket() {
        // NOP
    }

    /**
     * Creates a new {@code Ticket} with the minimum required properties only.
     *
     * @param id     the ID of the ticket.
     * @param state  the state of the ticket.
     * @param opened the instant when the ticket was opened.
     */
    public Ticket(long id, @NotNull TicketState state, @NotNull Instant opened) {
        this(id, state, opened, null, null, null, null, Stream.empty(), null, null, null, null);
    }

    /**
     * Creates a new {@code Ticket} with the given properties. The subtickets will get their parent ticket property
     * changed to point to the newly created ticket.
     *
     * @param id            the ID of the ticket.
     * @param state         the state of the ticket.
     * @param opened        the instant when the ticket was opened.
     * @param closed        the instant when the ticket was closed.
     * @param type          the type of the ticket.
     * @param priority      the priority of the ticket.
     * @param parentTicket  the parent of the ticket.
     * @param subtickets    the subtickets (children) of the ticket.
     * @param location      the location of the ticket.
     * @param details       the details of the ticket.
     * @param reporterName  the name of the person who reported the ticket.
     * @param reporterPhone the phone number of the person who reported the ticket.
     */
    public Ticket(long id, @NotNull TicketState state, @NotNull Instant opened, @Nullable Instant closed,
                  @Nullable TicketType type, @Nullable TicketPriority priority, @Nullable Ticket parentTicket,
                  @NotNull Stream<Ticket> subtickets, @Nullable Location location, @Nullable String details,
                  @Nullable String reporterName, @Nullable String reporterPhone) {
        this.id = id;
        this.state = Objects.requireNonNull(state, "state must not be null");
        this.opened = Objects.requireNonNull(opened, "opened must not be null");

        if (closed != null && state.isOpen()) {
            throw new IllegalArgumentException("Cannot specify closed instant when ticket is still in an open state");
        }
        if (closed == null && !state.isOpen()) {
            throw new IllegalArgumentException("Closed instant must not be null when ticket state is closed");
        }

        this.closed = closed;
        this.type = type;
        if (type != null && type.isPriorityRequired() && priority == null) {
            throw new IllegalArgumentException("Ticket type requires a priority");
        }
        this.priority = priority;
        this.parentTicket = parentTicket;
        this.subtickets = Objects.requireNonNull(subtickets, "subtickets must not be null but can be empty")
                .collect(Collectors.toSet());
        this.subtickets.forEach(ticket -> ticket.parentTicket = Ticket.this);
        this.location = location;
        this.details = details;
        this.reporterName = reporterName;
        this.reporterPhone = reporterPhone;
    }

    /**
     * Returns the ID of the ticket.
     */
    public long getId() {
        return id;
    }

    /**
     * Returns the state of the ticket.
     */
    @NotNull
    public TicketState getState() {
        return state;
    }

    /**
     * Returns the instant when the ticket was opened.
     */
    @NotNull
    public Instant getOpened() {
        return opened;
    }

    /**
     * Returns the instant when the ticket was closed, if it has been closed.
     *
     * @see #getState()
     */
    @NotNull
    @JsonIgnore
    public Optional<Instant> getClosed() {
        return Optional.ofNullable(closed);
    }

    /**
     * Returns the type of the ticket, if one has been selected.
     */
    @NotNull
    @JsonIgnore
    public Optional<TicketType> getType() {
        return Optional.ofNullable(type);
    }

    /**
     * Returns the priority of the ticket, if one has been selected.
     */
    @NotNull
    @JsonIgnore
    public Optional<TicketPriority> getPriority() {
        return Optional.ofNullable(priority);
    }

    /**
     * Returns the parent of the ticket, if it has one.
     *
     * @see #getSubtickets()
     */
    @NotNull
    @JsonIgnore
    public Optional<Ticket> getParentTicket() {
        return Optional.ofNullable(parentTicket);
    }

    /**
     * Returns an unmodifiable set of subtickets.
     *
     * @see #getParentTicket()
     */
    @NotNull
    @JsonIgnore
    public Set<Ticket> getSubtickets() {
        return Collections.unmodifiableSet(subtickets);
    }

    /**
     * Returns the location of the ticket, if one has been specified.
     */
    @NotNull
    @JsonIgnore
    public Optional<Location> getLocation() {
        return Optional.ofNullable(location);
    }

    /**
     * Returns the details of the ticket, if they have been added.
     */
    @NotNull
    @JsonIgnore
    public Optional<String> getDetails() {
        return Optional.ofNullable(details);
    }

    /**
     * Returns the name of the person who reported the ticket, if known.
     */
    @NotNull
    @JsonIgnore
    public Optional<String> getReporterName() {

        return Optional.ofNullable(reporterName);
    }

    /**
     * Returns the phone number of the person who reported the ticket, if known.
     */
    @NotNull
    @JsonIgnore
    public Optional<String> getReporterPhone() {
        return Optional.ofNullable(reporterPhone);
    }

    // TODO Equal and hashcode: only ID or entire value?

    // TODO Document builders once we know that the API is working

    /**
     *
     * @return
     */
    @NotNull
    public Builder derive() {
        return new Builder(this);
    }

    /**
     * @param <B>
     */
    public static abstract class AbstractBuilder<B extends AbstractBuilder<B>> {

        long id;
        TicketState state;
        Instant opened;
        Instant closed;
        TicketType type;
        TicketPriority priority;
        Location location;
        String details;
        String reporterName;
        String reporterPhone;

        /**
         *
         */
        AbstractBuilder() {
        }

        /**
         * @param original
         */
        AbstractBuilder(@NotNull Ticket original) {
            Objects.requireNonNull(original, "original must not be null");
            id = original.id;
            state = original.state;
            opened = original.opened;
            closed = original.closed;
            type = original.type;
            priority = original.priority;
            location = original.location;
            details = original.details;
            reporterName = original.reporterName;
            reporterPhone = original.reporterPhone;
        }

        /**
         * @param id
         * @return
         */
        public B withId(long id) {
            this.id = id;
            return thisInstance();
        }

        /**
         * @param state
         * @return
         */
        public B withState(@NotNull TicketState state) {
            this.state = Objects.requireNonNull(state, "state must not be null");
            return thisInstance();
        }

        /**
         * @param opened
         * @return
         */
        public B withOpened(@NotNull Instant opened) {
            this.opened = Objects.requireNonNull(opened, "opened must not be null");
            return thisInstance();
        }

        /**
         * @param closed
         * @return
         */
        public B withClosed(@Nullable Instant closed) {
            this.closed = closed;
            return thisInstance();
        }

        /**
         * @param type
         * @return
         */
        public B withType(@Nullable TicketType type) {
            this.type = type;
            return thisInstance();
        }

        /**
         * @param priority
         * @return
         */
        public B withPriority(@Nullable TicketPriority priority) {
            this.priority = priority;
            return thisInstance();
        }

        /**
         * @param location
         * @return
         */
        public B withLocation(@Nullable Location location) {
            this.location = location;
            return thisInstance();
        }

        /**
         * @param details
         * @return
         */
        public B withDetails(@Nullable String details) {
            this.details = details;
            return thisInstance();
        }

        /**
         * @param reporterName
         * @return
         */
        public B withReporterName(@Nullable String reporterName) {
            this.reporterName = reporterName;
            return thisInstance();
        }

        /**
         * @param reporterPhone
         * @return
         */
        public B withReporterPhone(@Nullable String reporterPhone) {
            this.reporterPhone = reporterPhone;
            return thisInstance();
        }

        @SuppressWarnings("unchecked")
        final B thisInstance() {
            return (B) this;
        }

    }

    /**
     *
     */
    public static class Builder extends AbstractBuilder<Builder> {

        private final Set<Subbuilder> subbuilders;

        /**
         *
         */
        public Builder() {
            subbuilders = new HashSet<>();
        }

        /**
         * @param original
         */
        Builder(Ticket original) {
            super(original);
            if (original.parentTicket != null){
                throw new UnsupportedOperationException("Only parent tickets can be used as originals for this builder");
            }
            subbuilders = original.subtickets.stream().map(ticket -> new Subbuilder(this, ticket))
                    .collect(Collectors.toSet());
        }

        /**
         * @return
         */
        public Subbuilder withSubticket() {
            Subbuilder subbuilder = new Subbuilder(this);
            subbuilders.add(subbuilder);
            return subbuilder;
        }

        /**
         *
         * @param id
         * @return
         */
        public Subbuilder withSubticket(long id) {
            Optional<Subbuilder> subbuilder = subbuilders.stream().filter(builder -> builder.id == id).findFirst();
            if (subbuilder.isPresent()) {
                return subbuilder.get();
            } else {
                return withSubticket().withId(id);
            }
        }

        /**
         * @return
         */
        public Ticket build() {
            Stream<Ticket> subtickets = subbuilders.stream().map(Subbuilder::build);
            return new Ticket(id, state, opened, closed, type, priority, null, subtickets, location,
                    details, reporterName, reporterPhone);
        }
    }

    /**
     *
     */
    public static class Subbuilder extends AbstractBuilder<Subbuilder> {

        private final Builder parentBuilder;

        Subbuilder(Builder parentBuilder) {
            this.parentBuilder = parentBuilder;
        }

        Subbuilder(Builder parentBuilder, Ticket original) {
            super(original);
            this.parentBuilder = parentBuilder;
        }

        Ticket build() {
            return new Ticket(id, state, opened, closed, type, priority, null, Stream.empty(), location,
                    details, reporterName, reporterPhone);
        }

        /**
         * @return
         */
        public Builder and() {
            return parentBuilder;
        }
    }

    /**
     *
     * @return
     */
    public static Builder builder() {
        return new Builder();
    }
}
