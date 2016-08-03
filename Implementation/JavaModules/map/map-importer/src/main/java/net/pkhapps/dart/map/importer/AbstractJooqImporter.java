package net.pkhapps.dart.map.importer;

import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;

import java.math.BigInteger;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

/**
 * Base class for map data importers that use JOOQ to insert data into the database. The importer requires three
 * different system properties to connect to the database: {@code jdbc.url}, {@code jdbc.user} and {@code jdbc.password}.
 */
public abstract class AbstractJooqImporter {

    private final String url;
    private final String user;
    private final String password;

    /**
     * Creates the importer and retrieves the system properties.
     *
     * @throws IllegalArgumentException if any of the required properties are missing.
     */
    protected AbstractJooqImporter() {
        url = getRequiredProperty("jdbc.url");
        user = getRequiredProperty("jdbc.user");
        password = getRequiredProperty("jdbc.password");
    }

    /**
     * Starts the import process. This is esentially the main entry point into the importer.
     *
     * @throws Exception if something goes wrong.
     */
    public final void importData() throws Exception {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            try (DSLContext context = DSL.using(connection)) {
                importData(context);
            }
        }
    }

    /**
     * The method that actually reads the data from the source and stores it into the database by using the specified
     * JOOQ {@code dslContext}.
     *
     * @param dslContext the DSL context to use.
     * @throws Exception if something goes wrong.
     */
    protected abstract void importData(DSLContext dslContext) throws Exception;

    /**
     * Returns the system property with the given {@code propertyName}.
     *
     * @param propertyName the name of the system property to fetch.
     * @return the value of the system property (never {@code null}).
     * @throws IllegalArgumentException if the property was not set.
     */
    protected static String getRequiredProperty(String propertyName) {
        String property = System.getProperty(propertyName);
        if (property == null) {
            throw new IllegalArgumentException("Please specify the " + propertyName + " system property");
        }
        return property;
    }

    /**
     * Inserts the {@code records} as a batch job using the specified {@code dslContext}.
     *
     * @param records    a list of records to batch insert.
     * @param dslContext the DSL context to use.
     */
    protected static void runBatch(List<? extends UpdatableRecord<?>> records, DSLContext dslContext) {
        System.out.println("Batch inserting " + records.size() + " records");
        try {
            dslContext.batchInsert(records).execute();
        } catch (DataAccessException ex) {
            if (ex.getCause() instanceof BatchUpdateException) {
                BatchUpdateException bue = (BatchUpdateException) ex.getCause();
                bue.getNextException().printStackTrace();
            }
            throw ex;
        }
        records.clear();
    }

    /**
     * Converts the specified {@code value} into a Long.
     *
     * @param value the value to covert.
     * @return the converted value, or {@code null} if {@code value} was {@code null}.
     * @throws IllegalArgumentException if the object could not be converted to a Long.
     */
    protected static Long toLong(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof BigInteger) {
            return ((BigInteger) value).longValue();
        } else if (value instanceof String) {
            return Long.valueOf(((String) value));
        } else if (value instanceof Long) {
            return (Long) value;
        } else {
            return Long.valueOf(value.toString());
        }
    }

    /**
     * Converts the specified {@code value} into an Integer.
     *
     * @param value the value to covert.
     * @return the converted value, or {@code null} if {@code value} was {@code null}.
     * @throws IllegalArgumentException if the object could not be converted to an Integer.
     */
    protected static Integer toInteger(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof BigInteger) {
            return ((BigInteger) value).intValue();
        } else if (value instanceof String) {
            return Integer.valueOf(((String) value));
        } else if (value instanceof Integer) {
            return (Integer) value;
        } else {
            return Integer.valueOf(value.toString());
        }
    }
}
