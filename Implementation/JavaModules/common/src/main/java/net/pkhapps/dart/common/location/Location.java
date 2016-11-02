package net.pkhapps.dart.common.location;

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
public class Location {

    private final Coordinates coordinates;
    private final String description;
    private final Municipality municipality;
    private final boolean isAccurate;
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
    public Location(@NotNull Coordinates coordinates, @Nullable String description, @NotNull Municipality municipality,
                    boolean isAccurate, @Nullable LocationFeature feature) {
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
    public Optional<LocationFeature> getFeature() {
        return Optional.ofNullable(feature);
    }
}
