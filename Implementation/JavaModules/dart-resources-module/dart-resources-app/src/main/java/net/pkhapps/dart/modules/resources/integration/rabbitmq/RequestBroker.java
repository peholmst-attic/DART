package net.pkhapps.dart.modules.resources.integration.rabbitmq;

import net.pkhapps.dart.modules.resources.integration.xsd.Request;
import net.pkhapps.dart.modules.resources.integration.xsd.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * TODO Document me!
 */
public interface RequestBroker {

    /**
     * @param request
     * @param userId
     * @return
     * @throws HandlerNotFoundException
     * @throws HandlerException
     * @throws AccessDeniedException
     */
    @NotNull
    Response handleRequest(@NotNull Request request, @Nullable String userId)
            throws HandlerNotFoundException, HandlerException, AccessDeniedException;

}
