package net.pkhapps.dart.resources.handlers;

import net.pkhapps.dart.database.DSLContextFactory;
import org.jooq.DSLContext;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

abstract class AbstractHandler {

    private final DSLContextFactory dslContextFactory;
    private final Clock clock;

    protected AbstractHandler(DSLContextFactory dslContextFactory, Clock clock) {
        this.dslContextFactory = Objects.requireNonNull(dslContextFactory);
        this.clock = Objects.requireNonNull(clock);
    }

    protected AbstractHandler() {
        this(DSLContextFactory.getInstance(), Clock.systemDefaultZone());
    }

    protected Instant now() {
        return clock.instant();
    }

    protected DSLContext ctx() {
        return dslContextFactory.create();
    }
}
