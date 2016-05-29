package net.pkhapps.dart.resources.queries;

import net.pkhapps.dart.database.AbstractQuery;
import net.pkhapps.dart.database.DataSourceProperties;
import net.pkhapps.dart.database.tables.Capabilities;
import net.pkhapps.dart.database.tables.records.CapabilitiesRecord;
import org.jooq.DSLContext;
import org.jooq.SelectLimitStep;

import java.util.List;
import java.util.Objects;

/**
 * Queries of the {@link Capabilities} table.
 */
public class CapabilityQuery extends AbstractQuery {

    private static final String QUERY_NAME_FIND_ALL_CAPABILITIES = "findAllCapabilities";

    public CapabilityQuery(DataSourceProperties dataSourceProperties) {
        super(dataSourceProperties);
    }

    /**
     * Returns all the resource capabilities currently stored in the database. The query is limited
     * and the name of the query is "{@value #QUERY_NAME_FIND_ALL_CAPABILITIES}".
     *
     * @param ctx the JOOQ DSL context to use (not {@code null}).
     * @return a list of records.
     * @throws net.pkhapps.dart.database.QueryResultLimitReachedException if the query limit is reached and the
     *                                                                    application has been configured to throw an
     *                                                                    exception when this happens.
     * @see #limitQuery(SelectLimitStep, String)
     */
    public List<CapabilitiesRecord> findAllCapabilities(DSLContext ctx) {
        Objects.requireNonNull(ctx);
        logger.trace("Finding all resource capabilities using context [{}]", ctx);
        return limitQuery(ctx.selectFrom(Capabilities.CAPABILITIES), QUERY_NAME_FIND_ALL_CAPABILITIES);
    }
}
