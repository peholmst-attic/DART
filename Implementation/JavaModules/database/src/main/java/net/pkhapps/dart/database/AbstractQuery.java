package net.pkhapps.dart.database;

import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectLimitStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public abstract class AbstractQuery {

    private final DataSourceProperties dataSourceProperties;
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected AbstractQuery(DataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    protected <R extends Record> Result<R> limitQuery(SelectLimitStep<R> query, String queryName) {
        int limit = dataSourceProperties.queryLimit(Objects.requireNonNull(queryName)).get();
        Result<R> result = query.limit(limit).fetch();
        if (result.size() == limit) {
            logger.warn("The query [{}] has reached its result limit of {}", queryName, limit);
            if (!dataSourceProperties.onlyWarnWhenQueryLimitReached().get()) {
                throw new QueryResultLimitReachedException(queryName, limit);
            }
        }
        return result;
    }
}
