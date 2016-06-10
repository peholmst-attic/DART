package net.pkhapps.dart.resources.services;

import net.pkhapps.dart.common.Coordinates;
import net.pkhapps.dart.database.enums.ResourceState;
import net.pkhapps.dart.database.tables.records.ResourcesRecord;
import net.pkhapps.dart.resources.queries.ResourceQuery;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.util.Objects;
import java.util.Optional;

import static net.pkhapps.dart.database.tables.ResourceLocations.RESOURCE_LOCATIONS;
import static net.pkhapps.dart.database.tables.ResourceStates.RESOURCE_STATES;

/**
 * TODO document me
 */
public class ResourceStatusService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Clock clock;
    private final ResourceQuery resourceQuery;

    /**
     * @param clock
     * @param resourceQuery
     */
    public ResourceStatusService(Clock clock, ResourceQuery resourceQuery) {
        this.clock = clock;
        this.resourceQuery = resourceQuery;
    }

    /**
     * @param ctx
     * @param resourceName
     * @param resourceState
     */
    public void recordState(DSLContext ctx, String resourceName, String resourceState) {
        Objects.requireNonNull(ctx);
        Objects.requireNonNull(resourceName);
        Objects.requireNonNull(resourceState);
        Optional<ResourcesRecord> resource = resourceQuery.findByName(ctx, resourceName);
        if (resource.isPresent()) {
            recordState(ctx, resource.get().getId(), ResourceState.valueOf(resourceState));
        } else {
            logger.warn("No resource with name [{}] was found", resourceName);
        }
    }

    /**
     * @param ctx
     * @param resourceId
     * @param state
     */
    public void recordState(DSLContext ctx, long resourceId, ResourceState state) {
        Objects.requireNonNull(ctx);
        Objects.requireNonNull(state);
        logger.debug("Recording state [{}] for resource {} using context [{}]", state, resourceId, ctx);
        ctx.insertInto(RESOURCE_STATES)
                .set(RESOURCE_STATES.STATE, state)
                .set(RESOURCE_STATES.RESOURCE_ID, resourceId)
                .set(RESOURCE_STATES.TS, clock.instant())
                .execute();
    }

    /**
     * @param ctx
     * @param resourceId
     * @param location
     */
    public void recordLocation(DSLContext ctx, long resourceId, Coordinates location) {
        Objects.requireNonNull(ctx);
        Objects.requireNonNull(location);
        logger.debug("Recording location [{}] for resource {} using context [{}]", location, resourceId, ctx);
        ctx.insertInto(RESOURCE_LOCATIONS)
                .set(RESOURCE_LOCATIONS.LOCATION, location)
                .set(RESOURCE_LOCATIONS.RESOURCE_ID, resourceId)
                .set(RESOURCE_STATES.TS, clock.instant())
                .execute();
    }
}
