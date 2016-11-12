package net.pkhapps.dart.resources.handlers;

import net.pkhapps.dart.common.Color;
import net.pkhapps.dart.common.i18n.LocalizedString;
import net.pkhapps.dart.database.DSLContextFactory;
import net.pkhapps.dart.database.tables.records.ResourceStateDescriptorsRecord;
import net.pkhapps.dart.messaging.handlers.RequestHandler;
import net.pkhapps.dart.resources.messages.AllResourceStates;
import net.pkhapps.dart.resources.messages.GetAllResourceStates;
import net.pkhapps.dart.resources.queries.ResourceStateDescriptorQuery;
import org.jooq.DSLContext;

import java.time.Clock;
import java.util.Objects;
import java.util.stream.Collectors;

import static net.pkhapps.dart.common.i18n.Locales.*;

/**
 * Handler for the {@link GetAllResourceStates} message. Returns a {@link AllResourceStates} response.
 */
public class GetAllResourceStatesHandler extends AbstractHandler implements RequestHandler<GetAllResourceStates, AllResourceStates> {

    private final ResourceStateDescriptorQuery resourceStateDescriptorQuery;

    /**
     * Constructor
     *
     * @param dslContextFactory            the DSL context factory to use when creating JOOQ queries (not {@code null}).
     * @param resourceStateDescriptorQuery the {@link ResourceStateDescriptorQuery} to use (not {@code null}).
     * @param clock                        the clock to use for timestamps (not {@code null}).
     */
    public GetAllResourceStatesHandler(DSLContextFactory dslContextFactory,
                                       ResourceStateDescriptorQuery resourceStateDescriptorQuery, Clock clock) {
        super(dslContextFactory, clock);
        this.resourceStateDescriptorQuery = Objects.requireNonNull(resourceStateDescriptorQuery);
    }

    @Override
    public AllResourceStates handleRequest(GetAllResourceStates request) {
        Objects.requireNonNull(request);
        logger.trace("Handling [{}]", request);
        try (final DSLContext ctx = ctx()) {
            return new AllResourceStates(now(), resourceStateDescriptorQuery.findAllResourceStateDescriptors(ctx)
                    .stream().map(GetAllResourceStatesHandler::toPojo).collect(Collectors.toSet()));
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
