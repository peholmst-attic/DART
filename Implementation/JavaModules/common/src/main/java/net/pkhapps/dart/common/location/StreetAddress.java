package net.pkhapps.dart.common.location;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

/**
 * A location feature that specifies a street address.
 */
public class StreetAddress extends LocationFeature {

    private final Road road;
    private final String number;
    private final String apartmentNumber;

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
    public Optional<String> getNumber() {
        return Optional.ofNullable(number);
    }

    /**
     * Returns the apartment number if available.
     */
    @NotNull
    public Optional<String> getApartmentNumber() {
        return Optional.ofNullable(apartmentNumber);
    }
}
