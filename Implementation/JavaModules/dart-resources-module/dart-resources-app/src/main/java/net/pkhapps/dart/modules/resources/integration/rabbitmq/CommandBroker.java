package net.pkhapps.dart.modules.resources.integration.rabbitmq;

import net.pkhapps.dart.modules.resources.integration.xsd.Command;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * TODO Document me!
 */
public interface CommandBroker {

    /**
     * @param command
     * @param userId
     * @throws HandlerNotFoundException
     * @throws HandlerException
     * @throws AccessDeniedException
     */
    void handleCommand(@NotNull Command command, @Nullable String userId)
            throws HandlerNotFoundException, HandlerException, AccessDeniedException;
}
