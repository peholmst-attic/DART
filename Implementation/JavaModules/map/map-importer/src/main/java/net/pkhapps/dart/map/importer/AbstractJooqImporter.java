package net.pkhapps.dart.map.importer;

import org.jooq.DSLContext;
import org.jooq.Table;
import org.jooq.UpdatableRecord;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;

import java.math.BigInteger;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

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

    private final AtomicLong batchCount = new AtomicLong();

    public interface BatchMode {
        void execute(List<? extends UpdatableRecord<?>> records, DSLContext dslContext);
    }

    public static BatchMode GroupAndReorder = (records, dslContext) -> {
        Map<Table, List<UpdatableRecord<?>>> tableToBatch = new HashMap<>();
        // This list is used to determine in which order the different table batches are to be executed
        List<Table<?>> tableOrder = new LinkedList<>();

        for (UpdatableRecord<?> record : records) {
            List<UpdatableRecord<?>> batch = tableToBatch.get(record.getTable());
            if (batch == null) {
                batch = new LinkedList<>();
                tableToBatch.put(record.getTable(), batch);
                tableOrder.add(record.getTable());
            }
            batch.add(record);
        }
        tableOrder.forEach(table -> {
            System.out.println("- Inserting into " + table.getName());
            dslContext.batchInsert(tableToBatch.get(table)).execute();
        });
    };

    public static BatchMode GroupAndKeepOrder = (records, dslContext) -> {
        Table<?> lastTable = null;
        List<UpdatableRecord<?>> batch = new LinkedList<>();
        for (UpdatableRecord<?> record : records) {
            if (lastTable == null) {
                lastTable = record.getTable();
            } else if (!lastTable.equals(record.getTable())) {
                System.out.println("- Inserting into " + lastTable.getName());
                dslContext.batchInsert(batch).execute();
                batch.clear();
                lastTable = record.getTable();
            }
            batch.add(record);
        }
        if (batch.size() > 0 && lastTable != null) {
            System.out.println("- Inserting into " + lastTable.getName());
            dslContext.batchInsert(batch).execute();
        }
    };

    public static BatchMode NoGrouping = (records, dslContext) -> {
        dslContext.batchInsert(records).execute();
    };

    /**
     * Inserts the {@code records} as a batch job using the specified {@code dslContext}.
     *
     * @param records    a list of records to batch insert.
     * @param dslContext the DSL context to use.
     */
    protected void runBatch(List<? extends UpdatableRecord<?>> records, DSLContext dslContext, BatchMode mode) {
        System.out.println(batchCount.incrementAndGet() + ": Batch inserting " + records.size() + " records");
        try {
            // We have to split up the batch inserting by table type, otherwise we get foreign key violations.
            // I don't exactly know why, maybe JOOQ is assuming you can only do batch inserting into one table
            // at a time.
            mode.execute(records, dslContext);
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
