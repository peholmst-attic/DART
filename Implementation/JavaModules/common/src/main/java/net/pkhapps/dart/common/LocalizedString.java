package net.pkhapps.dart.common;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class LocalizedString {

    private final Map<Locale, String> values;

    public LocalizedString(Map<Locale, String> values) {
        Objects.requireNonNull(values);
        this.values = new HashMap<>(values);
    }

    public String get(Locale locale) {
        Objects.requireNonNull(locale);
        return values.getOrDefault(locale, "");
    }

    public static class Builder {

        private final Map<Locale, String> values = new HashMap<>();

        public Builder with(Locale locale, String value) {
            Objects.requireNonNull(locale);
            if (value == null) {
                values.remove(locale);
            } else {
                values.put(locale, value);
            }
            return this;
        }

        public LocalizedString build() {
            return new LocalizedString(values);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
