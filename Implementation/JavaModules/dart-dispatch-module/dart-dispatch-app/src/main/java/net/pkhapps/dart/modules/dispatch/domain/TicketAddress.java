package net.pkhapps.dart.modules.dispatch.domain;

import net.pkhapps.dart.modules.dispatch.domain.gis.Municipality;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * Value object representing the address of a {@link Ticket}.
 */
public abstract class TicketAddress implements Serializable {

    private Coordinates coordinates;

    private CoordinateSource coordinateSource;

    private CoordinateAccuracy coordinateAccuracy;

    /**
     * Used by Spring Data only.
     */
    @SuppressWarnings("unused")
    protected TicketAddress() {
    }

    public TicketAddress(@NotNull Coordinates coordinates,
                         @NotNull CoordinateSource coordinateSource,
                         @NotNull CoordinateAccuracy coordinateAccuracy) {
        this.coordinates = Objects.requireNonNull(coordinates, "coordinates must not be null");
        this.coordinateSource = Objects.requireNonNull(coordinateSource, "coordinateSource must not be null");
        this.coordinateAccuracy = Objects.requireNonNull(coordinateAccuracy, "coordinateAccuracy must not be null");
    }

    public abstract @NotNull Municipality getMunicipality();

    public @NotNull Coordinates getCoordinates() {
        return coordinates;
    }

    public @NotNull CoordinateSource getCoordinateSource() {
        return coordinateSource;
    }

    public @NotNull CoordinateAccuracy getCoordinateAccuracy() {
        return coordinateAccuracy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TicketAddress that = (TicketAddress) o;

        if (!coordinates.equals(that.coordinates)) {
            return false;
        }
        if (coordinateSource != that.coordinateSource) {
            return false;
        }
        return coordinateAccuracy == that.coordinateAccuracy;
    }

    @Override
    public int hashCode() {
        int result = coordinates.hashCode();
        result = 31 * result + coordinateSource.hashCode();
        result = 31 * result + coordinateAccuracy.hashCode();
        return result;
    }
}
