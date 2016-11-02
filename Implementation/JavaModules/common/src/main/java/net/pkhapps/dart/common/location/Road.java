package net.pkhapps.dart.common.location;

import net.pkhapps.dart.common.LocalizedString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

/**
 * An immutable data type representing a road. No geographical attributes (like altitude, coordinates, etc) are included.
 */
public class Road {

    private final Integer number;
    private final Integer maxAddressNumber;
    private final Integer minAddressNumber;
    private final LocalizedString name;
    private final boolean isReliableSource;
    private final Municipality municipality;

    /**
     * Creates a new {@code Road}.
     *
     * @param number           the road number (optional).
     * @param maxAddressNumber the maximum address number (optional).
     * @param minAddressNumber the minimum address number (optional).
     * @param name             the name of the road.
     * @param isReliableSource whether the road is coming from a reliable source (a GIS, etc.) or not.
     * @param municipality     the municipality of the road (must not be {@code null}).
     */
    public Road(@Nullable Integer number, @Nullable Integer maxAddressNumber, @Nullable Integer minAddressNumber,
                @NotNull LocalizedString name, boolean isReliableSource, @NotNull Municipality municipality) {
        this.number = number;
        this.maxAddressNumber = maxAddressNumber;
        this.minAddressNumber = minAddressNumber;
        this.name = Objects.requireNonNull(name);
        this.isReliableSource = isReliableSource;
        this.municipality = Objects.requireNonNull(municipality);
    }

    /**
     * Creates a new {@code Road}.
     *
     * @param name             the name of the road (optional).
     * @param isReliableSource whether the road is coming from a reliable source (a GIS, etc.) or not.
     * @param municipality     the municipality of the road (optional).
     */
    public Road(@NotNull LocalizedString name, boolean isReliableSource, @NotNull Municipality municipality) {
        this(null, null, null, name, isReliableSource, municipality);
    }

    /**
     * Returns the official number of the road, if available.
     */
    @NotNull
    public Optional<Integer> getNumber() {
        return Optional.ofNullable(number);
    }

    /**
     * Returns the maximum address number of this road, if available. Please be aware that a road that spans
     * multiple municipalities may be split up into different {@code Road} objects with different address numbers.
     *
     * @see #getMinAddressNumber()
     */
    @NotNull
    public Optional<Integer> getMaxAddressNumber() {
        return Optional.ofNullable(maxAddressNumber);
    }

    /**
     * Returns the minimum address number of this road, if available. Please be aware that a road that spans
     * multiple municipalities may be split up into different {@code Road} objects.
     *
     * @see #getMaxAddressNumber()
     */
    @NotNull
    public Optional<Integer> getMinAddressNumber() {
        return Optional.of(minAddressNumber);
    }

    /**
     * Returns the localized name of the road.
     */
    @NotNull
    public LocalizedString getName() {
        return name;
    }

    /**
     * Returns whether the road data is coming from a reliable source (like a GIS or address list) or has been manually entered.
     */
    public boolean isReliableSource() {
        return isReliableSource;
    }

    /**
     * Returns the municipality that the road is located in.
     */
    @NotNull
    public Municipality getMunicipality() {
        return municipality;
    }
}
