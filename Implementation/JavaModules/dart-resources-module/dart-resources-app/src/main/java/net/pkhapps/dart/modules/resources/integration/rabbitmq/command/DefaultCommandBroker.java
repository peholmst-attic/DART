package net.pkhapps.dart.modules.resources.integration.rabbitmq.command;

import net.pkhapps.dart.modules.resources.integration.rabbitmq.AccessDeniedException;
import net.pkhapps.dart.modules.resources.integration.rabbitmq.CommandBroker;
import net.pkhapps.dart.modules.resources.integration.rabbitmq.HandlerException;
import net.pkhapps.dart.modules.resources.integration.rabbitmq.HandlerNotFoundException;
import net.pkhapps.dart.modules.resources.integration.xsd.Command;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.Objects;

/**
 * Default implementation of {@link CommandBroker}. The {@link HandlerFor} qualifier is used to select which command
 * handler to invoke.
 */
@ApplicationScoped
class DefaultCommandBroker implements CommandBroker {

    @Inject
    @Any
    Instance<CommandHandler> commandHandlers;

    @Override
    public void handleCommand(@NotNull Command command, @Nullable String userId)
            throws HandlerNotFoundException, HandlerException, AccessDeniedException {
        Objects.requireNonNull(command, "command must not be null");
        try {
            CommandHandler commandHandler = commandHandlers.select(new HandlerForQualifier(command.getClass())).get();
            try {
                commandHandler.handleCommand(command, userId);
            } catch (RuntimeException ex) {
                throw new HandlerException(ex.getMessage(), ex);
            }
        } catch (RuntimeException ex) {
            throw new HandlerNotFoundException();
        }
    }
}
