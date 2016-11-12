package net.pkhapps.dart.map.api;

import net.pkhapps.dart.common.location.Municipality;
import net.pkhapps.dart.common.location.Road;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * TODO Document me!
 */
public interface RoadService {

    @NotNull
    List<Road> findByName(@NotNull Municipality municipality, @Nullable String name, @NotNull NameMatch match);

    @NotNull
    List<Road> findByNameAndAddressNumber(@NotNull Municipality municipality, @Nullable String name,
                                          @Nullable Integer addressNumber, @NotNull NameMatch match);

    @NotNull
    List<Road> findByNumber(@Nullable Integer number);

}
