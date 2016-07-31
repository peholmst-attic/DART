package net.pkhapps.dart.common;

import java.io.Serializable;
import java.util.*;

/**
 * TODO Implement me!
 */
public class CoordinatesList implements Serializable, Iterable<Coordinates> {

    private final List<Coordinates> coordinatesList;

    /**
     * @param coordinatesList
     */
    public CoordinatesList(List<Coordinates> coordinatesList) {
        this.coordinatesList = Collections.unmodifiableList(
                new ArrayList<>(Objects.requireNonNull(coordinatesList)));
    }

    /**
     * @param coordinatesArray
     */
    public CoordinatesList(Coordinates... coordinatesArray) {
        this(Arrays.asList(coordinatesArray));
    }

    /**
     * @return
     */
    public int size() {
        return coordinatesList.size();
    }

    /**
     * @param index
     * @return
     * @throws IndexOutOfBoundsException
     */
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
