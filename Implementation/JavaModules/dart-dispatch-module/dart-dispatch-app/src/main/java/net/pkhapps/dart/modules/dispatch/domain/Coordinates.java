package net.pkhapps.dart.modules.dispatch.domain;

import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;

import java.io.Serializable;

/**
 * Value objects representing a pair of geographical coordinates.
 */
public class Coordinates implements Serializable {

    @GeoSpatialIndexed
    private Point point;

    /**
     * Used by Spring Data only.
     */
    @SuppressWarnings("unused")
    private Coordinates() {
    }

    public Coordinates(double latitude, double longitude) {
        requireWithinLimits(latitude, 90, -90);
        requireWithinLimits(longitude, 180, -180);
        this.point = new Point(longitude, latitude);
    }

    public double getLatitude() {
        return point.getY();
    }

    public double getLongitude() {
        return point.getX();
    }

    private static void requireWithinLimits(double coordinate, double max, double min) {
        if (coordinate > max) {
            throw new IllegalArgumentException("Coordinate must not be greater than " + max);
        }
        if (coordinate < min) {
            throw new IllegalArgumentException("Coordinate must not be less than " + min);
        }
    }

    @Override
    public String toString() {
        return String.format("Coordinates[lat=%s,lon=%s]", point.getY(), point.getX());
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

        return point.equals(that.point);
    }

    @Override
    public int hashCode() {
        return point.hashCode();
    }
}
