package net.pkhapps.dart.common;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;

/**
 * An immutable data type representing a list of geographical coordinates.
 *
 * @see Coordinates
 */
public class CoordinatesList implements Serializable, Iterable<Coordinates> {

    private final List<Coordinates> coordinatesList;

    /**
     * Creates a new {@code CoordinatesList}.
     *
     * @param coordinatesList a list of coordinate pairs.
     */
    public CoordinatesList(@NotNull List<Coordinates> coordinatesList) {
        this.coordinatesList = Collections.unmodifiableList(
                new ArrayList<>(Objects.requireNonNull(coordinatesList)));
    }

    /**
     * Creates a new {@code CoordinatesList}.
     *
     * @param coordinatesArray an array of coordinate pairs.
     */
    public CoordinatesList(@NotNull Coordinates... coordinatesArray) {
        this(Arrays.asList(coordinatesArray));
    }

    /**
     * Returns the number of coordinate pairs in the list.
     */
    public int size() {
        return coordinatesList.size();
    }

    /**
     * Returns the coordinate pair with the given index.
     *
     * @param index the 0-based index of the coordinate pair within the list.
     * @return the coordinate pair.
     * @throws IndexOutOfBoundsException if the index was out of bounds.
     */
    @NotNull
    public Coordinates get(int index) throws IndexOutOfBoundsException {
        return coordinatesList.get(index);
    }

    @Override
    public Iterator<Coordinates> iterator() {
        return coordinatesList.iterator();
    }

    @Override
    public String toString() {
        return String.format("CoordinatesList(%s)", coordinatesList);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CoordinatesList that = (CoordinatesList) o;

        return coordinatesList.equals(that.coordinatesList);
    }

    @Override
    public int hashCode() {
        return coordinatesList.hashCode();
    }
}
