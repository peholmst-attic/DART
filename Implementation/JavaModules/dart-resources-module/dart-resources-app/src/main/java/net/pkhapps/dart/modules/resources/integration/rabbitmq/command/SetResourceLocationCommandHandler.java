package net.pkhapps.dart.modules.resources.integration.rabbitmq.command;

import net.pkhapps.dart.modules.resources.integration.rabbitmq.AccessDeniedException;
import net.pkhapps.dart.modules.resources.integration.xsd.Command;
import net.pkhapps.dart.modules.resources.integration.xsd.SetResourceStatusCommand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

/**
 * TODO Document me!
 */
@ApplicationScoped
@HandlerFor(SetResourceStatusCommand.class)
class SetResourceLocationCommandHandler implements CommandHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetResourceLocationCommandHandler.class);

    @Override
    public void handleCommand(@NotNull Command command, @Nullable String userId) throws AccessDeniedException {
        LOGGER.info("Received command {}", command);
    }
}
