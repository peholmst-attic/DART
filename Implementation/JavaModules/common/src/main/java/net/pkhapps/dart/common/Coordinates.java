package net.pkhapps.dart.common;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Objects;

/**
 * An immutable data type that represents a pair of geographical coordinates (latitude and longitude) in WGS 84 (SRID 4326).
 */
public class Coordinates implements Serializable {

    private static final MathContext MATH_CONTEXT = new MathContext(9);
    private final BigDecimal latitude;
    private final BigDecimal longitude;

    /**
     * Creates a new {@code Coordinates}.
     *
     * @param latitude  the latitude coordinate in decimal degrees (DD).
     * @param longitude the longitude coordinate in decimal degrees (DD).
     */
    public Coordinates(@NotNull BigDecimal latitude, @NotNull BigDecimal longitude) {
        this.latitude = Objects.requireNonNull(latitude).round(MATH_CONTEXT);
        this.longitude = Objects.requireNonNull(longitude).round(MATH_CONTEXT);
    }

    /**
     * Returns the latitude coordinate (Y) in decimal degrees (DD).
     */
    @NotNull
    public BigDecimal getLatitude() {
        return latitude;
    }

    /**
     * Returns the longitude coordinate (X) in decimal degrees (DD).
     */
    @NotNull
    public BigDecimal getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return String.format("Coordinates(lat=%f, lon=%f)", latitude, longitude);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinates that = (Coordinates) o;

        if (!latitude.equals(that.latitude)) return false;
        return longitude.equals(that.longitude);
    }

    @Override
    public int hashCode() {
        int result = latitude.hashCode();
        result = 31 * result + longitude.hashCode();
        return result;
    }
}
