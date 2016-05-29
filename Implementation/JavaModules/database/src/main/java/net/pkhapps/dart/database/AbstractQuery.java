package net.pkhapps.dart.database;

import org.jooq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * A base class for query objects that perform JOOQ queries. For increased stability, queries should also always be
 * limited, meaning that they can return no records, exactly one record or a finite maximum number of records.
 *
 * @see #limitQuery(SelectLimitStep, String)
 * @see #limitQuery(SelectLimitStep, Function, String)
 */
public abstract class AbstractQuery {

    private final DataSourceProperties dataSourceProperties;

    /**
     * Protected logger that can be used by subclasses as well.
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Constructor
     *
     * @param dataSourceProperties the data source properties to use (not {@code null}).
     */
    protected AbstractQuery(DataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = Objects.requireNonNull(dataSourceProperties);
    }

    /**
     * Executes the specified {@code query}, limiting the number of results to the limit of
     * the specified {@code queryName}. The idea is to prevent queries from accidentally returning
     * too many results and eating up all the memory, for example if the database contains more records than normally expected.
     *
     * @param query     the JOOQ query to execute (not {@code null}).
     * @param queryName the name of the query to use when looking up the query result limit (not {@code null}).
     * @return the query result.
     * @throws QueryResultLimitReachedException if the number of records in the result is equal to the limit
     *                                          and {@link DataSourceProperties#onlyWarnWhenQueryLimitReached()} is false.
     * @see DataSourceProperties#queryLimit(String)
     */
    protected <R extends Record> Result<R> limitQuery(SelectLimitStep<R> query, String queryName) {
        return limitQuery(query, ResultQuery::fetch, queryName);
    }

    /**
     * Executes the specified {@code query}, limiting the number of results to the limit of
     * the specified {@code queryName}. The idea is to prevent queries from accidentally returning
     * too many results and eating up all the memory, for example if the database contains more records than normally expected.
     *
     * @param query           the JOOQ query to execute (not {@code null}).
     * @param resultExtractor a function that will execute the limited query and return the result.
     * @param queryName       the name of the query to use when looking up the query result limit (not {@code null}).
     * @return the query result.
     * @throws QueryResultLimitReachedException if the number of records in the result is equal to the limit
     *                                          and {@link DataSourceProperties#onlyWarnWhenQueryLimitReached()} is false.
     * @see DataSourceProperties#queryLimit(String)
     */
    protected <R extends Record, T, L extends List<T>> L limitQuery(SelectLimitStep<R> query,
                                                                    Function<SelectOffsetStep<R>, L> resultExtractor,
                                                                    String queryName) {
        Objects.requireNonNull(query);
        Objects.requireNonNull(resultExtractor);
        Objects.requireNonNull(queryName);
        final int limit = dataSourceProperties.queryLimit(queryName).get();
        final L result = resultExtractor.apply(query.limit(limit));
        logger.trace("Query [{}] returned [{}] record(s), limit is {}", queryName, result.size(), limit);
        if (result.size() == limit) {
            logger.warn("The query [{}] has reached its result limit of {}", queryName, limit);
            if (!dataSourceProperties.onlyWarnWhenQueryLimitReached().get()) {
                throw new QueryResultLimitReachedException(queryName, limit);
            }
        }
        return result;
    }
}
