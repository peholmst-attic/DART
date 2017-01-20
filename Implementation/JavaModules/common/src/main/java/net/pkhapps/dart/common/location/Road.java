package net.pkhapps.dart.common.location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.pkhapps.dart.common.i18n.LocalizedString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

/**
 * An immutable data type representing a road. No geographical attributes (like altitude, coordinates, etc) are included.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Road {

    @JsonProperty
    private Integer number;

    @JsonProperty
    private Integer maxAddressNumber;

    @JsonProperty
    private Integer minAddressNumber;

    @JsonProperty(required = true)
    private LocalizedString name;

    @JsonProperty(value = "reliable", required = true)
    private boolean isReliableSource;

    @JsonProperty(required = true)
    private Municipality municipality;

    /**
     * Used by Jackson only.
     */
    private Road() {
        // NOP
    }

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
    @JsonIgnore
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
    @JsonIgnore
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
    @JsonIgnore
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
    @JsonIgnore
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Road road = (Road) o;

        if (isReliableSource != road.isReliableSource) return false;
        if (number != null ? !number.equals(road.number) : road.number != null) return false;
        if (maxAddressNumber != null ? !maxAddressNumber.equals(road.maxAddressNumber) : road.maxAddressNumber != null)
            return false;
        if (minAddressNumber != null ? !minAddressNumber.equals(road.minAddressNumber) : road.minAddressNumber != null)
            return false;
        if (!name.equals(road.name)) return false;
        return municipality.equals(road.municipality);
    }

    @Override
    public int hashCode() {
        int result = number != null ? number.hashCode() : 0;
        result = 31 * result + (maxAddressNumber != null ? maxAddressNumber.hashCode() : 0);
        result = 31 * result + (minAddressNumber != null ? minAddressNumber.hashCode() : 0);
        result = 31 * result + name.hashCode();
        result = 31 * result + (isReliableSource ? 1 : 0);
        result = 31 * result + municipality.hashCode();
        return result;
    }
}
