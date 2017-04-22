package net.pkhapps.dart.modules.resources.integration.rabbitmq.command;

import net.pkhapps.dart.modules.resources.integration.rabbitmq.AccessDeniedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * TODO Document me!
 */
interface CommandHandler<COMMAND> {

    void handleCommand(@NotNull COMMAND command, @Nullable String userId) throws AccessDeniedException;
}
