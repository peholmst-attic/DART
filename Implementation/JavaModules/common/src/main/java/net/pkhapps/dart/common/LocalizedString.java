package net.pkhapps.dart.common;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class LocalizedString {

    private final Map<Locale, String> values;

    public LocalizedString(@NotNull Map<Locale, String> values) {
        Objects.requireNonNull(values, "values must not be null");
        this.values = new HashMap<>(values);
    }

    @NotNull
    public String get(@NotNull Locale locale) {
        Objects.requireNonNull(locale, "locale must not be null");
        return values.getOrDefault(locale, "");
    }

    public static class Builder {

        private final Map<Locale, String> values = new HashMap<>();

        @NotNull
        public Builder with(@NotNull Locale locale, @Nullable String value) {
            Objects.requireNonNull(locale, "locale must not be null");
            if (value == null) {
                values.remove(locale);
            } else {
                values.put(locale, value);
            }
            return this;
        }

        @NotNull
        public LocalizedString build() {
            return new LocalizedString(values);
        }
    }

    @NotNull
    public static Builder builder() {
        return new Builder();
    }
}
