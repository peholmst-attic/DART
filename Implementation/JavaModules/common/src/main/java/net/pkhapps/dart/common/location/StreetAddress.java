package net.pkhapps.dart.common.location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

/**
 * A location feature that specifies a street address.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StreetAddress extends LocationFeature {

    @JsonProperty(required = true)
    private Road road;

    @JsonProperty
    private String number;

    @JsonProperty
    private String apartmentNumber;

    /**
     * Used by Jackson only.
     */
    private StreetAddress() {
        // NOP
    }

    /**
     * Creates a new {@code StreetAddress}.
     *
     * @param road            the road.
     * @param number          the address number (optional).
     * @param apartmentNumber the apartment number or similar (optional).
     */
    public StreetAddress(@NotNull Road road, @Nullable String number, @Nullable String apartmentNumber) {
        this.road = Objects.requireNonNull(road);
        this.number = number;
        this.apartmentNumber = apartmentNumber;
    }

    /**
     * Returns the road of the address.
     */
    @NotNull
    public Road getRoad() {
        return road;
    }

    /**
     * Returns the address number if available.
     */
    @NotNull
    @JsonIgnore
    public Optional<String> getNumber() {
        return Optional.ofNullable(number);
    }

    /**
     * Returns the apartment number if available.
     */
    @NotNull
    @JsonIgnore
    public Optional<String> getApartmentNumber() {
        return Optional.ofNullable(apartmentNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StreetAddress that = (StreetAddress) o;

        if (!road.equals(that.road)) return false;
        if (number != null ? !number.equals(that.number) : that.number != null) return false;
        return apartmentNumber != null ? apartmentNumber.equals(that.apartmentNumber) : that.apartmentNumber == null;
    }

    @Override
    public int hashCode() {
        int result = road.hashCode();
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + (apartmentNumber != null ? apartmentNumber.hashCode() : 0);
        return result;
    }
}
