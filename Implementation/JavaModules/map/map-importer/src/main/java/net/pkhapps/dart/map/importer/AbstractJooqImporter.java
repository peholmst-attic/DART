package net.pkhapps.dart.map.importer;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * TODO Document me!
 */
public abstract class AbstractJooqImporter {

    private final String url;
    private final String user;
    private final String password;

    public AbstractJooqImporter() {
        url = getRequiredProperty("jdbc.url");
        user = getRequiredProperty("jdbc.user");
        password = getRequiredProperty("jdbc.password");
    }

    protected final void importData() throws Exception {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            try (DSLContext context = DSL.using(connection)) {
                importData(context);
            }
        }
    }

    protected abstract void importData(DSLContext dslContext) throws Exception;

    protected static String getRequiredProperty(String propertyName) {
        String property = System.getProperty(propertyName);
        if (property == null) {
            throw new IllegalArgumentException("Please specify the " + propertyName + " system property");
        }
        return property;
    }
}
