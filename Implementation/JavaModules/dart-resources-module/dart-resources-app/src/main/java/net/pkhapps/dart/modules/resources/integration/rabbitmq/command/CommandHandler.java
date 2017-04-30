package net.pkhapps.dart.modules.resources.integration.rabbitmq.command;

import net.pkhapps.dart.modules.resources.integration.rabbitmq.AccessDeniedException;
import net.pkhapps.dart.modules.resources.integration.xsd.Command;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * TODO Document me!
 */
interface CommandHandler {

    /**
     * @param command
     * @param userId
     * @throws AccessDeniedException
     */
    void handleCommand(@NotNull Command command, @Nullable String userId) throws AccessDeniedException;
}
