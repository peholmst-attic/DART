package net.pkhapps.dart.resources.queries;

import net.pkhapps.dart.database.AbstractQuery;
import net.pkhapps.dart.database.DataSourceProperties;
import net.pkhapps.dart.database.tables.records.ResourceStateDescriptorsRecord;
import org.jooq.DSLContext;
import org.jooq.SelectLimitStep;

import java.util.List;
import java.util.Objects;

import static net.pkhapps.dart.database.tables.ResourceStateDescriptors.RESOURCE_STATE_DESCRIPTORS;

/**
 * Queries of the {@link net.pkhapps.dart.database.tables.ResourceStateDescriptors} table.
 */
public class ResourceStateDescriptorQuery extends AbstractQuery {

    private static final String QUERY_NAME_FIND_ALL_RESOURCE_STATE_DESCRIPTORS = "findAllResourceStateDescriptors";

    public ResourceStateDescriptorQuery(DataSourceProperties dataSourceProperties) {
        super(dataSourceProperties);
    }

    /**
     * Returns all the resource state descriptors currently stored in the database. The query is limited
     * and the name of the query is "{@value #QUERY_NAME_FIND_ALL_RESOURCE_STATE_DESCRIPTORS}".
     *
     * @param ctx the JOOQ DSL context to use (not {@code null}).
     * @return a list of records.
     * @throws net.pkhapps.dart.database.QueryResultLimitReachedException if the query limit is reached and the
     *                                                                    application has been configured to throw an
     *                                                                    exception when this happens.
     * @see #limitQuery(SelectLimitStep, String)
     */
    public List<ResourceStateDescriptorsRecord> findAllResourceStateDescriptors(DSLContext ctx) {
        Objects.requireNonNull(ctx);
        logger.trace("Finding all resource state descriptors using context [{}]", ctx);
        return limitQuery(ctx.selectFrom(RESOURCE_STATE_DESCRIPTORS), QUERY_NAME_FIND_ALL_RESOURCE_STATE_DESCRIPTORS);
    }
}
