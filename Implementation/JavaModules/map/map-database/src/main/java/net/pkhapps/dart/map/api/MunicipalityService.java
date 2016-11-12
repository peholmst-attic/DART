package net.pkhapps.dart.map.api;

import net.pkhapps.dart.common.Coordinates;
import net.pkhapps.dart.common.location.Municipality;

import java.util.List;
import java.util.Optional;

/**
 * TODO Document me!
 */
public interface MunicipalityService {

    List<Municipality> findByName(String name);

    Optional<Municipality> findByCoordinates(Coordinates coordinates);

}
