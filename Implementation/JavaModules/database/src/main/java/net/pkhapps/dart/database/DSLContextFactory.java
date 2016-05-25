package net.pkhapps.dart.database;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

import java.util.Objects;

public class DSLContextFactory {

    private static DSLContextFactory INSTANCE;

    private final DataSourceProvider dataSourceProvider;
    private final DataSourceProperties dataSourceProperties;

    public DSLContextFactory(DataSourceProvider dataSourceProvider, DataSourceProperties dataSourceProperties) {
        this.dataSourceProvider = Objects.requireNonNull(dataSourceProvider, "dataSourceProvider must not be null");
        this.dataSourceProperties = Objects.requireNonNull(dataSourceProperties, "dataSourceProperties must not be null");
    }

    public DSLContext create() {
        return DSL.using(createConfiguration());
    }

    private Configuration createConfiguration() {
        return new DefaultConfiguration().set(dataSourceProvider.getDataSource()).set(getSQLDialect());
    }

    private SQLDialect getSQLDialect() {
        return SQLDialect.valueOf(dataSourceProperties.sqlDialect().get());
    }

    public static synchronized DSLContextFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DSLContextFactory(DataSourceProvider.getInstance(), DataSourceProperties.getInstance());
        }
        return INSTANCE;
    }
}
