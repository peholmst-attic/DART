package net.pkhapps.dart.common;

import java.time.Instant;
import java.util.Objects;

public class TemporalData<D> {

    private final D data;
    private final Instant lastChanged;

    public TemporalData(D data, Instant lastChanged) {
        this.data = Objects.requireNonNull(data, "data must not be null");
        this.lastChanged = Objects.requireNonNull(lastChanged, "lastChanged must not be null");
    }

    public D getData() {
        return data;
    }

    public Instant getLastChanged() {
        return lastChanged;
    }
}
