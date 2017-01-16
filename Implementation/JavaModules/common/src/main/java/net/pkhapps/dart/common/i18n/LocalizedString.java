package net.pkhapps.dart.common.i18n;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * An immutable text string that can have a default value and different localized values.
 *
 * @see #builder()
 */
public class LocalizedString {

    @JsonProperty(required = true)
    private Map<Locale, String> values;
    @JsonProperty(value = "default", required = true)
    private String defaultValue;

    // TODO Maybe default locale instead of default value?

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
        this(values, null);
    }

    /**
     * Creates a new {@code LocalizedString}.
     *
     * @param values       a map of values for different locales. The map will be copied.
     * @param defaultValue the default value to use (optional).
     */
    public LocalizedString(@NotNull Map<Locale, String> values, @Nullable String defaultValue) {
        Objects.requireNonNull(values, "values must not be null");
        this.values = new HashMap<>(values);
        this.defaultValue = defaultValue == null ? "" : defaultValue;
    }

    /**
     * Returns the value for the specified {@code locale}, or the default value if no localized value is found.
     *
     * @param locale the locale to use when looking up the string value.
     */
    @NotNull
    public String get(@NotNull Locale locale) {
        Objects.requireNonNull(locale);
        return Optional.ofNullable(values.get(locale)).orElse(defaultValue);
    }

    /**
     * Returns the default string value, e.g. when no locale information is available. If no default value has
     * been specified, an empty string is returned.
     */
    @NotNull
    @JsonIgnore
    public String getDefault() {
        return defaultValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocalizedString that = (LocalizedString) o;

        if (!values.equals(that.values)) return false;
        return defaultValue.equals(that.defaultValue);
    }

    @Override
    public int hashCode() {
        int result = values.hashCode();
        result = 31 * result + defaultValue.hashCode();
        return result;
    }

    /**
     * A builder class for constructing new {@code LocalizedString} instances.
     */
    public static class Builder {

        private final Map<Locale, String> values = new HashMap<>();
        private String defaultValue;

        /**
         * Sets the default string value of the builder instance.
         *
         * @param value the default string value.
         */
        @NotNull
        public Builder withDefault(@Nullable String value) {
            this.defaultValue = value;
            return this;
        }

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
            return new LocalizedString(values, defaultValue);
        }
    }

    /**
     * Creates a new builder for building a {@link LocalizedString} (this is easier to use than the constructors).
     */
    @NotNull
    public static Builder builder() {
        return new Builder();
    }
}
