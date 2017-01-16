package net.pkhapps.dart.common.location;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.pkhapps.dart.common.Coordinates;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

/**
 * An immutable data type representing a geographical location.
 *
 * @see LocationFeature
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Location {

    @JsonProperty(required = true)
    private final Coordinates coordinates;
    @JsonProperty
    private final String description;
    @JsonProperty(required = true)
    private final Municipality municipality;
    @JsonProperty(value = "accurate", required = true)
    private final boolean isAccurate;
    @JsonProperty
    private final LocationFeature feature;

    /**
     * Creates a new {@code Location}.
     *
     * @param coordinates  the coordinates of the location (must not be {@code null}).
     * @param description  a description of the location.
     * @param municipality the municipality of the location (must not be {@code null}).
     * @param isAccurate   whether the location is accurate or approximate.
     * @param feature      any additional feature that further enhances the location.
     */
    @JsonCreator
    public Location(@JsonProperty("coordinates") @NotNull Coordinates coordinates,
                    @JsonProperty("description") @Nullable String description,
                    @JsonProperty("municipality") @NotNull Municipality municipality,
                    @JsonProperty("accurate") boolean isAccurate,
                    @JsonProperty("feature") @Nullable LocationFeature feature) {
        this.coordinates = Objects.requireNonNull(coordinates);
        this.description = description;
        this.municipality = Objects.requireNonNull(municipality);
        this.isAccurate = isAccurate;
        this.feature = feature;
    }

    /**
     * Creates a new {@code Location}.
     *
     * @param coordinates  the coordinates of the location (must not be {@code null}).
     * @param municipality the municipality of the location (must not be {@code null}).
     * @param isAccurate   whether the location is accurate or approximate.
     */
    public Location(@NotNull Coordinates coordinates, @NotNull Municipality municipality, boolean isAccurate) {
        this(coordinates, null, municipality, isAccurate, null);
    }

    /**
     * Returns the geographical coordinates of the location. These may be accurate or approximate. A location
     * without any coordinates cannot exist (i.e. this method always returns a non-null value).
     *
     * @see #isAccurate()
     */
    @NotNull
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Returns whether this location is accurate (true) or approximate (false).
     * <p>
     * An accurate location is for example an exact street address, intersection or coordinates. An approximate location could be somewhere along a certain
     * road, somewhere on a certain island, in the forrest, in a location without an official name but with a name that locals
     * would probably recognize, etc.
     *
     * @see #getDescription()
     */
    @JsonIgnore
    public boolean isAccurate() {
        return isAccurate;
    }

    /**
     * Returns an optional textual description of the location. This may be especially useful if the location is
     * not accurate.
     *
     * @see #isAccurate()
     */
    @NotNull
    @JsonIgnore
    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    /**
     * Returns the municipality of the location.
     */
    @NotNull
    public Municipality getMunicipality() {
        return municipality;
    }

    /**
     * Returns an optional {@link LocationFeature} that further describes this geographical location.
     */
    @NotNull
    @JsonIgnore
    public Optional<LocationFeature> getFeature() {
        return Optional.ofNullable(feature);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (isAccurate != location.isAccurate) return false;
        if (!coordinates.equals(location.coordinates)) return false;
        if (description != null ? !description.equals(location.description) : location.description != null)
            return false;
        if (!municipality.equals(location.municipality)) return false;
        return feature != null ? feature.equals(location.feature) : location.feature == null;
    }

    @Override
    public int hashCode() {
        int result = coordinates.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + municipality.hashCode();
        result = 31 * result + (isAccurate ? 1 : 0);
        result = 31 * result + (feature != null ? feature.hashCode() : 0);
        return result;
    }
}
