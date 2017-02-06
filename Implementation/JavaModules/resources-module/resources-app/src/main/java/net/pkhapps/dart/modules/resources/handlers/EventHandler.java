package net.pkhapps.dart.modules.resources.handlers;

/**
 * Created by petterprivate on 03/02/2017.
 */
public interface EventHandler<EVENT> {

    void handle(EVENT event) throws Exception;
}
