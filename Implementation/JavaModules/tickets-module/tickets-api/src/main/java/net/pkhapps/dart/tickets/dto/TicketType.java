package net.pkhapps.dart.tickets.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import net.pkhapps.dart.common.i18n.LocalizedString;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Immutable DTO for a ticket type.
 */
@JsonIdentityInfo(property = "id", generator = ObjectIdGenerators.PropertyGenerator.class)
public class TicketType {

    @JsonProperty(required = true)
    private long id;

    @JsonProperty(required = true)
    private String code;

    @JsonProperty(required = true)
    private LocalizedString name;

    @JsonProperty(required = true)
    private boolean active;

    @JsonProperty(required = true)
    private boolean priorityRequired;

    /**
     * Used by Jackson only.
     */
    private TicketType() {
        // NOP
    }

    /**
     * Creates a new {@code TicketType}.
     *
     * @param id               the ID of the ticket type.
     * @param code             the code of the ticket type.
     * @param name             the localized name of the ticket type.
     * @param active           whether the ticket type is active or not.
     * @param priorityRequired whether tickets of this type require a ticket priority or not.
     */
    public TicketType(long id, @NotNull String code, @NotNull LocalizedString name, boolean active, boolean priorityRequired) {
        this.id = id;
        this.code = Objects.requireNonNull(code, "code must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.active = active;
        this.priorityRequired = priorityRequired;
    }

    /**
     * Returns the ID of the ticket type. The ID is always unique.
     */
    public long getId() {
        return id;
    }

    /**
     * Returns the code of the ticket type. The code is unique within the set of active tickets.
     */
    @NotNull
    public String getCode() {
        return code;
    }

    /**
     * Returns the localized name of the ticket type.
     */
    @NotNull
    public LocalizedString getName() {
        return name;
    }

    /**
     * Returns whether this ticket type is active, i.e. usable for new tickets.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Returns whether tickets of this type always require a ticket priority.
     */
    public boolean isPriorityRequired() {
        return priorityRequired;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TicketType that = (TicketType) o;

        if (id != that.id) return false;
        if (active != that.active) return false;
        if (priorityRequired != that.priorityRequired) return false;
        if (!code.equals(that.code)) return false;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + code.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (active ? 1 : 0);
        result = 31 * result + (priorityRequired ? 1 : 0);
        return result;
    }
}
