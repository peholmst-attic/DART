package net.pkhapps.dart.modules.base.messaging.command;

import io.reactivex.Completable;
import net.pkhapps.dart.modules.base.messaging.HandlerNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A command broker is responsible for looking up a suitable {@link CommandHandler} for a given {@link Command} and
 * invoking it.
 */
public interface CommandBroker {

    /**
     * Attempts to handle (execute) the given command using a suitable {@link CommandHandler}.
     *
     * @param command the command to execute.
     * @param userId  the ID of the user executing the command.
     * @return a {@link Completable} that will get informed when the command is complete or an error occurs.
     * @throws HandlerNotFoundException if no suitable command handler is found.
     */
    @NotNull <C extends Command> Completable handleCommand(@NotNull C command, @Nullable String userId)
            throws HandlerNotFoundException;
}
