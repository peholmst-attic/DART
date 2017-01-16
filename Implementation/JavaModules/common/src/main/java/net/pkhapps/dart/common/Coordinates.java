package net.pkhapps.dart.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Objects;

/**
 * An immutable data type that represents a pair of geographical coordinates (latitude and longitude) in WGS 84
 * (SRID 4326).
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
    @JsonCreator
    public Coordinates(@JsonProperty("latitude") @NotNull BigDecimal latitude,
                       @JsonProperty("longitude") @NotNull BigDecimal longitude) {
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

    // TODO Document and null check

    public static class Builder {

        private BigDecimal latitude;
        private BigDecimal longitude;

        public Builder() {
        }

        public Builder(@NotNull Coordinates original) {
            this.latitude = original.latitude;
            this.longitude = original.longitude;
        }

        @NotNull
        public Builder withLatitude(@NotNull BigDecimal latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder withLatitude(double latitude) {
            this.latitude = new BigDecimal(latitude);
            return this;
        }

        @NotNull
        public Builder withLatitude(@NotNull String latitude) {
            this.latitude = new BigDecimal(latitude);
            return this;
        }

        @NotNull
        public Builder withLongitude(@NotNull BigDecimal longitude) {
            this.longitude = longitude;
            return this;
        }

        @NotNull
        public Builder withLongitude(double longitude) {
            this.longitude = new BigDecimal(longitude);
            return this;
        }

        @NotNull
        public Builder withLongitude(@NotNull String longitude) {
            this.longitude = new BigDecimal(longitude);
            return this;
        }

        @NotNull
        public Coordinates build() {
            return new Coordinates(latitude, longitude);
        }
    }

    /**
     * @return
     */
    @NotNull
    public static Builder builder() {
        return new Builder();
    }
}
