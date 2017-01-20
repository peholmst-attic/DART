package net.pkhapps.dart.common.i18n;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * An immutable text string that can have different localized values.
 *
 * @see #builder()
 */
public class LocalizedString {

    @JsonProperty(required = true)
    private Map<Locale, String> values;

    /**
     * Used by Jackson only.
     */
    private LocalizedString() {
        // NOP
    }

    /**
     * Creates a new {@code LocalizedString}.
     *
     * @param values a map of values for different locales. The map will be copied.
     */
    public LocalizedString(@NotNull Map<Locale, String> values) {
        Objects.requireNonNull(values, "values must not be null");
        this.values = new HashMap<>(values);
    }

    /**
     * Returns the value for the specified {@code locale}, or an empty string if no localized value is found.
     *
     * @param locale the locale to use when looking up the string value.
     */
    @NotNull
    public String get(@NotNull Locale locale) {
        Objects.requireNonNull(locale);
        return Optional.ofNullable(values.get(locale)).orElse("");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocalizedString that = (LocalizedString) o;

        return values.equals(that.values);
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }

    /**
     * A builder class for constructing new {@code LocalizedString} instances.
     */
    public static class Builder {

        private final Map<Locale, String> values = new HashMap<>();

        /**
         * Adds a localized string value to the builder instance.
         *
         * @param locale the locale of the string.
         * @param value  the value of the string.
         */
        @NotNull
        public Builder with(@NotNull Locale locale, @Nullable String value) {
            Objects.requireNonNull(locale);
            if (value == null) {
                values.remove(locale);
            } else {
                values.put(locale, value);
            }
            return this;
        }

        /**
         * Builds and returns a new {@code LocalizedString} instance.
         */
        @NotNull
        public LocalizedString build() {
            return new LocalizedString(values);
        }
    }

    /**
     * Creates a new builder for building a {@code LocalizedString} (this is easier to use than the constructors).
     */
    @NotNull
    public static Builder builder() {
        return new Builder();
    }
}
