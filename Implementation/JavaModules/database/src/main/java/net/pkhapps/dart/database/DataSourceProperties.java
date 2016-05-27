package net.pkhapps.dart.database;

import com.netflix.config.*;
import org.jooq.SQLDialect;

import java.util.Objects;

public class DataSourceProperties {

    private static DataSourceProperties INSTANCE;

    private final DynamicPropertyFactory dynamicPropertyFactory;

    public DataSourceProperties(DynamicPropertyFactory dynamicPropertyFactory) {
        this.dynamicPropertyFactory = Objects.requireNonNull(dynamicPropertyFactory, "dynamicPropertyFactory must not be null");
    }

    public DynamicStringProperty url() {
        return dynamicPropertyFactory.getStringProperty("jdbc.url", "jdbc:postgresql:dart");
    }

    public DynamicStringProperty user() {
        return dynamicPropertyFactory.getStringProperty("jdbc.user", "dart");
    }

    public DynamicStringProperty password() {
        return dynamicPropertyFactory.getStringProperty("jdbc.password", "dart");
    }

    public DynamicStringProperty driverClassName() {
        return dynamicPropertyFactory.getStringProperty("jdbc.driver-class-name", "org.postgresql.Driver");
    }

    public DynamicStringProperty validationQuery() {
        return dynamicPropertyFactory.getStringProperty("jdbc.validation.query", "SELECT 1");
    }

    public DynamicIntProperty validationQueryTimeout() {
        return dynamicPropertyFactory.getIntProperty("jdbc.validation.query.timeout-sec", 5);
    }

    public DynamicIntProperty validationInterval() {
        return dynamicPropertyFactory.getIntProperty("jdbc.validation.interval-millis", 30000);
    }

    public DynamicIntProperty initialSize() {
        return dynamicPropertyFactory.getIntProperty("jdbc.pool.initial-size", 10);
    }

    public DynamicIntProperty maxActive() {
        return dynamicPropertyFactory.getIntProperty("jdbc.pool.max-active", 100);
    }

    public DynamicIntProperty maxIdle() {
        return dynamicPropertyFactory.getIntProperty("jdbc.pool.max-idle", maxActive().get());
    }

    public DynamicIntProperty minIdle() {
        return dynamicPropertyFactory.getIntProperty("jdbc.pool.min-idle", initialSize().get());
    }

    public DynamicBooleanProperty testOnBorrow() {
        return dynamicPropertyFactory.getBooleanProperty("jdbc.test.on-borrow", true);
    }

    public DynamicBooleanProperty testOnConnect() {
        return dynamicPropertyFactory.getBooleanProperty("jdbc.test.on-connect", false);
    }

    public DynamicBooleanProperty testOnReturn() {
        return dynamicPropertyFactory.getBooleanProperty("jdbc.test.on-return", false);
    }

    public DynamicBooleanProperty testWhileIdle() {
        return dynamicPropertyFactory.getBooleanProperty("jdbc.test.while-idle", false);
    }

    public DynamicIntProperty timeBetweenEvictionRunsMillis() {
        return dynamicPropertyFactory.getIntProperty("jdbc.eviction.time-between-runs-millis", 5000);
    }

    public DynamicIntProperty minEvictableIdleTimeMillis() {
        return dynamicPropertyFactory.getIntProperty("jdbc.eviction.min-idle-time-millis", 60000);
    }

    public DynamicStringProperty sqlDialect() {
        return dynamicPropertyFactory.getStringProperty("jdbc.jooq.sql-dialect", SQLDialect.POSTGRES_9_4.name());
    }

    public DynamicIntProperty defaultQueryLimit() {
        return dynamicPropertyFactory.getIntProperty("jdbc.query.limit.default", 500);
    }

    public DynamicIntProperty queryLimit(String queryName) {
        return dynamicPropertyFactory.getIntProperty("jdbc.query.limit." + queryName, defaultQueryLimit().get());
    }

    public DynamicBooleanProperty onlyWarnWhenQueryLimitReached() {
        return dynamicPropertyFactory.getBooleanProperty("jdbc.query.limit.only-warn-when-reached", false);
    }

    public static synchronized DataSourceProperties getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataSourceProperties(DynamicPropertyFactory.getInstance());
        }
        return INSTANCE;
    }
}
