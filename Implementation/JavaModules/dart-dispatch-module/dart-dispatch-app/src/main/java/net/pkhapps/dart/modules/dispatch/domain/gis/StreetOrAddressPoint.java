package net.pkhapps.dart.modules.dispatch.domain.gis;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Value object representing a Street or an Address point retrieved from a GIS.
 */
public class StreetOrAddressPoint extends AbstractGisObject {

    /**
     * Used by Spring Data only.
     */
    @SuppressWarnings("unused")
    private StreetOrAddressPoint() {
    }

    public StreetOrAddressPoint(@Nullable String gisId, @NotNull String name) {
        super(gisId, name);
    }

    public StreetOrAddressPoint(@Nullable String gisId, @NotNull String nameFi, @NotNull String nameSv) {
        super(gisId, nameFi, nameSv);
    }

    public StreetOrAddressPoint(@NotNull AbstractGisObject original) {
        super(original);
    }
}
