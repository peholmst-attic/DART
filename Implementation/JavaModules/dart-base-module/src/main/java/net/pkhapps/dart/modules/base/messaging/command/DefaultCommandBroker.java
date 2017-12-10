package net.pkhapps.dart.modules.base.messaging.command;

import io.reactivex.Completable;
import net.pkhapps.dart.modules.base.messaging.HandlerNotFoundException;
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
    Instance<CommandHandler<?>> commandHandlers;

    @Override
    @NotNull
    @SuppressWarnings("unchecked")
    public <C extends Command> Completable handleCommand(@NotNull C command, @Nullable String userId)
            throws HandlerNotFoundException {
        Objects.requireNonNull(command, "command must not be null");
        CommandHandler commandHandler;
        try {
            commandHandler = commandHandlers.select(new HandlerForQualifier(command.getClass())).get();
        } catch (RuntimeException ex) {
            throw new HandlerNotFoundException();
        }
        return commandHandler.handleCommand(command, userId);
    }
}
