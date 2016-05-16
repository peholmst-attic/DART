package net.pkhapps.dart.messaging.handlers;

import net.pkhapps.dart.messaging.messages.FireAndForgetCommand;

public interface FireAndForgetCommandHandler<C extends FireAndForgetCommand> extends CommandHandler<C> {

    void handleCommand(C command);
}
