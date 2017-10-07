package net.pkhapps.dart.modules.dispatch.domain.gis;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Value object representing a Street or an Address point retrieved from a GIS.
 */
public class StreetOrAddressPoint extends AbstractGisObject {

    private Municipality municipality;

    /**
     * Used by Spring Data only.
     */
    @SuppressWarnings("unused")
    private StreetOrAddressPoint() {
    }

    public StreetOrAddressPoint(@Nullable String gisId, @NotNull String name,
                                @NotNull Municipality municipality) {
        this(gisId, name, name, municipality);
    }

    public StreetOrAddressPoint(@Nullable String gisId, @NotNull String nameFi, @NotNull String nameSv,
                                @NotNull Municipality municipality) {
        super(gisId, nameFi, nameSv);
        this.municipality = Objects.requireNonNull(municipality, "municipality must not be null");
    }

    public StreetOrAddressPoint(@NotNull StreetOrAddressPoint original) {
        super(original);
        this.municipality = original.municipality;
    }

    public @NotNull Municipality getMunicipality() {
        return municipality;
    }
}
