package net.pkhapps.dart.common.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * A location feature that specifies an intersection between two (or more) roads.
 */
public class Intersection extends LocationFeature {

    @JsonProperty(required = true)
    private Set<Road> roads;

    /**
     * Used by Jackson only.
     */
    private Intersection() {
        // NOP
    }

    /**
     * Creates a new {@code Intersection}.
     *
     * @param roads an array of at least two {@code Road}s.
     */
    public Intersection(@NotNull Road... roads) {
        this(Arrays.asList(roads));
    }

    /**
     * Creates a new {@code Intersection}.
     *
     * @param roads a collection of at least two {@code Road}s.
     */
    public Intersection(@NotNull Collection<Road> roads) {
        Objects.requireNonNull(roads);
        this.roads = Collections.unmodifiableSet(new HashSet<>(roads));
        if (this.roads.size() < 2) { // We do the check on the set and not the collection in case the same Road instance is included twice.
            throw new IllegalArgumentException("At least two roads are needed for an intersection");
        }
    }

    /**
     * Returns all the roads that are intersecting (at least two).
     */
    public Set<Road> getRoads() {
        return roads;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Intersection that = (Intersection) o;

        return roads.equals(that.roads);
    }

    @Override
    public int hashCode() {
        return roads.hashCode();
    }
}
