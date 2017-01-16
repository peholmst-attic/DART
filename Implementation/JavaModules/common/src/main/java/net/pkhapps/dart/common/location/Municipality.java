package net.pkhapps.dart.common.location;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.pkhapps.dart.common.i18n.LocalizedString;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * An immutable data type representing a municipality.
 */
public class Municipality {

    @JsonProperty(required = true)
    private final long id;

    @JsonProperty(required = true)
    private final LocalizedString name;

    /**
     * Creates a new {@code Municipality}.
     *
     * @param id   the ID of the municipality.
     * @param name the localized name of the municipality.
     */
    @JsonCreator
    public Municipality(@JsonProperty("id") long id, @JsonProperty("name") @NotNull LocalizedString name) {
        this.id = id;
        this.name = Objects.requireNonNull(name);
    }

    /**
     * Returns the unique ID of this municipality. This may, but is not required to, be an official ID from the NLS.
     */
    public long getId() {
        return id;
    }

    /**
     * Returns the localized name of the municipality.
     */
    @NotNull
    public LocalizedString getName() {
        return name;
    }

    /**
     * Two {@code Municipality} objects are considered equal if they have the same ID.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Municipality that = (Municipality) o;

        return id == that.id;

    }

    /**
     * Two {@code Municipality} objects have the same hash code if they have the same ID.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
