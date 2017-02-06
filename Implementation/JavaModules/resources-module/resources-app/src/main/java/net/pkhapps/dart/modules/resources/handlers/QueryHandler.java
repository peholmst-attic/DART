package net.pkhapps.dart.modules.resources.handlers;

/**
 * Created by petterprivate on 03/02/2017.
 */
public interface QueryHandler<QUERY, RESULT> {

    RESULT handle(QUERY query) throws Exception;
}
