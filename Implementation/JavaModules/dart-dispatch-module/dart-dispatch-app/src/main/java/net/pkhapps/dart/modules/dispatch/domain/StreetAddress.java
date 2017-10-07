package net.pkhapps.dart.modules.dispatch.domain;

import net.pkhapps.dart.modules.dispatch.domain.gis.Municipality;
import net.pkhapps.dart.modules.dispatch.domain.gis.StreetOrAddressPoint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Value object representing a street address (or an address point).
 */
public class StreetAddress extends TicketAddress {

    private StreetOrAddressPoint streetOrAddressPoint;
    private String number;

    /**
     * Used by Spring Data only.
     */
    @SuppressWarnings("unused")
    private StreetAddress() {
    }

    public StreetAddress(
            @NotNull Coordinates coordinates,
            @NotNull CoordinateSource coordinateSource,
            @NotNull CoordinateAccuracy coordinateAccuracy,
            @NotNull StreetOrAddressPoint streetOrAddressPoint,
            @Nullable String number) {
        super(coordinates, coordinateSource, coordinateAccuracy);
        this.streetOrAddressPoint =
                Objects.requireNonNull(streetOrAddressPoint, "streetOrAddressPoint must not be null");
        this.number = number;
    }

    @Override
    public @NotNull Municipality getMunicipality() {
        return streetOrAddressPoint.getMunicipality();
    }

    public @NotNull StreetOrAddressPoint getStreetOrAddressPoint() {
        return streetOrAddressPoint;
    }

    public @Nullable String getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        StreetAddress that = (StreetAddress) o;

        if (!streetOrAddressPoint.equals(that.streetOrAddressPoint)) {
            return false;
        }
        return number != null ? number.equals(that.number) : that.number == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + streetOrAddressPoint.hashCode();
        result = 31 * result + (number != null ? number.hashCode() : 0);
        return result;
    }
}
