package net.pkhapps.dart.resources.handlers;

import net.pkhapps.dart.common.LocalizedString;
import net.pkhapps.dart.database.DSLContextFactory;
import net.pkhapps.dart.database.tables.records.CapabilitiesRecord;
import net.pkhapps.dart.messaging.handlers.RequestHandler;
import net.pkhapps.dart.resources.messages.AllResourceCapabilities;
import net.pkhapps.dart.resources.messages.GetAllResourceCapabilities;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;

import java.time.Clock;
import java.util.Objects;
import java.util.stream.Collectors;

import static net.pkhapps.dart.common.Locales.*;
import static net.pkhapps.dart.database.tables.Capabilities.CAPABILITIES;

public class GetAllResourceCapabilitiesHandler implements RequestHandler<GetAllResourceCapabilities, AllResourceCapabilities> {

    private final DSLContextFactory dslContextFactory;
    private final Clock clock;

    public GetAllResourceCapabilitiesHandler(@NotNull DSLContextFactory dslContextFactory, @NotNull Clock clock) {
        this.dslContextFactory = Objects.requireNonNull(dslContextFactory, "dslContextFactory must not be null");
        this.clock = Objects.requireNonNull(clock, "clock must not be null");
    }

    @Override
    @NotNull
    public AllResourceCapabilities handleRequest(@NotNull GetAllResourceCapabilities request) {
        Objects.requireNonNull(request, "request must not be null");
        try (final DSLContext create = dslContextFactory.create()) {
            // TODO Limit this query in case we end up receiving a LOT of results.
            // Getting lots of results would indicate there has been a mistake somewhere since there
            // should only be a couple of handfuls of capabilities.
            return new AllResourceCapabilities(clock.instant(), create.selectFrom(CAPABILITIES)
                    .fetch().stream().map(GetAllResourceCapabilitiesHandler::toPojo).collect(Collectors.toSet()));
        } // Error handling higher up
    }

    private static AllResourceCapabilities.ResourceCapability toPojo(CapabilitiesRecord record) {
        return new AllResourceCapabilities.ResourceCapability(record.getCapability(),
                LocalizedString.builder().with(SWEDISH, record.getDescriptionSv())
                        .with(FINNISH, record.getDescriptionFi())
                        .with(ENGLISH, record.getDescriptionEn()).build());
    }
}
