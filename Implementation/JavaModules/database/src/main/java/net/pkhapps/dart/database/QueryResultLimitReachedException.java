package net.pkhapps.dart.database;

import java.util.Objects;

public class QueryResultLimitReachedException extends RuntimeException {

    private final String queryName;
    private final int limit;

    public QueryResultLimitReachedException(String queryName, int limit) {
        super("The query " + queryName + " reached its result limit of " + limit);
        this.queryName = Objects.requireNonNull(queryName);
        this.limit = limit;
    }

    public String getQueryName() {
        return queryName;
    }

    public int getLimit() {
        return limit;
    }
}
