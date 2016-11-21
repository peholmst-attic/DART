package net.pkhapps.dart.map.jooq;

import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Base class for integration tests that require access to the map database. The connection properties are in a file
 * named {@code integration-tests.properties} in the root of the classpath. The database is assumed to be a recent
 * PostGIS database.
 */
public abstract class AbstractIntegrationTest {

    private final DSLContext dslContext;

    public AbstractIntegrationTest() {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream("/integration-tests.properties"));
        } catch (IOException ex) {
            throw new RuntimeException("Could not read integration test properties", ex);
        }
        try {
            Connection connection = DriverManager.getConnection(properties.getProperty("jdbc.map.url"),
                    properties.getProperty("jdbc.map.user"), properties.getProperty("jdbc.map.password"));
            dslContext = DSL.using(connection);
        } catch (SQLException ex) {
            throw new RuntimeException("Could not connect to map database", ex);
        }
    }

    /**
     * Returns a JOOQ DSL context connected to the map database.
     */
    @NotNull
    protected DSLContext getDSLContext() {
        return dslContext;
    }
}
