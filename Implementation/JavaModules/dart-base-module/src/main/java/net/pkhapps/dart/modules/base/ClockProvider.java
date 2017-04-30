package net.pkhapps.dart.modules.base;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.time.Clock;

/**
 * CDI producer that provides a {@link Clock} instance.
 */
@ApplicationScoped
class ClockProvider {

    @Produces
    @ApplicationScoped
    Clock getClock() {
        return Clock.systemUTC();
    }
}