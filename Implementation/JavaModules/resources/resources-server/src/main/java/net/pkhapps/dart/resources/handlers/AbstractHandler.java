package net.pkhapps.dart.resources.handlers;

import net.pkhapps.dart.database.DSLContextFactory;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

abstract class AbstractHandler {

    private final DSLContextFactory dslContextFactory;
    private final Clock clock;

    protected final Logger logger = LoggerFactory.getLogger(getClass());

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
