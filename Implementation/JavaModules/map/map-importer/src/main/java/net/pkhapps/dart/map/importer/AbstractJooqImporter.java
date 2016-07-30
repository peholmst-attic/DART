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

    protected static Long attributeValueToLong(Object attributeValue) {
        if (attributeValue == null) {
            return null;
        } else if (attributeValue instanceof BigInteger) {
            return ((BigInteger) attributeValue).longValue();
        } else if (attributeValue instanceof String) {
            return Long.valueOf(((String) attributeValue));
        } else if (attributeValue instanceof Long) {
            return (Long) attributeValue;
        } else {
            return Long.valueOf(attributeValue.toString());
        }
    }

    protected static Integer attributeValueToInteger(Object attributeValue) {
        if (attributeValue == null) {
            return null;
        } else if (attributeValue instanceof BigInteger) {
            return ((BigInteger) attributeValue).intValue();
        } else if (attributeValue instanceof String) {
            return Integer.valueOf(((String) attributeValue));
        } else if (attributeValue instanceof Integer) {
            return (Integer) attributeValue;
        } else {
            return Integer.valueOf(attributeValue.toString());
        }
    }

}
