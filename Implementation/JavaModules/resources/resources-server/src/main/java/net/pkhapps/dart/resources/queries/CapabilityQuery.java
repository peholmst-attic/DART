package net.pkhapps.dart.resources.queries;

import net.pkhapps.dart.database.AbstractQuery;
import net.pkhapps.dart.database.DataSourceProperties;
import net.pkhapps.dart.database.tables.Capabilities;
import net.pkhapps.dart.database.tables.records.CapabilitiesRecord;
import org.jooq.DSLContext;

import java.util.List;

public class CapabilityQuery extends AbstractQuery {

    private static final String QUERY_NAME_FIND_ALL_CAPABILITIES = "findAllCapabilities";

    public CapabilityQuery(DataSourceProperties dataSourceProperties) {
        super(dataSourceProperties);
    }

    public List<CapabilitiesRecord> findAllCapabilities(DSLContext ctx) {
        return limitQuery(ctx.selectFrom(Capabilities.CAPABILITIES), QUERY_NAME_FIND_ALL_CAPABILITIES);
    }
}
