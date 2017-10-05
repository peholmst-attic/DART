package net.pkhapps.dart.modules.dispatch.domain;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Value objects representing a pair of geographical coordinates.
 */
public class Coordinates implements Serializable {

    private BigDecimal latitude;
    private BigDecimal longitude;

    /**
     * Used by Spring Data only.
     */
    @SuppressWarnings("unused")
    private Coordinates() {
    }

    public Coordinates(@NotNull BigDecimal latitude, @NotNull BigDecimal longitude) {
        Objects.requireNonNull(latitude, "latitude must not be null");
        Objects.requireNonNull(longitude, "longitude must not be null");

        requireWithinLimits(latitude, new BigDecimal(90), new BigDecimal(-90));
        requireWithinLimits(longitude, new BigDecimal(180), new BigDecimal(-180));

        this.latitude = latitude;
        this.longitude = longitude;
    }

    public @NotNull BigDecimal getLatitude() {
        return latitude;
    }

    public @NotNull BigDecimal getLongitude() {
        return longitude;
    }

    private static void requireWithinLimits(BigDecimal coordinate, BigDecimal max, BigDecimal min) {
        if (coordinate.compareTo(max) > 0) {
            throw new IllegalArgumentException("Coordinate must not be greater than " + max);
        }
        if (coordinate.compareTo(min) < 0) {
            throw new IllegalArgumentException("Coordinate must not be less than " + min);
        }
    }

    @Override
    public String toString() {
        return String.format("Coordinates[lat=%s,lon=%s]", latitude, longitude);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Coordinates that = (Coordinates) o;

        if (!latitude.equals(that.latitude)) {
            return false;
        }
        return longitude.equals(that.longitude);
    }

    @Override
    public int hashCode() {
        int result = latitude.hashCode();
        result = 31 * result + longitude.hashCode();
        return result;
    }
}
