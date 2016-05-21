package net.pkhapps.dart.database;

import org.jooq.Configuration;
import org.jooq.DSLContext;

public class DSLContextFactory {

    // TODO Continue here

    private final DataSourceProvider dataSourceProvider;

    public DSLContextFactory(DataSourceProvider dataSourceProvider) {
        this.dataSourceProvider = dataSourceProvider;
    }

    public DSLContext create() {
        return null;
    }

    private Configuration createConfiguration() {
        return null;
    }
}
