package net.pkhapps.dart.modules.dispatch.domain.gis;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Value object representing a named object from a GIS.
 */
public abstract class AbstractGisObject implements Serializable {

    private String gisId;

    private String nameFi;

    private String nameSv;

    /**
     * Used by Spring Data only.
     */
    @SuppressWarnings("unused")
    protected AbstractGisObject() {
    }

    public AbstractGisObject(@Nullable String gisId, @NotNull String name) {
        this(gisId, name, name);
    }

    public AbstractGisObject(@Nullable String gisId, @NotNull String nameFi, @NotNull String nameSv) {
        this.gisId = gisId;
        this.nameFi = Objects.requireNonNull(nameFi, "nameFi must not be null");
        this.nameSv = Objects.requireNonNull(nameSv, "nameSv must not be null");
    }

    public AbstractGisObject(@NotNull AbstractGisObject original) {
        Objects.requireNonNull(original, "original must not be null");
        this.gisId = original.gisId;
        this.nameFi = original.nameFi;
        this.nameSv = original.nameSv;
    }

    public @Nullable String getGisId() {
        return gisId;
    }

    public @NotNull String getNameFi() {
        return nameFi;
    }

    public @NotNull String getNameSv() {
        return nameSv;
    }

    @Override
    public String toString() {
        return String.format("%s[gisId=%s,nameFi=%s,nameSv=%s]", getClass().getSimpleName(), gisId, nameFi, nameSv);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AbstractGisObject that = (AbstractGisObject) o;

        return (gisId != null ? gisId.equals(that.gisId) : that.gisId == null) && nameFi.equals(that.nameFi) &&
               nameSv.equals(that.nameSv);
    }

    @Override
    public int hashCode() {
        int result = gisId != null ? gisId.hashCode() : 0;
        result = 31 * result + nameFi.hashCode();
        result = 31 * result + nameSv.hashCode();
        return result;
    }
}
