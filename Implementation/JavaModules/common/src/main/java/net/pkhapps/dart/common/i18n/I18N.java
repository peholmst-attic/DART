package net.pkhapps.dart.common.i18n;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.*;

/**
 * TODO Document me!
 */
public class I18N {

    private final String baseName;
    private final UTF8Control utf8Control;
    private final Map<Locale, ResourceBundle> bundleMap = new HashMap<>();

    /**
     * @param baseName
     */
    public I18N(@NotNull String baseName) {
        this.baseName = Objects.requireNonNull(baseName);
        this.utf8Control = new UTF8Control();
    }

    /**
     * @param basePackage
     */
    public I18N(@NotNull Package basePackage) {
        this(Objects.requireNonNull(basePackage).getName() + ".messages");
    }

    private ResourceBundle getResourceBundle(Locale locale) {
        return bundleMap.computeIfAbsent(locale,
                k -> ResourceBundle.getBundle(baseName, Optional.ofNullable(locale)
                        .orElse(Locale.getDefault()), utf8Control));
    }

    /**
     * @param key
     * @param locale
     * @param parameters
     * @return
     */
    @NotNull
    public String getMessage(@NotNull String key, @Nullable Locale locale, @Nullable Object... parameters) {
        try {
            String string = getResourceBundle(locale).getString(key);
            if (parameters != null && parameters.length > 0) {
                return MessageFormat.format(string, parameters);
            } else {
                return string;
            }
        } catch (MissingResourceException ex) {
            return "!" + key;
        }
    }

    /**
     * @param locale
     * @return
     */
    @NotNull
    public I18N.MessageSource forLocale(@Nullable Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        return new MessageSource(Objects.requireNonNull(locale));
    }

    /**
     *
     */
    public class MessageSource {

        private final Locale locale;

        private MessageSource(@NotNull Locale locale) {
            this.locale = Objects.requireNonNull(locale);
        }

        /**
         * @return
         */
        @NotNull
        public Locale getLocale() {
            return locale;
        }

        /**
         * @param key
         * @param parameters
         * @return
         */
        @NotNull
        public String get(@NotNull String key, @Nullable Object... parameters) {
            return I18N.this.getMessage(key, locale, parameters);
        }
    }
}
