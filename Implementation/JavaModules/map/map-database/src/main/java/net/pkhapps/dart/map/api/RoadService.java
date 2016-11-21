package net.pkhapps.dart.map.api;

import net.pkhapps.dart.common.Coordinates;
import net.pkhapps.dart.common.location.Municipality;
import net.pkhapps.dart.common.location.Road;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for querying the road database.
 *
 * @see MunicipalityService
 * @see AddressPointService
 * @see PlaceService
 */
public interface RoadService {

    /**
     * Finds the roads that match the specified name search term.
     *
     * @param municipality the municipality to search in, or {@code null} if the municipality is not known.
     * @param name         the name or a part of the name of the road, in any language. If the name is empty or
     *                     {@code null}, no search will be performed and an empty list returned.
     * @param match        the mode to use when looking for matches.
     * @return a list of matching roads.
     */
    @NotNull
    List<Road> findByName(@Nullable Municipality municipality, @Nullable String name, @NotNull NameMatch match);

    /**
     * Finds the road and optional address number of the specified coordinates. If the coordinates are located in
     * an intersection between two roads, only one of the roads is returned (implementation may decide which one).
     *
     * @param coordinates the coordinates.
     * @return a pair of the road, and an optional address number.
     * @see #findByCoordinates(Coordinates)
     */
    @NotNull
    Optional<Pair<Road, Optional<Integer>>> findByCoordinates(@NotNull Coordinates coordinates);

    /**
     * Finds the approximate coordinates of the specified road and address number. If no address number is given,
     * the center of the road will be returned.
     *
     * @param road          the road.
     * @param addressNumber the address number or {@code null} if not found or not applicable.
     * @return the coordinates.
     * @see #findByCoordinates(Coordinates)
     */
    @NotNull
    Optional<Coordinates> findCoordinates(@NotNull Road road, @Nullable Integer addressNumber);

    /**
     * Finds all the roads that intersect the given road.
     *
     * @param road the road.
     * @return a list of intersecting roads.
     */
    @NotNull
    List<Road> findIntersectingRoads(@NotNull Road road);

    /**
     * Finds the intersection (or intersections) of the specified roads.
     *
     * @param road1 the first road.
     * @param road2 the second road.
     * @return a list of coordinates of the intersections between the specified road. If there are no intersections,
     * the list is empty.
     */
    @NotNull
    List<Coordinates> findIntersection(@NotNull Road road1, @NotNull Road road2);
}
