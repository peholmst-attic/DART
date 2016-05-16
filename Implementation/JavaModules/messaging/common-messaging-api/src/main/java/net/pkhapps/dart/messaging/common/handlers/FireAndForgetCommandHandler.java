package net.pkhapps.dart.messaging.common.handlers;

import net.pkhapps.dart.messaging.common.messages.FireAndForgetCommand;

public interface FireAndForgetCommandHandler<C extends FireAndForgetCommand> extends CommandHandler<C> {

    void handleCommand(C command);
}
