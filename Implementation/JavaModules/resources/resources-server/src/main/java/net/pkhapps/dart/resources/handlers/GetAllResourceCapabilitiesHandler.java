package net.pkhapps.dart.resources.handlers;

import net.pkhapps.dart.common.i18n.LocalizedString;
import net.pkhapps.dart.database.DSLContextFactory;
import net.pkhapps.dart.database.tables.records.CapabilitiesRecord;
import net.pkhapps.dart.messaging.handlers.RequestHandler;
import net.pkhapps.dart.resources.messages.AllResourceCapabilities;
import net.pkhapps.dart.resources.messages.GetAllResourceCapabilities;
import net.pkhapps.dart.resources.queries.CapabilityQuery;
import org.jooq.DSLContext;

import java.time.Clock;
import java.util.Objects;
import java.util.stream.Collectors;

import static net.pkhapps.dart.common.i18n.Locales.*;

/**
 * Handler for the {@link GetAllResourceCapabilities} message. Returns a {@link AllResourceCapabilities} response.
 */
public class GetAllResourceCapabilitiesHandler extends AbstractHandler implements RequestHandler<GetAllResourceCapabilities, AllResourceCapabilities> {

    private final CapabilityQuery capabilityQuery;

    /**
     * Constructor
     *
     * @param dslContextFactory the DSL context factory to use when creating JOOQ queries (not {@code null}).
     * @param capabilityQuery   the {@link CapabilityQuery} to use (not {@code null}).
     * @param clock             the clock to use for timestamps (not {@code null}).
     */
    public GetAllResourceCapabilitiesHandler(DSLContextFactory dslContextFactory, CapabilityQuery capabilityQuery, Clock clock) {
        super(dslContextFactory, clock);
        this.capabilityQuery = Objects.requireNonNull(capabilityQuery);
    }

    @Override
    public AllResourceCapabilities handleRequest(GetAllResourceCapabilities request) {
        Objects.requireNonNull(request);
        logger.trace("Handling [{}]", request);
        try (final DSLContext ctx = ctx()) {
            return new AllResourceCapabilities(now(), capabilityQuery.findAllCapabilities(ctx)
                    .stream().map(GetAllResourceCapabilitiesHandler::toPojo).collect(Collectors.toSet()));
        } // Error handling higher up
    }

    private static AllResourceCapabilities.ResourceCapability toPojo(CapabilitiesRecord record) {
        return new AllResourceCapabilities.ResourceCapability(record.getCapability(),
                LocalizedString.builder().with(SWEDISH, record.getDescriptionSv())
                        .with(FINNISH, record.getDescriptionFi())
                        .with(ENGLISH, record.getDescriptionEn()).build());
    }
}
