package net.pkhapps.dart.modules.dispatch.domain;

import net.pkhapps.dart.modules.dispatch.domain.gis.Municipality;
import net.pkhapps.dart.modules.dispatch.domain.gis.StreetOrAddressPoint;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Value object representing an intersection between two named streets.
 */
public class StreetIntersection extends TicketAddress {

    private StreetOrAddressPoint street1;

    private StreetOrAddressPoint street2;

    /**
     * Used by Spring Data only.
     */
    @SuppressWarnings("unused")
    private StreetIntersection() {
    }

    public StreetIntersection(
            @NotNull Coordinates coordinates,
            @NotNull CoordinateSource coordinateSource,
            @NotNull CoordinateAccuracy coordinateAccuracy,
            @NotNull StreetOrAddressPoint street1,
            @NotNull StreetOrAddressPoint street2) {
        super(coordinates, coordinateSource, coordinateAccuracy);
        this.street1 = Objects.requireNonNull(street1, "street1 must not be null");
        this.street2 = Objects.requireNonNull(street2, "street2 must not be null");
    }

    @Override
    public @NotNull Municipality getMunicipality() {
        return street1.getMunicipality();
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

        StreetIntersection that = (StreetIntersection) o;

        if (!street1.equals(that.street1)) {
            return false;
        }
        return street2.equals(that.street2);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + street1.hashCode();
        result = 31 * result + street2.hashCode();
        return result;
    }
}
