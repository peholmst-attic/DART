package net.pkhapps.dart.modules.accounts;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.PooledDataSource;
import com.netflix.config.DynamicPropertyFactory;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * CDI provider that creates and destroys a JDBC connection pool and a {@link org.jooq.DSLContext}.
 */
@ApplicationScoped
class DatabaseProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseProvider.class);

    private static final String JDBC_URL = "jdbc.url";
    private static final String JDBC_USER = "jdbc.user";
    private static final String JDBC_PASSWORD = "jdbc.password";
    private static final String JDBC_TEST_QUERY = "jdbc.testQuery";
    private static final String JOOQ_DIALECT = "jooq.dialect";

    @Produces
    @ApplicationScoped
    PooledDataSource createDataSource() {
        LOGGER.info("Creating data source");
        final DynamicPropertyFactory propertyFactory = DynamicPropertyFactory.getInstance();
        final String url = propertyFactory.getStringProperty(JDBC_URL, "").get();
        final String user = propertyFactory.getStringProperty(JDBC_USER, "").get();
        final String password = propertyFactory.getStringProperty(JDBC_PASSWORD, "").get();
        final String testQuery = propertyFactory.getStringProperty(JDBC_TEST_QUERY, "SELECT 1").get();

        final ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUser(user);
        dataSource.setPassword(password);
        dataSource.setTestConnectionOnCheckin(true);
        dataSource.setTestConnectionOnCheckout(true);
        dataSource.setPreferredTestQuery(testQuery);
        return dataSource;
    }

    void destroyDataSource(@Disposes PooledDataSource dataSource) {
        try {
            LOGGER.info("Closing data source [{}]", dataSource);
            dataSource.close();
        } catch (SQLException ex) {
            LOGGER.error("Error closing data source", ex);
        }
    }

    @Produces
    @ApplicationScoped
    DSLContext createDSLContext(DataSource dataSource) {
        LOGGER.info("Creating JOOQ DSL context");
        final SQLDialect dialect = SQLDialect.valueOf(
                DynamicPropertyFactory.getInstance().getStringProperty(JOOQ_DIALECT, SQLDialect.DEFAULT.name()).get());
        return DSL.using(dataSource, dialect);
    }

    void destroyDSLContext(@Disposes DSLContext dslContext) {
        LOGGER.info("Closing JOOQ DSL context [{}]", dslContext);
        dslContext.close();
    }
}
