package net.pkhapps.dart.common.ui.i18n;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import java.util.Locale;

/**
 * TODO Document me!
 */
public class I18NUIModule extends AbstractModule {

    private static Locale LOCALE = Locale.getDefault();

    /**
     * @param locale
     */
    public static void setLocale(Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        LOCALE = locale;
    }

    @Override
    protected void configure() {
    }

    /**
     * @return
     */
    @Provides
    Locale provideLocale() {
        return LOCALE;
    }
}
