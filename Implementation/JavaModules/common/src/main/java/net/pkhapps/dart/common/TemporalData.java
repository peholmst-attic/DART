package net.pkhapps.dart.common;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Objects;

public class TemporalData<D> {

    private final D data;
    private final Instant lastChanged;

    public TemporalData(@NotNull D data, @NotNull Instant lastChanged) {
        this.data = Objects.requireNonNull(data, "data must not be null");
        this.lastChanged = Objects.requireNonNull(lastChanged, "lastChanged must not be null");
    }

    @NotNull
    public D getData() {
        return data;
    }

    @NotNull
    public Instant getLastChanged() {
        return lastChanged;
    }
}
