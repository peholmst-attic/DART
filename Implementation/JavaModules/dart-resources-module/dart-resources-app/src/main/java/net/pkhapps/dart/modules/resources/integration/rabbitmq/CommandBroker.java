package net.pkhapps.dart.modules.resources.integration.rabbitmq;

import net.pkhapps.dart.modules.resources.integration.xsd.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * TODO Document me!
 */
public interface CommandBroker {

    /**
     * @param message
     * @param userId
     * @throws HandlerNotFoundException
     * @throws HandlerException
     * @throws AccessDeniedException
     */
    void handleCommand(@NotNull Message message, @Nullable String userId)
            throws HandlerNotFoundException, HandlerException, AccessDeniedException;
}