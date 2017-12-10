package net.pkhapps.dart.modules.base.messaging.command;

import io.reactivex.Completable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for a handler that knows how to handle (execute) a specific {@link Command}. Used in conjunction with
 * {@link HandlerFor}.
 */
public interface CommandHandler<C extends Command> {

    /**
     * Executes the given command.
     *
     * @param command the command to execute.
     * @param userId  the ID of the user executing the command.
     * @return a {@link Completable} that will get informed when the command is complete or an error occurs.
     */
    @NotNull
    Completable handleCommand(@NotNull C command, @Nullable String userId);
}
