package net.pkhapps.dart.database;

import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import javax.sql.DataSource;
import java.util.Objects;

public class DataSourceProvider {

    private static DataSourceProvider INSTANCE;

    private final DataSourceProperties dataSourceProperties;

    private org.apache.tomcat.jdbc.pool.DataSource dataSource;

    public DataSourceProvider(DataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = Objects.requireNonNull(dataSourceProperties);
    }

    public synchronized void closeAllConnections() {
        dataSource.close(true);
        populatePoolConfiguration(dataSource.getPoolProperties());
    }

    public synchronized DataSource getDataSource() {
        if (dataSource == null) {
            dataSource = createDataSource();
        }
        return dataSource;
    }

    private org.apache.tomcat.jdbc.pool.DataSource createDataSource() {
        PoolProperties p = new PoolProperties();
        populatePoolConfiguration(p);
        return new org.apache.tomcat.jdbc.pool.DataSource(p);
    }

    private void populatePoolConfiguration(PoolConfiguration p) {
        p.setUrl(dataSourceProperties.url().get());
        p.setUsername(dataSourceProperties.user().get());
        p.setPassword(dataSourceProperties.password().get());
        p.setDriverClassName(dataSourceProperties.driverClassName().get());
        p.setValidationQuery(dataSourceProperties.validationQuery().get());
        p.setValidationQueryTimeout(dataSourceProperties.validationQueryTimeout().get());
        p.setValidationInterval(dataSourceProperties.validationInterval().get());
        p.setInitialSize(dataSourceProperties.initialSize().get());
        p.setMaxActive(dataSourceProperties.maxActive().get());
        p.setMaxIdle(dataSourceProperties.maxIdle().get());
        p.setMinIdle(dataSourceProperties.minIdle().get());
        p.setTestOnBorrow(dataSourceProperties.testOnBorrow().get());
        p.setTestOnConnect(dataSourceProperties.testOnConnect().get());
        p.setTestOnReturn(dataSourceProperties.testOnReturn().get());
        p.setTestWhileIdle(dataSourceProperties.testWhileIdle().get());
        p.setTimeBetweenEvictionRunsMillis(dataSourceProperties.timeBetweenEvictionRunsMillis().get());
        p.setMinEvictableIdleTimeMillis(dataSourceProperties.minEvictableIdleTimeMillis().get());
    }

    public static synchronized DataSourceProvider getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataSourceProvider(DataSourceProperties.getInstance());
        }
        return INSTANCE;
    }
}
