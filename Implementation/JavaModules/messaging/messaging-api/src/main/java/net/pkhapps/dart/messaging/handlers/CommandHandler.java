package net.pkhapps.dart.messaging.handlers;

import net.pkhapps.dart.messaging.messages.Command;

public interface CommandHandler<C extends Command> extends MessageHandler<C> {
}
