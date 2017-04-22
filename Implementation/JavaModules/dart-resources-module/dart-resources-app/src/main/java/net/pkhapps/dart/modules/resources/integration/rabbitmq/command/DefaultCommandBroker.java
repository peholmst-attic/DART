package net.pkhapps.dart.modules.resources.integration.rabbitmq.command;

import net.pkhapps.dart.modules.resources.integration.rabbitmq.AccessDeniedException;
import net.pkhapps.dart.modules.resources.integration.rabbitmq.CommandBroker;
import net.pkhapps.dart.modules.resources.integration.rabbitmq.HandlerException;
import net.pkhapps.dart.modules.resources.integration.rabbitmq.HandlerNotFoundException;
import net.pkhapps.dart.modules.resources.integration.xsd.Message;
import net.pkhapps.dart.modules.resources.integration.xsd.SetResourceLocationCommand;
import net.pkhapps.dart.modules.resources.integration.xsd.SetResourceStatusCommand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Default implementation of {@link CommandBroker}. To keep things simple, the message is extracted and directed to the
 * correct handler manually (i.e. no fancy auto-detection of handlers).
 */
@ApplicationScoped
class DefaultCommandBroker implements CommandBroker {

    @Inject
    SetResourceLocationCommandHandler setResourceLocationCommandHandler;

    @Inject
    SetResourceStatusCommandHandler setResourceStatusCommandHandler;

    @Override
    public void handleCommand(@NotNull Message message, @Nullable String userId)
            throws HandlerNotFoundException, HandlerException, AccessDeniedException {

        SetResourceLocationCommand setResourceLocationCommand = message.getSetResourceLocationCommand();
        SetResourceStatusCommand setResourceStatusCommand = message.getSetResourceStatusCommand();

        if (setResourceLocationCommand != null) {
            handle(setResourceLocationCommandHandler, setResourceLocationCommand, userId);
        } else if (setResourceStatusCommand != null) {
            handle(setResourceStatusCommandHandler, setResourceStatusCommand, userId);
        }
    }

    private static <COMMAND> void handle(@NotNull CommandHandler<COMMAND> handler, @NotNull COMMAND command,
                                         @Nullable String userId) throws AccessDeniedException, HandlerException {
        try {
            handler.handleCommand(command, userId);
        } catch (RuntimeException ex) {
            // TODO Support for custom error codes through an annotation on the exception class
            throw new HandlerException(ex.getMessage(), ex);
        }
    }
}
