package net.pkhapps.dart.resources.queries;

import net.pkhapps.dart.database.AbstractQuery;
import net.pkhapps.dart.database.DataSourceProperties;
import net.pkhapps.dart.database.tables.records.ResourcesRecord;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.SelectLimitStep;
import org.jooq.SelectWhereStep;

import java.util.List;
import java.util.Objects;

import static net.pkhapps.dart.database.tables.Capabilities.CAPABILITIES;
import static net.pkhapps.dart.database.tables.ResourceCapabilities.RESOURCE_CAPABILITIES;
import static net.pkhapps.dart.database.tables.Resources.RESOURCES;

/**
 * Queries of the {@link net.pkhapps.dart.database.tables.Resources} table and its related tables.
 */
public class ResourceQuery extends AbstractQuery {

    private static final String QUERY_NAME_FIND_ALL_RESOURCES = "findAllResources";
    private static final String QUERY_NAME_FIND_CAPABILITIES_OF_RESOURCE = "findCapabilitiesOfResource";

    public ResourceQuery(DataSourceProperties dataSourceProperties) {
        super(dataSourceProperties);
    }

    /**
     * Returns all the resources currently stored in the database. The query is limited
     * and the name of the query is "{@value #QUERY_NAME_FIND_ALL_RESOURCES}".
     *
     * @param ctx          the JOOQ DSL context to use (not {@code null}).
     * @param alsoDisabled whether also disabled resources should be included.
     *                     If false, only active resources will be returned.
     * @return a list of records.
     * @throws net.pkhapps.dart.database.QueryResultLimitReachedException if the query limit is reached and the
     *                                                                    application has been configured to throw an
     *                                                                    exception when this happens.
     * @see #limitQuery(SelectLimitStep, String)
     */
    public List<ResourcesRecord> findAllResources(DSLContext ctx, boolean alsoDisabled) {
        Objects.requireNonNull(ctx);
        SelectWhereStep<ResourcesRecord> query = ctx.selectFrom(RESOURCES);
        if (!alsoDisabled) {
            logger.trace("Finding all active resources using context [{}]", ctx);
            query.where(RESOURCES.DISABLED.isFalse());
        } else {
            logger.trace("Finding all resources using context [{}]", ctx);
        }
        return limitQuery(query, QUERY_NAME_FIND_ALL_RESOURCES);
    }


    /**
     * Returns the capabilities as unique string names of the specified resource. The query is limited and
     * the name of the query is "{@value #QUERY_NAME_FIND_CAPABILITIES_OF_RESOURCE}".
     *
     * @param ctx        the JOOQ DSL context to use (not {@code null}).
     * @param resourceId the ID of the resource whose capabilities should be fetched.
     * @return a list of records.
     * @throws net.pkhapps.dart.database.QueryResultLimitReachedException if the query limit is reached and the
     *                                                                    application has been configured to throw an
     *                                                                    exception when this happens.
     * @see net.pkhapps.dart.database.tables.Capabilities#CAPABILITY
     * @see #limitQuery(SelectLimitStep, String)
     */
    public List<Record1<String>> findCapabilitiesOfResource(DSLContext ctx, long resourceId) {
        Objects.requireNonNull(ctx);
        logger.trace("Finding capabilities of resource {} using context [{}]", resourceId, ctx);
        return limitQuery(ctx.select(CAPABILITIES.CAPABILITY).from(CAPABILITIES).join(RESOURCE_CAPABILITIES)
                        .on(RESOURCE_CAPABILITIES.CAPABILITY_ID.eq(CAPABILITIES.ID))
                        .where(RESOURCE_CAPABILITIES.RESOURCE_ID.eq(resourceId)),
                QUERY_NAME_FIND_CAPABILITIES_OF_RESOURCE);
    }
}
