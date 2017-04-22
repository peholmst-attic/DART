package net.pkhapps.dart.modules.resources.integration.rabbitmq.command;

import net.pkhapps.dart.modules.resources.integration.rabbitmq.AccessDeniedException;
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
class SetResourceStatusCommandHandler implements CommandHandler<SetResourceStatusCommand> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetResourceStatusCommandHandler.class);

    @Override
    public void handleCommand(@NotNull SetResourceStatusCommand setResourceStatusCommand,
                              @Nullable String userId) throws AccessDeniedException {
        LOGGER.info("Received command {}", setResourceStatusCommand);
    }
}
