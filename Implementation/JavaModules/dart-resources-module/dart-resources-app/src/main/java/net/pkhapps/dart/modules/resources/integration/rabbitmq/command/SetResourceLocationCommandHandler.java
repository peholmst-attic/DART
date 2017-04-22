package net.pkhapps.dart.modules.resources.integration.rabbitmq.command;

import net.pkhapps.dart.modules.resources.integration.rabbitmq.AccessDeniedException;
import net.pkhapps.dart.modules.resources.integration.xsd.SetResourceLocationCommand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

/**
 * Created by peholmst on 22/04/2017.
 */
@ApplicationScoped
class SetResourceLocationCommandHandler implements CommandHandler<SetResourceLocationCommand> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetResourceStatusCommandHandler.class);

    @Override
    public void handleCommand(@NotNull SetResourceLocationCommand setResourceLocationCommand,
                              @Nullable String userId) throws AccessDeniedException {
        LOGGER.info("Received command {}", setResourceLocationCommand);
    }
}
