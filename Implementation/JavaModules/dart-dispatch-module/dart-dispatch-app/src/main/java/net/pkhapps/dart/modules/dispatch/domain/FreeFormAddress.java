package net.pkhapps.dart.modules.dispatch.domain;

import net.pkhapps.dart.modules.dispatch.domain.gis.Municipality;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Value object representing a free form address that does not correspond to a location in a GIS system. This is used
 * for situations where the location is explained by the reporter.
 */
public class FreeFormAddress extends TicketAddress {

    private String address;

    private Municipality municipality;

    /**
     * Used by Spring Data only.
     */
    @SuppressWarnings("unused")
    private FreeFormAddress() {
    }

    public FreeFormAddress(
            @NotNull Coordinates coordinates,
            @NotNull CoordinateSource coordinateSource,
            @NotNull CoordinateAccuracy coordinateAccuracy,
            @NotNull String address,
            @NotNull Municipality municipality) {
        super(coordinates, coordinateSource, coordinateAccuracy);
        this.address = Objects.requireNonNull(address, "address must not be null");
        this.municipality = Objects.requireNonNull(municipality, "municipality must not be null");
    }

    @Override
    public @NotNull Municipality getMunicipality() {
        return municipality;
    }

    public @NotNull String getAddress() {
        return address;
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

        FreeFormAddress that = (FreeFormAddress) o;

        if (!address.equals(that.address)) {
            return false;
        }
        return municipality.equals(that.municipality);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + address.hashCode();
        result = 31 * result + municipality.hashCode();
        return result;
    }
}
