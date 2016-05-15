package net.pkhapps.dart.messaging.common.handlers;

import net.pkhapps.dart.messaging.common.messages.Command;

public interface CommandHandler<C extends Command> extends MessageHandler<C> {
}
