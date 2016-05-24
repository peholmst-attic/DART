package net.pkhapps.dart.common;

import java.util.Locale;

/**
 * TODO Implement me
 */
public class LocalizedString {

    public static final Locale SWEDISH = new Locale("sv");
    public static final Locale FINNISH = new Locale("fi");
    public static final Locale ENGLISH = new Locale("en");

    public static class Builder {
        public Builder with(Locale locale, String value) {
            return this;
        }
        public LocalizedString build() {
            return null;
        }
    }
}
