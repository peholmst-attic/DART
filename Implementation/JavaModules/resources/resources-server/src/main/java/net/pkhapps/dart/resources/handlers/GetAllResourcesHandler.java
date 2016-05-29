package net.pkhapps.dart.resources.handlers;

import net.pkhapps.dart.database.DSLContextFactory;
import net.pkhapps.dart.database.tables.records.ResourcesRecord;
import net.pkhapps.dart.messaging.handlers.RequestHandler;
import net.pkhapps.dart.resources.messages.AllResources;
import net.pkhapps.dart.resources.messages.GetAllResources;
import net.pkhapps.dart.resources.queries.ResourceQuery;
import org.jooq.DSLContext;
import org.jooq.Record1;

import java.time.Clock;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handler for the {@link GetAllResources} message. Returns a {@link AllResources} response.
 */
public class GetAllResourcesHandler extends AbstractHandler implements RequestHandler<GetAllResources, AllResources> {

    private final ResourceQuery resourceQuery;

    /**
     * Constructor
     *
     * @param dslContextFactory the DSL context factory to use when creating JOOQ queries (not {@code null}).
     * @param resourceQuery     the {@link ResourceQuery} to use (not {@code null}).
     * @param clock             the clock to use for timestamps (not {@code null}).
     */
    public GetAllResourcesHandler(DSLContextFactory dslContextFactory, ResourceQuery resourceQuery, Clock clock) {
        super(dslContextFactory, clock);
        this.resourceQuery = Objects.requireNonNull(resourceQuery);
    }

    @Override
    public AllResources handleRequest(GetAllResources request) {
        Objects.requireNonNull(request);
        logger.trace("Handling [{}]", request);
        try (final DSLContext ctx = ctx()) {
            return new AllResources(now(), resourceQuery.findAllResources(ctx, request.isDisabledIncluded())
                    .stream().map(r ->
                            toPojo(r, resourceQuery.findCapabilitiesOfResource(ctx, r.getId())
                                    .stream().map(Record1::value1).collect(Collectors.toSet()))
                    ).collect(Collectors.toSet()));
        } // Error handling higher up
    }

    private static AllResources.Resource toPojo(ResourcesRecord record, Set<String> capabilities) {
        return new AllResources.Resource(record.getName(), capabilities, record.getDisabled());
    }
}
