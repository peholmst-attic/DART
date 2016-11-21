package net.pkhapps.dart.map.api;

import net.pkhapps.dart.common.Coordinates;
import net.pkhapps.dart.common.location.Municipality;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for querying the municipality database.
 *
 * @see AddressPointService
 * @see RoadService
 * @see PlaceService
 */
public interface MunicipalityService {

    /**
     * Finds the municipalities that match the specified name search term.
     *
     * @param name  the name or a part of the name of the municipality, in any language. If the name is empty or
     *              {@code null}, no search will be performed and an empty list returned.
     * @param match the mode to use when looking for matches.
     * @return a list of matching municipalities.
     */
    @NotNull
    List<Municipality> findByName(@Nullable String name, @NotNull NameMatch match);

    /**
     * Finds the municipality that the specified coordinates are located in.
     *
     * @param coordinates the coordinates.
     * @return the municipality if found.
     */
    @NotNull
    Optional<Municipality> findByCoordinates(@NotNull Coordinates coordinates);

}
