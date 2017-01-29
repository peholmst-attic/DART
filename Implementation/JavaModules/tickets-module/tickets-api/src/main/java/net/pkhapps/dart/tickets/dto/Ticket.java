package net.pkhapps.dart.tickets.dto;

import com.fasterxml.jackson.annotation.*;
import net.pkhapps.dart.common.location.Location;
import net.pkhapps.dart.tickets.enums.TicketPriority;
import net.pkhapps.dart.tickets.enums.TicketState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * TODO document me
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIdentityInfo(property = "id", generator = ObjectIdGenerators.PropertyGenerator.class)
public class Ticket {

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
        this(id, state, opened, null, null, null, null, null, null, null);
    }

    /**
     * Creates a new {@code Ticket} with the given properties.
     *
     * @param id            the ID of the ticket.
     * @param state         the state of the ticket.
     * @param opened        the instant when the ticket was opened.
     * @param closed        the instant when the ticket was closed.
     * @param type          the type of the ticket.
     * @param priority      the priority of the ticket.
     * @param location      the location of the ticket.
     * @param details       the details of the ticket.
     * @param reporterName  the name of the person who reported the ticket.
     * @param reporterPhone the phone number of the person who reported the ticket.
     */
    public Ticket(long id, @NotNull TicketState state, @NotNull Instant opened, @Nullable Instant closed,
                  @Nullable TicketType type, @Nullable TicketPriority priority, @Nullable Location location,
                  @Nullable String details, @Nullable String reporterName, @Nullable String reporterPhone) {
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
     * @return
     */
    @NotNull
    public Builder derive() {
        return new Builder(this);
    }

    public static class Builder {

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
        public Builder() {
        }

        /**
         * @param original
         */
        public Builder(@NotNull Ticket original) {
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
        public Builder withId(long id) {
            this.id = id;
            return this;
        }

        /**
         * @param state
         * @return
         */
        public Builder withState(@NotNull TicketState state) {
            this.state = Objects.requireNonNull(state, "state must not be null");
            return this;
        }

        /**
         * @param opened
         * @return
         */
        public Builder withOpened(@NotNull Instant opened) {
            this.opened = Objects.requireNonNull(opened, "opened must not be null");
            return this;
        }

        /**
         * @param closed
         * @return
         */
        public Builder withClosed(@Nullable Instant closed) {
            this.closed = closed;
            return this;
        }

        /**
         * @param type
         * @return
         */
        public Builder withType(@Nullable TicketType type) {
            this.type = type;
            return this;
        }

        /**
         * @param priority
         * @return
         */
        public Builder withPriority(@Nullable TicketPriority priority) {
            this.priority = priority;
            return this;
        }

        /**
         * @param location
         * @return
         */
        public Builder withLocation(@Nullable Location location) {
            this.location = location;
            return this;
        }

        /**
         * @param details
         * @return
         */
        public Builder withDetails(@Nullable String details) {
            this.details = details;
            return this;
        }

        /**
         * @param reporterName
         * @return
         */
        public Builder withReporterName(@Nullable String reporterName) {
            this.reporterName = reporterName;
            return this;
        }

        /**
         * @param reporterPhone
         * @return
         */
        public Builder withReporterPhone(@Nullable String reporterPhone) {
            this.reporterPhone = reporterPhone;
            return this;
        }

        /**
         * @return
         */
        public Ticket build() {
            return new Ticket(id, state, opened, closed, type, priority, location, details, reporterName,
                    reporterPhone);
        }
    }


    /**
     * @return
     */
    public static Builder builder() {
        return new Builder();
    }
}
