package net.pkhapps.dart.modules.dispatch.domain.gis;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Value object representing a Municipality retrieved from a GIS.
 */
public class Municipality extends AbstractGisObject {

    /**
     * Used by Spring Data only.
     */
    @SuppressWarnings("unused")
    private Municipality() {
    }

    public Municipality(@Nullable String gisId, @NotNull String name) {
        super(gisId, name);
    }

    public Municipality(@Nullable String gisId, @NotNull String nameFi, @NotNull String nameSv) {
        super(gisId, nameFi, nameSv);
    }

    public Municipality(@NotNull Municipality original) {
        super(original);
    }
}
