package net.pkhapps.dart.resources.handlers;

import net.pkhapps.dart.common.Color;
import net.pkhapps.dart.common.LocalizedString;
import net.pkhapps.dart.database.DSLContextFactory;
import net.pkhapps.dart.database.tables.records.ResourceStateDescriptorsRecord;
import net.pkhapps.dart.messaging.handlers.RequestHandler;
import net.pkhapps.dart.resources.messages.AllResourceStates;
import net.pkhapps.dart.resources.messages.GetAllResourceStates;
import org.jooq.DSLContext;

import java.time.Clock;
import java.util.Objects;
import java.util.stream.Collectors;

import static net.pkhapps.dart.common.Locales.*;
import static net.pkhapps.dart.database.tables.ResourceStateDescriptors.RESOURCE_STATE_DESCRIPTORS;

public class GetAllResourceStatesHandler extends AbstractHandler implements RequestHandler<GetAllResourceStates, AllResourceStates> {

    public GetAllResourceStatesHandler(DSLContextFactory dslContextFactory, Clock clock) {
        super(dslContextFactory, clock);
    }

    @Override
    public AllResourceStates handleRequest(GetAllResourceStates request) {
        Objects.requireNonNull(request, "request must not be null");
        try (final DSLContext create = ctx()) {
            // No need to limit this query since the current database structure guarantees that only a handful of
            // records will be returned.
            return new AllResourceStates(now(), create.selectFrom(RESOURCE_STATE_DESCRIPTORS)
                    .fetch().stream().map(GetAllResourceStatesHandler::toPojo).collect(Collectors.toSet()));
        } // Error handling higher up
    }

    private static AllResourceStates.ResourceState toPojo(ResourceStateDescriptorsRecord record) {
        return new AllResourceStates.ResourceState(record.getState().name(),
                LocalizedString.builder().with(SWEDISH, record.getDescriptionSv())
                        .with(FINNISH, record.getDescriptionFi())
                        .with(ENGLISH, record.getDescriptionEn()).build(),
                record.getLocationTrackingEnabled(), Color.valueOf(record.getColor()));
    }
}
